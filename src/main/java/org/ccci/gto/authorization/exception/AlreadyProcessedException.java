package org.ccci.gto.authorization.exception;

public class AlreadyProcessedException extends ProcessingException {
    private static final long serialVersionUID = 1200500588747952902L;

    public AlreadyProcessedException(final String message) {
	super(message);
    }
}
