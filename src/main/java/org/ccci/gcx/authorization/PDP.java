/**
 * 
 */
package org.ccci.gcx.authorization;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;

/**
 * @author frett
 *
 */
public class PDP {

	private HttpClient client;
	private DocumentBuilder XMLParser;
	private XPath xpathEngine;

	/**
	 * 
	 */
	public PDP() {
		this.client = new HttpClient();
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

		//generate POST request
		String requestXML = "<?xml version='1.0' encoding='UTF-8'?><auth_question><entity name='" + entity + "'><target name='" + target + "'/></entity></auth_question>";
		PostMethod request = new PostMethod("http://dev.mygcx.org/authz");
		request.addParameter("auth_question", requestXML);

		//try executing the request and parsing the response
		try {
			//execute the request
			this.client.executeMethod(request);

			//parse the XML response to a Document object
			Document responseDOM = this.XMLParser.parse(request.getResponseBodyAsStream());

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
		catch (Exception e) {
			return false;
		}
		// default to false
		return false;
	}
}
