package org.ccci.gto.authorization;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

public final class AuthzNamespaceContext implements NamespaceContext {
    public final static AuthzNamespaceContext INSTANCE = new AuthzNamespaceContext();

    public String getNamespaceURI(final String prefix) {
	if (prefix == null) {
	    throw new IllegalArgumentException("No prefix provided");
	} else if (prefix.equals(XMLConstants.XML_NS_PREFIX)) {
	    return XMLConstants.XML_NS_URI;
	} else if (prefix.equals(XMLConstants.XMLNS_ATTRIBUTE)) {
	    return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
	} else if (prefix.equals("authz")) {
	    return AuthzConstants.XMLNS;
	} else {
	    return XMLConstants.NULL_NS_URI;
	}
    }

    public String getPrefix(final String arg0) {
	// TODO Not Implemented
	return null;
    }

    public Iterator<String> getPrefixes(final String arg0) {
	// TODO Not Implemented
	return null;
    }
}
