package org.ccci.gto.authorization;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface Command {
    public Response newResponse(final Element commandXml, final XPath xpathEngine) throws InvalidXmlException;

    public Element toXml(final Document doc);
}
