package org.ccci.gto.authorization;

import java.util.List;

import org.ccci.gto.authorization.exception.ProcessingException;

public interface Processor {
    public List<Response> process(Commands commands) throws ProcessingException;
}
