package org.cohoman.model.integration.persistence.beans;

import java.sql.Date;


public class SecurityStartingPointBean {

	private String section;
	private String unitid;
	private Date startdate;
	
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getUnitid() {
		return unitid;
	}
	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	
}
