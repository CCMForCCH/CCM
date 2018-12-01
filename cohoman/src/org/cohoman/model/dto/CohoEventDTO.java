package org.cohoman.model.dto;

import java.util.Date;
import java.util.List;

public class CohoEventDTO {

	private Long eventid;
	private Date eventDate;
	private Date eventdateend;
	private String eventinfo;

	private String eventName = null;
	private String eventtype;
	private Long enteredby;
	
	private List<String> spaceList = null;	

	public Long getEventid() {
		return eventid;
	}

	public void setEventid(Long eventid) {
		this.eventid = eventid;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public Date getEventdateend() {
		return eventdateend;
	}

	public void setEventdateend(Date eventdateend) {
		this.eventdateend = eventdateend;
	}

	public String getEventinfo() {
		return eventinfo;
	}

	public void setEventinfo(String eventinfo) {
		this.eventinfo = eventinfo;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public String getEventtype() {
		return eventtype;
	}

	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}

	public Long getEnteredby() {
		return enteredby;
	}

	public void setEnteredby(Long enteredby) {
		this.enteredby = enteredby;
	}

	public List<String> getSpaceList() {
		return spaceList;
	}

	public void setSpaceList(List<String> spaceList) {
		this.spaceList = spaceList;
	}
	
}
