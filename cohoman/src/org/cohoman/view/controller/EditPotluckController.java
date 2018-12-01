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
import org.cohoman.model.dto.PotluckEventDTO;
import org.cohoman.model.integration.persistence.beans.PotluckEvent;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.model.service.EventService;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.CalendarUtils;

@ManagedBean
@SessionScoped
public class EditPotluckController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4678206276499587830L;

	private List<PotluckEvent> potluckEventList;
	private PotluckEvent chosenPotluckEvent;
	private String chosenPotluckEventString;
	private String potluckOperation;
	private PotluckEventDTO potluckEventDTO;
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

	public PotluckEventDTO getPotluckEventDTO() {
		return potluckEventDTO;
	}

	public void setPotluckEventDTO(PotluckEventDTO potluckEventDTO) {
		this.potluckEventDTO = potluckEventDTO;
	}

	private EventService eventService = null;

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public EditPotluckController() {
	}

	public String getChosenPotluckEventString() {
		return chosenPotluckEventString;
	}

	public void setChosenPotluckEventString(String chosenPotluckEventString) {
		this.chosenPotluckEventString = chosenPotluckEventString;
	}

	public String getPotluckOperation() {
		return potluckOperation;
	}

	public void setPotluckOperation(String potluckOperation) {
		this.potluckOperation = potluckOperation;
	}

	public List<PotluckEvent> getPotluckEventList() {

		potluckEventList = eventService.getCurrentPotluckEvents();

		// filter out meals that have passed for all users
		// except the meal admin
		potluckEventList = filterOutPastMeals(potluckEventList);
		return potluckEventList;
	}

	public void setPotluckEventList(List<PotluckEvent> potluckEventList) {
		this.potluckEventList = potluckEventList;
	}

	public PotluckEvent getChosenPotluckEvent() {
		return chosenPotluckEvent;
	}

	public void setChosenPotluckEvent(PotluckEvent chosenPotluckEvent) {
		this.chosenPotluckEvent = chosenPotluckEvent;
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
		// First time thru, set user to first of list as that's what's
		// displayed.
		if (chosenUserString == null) {
			chosenUserString = fullUserList.get(0).getUserid().toString();
		}

		return fullUserList;
	}

	public String editPotluckView() throws Exception {

		// convert chosenMealEventString to an id to find the Eventmeal
		Long eventId = Long.valueOf(chosenPotluckEventString);
		chosenPotluckEvent = eventService.getPotluckEvent(eventId);
		slotNumber = Integer.toString((CalendarUtils
				.getTimeSlotForDate(chosenPotluckEvent.getEventDate())));
		if (potluckOperation.equals("deletePotluck")) {
			eventService.deletePotluckEvent(chosenPotluckEvent);
		}
		return potluckOperation;
	}

	public String editPotluckItemsView() throws Exception {
		eventService.editPotluckEvent(chosenPotluckEvent);
		return "gohome";
	}

	// Private method to remove meal events that shouldn't be displayed
	// to users in drop-downs since they've past and/or are closed.
	private List<PotluckEvent> filterOutPastMeals(List<PotluckEvent> potluckList) {

		GregorianCalendar now = new GregorianCalendar();
		Calendar nowMinus7days = now;
		nowMinus7days.add(Calendar.DAY_OF_YEAR, -7);
		List<PotluckEvent> newPotluckEventList = new ArrayList<PotluckEvent>();
		for (PotluckEvent potluckEvent : potluckList) {
			Calendar potluckEventCal = Calendar.getInstance();
			potluckEventCal.setTime(potluckEvent.getEventDate());

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
				if (potluckEventCal.before(nowMinus7days)) {
					continue;
				}
			}
			newPotluckEventList.add(potluckEvent);
		}

		return newPotluckEventList;
	}

}
