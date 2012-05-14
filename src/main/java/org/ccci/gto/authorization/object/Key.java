package org.ccci.gto.authorization.object;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import org.ccci.gto.authorization.AuthzObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class Key implements AuthzObject {
    final String key;

    public Key(final String key) {
	// throw an error if the key is too long
	if (key.length() > 44) {
	    throw new IllegalArgumentException(
		    "Authorization keys are 44 or less characters");
	}

        this.key = key;
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

        // compare the actual key for both Key objects
	return this.getKey().equals(obj.getKey());
    }

    public final String getKey() {
        return this.key;
    }

    /* (non-Javadoc)
     * @see org.ccci.gcx.authorization.object.Base#toXml(org.w3c.dom.Document)
     */
    @Override
    public Element toXml(final Document doc) {
        final Element e = doc.createElementNS(XMLNS_AUTHZ, "key");
        e.setAttributeNS(null, "key", this.key);
	return e;
    }
}
