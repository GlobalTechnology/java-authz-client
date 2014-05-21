package org.ccci.gto.authorization.command;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import org.ccci.gto.authorization.AuthzObject;
import org.ccci.gto.authorization.exception.NullObjectException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractObjectsCommand<O extends AuthzObject> extends AbstractCommand {
    private static final long serialVersionUID = -23161568779755255L;

    private final Set<O> objects;

    @SafeVarargs
    protected AbstractObjectsCommand(final O... objects) {
        this(Arrays.asList(objects));
    }

    protected AbstractObjectsCommand(final Collection<? extends O> objects) {
        this.objects = Collections.unmodifiableSet(new HashSet<>(objects));
        if(this.objects.contains(null)) {
            throw new NullObjectException();
        }
    }

    protected abstract String getObjectsXmlGroupName();

    protected final Collection<O> getObjects() {
        return this.objects;
    }

    @Override
    public Element toXml(final Document doc) {
        final Element commandXml = super.toXml(doc);

        // generate xml for objects
        final Element objectsXml = doc.createElementNS(XMLNS_AUTHZ, this.getObjectsXmlGroupName());
        for (final AuthzObject object : this.objects) {
            objectsXml.appendChild(object.toXml(doc));
        }
        commandXml.appendChild(objectsXml);

        return commandXml;
    }
}
