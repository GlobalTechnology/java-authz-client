package org.ccci.gto.authorization.command;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.response.DumpExecutionContextResponse;
import org.w3c.dom.Element;

public final class DumpExecutionContext extends AbstractCommand {
    public final static String TYPE = "dumpExecutionContext";

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public DumpExecutionContextResponse newResponse(Element commandXml, XPath xpathEngine)
            throws InvalidXmlException {
        return new DumpExecutionContextResponse(this, commandXml, xpathEngine);
    }
}
