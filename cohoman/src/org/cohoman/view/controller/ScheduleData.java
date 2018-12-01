package org.cohoman.view.controller;

import java.io.Serializable;

public class ScheduleData implements Serializable {
	
	private static final long serialVersionUID = 3268563773856436524L;
	private static final String[] weekNumbers = 
	{"1", "2", "3", "4", "5", "6", "7"};
	
	public String [] getWeekNumbers() {
		return weekNumbers;
	}
}
