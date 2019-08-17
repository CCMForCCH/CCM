package org.cohoman.model.business.trash;

public class TrashPerson {

	private String unitnumber;
	private String firstname;
	private String username;
	private String trashRole;
	
	public TrashPerson() {}
	
	public String getUnitnumber() {
		return unitnumber;
	}
	public void setUnitnumber(String unitnumber) {
		this.unitnumber = unitnumber;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTrashRole() {
		return trashRole;
	}

	public void setTrashRole(String trashRole) {
		this.trashRole = trashRole;
	}

}
