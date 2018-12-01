package org.cohoman.model.integration.persistence.beans;

import java.sql.Date;


public class TimePeriodBean {

	private Long timeperiodid;
	private String timeperiodtypeenum;
	private Date periodstartdate;
	private Date periodenddate;
	
	public Long getTimeperiodid() {
		return timeperiodid;
	}
	public void setTimeperiodid(Long timeperiodid) {
		this.timeperiodid = timeperiodid;
	}
	public String getTimeperiodtypeenum() {
		return timeperiodtypeenum;
	}
	public void setTimeperiodtypeenum(String timeperiodtypeenum) {
		this.timeperiodtypeenum = timeperiodtypeenum;
	}
	public Date getPeriodstartdate() {
		return periodstartdate;
	}
	public void setPeriodstartdate(Date periodstartdate) {
		this.periodstartdate = periodstartdate;
	}
	public Date getPeriodenddate() {
		return periodenddate;
	}
	public void setPeriodenddate(Date periodenddate) {
		this.periodenddate = periodenddate;
	}
	
	
}
