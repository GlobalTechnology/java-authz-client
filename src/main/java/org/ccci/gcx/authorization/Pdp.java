/**
 *
 */
package org.ccci.gcx.authorization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author Daniel Frett
 *
 */
public class Pdp {
	private String gcxServerRoot;
	private HttpClient httpClient;
	private DocumentBuilder xmlParser;
	private XPath xPathEngine;

	/**
	 *
	 */
	public Pdp() {
		//default to www.mygcx.org for the authorization server
		this("https://www.mygcx.org");
	}

	/**
	 * @param gcxServerRoot
	 */
	public Pdp(final String gcxServerRoot) {
		super();

		//set the root server
		//TODO: this should be a final string set here only
		this.setGcxServerRoot(gcxServerRoot);
	}

	/**
	 * @param entity the entity that authorization is being checked for
	 * @param targets a list of targets to check authorization for
	 * @return List<Boolean> a list of Boolean objects in the same order as the provided list of targets, method uses ArrayList to create List being returned
	 */
	public List<Boolean> check(final String entity, final List<String> targets) {
		//skip processing if there are no targets to test
		if(targets.isEmpty()) {
			return new ArrayList<Boolean>();
		}

		//generate the request xml
		String requestXML = "<?xml version='1.0' encoding='UTF-8'?><auth_question><entity name='" + entity + "'>";
		//iterate over targets list appending each String in the list as a target
		Iterator<String> i = targets.iterator();
		while(i.hasNext()) {
			requestXML += "<target name='" + i.next() + "'/>";
		}
		//close request xml
		requestXML += "</entity></auth_question>";

		//create a key-value table to store responses
		Map<String,Boolean> responses = new HashMap<String,Boolean>((int)(targets.size()/0.75) + 1, (float)0.75);

		//generate the parameters for the request
		List <NameValuePair> params = new ArrayList <NameValuePair>();
		params.add(new BasicNameValuePair("auth_question", requestXML));

		//try executing the request and parsing the response
		try {
			//generate POST request
			HttpPost request = new HttpPost(this.gcxServerRoot + "/system/authz");
			request.setEntity(new UrlEncodedFormEntity(params));

			//execute the request
			HttpResponse response = this.getHttpClient().execute(request);

			//parse the XML response to a Document object
			Document responseDOM = this.getXmlParser().parse(response.getEntity().getContent());

			//locate all entity nodes
			NodeList entities = (NodeList) this.getXPathEngine().evaluate("/auth_answer/entity", responseDOM.getDocumentElement(), XPathConstants.NODESET);

			//iterate over all found entity nodes
			for(int x = 0; x < entities.getLength(); x++) {
				//find the correct entity node
				if(this.getXPathEngine().evaluate("@name", entities.item(x)).equalsIgnoreCase(entity)) {
					//locate all target nodes
					NodeList XMLtargets = (NodeList) this.getXPathEngine().evaluate("target", entities.item(x), XPathConstants.NODESET);

					//iterate over all target nodes
					for(int y = 0; y < XMLtargets.getLength(); y++) {
						//extract the name and whether the entity has access to the target
						String name = this.getXPathEngine().evaluate("@name", XMLtargets.item(y)).toLowerCase();
						Boolean isAuthorized = (Boolean) this.getXPathEngine().evaluate(". = 'yes'", XMLtargets.item(y), XPathConstants.BOOLEAN);

						//store the response in the hashmap
						responses.put(name, isAuthorized);
					}
				}
			}
		}
		//we had an error while trying to execute the request, assume not authorized
		catch(Exception e) {
			//output error encountered to the standard error stream before generating response
			e.printStackTrace(System.err);
		}

		//generate response list
		ArrayList<Boolean> response = new ArrayList<Boolean>(targets.size());
		i = targets.iterator();
		while(i.hasNext()) {
			String target = i.next();
			Boolean isAuthorized = responses.get(target.toLowerCase());

			//default to null if authorization response wasn't found
			isAuthorized = (isAuthorized == null) ? Boolean.FALSE : isAuthorized;

			//add authorization response to response list
			response.add(isAuthorized);
		}

		//return the list of Boolean's
		return response;
	}

