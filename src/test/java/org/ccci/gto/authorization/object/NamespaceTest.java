package org.ccci.gto.authorization.object;

import junit.framework.TestCase;

public class NamespaceTest extends TestCase {
    public void testEquals() {
        final Namespace ns1 = new Namespace("test:namespace");
        final Namespace ns2 = new Namespace("TEST:NameSpace");
        assertEquals(ns1, ns2);
    }

    public void testDescendant() {
        final Namespace base = Namespace.ROOT;
        final Namespace child1 = new Namespace("child1");
        final Namespace child2 = new Namespace("child1:child2:child3");
        assertEquals(base, base.descendant(null));
        assertEquals(base, base.descendant(""));
        assertEquals(child1, child1.descendant(null));
        assertEquals(child1, child1.descendant(""));
        assertEquals(child1, base.descendant("child1"));
        assertEquals(child2, child1.descendant("child2:child3"));
        assertEquals(child2, base.descendant("child1").descendant("child2:child3"));
    }

    public void testParent() {
        final Namespace base = Namespace.ROOT;
        final Namespace child1 = new Namespace("child1");
        final Namespace child2 = new Namespace("child1:child2:child3");
        assertNull(base.parent());
        assertEquals(base, child1.parent());
        assertNull(child1.parent().parent());
        assertEquals(child1, child2.parent().parent());
        assertEquals(base, child2.parent().parent().parent());
        assertNull(child2.parent().parent().parent().parent());
    }
}
