package org.cohoman.model.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MealEventDTO {

	private Long eventid;
	private Date eventDate;
	private Date eventdateend;
	private String eventinfo;
	private List<String> spaceList = null;	

	private String menu = null;
	private Long cook1 = null;
	private Long cook2 = null;
	private Long cook3 = null;
	private Long cook4 = null;
	private Long cleaner1 = null;
	private Long cleaner2 = null;
	private Long cleaner3 = null;
	private int maxattendees = 0;
	private boolean ismealclosed = false;
	
	
	public Long getEventid() {
		return eventid;
	}
	public void setEventid(Long eventid) {
		this.eventid = eventid;
	}
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	public Date getEventdateend() {
		return eventdateend;
	}
	public void setEventdateend(Date eventdateend) {
		this.eventdateend = eventdateend;
	}
	public String getEventinfo() {
		return eventinfo;
	}
	public void setEventinfo(String eventinfo) {
		this.eventinfo = eventinfo;
	}
	public String getMenu() {
		return menu;
	}
	public void setMenu(String menu) {
		this.menu = menu;
	}
	
	public Long getCook1() {
		return cook1;
	}
	public void setCook1(Long cook1) {
		this.cook1 = cook1;
	}
	public Long getCook2() {
		return cook2;
	}
	public void setCook2(Long cook2) {
		this.cook2 = cook2;
	}
	public Long getCook3() {
		return cook3;
	}
	public void setCook3(Long cook3) {
		this.cook3 = cook3;
	}
	public Long getCook4() {
		return cook4;
	}
	public void setCook4(Long cook4) {
		this.cook4 = cook4;
	}
	public Long getCleaner1() {
		return cleaner1;
	}
	public void setCleaner1(Long cleaner1) {
		this.cleaner1 = cleaner1;
	}
	public Long getCleaner2() {
		return cleaner2;
	}
	public void setCleaner2(Long cleaner2) {
		this.cleaner2 = cleaner2;
	}
	public Long getCleaner3() {
		return cleaner3;
	}
	public void setCleaner3(Long cleaner3) {
		this.cleaner3 = cleaner3;
	}
	public int getMaxattendees() {
		return maxattendees;
	}
	public void setMaxattendees(int maxattendees) {
		this.maxattendees = maxattendees;
	}
	public boolean isIsmealclosed() {
		return ismealclosed;
	}
	public void setIsmealclosed(boolean ismealclosed) {
		this.ismealclosed = ismealclosed;
	}
	
	public List<String> getSpaceList() {
		List<String> localList = new ArrayList<String>();
		//TODO: don't hard-code space numbers
		localList.add("1");
		localList.add("2");
		return localList;
	}
	public void setSpaceList(List<String> spaceList) {
		this.spaceList = spaceList;
	}
	
}
