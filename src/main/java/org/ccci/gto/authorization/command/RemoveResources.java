package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Resource;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import java.util.Collection;

public final class RemoveResources extends AbstractObjectsCommand<Resource> {
    private static final long serialVersionUID = 8755371915056441529L;

    private static final String TYPE = "removeResources";

    public RemoveResources(final Collection<? extends Resource> resources) {
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
    public GenericResponse<RemoveResources> newResponse(final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException {
        return new GenericResponse<RemoveResources>(this, commandXml, xpathEngine);
    }

    @Override
    protected String getObjectsXmlGroupName() {
        return "resources";
    }
}
