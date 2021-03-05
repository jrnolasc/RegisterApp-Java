package edu.uark.registerapp.commands.exceptions;

public class UnauthorizedException extends RuntimeException {
	public UnauthorizedException() { super("Manager Found Proceed With Button Bellow"); }

	private static final long serialVersionUID = 2725536148764655946L;
}
