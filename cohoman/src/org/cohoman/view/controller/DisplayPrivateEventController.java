package org.cohoman.view.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.cohoman.model.business.User;
import org.cohoman.model.dto.PrivateEventDTO;
import org.cohoman.model.integration.persistence.beans.EventTypeDefs;
import org.cohoman.model.integration.persistence.beans.PrivateEvent;
import org.cohoman.model.integration.persistence.beans.SpaceBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.model.service.EventService;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.cohoman.view.controller.utils.CalendarUtils.MealDate;
import org.cohoman.view.controller.utils.EventStateEnums;

@ManagedBean
@SessionScoped
public class DisplayPrivateEventController implements Serializable {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private static final long serialVersionUID = 4960034113499669484L;
	private String privateEventStartYear;
	private String privateEventStartMonth;
	private String privateEventStartDay;
	private String privateEventEndYear;
	private String privateEventEndMonth;
	private String privateEventEndDay;
	private String slotNumberStart;
	private String slotNumberEnd;
	private List<MealDate> privateEventDaysForPeriod;
	private int chosenEventDateTimestamp;
	private String chosenEventType;
	private EventService eventService = null;
	private UserService userService = null;

	private String privateEventName = null;
	private String privateEventInfo = null;
	private Date privateCreatedate;
	private Long privateRequester;
	private int privateExpectednumberofadults;
	private int privateExpectednumberofchildren;
	private String privateOrganization;
	private boolean privateIscohousingevent;
	private boolean privateIsinclusiveevent;
	private boolean privateIsexclusiveevent;
	private boolean privateIsincomeevent;
	private boolean privateAremajorityresidents;
	private int privateDonation;
	private boolean privateIsphysicallyactiveevent;
	private boolean privateIsonetimeparty;
	private boolean privateIsclassorworkshop;
	// TODO make enumerations for state
	private String privateState = EventStateEnums.REQUESTED.name();
	private String privateRejectreason;
	private Long privateApprovedby;
	private Date privateApprovaldate;

	private List<String> chosenSpaceList = new ArrayList<String>();
	private List<String> chosenCharacteristicsList = new ArrayList<String>();

	private Long chosenPrivateEventId;
	private PrivateEvent theCurrentPrivateEvent;

	public DisplayPrivateEventController() {
		GregorianCalendar now = new GregorianCalendar();
		privateEventStartYear = new Integer(now.get(Calendar.YEAR)).toString();
		privateEventStartMonth = new Integer(now.get(Calendar.MONTH))
				.toString();
		privateEventStartDay = new Integer(now.get(Calendar.DAY_OF_MONTH))
				.toString();
		privateEventEndYear = new Integer(now.get(Calendar.YEAR)).toString();
		privateEventEndMonth = new Integer(now.get(Calendar.MONTH)).toString();
		privateEventEndDay = new Integer(now.get(Calendar.DAY_OF_MONTH))
				.toString();

		// get the userid of the current user to set the requester.
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(true);
		User dbUser = (User) session
				.getAttribute(AuthenticateController.SESSIONVAR_USER_NAME);
		privateRequester = dbUser.getUserid();

	}

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getPrivateEventName() {
		return privateEventName;
	}

	public void setPrivateEventName(String privateEventName) {
		this.privateEventName = privateEventName;
	}

	public List<String> getChosenSpaceList() {
		return chosenSpaceList;
	}

	public void setChosenSpaceList(List<String> chosenSpaceList) {
		this.chosenSpaceList = chosenSpaceList;
	}

	public List<String> getChosenCharacteristicsList() {
		return chosenCharacteristicsList;
	}

