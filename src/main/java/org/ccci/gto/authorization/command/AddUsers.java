package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.User;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import java.util.Collection;

public final class AddUsers extends AbstractObjectsCommand<User> {
    private static final long serialVersionUID = 6364319586036384123L;

    private static final String TYPE = "addUsers";

    public AddUsers(final User... users) {
        super(users);
    }

    public AddUsers(final Collection<User> users) {
        super(users);
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Collection<User> getUsers() {
        return super.getObjects();
    }

    @Override
    public GenericResponse<AddUsers> newResponse(final Element commandXml, final XPath xpathEngine) throws
            InvalidXmlException {
        return new GenericResponse<>(this, commandXml, xpathEngine);
    }

    @Override
    protected String getObjectsXmlGroupName() {
        return "users";
    }
}
