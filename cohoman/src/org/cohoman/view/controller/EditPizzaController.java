package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.cohoman.model.business.User;
import org.cohoman.model.dto.PizzaEventDTO;
import org.cohoman.model.integration.persistence.beans.PizzaEvent;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.model.service.EventService;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.CalendarUtils;

@ManagedBean
@SessionScoped
public class EditPizzaController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4678206276499587830L;
	
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	private List<PizzaEvent> pizzaEventList;
	private PizzaEvent chosenPizzaEvent;
	private String chosenPizzaEventString;
	private String pizzaOperation;
	private String eventName;
	private String eventinfo;
	private PizzaEventDTO pizzaEventDTO;
	private String slotNumber;
	private String chosenUserString;
	private long lastPizzaDaysLoadedTime;
	private UserService userService = null;
	
	private void clearFormFields() {
		// Make the user choose again
		chosenPizzaEventString = "1";
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}

	public PizzaEventDTO getPizzaEventDTO() {
		return pizzaEventDTO;
	}

	public void setPizzaEventDTO(PizzaEventDTO pizzaEventDTO) {
		this.pizzaEventDTO = pizzaEventDTO;
	}

	private EventService eventService = null;
	
	public EventService getEventService() {
		return eventService;
	}
	
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public EditPizzaController() {
	}
	
	public String getChosenPizzaEventString() {
		return chosenPizzaEventString;
	}

	public void setChosenPizzaEventString(String chosenPizzaEventString) {
		this.chosenPizzaEventString = chosenPizzaEventString;
	}

	public String getPizzaOperation() {
		return pizzaOperation;
	}

	public void setPizzaOperation(String pizzaOperation) {
		this.pizzaOperation = pizzaOperation;
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

	public List<PizzaEvent> getPizzaEventList() {
		
		// Only go to the database once per page. Do this by setting up
		// a cache of sorts. If more than 500 ms have passed since last 
		// access to the database, read it in again. Otherwise, just 
		// return the list we got from the database before.

		Logger logger = Logger.getLogger(this.getClass().getName());
		if (pizzaEventList == null) {
			//logger.info("AUDIT: inside getPizzaEventList() with pizzaEventList being null.");
		} else {
			//logger.info("AUDIT: inside getPizzaEventList() with isEmpty = " + getPizzaEventList.isEmpty() );
		}

		Calendar calNow = Calendar.getInstance();
		long currentTimeInMS = calNow.getTimeInMillis();
		
		// If the list is empty or the 2 times differ by more
		// than 500 ms., read from the database. Otherwise, use
		// the list we just recently got from the database.
		long timeDiff = (currentTimeInMS - lastPizzaDaysLoadedTime);
		lastPizzaDaysLoadedTime = currentTimeInMS;
		//logger.info("AUDIT: inside getPizzaEventList() with timediff1 = " + timeDiff );

		if (pizzaEventList == null || pizzaEventList.isEmpty()
				|| timeDiff > 500L) {

			//logger.info("AUDIT: inside getPizzaDaysForPeriod() with timediff2 = "
			//		+ timeDiff);

			pizzaEventList = eventService.getCurrentPizzaEvents();

			// filter out meals that have passed for all users
			// except the meal admin
			pizzaEventList = filterOutPastMeals(pizzaEventList);

			// added 12/28/18 to make users choose the meal
			if (pizzaEventList != null && !pizzaEventList.isEmpty()
					&& chosenPizzaEventString == null) {
				chosenPizzaEventString = "1";
			}
		}

		return pizzaEventList;
	}

	public void setPizzaEventList(List<PizzaEvent> pizzaEventList) {
		this.pizzaEventList = pizzaEventList;
	}

	public PizzaEvent getChosenPizzaEvent() {
		return chosenPizzaEvent;
	}

	public void setChosenPizzaEvent(PizzaEvent chosenPizzaEvent) {
		this.chosenPizzaEvent = chosenPizzaEvent;
	}

	public CalendarUtils.TimeSlot[] getTimeSlotsOfTheDay() {
		return CalendarUtils.getTimeSlotsOfTheDay(2010, 1);
	}

	public String getChosenUserString() {
		return chosenUserString;
	}

	public void setChosenUserString(String chosenUserString) {
		this.chosenUserString = chosenUserString;
	}

	public List<User> getUserList() {
		List<User> fullUserList = userService.getUsersHereNow();
		// First time thru, set user to first of list as that's what's displayed.
		if (chosenUserString == null) {
			chosenUserString = fullUserList.get(0).getUserid().toString();
		}
	
		return fullUserList;
	}

	public String editPizzaView() throws Exception {
		
		// convert chosenPizzaEventString to an id to find the Eventmeal
		if (chosenPizzaEventString == null || chosenPizzaEventString.length() == 0) {
			logger.log(Level.SEVERE,
					"Internal Error: invalid PizzaEventId parameter");
			FacesMessage message = new FacesMessage(
					"Internal Error: invalid PizzaEventId parameter. Click on Main Menu link.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		// Error check that a meal has been chosen
		if (chosenPizzaEventString.equalsIgnoreCase("1")) {
			FacesMessage message = new FacesMessage(
					"User Error: you must choose a pizza/potluck to change or delete.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		// convert chosenMealEventString to an id to find the Eventmeal
		Long eventId = Long.valueOf(chosenPizzaEventString);
		chosenPizzaEvent = eventService.getPizzaEvent(eventId);
		slotNumber = 
			Integer.toString((CalendarUtils.getTimeSlotForDate(chosenPizzaEvent.getEventDate())));
		if (pizzaOperation.equals("deletePizza")) {
			eventService.deletePizzaEvent(chosenPizzaEvent);
		}
		
		// Clear fields for next time.
		clearFormFields();

		return pizzaOperation;
	}
	
	public String editPizzaItemsView() throws Exception {
		eventService.editPizzaEvent(chosenPizzaEvent);
		return "gohome";
	}
	
	// Private method to remove meal events that shouldn't be displayed
	// to users in drop-downs since they've past and/or are closed.
	private List<PizzaEvent> filterOutPastMeals(List<PizzaEvent> pizzaList) {

		GregorianCalendar now = new GregorianCalendar();
		Calendar nowMinus7days = now;
		nowMinus7days.add(Calendar.DAY_OF_YEAR, -7);
		List<PizzaEvent> newPizzaEventList = new ArrayList<PizzaEvent>();
		for (PizzaEvent pizzaEvent : pizzaList) {
			Calendar pizzaEventCal = Calendar.getInstance();
			pizzaEventCal.setTime(pizzaEvent.getEventDate());

			FacesContext ctx = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) ctx.getExternalContext()
					.getSession(true);
			Role currentRole = (Role) session
					.getAttribute(AuthenticateController.SESSIONVAR_CHOSEN_ROLE);
			
			// Don't filter anything if role is Admin user. But 
			// otherwise make other checks to only return meal
			// events that a user might reasonably edit/delete
			if (currentRole.getRoleid() != Role.MEALADMIN_ID) {

				// If the date of the event is more than 7 days old, ignore it
				if (pizzaEventCal.before(nowMinus7days)) {
					continue;
				}

				// If the meal is closed, also ignore it
				if (pizzaEvent.isIsmealclosed()) {
					continue;
				}
			}
			newPizzaEventList.add(pizzaEvent);
		}

		return newPizzaEventList;
	}

}
