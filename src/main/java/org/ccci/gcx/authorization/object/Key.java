package org.ccci.gcx.authorization.object;

public final class Key extends Base {
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
}
