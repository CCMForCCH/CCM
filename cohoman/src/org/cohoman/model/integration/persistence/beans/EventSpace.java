package org.cohoman.model.integration.persistence.beans;

import java.io.Serializable;

public class EventSpace implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 321321565461880117L;

	private Long eventspaceid;
	private Long eventid;
	private String eventtype;
	private Long spaceId;
	
	public Long getEventspaceid() {
		return eventspaceid;
	}
	public void setEventspaceid(Long eventspaceid) {
		this.eventspaceid = eventspaceid;
	}
	public Long getEventid() {
		return eventid;
	}
	public void setEventid(Long eventid) {
		this.eventid = eventid;
	}
	public String getEventtype() {
		return eventtype;
	}
	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}
	public Long getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
		
}
