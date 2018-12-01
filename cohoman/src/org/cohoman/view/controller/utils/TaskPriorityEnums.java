package org.cohoman.view.controller.utils;

public enum TaskPriorityEnums {
	
	HIGH("High"), MEDIUM("Medium"), LOW("Low");
	
    private final String text;

    private TaskPriorityEnums(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }

}