	public void setChosenCharacteristicsList(
			List<String> chosenCharacteristicsList) {
		privateIscohousingevent = false;
		privateIsinclusiveevent = false;
		privateIsexclusiveevent = false;
		privateIsincomeevent = false;
		privateIsphysicallyactiveevent = false;

		for (String onechar : chosenCharacteristicsList) {
			if (EventTypeDefs.COHOUSINGEVENTTYPE.equalsIgnoreCase(onechar)) {
				privateIscohousingevent = true;
			}
			if (EventTypeDefs.INCLUSIVEEVENTTYPE.equalsIgnoreCase(onechar)) {
				privateIsinclusiveevent = true;
			}
			if (EventTypeDefs.EXCLUSIVEEVENTTYPE.equalsIgnoreCase(onechar)) {
				privateIsexclusiveevent = true;
			}
			if (EventTypeDefs.INCOMEEVENTTYPE.equalsIgnoreCase(onechar)) {
				privateIsincomeevent = true;
			}
			if (EventTypeDefs.PHYSICALLYACTIVEEVENTTYPE
					.equalsIgnoreCase(onechar)) {
				privateIsphysicallyactiveevent = true;
			}
		}
		this.chosenCharacteristicsList = chosenCharacteristicsList;
	}

	public String getPrivateEventInfo() {
		return privateEventInfo;
	}

	public void setPrivateEventInfo(String privateEventInfo) {
		this.privateEventInfo = privateEventInfo;
	}

	public String getPrivateEventStartYear() {
		return privateEventStartYear;
	}

	public void setPrivateEventStartYear(String privateEventStartYear) {
		this.privateEventStartYear = privateEventStartYear;
	}

	public String getPrivateEventStartMonth() {
		return privateEventStartMonth;
	}

	public void setPrivateEventStartMonth(String privateEventStartMonth) {
		this.privateEventStartMonth = privateEventStartMonth;
		// this.privateEventEndMonth = privateEventStartMonth; // Set BOTH
		// values!!
	}

	public String getPrivateEventStartDay() {
		return privateEventStartDay;
	}

	public void setPrivateEventStartDay(String privateEventStartDay) {
		this.privateEventStartDay = privateEventStartDay;
	}

	public String getPrivateEventEndYear() {
		return privateEventEndYear;
	}

	public void setPrivateEventEndYear(String privateEventEndYear) {
		this.privateEventEndYear = privateEventEndYear;
	}

	public String getPrivateEventEndMonth() {
		return privateEventEndMonth;
	}

	public void setPrivateEventEndMonth(String privateEventEndMonth) {
		this.privateEventEndMonth = privateEventEndMonth;
	}

	public String getPrivateEventEndDay() {
		return privateEventEndDay;
	}

	public void setPrivateEventEndDay(String privateEventEndDay) {
		this.privateEventEndDay = privateEventEndDay;
	}

	public List<MealDate> getPrivateEventDaysForPeriod() {
		return privateEventDaysForPeriod;
	}

	public void setPrivateEventDaysForPeriod(
			List<MealDate> privateEventDaysForPeriod) {
		this.privateEventDaysForPeriod = privateEventDaysForPeriod;
	}

	public Date getPrivateCreatedate() {
		return privateCreatedate;
	}

	public void setPrivateCreatedate(Date privateCreatedate) {
		this.privateCreatedate = privateCreatedate;
	}

	public Long getPrivateRequester() {
		return privateRequester;
	}

	public void setPrivateRequester(Long privateRequester) {
		this.privateRequester = privateRequester;
	}

	public int getPrivateExpectednumberofadults() {
		return privateExpectednumberofadults;
	}

	public void setPrivateExpectednumberofadults(
			int privateExpectednumberofadults) {
		this.privateExpectednumberofadults = privateExpectednumberofadults;
	}

	public int getPrivateExpectednumberofchildren() {
		return privateExpectednumberofchildren;
	}

	public void setPrivateExpectednumberofchildren(
			int privateExpectednumberofchildren) {
		this.privateExpectednumberofchildren = privateExpectednumberofchildren;
	}

	public String getPrivateOrganization() {
		return privateOrganization;
	}

	public void setPrivateOrganization(String privateOrganization) {
		this.privateOrganization = privateOrganization;
	}

	public boolean isPrivateIscohousingevent() {
		return privateIscohousingevent;
	}

	public void setPrivateIscohousingevent(boolean privateIscohousingevent) {
		this.privateIscohousingevent = privateIscohousingevent;
	}

	public boolean isPrivateIsinclusiveevent() {
		return privateIsinclusiveevent;
	}

