package org.cohoman.model.integration.persistence.beans;


public class UserBean {

	private Long userid = null;
	private String username;
	private String firstname;
	private String lastname;
	private String homephone;
	private String cellphone;
	private String workphone;
	private String email;
	private Long unit;
	private String password;
	private String lastlogin;
	private String emergencyinfo;
	private String foodrestrictions;
	private int birthday;
	private int birthmonth;
	private int birthyear;
	private String usertype;

	public UserBean() {
	}
	
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getHomephone() {
		return homephone;
	}
	public void setHomephone(String homephone) {
		this.homephone = homephone;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getWorkphone() {
		return workphone;
	}
	public void setWorkphone(String workphone) {
		this.workphone = workphone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getUnit() {
		return unit;
	}

	public void setUnit(Long unit) {
		this.unit = unit;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLastlogin() {
		return lastlogin;
	}
	public void setLastlogin(String lastlogin) {
		this.lastlogin = lastlogin;
	}

	public String getEmergencyinfo() {
		return emergencyinfo;
	}

	public void setEmergencyinfo(String emergencyinfo) {
		this.emergencyinfo = emergencyinfo;
	}

	public String getFoodrestrictions() {
		return foodrestrictions;
	}

	public void setFoodrestrictions(String foodrestrictions) {
		this.foodrestrictions = foodrestrictions;
	}

	public int getBirthday() {
		return birthday;
	}

	public void setBirthday(int birthday) {
		this.birthday = birthday;
	}

	public int getBirthmonth() {
		return birthmonth;
	}

	public void setBirthmonth(int birthmonth) {
		this.birthmonth = birthmonth;
	}

	public int getBirthyear() {
		return birthyear;
	}

	public void setBirthyear(int birthyear) {
		this.birthyear = birthyear;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

}
