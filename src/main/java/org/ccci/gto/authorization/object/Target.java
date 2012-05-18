package org.ccci.gto.authorization.object;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Target extends AbstractObject {
    public Target(final String name) {
        super(name);
    }

    public Target(final String ns, final String name) {
        super(ns, name);
    }

    public Target(final Element object) {
        super(object);
    }

    public Target(final Namespace ns, final String name) {
        super(ns, name);
    }

    @Override
    public Element toXml(final Document doc) {
        final Element e = doc.createElementNS(XMLNS_AUTHZ, "target");
	e.setAttributeNS(null, "namespace", this.getNamespace().toString());
	e.setAttributeNS(null, "name", this.getName());
	return e;
    }
}
