package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.dto.ProblemUpdateDTO;
import org.cohoman.view.controller.CohomanException;

public interface ProblemUpdateDao {
	public void createProblemUpdate(ProblemUpdateDTO dto) throws CohomanException;

	public List<ProblemUpdateDTO> getProblemUpdatesForProblemItem(Long problemItemId);
	
	public ProblemUpdateDTO getProblemUpdate(Long problemUpdateId);

	public void updateProblemUpdate(ProblemUpdateDTO problemUpdateDTO);

	public void deleteProblemUpdate(Long problemUpdateId);

}
