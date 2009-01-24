/**
 * 
 */
package org.ccci.gcx.authorization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
public class Pdp {
	private DefaultHttpClient client;
	private String gcxServerRoot;
	private DocumentBuilder XMLParser;
	private XPath xpathEngine;

	/**
	 * 
	 */
	public Pdp() {
		this("https://www.mygcx.org");
	}

	/**
	 * @param gcxServerRoot
	 */
	public Pdp(String gcxServerRoot) {
		super();
		this.client = new DefaultHttpClient();
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

		this.setGcxServerRoot(gcxServerRoot);
	}

	/**
	 * @param entity the entity that authorization is being checked for
	 * @param targets a list of targets to check authorization for 
	 * @return List<Boolean> a list of Boolean objects in the same order as the provided list of targets, method uses ArrayList to create List being returned
	 */
	public List<Boolean> check(String entity, List<String> targets) {
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
		HashMap<String,Boolean> responses = new HashMap<String,Boolean>((int)(targets.size()/0.75) + 1, (float)0.75);

		//generate the parameters for the request 
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
					NodeList XMLtargets = (NodeList) this.xpathEngine.evaluate("target", entities.item(x), XPathConstants.NODESET);

					//iterate over all target nodes
					for(int y = 0; y < XMLtargets.getLength(); y++) {
						//extract the name and whether the entity has access to the target
						String name = this.xpathEngine.evaluate("@name", XMLtargets.item(y)).toLowerCase();
						Boolean isAuthorized = (Boolean) this.xpathEngine.evaluate(". = 'yes'", XMLtargets.item(y), XPathConstants.BOOLEAN); 

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
	public boolean check(String entity, String target) {
		//create list to hold the 1 target being checked
		ArrayList<String> targets = new ArrayList<String>(1);
		targets.add(target);

		//process check and return results
		return check(entity, targets).get(0).booleanValue();
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
		//trim trailing / if it exists
		if(gcxServerRoot.endsWith("/")) {
			gcxServerRoot = gcxServerRoot.substring(0, gcxServerRoot.length()-1);
		}

		this.gcxServerRoot = gcxServerRoot;
	}
}
