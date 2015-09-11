package model.exception;

public class InvalidPlayerIdException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 396564238204759407L;

	public InvalidPlayerIdException(String playerId) {
		super("Invalid player identity " + playerId);
	}

	public InvalidPlayerIdException(String playerId, Throwable cause) {
		super("Invalid player identity " + playerId, cause);
	}

}
