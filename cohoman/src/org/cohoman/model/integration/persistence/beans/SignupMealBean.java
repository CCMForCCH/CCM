package org.cohoman.model.integration.persistence.beans;

public class SignupMealBean {

	private Long signupmealid = null;
	private Long eventid = null;
	private Long userid = null;
	private int numberattending = 0;
	
	public SignupMealBean() {
	}

	public Long getSignupmealid() {
		return signupmealid;
	}
	public void setSignupmealid(Long signupmealid) {
		this.signupmealid = signupmealid;
	}
	public Long getEventid() {
		return eventid;
	}
	public void setEventid(Long eventid) {
		this.eventid = eventid;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public int getNumberattending() {
		return numberattending;
	}
	public void setNumberattending(int numberattending) {
		this.numberattending = numberattending;
	}


}
