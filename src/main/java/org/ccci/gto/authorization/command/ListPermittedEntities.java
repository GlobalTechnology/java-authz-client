package org.ccci.gto.authorization.command;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Namespace;
import org.ccci.gto.authorization.object.Target;
import org.ccci.gto.authorization.response.GenericListResponse;
import org.w3c.dom.Element;

public final class ListPermittedEntities extends AbstractDoubleObjectsCommand<Target, Namespace> {
    public final static String TYPE = "listPermittedEntities";

    public ListPermittedEntities(final Collection<Target> targets) {
        this(targets, Arrays.asList(Namespace.ROOT));
    }

    public ListPermittedEntities(final Collection<Target> targets, final Collection<Namespace> namespaces) {
        super(targets, namespaces);
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public GenericListResponse<ListPermittedEntities, Entity> newResponse(final Element commandXml,
            final XPath xpathEngine) throws InvalidXmlException {
        return new GenericListResponse<ListPermittedEntities, Entity>(this, Entity.class, commandXml, xpathEngine);
    }

    @Override
    protected String getObjectsXmlGroupName() {
        return "targets";
    }

    @Override
    protected String getObjectsXmlGroupName2() {
        return "namespaces";
    }
}
