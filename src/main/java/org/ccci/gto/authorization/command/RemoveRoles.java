package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Role;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import java.util.Collection;

public final class RemoveRoles extends AbstractObjectsCommand<Role> {
    private static final long serialVersionUID = 8716887591277927323L;

    private static final String TYPE = "removeRoles";

    public RemoveRoles(final Collection<? extends Role> roles) {
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
    public GenericResponse<RemoveRoles> newResponse(final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException {
        return new GenericResponse<RemoveRoles>(this, commandXml, xpathEngine);
    }

    @Override
    protected String getObjectsXmlGroupName() {
        return "roles";
    }
}
