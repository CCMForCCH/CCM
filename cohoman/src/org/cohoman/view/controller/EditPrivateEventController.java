package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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

import org.cohoman.model.dto.PrivateEventDTO;
import org.cohoman.model.integration.persistence.beans.EventTypeDefs;
import org.cohoman.model.integration.persistence.beans.PrivateEvent;
import org.cohoman.model.integration.persistence.beans.SpaceBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.model.service.EventService;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.cohoman.view.controller.utils.EventStateEnums;

@ManagedBean
@SessionScoped
public class EditPrivateEventController implements Serializable {

	/**
	 * 
	 */
	Logger logger = Logger.getLogger(this.getClass().getName());

	private static final long serialVersionUID = 4678206276499587830L;

	private List<PrivateEvent> privateEventList;
	private PrivateEvent chosenPrivateEvent;
	private String chosenPrivateEventString;
	private String chosenPrivateEventTypeString;
	private String privateOperation;
	private PrivateEventDTO privateEventDTO;
	private String slotNumberStart;
	private String slotNumberEnd;
	private Set<SpaceBean> chosenSpaceList = new LinkedHashSet<SpaceBean>();
	private Set<String> chosenSpaceListStringInts = new LinkedHashSet<String>();
	private List<String> chosenCharacteristicsList = new ArrayList<String>();

	private Long chosenPrivateEventId;
	private PrivateEvent theCurrentPrivateEvent;

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

	public EditPrivateEventController() throws Exception {
	}

	public PrivateEventDTO getPrivateEventDTO() {
		return privateEventDTO;
	}

	public void setPrivateEventDTO(PrivateEventDTO privateEventDTO) {
		this.privateEventDTO = privateEventDTO;
	}

	private EventService eventService = null;

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public String getChosenPrivateEventString() {
		return chosenPrivateEventString;
	}

	public void setChosenPrivateEventString(String chosenPrivateEventString) {
		this.chosenPrivateEventString = chosenPrivateEventString;
	}

	public String getChosenPrivateEventTypeString() {
		return chosenPrivateEventTypeString;
	}

	public void setChosenPrivateEventTypeString(
			String chosenPrivateEventTypeString) {
		this.chosenPrivateEventTypeString = chosenPrivateEventTypeString;
	}

	public String getPrivateOperation() {
		return privateOperation;
	}

	public void setPrivateOperation(String privateOperation) {
		this.privateOperation = privateOperation;
	}

	public List<PrivateEvent> getPrivateEventList() {
		privateEventList = eventService.getMyPrivateEvents();
		return privateEventList;
	}

	public void setPrivateEventList(List<PrivateEvent> privateEventList) {
		this.privateEventList = privateEventList;
	}

	public PrivateEvent getChosenPrivateEvent() {
		return chosenPrivateEvent;
	}

	public void setChosenPrivateEvent(PrivateEvent chosenPrivateEvent) {
		this.chosenPrivateEvent = chosenPrivateEvent;
	}

	public String getSlotNumberStart() {
		return slotNumberStart;
	}

	public void setSlotNumberStart(String slotNumberStart) {
		this.slotNumberStart = slotNumberStart;
		
		// Set the ending time to be one hour more than the
		// start time as the default. Relies on Ajax
		int slotStartAsInt = 0;
		if (slotNumberStart != null) {
			slotStartAsInt = Integer.parseInt(slotNumberStart);
		}
		slotStartAsInt += 2;

		// Make sure slots don't wrap back to 0
		if (slotStartAsInt > 35) {
			slotStartAsInt = 35;
		}
		this.slotNumberEnd = String.valueOf(slotStartAsInt);

	}

	public String getSlotNumberEnd() {
		return slotNumberEnd;
	}

	public void setSlotNumberEnd(String slotNumberEnd) {
		
		// Only set the end of the slots if it is greater than the start
		int slotStartAsInt = 0;
		if (slotNumberStart != null) {
			slotStartAsInt = Integer.parseInt(slotNumberStart);
		}
		int slotEndAsInt = 0;
		if (slotNumberEnd != null) {
			slotEndAsInt = Integer.parseInt(slotNumberEnd);
		}

		if (slotEndAsInt > slotStartAsInt) {
			this.slotNumberEnd = slotNumberEnd;
		}
	}

