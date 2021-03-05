package edu.uark.registerapp.commands.exceptions;

public class UnprocessableEntityException extends RuntimeException {
	public UnprocessableEntityException(String parameterName) {
		super("'Functionality for Employee creation has not yet been implemented.'".concat(parameterName));
	}

	private static final long serialVersionUID = 6128380142041898414L;
}
