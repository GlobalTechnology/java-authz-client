package org.ccci.gto.authorization;

import org.ccci.gto.authorization.exception.ProcessingException;
import org.ccci.gto.authorization.object.Namespace;

public interface Controller {
    public Commands process(final Commands commands) throws ProcessingException;

    public Namespace baseNamespace();
}
