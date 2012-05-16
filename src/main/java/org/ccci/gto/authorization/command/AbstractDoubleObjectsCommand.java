package org.ccci.gto.authorization.command;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.ccci.gto.authorization.AuthzObject;
import org.ccci.gto.authorization.exception.NullObjectException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class AbstractDoubleObjectsCommand<O1 extends AuthzObject, O2 extends AuthzObject> extends
        AbstractObjectsCommand<O1> {
    final private String objectsXmlName;
    final private Collection<O2> objects;

    public AbstractDoubleObjectsCommand(final String objects1XmlName, final Collection<O1> objects1,
            final String objects2XmlName, final Collection<O2> objects2) {
        super(objects1XmlName, objects1);

        for (final O2 object : objects2) {
            if (object == null) {
                throw new NullObjectException();
            }
        }
        this.objectsXmlName = objects2XmlName;
        this.objects = Collections.unmodifiableSet(new HashSet<O2>(objects2));
    }

    protected final Collection<O2> getObjects2() {
        return this.objects;
    }

    @Override
    public Element toXml(final Document doc) {
        final Element commandXml = super.toXml(doc);

        // generate xml for objects
        final Element objectsXml = doc.createElementNS(XMLNS_AUTHZ, this.objectsXmlName);
        for (final AuthzObject object : this.objects) {
            objectsXml.appendChild(object.toXml(doc));
        }
        commandXml.appendChild(objectsXml);

        return commandXml;
    }
}
