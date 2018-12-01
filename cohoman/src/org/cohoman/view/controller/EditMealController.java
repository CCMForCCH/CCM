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
import org.cohoman.model.dto.MealEventDTO;
import org.cohoman.model.integration.persistence.beans.MealEvent;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.model.service.EventService;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.CalendarUtils;

@ManagedBean
@SessionScoped
public class EditMealController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4678206276499587830L;

	Logger logger = Logger.getLogger(this.getClass().getName());

	private List<MealEvent> mealEventList;
	private MealEvent chosenMealEvent;
	private String chosenMealEventString;
	private String chosenUserString;
	private String mealOperation;
	private MealEventDTO mealEventDTO;
	private String slotNumber;
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

	public MealEventDTO getMealEventDTO() {
		return mealEventDTO;
	}

	public void setMealEventDTO(MealEventDTO mealEventDTO) {
		this.mealEventDTO = mealEventDTO;
	}

	private EventService eventService = null;

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public String getChosenMealEventString() {
		return chosenMealEventString;
	}

	public void setChosenMealEventString(String chosenMealEventString) {
		this.chosenMealEventString = chosenMealEventString;
	}

	public String getMealOperation() {
		return mealOperation;
	}

	public void setMealOperation(String mealOperation) {
		this.mealOperation = mealOperation;
	}

	public List<MealEvent> getMealEventList() {
		// if (mealEventList == null) {
		mealEventList = eventService.getCurrentMealEvents();
		// }
		mealEventList = filterOutPastMeals(mealEventList);
		return mealEventList;
	}

	public void setMealEventList(List<MealEvent> mealEventList) {
		this.mealEventList = mealEventList;
	}

	public MealEvent getChosenMealEvent() {
		return chosenMealEvent;
	}

	public void setChosenMealEvent(MealEvent chosenMealEvent) {
		this.chosenMealEvent = chosenMealEvent;
	}

	public String getChosenUserString() {
		return chosenUserString;
	}

	public void setChosenUserString(String chosenUserString) {
		this.chosenUserString = chosenUserString;
	}

	public CalendarUtils.TimeSlot[] getTimeSlotsOfTheDay() {
		return CalendarUtils.getTimeSlotsOfTheDay(2010, 1);
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

	public String editMealView() throws Exception {

		// convert chosenMealEventString to an id to find the Eventmeal
		if (chosenMealEventString == null || chosenMealEventString.length() == 0) {
			logger.log(Level.SEVERE,
					"Internal Error: invalid MealEventId parameter");
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage("Internal Error: invalid MealEventId parameter. Click on Main Menu link."));
			return null;
		}

		Long eventId = Long.valueOf(chosenMealEventString);
		chosenMealEvent = eventService.getMealEvent(eventId);
		
		// Error checking to make sure we got an event.
		if (chosenMealEvent == null) {
			logger.log(Level.SEVERE,
					"Internal Error: unable to find meal event for mealId " + 
					eventId);
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage("Internal Error: unable to find meal event. Click on Main Menu link."));
			return null;
		}

		
		slotNumber = Integer.toString((CalendarUtils
				.getTimeSlotForDate(chosenMealEvent.getEventDate())));
		if (mealOperation.equals("deleteMeal")) {
			eventService.deleteMealEvent(chosenMealEvent);
		}
		return mealOperation;
	}

	public String editMealItemsView() throws Exception {
		// Have to recompute Date based on potential time slot change
		int slotAsInt = Integer.parseInt(slotNumber);
		chosenMealEvent.setEventDate(CalendarUtils.setTimeSlotForDate(
				chosenMealEvent.getEventDate(), slotAsInt));

		// TODO Make this a config parameter
		// Add 1 hour to potentially updated new slot
		chosenMealEvent.setEventdateend(CalendarUtils.setTimeSlotForDate(
				chosenMealEvent.getEventDate(), slotAsInt + 2)); // 2 slots => 1 hr

		try {
			eventService.editMealEvent(chosenMealEvent);
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		return "gohome";
	}

	// Private method to remove meal events that shouldn't be displayed
	// to users in drop-downs since they've past and/or are closed.
	private List<MealEvent> filterOutPastMeals(List<MealEvent> mealList) {

		Calendar nowMinus1day = new GregorianCalendar();
		Calendar nowMinus7days = new GregorianCalendar();
		nowMinus1day.add(Calendar.DAY_OF_YEAR, -1);
		nowMinus7days.add(Calendar.DAY_OF_YEAR, -7);
		List<MealEvent> newMealEventList = new ArrayList<MealEvent>();
		for (MealEvent mealEvent : mealList) {
			Calendar mealEventCal = Calendar.getInstance();
			mealEventCal.setTime(mealEvent.getEventDate());

			FacesContext ctx = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) ctx.getExternalContext()
					.getSession(true);
			Role currentRole = (Role) session
					.getAttribute(AuthenticateController.SESSIONVAR_CHOSEN_ROLE);
			
			// Don't filter anything if role is Admin user. But
			// otherwise make other checks to only return meal
			// events that a user might reasonably edit/delete. Also,
			// give the Meal Leader 7 days after the meal to make
			// changes.
			User dbUser = userService.getUser(mealEvent.getCook1());

			if (!(currentRole.getRoleid() == Role.MEALADMIN_ID || dbUser
					.getUsername().equalsIgnoreCase(
							LoggingUtils.getCurrentUsername()))) {

				// Here if not a special user
				// If the date of the event is more than one day old, don't
				// let people access the meal by eliminating it from
				// the drop-down
				if (mealEventCal.before(nowMinus1day)) {
					// continue => don't add the meal to the drop-down list
					continue;
				}
			} else {

				// Here if Meal Leader or MEAL ADMIN
				if (dbUser.getUsername().equalsIgnoreCase(
						LoggingUtils.getCurrentUsername())
						&& currentRole.getRoleid() != Role.MEALADMIN_ID) {

					// Here only if Meal Leader. Need to filter out meals
					// that have occurred more than 7 days ago.
					if (mealEventCal.before(nowMinus7days)) {
						// continue => don't add the meal to the drop-down list
						continue;
					}
				}
			}
			
			newMealEventList.add(mealEvent);
		}

		return newMealEventList;
	}
}
