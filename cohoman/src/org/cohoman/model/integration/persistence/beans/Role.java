package org.cohoman.model.integration.persistence.beans;

import java.io.Serializable;

public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 321321565461880117L;

	// TODO: decent way of relating this to prepopulated role table
	public static final Long BASICUSER_ID = 1L;
	public static final Long SUPERADMIN_ID = 2L;
	public static final Long SPACEADMIN_ID = 3L;
	public static final Long MEALADMIN_ID = 4L;
	public static final Long HOFELLERADMIN_ID = 5L;
	
	private Long roleid;
	private String rolename;
	private String roledesc;
	
	public Long getRoleid() {
		return roleid;
	}
	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getRoledesc() {
		return roledesc;
	}
	public void setRoledesc(String roledesc) {
		this.roledesc = roledesc;
	}

}
