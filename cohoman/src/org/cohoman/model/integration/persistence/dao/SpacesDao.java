package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.integration.persistence.beans.SpaceBean;

public interface SpacesDao {
	
	public List<SpaceBean> getAllSpaces();
    
}
