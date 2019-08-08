package org.cohoman.model.business.trash;

public class TrashPerson {

	private String unitnumber;
	private String firstname;
	private Long userid;
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
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getTrashRole() {
		return trashRole;
	}

	public void setTrashRole(String trashRole) {
		this.trashRole = trashRole;
	}

}
