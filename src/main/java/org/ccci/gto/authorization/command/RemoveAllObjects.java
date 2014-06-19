package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Namespace;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import java.util.Collection;

public final class RemoveAllObjects extends AbstractObjectsCommand<Namespace> {
    private static final long serialVersionUID = 6362596204432287364L;

    private static final String TYPE = "removeAllObjects";

    public RemoveAllObjects(final Namespace... namespaces) {
        super(namespaces);
    }

    public RemoveAllObjects(final Collection<? extends Namespace> namespaces) {
        super(namespaces);
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    protected String getObjectsXmlGroupName() {
        return "namespaces";
    }

    @Override
    public GenericResponse<RemoveAllObjects> newResponse(final Element commandXml,
                                                         final XPath xpathEngine) throws InvalidXmlException {
        return new GenericResponse<>(this, commandXml, xpathEngine);
    }
}
