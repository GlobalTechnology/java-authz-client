package org.ccci.gto.authorization.exception;

public class InvalidCommandException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidCommandException(final Exception e) {
	super(e);
    }
}
