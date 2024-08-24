package org.cohoman.model.dto;

import java.util.Date;

public class ProblemItemDTO {

	private Long problemitemid;
	private String itemname;
	private String itemdescription;
	private Date itemCreatedDate;
	private Long itemCreatedBy;
	private String itemCreatedByString;
	private String priority;
	private String priorityToPrint;
	private String printableCreatedDate;
	private String printableCompletedDate;
	private String problemStatus;
	private String problemStatusToPrint;
	private String problemType;
	private String problemTypeToPrint;
	private String username;
	private String vendor;
	private String duplicateproblemitemname;
	private String location;
	private String locationToPrint;
	private Long assignedTo;
	private String assignedToString;
	private String cost;
	private String invoiceLink;
	private String invoiceNumber;
	private Date itemCompletedDate;
	
	
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
	public String getItemCreatedByString() {
		return itemCreatedByString;
	}
	public void setItemCreatedByString(String itemCreatedByString) {
		this.itemCreatedByString = itemCreatedByString;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getPriorityToPrint() {
		return priorityToPrint;
	}
	public void setPriorityToPrint(String priorityToPrint) {
		this.priorityToPrint = priorityToPrint;
	}
	public String getPrintableCreatedDate() {
		return printableCreatedDate;
	}
	public void setPrintableCreatedDate(String printableCreatedDate) {
		this.printableCreatedDate = printableCreatedDate;
	}
	public String getPrintableCompletedDate() {
		return printableCompletedDate;
	}
	public void setPrintableCompletedDate(String printableCompletedDate) {
		this.printableCompletedDate = printableCompletedDate;
	}
	public String getProblemStatus() {
		return problemStatus;
	}
	public void setProblemStatus(String problemStatus) {
		this.problemStatus = problemStatus;
	}
	public String getProblemStatusToPrint() {
		return problemStatusToPrint;
	}
	public void setProblemStatusToPrint(String problemStatusToPrint) {
		this.problemStatusToPrint = problemStatusToPrint;
	}
	public String getProblemType() {
		return problemType;
	}
	public void setProblemType(String problemType) {
		this.problemType = problemType;
	}
	public String getProblemTypeToPrint() {
		return problemTypeToPrint;
	}
	public void setProblemTypeToPrint(String problemTypeToPrint) {
		this.problemTypeToPrint = problemTypeToPrint;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public String getLocationToPrint() {
		return locationToPrint;
	}
	public void setLocationToPrint(String locationToPrint) {
		this.locationToPrint = locationToPrint;
	}
	public Long getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(Long assignedTo) {
		this.assignedTo = assignedTo;
	}
	public String getAssignedToString() {
		return assignedToString;
	}
	public void setAssignedToString(String assignedToString) {
		this.assignedToString = assignedToString;
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

	public String getComboTypePlusName() {
		return this.problemType + ": " + this.itemname;
	}
}
