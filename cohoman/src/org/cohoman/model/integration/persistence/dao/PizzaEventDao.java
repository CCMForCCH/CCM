package org.cohoman.model.integration.persistence.dao;

import java.util.Date;
import java.util.List;

import org.cohoman.model.dto.PizzaEventDTO;
import org.cohoman.model.integration.persistence.beans.PizzaEvent;
import org.cohoman.view.controller.CohomanException;

public interface PizzaEventDao {
	public void createPizzaEvent(PizzaEventDTO dto) throws CohomanException ;
	
	public PizzaEvent getPizzaEvent(Long eventId);
	
	public List<PizzaEvent> getPizzaEvents();
	
	public List<PizzaEvent> getPizzaEventsForDay(Date dateOfDay);
		
	public void updatePizzaEvent(PizzaEvent pizzaEvent) throws CohomanException;
	
	public void deletePizzaEvent(PizzaEvent pizzaEvent);

}
