package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Target;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import java.util.Collection;

public final class RemovePermissions extends AbstractDoubleObjectsCommand<Entity, Target> {
    private static final long serialVersionUID = 6969811552062195193L;

    private static final String TYPE = "removePermissions";

    public RemovePermissions(final Collection<? extends Entity> entities, final Collection<? extends Target> targets) {
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
    public GenericResponse<RemovePermissions> newResponse(final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException {
        return new GenericResponse<>(this, commandXml, xpathEngine);
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
