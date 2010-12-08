package org.ccci.gcx.authorization.object;

public class Entity extends Base {
    public Entity(final Namespace ns, final String name) {
	super(ns, name);

	// throw an error if an invalid name was specified
	if (name.contains("|")) {
	    throw new IllegalArgumentException(
		    "Authorization Entity name contains a |");
	} else if (name.length() <= 0) {
	    throw new IllegalArgumentException(
		    "Authorization Entity names cannot be blank");
	}
    }

    public Entity(final String ns, final String name) {
	this(new Namespace(ns), name);
    }
}
