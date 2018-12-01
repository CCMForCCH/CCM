package org.cohoman.model.integration.persistence.beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public abstract class EventBean implements Comparable<EventBean> {

	private Long eventid;
	private String eventtype;
	private Date eventDate;
	private Date eventdateend;
	private String eventinfo;
	
	EventBean(Date eventDate) {
		this.eventDate = eventDate;
	}
	
	public String getPrintableEventDate() {
		SimpleDateFormat formatter;
		Calendar cal = new GregorianCalendar();
		cal.setTime(eventDate);
		if (cal.get(Calendar.HOUR_OF_DAY) == 0) {
			formatter = new SimpleDateFormat("EEE, MMM d");
		} else {
			formatter = new SimpleDateFormat("EEE, MMM d h:mm aa");
		}
		return formatter.format(eventDate.getTime());
	}

	public String getPrintableEventDateEnd() {
		SimpleDateFormat formatter;
		Calendar cal = new GregorianCalendar();
		cal.setTime(eventdateend);
		if (cal.get(Calendar.HOUR_OF_DAY) == 0) {
			formatter = new SimpleDateFormat("EEE, MMM d");
		} else {
			formatter = new SimpleDateFormat("EEE, MMM d h:mm aa");
		}
		return formatter.format(eventdateend.getTime());
	}

	// Newer, nicer versions of printable dates (3/5/17)
	public String getPrintableEventDateDay() {
		SimpleDateFormat formatter;
		Calendar cal = new GregorianCalendar();
		cal.setTime(eventDate);
		if (cal.get(Calendar.HOUR_OF_DAY) == 0) {
			formatter = new SimpleDateFormat("EEEE, MMM d");
		} else {
			formatter = new SimpleDateFormat("EEE, MMM d");
		}
		return formatter.format(eventDate.getTime());
	}

	public String getPrintableEventDateTimeStart() {
		SimpleDateFormat formatter;
		Calendar cal = new GregorianCalendar();
		cal.setTime(eventDate);
		if (cal.get(Calendar.HOUR_OF_DAY) == 0) {
			formatter = new SimpleDateFormat("EEEE, MMM d");
		} else {
			formatter = new SimpleDateFormat("h:mmaa");
		}
		return formatter.format(eventDate.getTime());
	}

	public String getPrintableEventDateTimeEnd() {
		SimpleDateFormat formatter;
		Calendar cal = new GregorianCalendar();
		cal.setTime(eventdateend);
		if (cal.get(Calendar.HOUR_OF_DAY) == 0) {
			formatter = new SimpleDateFormat("EEEE, MMM d");
		} else {
			formatter = new SimpleDateFormat("h:mmaa");
		}
		return formatter.format(eventdateend.getTime());
	}

	public Date getEventDate() {
		return eventDate;
	}
	
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public int compareTo(EventBean anotherMealEvent) {
		return eventDate.compareTo(anotherMealEvent.getEventDate());
	}

	public Long getEventid() {
		return eventid;
	}

	public void setEventid(Long eventid) {
		this.eventid = eventid;
	}

	public String getEventtype() {
		return eventtype;
	}

	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
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
	
}
