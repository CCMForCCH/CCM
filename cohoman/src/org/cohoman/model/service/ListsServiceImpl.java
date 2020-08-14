package org.cohoman.model.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.business.ListsManager;
import org.cohoman.model.business.User;
import org.cohoman.model.business.UserManager;
import org.cohoman.model.business.ListsManagerImpl.SecurityDataForRow;
import org.cohoman.model.business.ListsManagerImpl.SecurityRow;
import org.cohoman.model.business.trash.TrashPerson;
import org.cohoman.model.business.trash.TrashRow;
import org.cohoman.model.business.trash.TrashSchedule;
import org.cohoman.model.business.trash.TrashTeam;
import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.dto.MtaskDTO;
import org.cohoman.model.integration.email.SendEmail;
import org.cohoman.model.integration.persistence.beans.CchSectionTypeEnum;
import org.cohoman.model.integration.persistence.beans.SubstitutesBean;
import org.cohoman.model.integration.persistence.beans.TrashSubstitutesBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.MaintenanceTypeEnums;
import org.cohoman.view.controller.utils.SortEnums;

public class ListsServiceImpl implements ListsService {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private UserManager userManager = null;
	private ListsManager listsManager = null;

	public ListsManager getListsManager() {
		return listsManager;
	}

	public void setListsManager(ListsManager listsManager) {
		this.listsManager = listsManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public List<SecurityRow> getSecurityList(CchSectionTypeEnum sectionEnum) {
		return listsManager.getSecurityList(sectionEnum);
	}

	public List<SecurityDataForRow> getSecurityListWithSubs(
			CchSectionTypeEnum sectionEnum) {
		return listsManager.getSecurityListWithSubs(sectionEnum);
	}

	public void setSubstitute(Date startingDate, Long userid)
			throws CohomanException {

		SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");

		logger.info("AUDIT: Setting substitute for date "
				+ formatter.format(startingDate.getTime()) + " to user "
				+ getUserFullname(userid) + ", being set by "
				+ LoggingUtils.getCurrentUsername());
		listsManager.setSubstitute(startingDate, userid);
	}

	public void setTrashSubstitute(Date startingDate, String originalUsername,
			String substituteUsername) throws CohomanException {

		SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");

		logger.info("AUDIT: Setting trash substitute for date "
				+ formatter.format(startingDate.getTime())
				+ " and original user " + originalUsername
				+ " with substitute user " + substituteUsername
				+ ", being set by " + LoggingUtils.getCurrentUsername());
		listsManager.setTrashSubstitute(startingDate, originalUsername,
				substituteUsername);
	}

	private String getUserFullname(Long userid) throws CohomanException {
		User dbUser = userManager.getUser(userid);
		if (dbUser == null) {
			throw new CohomanException("No such user exists for this userid: "
					+ userid);
		}
		String fullname = dbUser.getFirstname() + " " + dbUser.getLastname();
		return fullname;
	}

	public SubstitutesBean getSubstitute(String startingDate) {
		return listsManager.getSubstitute(startingDate);
	}

	public void deleteSubstitute(Long substitutesId) {
		logger.info("AUDIT: deleting substitute for entry " + substitutesId
				+ ", being deleted by " + LoggingUtils.getCurrentUsername());
		listsManager.deleteSubstitute(substitutesId);
	}

	// Maintenance Item services
	public void createMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO)
			throws CohomanException {
		logger.info("AUDIT: Create maintenance for "
				+ maintenanceItemDTO.getItemname() + ", by "
				+ getUserFullname(maintenanceItemDTO.getItemCreatedBy())
				+ ", description =\"" + maintenanceItemDTO.getItemdescription()
				+ "\", frequency = " + maintenanceItemDTO.getFrequencyOfItem()
				+ "\", priority = " + maintenanceItemDTO.getPriority()
				+ "\", target time of year = "
				+ maintenanceItemDTO.getTargetTimeOfyear());
		listsManager.createMaintenanceItem(maintenanceItemDTO);
	}

	public List<MaintenanceItemDTO> getMaintenanceItems(
			SortEnums sortEnum, MaintenanceTypeEnums maintenanceTypeEnum) {
		return listsManager.getMaintenanceItems(sortEnum, maintenanceTypeEnum);
	}

	public MaintenanceItemDTO getMaintenanceItem(Long maintenanceItemId) {
		return listsManager.getMaintenanceItem(maintenanceItemId);
	}

