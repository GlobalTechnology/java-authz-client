package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.Response;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Group;
import org.ccci.gto.authorization.object.Namespace;
import org.ccci.gto.authorization.response.GenericListResponse;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import java.util.Arrays;
import java.util.Collection;

public final class ListGroupMembers extends AbstractDoubleObjectsCommand<Group, Namespace> {
    private static final long serialVersionUID = 2859733961230082907L;

    private final static String TYPE = "listGroupMembers";

    public ListGroupMembers(final Group... groups) {
        this(Arrays.asList(groups));
    }

    public ListGroupMembers(final Collection<Group> groups) {
        this(groups, Arrays.asList(Namespace.ROOT));
    }

    public ListGroupMembers(final Collection<Group> groups, final Collection<Namespace> namespaces) {
        super(groups, namespaces);
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Collection<Group> getGroups() {
        return this.getObjects();
    }

    @Override
    public Response<ListGroupMembers> newResponse(final Element commandXml, final XPath xpathEngine) throws
            InvalidXmlException {
        return new GenericListResponse<ListGroupMembers, Entity>(this, Entity.class, commandXml, xpathEngine);
    }

    @Override
    public Collection<Namespace> getNamespaces() {
        return this.getObjects2();
    }

    @Override
    protected String getObjectsXmlGroupName() {
        return "groups";
    }

    @Override
    protected String getObjectsXmlGroupName2() {
        return "namespaces";
    }
}
