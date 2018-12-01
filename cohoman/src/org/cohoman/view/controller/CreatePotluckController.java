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
import org.cohoman.model.dto.PotluckEventDTO;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.model.service.EventService;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.cohoman.view.controller.utils.CalendarUtils.MealDate;

@ManagedBean
@SessionScoped
public class CreatePotluckController implements Serializable {

	// Action: call the service layer to build and return a schedule
	// which is basically a list of weeks of days containing events

	/**
	 * 
	 */
	private static final long serialVersionUID = 4678206276499587830L;
	private String potluckYear;
	private String potluckMonth;
	private String potluckDay;
	private String slotNumber = "";
	private List<MealDate> potluckDaysForPeriod;
	private int chosenPotluckDateTimestamp;
	private EventService eventService = null;
	private String leader1 = null;
	private String potluckInfo = null;
	private String chosenUserString;
	private UserService userService = null;

	
	public CreatePotluckController() {
		GregorianCalendar now = new GregorianCalendar();
		potluckYear = new Integer(now.get(Calendar.YEAR)).toString();
		potluckMonth = new Integer(now.get(Calendar.MONTH)).toString();
		potluckDay = new Integer(now.get(Calendar.DAY_OF_MONTH)).toString();
	}

	private void clearFormFields() {
		
		slotNumber = "";
		potluckInfo = "";
		leader1 = null;
		chosenUserString = "";
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

	public int getChosenPotluckDateTimestamp() {
		return chosenPotluckDateTimestamp;
	}

	public void setChosenPotluckDateTimestamp(int chosenPotluckDateTimestamp) {
		this.chosenPotluckDateTimestamp = chosenPotluckDateTimestamp;
		for (MealDate mealDate : potluckDaysForPeriod) {
			if (mealDate.getTimestamp() == chosenPotluckDateTimestamp) {
				potluckYear = Integer.toString(mealDate.getTheYear());
				potluckMonth = Integer.toString(mealDate.getTheMonth().getMonthNumber());
				potluckDay = Integer.toString(mealDate.getTheDay());
				return;
			}
		}
		throw new RuntimeException ("Cannot find selected MealDate object.");
	}

	public String getLeader1() {
		return leader1;
	}

	public void setLeader1(String leader) {
		this.leader1 = leader;
	}

	public String getPotluckInfo() {
		return potluckInfo;
	}

	public void setPotluckInfo(String potluckInfo) {
		this.potluckInfo = potluckInfo;
	}

	public String getChosenUserString() {
		return chosenUserString;
	}

	public void setChosenUserString(String chosenUserString) {
		this.chosenUserString = chosenUserString;
	}

	public String getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}

	public String addPotluck() throws Exception {
		Date eventDate = calculateTheDate();
		Calendar endDateCal = Calendar.getInstance();
		endDateCal.setTime(eventDate);
		endDateCal.add(Calendar.HOUR_OF_DAY, 1); // assume meals are 1 hour for now
		Date eventdateend = endDateCal.getTime();
		PotluckEventDTO dto = createPotluckEventDTO(eventDate);
		dto.setEventdateend(eventdateend);

		try {
			eventService.createPotluckEvent(dto);
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		// clear out fields for return to the page in this session
		clearFormFields();

		return "addPotluck";
	}
	
	public List<MealDate> getPotluckDaysForPeriod() {

		potluckDaysForPeriod = eventService.getPotluckDaysForPeriod();
		
		// Filter out days that have passed for everyone 
		// except the meal admin
		potluckDaysForPeriod = filterOutPastMeals(potluckDaysForPeriod);
		return potluckDaysForPeriod;
	}

	public List<User> getUserList() {
		List<User> fullUserList = userService.getUsersHereNow();
		// First time thru, set user to first of list as that's what's displayed.
		if (chosenUserString == null) {
			chosenUserString = fullUserList.get(0).getUserid().toString();
		}
		
		return fullUserList;
	}

	// method to create a PotluckEvent
	private PotluckEventDTO createPotluckEventDTO(Date eventDate) {
		PotluckEventDTO dto = new PotluckEventDTO();
		dto.setEventDate(eventDate);
		dto.setLeader1(Long.valueOf(chosenUserString));
		dto.setEventinfo(potluckInfo);
		return dto;
	}

	private Date calculateTheDate() {
/*
		int hour = (new ConfigInfo()).getPotluckStarttimeHour();
		Calendar chosenTimeCal = new GregorianCalendar(Integer.parseInt(potluckYear), 
				Integer.parseInt(potluckMonth), Integer.parseInt(potluckDay), 
				hour, 0);
		Date chosenDate = chosenTimeCal.getTime();
		return chosenDate;
*/
		
		CalendarUtils.TimeSlot[] slots = getTimeSlotsOfTheDay();
		int slotInt = Integer.parseInt(slotNumber);
		Calendar chosenTimeCal = new GregorianCalendar(Integer.parseInt(potluckYear), 
				Integer.parseInt(potluckMonth), Integer.parseInt(potluckDay), 
				slots[slotInt].getHour(), slots[slotInt].getMinutes());
		
		Date chosenDate = chosenTimeCal.getTime();
		return chosenDate;
	}
	
	// Private method to remove meal events that shouldn't be displayed
	// to users in drop-downs since they've past and/or are closed.
	private List<MealDate> filterOutPastMeals(List<MealDate> mealList) {

		GregorianCalendar now = new GregorianCalendar();
		List<MealDate> newMealDateList = new ArrayList<MealDate>();
		for (MealDate mealDate : mealList) {
			Calendar mealDateCal = Calendar.getInstance();
			mealDateCal.set(mealDate.getTheYear(), mealDate.getTheMonth().getMonthNumber(), mealDate.getTheDay());

			FacesContext ctx = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) ctx.getExternalContext()
					.getSession(true);
			Role currentRole = (Role) session
					.getAttribute(AuthenticateController.SESSIONVAR_CHOSEN_ROLE);
			
			// Don't filter anything if role is Admin user. But 
			// otherwise make other checks to only return meal
			// events that a user might reasonably edit/delete
			if (currentRole.getRoleid() != Role.MEALADMIN_ID) {

				// If the date of the event has passed, don't
				// let people create a new meal for that date
				if (mealDateCal.before(now)) {
					continue;
				}

			}
			newMealDateList.add(mealDate);
		}

		return newMealDateList;
	}

	public CalendarUtils.TimeSlot[] getTimeSlotsOfTheDay() {
		return CalendarUtils.getTimeSlotsOfTheDay(Integer.parseInt(potluckYear),
				Integer.parseInt(potluckMonth));
	}

}
