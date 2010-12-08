package org.ccci.gcx.authorization.object;

public class Target extends Base {
    public Target(final Namespace ns, final String name) {
	super(ns, name);

	// throw an error if an invalid name was specified
	if (name.contains("|")) {
	    throw new IllegalArgumentException(
		    "Authorization Target name contains a |");
	} else if (name.length() <= 0) {
	    throw new IllegalArgumentException(
		    "Authorization Target names cannot be blank");
	}
    }

    public Target(final String ns, final String name) {
	this(new Namespace(ns), name);
    }
}
