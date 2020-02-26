package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.SortEnums;

public interface MaintenanceDao {
	
	public void createMaintenanceItem(MaintenanceItemDTO dto) throws CohomanException;
	
	public List<MaintenanceItemDTO> getMaintenanceItems(SortEnums sortEnum);

	public MaintenanceItemDTO getMaintenanceItem(Long maintenanceItemId);

	public void deleteMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO);
	
	public void updateMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO);

}
