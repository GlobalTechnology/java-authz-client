package org.ccci.gto.authorization.exception;

public class InvalidCommandException extends Exception {
    private static final long serialVersionUID = -3986344915893518987L;

    public InvalidCommandException(final Exception e) {
	super(e);
    }
}
