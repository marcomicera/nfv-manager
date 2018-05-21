package it.polito.dp2.NFV.lab3;

public class NoNodeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4695069900727696020L;

	public NoNodeException() {
	}

	public NoNodeException(String message) {
		super(message);
	}

	public NoNodeException(Throwable cause) {
		super(cause);
	}

	public NoNodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoNodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
