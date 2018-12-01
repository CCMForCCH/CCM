package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.dto.SignupPotluckDTO;
import org.cohoman.view.controller.CohomanException;

public interface SignupPotluckDao {
	public void create(SignupPotluckDTO dto) throws CohomanException;
	
	public List<SignupPotluckDTO> getAllPotluckSignups(Long eventId);
			
	public void delete(SignupPotluckDTO dto);

}
