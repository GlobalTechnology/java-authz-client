package org.ccci.gto.authorization.object;

import org.ccci.gto.authorization.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Target extends AbstractObject {
    public Target(final Namespace ns, final String name) {
	super(ns, name);

	// throw an error if an invalid name was specified
	if (name.contains("|")) {
	    throw new IllegalArgumentException(
		    "Authorization Target name contains a |");
	} else if (name.length() <= 0) {
	    throw new IllegalArgumentException(
		    "Authorization Target names cannot be blank");
	}
    }

    public Target(final String ns, final String name) {
	this(new Namespace(ns), name);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ccci.gcx.authorization.object.Base#toXml(org.w3c.dom.Document)
     */
    @Override
    public Element toXml(final Document doc) {
	final Element e = doc.createElementNS(Constants.XMLNS_AUTHZ, "target");
	e.setAttributeNS(null, "namespace", this.getNamespace().toString());
	e.setAttributeNS(null, "name", this.getName());
	return e;
    }
}
