package org.cohoman.model.integration.persistence.dao;

import java.util.Date;
import java.util.List;

import org.cohoman.model.dto.PotluckEventDTO;
import org.cohoman.model.integration.persistence.beans.PotluckEvent;
import org.cohoman.view.controller.CohomanException;

public interface PotluckEventDao {
	public void createPotluckEvent(PotluckEventDTO dto) throws CohomanException;
	
	public PotluckEvent getPotluckEvent(Long eventId);
	
	public List<PotluckEvent> getPotluckEvents();
	
	public List<PotluckEvent> getPotluckEventsForDay(Date dateOfDay);
		
	public void updatePotluckEvent(PotluckEvent PotluckEvent) throws CohomanException;
	
	public void deletePotluckEvent(PotluckEvent PotluckEvent);

}
