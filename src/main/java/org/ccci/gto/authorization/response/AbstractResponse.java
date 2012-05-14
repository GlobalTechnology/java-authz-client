package org.ccci.gto.authorization.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

import org.ccci.gto.authorization.AuthzObject;
import org.ccci.gto.authorization.Command;
import org.ccci.gto.authorization.Response;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class AbstractResponse<T extends Command> implements Response<T> {
    private final T command;
    private final Integer code;
    private final ArrayList<AuthzObject> objects;

    public AbstractResponse(final T command, final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException {
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
            this.objects = new ArrayList<AuthzObject>(objectsNL.getLength());

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

    public AbstractResponse(final T command, final Integer code) {
	this(command, code, null);
    }

    public AbstractResponse(final T command, final Integer code, final List<AuthzObject> objects) {
	this.command = command;
	this.code = code;
	if (objects != null) {
            this.objects = new ArrayList<AuthzObject>(objects);
	} else {
            this.objects = new ArrayList<AuthzObject>(0);
	}
    }

    /**
     * @return the code
     */
    public Integer getCode() {
	return this.code;
    }

    public T getCommand() {
	return this.command;
    }

    /**
     * @return the objects
     */
    public ArrayList<AuthzObject> getObjects() {
        return new ArrayList<AuthzObject>(this.objects);
    }
}
