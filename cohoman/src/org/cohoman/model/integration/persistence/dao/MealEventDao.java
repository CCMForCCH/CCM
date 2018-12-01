package org.cohoman.model.integration.persistence.dao;

import java.util.Date;
import java.util.List;

import org.cohoman.model.dto.MealEventDTO;
import org.cohoman.model.integration.persistence.beans.MealEvent;
import org.cohoman.view.controller.CohomanException;

public interface MealEventDao {
	public void createMealEvent(MealEventDTO dto) throws CohomanException;
	
	public List<MealEvent> getMealEvents();

	public List<MealEvent> getAllMealEvents();

	public MealEvent getMealEvent(Long eventId);
	
	public List<MealEvent> getMealEventsForDay(Date dateOfDay);

	public void updateMealEvent(MealEvent mealEvent) throws CohomanException;
	
	public void deleteMealEvent(MealEvent mealEvent);

}
