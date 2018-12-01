package org.cohoman.model.integration.persistence.beans;

import java.util.Date;
import java.util.Set;

public class PrivateEvent extends EventBean {

	private String eventName = null;
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

  	// Fields in addition to PrivateEventBean
	private Set<SpaceBean> spaceList = null;
	private String printableSpaceList = null;
	private Set<String> chosenSpaceListStringInts = null;
	private String requesterUsername = null;
	private String approvedByUsername = null;
	private String whoComes = null;

	public PrivateEvent(Date eventDate) {
		super(eventDate);
	}

	public PrivateEvent() {
		super(new Date());
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
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

	// Extensions above and beyond the PrivateEventbean
	public int getUsableEventid() {
		return super.getEventid().intValue();
	}
	
	public String getChoosableEventDate() {
		return super.getPrintableEventDate();
	}

	public String getPrintableSpaceList() {
		return printableSpaceList;
	}

	public void setPrintableSpaceList(String printableSpaceList) {
		this.printableSpaceList = printableSpaceList;
	}

	public Set<SpaceBean> getSpaceList() {
		return spaceList;
	}

	public void setSpaceList(Set<SpaceBean> spaceList) {
		this.spaceList = spaceList;
	}

	public Set<String> getChosenSpaceListStringInts() {
		return chosenSpaceListStringInts;
	}

	public void setChosenSpaceListStringInts(Set<String> chosenSpaceListStringInts) {
		this.chosenSpaceListStringInts = chosenSpaceListStringInts;
	}
	public String getRequesterUsername() {
		return requesterUsername;
	}
	public void setRequesterUsername(String requesterUsername) {
		this.requesterUsername = requesterUsername;
	}

	public String getWhoComes() {
		return whoComes;
	}

	public void setWhoComes(String whoComes) {
		this.whoComes = whoComes;
	}

	public String getApprovedByUsername() {
		return approvedByUsername;
	}

	public void setApprovedByUsername(String approvedByUsername) {
		this.approvedByUsername = approvedByUsername;
	}

}
