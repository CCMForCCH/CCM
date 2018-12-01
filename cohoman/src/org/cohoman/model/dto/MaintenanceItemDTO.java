package org.cohoman.model.dto;

import java.util.Date;

public class MaintenanceItemDTO {

	private Long maintenanceitemid;
	private String itemname;
	private String itemdescription;
	private Date itemCreatedDate;
	private Long itemCreatedBy;
	private String priority;
	private String frequencyOfItem;
	private Date lastperformedDate;
	private String targetTimeOfyear;
	private String username;
	private String printableCreatedDate;
	private String printableLastperformedDate;
	
	
	public Long getMaintenanceitemid() {
		return maintenanceitemid;
	}
	public void setMaintenanceitemid(Long maintenanceitemid) {
		this.maintenanceitemid = maintenanceitemid;
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
	public String getFrequencyOfItem() {
		return frequencyOfItem;
	}
	public void setFrequencyOfItem(String frequencyOfItem) {
		this.frequencyOfItem = frequencyOfItem;
	}
	public Date getLastperformedDate() {
		return lastperformedDate;
	}
	public void setLastperformedDate(Date lastperformedDate) {
		this.lastperformedDate = lastperformedDate;
	}
	public String getTargetTimeOfyear() {
		return targetTimeOfyear;
	}
	public void setTargetTimeOfyear(String targetTimeOfyear) {
		this.targetTimeOfyear = targetTimeOfyear;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPrintableCreatedDate() {
		return printableCreatedDate;
	}
	public void setPrintableCreatedDate(String printableCreatedDate) {
		this.printableCreatedDate = printableCreatedDate;
	}
	public String getPrintableLastperformedDate() {
		return printableLastperformedDate;
	}
	public void setPrintableLastperformedDate(String printableLastperformedDate) {
		this.printableLastperformedDate = printableLastperformedDate;
	}	

}
