package org.ccci.gto.authorization.command;

import java.util.Collection;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.Response;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.User;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

public final class AddUsers extends AbstractObjectsCommand<User> {
    public static final String TYPE = "addUsers";

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
    public Response<AddUsers> newResponse(final Element commandXml, final XPath xpathEngine) throws InvalidXmlException {
        return new GenericResponse<AddUsers>(this, commandXml, xpathEngine);
    }

    @Override
    protected String getObjectsXmlGroupName() {
        return "users";
    }
}
