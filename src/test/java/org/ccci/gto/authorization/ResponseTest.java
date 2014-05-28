package org.ccci.gto.authorization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ResponseTest {
    @Test
    public void testCodeFromCode() throws Exception {
        for (final Response.Code code : Response.Code.values()) {
            assertEquals(code, Response.Code.fromCode(code.getCode()));
        }
    }
}
