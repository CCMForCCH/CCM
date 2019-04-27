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
import org.cohoman.model.dto.MealEventDTO;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.model.service.EventService;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.cohoman.view.controller.utils.CalendarUtils.MealDate;

@ManagedBean
@SessionScoped
public class CreateMealController implements Serializable {

	// Action: call the service layer to build and return a schedule
	// which is basically a list of weeks of days containing events

	/**
	 * 
	 */
	private static final long serialVersionUID = 4678206276499587830L;
	private String mealYear;
	private String mealMonth;
	private String mealDay;
	private String slotNumber = "";
	private String menu = null;
	private Long cook1 = null;
	private List<MealDate> mealDaysForPeriod;
	private int chosenMealDateTimestamp;
	private String chosenUserString;
	private EventService eventService = null;
	private UserService userService = null;

	public CreateMealController() {
		GregorianCalendar now = new GregorianCalendar();
		mealYear = new Integer(now.get(Calendar.YEAR)).toString();
		mealMonth = new Integer(now.get(Calendar.MONTH)).toString();
		mealDay = new Integer(now.get(Calendar.DAY_OF_MONTH)).toString();
	}

	private void clearFormFields() {
		
		slotNumber = "";
		menu = "";
		cook1 = null;
		chosenUserString = "";
		chosenMealDateTimestamp = 0;
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

	public int getChosenMealDateTimestamp() {
		return chosenMealDateTimestamp;
	}

	public void setChosenMealDateTimestamp(int chosenMealDateTimestamp) {
		
		// Let this value be 0 (which it will be if the user failed
		// to select a date). There's a later check in addMeal()
		// for just this case.
		if (chosenMealDateTimestamp == 0) {
			return;
		}
		
		this.chosenMealDateTimestamp = chosenMealDateTimestamp;
		for (MealDate mealDate : mealDaysForPeriod) {
			if (mealDate.getTimestamp() == chosenMealDateTimestamp) {
				mealYear = Integer.toString(mealDate.getTheYear());
				mealMonth = Integer.toString(mealDate.getTheMonth().getMonthNumber());
				mealDay = Integer.toString(mealDate.getTheDay());
				return;
			}
		}
		throw new RuntimeException ("Cannot find selected MealDate object.");
	}

	
	public String getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}

	public CalendarUtils.TimeSlot[] getTimeSlotsOfTheDay() {
		return CalendarUtils.getTimeSlotsOfTheDay(Integer.parseInt(mealYear),
				Integer.parseInt(mealMonth));
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public void setCook1(Long cook1) {
		this.cook1 = cook1;
	}

	public Long getCook1() {
		return cook1;
	}

	public String getChosenUserString() {
		return chosenUserString;
	}

	public void setChosenUserString(String chosenUserString) {
		this.chosenUserString = chosenUserString;
	}

	public List<User> getUserList() {
		List<User> fullUserList = userService.getUsersHereNow();
		// as of 12/29/18 null => choose user
		// First time thru, set user to first of list as that's what's displayed.
		//if (chosenUserString == null) {
			//chosenUserString = fullUserList.get(0).getUserid().toString();
		//}
		
		return fullUserList;
	}

	public String addMeal() throws Exception {
		
		// Error check first that a leader has been chosen
		if (chosenUserString == null || chosenUserString.isEmpty()) {
			FacesMessage message = new FacesMessage(
					"User Error: you must choose a lead cook for the meal.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		// Error checkk that the date has been specified
		if (chosenMealDateTimestamp == 0) {
			FacesMessage message = new FacesMessage(
					"User Error: you must choose the date for the meal.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		Date eventDate = calculateTheDate();
		//TODO: calculate ending time from new duration field
		Calendar endDateCal = Calendar.getInstance();
		endDateCal.setTime(eventDate);
		endDateCal.add(Calendar.HOUR_OF_DAY, 1); // assume meals are 1 hour for now
		Date eventdateend = endDateCal.getTime();
		MealEventDTO dto = createMealEventDTO(eventDate);
		dto.setEventdateend(eventdateend);
		try {
			eventService.createMealEvent(dto);
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		// clear out fields for return to the page in this session
		clearFormFields();
		
		return "addMeal";
	}
	
	// method to create a MealEvent
	private MealEventDTO createMealEventDTO(Date eventDate) {
		MealEventDTO dto = new MealEventDTO();
		dto.setCook1(Long.valueOf(chosenUserString));
		dto.setEventDate(eventDate);
		dto.setMenu(menu);
		return dto;
	}

	public List<MealDate> getMealDaysForPeriod() {
		mealDaysForPeriod = eventService.getMealDaysForPeriod();
		
		// Filter out days that have past
		mealDaysForPeriod = filterOutPastMeals(mealDaysForPeriod);
		return mealDaysForPeriod;
	}
	
	private Date calculateTheDate() {
		CalendarUtils.TimeSlot[] slots = getTimeSlotsOfTheDay();
		int slotInt = Integer.parseInt(slotNumber);
		Calendar chosenTimeCal = new GregorianCalendar(Integer.parseInt(mealYear), 
				Integer.parseInt(mealMonth), Integer.parseInt(mealDay), 
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

}
