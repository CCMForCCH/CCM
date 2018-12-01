package org.cohoman.model.integration.persistence.beans;

import java.util.Date;

public class CohoEventBean extends EventBean {

	private String eventName = null;
	private Long enteredby = null;
	
	//private Set<SpaceBean> spaceList = null;
	//private Set<String> chosenSpaceListInts = null;

	public CohoEventBean(Date eventDate) {
		super(eventDate);
	}

	//TODO make sense????
	public CohoEventBean() {
		super(new Date());
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Long getEnteredby() {
		return enteredby;
	}

	public void setEnteredby(Long enteredby) {
		this.enteredby = enteredby;
	}

}
