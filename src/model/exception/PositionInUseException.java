package model.exception;

import model.Point;

public class PositionInUseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4341759293807237220L;

	public PositionInUseException(Point position) {
		super("Position in use by other player : " + position.toString());
	}

	public PositionInUseException(Point position, String message) {
		super("Position in use by other player : " + position.toString() + "\n" + message);
	}

	public PositionInUseException(Point position, Throwable cause) {
		super("Position in use by other player : " + position.toString(), cause);
	}

	public PositionInUseException(Point position, String message, Throwable cause) {
		super("Position in use by other player : " + position.toString() + "\n" + message, cause);
	}

}
