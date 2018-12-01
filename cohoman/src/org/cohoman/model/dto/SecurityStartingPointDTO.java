package org.cohoman.model.dto;

import java.sql.Date;

public class SecurityStartingPointDTO {

	private String section;
	private String unitid;
	private String unitnumber;
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
	public String getUnitnumber() {
		return unitnumber;
	}
	public void setUnitnumber(String unitnumber) {
		this.unitnumber = unitnumber;
	}
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

}
