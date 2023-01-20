package org.cohoman.view.controller.utils;

public enum MaintenanceTypeEnums {
	
	HOFELLER("Common Area"), OWNER("Owner"), ALL("All");
	
    private final String text;

    private MaintenanceTypeEnums(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }

}
