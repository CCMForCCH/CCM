package org.cohoman.view.controller.utils;

public enum ProblemPriorityEnums {
	
	P1CRITICAL("Critical"), 
	P2EMERGENCY("Emergency"), 
	P3HIGH("High"), 
	P4MEDIUM("Medium"), 
	P5LOW("Low");
	
    private final String text;

    private ProblemPriorityEnums(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }

}
