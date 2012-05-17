package org.ccci.gto.authorization.command;

import java.util.Collection;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Resource;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

public final class RemoveResources extends AbstractObjectsCommand<Resource> {
    final public static String TYPE = "removeResources";

    public RemoveResources(final Collection<Resource> resources) {
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
