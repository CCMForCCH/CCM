package org.cohoman.model.dto;

import java.util.Date;

public class MtaskDTO {

	private Long mtaskitemid;
	private Long maintenanceitemid;
	private String vendorname;
	private Date taskstartDate;
	private Date taskendDate;
	private Long itemCreatedBy;
	private String notes;	
	
	private String username;
	private String printableStartdate;
	private String printableEnddate;
	
	
	public Long getMtaskitemid() {
		return mtaskitemid;
	}
	public void setMtaskitemid(Long mtaskitemid) {
		this.mtaskitemid = mtaskitemid;
	}
	public Long getMaintenanceitemid() {
		return maintenanceitemid;
	}
	public void setMaintenanceitemid(Long maintenanceitemid) {
		this.maintenanceitemid = maintenanceitemid;
	}
	public String getVendorname() {
		return vendorname;
	}
	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}
	public Date getTaskstartDate() {
		return taskstartDate;
	}
	public void setTaskstartDate(Date taskstartDate) {
		this.taskstartDate = taskstartDate;
	}
	public Date getTaskendDate() {
		return taskendDate;
	}
	public void setTaskendDate(Date taskendDate) {
		this.taskendDate = taskendDate;
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
	public String getPrintableStartdate() {
		return printableStartdate;
	}
	public void setPrintableStartdate(String printableStartdate) {
		this.printableStartdate = printableStartdate;
	}
	public String getPrintableEnddate() {
		return printableEnddate;
	}
	public void setPrintableEnddate(String printableEnddate) {
		this.printableEnddate = printableEnddate;
	}

}
