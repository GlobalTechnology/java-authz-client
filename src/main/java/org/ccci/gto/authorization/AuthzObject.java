package org.ccci.gto.authorization;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface AuthzObject {
    public Element toXml(final Document doc);
}