	public void updateMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO)
			throws CohomanException {
		logger.info("AUDIT: Create maintenance for "
				+ maintenanceItemDTO.getItemname() + ", by "
				+ getUserFullname(maintenanceItemDTO.getItemCreatedBy())
				+ ", description =\"" + maintenanceItemDTO.getItemdescription()
				+ "\", frequency = " + maintenanceItemDTO.getFrequencyOfItem()
				+ "\", priority = " + maintenanceItemDTO.getPriority()
				+ "\", target time of year = "
				+ maintenanceItemDTO.getTargetTimeOfyear());
		listsManager.updateMaintenanceItem(maintenanceItemDTO);
	}

	public void deleteMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO)
			throws CohomanException {

		// public void deleteMealEvent(MealEvent mealEvent) throws Exception {
		/*
		 * String cook2 = ""; String cook3 = ""; String cook4 = ""; String
		 * cleaner1 = ""; String cleaner2 = ""; String cleaner3 = ""; if
		 * (mealEvent.getCook2() != null) { cook2 =
		 * getUserFullname(mealEvent.getCook2()); } if (mealEvent.getCook3() !=
		 * null) { cook3 = getUserFullname(mealEvent.getCook3()); } if
		 * (mealEvent.getCook4() != null) { cook4 =
		 * getUserFullname(mealEvent.getCook4()); } if (mealEvent.getCleaner1()
		 * != null) { cleaner1 = getUserFullname(mealEvent.getCleaner1()); } if
		 * (mealEvent.getCleaner2() != null) { cleaner2 =
		 * getUserFullname(mealEvent.getCleaner2()); } if
		 * (mealEvent.getCleaner3() != null) { cleaner3 =
		 * getUserFullname(mealEvent.getCleaner3()); }
		 * 
		 * logger.info("AUDIT: Delete meal for " + mealEvent.getEventDate() +
		 * ", menu = \"" + mealEvent.getMenu() + "\", lead cook = " +
		 * getUserFullname(mealEvent.getCook1()) + ", cook2 = " + cook2 +
		 * ", cook3 = " + cook3 + ", cook4 = " + cook4 + ", cleaner1 = " +
		 * cleaner1 + ", cleaner2 = " + cleaner2 + ", cleaner3 = " + cleaner3 +
		 * ", maxattendees = " + mealEvent.getMaxattendees() +
		 * ", ismealclosed = " + mealEvent.isIsmealclosed() + " deleted by " +
		 * LoggingUtils.getCurrentUsername());
		 * 
		 * try { eventManager.deleteMealEvent(mealEvent); } catch (Exception ex)
		 * { logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
		 * throw new CohomanException("Unable to delete Meal Event = " +
		 * mealEvent.getEventDate()); }
		 */
		try {
			listsManager.deleteMaintenanceItem(maintenanceItemDTO);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			throw new CohomanException("Unable to delete Maintenance Item = "
					+ maintenanceItemDTO.getItemname());
		}

		logger.info("AUDIT: Delete maintenance item for "
				+ maintenanceItemDTO.getItemname() + ", description = \""
				+ maintenanceItemDTO.getItemdescription()
				+ "\", date created = "
				+ maintenanceItemDTO.getPrintableCreatedDate()
				+ "\", date last performed = "
				+ maintenanceItemDTO.getLastperformedDate() + " deleted by "
				+ LoggingUtils.getCurrentUsername());

	}

	// Maintenance Item services
	public void createMtask(MtaskDTO mtaskDTO) throws CohomanException {
		/*
		 * logger.info("AUDIT: Create maintenance task for " +
		 * maintenanceItemDTO.getItemname() + ", by " +
		 * getUserFullname(maintenanceItemDTO.getItemCreatedBy()) +
		 * ", description =\"" + maintenanceItemDTO.getItemdescription() +
		 * "\", frequency = " + maintenanceItemDTO.getFrequencyOfItem() +
		 * "\", priority = " + maintenanceItemDTO.getPriority() +
		 * "\", target time of year = " +
		 * maintenanceItemDTO.getTargetTimeOfyear());
		 */

		listsManager.createMtask(mtaskDTO);
	}

	public List<MtaskDTO> getMtasksForMaintenanceItem(Long maintenanceItemId) {
		return listsManager.getMtasksForMaintenanceItem(maintenanceItemId);
	}

	public MtaskDTO getMtask(Long mtaskItemId) {
		return listsManager.getMtask(mtaskItemId);
	}

	public void updateMtask(MtaskDTO mtaskDTO) {
		listsManager.updateMtask(mtaskDTO);

		// TODO Add in the auditing!!!!!!!!!!
	}

	public void deleteMtask(Long mtaskitemid) {
		listsManager.deleteMtask(mtaskitemid);

		// TODO Add in the auditing!!!!!!!!!!!!
	}

	// Trash
	public List<TrashRow> getTrashSchedule() throws CohomanException {
		return listsManager.getTrashSchedule();
	}

	public List<TrashTeam> getTrashTeams(int numberOfCycles)
			throws CohomanException {
		return listsManager.getTrashTeams(numberOfCycles);
	}

	public List<TrashPerson> getTrashPersonListOrig() {
		return listsManager.getTrashPersonListOrig();
	}

	public void deleteTrashSubstitute(Long substitutesId, Date startingDate, String origUsername) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
		logger.info("AUDIT: Deleting trash substitute for date "
				+ formatter.format(startingDate.getTime())
				+ " and original user " + origUsername
				+ ", being deleted by " + LoggingUtils.getCurrentUsername());
		listsManager.deleteTrashSubstitute(substitutesId);
	}

	public List<TrashSubstitutesBean> getTrashSubstitutes() {
		return listsManager.getTrashSubstitutes();
	}

	public TrashSubstitutesBean getTrashSubstitute(String startingDate,
			String origUsername) {
		return listsManager.getTrashSubstitute(startingDate, origUsername);
	}

	public List<TrashRow> getTrashTeamsFromDB() {
		return listsManager.getTrashTeamsFromDB();
	}

	public void sendEmailToAddress(String emailAddress, String subject, String body) {
		listsManager.sendEmailToAddress(emailAddress, subject, body);
	}

	public void sendTextMessageToPerson(String cellphoneNumber, String textMessage) {
		listsManager.sendTextMessageToPerson(cellphoneNumber, textMessage);
	}

}
