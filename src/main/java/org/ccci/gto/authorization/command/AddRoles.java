package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Role;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import java.util.Collection;

public final class AddRoles extends AbstractObjectsCommand<Role> {
    private static final long serialVersionUID = 3549172108969858537L;

    private static final String TYPE = "addRoles";

    public AddRoles(final Collection<? extends Role> roles) {
        super(roles);
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Collection<Role> getRoles() {
        return this.getObjects();
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
