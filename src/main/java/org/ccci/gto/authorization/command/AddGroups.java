package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.Response;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Group;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import java.util.Collection;

public final class AddGroups extends AbstractObjectsCommand<Group> {
    private static final long serialVersionUID = -4115862949382898397L;

    private static final String TYPE = "addGroups";

    public AddGroups(final Group... groups) {
        super(groups);
    }

    public AddGroups(final Collection<? extends Group> objects) {
        super(objects);
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Collection<Group> getGroups() {
        return this.getObjects();
    }

    @Override
    protected String getObjectsXmlGroupName() {
        return "groups";
    }

    @Override
    public Response<AddGroups> newResponse(final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException {
        return new GenericResponse<AddGroups>(this, commandXml, xpathEngine);
    }
}
