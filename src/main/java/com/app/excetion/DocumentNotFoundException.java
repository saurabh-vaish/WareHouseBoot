package com.app.excetion;

public class DocumentNotFoundException extends RuntimeException{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7740738876644315645L;

	public DocumentNotFoundException(String msg) {

		super(msg);
	}
}
