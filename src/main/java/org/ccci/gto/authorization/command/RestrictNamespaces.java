package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Namespace;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import java.util.Collection;

public final class RestrictNamespaces extends AbstractObjectsCommand<Namespace> {
    private static final long serialVersionUID = -5668877882450846078L;

    private static final String TYPE = "restrictNamespaces";

    public RestrictNamespaces(final Collection<Namespace> namespaces) {
        super(namespaces);
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Collection<Namespace> getNamespaces() {
        return this.getObjects();
    }

    @Override
    public GenericResponse<RestrictNamespaces> newResponse(final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException {
        return new GenericResponse<RestrictNamespaces>(this, commandXml, xpathEngine);
    }

    @Override
    protected String getObjectsXmlGroupName() {
        return "namespaces";
    }
}
