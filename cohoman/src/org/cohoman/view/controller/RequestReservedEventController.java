package org.cohoman.view.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.cohoman.model.business.User;
import org.cohoman.model.dto.PrivateEventDTO;
import org.cohoman.model.integration.persistence.beans.EventTypeDefs;
import org.cohoman.model.integration.persistence.beans.SpaceBean;
import org.cohoman.model.service.EventService;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.cohoman.view.controller.utils.EventStateEnums;
import org.cohoman.view.controller.utils.CalendarUtils.MealDate;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class RequestReservedEventController implements Serializable {

	private static final long serialVersionUID = 4960034113499669484L;
	
  	private Date privateChosenDate;
  	private Calendar privateChosenCal;
  	/*
	private String privateEventStartYear;
	private String privateEventStartMonth;
	private String privateEventStartDay;
	*/
	private String privateEventEndYear;
	private String privateEventEndMonth;
	private String privateEventEndDay;
	private String slotNumberStart;
	private String slotNumberEnd;
	private List<MealDate> privateEventDaysForPeriod;
	//private int chosenEventDateTimestamp;
	private String chosenEventType;
	private EventService eventService = null;
	
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
  	
  	// New recurring feature values 01/21/2019
  	private boolean privateIsRecurring = false;
  	private int privateRecurringTimes;
  	private int privateRecurringFrequency;
  	private List<String> privateRecurringErrorsList = new ArrayList<String>();
  	private List<String> privateRecurringSuccessList = new ArrayList<String>();
  	
	
	private List<String> chosenSpaceList = new ArrayList<String>();
	private List<String> chosenCharacteristicsList = new ArrayList<String>();
		
	public RequestReservedEventController() {
		initDateFields();
	}

	private void initDateFields() {
		GregorianCalendar now = new GregorianCalendar();
		//privateEventStartYear = new Integer(now.get(Calendar.YEAR)).toString();
		//privateEventStartMonth = new Integer(now.get(Calendar.MONTH)).toString();
		//privateEventStartDay = new Integer(now.get(Calendar.DAY_OF_MONTH)).toString();
		privateChosenDate = now.getTime();
		privateChosenCal = new GregorianCalendar();
		privateEventEndYear = new Integer(now.get(Calendar.YEAR)).toString();
		privateEventEndMonth = new Integer(now.get(Calendar.MONTH)).toString();
		privateEventEndDay = new Integer(now.get(Calendar.DAY_OF_MONTH)).toString();
		
		// get the userid of the current user to set the requester.
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession)ctx.getExternalContext().getSession(true); 
		User dbUser = (User) session.getAttribute(AuthenticateController.SESSIONVAR_USER_NAME);
		privateRequester = dbUser.getUserid();
		
		slotNumberStart = "0";   // init to 6AM
		slotNumberEnd = "2";     // init to 7AM
		privateIsRecurring = false;
		privateRecurringTimes = 1;
		//privateRecurringErrorsList = new ArrayList<String>();

	}
	
	private void clearFormFields() {
		
		initDateFields();
		privateEventName = "";
		privateEventInfo = "";
	  	privateExpectednumberofadults = 0;
	  	privateExpectednumberofchildren = 0;
	  	privateOrganization = "";
	  	privateIscohousingevent = false;
	  	privateIsinclusiveevent =false;
	  	privateIsexclusiveevent = false;
	  	privateIsincomeevent= false;
	  	privateAremajorityresidents = false;
	  	privateDonation = 0;
	  	privateIsphysicallyactiveevent = false;
	  	privateIsonetimeparty = false;
	  	privateIsclassorworkshop = false;
	  	chosenSpaceList = new ArrayList<String>();
		chosenCharacteristicsList = new ArrayList<String>();
		slotNumberStart = "0";   // init to 6AM
		slotNumberEnd = "2";     // init to 7AM
		privateIsRecurring = false;
		privateRecurringTimes = 1;
	}
	
	public EventService getEventService() {
		return eventService;
	}
	
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
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

	public List<String> getPrivateRecurringErrorsList() {
		return privateRecurringErrorsList;
	}

	public void setPrivateRecurringErrorsList(
			List<String> privateRecurringErrorsList) {
		this.privateRecurringErrorsList = privateRecurringErrorsList;
	}

	public List<String> getPrivateRecurringSuccessList() {
		return privateRecurringSuccessList;
	}

	public void setPrivateRecurringSuccessList(
			List<String> privateRecurringSuccessList) {
		this.privateRecurringSuccessList = privateRecurringSuccessList;
	}

	public void setChosenCharacteristicsList(List<String> chosenCharacteristicsList) {
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
			if (EventTypeDefs.PHYSICALLYACTIVEEVENTTYPE.equalsIgnoreCase(onechar)) {
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

/*
	public String getPrivateEventYear() {
		return privateEventStartYear;
	}

	public void setPrivateEventYear(String privateEventYear) {
		this.privateEventStartYear = privateEventYear;
		this.privateEventEndYear = privateEventYear;
	}

	public String getPrivateEventMonth() {
		return privateEventStartMonth;
	}

	public void setPrivateEventMonth(String privateEventMonth) {
		this.privateEventStartMonth = privateEventMonth;
		this.privateEventEndMonth = privateEventMonth; 
	}

	public String getPrivateEventDay() {
		return privateEventStartDay;
	}

	public void setPrivateEventDay(String privateEventDay) {
		this.privateEventStartDay = privateEventDay;
		this.privateEventEndDay = privateEventDay;
	}
*/
	
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

	public void setPrivateExpectednumberofadults(int privateExpectednumberofadults) {
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

	public void setPrivateAremajorityresidents(boolean privateAremajorityresidents) {
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

	public boolean isPrivateIsRecurring() {
		return privateIsRecurring;
	}

	public void setPrivateIsRecurring(boolean privateIsRecurring) {
		this.privateIsRecurring = privateIsRecurring;
	}

	public int getPrivateRecurringTimes() {
		return privateRecurringTimes;
	}

	public void setPrivateRecurringTimes(int privateRecurringTimes) {
		this.privateRecurringTimes = privateRecurringTimes;
	}

	public int getPrivateRecurringFrequency() {
		return privateRecurringFrequency;
	}

	public void setPrivateRecurringFrequency(int privateRecurringFrequency) {
		this.privateRecurringFrequency = privateRecurringFrequency;
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

	/*
	public int getChosenEventDateTimestamp() {
		return chosenEventDateTimestamp;
	}

	public void setChosenEventDateTimestamp(int chosenEventDateTimestamp) {
		this.chosenEventDateTimestamp = chosenEventDateTimestamp;
		for (MealDate mealDate : privateEventDaysForPeriod) {
			if (mealDate.getTimestamp() == chosenEventDateTimestamp) {
				privateEventStartYear = Integer.toString(mealDate.getTheYear());
				privateEventStartMonth = Integer.toString(mealDate.getTheMonth().getMonthNumber());
				privateEventStartDay = Integer.toString(mealDate.getTheDay());
				return;
			}
		}
		throw new RuntimeException ("Cannot find selected CohoDate object.");
	}
	*/

	public String addRequestReservedEvent() throws Exception {
		Calendar chosenStartDate = calculateTheStartDate();
		Calendar chosenEndDate = calculateTheEndDate();
		
		Calendar rightNow = Calendar.getInstance();
		if (chosenStartDate.before(rightNow) ||
				chosenEndDate.before(rightNow) ||
				chosenStartDate.equals(chosenEndDate) ||
				chosenStartDate.after(chosenEndDate))
		{
			// Dates are not valid
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage("Invalid dates: must be in the future and start date before end date"));
			return null;
		} 
		
		// Remove all entries from error and success lists before we attempt to
		// create the request.
		privateRecurringErrorsList.clear();
		privateRecurringSuccessList.clear();

		//Date eventDate = calculateTheStartDate().getTime();
		//Date eventdateend = calculateTheEndDate().getTime();
		for (int times = 0; times < privateRecurringTimes; times++) {
			Date eventDate = chosenStartDate.getTime();
			Date eventdateend = chosenEndDate.getTime();

			PrivateEventDTO dto = PrivateEventDTO(eventDate);
			dto.setEventdateend(eventdateend);
			dto.setCreatedate(rightNow.getTime());
	
			try {
				eventService.createPrivateEvent(dto);
				// Save date of success strings for printing results
				SimpleDateFormat formatter = new SimpleDateFormat(
						"EEE, MMM d, yyyy h:mm aa");
				String eventDateString = formatter.format(eventDate);
				privateRecurringSuccessList.add(eventDateString);
				// advance to next week
				chosenStartDate.add(Calendar.DAY_OF_YEAR, 7);
				chosenEndDate.add(Calendar.DAY_OF_YEAR, 7);

			} catch (CohomanException ex) {
				
				// Check if this is a recurring event. Handle
				// errors differently if it is.
				if (privateRecurringTimes == 1) {
					// If we're only going through once, just
					// return the error
					FacesMessage message = new FacesMessage(ex.getErrorText());
					message.setSeverity(FacesMessage.SEVERITY_ERROR);
					FacesContext.getCurrentInstance().addMessage(null, message);
					return null;
				} else {
					// Recurring case. Save the error in an 
					// error list to be displayed on another page
					// later on. For now, just continue with the list.
					// advance to next week
					chosenStartDate.add(Calendar.DAY_OF_YEAR, 7);
					chosenEndDate.add(Calendar.DAY_OF_YEAR, 7);

					privateRecurringErrorsList.add(ex.getErrorText());
				}
			}
			
		}
	

		// Return a string that will either go to the
		// "list-my-reserved-events" page or go to
		// a special error page.
		if (privateRecurringErrorsList.isEmpty()) {
			if (privateRecurringTimes == 1) {
				// clear out fields for return to the page in this session
				clearFormFields();	
				return "requestReservedEvent";
			} else {
				// clear out fields for return to the page in this session
				clearFormFields();	
				return "requestReservedEventsOK";
			}
		} else {
			// Recurring events error case
			// clear out fields for return to the page in this session
			clearFormFields();	
			return "requestReservedEventWithErrors";
		}
	}
	
	// method to create a PrivateEvent
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
		/*
		Calendar chosenTimeCal = new GregorianCalendar(Integer.parseInt(privateEventStartYear), 
				Integer.parseInt(privateEventStartMonth), Integer.parseInt(privateEventStartDay), 
				slots[slotInt].getHour(), slots[slotInt].getMinutes());
		*/
		Calendar chosenTimeCal = new GregorianCalendar(privateChosenCal.get(Calendar.YEAR), 
			privateChosenCal.get(Calendar.MONTH), privateChosenCal.get(Calendar.DAY_OF_MONTH), 
				slots[slotInt].getHour(), slots[slotInt].getMinutes());

		return chosenTimeCal;
	}
	
	private Calendar calculateTheEndDate() {
		CalendarUtils.TimeSlot[] slots = getTimeSlotsOfTheDay();
		int slotInt = Integer.parseInt(slotNumberEnd);
		Calendar chosenTimeCal = new GregorianCalendar(Integer.parseInt(privateEventEndYear), 
				Integer.parseInt(privateEventEndMonth), Integer.parseInt(privateEventEndDay), 
				slots[slotInt].getHour(), slots[slotInt].getMinutes());
		return chosenTimeCal;
	}

	public CalendarUtils.TimeSlot[] getTimeSlotsOfTheDay() {
		return CalendarUtils.getTimeSlotsOfTheDay(privateChosenCal.get(Calendar.YEAR),
				privateChosenCal.get(Calendar.MONTH));
	}

	public CalendarUtils.OneMonth[] getMonthsOfTheYear() {
		return CalendarUtils.getMonthsOfTheYear();
	}

	public String[] getDaysOfTheMonth() {
		return CalendarUtils.getDaysOfTheMonth(privateChosenCal.get(Calendar.YEAR),
				privateChosenCal.get(Calendar.DAY_OF_MONTH));
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
		return 	eventService.getAllSpaces();
	}
	
	// Method for drop-down of expected number of adults attending
	public List<String> getNumberOfAdults() {

		List<String> countList = new ArrayList<String>();
		for (int idx = 1; idx < 76; idx++) {
			countList.add(Integer.toString(idx));
		}
		return countList;
	}

	// Method for drop-down of expected number of adults attending
	public List<String> getNumberOfChildren() {

		List<String> countList = new ArrayList<String>();
		for (int idx = 0; idx < 26; idx++) {
			countList.add(Integer.toString(idx));
		}
		return countList;
	}

	// Method for drop-down of the number of recurred events
	public List<String> getNumberOfRecurredEvents() {

		List<String> countList = new ArrayList<String>();
		for (int idx = 1; idx < 26; idx++) {
			countList.add(Integer.toString(idx));
		}
		return countList;
	}

	// new stuff for recurring support 1/20/2019
	public Date getPrivateChosenDate() {
		return privateChosenDate;
	}

	public void setPrivateChosenDate(Date privateChosenDate) {
		this.privateChosenDate = privateChosenDate;
		this.privateChosenCal.setTime(privateChosenDate);
		// Not very useful at the moment since we don't allow events to span days
		this.privateEventEndYear = new Integer(privateChosenCal.get(Calendar.YEAR)).toString();
		this.privateEventEndMonth = new Integer(privateChosenCal.get(Calendar.MONTH)).toString();
		this.privateEventEndDay = new Integer(privateChosenCal.get(Calendar.DAY_OF_MONTH)).toString();
	}

	public void dateSelect(SelectEvent event) {
		setPrivateChosenDate((Date)event.getObject());	
	}

}