	/**
	 * @param entity authorization entity to check authorization for
	 * @param target authorization target to check authorization for
	 * @return boolean indicating whether entity has access to target, defaults to false if an error is encountered
	 */
	public boolean check(final String entity, final String target) {
		//create list to hold the 1 target being checked
		ArrayList<String> targets = new ArrayList<String>(1);
		targets.add(target);

		//process check and return results
		return check(entity, targets).get(0).booleanValue();
	}

	public boolean confluenceCheck(final String entity, final String spaceKey, final String permissionType) {
		//generate the authorization check params
		List <NameValuePair> params = new ArrayList <NameValuePair>();
		params.add(new BasicNameValuePair("entity", entity));
		params.add(new BasicNameValuePair("spaceKey", spaceKey));
		params.add(new BasicNameValuePair("permissionType", permissionType));

		//try executing the request and parsing the response
		try {
			//generate POST request
			HttpPost request = new HttpPost(this.gcxServerRoot + "/system/authz-confluence");
			request.setEntity(new UrlEncodedFormEntity(params));

			//execute the request
			String response = this.getHttpClient().execute(request, new BasicResponseHandler());

			//test response to see if it was yes or no
			return response.equalsIgnoreCase("yes");
		}
		//an error was encountered, so assume not authorized
		catch(Exception e) {
			//output error encountered to the standard error stream before returning not authorized
			e.printStackTrace(System.err);
			return false;
		}
	}

	/**
	 * @return the gcxServerRoot
	 */
	public String getGcxServerRoot() {
		return this.gcxServerRoot;
	}

	/**
	 * @return the client
	 */
	public HttpClient getHttpClient() {
		//Create HTTP client if client doesn't exist yet
		if(this.httpClient == null) {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			HttpProtocolParams.setUseExpectContinue(params, true);
			ConnManagerParams.setMaxTotalConnections(params, 100);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
			ClientConnectionManager cm = new ThreadSafeClientConnManager(params, registry);
			this.httpClient = new DefaultHttpClient(cm, params);
		}

		return this.httpClient;
	}

	/**
	 * @return the xMLParser
	 */
	public DocumentBuilder getXmlParser() throws ParserConfigurationException {
		//create the XML parser
		if(this.xmlParser == null) {
			DocumentBuilderFactory XMLParserFactory = DocumentBuilderFactory.newInstance();
			XMLParserFactory.setCoalescing(true);
			this.xmlParser = XMLParserFactory.newDocumentBuilder();
		}

		return this.xmlParser;
	}

	/**
	 * @return the xpathEngine
	 */
	public XPath getXPathEngine() {
		//create the XPath engine for use with the XML parser if one doesn't exist yet
		if(this.xPathEngine == null) {
			XPathFactory xpathFactory = XPathFactory.newInstance();
			this.xPathEngine = xpathFactory.newXPath();
		}

		return this.xPathEngine;
	}

	public void setGcxServerRoot(String gcxServerRoot) {
		//trim trailing / if it exists
		if(gcxServerRoot.endsWith("/")) {
			gcxServerRoot = gcxServerRoot.substring(0, gcxServerRoot.length()-1);
		}

		this.gcxServerRoot = gcxServerRoot;
	}

	/**
	 * @param client the client to set
	 */
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	/**
	 * @param parser the XmlParser to set
	 */
	public void setXmlParser(DocumentBuilder xmlParser) {
		this.xmlParser = xmlParser;
	}

	/**
	 * @param xpathEngine the xpathEngine to set
	 */
	public void setXPathEngine(XPath xPathEngine) {
		this.xPathEngine = xPathEngine;
	}
}
