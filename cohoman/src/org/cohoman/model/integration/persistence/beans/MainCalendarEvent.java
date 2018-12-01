package org.cohoman.model.integration.persistence.beans;

import java.util.Date;

public class MainCalendarEvent implements Comparable<MainCalendarEvent>{

	private String eventType;
	private Date startDate;
	private Date endDate;
	private String eventInfo;
	private String eventName;
	private String thisweekPrintableDay;
	private String thisweekPrintableTime;
	private String eventlink;
	
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}	
	public String getEventInfo() {
		return eventInfo;
	}
	public void setEventInfo(String eventInfo) {
		this.eventInfo = eventInfo;
	}
	public String getThisweekPrintableDay() {
		return thisweekPrintableDay;
	}
	public void setThisweekPrintableDay(String thisweekPrintableDay) {
		this.thisweekPrintableDay = thisweekPrintableDay;
	}
	public String getThisweekPrintableTime() {
		return thisweekPrintableTime;
	}
	public void setThisweekPrintableTime(String thisweekPrintableTime) {
		this.thisweekPrintableTime = thisweekPrintableTime;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventlink() {
		return eventlink;
	}
	public void setEventlink(String eventlink) {
		this.eventlink = eventlink;
	}
	public int compareTo(MainCalendarEvent mainCalendarEvent) {
		return startDate.compareTo(mainCalendarEvent.getStartDate());
	}
}
