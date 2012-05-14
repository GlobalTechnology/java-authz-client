package org.ccci.gto.authorization;

public interface Response<T extends Command> {
    public T getCommand();

    /**
     * @return the code
     */
    public Integer getCode();
}
