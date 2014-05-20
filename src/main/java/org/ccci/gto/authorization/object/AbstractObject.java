package org.ccci.gto.authorization.object;

import org.ccci.gto.authorization.AuthzObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class AbstractObject implements AuthzObject {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractObject.class);
    private final Namespace ns;
    private final String name;

    protected AbstractObject(final String ns, final String name) {
        this(new Namespace(ns), name);
    }

    protected AbstractObject(final Element object) {
        this(object.getAttributeNS(null, "namespace"), object.getAttributeNS(null, "name"));
    }

    protected AbstractObject(final Namespace ns, final String name) {
        // throw an error if an invalid namespace or name is specified
        if (ns == null) {
            LOG.debug("no namespace specified");
            throw new IllegalArgumentException("Authorization objects require a namespace to be specified");
        } else if (name == null || name.length() == 0) {
            LOG.debug("no name specified");
            throw new IllegalArgumentException("Authorization objects require a name to be specified");
        } else if (name.contains("|")) {
            LOG.debug("invalid character in name: {}", name);
            throw new IllegalArgumentException("Authorization object names cannot contain a |");
        }

	// store the namespace and name in this object
	this.ns = ns;
	this.name = name;
    }

    public final Namespace getNamespace() {
        return this.ns;
    }

    public final String getName() {
        return this.name;
    }

    public abstract Element toXml(final Document doc);

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
        } else if (obj == null) {
	    return false;
        } else if (!this.getClass().equals(obj.getClass())) {
	    return false;
	}

	final AbstractObject other = (AbstractObject) obj;
        return this.ns.equals(other.ns) && this.name.equalsIgnoreCase(other.name);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + this.ns.hashCode();
        result = 31 * result + this.name.toLowerCase().hashCode();
        return result;
    }

    @Override
    public final String toString() {
        final String ns = this.ns.getName();
        return (ns.length() > 0 ? ns + "|" : "") + this.name;
    }
}
