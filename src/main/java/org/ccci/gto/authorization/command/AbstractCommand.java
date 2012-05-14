package org.ccci.gto.authorization.command;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.Command;
import org.ccci.gto.authorization.Response;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class AbstractCommand implements Command {
    public abstract Response<? extends AbstractCommand> newResponse(final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException;

    public Element toXml(final Document doc) {
        return doc.createElementNS(XMLNS_AUTHZ, "command");
    }
}
