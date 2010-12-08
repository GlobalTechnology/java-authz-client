package org.ccci.gcx.authorization.object;

public final class Resource extends Target {
    public Resource(final Namespace ns, final String name) {
	super(ns, name);
    }

    public Resource(final String ns, final String name) {
	this(new Namespace(ns), name);
    }
}
