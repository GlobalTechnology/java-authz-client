package org.ccci.gto.authorization;

import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Namespace;
import org.ccci.gto.authorization.object.Target;

import java.io.Serializable;
import java.util.Collection;

public interface Response<T extends Command> extends Serializable {
    public T getCommand();

    /**
     * @return the code
     */
    public Integer getCode();

    public boolean isAuthorized(final int index);

    public boolean isAuthorized(final Target target);

    public boolean areAllAuthorized();

    public Collection<Namespace> getNamespaces();

    public Collection<Entity> getEntities();
}
