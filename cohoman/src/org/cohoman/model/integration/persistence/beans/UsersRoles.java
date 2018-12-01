package org.cohoman.model.integration.persistence.beans;

import java.io.Serializable;

public class UsersRoles implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 321321565461880117L;

	private long usersrolesid;
	private long userid;
	private long roleid;
	
	public long getUsersrolesid() {
		return usersrolesid;
	}
	public void setUsersrolesid(long usersrolesid) {
		this.usersrolesid = usersrolesid;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public long getRoleid() {
		return roleid;
	}
	public void setRoleid(long roleid) {
		this.roleid = roleid;
	}
	
}
