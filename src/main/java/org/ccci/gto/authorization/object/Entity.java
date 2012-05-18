package org.ccci.gto.authorization.object;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Entity extends AbstractObject {
    public Entity(final String name) {
        super(name);
    }

    public Entity(final Element object) {
        super(object);
    }

    public Entity(final String ns, final String name) {
        super(ns, name);
    }

    public Entity(final Namespace ns, final String name) {
	super(ns, name);
    }

    @Override
    public Element toXml(final Document doc) {
        final Element e = doc.createElementNS(XMLNS_AUTHZ, "entity");
	e.setAttributeNS(null, "namespace", this.getNamespace().toString());
	e.setAttributeNS(null, "name", this.getName());
	return e;
    }
}
