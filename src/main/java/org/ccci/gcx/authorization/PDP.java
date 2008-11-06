/**
 * 
 */
package org.ccci.gcx.authorization;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author Daniel Frett
 *
 */
public class PDP {

	private DefaultHttpClient client;
	private DocumentBuilder XMLParser;
	private XPath xpathEngine;
	private String gcxServerRoot;

	/**
	 * 
	 */
	public PDP() {
		this.client = new DefaultHttpClient();
		this.gcxServerRoot = "https://www.mygcx.org";
		try {
			//create the XML parser
			DocumentBuilderFactory XMLParserFactory = DocumentBuilderFactory.newInstance();
			XMLParserFactory.setCoalescing(true);
			this.XMLParser = XMLParserFactory.newDocumentBuilder();

			//create the XPath engine for use with the XML parser
			XPathFactory xpathFactory = XPathFactory.newInstance();
			this.xpathEngine = xpathFactory.newXPath();
		}
		catch(Exception e) {
		}
	}

	/**
	 * @param entity authorization entity to check authorization for
	 * @param target authorization target to check authorization for
	 * @return boolean indicating whether entity has access to target, defaults to false if an error is encountered
	 */
	public boolean check(String entity, String target) {
		//generate the authorization check params
		String requestXML = "<?xml version='1.0' encoding='UTF-8'?><auth_question><entity name='" + entity + "'><target name='" + target + "'/></entity></auth_question>";
		List <NameValuePair> params = new ArrayList <NameValuePair>();
        params.add(new BasicNameValuePair("auth_question", requestXML));

		//try executing the request and parsing the response
		try {
			//generate POST request
			HttpPost request = new HttpPost(this.gcxServerRoot + "/system/authz");
	        request.setEntity(new UrlEncodedFormEntity(params));
			
			//execute the request
			HttpResponse response = this.client.execute(request);

			//parse the XML response to a Document object
			Document responseDOM = this.XMLParser.parse(response.getEntity().getContent());

			//locate all entity nodes
			NodeList entities = (NodeList) this.xpathEngine.evaluate("/auth_answer/entity", responseDOM.getDocumentElement(), XPathConstants.NODESET);

			//iterate over all found entity nodes
			for(int x = 0; x < entities.getLength(); x++) {

				//find the correct entity node
				if(this.xpathEngine.evaluate("@name", entities.item(x)).equalsIgnoreCase(entity)) {
					//locate all target nodes
					NodeList targets = (NodeList) this.xpathEngine.evaluate("target", entities.item(x), XPathConstants.NODESET);
					//iterate over all target nodes
					for(int y = 0; y < targets.getLength(); y++) {
						//is this the requested target?
						if(this.xpathEngine.evaluate("@name", targets.item(y)).equalsIgnoreCase(target)) {
							//test if the entity is authorized for this target
							return (Boolean) this.xpathEngine.evaluate(". = 'yes'", targets.item(y), XPathConstants.BOOLEAN); 
						}
					}
				}
			}
		}
		//we had an error while trying to execute the request, assume not authorized
		catch(Exception e) {
			//output error encountered to the standard error stream before returning not authorized
			e.printStackTrace(System.err);
			return false;
		}
		// default to false
		return false;
	}

	public boolean confluenceCheck(String entity, String spaceKey, String permissionType) {
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
			String response = this.client.execute(request, new BasicResponseHandler());
			
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

	public void setGcxServerRoot(String gcxServerRoot) {
		this.gcxServerRoot = gcxServerRoot;
	}
}
