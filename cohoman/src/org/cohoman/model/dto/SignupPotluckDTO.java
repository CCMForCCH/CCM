package org.cohoman.model.dto;

public class SignupPotluckDTO {

	private Long eventid = null;
	private Long userid = null;
	private int numberattending = 0;
	private String username;
	private String unitnumber;
	private String itemtype;
	private String itemdescription;
	
	
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUnitnumber() {
		return unitnumber;
	}
	public void setUnitnumber(String unitnumber) {
		this.unitnumber = unitnumber;
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
