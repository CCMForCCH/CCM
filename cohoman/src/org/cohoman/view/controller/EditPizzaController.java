package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.enterprise.context.SessionScoped;
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
	
	private List<PizzaEvent> pizzaEventList;
	private PizzaEvent chosenPizzaEvent;
	private String chosenPizzaEventString;
	private String pizzaOperation;
	private String eventName;
	private String eventinfo;
	private PizzaEventDTO pizzaEventDTO;
	private String slotNumber;
	private String chosenUserString;
	private UserService userService = null;
	
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
		
		pizzaEventList = eventService.getCurrentPizzaEvents();

		// filter out meals that have passed for all users
		// except the meal admin
		pizzaEventList = filterOutPastMeals(pizzaEventList);
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
		
		// convert chosenMealEventString to an id to find the Eventmeal
		Long eventId = Long.valueOf(chosenPizzaEventString);
		chosenPizzaEvent = eventService.getPizzaEvent(eventId);
		slotNumber = 
			Integer.toString((CalendarUtils.getTimeSlotForDate(chosenPizzaEvent.getEventDate())));
		if (pizzaOperation.equals("deletePizza")) {
			eventService.deletePizzaEvent(chosenPizzaEvent);
		}
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
