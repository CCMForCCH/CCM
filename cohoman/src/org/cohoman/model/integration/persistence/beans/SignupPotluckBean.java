package org.cohoman.model.integration.persistence.beans;

public class SignupPotluckBean {

	private Long signuppotluckid = null;
	private Long eventid = null;
	private Long userid = null;
	private int numberattending = 0;
	private String itemtype;
	private String itemdescription;
	
	public SignupPotluckBean() {
	}

	public Long getSignuppotluckid() {
		return signuppotluckid;
	}
	public void setSignuppotluckid(Long signuppotluckid) {
		this.signuppotluckid = signuppotluckid;
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
	public String getItemtype() {
		return itemtype;
	}

	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}

	public String getItemdescription() {
		return itemdescription;
	}
	public void setItemdescription(String itemdescription) {
		this.itemdescription = itemdescription;
	}
	
}
