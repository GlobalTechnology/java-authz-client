package org.ccci.gcx.authorization.object;

import java.util.regex.Pattern;

import org.ccci.gcx.authorization.AuthzConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class User extends Entity {
    private static final Pattern userRegex = Pattern
	    .compile("^[0-9A-F]{8}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{12}$");
    public static final User GUEST = new User("GUEST");
    public static final User SUPERUSER = new User("SUPERUSER");

    public User(final Element xmlNode) {
	this(xmlNode.getAttributeNS(null, "name"));
    }

    public User(final String name) {
	super(Namespace.ROOT, name.toUpperCase());

	// throw an error if an invalid user is specified
	if (!(name.equals("SUPERUSER") || name.equals("GUEST")
		|| name.equals("DEFAULT") || userRegex.matcher(name).matches())) {
	    throw new IllegalArgumentException("Invalid User Specified");
	}
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final User obj) {
	//short-circuit if this is the same object as the referenced object
	if(this == obj) {
	    return true;
	}

	//short-circuit if an undefined object is being compared
	if (obj == null) {
	    return false;
	}

	//compare the actual name for both user objects
	return this.getName().equals(obj.getName());
    }

    /* (non-Javadoc)
     * @see org.ccci.gcx.authorization.object.Entity#toXml(org.w3c.dom.Document)
     */
    @Override
    public Element toXml(final Document doc) {
	final Element e = doc.createElementNS(AuthzConstants.XMLNS, "user");
	e.setAttributeNS(null, "name", this.getName());
	return e;
    }
}
