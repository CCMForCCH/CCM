package org.cohoman.model.integration.persistence.beans;

import java.util.Date;

//import java.sql.Date;


public class TrashCycleBean {

	private Long trashcycleid;
	private Date trashcyclestartdate;
	private Date trashcycleenddate;
	private String lastuserskipped;
	private String lastunitskipped;
	private String nextusertoskip;
	
	
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
	public Date getTrashcycleenddate() {
		return trashcycleenddate;
	}
	public void setTrashcycleenddate(Date trashcycleenddate) {
		this.trashcycleenddate = trashcycleenddate;
	}
	public String getLastuserskipped() {
		return lastuserskipped;
	}
	public void setLastuserskipped(String lastuserskipped) {
		this.lastuserskipped = lastuserskipped;
	}
	public String getLastunitskipped() {
		return lastunitskipped;
	}
	public void setLastunitskipped(String lastunitskipped) {
		this.lastunitskipped = lastunitskipped;
	}
	public String getNextusertoskip() {
		return nextusertoskip;
	}
	public void setNextusertoskip(String nextusertoskip) {
		this.nextusertoskip = nextusertoskip;
	}
		
}
