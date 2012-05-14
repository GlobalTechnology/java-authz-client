package org.ccci.gto.authorization.object;

import org.ccci.gto.authorization.AuthzConstants;
import org.ccci.gto.authorization.Object;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class Key extends Object {
    public Key(final String key) {
	super(null, key);

	// throw an error if the key is too long
	if (key.length() > 44) {
	    throw new IllegalArgumentException(
		    "Authorization keys are 44 or less characters");
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Key obj) {
	// short-circuit if this is the same object as the referenced object
	if (this == obj) {
	    return true;
	}

	// short-circuit if an undefined object is being compared
	if (obj == null) {
	    return false;
	}

	// compare the actual name for both namespace objects
	return this.getKey().equals(obj.getKey());
    }

    public final String getKey() {
	return this.getName();
    }

    /* (non-Javadoc)
     * @see org.ccci.gcx.authorization.object.Base#toXml(org.w3c.dom.Document)
     */
    @Override
    public Element toXml(final Document doc) {
	final Element e = doc.createElementNS(AuthzConstants.XMLNS, "key");
	e.setAttributeNS(null, "key", this.getKey());
	return e;
    }
}
