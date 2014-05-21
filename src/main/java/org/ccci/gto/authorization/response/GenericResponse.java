package org.ccci.gto.authorization.response;

import org.ccci.gto.authorization.Command;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;

public final class GenericResponse<T extends Command> extends AbstractResponse<T> {
    private static final long serialVersionUID = 214933253518595264L;

    public GenericResponse(final T command, final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException {
        super(command, commandXml, xpathEngine);
    }

    public GenericResponse(final T command, final Integer code) {
        super(command, code);
    }
}
