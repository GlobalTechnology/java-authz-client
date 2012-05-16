package org.ccci.gto.authorization.command;

import java.util.Collection;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.Response;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Role;
import org.ccci.gto.authorization.object.Target;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

public final class AddToRoles extends AbstractDoubleObjectsCommand<Target, Role> {
    final public String TYPE = "addToRoles";

    protected AddToRoles(final Collection<Target> objects1, final Collection<Role> objects2) {
        super("targets", objects1, "roles", objects2);
    }

    @Override
    public String type() {
        return TYPE;
    }

    public Collection<Target> getTargets() {
        return this.getObjects();
    }

    public Collection<Role> getRoles() {
        return this.getObjects2();
    }

    @Override
    public Response<AddToRoles> newResponse(final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException {
        return new GenericResponse<AddToRoles>(this, commandXml, xpathEngine);
    }
}
