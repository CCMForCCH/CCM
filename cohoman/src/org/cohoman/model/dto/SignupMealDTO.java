package org.cohoman.model.dto;

public class SignupMealDTO {

	private Long eventid;
	private Long userid;
	private int numberattending;
	private String username;
	private String foodrestrictions;
	private String unitnumber;

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

	public Long getEventid() {
		return eventid;
	}

	public void setEventid(Long eventid) {
		this.eventid = eventid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFoodrestrictions() {
		return foodrestrictions;
	}

	public void setFoodrestrictions(String foodrestrictions) {
		this.foodrestrictions = foodrestrictions;
	}

	public String getUnitnumber() {
		return unitnumber;
	}

	public void setUnitnumber(String unitnumber) {
		this.unitnumber = unitnumber;
	}

}
