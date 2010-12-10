package org.ccci.gcx.authorization;

import java.util.List;

import org.ccci.gcx.authorization.exception.ProcessingException;

public interface Processor {
    public List<Response> process(Commands commands) throws ProcessingException;
}
