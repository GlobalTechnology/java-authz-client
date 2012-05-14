package org.ccci.gto.authorization.object;

import org.ccci.gto.authorization.AuthzConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Entity extends AbstractObject {
    public Entity(final Namespace ns, final String name) {
	super(ns, name);

	// throw an error if an invalid name was specified
	if (name.contains("|")) {
	    throw new IllegalArgumentException(
		    "Authorization Entity name contains a |");
	} else if (name.length() <= 0) {
	    throw new IllegalArgumentException(
		    "Authorization Entity names cannot be blank");
	}
    }

    public Entity(final String ns, final String name) {
	this(new Namespace(ns), name);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ccci.gcx.authorization.object.Base#toXml(org.w3c.dom.Document)
     */
    @Override
    public Element toXml(final Document doc) {
	final Element e = doc.createElementNS(AuthzConstants.XMLNS, "entity");
	e.setAttributeNS(null, "namespace", this.getNamespace().toString());
	e.setAttributeNS(null, "name", this.getName());
	return e;
    }
}
