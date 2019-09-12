package org.cohoman.model.business;

import java.util.Date;
import java.util.List;

import org.cohoman.model.business.ListsManagerImpl.SecurityDataForRow;
import org.cohoman.model.business.ListsManagerImpl.SecurityRow;
import org.cohoman.model.business.trash.TrashPerson;
import org.cohoman.model.business.trash.TrashRow;
import org.cohoman.model.business.trash.TrashTeam;
import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.dto.MtaskDTO;
import org.cohoman.model.integration.persistence.beans.CchSectionTypeEnum;
import org.cohoman.model.integration.persistence.beans.SubstitutesBean;
import org.cohoman.model.integration.persistence.beans.TrashSubstitutesBean;
import org.cohoman.view.controller.CohomanException;

public interface ListsManager {
	
	public List<SecurityRow> getSecurityList(CchSectionTypeEnum sectionEnum);

	public List<SecurityDataForRow> getSecurityListWithSubs(CchSectionTypeEnum sectionEnum);
	
	public void setSubstitute(Date startingDate, Long userid) throws CohomanException;

	public SubstitutesBean getSubstitute(String startingDate);
	
	public void deleteSubstitute(Long substitutesId);

	// Maintenance item operations
	public void createMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO) throws CohomanException;
	public void deleteMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO);
	public List<MaintenanceItemDTO> getMaintenanceItems();
	public MaintenanceItemDTO getMaintenanceItem(Long maintenanceItemId);
	public void updateMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO);

	// Maintenance task instances
	public void createMtask(MtaskDTO dto) throws CohomanException;
	public List<MtaskDTO> getMtasksForMaintenanceItem(Long maintenanceItemId);
	public MtaskDTO getMtask(Long mtaskItemId);
	public void updateMtask(MtaskDTO mtaskDTO);
	public void deleteMtask(Long mtaskitemid);

	// Trash
	public List<TrashRow> getTrashSchedule();
	public List<TrashTeam> getTrashTeams(int numberOfCycles);
	public List<TrashPerson> getTrashPersonListOrig();
	public void setTrashSubstitute(Date startingDate, String originalUsername, String substituteUsername) throws CohomanException;
	public void deleteTrashSubstitute(Long substitutesId);
	public List<TrashSubstitutesBean> getTrashSubstitutes();
	public TrashSubstitutesBean getTrashSubstitute(String startingDate, String origUsername);


}