	public String getPrivateEventName() {
		return privateEventName;
	}

	public void setPrivateEventName(String privateEventName) {
		this.privateEventName = privateEventName;
	}

	public String getPrivateEventInfo() {
		return privateEventInfo;
	}

	public void setPrivateEventInfo(String privateEventInfo) {
		this.privateEventInfo = privateEventInfo;
	}

	public Set<SpaceBean> getChosenSpaceList() {
		return chosenSpaceList;
	}

	public void setChosenSpaceList(Set<SpaceBean> chosenSpaceList) {
		this.chosenSpaceList = chosenSpaceList;
	}

	public Set<String> getChosenSpaceListStringInts() {
		return chosenSpaceListStringInts;
	}

	public void setChosenSpaceListStringInts(
			Set<String> chosenSpaceListStringInts) {
		this.chosenSpaceListStringInts = chosenSpaceListStringInts;
	}

	public CalendarUtils.TimeSlot[] getTimeSlotsOfTheDay() {
		return CalendarUtils.getTimeSlotsOfTheDay(2010, 1);
	}

	public Long getChosenPrivateEventId() {
		return chosenPrivateEventId;
	}

	public void setChosenPrivateEventId(Long chosenPrivateEventId) {
		this.chosenPrivateEventId = chosenPrivateEventId;
	}

	public PrivateEvent getTheCurrentPrivateEvent() {
		return theCurrentPrivateEvent;
	}

	public void setTheCurrentPrivateEvent(PrivateEvent theCurrentPrivateEvent) {
		this.theCurrentPrivateEvent = theCurrentPrivateEvent;
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

	/*
	 * public String editPrivateEventView() throws Exception {
	 * 
	 * FacesContext ctx = FacesContext.getCurrentInstance(); Map<String, String>
	 * requestParams = ctx.getExternalContext() .getRequestParameterMap();
	 * String chosenPrivateEventIdAsString = requestParams
	 * .get("chosenPrivateEventId"); if (chosenPrivateEventIdAsString == null ||
	 * chosenPrivateEventIdAsString.length() == 0) { System.out
	 * .println("Invalid EventId value in DisplayprivateEventController."); }
	 * chosenPrivateEventId = Long.valueOf(chosenPrivateEventIdAsString);
	 * theCurrentPrivateEvent = eventService
	 * .getPrivateEvent(chosenPrivateEventId);
	 * 
	 * //Long eventId = Long.valueOf(chosenPrivateEventString);
	 * chosenPrivateEvent = eventService.getPrivateEvent(chosenPrivateEventId);
	 * slotNumberStart = Integer.toString((CalendarUtils
	 * .getTimeSlotForDate(chosenPrivateEvent.getEventDate()))); slotNumberEnd =
	 * Integer.toString((CalendarUtils
	 * .getTimeSlotForDate(chosenPrivateEvent.getEventdateend())));
	 * chosenPrivateEventTypeString = chosenPrivateEvent.getEventtype();
	 * privateEventName = chosenPrivateEvent.getEventName(); privateEventInfo =
	 * chosenPrivateEvent.getEventinfo(); chosenPrivateEventTypeString =
	 * chosenPrivateEvent.getEventtype();
	 * 
	 * // Get list of checkboxes from database and set appropriate // checkboxes
	 * in UI chosenSpaceList = chosenPrivateEvent.getSpaceList();
	 * chosenSpaceListStringInts.clear(); for (SpaceBean oneSpaceBean :
	 * chosenSpaceList) {
	 * chosenSpaceListStringInts.add(oneSpaceBean.getSpaceId().toString()); }
	 * 
	 * if (privateOperation.equals("deletePrivateEvent")) {
	 * eventService.deletePrivateEvent(chosenPrivateEvent); }
	 * 
	 * return privateOperation; }
	 */
	public String editMyPrivateEventView() throws Exception {
		return editPrivateEventView("changeMyPrivateEvent");
	}

	public String editAllPrivateEventView() throws Exception {
		return editPrivateEventView("changeAllPrivateEvent");
	}

	private String editPrivateEventView(String outcome) throws Exception {

		// Before doing the edit, do some checking on the input.
		// Have to recompute Date based on potential time slot change
		int startSlotAsInt = Integer.parseInt(slotNumberStart);
		int endSlotAsInt = Integer.parseInt(slotNumberEnd);
		if (startSlotAsInt >= endSlotAsInt) {
			// Times are not valid

			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									"Invalid times: starting time must be less than the ending time"));
			return null;
		}
		
