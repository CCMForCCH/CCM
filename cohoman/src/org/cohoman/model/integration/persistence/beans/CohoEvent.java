package org.cohoman.model.integration.persistence.beans;

import java.util.Date;
import java.util.Set;

public class CohoEvent extends EventBean {

	private String eventName = null;
	private Long enteredby = null;
	private Set<SpaceBean> spaceList = null;
	private String printableSpaceList = null;
	private Set<String> chosenSpaceListStringInts = null;
	private String enteredbyUsername = null;

	public CohoEvent(Date eventDate) {
		super(eventDate);
	}

	// TODO make sense????
	public CohoEvent() {
		super(new Date());
	}

	public int getUsableEventid() {
		return super.getEventid().intValue();
	}

	public String getChoosableEventDate() {
		String eventCandidate = super.getPrintableEventDate() + " " + eventName;
		return eventCandidate;
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

	public String getEnteredbyUsername() {
		return enteredbyUsername;
	}

	public void setEnteredbyUsername(String enteredbyUsername) {
		this.enteredbyUsername = enteredbyUsername;
	}

	public String getPrintableSpaceList() {
		return printableSpaceList;
	}

	public void setPrintableSpaceList(String printableSpaceList) {
		this.printableSpaceList = printableSpaceList;
	}

	public Set<SpaceBean> getSpaceList() {
		return spaceList;
	}

	public void setSpaceList(Set<SpaceBean> spaceList) {
		this.spaceList = spaceList;
	}

	public Set<String> getChosenSpaceListStringInts() {
		return chosenSpaceListStringInts;
	}

	public void setChosenSpaceListStringInts(
			Set<String> chosenSpaceListStringInts) {
		this.chosenSpaceListStringInts = chosenSpaceListStringInts;
	}

}
