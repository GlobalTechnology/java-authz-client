package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.response.DumpExecutionContextResponse;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;

public final class DumpExecutionContext extends AbstractCommand {
    private static final long serialVersionUID = -1174490990581623045L;

    private static final String TYPE = "dumpExecutionContext";

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public DumpExecutionContextResponse newResponse(final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException {
        return new DumpExecutionContextResponse(this, commandXml, xpathEngine);
    }
}
