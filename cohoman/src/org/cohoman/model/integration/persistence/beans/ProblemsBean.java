package org.cohoman.model.integration.persistence.beans;

import java.util.Date;

public class ProblemsBean {
	
	private Long problemitemid;
	private String itemname;
	private String itemdescription;
	private Date itemCreatedDate;
	private Long itemCreatedBy;
	private String priority;
	private String problemStatus;
	private String problemType;
	private String vendor;
	private String duplicateproblemitemname;
	private String location;
	private Long assignedTo;
	private String cost;
	private String invoiceLink;
	private String invoiceNumber;
	private Date itemCompletedDate;

	public ProblemsBean() {
		
	}

	public Long getProblemitemid() {
		return problemitemid;
	}

	public void setProblemitemid(Long problemitemid) {
		this.problemitemid = problemitemid;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getItemdescription() {
		return itemdescription;
	}

	public void setItemdescription(String itemdescription) {
		this.itemdescription = itemdescription;
	}

	public Date getItemCreatedDate() {
		return itemCreatedDate;
	}

	public void setItemCreatedDate(Date itemCreatedDate) {
		this.itemCreatedDate = itemCreatedDate;
	}

	public Long getItemCreatedBy() {
		return itemCreatedBy;
	}

	public void setItemCreatedBy(Long itemCreatedBy) {
		this.itemCreatedBy = itemCreatedBy;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getProblemStatus() {
		return problemStatus;
	}

	public void setProblemStatus(String problemStatus) {
		this.problemStatus = problemStatus;
	}

	public String getProblemType() {
		return problemType;
	}

	public void setProblemType(String problemType) {
		this.problemType = problemType;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getDuplicateproblemitemname() {
		return duplicateproblemitemname;
	}

	public void setDuplicateproblemitemname(String duplicateproblemitemname) {
		this.duplicateproblemitemname = duplicateproblemitemname;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(Long assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getInvoiceLink() {
		return invoiceLink;
	}

	public void setInvoiceLink(String invoiceLink) {
		this.invoiceLink = invoiceLink;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getItemCompletedDate() {
		return itemCompletedDate;
	}

	public void setItemCompletedDate(Date itemCompletedDate) {
		this.itemCompletedDate = itemCompletedDate;
	}
	

}
