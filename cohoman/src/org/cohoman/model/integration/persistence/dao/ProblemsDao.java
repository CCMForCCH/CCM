package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.dto.ProblemItemDTO;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.ProblemStateEnums;

public interface ProblemsDao {
	
	public void createProblemItem(ProblemItemDTO dto) throws CohomanException;
	
	public List<ProblemItemDTO> getProblemItems(
			ProblemStateEnums problemStateEnum);

	public ProblemItemDTO getProblemItem(Long problemItemId);

	public void deleteProblemItem(ProblemItemDTO problemItemDTO);
	
	public void updateProblemItem(ProblemItemDTO problemItemDTO);

}
