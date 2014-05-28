package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Group;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import java.util.Collection;

public final class RemoveFromGroups extends AbstractDoubleObjectsCommand<Entity, Group> {
    private static final long serialVersionUID = 1399714931329305348L;

    private static final String TYPE = "removeFromGroups";

    public RemoveFromGroups(final Collection<? extends Entity> entities, final Collection<Group> groups) {
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
    public GenericResponse<RemoveFromGroups> newResponse(final Element commandXml, final XPath xpathEngine)
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
