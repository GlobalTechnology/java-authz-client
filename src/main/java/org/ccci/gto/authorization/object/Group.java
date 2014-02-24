package org.ccci.gto.authorization.object;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author frett
 *
 */
public final class Group extends Entity {
    public Group(final String ns, final String name) {
        super(ns, name);
    }

    public Group(final Namespace ns, final String name) {
	super(ns, name);
    }

    public Group(final Element object) {
        super(object);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ccci.gcx.authorization.object.Entity#toXml(org.w3c.dom.Document)
     */
    @Override
    public Element toXml(final Document doc) {
        final Element e = doc.createElementNS(XMLNS_AUTHZ, "group");
	e.setAttributeNS(null, "namespace", this.getNamespace().toString());
	e.setAttributeNS(null, "name", this.getName());
	return e;
    }
}
