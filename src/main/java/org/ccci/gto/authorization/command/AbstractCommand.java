package org.ccci.gto.authorization.command;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.Command;
import org.ccci.gto.authorization.Response;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class AbstractCommand implements Command {
    public abstract String type();

    public Element toXml(final Document doc) {
        final Element cmdXml = doc.createElementNS(XMLNS_AUTHZ, "command");
        cmdXml.setAttributeNS(null, "type", this.type());
        return cmdXml;
    }

    public abstract Response<? extends AbstractCommand> newResponse(final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException;
}
