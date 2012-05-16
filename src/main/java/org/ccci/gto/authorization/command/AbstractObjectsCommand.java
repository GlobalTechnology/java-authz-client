package org.ccci.gto.authorization.command;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.ccci.gto.authorization.AuthzObject;
import org.ccci.gto.authorization.exception.NullObjectException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class AbstractObjectsCommand<O extends AuthzObject> extends AbstractCommand {
    final private String objectsXmlName;
    final private Collection<O> objects;

    protected AbstractObjectsCommand(final String objectsXmlName, final O... objects) {
        this(objectsXmlName, Arrays.asList(objects));
    }

    protected AbstractObjectsCommand(final String objectsXmlName, final Collection<O> objects) {
        for (final O object : objects) {
            if (object == null) {
                throw new NullObjectException();
            }
        }
        this.objectsXmlName = objectsXmlName;
        this.objects = Collections.unmodifiableSet(new HashSet<O>(objects));
    }

    protected final Collection<O> getObjects() {
        return this.objects;
    }

    @Override
    public final Element toXml(Document doc) {
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
