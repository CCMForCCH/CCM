package org.cohoman.view.controller;

import java.util.List;

import org.cohoman.view.controller.utils.MainEventLink;

public class MainCalendarDay {

	private String dayNumber;
	private List<String> eventStrings;
	private List<MainEventLink> eventLinks;
	
	public String getDayNumber() {
		return dayNumber;
	}
	public void setDayNumber(String dayNumber) {
		this.dayNumber = dayNumber;
	}
	public List<String> getEventStrings() {
		return eventStrings;
	}
	public void setEventStrings(List<String> eventStrings) {
		this.eventStrings = eventStrings;
	}
	public boolean isEmptyOfEvents() {
		if (this.eventStrings == null || this.eventStrings.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
	public List<MainEventLink> getEventLinks() {
		return eventLinks;
	}
	public void setEventLinks(List<MainEventLink> eventLinks) {
		this.eventLinks = eventLinks;
	}
	
}
