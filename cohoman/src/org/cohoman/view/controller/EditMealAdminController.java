package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.dto.MealEventDTO;
import org.cohoman.model.integration.persistence.beans.MealEvent;
import org.cohoman.model.service.EventService;
import org.cohoman.view.controller.utils.CalendarUtils;

@ManagedBean
@SessionScoped
public class EditMealAdminController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4678206276499587830L;

	private List<MealEvent> mealEventList;
	private MealEvent chosenMealEvent;
	private String chosenMealEventString;
	private MealEventDTO mealEventDTO;
	private String slotNumber;
	private EventService eventService = null;

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

	public List<MealEvent> getMealEventList() {
		// if (mealEventList == null) {
		mealEventList = eventService.getAllMealEvents();			
		// }
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

	public CalendarUtils.TimeSlot[] getTimeSlotsOfTheDay() {
		return CalendarUtils.getTimeSlotsOfTheDay(2010, 1);
	}

	public String editMealView() throws Exception {

		Long eventId = Long.valueOf(chosenMealEventString);
		chosenMealEvent = eventService.getMealEvent(eventId);
		slotNumber = Integer.toString((CalendarUtils
				.getTimeSlotForDate(chosenMealEvent.getEventDate())));

		// Only operation/option is to delete meal
		eventService.deleteMealEvent(chosenMealEvent);
		return "deletedSpecificMeal";
	}

}
