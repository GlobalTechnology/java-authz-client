package org.ccci.gto.authorization.object;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class Resource extends Target {
    private static final long serialVersionUID = -7909566464967037187L;

    public Resource(final String ns, final String name) {
        super(ns, name);
    }

    public Resource(final Element object) {
        super(object);
    }

    public Resource(final Namespace ns, final String name) {
        super(ns, name);
    }

    /* (non-Javadoc)
     * @see org.ccci.gcx.authorization.object.Base#toXml(org.w3c.dom.Document)
     */
    @Override
    public Element toXml(final Document doc) {
        final Element e = doc.createElementNS(XMLNS_AUTHZ, "resource");
	e.setAttributeNS(null, "namespace", this.getNamespace().toString());
	e.setAttributeNS(null, "name", this.getName());
	return e;
    }
}
