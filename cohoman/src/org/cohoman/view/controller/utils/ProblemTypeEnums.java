package org.cohoman.view.controller.utils;

public enum ProblemTypeEnums {
	
	ADMINISTRATIVE("Administrative"),
	BUILDINGEX("Building Exterior"),
	BUILDINGIN("Building Interior"),
	ELECTRICAL("Electrical"),
	FINANCIAL("Financial"),
	FIREALARM("Fire Alarm System"),
	HEATINGCOOLING("Heating/Cooling"),
	HOTWATER("Hot water"),
	MECHANICAL("Mechanical"), 
	OTHER("Other"),
	PERIODICSERVICES("Periodic Services"),
	PLUMBING("Plumbing");
	
    private final String text;

    private ProblemTypeEnums(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }

}
