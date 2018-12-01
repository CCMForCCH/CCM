package org.cohoman.model.service;

import java.util.Date;
import java.util.List;

import org.cohoman.model.business.MealRow;
import org.cohoman.model.business.MealSchedule.MealScheduleText;
import org.cohoman.model.business.User;
import org.cohoman.model.dto.CohoEventDTO;
import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.dto.MealEventDTO;
import org.cohoman.model.dto.PizzaEventDTO;
import org.cohoman.model.dto.PotluckEventDTO;
import org.cohoman.model.dto.PrivateEventDTO;
import org.cohoman.model.dto.SignupMealDTO;
import org.cohoman.model.dto.SignupPizzaDTO;
import org.cohoman.model.dto.SignupPotluckDTO;
import org.cohoman.model.dto.UserDTO;
import org.cohoman.model.integration.persistence.beans.CohoEvent;
import org.cohoman.model.integration.persistence.beans.MainCalendarEvent;
import org.cohoman.model.integration.persistence.beans.MealEvent;
import org.cohoman.model.integration.persistence.beans.PizzaEvent;
import org.cohoman.model.integration.persistence.beans.PotluckEvent;
import org.cohoman.model.integration.persistence.beans.PrivateEvent;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.model.integration.persistence.beans.SpaceBean;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.CalendarUtils.MealDate;

public interface EventService {
	
	// Meal Event services
	public void createMealEvent(MealEventDTO mealEventDTO) throws CohomanException;
	public void editMealEvent(MealEvent mealEvent) throws CohomanException;
	public void deleteMealEvent(MealEvent mealEvent) throws Exception;
	public List<MealEvent> getCurrentMealEvents();
	public List<MealEvent> getAllMealEvents();
	public MealEvent getMealEvent(Long eventId);
	public List<MealDate> getMealDaysForPeriod();

	// Pizza Event services
	public void createPizzaEvent(PizzaEventDTO pizzaEventDTO) throws CohomanException;
	public void editPizzaEvent(PizzaEvent pizzaEvent) throws CohomanException;
	public void deletePizzaEvent(PizzaEvent pizzaEvent) throws Exception;
	public List<PizzaEvent> getCurrentPizzaEvents();
	public PizzaEvent getPizzaEvent(Long eventId);
	public List<MealDate> getPizzaDaysForPeriod();
	
	// Potluck Event services
	public void createPotluckEvent(PotluckEventDTO potluckEventDTO) throws CohomanException;
	public void editPotluckEvent(PotluckEvent potluckEvent) throws CohomanException;
	public void deletePotluckEvent(PotluckEvent potluckEvent) throws Exception;
	public List<PotluckEvent> getCurrentPotluckEvents();
	public PotluckEvent getPotluckEvent(Long eventId);
	public List<MealDate> getPotluckDaysForPeriod();	
	
	// Coho Event services
	public void createCohoEvent(CohoEventDTO cohoEventDTO) throws CohomanException;
	public void editCohoEvent(CohoEvent cohoEvent) throws CohomanException;
	public void deleteCohoEvent(CohoEvent cohoEvent) throws Exception;
	public List<CohoEvent> getCurrentCohoEvents();
	public CohoEvent getCohoEvent(Long eventId);

	// Private Event services
	public void createPrivateEvent(PrivateEventDTO privateEventDTO) throws CohomanException;
	public void editPrivateEvent(PrivateEvent privateEvent) throws CohomanException;
	public void deletePrivateEvent(Long privateEventId) throws Exception;
	public List<PrivateEvent> getAllPrivateEvents();
	public List<PrivateEvent> getPendingPrivateEvents();
	public List<PrivateEvent> getMyPrivateEvents();
	public List<PrivateEvent> getUpcomingPrivateEvents();
	public PrivateEvent getPrivateEvent(Long eventId) throws CohomanException;

	// Common meal signups
	public void signupMeal(SignupMealDTO dto)throws CohomanException;
	public List<SignupMealDTO> getAllMealSignups(Long eventid)throws CohomanException;
	public void deleteSignupMeal(SignupMealDTO dto)throws CohomanException;

	// Pizza/potluck signups
	public void signupPizza(SignupPizzaDTO dto)throws CohomanException;
	public List<SignupPizzaDTO> getAllPizzaSignups(Long eventid)throws CohomanException;
	public void deleteSignupPizza(SignupPizzaDTO dto)throws CohomanException;

	// Potluck signups
	public void signupPotluck(SignupPotluckDTO dto)throws CohomanException;
	public List<SignupPotluckDTO> getAllPotluckSignups(Long eventid)throws CohomanException;
	public void deleteSignupPotluck(SignupPotluckDTO dto)throws CohomanException;

	public List<List<MealScheduleText>> getCurrentMealScheduleRows();
	
	public User authenticateUser(UserDTO theUser) throws Exception;
	public List<Role> getRolesForUser(Long userId);
	
	public List<MainCalendarEvent> getMonthsCalendarEvents(Date theMonth);
	
	public List<MainCalendarEvent> getMainCalendarEventsForDay(Date dateOfDay);

	public List<SpaceBean> getAllSpaces();

}
