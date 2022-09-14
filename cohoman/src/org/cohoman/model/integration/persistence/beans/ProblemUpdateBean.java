package org.cohoman.model.integration.persistence.beans;

import java.util.Date;

public class ProblemUpdateBean {
	
	private Long problemupdateid;
	private Long problemitemid;
	private Date updateDate;
	private Long itemCreatedBy;
	private String notes;	

	public ProblemUpdateBean() {
		
	}

	public Long getProblemupdateid() {
		return problemupdateid;
	}

	public void setProblemupdateid(Long problemupdateid) {
		this.problemupdateid = problemupdateid;
	}

	public Long getProblemitemid() {
		return problemitemid;
	}

	public void setProblemitemid(Long problemitemid) {
		this.problemitemid = problemitemid;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getItemCreatedBy() {
		return itemCreatedBy;
	}

	public void setItemCreatedBy(Long itemCreatedBy) {
		this.itemCreatedBy = itemCreatedBy;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	
}