		// Make sure at least one space is chosen.
		if (chosenSpaceListStringInts.isEmpty()) {
			// No spaces selected
			FacesMessage message = new FacesMessage(
					"Error: You must choose at least one space to reserve.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		
		// Make sure at least one event characteristic is chosen.
		if (!(privateIscohousingevent || privateIsinclusiveevent
				|| privateIsexclusiveevent || privateIsincomeevent || privateIsphysicallyactiveevent)) {
			// No event characteristic selected
			FacesMessage message = new FacesMessage(
					"Error: You must choose at least one event characteristic.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		
		// Now start doing the edits.
		chosenPrivateEvent.setEventDate(CalendarUtils.setTimeSlotForDate(
				chosenPrivateEvent.getEventDate(), startSlotAsInt));
		chosenPrivateEvent.setEventdateend(CalendarUtils.setTimeSlotForDate(
				chosenPrivateEvent.getEventdateend(), endSlotAsInt));
		chosenPrivateEvent.setEventName(privateEventName);
		chosenPrivateEvent.setEventinfo(privateEventInfo);
		chosenPrivateEvent.setEventtype(chosenPrivateEventTypeString);

		chosenPrivateEvent.setCreatedate(privateCreatedate);
		chosenPrivateEvent
				.setExpectednumberofadults(privateExpectednumberofadults);
		chosenPrivateEvent
				.setExpectednumberofchildren(privateExpectednumberofchildren);
		chosenPrivateEvent.setOrganization(privateOrganization);
		chosenPrivateEvent.setIscohousingevent(privateIscohousingevent);
		chosenPrivateEvent.setIsinclusiveevent(privateIsinclusiveevent);
		chosenPrivateEvent.setIsexclusiveevent(privateIsexclusiveevent);
		chosenPrivateEvent.setIsincomeevent(privateIsincomeevent);
		chosenPrivateEvent.setAremajorityresidents(privateAremajorityresidents);
		chosenPrivateEvent.setDonation(privateDonation);
		chosenPrivateEvent
				.setIsphysicallyactiveevent(privateIsphysicallyactiveevent);
		chosenPrivateEvent.setIsonetimeparty(privateIsonetimeparty);
		chosenPrivateEvent.setIsclassorworkshop(privateIsclassorworkshop);
		chosenPrivateEvent.setRequester(privateRequester);
		chosenPrivateEvent.setRejectreason(privateRejectreason);
		chosenPrivateEvent.setApprovedby(privateApprovedby);
		chosenPrivateEvent.setApprovaldate(privateApprovaldate);

		chosenPrivateEvent
				.setChosenSpaceListStringInts(chosenSpaceListStringInts);
		try {
			eventService.editPrivateEvent(chosenPrivateEvent);
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		return outcome;
	}

	public String[] getEventTypeChoices() {
		return EventTypeDefs.eventTypeChoicesNoApproval;
	}

	public String[] getEventCharacteristicsChoices() {
		return EventTypeDefs.eventAttributes;
	}

	public List<SpaceBean> getAllSpaces() {
		return eventService.getAllSpaces();
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

	// Fills in the discrete values for the page that are to be edited.
	public PrivateEvent getPrivateEventDetails() {

		// if (chosenPrivateEventId == null || chosenPrivateEventId == 0) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		String chosenPrivateEventIdAsString = requestParams
				.get("chosenPrivateEventId");
		if (chosenPrivateEventIdAsString == null
				|| chosenPrivateEventIdAsString.length() == 0) {
			List<FacesMessage> messageList = ctx.getMessageList();

			// If there was a conflict, you may get here without the
			// EventID as a parameter since you're not changing pages.
			// That's OK since we already have the information, most
			// notably chosenPrivateEventId. So, don't throw the
			// exception if we have a pending error message (=>
			// we stay on the same page). (09/23/16)
			if (!(messageList.size() > 0 && chosenPrivateEventId > 0)) {
				System.out
						.println("Invalid EventId value in EditPrivateEventController.");
				throw new RuntimeException(
						"Invalid EventId value in EditPrivateEventController.");
			} // No need to set chosenPrivateEventId as it's already set.
		} else {
			chosenPrivateEventId = Long.valueOf(chosenPrivateEventIdAsString);
		}
		// }
		
		// Robustness check 05/12/2018
		try {
			theCurrentPrivateEvent = eventService
					.getPrivateEvent(chosenPrivateEventId);
		} catch (CohomanException cex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(cex));
			logger.log(Level.SEVERE, "Failed to get the private event to edit.");
			FacesMessage message = new FacesMessage(LoggingUtils.INTERNAL_ERROR);
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		// Long eventId = Long.valueOf(chosenPrivateEventString);
		// Robustness check 05/12/2018
		try {
			chosenPrivateEvent = eventService
					.getPrivateEvent(chosenPrivateEventId);
		} catch (CohomanException cex) {
			logger.log(Level.SEVERE,
					"Internal Error: private event not found for event id "
							+ chosenPrivateEventId);
			return null;
		}		
		
		slotNumberStart = Integer.toString((CalendarUtils
				.getTimeSlotForDate(chosenPrivateEvent.getEventDate())));
		slotNumberEnd = Integer.toString((CalendarUtils
				.getTimeSlotForDate(chosenPrivateEvent.getEventdateend())));
		chosenPrivateEventTypeString = chosenPrivateEvent.getEventtype();
		privateEventName = chosenPrivateEvent.getEventName();
		privateEventInfo = chosenPrivateEvent.getEventinfo();
		chosenPrivateEventTypeString = chosenPrivateEvent.getEventtype();

		privateCreatedate = chosenPrivateEvent.getCreatedate();
		privateRequester = chosenPrivateEvent.getRequester();
		privateExpectednumberofadults = chosenPrivateEvent
				.getExpectednumberofadults();
		privateExpectednumberofchildren = chosenPrivateEvent
				.getExpectednumberofchildren();
		privateOrganization = chosenPrivateEvent.getOrganization();
		privateIscohousingevent = chosenPrivateEvent.isIscohousingevent();
		privateIsinclusiveevent = chosenPrivateEvent.isIsinclusiveevent();
		privateIsexclusiveevent = chosenPrivateEvent.isIsexclusiveevent();
		privateIsincomeevent = chosenPrivateEvent.isIsincomeevent();
		privateAremajorityresidents = chosenPrivateEvent
				.isAremajorityresidents();
		privateDonation = chosenPrivateEvent.getDonation();
		privateIsphysicallyactiveevent = chosenPrivateEvent
				.isIsphysicallyactiveevent();
		privateIsonetimeparty = chosenPrivateEvent.isIsonetimeparty();
		privateIsclassorworkshop = chosenPrivateEvent.isIsclassorworkshop();
		// TODO make enumerations for state
		privateState = chosenPrivateEvent.getState();
		privateRejectreason = chosenPrivateEvent.getRejectreason();
		privateApprovedby = chosenPrivateEvent.getApprovedby();
		privateApprovaldate = chosenPrivateEvent.getApprovaldate();

		// Get list of checkboxes from database and set appropriate
		// checkboxes in UI
		chosenSpaceList = chosenPrivateEvent.getSpaceList();
		chosenSpaceListStringInts.clear();
		for (SpaceBean oneSpaceBean : chosenSpaceList) {
			chosenSpaceListStringInts.add(oneSpaceBean.getSpaceId().toString());
		}

		// if (privateOperation.equals("deletePrivateEvent")) {
		// eventService.deletePrivateEvent(chosenPrivateEvent);
		// }

		return theCurrentPrivateEvent;

	}

}
