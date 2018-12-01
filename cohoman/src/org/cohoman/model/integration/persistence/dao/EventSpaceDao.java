package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.integration.persistence.beans.EventSpace;

public interface EventSpaceDao {
	
	public List<EventSpace> getEventSpaces(Long eventid, String eventtype);
    
}
