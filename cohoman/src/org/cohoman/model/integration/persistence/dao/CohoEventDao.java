package org.cohoman.model.integration.persistence.dao;

import java.util.Date;
import java.util.List;

import org.cohoman.model.dto.CohoEventDTO;
import org.cohoman.model.integration.persistence.beans.CohoEvent;
import org.cohoman.view.controller.CohomanException;

public interface CohoEventDao {
	public void createCohoEvent(CohoEventDTO dto) throws CohomanException;
	
	public CohoEvent getCohoEvent(Long eventId);
	
	public List<CohoEvent> getCohoEvents();
	
	public List<CohoEvent> getCohoEventsForDay(Date dateOfDay);
		
	public void updateCohoEvent(CohoEvent cohoEvent) throws CohomanException ;
	
	public void deleteCohoEvent(CohoEvent cohoEvent);

}
