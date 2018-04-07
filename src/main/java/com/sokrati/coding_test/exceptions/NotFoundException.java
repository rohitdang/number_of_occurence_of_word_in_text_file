package com.sokrati.coding_test.exceptions;

public class NotFoundException extends Exception {
	public NotFoundException(final String sErrorMessage, final Throwable errorObject) {
		super(sErrorMessage, errorObject);
		
	}

	private static final long serialVersionUID = 1L;
}
