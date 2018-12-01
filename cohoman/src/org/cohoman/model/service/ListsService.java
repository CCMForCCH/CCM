package org.cohoman.model.service;

import java.util.Date;
import java.util.List;

import org.cohoman.model.business.ListsManagerImpl.SecurityDataForRow;
import org.cohoman.model.business.ListsManagerImpl.SecurityRow;
import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.dto.MtaskDTO;
import org.cohoman.model.integration.persistence.beans.CchSectionTypeEnum;
import org.cohoman.model.integration.persistence.beans.SubstitutesBean;
import org.cohoman.view.controller.CohomanException;
 
public interface ListsService {
		
	public List<SecurityRow> getSecurityList(CchSectionTypeEnum sectionEnum);

	public List<SecurityDataForRow> getSecurityListWithSubs(CchSectionTypeEnum sectionEnum);
	
	public void setSubstitute(Date startingDate, Long userid) throws CohomanException;
	
	public SubstitutesBean getSubstitute(String startingDate);
	
	public void deleteSubstitute(Long substitutesId);

	// Maintenance item operations
	public void createMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO) throws CohomanException;
	public List<MaintenanceItemDTO> getMaintenanceItems();
	public void updateMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO)throws CohomanException;
	public void deleteMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO) throws CohomanException;
	public MaintenanceItemDTO getMaintenanceItem(Long maintenanceItemId);
	
	// Maintenance task instances
	public void createMtask(MtaskDTO dto) throws CohomanException;
	public List<MtaskDTO> getMtasksForMaintenanceItem(Long maintenanceItemId);
	public MtaskDTO getMtask(Long mtaskItemId);
	public void updateMtask(MtaskDTO mtaskDTO);
	public void deleteMtask(Long mtaskitemid);

}
