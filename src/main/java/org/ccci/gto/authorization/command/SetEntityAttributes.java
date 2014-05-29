package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Attribute;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import java.util.Collection;
import java.util.Collections;

public final class SetEntityAttributes extends AbstractDoubleObjectsCommand<Entity, Attribute> {
    private static final long serialVersionUID = -6303981763869705253L;

    private static final String TYPE = "setEntityAttributes";

    public SetEntityAttributes(final Entity entity, final Collection<Attribute> attributes) {
        this(Collections.singleton(entity), attributes);
    }

    public SetEntityAttributes(final Collection<? extends Entity> entities, final Collection<Attribute> attributes) {
        super(entities, attributes);
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    protected String getObjectsXmlGroupName() {
        return "entities";
    }

    @Override
    protected String getObjectsXmlGroupName2() {
        return "attributes";
    }

    @Override
    public GenericResponse<SetEntityAttributes> newResponse(final Element commandXml,
                                                            final XPath xpathEngine) throws InvalidXmlException {
        return new GenericResponse<>(this, commandXml, xpathEngine);
    }
}
