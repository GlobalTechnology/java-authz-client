package org.ccci.gto.authorization.command;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Namespace;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class RestrictNamespaces extends AbstractCommand {
    public final static String TYPE = "restrictNamespaces";

    private final Collection<Namespace> namespaces;

    public RestrictNamespaces(final Namespace... namespaces) {
        this(Arrays.asList(namespaces));
    }

    public RestrictNamespaces(final Collection<Namespace> namespaces) {
        this.namespaces = Collections.unmodifiableSet(new HashSet<Namespace>(namespaces));
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

    protected Collection<Namespace> getNamespaces() {
        return this.namespaces;
    }

    @Override
    public Element toXml(final Document doc) {
        final Element command = super.toXml(doc);

        // attach the namespaces to the generated xml
        final Element namespaces = doc.createElementNS(XMLNS_AUTHZ, "namespaces");
        for (final Namespace namespace : this.namespaces) {
            namespaces.appendChild(namespace.toXml(doc));
        }
        command.appendChild(namespaces);

        return command;
    }
}
