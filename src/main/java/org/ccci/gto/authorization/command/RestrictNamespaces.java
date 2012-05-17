package org.ccci.gto.authorization.command;

import java.util.Collection;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Namespace;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

public final class RestrictNamespaces extends AbstractObjectsCommand<Namespace> {
    public final static String TYPE = "restrictNamespaces";

    public RestrictNamespaces(final Collection<Namespace> namespaces) {
        super(namespaces);
    }

    protected Collection<Namespace> getNamespaces() {
        return this.getObjects();
    }

    @Override
    public String type() {
        return TYPE;
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
