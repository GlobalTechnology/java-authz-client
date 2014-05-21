package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Namespace;
import org.ccci.gto.authorization.object.Target;
import org.ccci.gto.authorization.response.GenericListResponse;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import java.util.Collection;
import java.util.Collections;

public final class ListPermittedEntities extends AbstractDoubleObjectsCommand<Target, Namespace> {
    private static final long serialVersionUID = 690704196560456273L;

    private static final String TYPE = "listPermittedEntities";

    public ListPermittedEntities(final Collection<? extends Target> targets) {
        this(targets, Collections.singleton(Namespace.ROOT));
    }

    public ListPermittedEntities(final Collection<? extends Target> targets, final Collection<Namespace> namespaces) {
        super(targets, namespaces);
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Collection<Target> getTargets() {
        return this.getObjects();
    }

    @Override
    public Collection<Namespace> getNamespaces() {
        return this.getObjects2();
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
