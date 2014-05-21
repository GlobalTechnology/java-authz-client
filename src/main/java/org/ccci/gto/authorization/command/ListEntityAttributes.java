package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.Response;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Attribute;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Namespace;
import org.ccci.gto.authorization.response.GenericListResponse;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public final class ListEntityAttributes extends AbstractDoubleObjectsCommand<Entity, Namespace> {
    private static final long serialVersionUID = -3866073386567742815L;

    private static final String TYPE = "listEntityAttributes";

    public ListEntityAttributes(final Entity... entities) {
        this(Arrays.asList(entities));
    }

    public ListEntityAttributes(final Collection<? extends Entity> entities) {
        this(entities, Collections.singleton(Namespace.ROOT));
    }

    public ListEntityAttributes(final Collection<? extends Entity> entities, final Collection<Namespace> namespaces) {
        super(entities, namespaces);
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    protected String getObjectsXmlGroupName() {
        return "entities";
    }

    @Override
    public Collection<Entity> getEntities() {
        return this.getObjects();
    }

    @Override
    protected String getObjectsXmlGroupName2() {
        return "namespaces";
    }

    @Override
    public Collection<Namespace> getNamespaces() {
        return this.getObjects2();
    }

    @Override
    public Response<ListEntityAttributes> newResponse(final Element commandXml, final XPath xpathEngine) throws
            InvalidXmlException {
        return new GenericListResponse<>(this, Attribute.class, commandXml, xpathEngine);
    }
}
