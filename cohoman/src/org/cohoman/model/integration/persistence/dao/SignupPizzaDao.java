package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.dto.SignupPizzaDTO;
import org.cohoman.view.controller.CohomanException;

public interface SignupPizzaDao {
	public void create(SignupPizzaDTO dto) throws CohomanException;
	
	public List<SignupPizzaDTO> getAllPizzaSignups(Long eventId);
			
	public void delete(SignupPizzaDTO dto);

}
