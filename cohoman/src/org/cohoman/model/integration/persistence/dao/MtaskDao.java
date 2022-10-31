package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.dto.MtaskDTO;
import org.cohoman.view.controller.CohomanException;

public interface MtaskDao {
	public void createMtask(MtaskDTO dto) throws CohomanException;

	public List<MtaskDTO> getMtasksForMaintenanceItem(Long maintenanceItemId);
	
	public MtaskDTO getMtask(Long mtaskItemId);

	public void updateMtask(MtaskDTO mtaskDTO);

	public void deleteMtask(Long mtaskitemid);

}
