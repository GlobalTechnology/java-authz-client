package org.ccci.gto.authorization;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.Serializable;

public interface AuthzObject extends Serializable {
    public Element toXml(final Document doc);
}
