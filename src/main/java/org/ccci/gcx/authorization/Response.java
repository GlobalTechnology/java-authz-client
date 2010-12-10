package org.ccci.gcx.authorization;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

import org.ccci.gcx.authorization.exception.InvalidXmlException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Response {
    private final Command command;
    private final Integer code;
    private final ArrayList<Object> objects;

    public Response(final Command command, final Element commandXml, final XPath xpathEngine) throws InvalidXmlException {
	// throw an error if the id of cmdXml doesn't match the id of this
	// command
	if (!command.getId().equals(commandXml.getAttributeNS(null, "id"))) {
	    throw new InvalidXmlException("response command id doesn't match");
	}
	this.command = command;

	try {
	    // extract the response node from the command
	    final Element responseXml = (Element) xpathEngine.evaluate(
		    "authz:response", commandXml, XPathConstants.NODE);

	    // extract the response code
	    this.code = Integer.parseInt(responseXml.getAttributeNS(null, "code"));

	    // extract any objects attached to the response
	    final NodeList objectsNL = (NodeList) xpathEngine.evaluate(
		    "authz:entity | authz:user | authz:group | authz:target | authz:resource | authz:role | authz:namespace | authz:key",
		    responseXml, XPathConstants.NODESET);
	    this.objects = new ArrayList<Object>(objectsNL.getLength());

	    // iterate over all the found objects
	    for (int x = 0; x < objectsNL.getLength(); x++) {
		final Element object = (Element) objectsNL.item(x);

		// switch on node type
		// final String type = object.getLocalName();
		// TODO
	    }
	} catch(final Exception e) {
	    throw new InvalidXmlException(e);
	}
    }

    public Response(final Command command, final Integer code) {
	this(command, code, null);
    }

    public Response(final Command command, final Integer code,
	    final List<Object> objects) {
	this.command = command;
	this.code = code;
	if (objects != null) {
	    this.objects = new ArrayList<Object>(objects);
	} else {
	    this.objects = new ArrayList<Object>(0);
	}
    }

    /**
     * @return the code
     */
    public Integer getCode() {
	return this.code;
    }

    public Command getCommand() {
	return this.command;
    }

    /**
     * @return the objects
     */
    public ArrayList<Object> getObjects() {
	return new ArrayList<Object>(this.objects);
    }
}