	public void setPrivateIsinclusiveevent(boolean privateIsinclusiveevent) {
		this.privateIsinclusiveevent = privateIsinclusiveevent;
	}

	public boolean isPrivateIsexclusiveevent() {
		return privateIsexclusiveevent;
	}

	public void setPrivateIsexclusiveevent(boolean privateIsexclusiveevent) {
		this.privateIsexclusiveevent = privateIsexclusiveevent;
	}

	public boolean isPrivateIsincomeevent() {
		return privateIsincomeevent;
	}

	public void setPrivateIsincomeevent(boolean privateIsincomeevent) {
		this.privateIsincomeevent = privateIsincomeevent;
	}

	public boolean isPrivateAremajorityresidents() {
		return privateAremajorityresidents;
	}

	public void setPrivateAremajorityresidents(
			boolean privateAremajorityresidents) {
		this.privateAremajorityresidents = privateAremajorityresidents;
	}

	public int getPrivateDonation() {
		return privateDonation;
	}

	public void setPrivateDonation(int privateDonation) {
		this.privateDonation = privateDonation;
	}

	public boolean isPrivateIsphysicallyactiveevent() {
		return privateIsphysicallyactiveevent;
	}

	public void setPrivateIsphysicallyactiveevent(
			boolean privateIsphysicallyactiveevent) {
		this.privateIsphysicallyactiveevent = privateIsphysicallyactiveevent;
	}

	public boolean isPrivateIsonetimeparty() {
		return privateIsonetimeparty;
	}

	public void setPrivateIsonetimeparty(boolean privateIsonetimeparty) {
		this.privateIsonetimeparty = privateIsonetimeparty;
	}

	public boolean isPrivateIsclassorworkshop() {
		return privateIsclassorworkshop;
	}

	public void setPrivateIsclassorworkshop(boolean privateIsclassorworkshop) {
		this.privateIsclassorworkshop = privateIsclassorworkshop;
	}

	public String getPrivateState() {
		return privateState;
	}

	public void setPrivateState(String privateState) {
		this.privateState = privateState;
	}

	public String getPrivateRejectreason() {
		return privateRejectreason;
	}

	public void setPrivateRejectreason(String privateRejectreason) {
		this.privateRejectreason = privateRejectreason;
	}

	public Long getPrivateApprovedby() {
		return privateApprovedby;
	}

	public void setPrivateApprovedby(Long privateApprovedby) {
		this.privateApprovedby = privateApprovedby;
	}

	public Date getPrivateApprovaldate() {
		return privateApprovaldate;
	}

