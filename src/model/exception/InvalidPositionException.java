package model.exception;

import model.Point;

public class InvalidPositionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3322862942533742261L;

	public InvalidPositionException(Point position) {
		super("Invalid position : " + position.toString());
	}

	public InvalidPositionException(Point position, String message) {
		super("Invalid position : " + position.toString() + "\n" + message);
	}

	public InvalidPositionException(Point position, Throwable cause) {
		super("Invalid position : " + position.toString(), cause);
	}

	public InvalidPositionException(Point position, String message, Throwable cause) {
		super("Invalid position : " + position.toString() + "\n" + message, cause);
	}

}
