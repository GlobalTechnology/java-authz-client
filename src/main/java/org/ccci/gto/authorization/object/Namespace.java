package org.ccci.gto.authorization.object;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import org.ccci.gto.authorization.AuthzObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class Namespace implements AuthzObject {
    public final static Namespace ROOT = new Namespace("");

    final private String name;
    final private String[] parts;

    public Namespace(final String name) {
        if (name == null) {
            this.name = "";
        } else if (name.contains("|")) {
            // throw an error if the namespace name contains a |
            throw new IllegalArgumentException("Authorization Namespaces cannot contain |");
        } else {
            this.name = name;
        }

        if (this.name.length() == 0) {
            this.parts = new String[0];
        } else {
            this.parts = this.name.split(":");
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

    public Namespace parent() {
        if (this.parts.length == 0) {
            return null;
        }

        final StringBuilder builder = new StringBuilder();
        for (int x = 0; x < this.parts.length - 1; x++) {
            if (x > 0) {
                builder.append(":");
            }
            builder.append(this.parts[x]);
        }
        return new Namespace(builder.toString());
    }

    public Namespace descendant(final String descendant) {
        if (descendant == null || descendant.length() == 0) {
            return this;
        } else if (this.name.length() == 0) {
            return new Namespace(descendant);
        }

        return new Namespace(this.name + ":" + descendant);
    }

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
        int result = 0;
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
