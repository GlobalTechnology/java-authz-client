package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.Response;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Role;
import org.ccci.gto.authorization.object.Target;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import java.util.Collection;

public final class AddToRoles extends AbstractDoubleObjectsCommand<Target, Role> {
    private static final long serialVersionUID = 4315367511812905961L;

    private static final String TYPE = "addToRoles";

    public AddToRoles(final Collection<? extends Target> targets, final Collection<Role> roles) {
        super(targets, roles);
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
    public Collection<Role> getRoles() {
        return this.getObjects2();
    }

    @Override
    public Response<AddToRoles> newResponse(final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException {
        return new GenericResponse<AddToRoles>(this, commandXml, xpathEngine);
    }

    @Override
    protected String getObjectsXmlGroupName() {
        return "targets";
    }

    @Override
    protected String getObjectsXmlGroupName2() {
        return "roles";
    }
}
