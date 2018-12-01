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
import org.cohoman.model.dto.PizzaEventDTO;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.model.integration.persistence.dao.ConfigInfo;
import org.cohoman.model.service.EventService;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.CalendarUtils.MealDate;

@ManagedBean
@SessionScoped
public class CreatePizzaController implements Serializable {

	// Action: call the service layer to build and return a schedule
	// which is basically a list of weeks of days containing events

	/**
	 * 
	 */
	private static final long serialVersionUID = 4678206276499587830L;
	private String pizzaYear;
	private String pizzaMonth;
	private String pizzaDay;
	private List<MealDate> pizzaDaysForPeriod;
	private int chosenPizzaDateTimestamp;
	private String eventName;
	private String eventinfo;
	private EventService eventService = null;
	private String leader1 = null;
	private String chosenUserString;
	private UserService userService = null;

	
	public CreatePizzaController() {
		GregorianCalendar now = new GregorianCalendar();
		pizzaYear = new Integer(now.get(Calendar.YEAR)).toString();
		pizzaMonth = new Integer(now.get(Calendar.MONTH)).toString();
		pizzaDay = new Integer(now.get(Calendar.DAY_OF_MONTH)).toString();
	}

	private void clearFormFields() {
		
		eventName = "";
		eventinfo = "";
		chosenUserString = "";
	}

	public EventService getEventService() {
		return eventService;
	}
	
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public int getChosenPizzaDateTimestamp() {
		return chosenPizzaDateTimestamp;
	}

	public void setChosenPizzaDateTimestamp(int chosenPizzaDateTimestamp) {
		this.chosenPizzaDateTimestamp = chosenPizzaDateTimestamp;
		for (MealDate mealDate : pizzaDaysForPeriod) {
			if (mealDate.getTimestamp() == chosenPizzaDateTimestamp) {
				pizzaYear = Integer.toString(mealDate.getTheYear());
				pizzaMonth = Integer.toString(mealDate.getTheMonth().getMonthNumber());
				pizzaDay = Integer.toString(mealDate.getTheDay());
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

	public String getChosenUserString() {
		return chosenUserString;
	}

	public void setChosenUserString(String chosenUserString) {
		this.chosenUserString = chosenUserString;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventinfo() {
		return eventinfo;
	}

	public void setEventinfo(String eventinfo) {
		this.eventinfo = eventinfo;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String addPizza() throws Exception {
		Date eventDate = calculateTheDate();
		Calendar endDateCal = Calendar.getInstance();
		endDateCal.setTime(eventDate);
		endDateCal.add(Calendar.HOUR_OF_DAY, 1); // assume meals are one hour for now
		Date eventdateend = endDateCal.getTime();
		PizzaEventDTO dto = createPizzaEventDTO(eventDate);
		dto.setEventdateend(eventdateend);
		try {
			eventService.createPizzaEvent(dto);
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		
		clearFormFields();
		
		return "addPizza";
	}
	
	public List<MealDate> getPizzaDaysForPeriod() {
		pizzaDaysForPeriod = eventService.getPizzaDaysForPeriod();
		
		// Filter out days that have passed for everyone 
		// except the meal admin
		pizzaDaysForPeriod = filterOutPastMeals(pizzaDaysForPeriod);
		return pizzaDaysForPeriod;
	}

	public List<User> getUserList() {
		List<User> fullUserList = userService.getUsersHereNow();
		// First time thru, set user to first of list as that's what's displayed.
		if (chosenUserString == null) {
			chosenUserString = fullUserList.get(0).getUserid().toString();
		}
		
		return fullUserList;
	}

	// method to create a PizzaEvent
	private PizzaEventDTO createPizzaEventDTO(Date eventDate) {
		PizzaEventDTO dto = new PizzaEventDTO();
		dto.setEventDate(eventDate);
		dto.setLeader1(Long.valueOf(chosenUserString));
		dto.setEventName(eventName);
		dto.setEventinfo(eventinfo);
		return dto;
	}

	private Date calculateTheDate() {
		int hour = (new ConfigInfo()).getPizzaStarttimeHour();
		Calendar chosenTimeCal = new GregorianCalendar(Integer.parseInt(pizzaYear), 
				Integer.parseInt(pizzaMonth), Integer.parseInt(pizzaDay), 
				hour, 0);
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
