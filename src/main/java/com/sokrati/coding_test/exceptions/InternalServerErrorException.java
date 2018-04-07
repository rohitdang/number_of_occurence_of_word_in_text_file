package com.sokrati.coding_test.exceptions;

public class InternalServerErrorException extends Exception {
	
	public InternalServerErrorException(final String sErrorMessage)
    {
        super(sErrorMessage);
    }
	
	public InternalServerErrorException(final String message, final Exception e) 
	{
		super(message, e);
	}

	private static final long serialVersionUID = 1L;
}
