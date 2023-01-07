package org.cohoman.view.controller.utils;

public enum ProblemStatusEnums {
	
	NEW("New"),
	//ASSIGNED("Assigned"),
	INPROGRESS("In-Progress"),
	CLOSED("Closed"),
	COMPLETED("Completed"); 
	
    private final String text;

    private ProblemStatusEnums(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }

}
