package org.ccci.gto.authorization.command;

import java.util.Collection;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Target;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

public final class AddPermissions extends AbstractDoubleObjectsCommand<Entity, Target> {
    final public static String TYPE = "addPermissions";

    public AddPermissions(final Collection<? extends Entity> entities, final Collection<? extends Target> targets) {
        super(entities, targets);
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Collection<Entity> getEntities() {
        return this.getObjects();
    }

    @Override
    public Collection<Target> getTargets() {
        return this.getObjects2();
    }

    @Override
    public GenericResponse<AddPermissions> newResponse(Element commandXml, XPath xpathEngine)
            throws InvalidXmlException {
        return new GenericResponse<AddPermissions>(this, commandXml, xpathEngine);
    }

    @Override
    protected String getObjectsXmlGroupName() {
        return "entities";
    }

    @Override
    protected String getObjectsXmlGroupName2() {
        return "targets";
    }
}
