package org.cohoman.model.business;

import java.util.Date;

public class User {

	private Long userid = null;
	private String username;
	private String firstname;
	private String lastname;
	private String homephone;
	private String cellphone;
	private String workphone;
	private String email;
	private String unit;
	private String password;
	private Date lastlogin;
	private String emergencyinfo;
	private String foodrestrictions;
	private int birthday;
	private int birthmonth;
	private int birthyear;
	private String usertype;
	private String trashrole;
	private boolean allowtexting;
		
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
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getLastlogin() {
		return lastlogin;
	}
	public void setLastlogin(Date lastlogin) {
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
	public String getTrashrole() {
		return trashrole;
	}
	public void setTrashrole(String trashrole) {
		this.trashrole = trashrole;
	}
	public boolean isAllowtexting() {
		return allowtexting;
	}
	public void setAllowtexting(boolean allowtexting) {
		this.allowtexting = allowtexting;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (allowtexting ? 1231 : 1237);
		result = prime * result + birthday;
		result = prime * result + birthmonth;
		result = prime * result + birthyear;
		result = prime * result
				+ ((cellphone == null) ? 0 : cellphone.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((emergencyinfo == null) ? 0 : emergencyinfo.hashCode());
		result = prime * result
				+ ((firstname == null) ? 0 : firstname.hashCode());
		result = prime
				* result
				+ ((foodrestrictions == null) ? 0 : foodrestrictions.hashCode());
		result = prime * result
				+ ((homephone == null) ? 0 : homephone.hashCode());
		result = prime * result
				+ ((lastlogin == null) ? 0 : lastlogin.hashCode());
		result = prime * result
				+ ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((trashrole == null) ? 0 : trashrole.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime * result + ((userid == null) ? 0 : userid.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		result = prime * result
				+ ((usertype == null) ? 0 : usertype.hashCode());
		result = prime * result
				+ ((workphone == null) ? 0 : workphone.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (allowtexting != other.allowtexting)
			return false;
		if (birthday != other.birthday)
			return false;
		if (birthmonth != other.birthmonth)
			return false;
		if (birthyear != other.birthyear)
			return false;
		if (cellphone == null) {
			if (other.cellphone != null)
				return false;
		} else if (!cellphone.equals(other.cellphone))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (emergencyinfo == null) {
			if (other.emergencyinfo != null)
				return false;
		} else if (!emergencyinfo.equals(other.emergencyinfo))
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (foodrestrictions == null) {
			if (other.foodrestrictions != null)
				return false;
		} else if (!foodrestrictions.equals(other.foodrestrictions))
			return false;
		if (homephone == null) {
			if (other.homephone != null)
				return false;
		} else if (!homephone.equals(other.homephone))
			return false;
		if (lastlogin == null) {
			if (other.lastlogin != null)
				return false;
		} else if (!lastlogin.equals(other.lastlogin))
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (trashrole == null) {
			if (other.trashrole != null)
				return false;
		} else if (!trashrole.equals(other.trashrole))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		if (userid == null) {
			if (other.userid != null)
				return false;
		} else if (!userid.equals(other.userid))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (usertype == null) {
			if (other.usertype != null)
				return false;
		} else if (!usertype.equals(other.usertype))
			return false;
		if (workphone == null) {
			if (other.workphone != null)
				return false;
		} else if (!workphone.equals(other.workphone))
			return false;
		return true;
	}	
	
}
