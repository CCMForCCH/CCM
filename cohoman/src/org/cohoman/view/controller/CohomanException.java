package org.cohoman.view.controller;

public class CohomanException extends Exception {

	private String errorText;
	
	public CohomanException(String errorText) {
		this.errorText = errorText;
	}
	
	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
	
}
