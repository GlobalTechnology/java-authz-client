package org.ccci.gcx.authorization.object;

public final class Role extends Target {
    public Role(final Namespace ns, final String name) {
	super(ns, name);
    }

    public Role(final String ns, final String name) {
	this(new Namespace(ns), name);
    }
}
