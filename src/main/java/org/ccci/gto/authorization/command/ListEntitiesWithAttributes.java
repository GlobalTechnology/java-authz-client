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

public class ListEntitiesWithAttributes extends AbstractDoubleObjectsCommand<Attribute, Namespace> {
    private static final String TYPE = "listEntitiesWithAttributes";

    public ListEntitiesWithAttributes(final Attribute... attributes) {
        this(Arrays.asList(attributes));
    }

    public ListEntitiesWithAttributes(final Collection<Attribute> attributes) {
        this(attributes, Collections.singleton(Namespace.ROOT));
    }

    public ListEntitiesWithAttributes(final Collection<Attribute> attributes, final Collection<Namespace> namespaces) {
        super(attributes, namespaces);
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    protected String getObjectsXmlGroupName() {
        return "attributes";
    }

    @Override
    public Collection<Attribute> getAttributes() {
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
    public Response<ListEntitiesWithAttributes> newResponse(final Element commandXml,
                                                            final XPath xpathEngine) throws InvalidXmlException {
        return new GenericListResponse<>(this, Entity.class, commandXml, xpathEngine);
    }
}
