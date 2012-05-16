package org.ccci.gto.authorization.command;

import java.util.Collection;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Role;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

public final class RemoveRoles extends AbstractObjectsCommand<Role> {
    final public static String TYPE = "removeRoles";

    public RemoveRoles(final Role... roles) {
        super("roles", roles);
    }

    public RemoveRoles(final Collection<Role> roles) {
        super("roles", roles);
    }

    @Override
    public String type() {
        return TYPE;
    }

    public Collection<Role> getRoles() {
        return this.getObjects();
    }

    @Override
    public GenericResponse<RemoveRoles> newResponse(final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException {
        return new GenericResponse<RemoveRoles>(this, commandXml, xpathEngine);
    }
}
