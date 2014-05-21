package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Resource;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import java.util.Collection;

public final class AddResources extends AbstractObjectsCommand<Resource> {
    private static final long serialVersionUID = -5532989048920383926L;

    private static final String TYPE = "addResources";

    public AddResources(final Resource... resources) {
        super(resources);
    }

    public AddResources(final Collection<? extends Resource> resources) {
        super(resources);
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Collection<Resource> getResources() {
        return this.getObjects();
    }

    @Override
    public GenericResponse<AddResources> newResponse(final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException {
        return new GenericResponse<AddResources>(this, commandXml, xpathEngine);
    }

    @Override
    protected String getObjectsXmlGroupName() {
        return "resources";
    }
}
