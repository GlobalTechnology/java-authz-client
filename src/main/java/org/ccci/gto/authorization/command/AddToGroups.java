package org.ccci.gto.authorization.command;

import java.util.Collection;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.Response;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Group;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

public final class AddToGroups extends AbstractDoubleObjectsCommand<Entity, Group> {
    final public String TYPE = "addToGroups";

    public AddToGroups(final Collection<? extends Entity> entities, final Collection<Group> groups) {
        super(entities, groups);
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Collection<Entity> getEntities() {
        return super.getObjects();
    }

    @Override
    public Collection<Group> getGroups() {
        return super.getObjects2();
    }

    @Override
    public Response<AddToGroups> newResponse(final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException {
        return new GenericResponse<AddToGroups>(this, commandXml, xpathEngine);
    }

    @Override
    protected String getObjectsXmlGroupName() {
        return "entities";
    }

    @Override
    protected String getObjectsXmlGroupName2() {
        return "groups";
    }
}
