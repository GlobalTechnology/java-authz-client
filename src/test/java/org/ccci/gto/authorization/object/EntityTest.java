package org.ccci.gto.authorization.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class EntityTest {
    @Test
    public void testConstructors() {
        // valid entities
        {
            final Entity expected = new Entity(new Namespace("namespace"), "group");
            assertEquals(expected, new Entity("namespace", "group"));
        }

        // invalid entities
        {
            // no namespace
            try {
                new Entity((Namespace) null, "a");
            } catch (final IllegalArgumentException e) {
            }

            // no name
            try {
                new Entity("namespace", "");
                fail();
            } catch (final IllegalArgumentException e) {
            }

            // name containing |
            try {
                new Entity("", "b|c");
                fail();
            } catch (final IllegalArgumentException e) {
            }
            try {
                new Entity(Namespace.ROOT, "b|c");
                fail();
            } catch (final IllegalArgumentException e) {
            }
        }
    }
}
