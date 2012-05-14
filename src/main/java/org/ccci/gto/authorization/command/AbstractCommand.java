package org.ccci.gto.authorization.command;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.AuthzConstants;
import org.ccci.gto.authorization.Command;
import org.ccci.gto.authorization.Response;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class AbstractCommand implements Command {
    public Response newResponse(final Element commandXml,
	    final XPath xpathEngine) throws InvalidXmlException {
	return new Response(this, commandXml, xpathEngine);
    }

    public Element toXml(final Document doc) {
        return doc.createElementNS(AuthzConstants.XMLNS, "command");
    }
}
