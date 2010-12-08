/**
 *
 */
package org.ccci.gcx.authorization.object;

/**
 * @author frett
 *
 */
public final class Group extends Entity {
    public Group(final Namespace ns, final String name) {
	super(ns, name);
    }

    public Group(final String ns, final String name) {
	this(new Namespace(ns), name);
    }
}
