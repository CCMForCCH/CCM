package org.cohoman.view.controller.utils;

public enum PotluckCategoriesEnums {
	
	ENTREES("Entrees"), SIDES("Sides"), SALADS("Salads"), DESSERTS("Desserts"), OTHERS("Others");
	
    private final String text;

    private PotluckCategoriesEnums(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }

}
