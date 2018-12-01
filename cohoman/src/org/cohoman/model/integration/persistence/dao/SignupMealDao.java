package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.dto.SignupMealDTO;
import org.cohoman.view.controller.CohomanException;

public interface SignupMealDao {
	public void create(SignupMealDTO dto) throws CohomanException;
	
	public List<SignupMealDTO> getAllMealSignups(Long eventId);
			
	public void delete(SignupMealDTO dto);

}
