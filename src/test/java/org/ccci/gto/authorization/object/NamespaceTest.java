package org.ccci.gto.authorization.object;

import junit.framework.TestCase;

public class NamespaceTest extends TestCase {
    public void testEquals() {
	final Namespace ns1 = new Namespace("test:namespace");
	final Namespace ns2 = new Namespace("test:namespace");
	assertEquals(ns1, ns2);
    }
}
