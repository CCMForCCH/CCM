package org.cohoman.model.business;

import java.util.Date;
import java.util.List;

import org.cohoman.model.business.MealSchedule.MealScheduleText;
import org.cohoman.model.dto.CohoEventDTO;
import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.dto.MealEventDTO;
import org.cohoman.model.dto.PizzaEventDTO;
import org.cohoman.model.dto.PotluckEventDTO;
import org.cohoman.model.dto.PrivateEventDTO;
import org.cohoman.model.dto.SignupMealDTO;
import org.cohoman.model.dto.SignupPizzaDTO;
import org.cohoman.model.dto.SignupPotluckDTO;
import org.cohoman.model.integration.persistence.beans.CohoEvent;
import org.cohoman.model.integration.persistence.beans.MainCalendarEvent;
import org.cohoman.model.integration.persistence.beans.MealEvent;
import org.cohoman.model.integration.persistence.beans.PizzaEvent;
import org.cohoman.model.integration.persistence.beans.PotluckEvent;
import org.cohoman.model.integration.persistence.beans.PrivateEvent;
import org.cohoman.model.integration.persistence.beans.SpaceBean;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.CalendarUtils.MealDate;

public interface EventManager {
	public void createMealEvent(MealEventDTO mealEventDTO)
			throws CohomanException;

	// Meal events
	public void editMealEvent(MealEvent mealEvent) throws CohomanException;

	public void deleteMealEvent(MealEvent mealEvent);

	public List<MealDate> getMealDaysForPeriod();

	public List<MealEvent> getCurrentMealEvents();

	public List<MealEvent> getAllMealEvents();

	public MealEvent getMealEvent(Long eventId);

	// Pizza Events
	public void createPizzaEvent(PizzaEventDTO pizzaEventDTO)
			throws CohomanException;

	public void editPizzaEvent(PizzaEvent pizzaEvent) throws CohomanException;

	public void deletePizzaEvent(PizzaEvent pizzaEvent);

	public List<MealDate> getPizzaDaysForPeriod();

	public List<PizzaEvent> getCurrentPizzaEvents();

	public PizzaEvent getPizzaEvent(Long eventId);

	// Potluck Events
	public void createPotluckEvent(PotluckEventDTO potluckEventDTO)
	throws CohomanException;

	public void editPotluckEvent(PotluckEvent potluckEvent) throws CohomanException;

	public void deletePotluckEvent(PotluckEvent potluckEvent);

	public List<MealDate> getPotluckDaysForPeriod();

	public List<PotluckEvent> getCurrentPotluckEvents();

	public PotluckEvent getPotluckEvent(Long eventId);


	// Coho Events
	public void createCohoEvent(CohoEventDTO cohoEventDTO)
			throws CohomanException;

	public void editCohoEvent(CohoEvent cohoEvent) throws CohomanException;

	public void deleteCohoEvent(CohoEvent cohoEvent);

	public List<CohoEvent> getCurrentCohoEvents();

	public CohoEvent getCohoEvent(Long eventId);

	// Private Events
	public void createPrivateEvent(PrivateEventDTO privateEventDTO)
			throws CohomanException;

	public void editPrivateEvent(PrivateEvent privateEvent)
			throws CohomanException;

	public void deletePrivateEvent(Long privateEventId);

	public List<PrivateEvent> getMyPrivateEvents();

	public List<PrivateEvent> getUpcomingPrivateEvents();

	public List<PrivateEvent> getPendingPrivateEvents();

	public List<PrivateEvent> getAllPrivateEvents();

	public PrivateEvent getPrivateEvent(Long eventId);

	// meal sign-ups
	public void signupForMeal(SignupMealDTO dto) throws CohomanException;
	public List<SignupMealDTO> getAllMealSignups(Long eventid) throws CohomanException;
	public void deleteSignupForMeal(SignupMealDTO dto) throws CohomanException;

	// pizza/potluck sign-ups
	public void signupForPizza(SignupPizzaDTO dto) throws CohomanException;
	public List<SignupPizzaDTO> getAllPizzaSignups(Long eventid) throws CohomanException;
	public void deleteSignupForPizza(SignupPizzaDTO dto) throws CohomanException;

	// potluck sign-ups
	public void signupForPotluck(SignupPotluckDTO dto) throws CohomanException;
	public List<SignupPotluckDTO> getAllPotluckSignups(Long eventid) throws CohomanException;
	public void deleteSignupForPotluck(SignupPotluckDTO dto) throws CohomanException;

	// Other special Getters
	public List<List<MealScheduleText>> getCurrentMealScheduleRows();

	public List<MainCalendarEvent> getMonthsCalendarEvents(Date theMonth);

	public List<MainCalendarEvent> getMainCalendarEventsForDay(Date dateOfDay);

	public List<SpaceBean> getAllSpaces();

}
