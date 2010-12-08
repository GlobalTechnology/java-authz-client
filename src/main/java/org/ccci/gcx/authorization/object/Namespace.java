package org.ccci.gcx.authorization.object;

public final class Namespace extends Base {
    public final static Namespace ROOT = new Namespace("");

    public Namespace(final String name) {
	super((Namespace) null, name == null ? "" : name);

	// throw an error if the namespace name contains a |
	if (name != null && name.contains("|")) {
	    throw new IllegalArgumentException(
		    "Authorization Namespaces cannot contain |");
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Namespace obj) {
	// short-circuit if this is the same object as the referenced object
	if (this == obj) {
	    return true;
	}

	// short-circuit if an undefined object is being compared
	if (obj == null) {
	    return false;
	}

	// compare the actual name for both namespace objects
	return this.getName().equalsIgnoreCase(obj.getName());
    }
}
