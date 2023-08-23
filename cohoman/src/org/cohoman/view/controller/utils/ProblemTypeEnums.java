package org.cohoman.view.controller.utils;

public enum ProblemTypeEnums {
	
	ADMINISTRATIVE("Administrative"),
	BUILDINGEX("Building Exterior"),
	BUILDINGIN("Building Interior"),
	ELECTRICAL("Electrical"),
	ELEVATOR("Elevator"),
	FINANCIAL("Financial"),
	FIREALARM("Fire Alarm System"),
	HEATINGCOOLING("Heating/Cooling"),
	HOTWATER("Hot water"), 
	LANDSCAPING("Landscaping"),
	LAUNDRYMACHINES("Laundry Machines"),
	LIGHTING("Lighting"),
	MECHANICAL("Mechanical"),
	PERIODICSERVICES("Periodic Services"),
	PESTCONTROL("Pest Control"),
	PLUMBING("Plumbing"),
	SNOWREMOVAL("Snow Removal"),
	TRASH("Trash"),
	NONEOFTHEABOVE("None of the above");
	
    private final String text;

    private ProblemTypeEnums(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }

}
