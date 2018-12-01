package org.cohoman.model.integration.persistence.beans;

import java.util.Date;

public class SubstitutesBean {

	private Long substitutesid = null;
	private Date startingdate;
	private Long userid;
	
	public SubstitutesBean() {
		
	}

	public Long getSubstitutesid() {
		return substitutesid;
	}

	public void setSubstitutesid(Long substitutesid) {
		this.substitutesid = substitutesid;
	}

	public Date getStartingdate() {
		return startingdate;
	}

	public void setStartingdate(Date startingdate) {
		this.startingdate = startingdate;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}
	
}
