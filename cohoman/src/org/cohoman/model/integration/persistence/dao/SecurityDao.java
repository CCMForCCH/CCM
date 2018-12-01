package org.cohoman.model.integration.persistence.dao;

import org.cohoman.model.dto.SecurityStartingPointDTO;

public interface SecurityDao {
	
	public SecurityStartingPointDTO getSecurityStart(String section);
	public void updateSecurityStart(SecurityStartingPointDTO securityStartingPointDTO);
    
}
