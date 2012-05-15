package org.ccci.gto.authorization.exception;

public class InvalidXmlException extends Exception {
    private static final long serialVersionUID = 3583299168399118441L;

    public InvalidXmlException(final String error) {
	super(error);
    }

    public InvalidXmlException(final Throwable exception) {
	super(exception);
    }

    public InvalidXmlException(final String error, final Throwable exception) {
        super(error, exception);
    }
}
