package org.ccci.gto.authorization.object.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.ccci.gto.authorization.object.Attribute;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class AttributeSet extends AbstractSet<Attribute> {
    private final Multimap<Attribute.Name, Attribute> attributes;

    public AttributeSet() {
        this.attributes = HashMultimap.create();
    }

    public AttributeSet(final int expectedSize) {
        this.attributes = HashMultimap.create(expectedSize, 1);
    }

    public AttributeSet(final Collection<Attribute> attributes) {
        this();
        this.addAll(attributes);
    }

    @Override
    public Iterator<Attribute> iterator() {
        return this.attributes.values().iterator();
    }

    @Override
    public int size() {
        return this.attributes.size();
    }

    @Override
    public boolean add(final Attribute attribute) {
        if (attribute != null) {
            return this.attributes.put(new Attribute.Name(attribute), attribute);
        }

        return false;
    }

    public Collection<Attribute> get(final Attribute.Name name) {
        return Collections.unmodifiableCollection(this.attributes.get(name));
    }

    @Override
    public boolean remove(final Object o) {
        if (o instanceof Attribute) {
            final Attribute attribute = (Attribute) o;
            return this.attributes.remove(new Attribute.Name(attribute), attribute);
        }

        return false;
    }

    @Override
    public boolean contains(final Object o) {
        return o instanceof Attribute && this.attributes.containsValue(o);
    }

    @Override
    public boolean isEmpty() {
        return this.attributes.isEmpty();
    }

    @Override
    public void clear() {
        this.attributes.clear();
    }
}
