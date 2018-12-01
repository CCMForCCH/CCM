package org.cohoman.model.dto;

import java.util.Date;
import java.util.List;

public class PrivateEventDTO {

	private Long eventid;
	private Date eventDate;
	private Date eventdateend;
	private String eventinfo;

	private String printableEventDate;
	private String printableEventDateEnd;
	private String printableEventDateDay;
	private String printableEventDateTimeStart;
	private String printableEventDateTimeEnd;

	private String eventName = null;
	private String eventtype;
	private List<String> spaceList = null;	
	private List<String> characteristicsList = null;
	private String requesterName = null;
	private String printableSpaceList = null;

	// Additional fields on the form
  	private Date createdate;
  	private Long requester;
  	private int expectednumberofadults;
  	private int expectednumberofchildren;
  	private String organization;
  	private boolean iscohousingevent;
  	private boolean isinclusiveevent;
  	private boolean isexclusiveevent;
  	private boolean isincomeevent;
  	private boolean aremajorityresidents;
  	private int donation;
  	private boolean isphysicallyactiveevent;
  	private boolean isonetimeparty;
  	private boolean isclassorworkshop;
  	private String state;
  	private String rejectreason;
  	private Long approvedby;
  	private Date approvaldate;
  	private String whoComes;

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

	public String getPrintableEventDate() {
		return printableEventDate;
	}

	public void setPrintableEventDate(String printableEventDate) {
		this.printableEventDate = printableEventDate;
	}

	public String getPrintableEventDateEnd() {
		return printableEventDateEnd;
	}

	public void setPrintableEventDateEnd(String printableEventDateEnd) {
		this.printableEventDateEnd = printableEventDateEnd;
	}

	public String getPrintableEventDateDay() {
		return printableEventDateDay;
	}

	public void setPrintableEventDateDay(String printableEventDateDay) {
		this.printableEventDateDay = printableEventDateDay;
	}

	public String getPrintableEventDateTimeStart() {
		return printableEventDateTimeStart;
	}

	public void setPrintableEventDateTimeStart(String printableEventDateTimeStart) {
		this.printableEventDateTimeStart = printableEventDateTimeStart;
	}

	public String getPrintableEventDateTimeEnd() {
		return printableEventDateTimeEnd;
	}

	public void setPrintableEventDateTimeEnd(String printableEventDateTimeEnd) {
		this.printableEventDateTimeEnd = printableEventDateTimeEnd;
	}

	public String getEventinfo() {
		return eventinfo;
	}

	public void setEventinfo(String eventinfo) {
		this.eventinfo = eventinfo;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public String getEventtype() {
		return eventtype;
	}

	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}

	public List<String> getSpaceList() {
		return spaceList;
	}

	public void setSpaceList(List<String> spaceList) {
		this.spaceList = spaceList;
	}

	public List<String> getCharacteristicsList() {
		return characteristicsList;
	}

	public void setCharacteristicsList(List<String> characteristicsList) {
		this.characteristicsList = characteristicsList;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public String getPrintableSpaceList() {
		return printableSpaceList;
	}

	public void setPrintableSpaceList(String printableSpaceList) {
		this.printableSpaceList = printableSpaceList;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Long getRequester() {
		return requester;
	}

	public void setRequester(Long requester) {
		this.requester = requester;
	}

	public int getExpectednumberofadults() {
		return expectednumberofadults;
	}

	public void setExpectednumberofadults(int expectednumberofadults) {
		this.expectednumberofadults = expectednumberofadults;
	}

	public int getExpectednumberofchildren() {
		return expectednumberofchildren;
	}

	public void setExpectednumberofchildren(int expectednumberofchildren) {
		this.expectednumberofchildren = expectednumberofchildren;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	
	public boolean isIscohousingevent() {
		return iscohousingevent;
	}

	public void setIscohousingevent(boolean iscohousingevent) {
		this.iscohousingevent = iscohousingevent;
	}

	public boolean isIsinclusiveevent() {
		return isinclusiveevent;
	}

	public void setIsinclusiveevent(boolean isinclusiveevent) {
		this.isinclusiveevent = isinclusiveevent;
	}

	public boolean isIsexclusiveevent() {
		return isexclusiveevent;
	}

	public void setIsexclusiveevent(boolean isexclusiveevent) {
		this.isexclusiveevent = isexclusiveevent;
	}

	public boolean isIsincomeevent() {
		return isincomeevent;
	}

	public void setIsincomeevent(boolean isincomeevent) {
		this.isincomeevent = isincomeevent;
	}

	public boolean isAremajorityresidents() {
		return aremajorityresidents;
	}

	public void setAremajorityresidents(boolean aremajorityresidents) {
		this.aremajorityresidents = aremajorityresidents;
	}

	public int getDonation() {
		return donation;
	}

	public void setDonation(int donation) {
		this.donation = donation;
	}

	public boolean isIsphysicallyactiveevent() {
		return isphysicallyactiveevent;
	}

	public void setIsphysicallyactiveevent(boolean isphysicallyactiveevent) {
		this.isphysicallyactiveevent = isphysicallyactiveevent;
	}

	public boolean isIsonetimeparty() {
		return isonetimeparty;
	}

	public void setIsonetimeparty(boolean isonetimeparty) {
		this.isonetimeparty = isonetimeparty;
	}

	public boolean isIsclassorworkshop() {
		return isclassorworkshop;
	}

	public void setIsclassorworkshop(boolean isclassorworkshop) {
		this.isclassorworkshop = isclassorworkshop;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRejectreason() {
		return rejectreason;
	}

	public void setRejectreason(String rejectreason) {
		this.rejectreason = rejectreason;
	}

	public Long getApprovedby() {
		return approvedby;
	}

	public void setApprovedby(Long approvedby) {
		this.approvedby = approvedby;
	}

	public Date getApprovaldate() {
		return approvaldate;
	}

	public void setApprovaldate(Date approvaldate) {
		this.approvaldate = approvaldate;
	}

	public String getWhoComes() {
		return whoComes;
	}

	public void setWhoComes(String whoComes) {
		this.whoComes = whoComes;
	}
	
}
