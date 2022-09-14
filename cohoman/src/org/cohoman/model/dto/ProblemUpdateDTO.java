package org.cohoman.model.dto;

import java.util.Date;

public class ProblemUpdateDTO {

	private Long problemupdateid;
	private Long problemitemid;
	private Date updateDate;
	private Long itemCreatedBy;
	private String notes;	
	
	private String username;
	private String printableUpdateDate;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPrintableUpdateDate() {
		return printableUpdateDate;
	}
	public void setPrintableUpdateDate(String printableUpdateDate) {
		this.printableUpdateDate = printableUpdateDate;
	}
	
}
