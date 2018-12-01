package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.view.controller.CohomanException;

public interface MaintenanceDao {
	
	public void createMaintenanceItem(MaintenanceItemDTO dto) throws CohomanException;
	
	public List<MaintenanceItemDTO> getMaintenanceItems();

	public MaintenanceItemDTO getMaintenanceItem(Long maintenanceItemId);

	public void deleteMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO);
	
	public void updateMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO);

}
