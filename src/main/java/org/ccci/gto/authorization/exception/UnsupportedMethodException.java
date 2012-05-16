package org.ccci.gto.authorization.exception;

public final class UnsupportedMethodException extends RuntimeException {
    private static final long serialVersionUID = 1092431306420079846L;

    public UnsupportedMethodException() {
    }

    public UnsupportedMethodException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnsupportedMethodException(final String message) {
        super(message);
    }

    public UnsupportedMethodException(final Throwable cause) {
        super(cause);
    }
}
