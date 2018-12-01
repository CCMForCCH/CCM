package org.cohoman.view.controller;

import java.io.Serializable;
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
import org.cohoman.model.dto.CohoEventDTO;
import org.cohoman.model.integration.persistence.beans.EventTypeDefs;
import org.cohoman.model.integration.persistence.beans.SpaceBean;
import org.cohoman.model.service.EventService;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.cohoman.view.controller.utils.CalendarUtils.MealDate;

@ManagedBean
@SessionScoped
public class CreateCohoEventController implements Serializable {


	private static final long serialVersionUID = 5716065327257928138L;
	
	private String cohoEventStartYear;
	private String cohoEventStartMonth;
	private String cohoEventStartDay;
	private String cohoEventEndYear;
	private String cohoEventEndMonth;
	private String cohoEventEndDay;
	private String slotNumberStart;
	private String slotNumberEnd;
	private List<MealDate> cohoEventDaysForPeriod;
	private int chosenEventDateTimestamp;
	private String chosenEventType;
	private EventService eventService = null;
	
	private String cohoEventName;
	private List<String> chosenSpaceList = new ArrayList<String>();
	private String cohoEventInfo;
		
	public CreateCohoEventController() {
		GregorianCalendar now = new GregorianCalendar();
		cohoEventStartYear = new Integer(now.get(Calendar.YEAR)).toString();
		cohoEventStartMonth = new Integer(now.get(Calendar.MONTH)).toString();
		cohoEventStartDay = new Integer(now.get(Calendar.DAY_OF_MONTH)).toString();
		cohoEventEndYear = new Integer(now.get(Calendar.YEAR)).toString();
		cohoEventEndMonth = new Integer(now.get(Calendar.MONTH)).toString();
		cohoEventEndDay = new Integer(now.get(Calendar.DAY_OF_MONTH)).toString();
	}

	private void clearFormFields() {
	
		GregorianCalendar now = new GregorianCalendar();
		cohoEventStartYear = new Integer(now.get(Calendar.YEAR)).toString();
		cohoEventStartMonth = new Integer(now.get(Calendar.MONTH)).toString();
		cohoEventStartDay = new Integer(now.get(Calendar.DAY_OF_MONTH)).toString();
		cohoEventEndYear = new Integer(now.get(Calendar.YEAR)).toString();
		cohoEventEndMonth = new Integer(now.get(Calendar.MONTH)).toString();
		cohoEventEndDay = new Integer(now.get(Calendar.DAY_OF_MONTH)).toString();

		cohoEventName = "";
		cohoEventInfo = "";
		chosenSpaceList = new ArrayList<String>();
		chosenEventType = "";
		slotNumberStart = "";
		slotNumberEnd = "";
	}

	public EventService getEventService() {
		return eventService;
	}
	
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public String getCohoEventName() {
		return cohoEventName;
	}

	public void setCohoEventName(String cohoEventName) {
		this.cohoEventName = cohoEventName;
	}
	
	public List<String> getChosenSpaceList() {
		return chosenSpaceList;
	}

	public void setChosenSpaceList(List<String> chosenSpaceList) {
		this.chosenSpaceList = chosenSpaceList;
	}

	public String getCohoEventInfo() {
		return cohoEventInfo;
	}

	public void setCohoEventInfo(String cohoEventInfo) {
		this.cohoEventInfo = cohoEventInfo;
	}

	public String getCohoEventYear() {
		return cohoEventStartYear;
	}

	public void setCohoEventYear(String cohoEventYear) {
		this.cohoEventStartYear = cohoEventYear;
		this.cohoEventEndYear = cohoEventYear;
	}

	public String getCohoEventMonth() {
		return cohoEventStartMonth;
	}

	public void setCohoEventMonth(String cohoEventMonth) {
		this.cohoEventStartMonth = cohoEventMonth;
		this.cohoEventEndMonth = cohoEventMonth;  
	}

	public String getCohoEventDay() {
		return cohoEventStartDay;
	}

