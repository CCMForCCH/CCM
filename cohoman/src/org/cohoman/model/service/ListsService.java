package org.cohoman.model.service;

import java.util.Date;
import java.util.List;

import org.cohoman.model.business.ListsManagerImpl.SecurityDataForRow;
import org.cohoman.model.business.ListsManagerImpl.SecurityRow;
import org.cohoman.model.business.trash.TrashPerson;
import org.cohoman.model.business.trash.TrashRow;
import org.cohoman.model.business.trash.TrashTeam;
import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.dto.MtaskDTO;
import org.cohoman.model.dto.ProblemItemDTO;
import org.cohoman.model.integration.persistence.beans.CchSectionTypeEnum;
import org.cohoman.model.integration.persistence.beans.SubstitutesBean;
import org.cohoman.model.integration.persistence.beans.TrashSubstitutesBean;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.MaintenanceTypeEnums;
import org.cohoman.view.controller.utils.SortEnums;

public interface ListsService {

	public List<SecurityRow> getSecurityList(CchSectionTypeEnum sectionEnum);

	public List<SecurityDataForRow> getSecurityListWithSubs(
			CchSectionTypeEnum sectionEnum);

	public void setSubstitute(Date startingDate, Long userid)
			throws CohomanException;

	public SubstitutesBean getSubstitute(String startingDate);

	public void deleteSubstitute(Long substitutesId);

	// Maintenance item operations
	public void createMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO)
			throws CohomanException;

	public List<MaintenanceItemDTO> getMaintenanceItems(
			SortEnums sortEnum, MaintenanceTypeEnums maintenanceTypeEnum);

	public void updateMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO)
			throws CohomanException;

	public void deleteMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO)
			throws CohomanException;

	public MaintenanceItemDTO getMaintenanceItem(Long maintenanceItemId);

	// Maintenance task instances
	public void createMtask(MtaskDTO dto) throws CohomanException;

	public List<MtaskDTO> getMtasksForMaintenanceItem(Long maintenanceItemId);

	public MtaskDTO getMtask(Long mtaskItemId);

	public void updateMtask(MtaskDTO mtaskDTO);

	public void deleteMtask(Long mtaskitemid);

	// Problem item operations
	public void createProblemItem(ProblemItemDTO problemItemDTO)
			throws CohomanException;

	public List<ProblemItemDTO> getProblemItems(
			SortEnums sortEnum);

	public void updateProblemItem(ProblemItemDTO problemItemDTO)
			throws CohomanException;

	public void deleteProblemItem(ProblemItemDTO problemItemDTO)
			throws CohomanException;

	public ProblemItemDTO getProblemItem(Long problemItemId);

	// Trash
	public List<TrashRow> getTrashSchedule() throws CohomanException;

	public List<TrashTeam> getTrashTeams(int numberOfCycles)
			throws CohomanException;

	public List<TrashPerson> getTrashPersonListOrig();

	public void setTrashSubstitute(Date startingDate, String originalUsername,
			String substituteUsername) throws CohomanException;

	public void deleteTrashSubstitute(Long substitutesId, Date startingDate, String origUsername);

	public List<TrashSubstitutesBean> getTrashSubstitutes();

	public TrashSubstitutesBean getTrashSubstitute(String startingDate,
			String origUsername);

	public List<TrashRow> getTrashTeamsFromDB();

	// Test notifications
	public void sendEmailToAddress(String emailAddress, String subject, String body);
	public void sendTextMessageToPerson(String cellphoneNumber, String textMessage);

}
