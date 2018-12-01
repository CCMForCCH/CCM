package org.cohoman.model.integration.persistence.dao;

import java.util.Date;
import java.util.List;

import org.cohoman.model.dto.PrivateEventDTO;
import org.cohoman.model.integration.persistence.beans.PrivateEvent;
import org.cohoman.view.controller.CohomanException;

public interface PrivateEventDao {
	public void createPrivateEvent(PrivateEventDTO dto) throws CohomanException;
	
	public PrivateEvent getPrivateEvent(Long eventId);
	
	public List<PrivateEvent> getMyPrivateEvents();

	public List<PrivateEvent> getPendingPrivateEvents();

	public List<PrivateEvent> getAllPrivateEvents();

	public List<PrivateEvent> getUpcomingPrivateEvents();

	public List<PrivateEvent> getPrivateEventsForDay(Date dateOfDay);
		
	public void updatePrivateEvent(PrivateEvent privateEvent) throws CohomanException ;
	
	public void deletePrivateEvent(Long privateEventId);

}