	public void setCohoEventDay(String cohoEventDay) {
		this.cohoEventStartDay = cohoEventDay;
		this.cohoEventEndDay = cohoEventDay;
	}

/*
	public String getCohoEventEndYear() {
		return cohoEventEndYear;
	}

	public void setCohoEventEndYear(String cohoEventEndYear) {
		this.cohoEventEndYear = cohoEventEndYear;
	}

	public String getCohoEventEndMonth() {
		return cohoEventEndMonth;
	}

	public void setCohoEventEndMonth(String cohoEventEndMonth) {
		this.cohoEventEndMonth = cohoEventEndMonth;
	}

	public String getCohoEventEndDay() {
		return cohoEventEndDay;
	}

	public void setCohoEventEndDay(String cohoEventEndDay) {
		this.cohoEventEndDay = cohoEventEndDay;
	}
*/
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
		if (slotNumberStart != null && slotNumberStart.length() > 0) {
			slotStartAsInt = Integer.parseInt(slotNumberStart);
		} 
		int slotEndAsInt = 0;
		if (slotNumberEnd != null && slotNumberEnd.length() > 0) {
			slotEndAsInt = Integer.parseInt(slotNumberEnd);
		}
		
		if (slotEndAsInt > slotStartAsInt) {
			this.slotNumberEnd = slotNumberEnd;
		}
	}

	public int getChosenEventDateTimestamp() {
		return chosenEventDateTimestamp;
	}

	public void setChosenEventDateTimestamp(int chosenEventDateTimestamp) {
		this.chosenEventDateTimestamp = chosenEventDateTimestamp;
		for (MealDate mealDate : cohoEventDaysForPeriod) {
			if (mealDate.getTimestamp() == chosenEventDateTimestamp) {
				cohoEventStartYear = Integer.toString(mealDate.getTheYear());
				cohoEventStartMonth = Integer.toString(mealDate.getTheMonth().getMonthNumber());
				cohoEventStartDay = Integer.toString(mealDate.getTheDay());
				return;
			}
		}
		throw new RuntimeException ("Cannot find selected CohoDate object.");
	}

	public String addCohoEvent() throws Exception {
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

		Date eventDate = calculateTheStartDate().getTime();
		Date eventdateend = calculateTheEndDate().getTime();
	
		CohoEventDTO dto = createCohoEventDTO(eventDate);
		dto.setEventdateend(eventdateend);
		try {
		    eventService.createCohoEvent(dto);
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		
		// clear out fields for return to the page in this session
		clearFormFields();

		return "addCohoEvent";
	}
	
	// method to create a CohoEvent
	private CohoEventDTO createCohoEventDTO(Date eventDate) {
		CohoEventDTO dto = new CohoEventDTO();
		dto.setEventDate(eventDate);
		dto.setEventinfo(cohoEventInfo);
		dto.setEventName(cohoEventName);
		dto.setEventtype(chosenEventType);
		dto.setSpaceList(chosenSpaceList);

		// populate fields initially with what's in the session from login
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(false);
		User currentUser = (User) session
				.getAttribute(AuthenticateController.SESSIONVAR_USER_NAME);
		dto.setEnteredby(currentUser.getUserid());

		return dto;
	}

	private Calendar calculateTheStartDate() {
		CalendarUtils.TimeSlot[] slots = getTimeSlotsOfTheDay();
		int slotInt = Integer.parseInt(slotNumberStart);
		Calendar chosenTimeCal = new GregorianCalendar(Integer.parseInt(cohoEventStartYear), 
				Integer.parseInt(cohoEventStartMonth), Integer.parseInt(cohoEventStartDay), 
				slots[slotInt].getHour(), slots[slotInt].getMinutes());
		return chosenTimeCal;
	}
	
	private Calendar calculateTheEndDate() {
		CalendarUtils.TimeSlot[] slots = getTimeSlotsOfTheDay();
		int slotInt = Integer.parseInt(slotNumberEnd);
		Calendar chosenTimeCal = new GregorianCalendar(Integer.parseInt(cohoEventEndYear), 
				Integer.parseInt(cohoEventEndMonth), Integer.parseInt(cohoEventEndDay), 
				slots[slotInt].getHour(), slots[slotInt].getMinutes());
		return chosenTimeCal;
	}

	public CalendarUtils.TimeSlot[] getTimeSlotsOfTheDay() {
		return CalendarUtils.getTimeSlotsOfTheDay(Integer.parseInt(cohoEventStartYear),
				Integer.parseInt(cohoEventStartMonth));
	}

	public CalendarUtils.OneMonth[] getMonthsOfTheYear() {
		return CalendarUtils.getMonthsOfTheYear();
	}

	public String[] getDaysOfTheMonth() {
		return CalendarUtils.getDaysOfTheMonth(Integer.parseInt(cohoEventStartYear),
				Integer.parseInt(cohoEventStartMonth));
	}
	
	public String[] getYears() {
		return CalendarUtils.getYears();
	}

	public String[] getEventTypeChoices() {
		return EventTypeDefs.eventTypeChoicesNoApproval;
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
}
