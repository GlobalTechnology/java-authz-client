package org.ccci.gto.authorization.command;

import java.util.Collection;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Role;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

public final class AddRoles extends AbstractObjectsCommand<Role> {
    public final static String TYPE = "addRoles";

    public AddRoles(final Role... roles) {
        super(roles);
    }

    public AddRoles(final Collection<Role> roles) {
        super(roles);
    }

    public Collection<Role> getRoles() {
        return this.getObjects();
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public GenericResponse<AddRoles> newResponse(final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException {
        return new GenericResponse<AddRoles>(this, commandXml, xpathEngine);
    }

    @Override
    protected String getObjectsXmlGroupName() {
        return "roles";
    }
}
