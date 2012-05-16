package org.ccci.gto.authorization;

import java.util.Collection;

import org.ccci.gto.authorization.object.Namespace;
import org.ccci.gto.authorization.object.Target;

public interface Response<T extends Command> {
    public T getCommand();

    /**
     * @return the code
     */
    public Integer getCode();

    public boolean isAuthorized(final int index);

    public boolean isAuthorized(final Target target);

    public boolean areAllAuthorized();

    public Collection<? extends AuthzObject> getObjects();

    public Collection<Namespace> getNamespaces();
}
