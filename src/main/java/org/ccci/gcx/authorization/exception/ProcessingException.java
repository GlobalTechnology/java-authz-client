package org.ccci.gcx.authorization.exception;

public class ProcessingException extends Exception {
    private static final long serialVersionUID = 1L;

    public ProcessingException(final String error) {
	super(error);
    }

    public ProcessingException(final Throwable exception) {
	super(exception);
    }
}
