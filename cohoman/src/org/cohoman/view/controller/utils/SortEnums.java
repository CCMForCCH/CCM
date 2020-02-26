package org.cohoman.view.controller.utils;

public enum SortEnums {
	
	ORDERBYNAME("OrderByName"), ORDERBYNEXTSERVICEDATE("OrderByNextServiceDate");
	
    private final String text;

    private SortEnums(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }

}
