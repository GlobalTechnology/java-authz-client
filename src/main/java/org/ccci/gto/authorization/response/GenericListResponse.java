package org.ccci.gto.authorization.response;

import java.util.Collection;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.AuthzObject;
import org.ccci.gto.authorization.Command;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.w3c.dom.Element;

public final class GenericListResponse<T extends Command, O extends AuthzObject> extends AbstractListResponse<T, O> {
    public GenericListResponse(final T command, final Integer code, final Class<O> objectClass,
            final Collection<O> objects) {
        super(command, code, objectClass, objects);
    }

    public GenericListResponse(final T command, final Class<O> objectClass, final Element commandXml,
            final XPath xpathEngine) throws InvalidXmlException {
        super(command, objectClass, commandXml, xpathEngine);
    }
}
