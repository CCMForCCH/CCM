package org.cohoman.view.controller.utils;

public enum ProblemLocationEnums {
	
	EE("East End"),
	BASEMENTEE("East End, Basement"),
	BUILDINGENVEE("East End, Building Envelope"),
	COMMONBREE("East End, Common Bathroom"),
	HALLWAYEE("East End, Hallway"),
	LAUNDRYEE("East End, Laundry Room"),
	OUTSIDEEE("East End, Outside"),
	WORKSHOP("East End, Workshop"),
	MYUNIT("My Unit"),
	SPINE("Spine"),
	WE("West End"),
	BASEMENTWE("West End, Basement"),
	BUILDINGENVWE("West End, Building Envelope"),
	DINING("West End, Dining Room"),
	ELEVATOR("West End, Elevator"),
	EXERCISE("West End, Exercise Room"),
	GARAGE("West End, Garage"),
	GUESTBATHROOM("West End, Guest Rooms Bathroom"),
	GUESTROOMS("West End, Guest Rooms"),
	HALLWAYWE("West End, Hallway"),
	HVACROOM("West End, HVAC Room"),
	COMMONBRKIDS("West End, Kids Bathroom"),
	KIDS("West End, Kids Room"),
	KITCHEN("West End, Kitchen"),
	LAUNDRYWE("West End, Laundry Room"),
	LIBRARY("West End, Library"),
	LIVINGROOM("West End, Living Room"),
	LOBBY("West End, Lobby"),
	COMMONBRLOB("West End, Lobby, Common Bathroom"),
	MAILROOM("West End, Mailroom"),
	OUTSIDEWE("West End, Outside"),
	RECROOM("West End, Recreation Room"),
	COMMONBRREC("West End, Recreation Room, Bathroom"),
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
