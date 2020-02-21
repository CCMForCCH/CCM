package org.cohoman.view.controller.utils;

public enum TaskStatusEnums {
	
	UPTODATE("Uptodate"), OVERDUE("Overdue"), INPROGRESS("InProgress");
	
    private final String text;

    private TaskStatusEnums(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }

}
