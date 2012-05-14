package org.ccci.gto.authorization;

import junit.framework.TestCase;

import org.ccci.gto.authorization.object.Namespace;

public class ObjectTest extends TestCase {
    public void testNamespace() {
	final Namespace ns1 = new Namespace("test:namespace");
	final Namespace ns2 = new Namespace("test:namespace");
	assertEquals(ns1, ns2);
    }
}
