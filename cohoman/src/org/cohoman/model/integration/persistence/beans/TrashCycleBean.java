package org.cohoman.model.integration.persistence.beans;

import java.sql.Date;


public class TrashCycleBean {

	private Long trashcycleid;
	private Date trashcyclestartdate;
	private Date trashcyclelastdate;
	private String nextuseridtoskip;
	
	
	public Long getTrashcycleid() {
		return trashcycleid;
	}
	public void setTrashcycleid(Long trashcycleid) {
		this.trashcycleid = trashcycleid;
	}
	public Date getTrashcyclestartdate() {
		return trashcyclestartdate;
	}
	public void setTrashcyclestartdate(Date trashcyclestartdate) {
		this.trashcyclestartdate = trashcyclestartdate;
	}
	public Date getTrashcyclelastdate() {
		return trashcyclelastdate;
	}
	public void setTrashcyclelastdate(Date trashcyclelastdate) {
		this.trashcyclelastdate = trashcyclelastdate;
	}
	public String getNextuseridtoskip() {
		return nextuseridtoskip;
	}
	public void setNextuseridtoskip(String nextuseridtoskip) {
		this.nextuseridtoskip = nextuseridtoskip;
	}	
	
}
