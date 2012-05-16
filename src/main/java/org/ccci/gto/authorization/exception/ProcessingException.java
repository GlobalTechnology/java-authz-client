package org.ccci.gto.authorization.exception;

public class ProcessingException extends Exception {
    private static final long serialVersionUID = 4455928029478202083L;

    public ProcessingException() {
    }

    public ProcessingException(final String message) {
        super(message);
    }

    public ProcessingException(final Throwable cause) {
        super(cause);
    }
}
