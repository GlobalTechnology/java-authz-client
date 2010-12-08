package org.ccci.gcx.authorization;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class Command {
    public Element toXml(final Document doc) {
	return doc.createElementNS(AuthzConstants.XMLNS, "command");
    }
}
