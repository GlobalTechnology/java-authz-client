package org.ccci.gcx.authorization.object;

public abstract class Base {
    private final static int HASHSEED = 31;
    final private Namespace ns;
    final private String name;

    public Base(final Namespace ns, final String name) {
	// throw an error if a name is not specified
	if (name == null) {
	    throw new IllegalArgumentException("Authorization objects require a name to be specified");
	}

	// store the namespace and name in this object
	this.ns = ns;
	this.name = name;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (this.getClass() != obj.getClass()) {
	    return false;
	}
	final Base other = (Base) obj;
	if (this.name == null) {
	    if (other.name != null) {
		return false;
	    }
	} else if (!this.name.equalsIgnoreCase(other.name)) {
	    return false;
	}
	if (this.ns == null) {
	    if (other.ns != null) {
		return false;
	    }
	} else if (!this.ns.equals(other.ns)) {
	    return false;
	}
	return true;
    }

    public final String getName() {
	return this.name;
    }

    public final Namespace getNamespace() {
	return this.ns;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	int hash = 1;
	hash = HASHSEED * hash + this.name.toLowerCase().hashCode();
	hash = HASHSEED * hash + ((this.ns == null) ? 0 : this.ns.hashCode());
	return hash;
    }

    @Override
    public final String toString() {
	if (this.ns == null || this.ns.getName().length() == 0) {
	    return this.name;
	} else {
	    return this.ns + "|" + this.name;
	}
    }
}