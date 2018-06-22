package it.polito.dp2.NFV.sol3.service.database;

public class MalformedException extends Exception {

	/**
	 * Thrown when an object is not well-formed compared to its schema.
	 */
	private static final long serialVersionUID = 1L;
	
	public MalformedException() {
	}

	public MalformedException(String message) {
		super(message);
	}

	public MalformedException(Throwable cause) {
		super(cause);
	}

	public MalformedException(String message, Throwable cause) {
		super(message, cause);
	}

	public MalformedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
