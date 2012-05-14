package org.ccci.gto.authorization.object;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class Role extends Target {
    public Role(final Namespace ns, final String name) {
	super(ns, name);
    }

    public Role(final String ns, final String name) {
	this(new Namespace(ns), name);
    }

    /* (non-Javadoc)
     * @see org.ccci.gcx.authorization.object.Base#toXml(org.w3c.dom.Document)
     */
    @Override
    public Element toXml(final Document doc) {
        final Element e = doc.createElementNS(XMLNS_AUTHZ, "role");
	e.setAttributeNS(null, "namespace", this.getNamespace().toString());
	e.setAttributeNS(null, "name", this.getName());
	return e;
    }
}
