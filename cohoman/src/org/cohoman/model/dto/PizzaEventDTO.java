package org.cohoman.model.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PizzaEventDTO {

	private Long eventid;
	private Date eventDate;
	private Date eventdateend;
	private String eventinfo;
	private String eventName;
	private List<String> spaceList = null;

	private Long leader1 = 0L;
	private Long leader2 = null;
	private boolean ismealclosed = false;

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

	public Long getLeader1() {
		return leader1;
	}

	public void setLeader1(Long leader1) {
		this.leader1 = leader1;
	}

	public Long getLeader2() {
		return leader2;
	}

	public void setLeader2(Long leader2) {
		this.leader2 = leader2;
	}

	
	public boolean isIsmealclosed() {
		return ismealclosed;
	}

	public void setIsmealclosed(boolean ismealclosed) {
		this.ismealclosed = ismealclosed;
	}


	public List<String> getSpaceList() {
		List<String> localList = new ArrayList<String>();
		// TODO: don't hard-code space numbers
		localList.add("1");
		localList.add("2");
		return localList;
	}

	public void setSpaceList(List<String> spaceList) {
		this.spaceList = spaceList;
	}

}
