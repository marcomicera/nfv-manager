package it.polito.dp2.NFV.lab3;

public class LinkAlreadyPresentException extends Exception {

	/**
	 * Indicates the exceptional condition that a link is already present
	 */
	private static final long serialVersionUID = -5086751842696710767L;

	public LinkAlreadyPresentException() {
	}

	public LinkAlreadyPresentException(String message) {
		super(message);
	}

	public LinkAlreadyPresentException(Throwable cause) {
		super(cause);
	}

	public LinkAlreadyPresentException(String message, Throwable cause) {
		super(message, cause);
	}

	public LinkAlreadyPresentException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
