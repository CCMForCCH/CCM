package org.cohoman.view.controller.utils;

public enum ProblemStateEnums {
	
	PROBLEMISACTIVE("ProblemIsActive"), 
	PROBLEMISINACTIVE("ProblemIsInactive"),
	ALLPROBLEMS("AllProblems");
	
    private final String text;

    private ProblemStateEnums(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }

}
