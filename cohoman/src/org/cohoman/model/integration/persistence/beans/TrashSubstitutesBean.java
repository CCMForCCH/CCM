package org.cohoman.model.integration.persistence.beans;

import java.util.Date;

public class TrashSubstitutesBean {

	private Long substitutesid = null;
	private Date startingdate;
	private String origusername;
	private String substituteusername;
	
	public TrashSubstitutesBean() {
		
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

	public String getOrigusername() {
		return origusername;
	}

	public void setOrigusername(String origusername) {
		this.origusername = origusername;
	}

	public String getSubstituteusername() {
		return substituteusername;
	}

	public void setSubstituteusername(String substituteusername) {
		this.substituteusername = substituteusername;
	}
	
}
