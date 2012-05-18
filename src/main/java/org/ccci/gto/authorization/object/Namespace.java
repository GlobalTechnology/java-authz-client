package org.ccci.gto.authorization.object;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import org.ccci.gto.authorization.AuthzObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class Namespace implements AuthzObject {
    public final static Namespace ROOT = new Namespace("");

    final private String name;

    public Namespace(final String name) {
        if (name == null) {
            this.name = "";
        } else if (name.contains("|")) {
            // throw an error if the namespace name contains a |
            throw new IllegalArgumentException("Authorization Namespaces cannot contain |");
        } else {
            this.name = name;
        }
    }

    public Namespace(final Element object) {
        this(object.getAttributeNS(null, "name"));
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ccci.gcx.authorization.object.Base#toXml(org.w3c.dom.Document)
     */
    @Override
    public Element toXml(final Document doc) {
        final Element e = doc.createElementNS(XMLNS_AUTHZ, "namespace");
	e.setAttributeNS(null, "name", this.getName());
	return e;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {
        if (this == obj) {
            // the objects are the same
            return true;
        } else if (obj == null) {
            // the object being compared is null
            return false;
        } else if (!this.getClass().equals(obj.getClass())) {
            // the objects are the same class
            return false;
        }

        // compare the actual name for both namespace objects
        return ((Namespace) obj).getName().equalsIgnoreCase(this.getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = result * 31 + this.name.toLowerCase().hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getName();
    }
}