	public void setPrivateApprovaldate(Date privateApprovaldate) {
		this.privateApprovaldate = privateApprovaldate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSlotNumberStart() {
		return slotNumberStart;
	}

	public void setSlotNumberStart(String slotNumberStart) {
		this.slotNumberStart = slotNumberStart;
	}

	public String getSlotNumberEnd() {
		return slotNumberEnd;
	}

	public void setSlotNumberEnd(String slotNumberEnd) {
		this.slotNumberEnd = slotNumberEnd;
	}

	public int getChosenEventDateTimestamp() {
		return chosenEventDateTimestamp;
	}

	public void setChosenEventDateTimestamp(int chosenEventDateTimestamp) {
		this.chosenEventDateTimestamp = chosenEventDateTimestamp;
		for (MealDate mealDate : privateEventDaysForPeriod) {
			if (mealDate.getTimestamp() == chosenEventDateTimestamp) {
				privateEventStartYear = Integer.toString(mealDate.getTheYear());
				privateEventStartMonth = Integer.toString(mealDate
						.getTheMonth().getMonthNumber());
				privateEventStartDay = Integer.toString(mealDate.getTheDay());
				return;
			}
		}
		throw new RuntimeException("Cannot find selected CohoDate object.");
	}

	// method to create a PrivateEvent
/*
	private PrivateEventDTO PrivateEventDTO(Date eventDate) {
		PrivateEventDTO dto = new PrivateEventDTO();
		dto.setEventDate(eventDate);
		dto.setEventinfo(privateEventInfo);
		dto.setEventName(privateEventName);
		dto.setEventtype(makeCharacteristicsString());
		dto.setSpaceList(chosenSpaceList);
		dto.setCreatedate(privateCreatedate);
		dto.setRequester(privateRequester);
		dto.setExpectednumberofadults(privateExpectednumberofadults);
		dto.setExpectednumberofchildren(privateExpectednumberofchildren);
		dto.setOrganization(privateOrganization);
		dto.setIscohousingevent(privateIscohousingevent);
		dto.setIsinclusiveevent(privateIsinclusiveevent);
		dto.setIsexclusiveevent(privateIsexclusiveevent);
		dto.setAremajorityresidents(privateAremajorityresidents);
		dto.setDonation(privateDonation);
		dto.setIsphysicallyactiveevent(privateIsphysicallyactiveevent);
		dto.setIsonetimeparty(privateIsonetimeparty);
		dto.setIsclassorworkshop(privateIsclassorworkshop);
		dto.setIsincomeevent(privateIsincomeevent);
		dto.setState(privateState);
		dto.setRejectreason(privateRejectreason);
		dto.setApprovedby(privateApprovedby);
		dto.setApprovaldate(privateApprovaldate);

		return dto;
	}
*/
	private String makeCharacteristicsString() {

		String characteristicsString = "";
		for (String oneCharacteristic : chosenCharacteristicsList) {
			characteristicsString += oneCharacteristic;
		}
		return characteristicsString;
	}

	private Calendar calculateTheStartDate() {
		CalendarUtils.TimeSlot[] slots = getTimeSlotsOfTheDay();
		int slotInt = Integer.parseInt(slotNumberStart);
		Calendar chosenTimeCal = new GregorianCalendar(
				Integer.parseInt(privateEventStartYear),
				Integer.parseInt(privateEventStartMonth),
				Integer.parseInt(privateEventStartDay),
				slots[slotInt].getHour(), slots[slotInt].getMinutes());
		return chosenTimeCal;
	}

	private Calendar calculateTheEndDate() {
		CalendarUtils.TimeSlot[] slots = getTimeSlotsOfTheDay();
		int slotInt = Integer.parseInt(slotNumberEnd);
		Calendar chosenTimeCal = new GregorianCalendar(
				Integer.parseInt(privateEventEndYear),
				Integer.parseInt(privateEventEndMonth),
				Integer.parseInt(privateEventEndDay), slots[slotInt].getHour(),
				slots[slotInt].getMinutes());
		return chosenTimeCal;
	}

	public CalendarUtils.TimeSlot[] getTimeSlotsOfTheDay() {
		return CalendarUtils.getTimeSlotsOfTheDay(
				Integer.parseInt(privateEventStartYear),
				Integer.parseInt(privateEventStartMonth));
	}

	public CalendarUtils.OneMonth[] getMonthsOfTheYear() {
		return CalendarUtils.getMonthsOfTheYear();
	}

	public String[] getDaysOfTheMonth() {
		return CalendarUtils.getDaysOfTheMonth(
				Integer.parseInt(privateEventStartYear),
				Integer.parseInt(privateEventStartMonth));
	}

	public String[] getYears() {
		return CalendarUtils.getYears();
	}

	public String[] getEventCharacteristicsChoices() {
		return EventTypeDefs.eventAttributes;
	}

	public String getChosenEventType() {
		return chosenEventType;
	}

	public void setChosenEventType(String chosenEventType) {
		this.chosenEventType = chosenEventType;
	}

	public List<SpaceBean> getAllSpaces() {
		return eventService.getAllSpaces();
	}

	public Long getChosenPrivateEventId() {
		return chosenPrivateEventId;
	}

	public PrivateEvent getPrivateEventDetails() {
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		String chosenPrivateEventIdAsString = requestParams
				.get("chosenPrivateEventId");
		if (chosenPrivateEventIdAsString != null
				&& chosenPrivateEventIdAsString.length() > 0) {
			// Have a new chosenPrivateEventId to set
			chosenPrivateEventId = Long.valueOf(chosenPrivateEventIdAsString);
		} else if (chosenPrivateEventId < 1) {
			// Don't have a new chosenPrivate EventId to set: use old one
			// unless it's also not set (don't know why 8/21/16)
			throw new RuntimeException("chosenPrivateEventId isn't set");
		}
		
		try {
			theCurrentPrivateEvent = eventService
					.getPrivateEvent(chosenPrivateEventId);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			FacesMessage message = new FacesMessage(LoggingUtils.INTERNAL_ERROR);
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		//TODO 8/25/16 Set ALL values in this controller from the PrivateEvent????
		setPrivateState(theCurrentPrivateEvent.getState());
		setPrivateRejectreason(theCurrentPrivateEvent.getRejectreason());
		
		return theCurrentPrivateEvent;
	}

	public String getResponsiblePerson() {

		if (theCurrentPrivateEvent != null) {
			Long userId = theCurrentPrivateEvent.getRequester();
			User user = userService.getUser(userId);
			return user.getFirstname() + " " + user.getLastname();
		} else {
			return "";
		}
	}

	public String getApprovedPerson() {

		Long userId = theCurrentPrivateEvent.getApprovedby();
		if (userId == null) {
			return "";
		}
		User user = userService.getUser(userId);
		return user.getFirstname() + " " + user.getLastname();
	}

	public String getReservedSpaces() {

		String printableListOfSpaces = "";
		if (theCurrentPrivateEvent != null) {
			Set<SpaceBean> spaceBeanList = theCurrentPrivateEvent
					.getSpaceList();
			for (SpaceBean oneBean : spaceBeanList) {
				printableListOfSpaces += oneBean.getSpaceName() + ", ";
			}
			if (printableListOfSpaces.length() > 0) {
				printableListOfSpaces = printableListOfSpaces.substring(0,
						printableListOfSpaces.length() - 2);
			}
		}
		return printableListOfSpaces;
	}

	public String getChosenEventCharacteristics() {

		String chosenCharacteristics = "";
		if (theCurrentPrivateEvent != null) {
			if (theCurrentPrivateEvent.isIscohousingevent()) {
				chosenCharacteristics += EventTypeDefs.COHOUSINGEVENTTYPE
						+ ", ";
			}
			if (theCurrentPrivateEvent.isIsinclusiveevent()) {
				chosenCharacteristics += EventTypeDefs.INCLUSIVEEVENTTYPE
						+ ", ";
			}
			if (theCurrentPrivateEvent.isIsexclusiveevent()) {
				chosenCharacteristics += EventTypeDefs.EXCLUSIVEEVENTTYPE
						+ ", ";
			}
			if (theCurrentPrivateEvent.isIsincomeevent()) {
				chosenCharacteristics += EventTypeDefs.INCOMEEVENTTYPE + ", ";
			}
			if (theCurrentPrivateEvent.isIsphysicallyactiveevent()) {
				chosenCharacteristics += EventTypeDefs.PHYSICALLYACTIVEEVENTTYPE
						+ ", ";
			}
			if (chosenCharacteristics.length() > 0) {
				chosenCharacteristics = chosenCharacteristics.substring(0,
						chosenCharacteristics.length() - 2);
			}
		}
		return chosenCharacteristics;
	}

	public String getIncomeEventCharacteristics() {
		String incomeCharacteristics = "";
		if (theCurrentPrivateEvent != null) {
			if (theCurrentPrivateEvent.isAremajorityresidents()) {
				incomeCharacteristics += EventTypeDefs.AMAJORITYRESIDENTS
						+ ", ";
			} else {
				incomeCharacteristics += EventTypeDefs.AMAJORITYNOTRESIDENTS
						+ ", ";
			}
			int donation = theCurrentPrivateEvent.getDonation();
			incomeCharacteristics += "Donation = $" + donation;
		}
		return incomeCharacteristics;
	}

	public String getPhysicalCharacteristics() {
		String physicalCharacteristics = "";
		if (theCurrentPrivateEvent != null) {
			if (theCurrentPrivateEvent.isIsonetimeparty()) {
				physicalCharacteristics += EventTypeDefs.ONETIMEPARTY + ", ";
			} else {
				physicalCharacteristics += EventTypeDefs.NOTONETIMEPARTY + ", ";
			}
			if (theCurrentPrivateEvent.isIsclassorworkshop()) {
				physicalCharacteristics += EventTypeDefs.CLASSORWORKSHOP + ", ";
			} else {
				physicalCharacteristics += EventTypeDefs.NOTCLASSORWORKSHOP
						+ ", ";
			}
			if (physicalCharacteristics.length() > 0) {
				physicalCharacteristics = physicalCharacteristics.substring(0,
						physicalCharacteristics.length() - 2);
			}
		}
		return physicalCharacteristics;
	}

	public String getPrintableCreateDate() {

		String printableCreateDate = "";

		// Not sure why this can't be done earlier or is
		// needed, but this value is null for this method and note others??
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		String chosenPrivateEventIdAsString = requestParams
				.get("chosenPrivateEventId");
		if (chosenPrivateEventIdAsString != null
				&& chosenPrivateEventIdAsString.length() > 0) {
			// Have a new chosenPrivateEventId to set
			chosenPrivateEventId = Long.valueOf(chosenPrivateEventIdAsString);
		} else if (chosenPrivateEventId < 1) {
			// Don't have a new chosenPrivate EventId to set: use old one
			// unless it's also not set (don't know why 8/21/16)
			throw new RuntimeException("chosenPrivateEventId isn't set");
		}
		
		try {
			theCurrentPrivateEvent = eventService
					.getPrivateEvent(chosenPrivateEventId);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			FacesMessage message = new FacesMessage(LoggingUtils.INTERNAL_ERROR);
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return "Invalid Date";
		}

		Date createDate = theCurrentPrivateEvent.getCreatedate();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"EEEE, MMM. d h:mm aa");
		printableCreateDate = formatter.format(createDate.getTime());
		return printableCreateDate;
	}

	public String getPrintableApprovedDate() {
		SimpleDateFormat formatter;
		Calendar cal = new GregorianCalendar();
		cal.setTime(theCurrentPrivateEvent.getApprovaldate());
		if (cal.get(Calendar.HOUR_OF_DAY) == 0) {
			formatter = new SimpleDateFormat("EEEE, MMM. d");
		} else {
			formatter = new SimpleDateFormat("EEEE, MMM. d h:mm aa");
		}
		return formatter.format(theCurrentPrivateEvent.getApprovaldate().getTime());
	}

	public String approvePrivateEvent() throws CohomanException {

		// Set the state to "Approved"
		theCurrentPrivateEvent.setState(EventStateEnums.APPROVED.name());
		if (approveOrRejectPrivateEvent()) {
			return "approvePrivateEvent";
		} else {
			return null;
		}
	}

	public String rejectPrivateEvent() throws CohomanException {

		// Set the state to "REJECTED"
		theCurrentPrivateEvent.setState(EventStateEnums.REJECTED.name());
		theCurrentPrivateEvent.setRejectreason(privateRejectreason);
		approveOrRejectPrivateEvent();
		return "rejectPrivateEvent";			
	}

	private boolean approveOrRejectPrivateEvent() throws CohomanException {

		// Figure out who I am so we can set the approver
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(true);
		User dbUser = (User) session
				.getAttribute(AuthenticateController.SESSIONVAR_USER_NAME);
		theCurrentPrivateEvent.setApprovedby(dbUser.getUserid());

		// Set the date time of now for approval
		Calendar rightNow = Calendar.getInstance();
		theCurrentPrivateEvent.setApprovaldate(rightNow.getTime());

		// For the PrivateEventDaoImpl, create a ChosenSpaceListStringInts
		// from the space list to conform with the way that the edit private
		// event case works.
		Set<String> chosenSpaceListStringInts = new LinkedHashSet<String>();
		for (SpaceBean oneSpaceBean : theCurrentPrivateEvent.getSpaceList()) {
			chosenSpaceListStringInts.add(oneSpaceBean.getSpaceId().toString());
		}
		theCurrentPrivateEvent
				.setChosenSpaceListStringInts(chosenSpaceListStringInts);

		// Update the private event in the DB
		try {
			eventService.editPrivateEvent(theCurrentPrivateEvent);
			return true;
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return false;
		}

	}
}
