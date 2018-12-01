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

/*
	
	public List<MealEvent> getMealEvents();

	public List<MealEvent> getAllMealEvents();

	public MealEvent getMealEvent(Long eventId);
	
	public List<MealEvent> getMealEventsForDay(Date dateOfDay);

	public void updateMealEvent(MealEvent mealEvent) throws CohomanException;
*/
	
	//public List<MDTO> getMaintenanceItems();

	//public MaintenanceItemDTO getMaintenanceItem(Long maintenanceItemId);

	//public void deleteMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO);

}
