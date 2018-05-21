package it.polito.dp2.NFV.lab3;

public class AllocationException extends Exception {

	/**
	 * Thrown when an error related to allocation occurs
	 */
	private static final long serialVersionUID = 1L;

	public AllocationException() {
	}

	public AllocationException(String message) {
		super(message);
	}

	public AllocationException(Throwable cause) {
		super(cause);
	}

	public AllocationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AllocationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
