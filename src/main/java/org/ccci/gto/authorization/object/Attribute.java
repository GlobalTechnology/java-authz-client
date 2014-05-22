package org.ccci.gto.authorization.object;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Objects;

public final class Attribute extends AbstractObject {
    private static final long serialVersionUID = -3420447292079113215L;

    private final String value;

    public Attribute(final Element object) {
        this(object.getAttributeNS(null, "namespace"), object.getAttributeNS(null, "name"),
                (object.hasAttributeNS(null, "value") ? object.getAttributeNS(null, "value") : null));
    }

    public Attribute(final String ns, final String name) {
        this(ns, name, null);
    }

    public Attribute(final String ns, final String name, final String value) {
        this(new Namespace(ns), name, value);
    }

    public Attribute(final Namespace ns, final String name) {
        this(ns, name, null);
    }

    public Attribute(final Namespace ns, final String name, String value) {
        super(ns, name);
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public Element toXml(final Document doc) {
        final Element e = doc.createElementNS(XMLNS_AUTHZ, "attribute");
        e.setAttributeNS(null, "namespace", this.getNamespace().toString());
        e.setAttributeNS(null, "name", this.getName());
        if (this.value != null) {
            e.setAttributeNS(null, "value", this.value);
        }
        return e;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Attribute)) { return false; }

        Attribute attribute = (Attribute) o;

        return super.equals(attribute) && Objects.equals(this.value, attribute.value);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public static class Name extends AbstractObject {
        private static final long serialVersionUID = 3980374626818710777L;

        public Name(final Namespace ns, final String name) {
            super(ns, name);
        }

        public Name(final String ns, final String name) {
            super(ns, name);
        }

        public Name(final Attribute attribute) {
            super(attribute.getNamespace(), attribute.getName());
        }

        @Override
        public Element toXml(final Document doc) {
            throw new UnsupportedOperationException("toXml is unsupported for Attribute.Name objects");
        }
    }
}
