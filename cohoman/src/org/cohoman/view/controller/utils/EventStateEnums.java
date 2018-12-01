package org.cohoman.view.controller.utils;

public enum EventStateEnums {
	
	REQUESTED("Requested"), APPROVED("Approved"), REJECTED("Rejected");
	
    private final String text;

    private EventStateEnums(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }

}
