package com.hepolite.api.exception;

public final class ArgumentParseException extends RuntimeException
{
	private static final long serialVersionUID = 6534997834680158301L;

	public ArgumentParseException()
	{
		super();
	}
	public ArgumentParseException(final String message)
	{
		super(message);
	}
}
