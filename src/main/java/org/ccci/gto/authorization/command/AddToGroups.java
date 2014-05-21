package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.Response;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Group;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import java.util.Collection;

public final class AddToGroups extends AbstractDoubleObjectsCommand<Entity, Group> {
    private static final long serialVersionUID = 4260273465866360785L;

    private static final String TYPE = "addToGroups";

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
        return new GenericResponse<>(this, commandXml, xpathEngine);
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
