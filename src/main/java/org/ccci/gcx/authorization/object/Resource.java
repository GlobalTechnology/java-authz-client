package org.ccci.gcx.authorization.object;

import org.ccci.gcx.authorization.AuthzConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class Resource extends Target {
    public Resource(final Namespace ns, final String name) {
	super(ns, name);
    }

    public Resource(final String ns, final String name) {
	this(new Namespace(ns), name);
    }

    /* (non-Javadoc)
     * @see org.ccci.gcx.authorization.object.Base#toXml(org.w3c.dom.Document)
     */
    @Override
    public Element toXml(final Document doc) {
	final Element e = doc.createElementNS(AuthzConstants.XMLNS, "resource");
	e.setAttributeNS(null, "namespace", this.getNamespace().toString());
	e.setAttributeNS(null, "name", this.getName());
	return e;
    }
}
