package org.cohoman.view.controller.utils;

public enum ProblemLocationEnums {
	
	BASEMENTEE("Basement East End"),
	BASEMENTWE("Basement West End"),
	BUILDINGENVEE("Building Envelope East End"),
	BUILDINGENVWE("Building Envelope West End"),
	COMMONBREE("Common Bathroom, East End"),
	COMMONBRKIDS("Common Bathroom, Kids Room"),
	COMMONBRLOB("Common Bathroom, Lobby"),
	COMMONBRREC("Common Bathroom, Rec Room"),
	DINING("Dining Room"),
	ELEVATOR("Elevator"),
	EXERCISE("Exercise Room"),
	GARAGE("Garage"),
	HALLWAYEE("Hallway, East End"),
	HALLWAYWE("Hallway, West End"),
	HVACROOM("HVAC Room"),
	KIDS("Kids Room"),
	KITCHEN("Kitchen"),
	LAUNDRYEE("East End Laundry Room"),
	LAUNDRYWE("West End Laundry Room"),
	LIBRARY("Library"),
	LIVINGROOM("Living Room"),
	LOBBY("Lobby"),
	MAILROOM("Mailroom"),
	MYUNIT("My Unit"),
	OUTSIDEEE("Outside East End"),
	OUTSIDEWE("Outside West End"),
	RECROOM("Recreation Room"),
	SPINE("Spine"),
	WORKSHOP("Workshop"),
	OTHER("Other"),
	NA("Not Applicable");
	
    private final String text;

    private ProblemLocationEnums(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }

}
