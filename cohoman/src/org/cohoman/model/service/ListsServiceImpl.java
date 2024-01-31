package org.cohoman.model.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.business.ListsManager;
import org.cohoman.model.business.ListsManagerImpl.SecurityDataForRow;
import org.cohoman.model.business.ListsManagerImpl.SecurityRow;
import org.cohoman.model.business.User;
import org.cohoman.model.business.UserManager;
import org.cohoman.model.business.trash.TrashPerson;
import org.cohoman.model.business.trash.TrashRow;
import org.cohoman.model.business.trash.TrashTeam;
import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.dto.MtaskDTO;
import org.cohoman.model.dto.ProblemItemDTO;
import org.cohoman.model.dto.ProblemUpdateDTO;
import org.cohoman.model.integration.email.SendEmail;
import org.cohoman.model.integration.persistence.beans.CchSectionTypeEnum;
import org.cohoman.model.integration.persistence.beans.SubstitutesBean;
import org.cohoman.model.integration.persistence.beans.TrashSubstitutesBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.MaintenanceTypeEnums;
import org.cohoman.view.controller.utils.ProblemPriorityEnums;
import org.cohoman.view.controller.utils.ProblemStateEnums;
import org.cohoman.view.controller.utils.ProblemStatusEnums;
import org.cohoman.view.controller.utils.ProblemTypeEnums;
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
		
		// First, need to convert the assigned user as a string number into
		// a userid and a username for the DTO.
		// Start with the chosen userid as a string and convert it into the
		// userid (which is Long) and username (which is a string).
		String assignedToString = null;
		Long userid = Long.valueOf(maintenanceItemDTO.getAssignedToString());
		List<User> theusers = userManager.getUsersHereNow();
		for (User oneuser : theusers) {
			if (oneuser.getUserid().equals(userid)) {
				assignedToString = oneuser.getUsername();
				break;
			}
		}	
		
		// Now, put the userid and username
		maintenanceItemDTO.setAssignedToString(assignedToString);
		maintenanceItemDTO.setAssignedTo(userid);
		
		String assignedTo = "";
		if (maintenanceItemDTO.getAssignedTo() != null && maintenanceItemDTO.getAssignedTo() != 0) {
			assignedTo = getUserFullname(maintenanceItemDTO.getAssignedTo());
		}

		logger.info("AUDIT: Create maintenance item for "
				+ "\"" + maintenanceItemDTO.getItemname() + "\", "
				+ "by \"" + getUserFullname(maintenanceItemDTO.getItemCreatedBy()) + "\", "
				+ "description = \"" + maintenanceItemDTO.getItemdescription() + "\", "
				+ "frequency = \"" + maintenanceItemDTO.getFrequencyOfItem() + "\", "
				+ "priority = \"" + maintenanceItemDTO.getPriority() + "\", "
				+ "target time of year = \"" + maintenanceItemDTO.getTargetTimeOfyear() + "\", "
				+ "assigned to = \"" + assignedTo + "\"");
		
		// Now, let the Manager do the work.
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
		String assignedTo = "";
		if (maintenanceItemDTO.getAssignedTo() != null && maintenanceItemDTO.getAssignedTo() != 0) {
			assignedTo = getUserFullname(maintenanceItemDTO.getAssignedTo());
		}
		
		logger.info("AUDIT: Update maintenance item for "
				+ "\"" + maintenanceItemDTO.getItemname() + "\", "
				+ "by \"" + getUserFullname(maintenanceItemDTO.getItemCreatedBy()) + "\", "
				+ "description = \"" + maintenanceItemDTO.getItemdescription() + "\", "
				+ "frequency = \"" + maintenanceItemDTO.getFrequencyOfItem() + "\", "
				+ "priority = \"" + maintenanceItemDTO.getPriority() + "\", "
				+ "target time of year = \"" + maintenanceItemDTO.getTargetTimeOfyear() + "\", "
				+ "assigned to = \"" + assignedTo + "\", "
				+ "status = \"" + maintenanceItemDTO.getTaskStatus() + "\"");
		listsManager.updateMaintenanceItem(maintenanceItemDTO);
	}

	public void deleteMaintenanceItem(MaintenanceItemDTO maintenanceItemDTO)
			throws CohomanException {

		// public void deleteMealEvent(MealEvent mealEvent) throws Exception {
		
		try {
			listsManager.deleteMaintenanceItem(maintenanceItemDTO);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			throw new CohomanException("Unable to delete Maintenance Item = "
					+ maintenanceItemDTO.getItemname());
		}

		String assignedTo = "";
		if (maintenanceItemDTO.getAssignedTo() != null && maintenanceItemDTO.getAssignedTo() != 0) {
			assignedTo = getUserFullname(maintenanceItemDTO.getAssignedTo());
		}

		logger.info("AUDIT: Delete maintenance item for "
				+ "\"" + maintenanceItemDTO.getItemname() + "\", "
				+ "by \"" + getUserFullname(maintenanceItemDTO.getItemCreatedBy()) + "\", "
				+ "description = \"" + maintenanceItemDTO.getItemdescription() + "\", "
				+ "date created = \"" + maintenanceItemDTO.getPrintableCreatedDate() + "\", "
				+ "date last performed = \"" + maintenanceItemDTO.getLastperformedDate() + "\", "
				+ "assigned to = \"" + assignedTo + "\", "
				+ "deleted by \"" + LoggingUtils.getCurrentUsername() + "\"");

	}

	// Maintenance Task services
	public void createMtask(MtaskDTO mtaskDTO) throws CohomanException {
	
		logger.info("AUDIT: Create maintenance task for item \""
				+ getMaintenanceItem(mtaskDTO.getMaintenanceitemid())
						.getItemname() + "\", Task Start = \""
				+ mtaskDTO.getTaskstartDate() + "\", Vendor Name = \""
				+ mtaskDTO.getVendorname() + "\", Added By = \""
				+ getUserFullname(mtaskDTO.getItemCreatedBy())
				+ "\", Task Completed On = \"" + mtaskDTO.getTaskendDate()
				+ "\", Notes = \"" + mtaskDTO.getNotes()
				+ "\"");

		listsManager.createMtask(mtaskDTO);
	}

	public List<MtaskDTO> getMtasksForMaintenanceItem(Long maintenanceItemId) {
		return listsManager.getMtasksForMaintenanceItem(maintenanceItemId);
	}

	public MtaskDTO getMtask(Long mtaskItemId) {
		return listsManager.getMtask(mtaskItemId);
	}

	public void updateMtask(MtaskDTO mtaskDTO) {
		
		try {
			logger.info("AUDIT: Update maintenance task for item \""
					+ getMaintenanceItem(mtaskDTO.getMaintenanceitemid())
							.getItemname() + "\", Task Start = \""
					+ mtaskDTO.getTaskstartDate() + "\", Vendor Name = \""
					+ mtaskDTO.getVendorname() + "\", Added By = \""
					+ getUserFullname(mtaskDTO.getItemCreatedBy())
					+ "\", Task Completed On = \"" + mtaskDTO.getTaskendDate()
					+ "\", Notes = \"" + mtaskDTO.getNotes());
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
		}

		listsManager.updateMtask(mtaskDTO);

	}

	public void deleteMtask(Long mtaskitemid) {
		
		MtaskDTO mtaskDTO = getMtask(mtaskitemid);
		listsManager.deleteMtask(mtaskitemid);

		try {
		logger.info("AUDIT: delete maintenance task update added by \""
				+ getUserFullname(mtaskDTO.getItemCreatedBy())
				+ "\", vendor name = \"" + mtaskDTO.getVendorname()
				+ "\", start date = \"" + mtaskDTO.getPrintableStartdate()
				+ "\", notes = \"" + mtaskDTO.getNotes());
		} catch (CohomanException ex) {
			// ???
		}

	}

	// Problem Item services
	public void createProblemItem(ProblemItemDTO problemItemDTO)
			throws CohomanException {
		
		// Compute service manager string and user id (as long) for DTO.
		// This will be put into the DTO as the assigned user
		String serviceManagerAsString = getServiceManager(problemItemDTO);
		Long serviceManagerAsLong = 0L;
		List<User> theusers = userManager.getUsersHereNow();
		for (User oneuser : theusers) {
			if (oneuser.getUsername().equals(serviceManagerAsString)) { 
				serviceManagerAsLong = oneuser.getUserid();
				break;
			}
		}
		
		// Log audit info before call
		logger.info("AUDIT: Create problem item for "
				+ problemItemDTO.getItemname() + ", by "
				+ getUserFullname(problemItemDTO.getItemCreatedBy())
				+ ", description =\"" + problemItemDTO.getItemdescription()
				+ "\", priority = \"" + problemItemDTO.getPriority()
				+ "\", type = \"" + problemItemDTO.getProblemType()
				+ "\", location = \"" + problemItemDTO.getLocation()
				+ "\", status = \""
				+ problemItemDTO.getProblemStatus()
				+ "\", assigned to = \""
				+ serviceManagerAsString
				+ "\"");

		// Set assigned to field in DTO with Service Manager
		problemItemDTO.setAssignedToString(serviceManagerAsString);
		problemItemDTO.setAssignedTo(serviceManagerAsLong);
		listsManager.createProblemItem(problemItemDTO);
		notifyPeopleAboutProblem(problemItemDTO);
	}

	public String getServiceManager(ProblemItemDTO problemItemDTO) {

		// Get Service Manager with this problem type
		String serviceManager = listsManager
				.getUsernameForProblemType(ProblemTypeEnums
						.valueOf(problemItemDTO.getProblemType()));

		// Return username of the Service Manager
		if (serviceManager == null) {

			logger.log(Level.WARNING,
					"Unable to determine a Service Manager to notify");
			return "bill";
		} else {
			return serviceManager;
		}
	}
	
	public void notifyPeopleAboutProblem(ProblemItemDTO problemItemDTO) {
		
		List<User> theusers = userManager.getUsersHereNow();
		String problemPriorityText = ProblemPriorityEnums.valueOf(problemItemDTO.getPriority()).toString();
		User submittedUser = userManager.getUser(problemItemDTO.getItemCreatedBy());
		String submitter = submittedUser.getUsername();

		// Get Service Manager with this problem type
		String serviceManager = listsManager
				.getUsernameForProblemType(ProblemTypeEnums
						.valueOf(problemItemDTO.getProblemType()));

		/*
		 * CRITICAL PRIORITY
		 */
/*
		if (problemItemDTO.getPriority().equals(ProblemPriorityEnums.P1CRITICAL
				.name())) {

			// Critical problem
			// Send text messages to all residents
			for (User oneuser : theusers) {
				//if (oneuser.getUsername().equals("bill")) {  //temp!!!!!
				if (!oneuser.getCellphone().isEmpty()) {
					listsManager.sendTextMessageToPerson(
							oneuser.getCellphone(),
							"CCM: New " + problemPriorityText
									+ " priority problem report: "
									+ problemItemDTO.getItemname()
									+ ". Submitted by "
									+ submitter
									+ ".");
				}
				else {
					logger.log(Level.WARNING,
							"Critical Report: No cellphone number for " + oneuser.getUsername());

				}
				//}
			}

		}  else 
*/
		
		if (problemItemDTO.getPriority().equals(ProblemPriorityEnums.P2EMERGENCY
				.name())) {

		/*
		 * EMERGENCY PRIORITY
		 */
			// Send text messages to emergency response team
			for (User oneuser : theusers) {
				if (oneuser.getUsername().equals("bill")
						|| oneuser.getUsername().equals("bobm")
						|| oneuser.getUsername().equals("walter")
						|| oneuser.getUsername().equals("nick")
						|| oneuser.getUsername().equals("gwenn")) {
					listsManager.sendTextMessageToPerson(
							oneuser.getCellphone(), "CCM: New "
									+ problemPriorityText
									+ " priority problem report: "
									+ problemItemDTO.getItemname()
									+ ". Submitted by " + submitter + ".");
				}
			}

			// Also, send it to the Service Manager if a text was
			// not already sent
			
			// Don't send another message if the Service Manager
			// is also part of the emergency team
			if (serviceManager.equals("bill") 
					|| serviceManager.equals("bobm")
					|| serviceManager.equals("walter")
					|| serviceManager.equals("nick")
					|| serviceManager.equals("gwenn")) {
				return;
			}

			// Service Manager is not part of the emergency team.
			// So, send that person a text as well.
			for (User oneuser : theusers) {

				if (serviceManager.equals(oneuser.getUsername())) {
		
					listsManager.sendTextMessageToPerson(
							oneuser.getCellphone(), "CCM: New "
									+ problemPriorityText
									+ " priority problem report: "
									+ problemItemDTO.getItemname()
									+ ". Submitted by " + submitter + ".");
					// There is only one service manager, so all done. 
					return;
				}

			}

		} else if (problemItemDTO.getPriority().equals(ProblemPriorityEnums.P3HIGH
				.name())) {
			
		/*
		 * HIGH PRIORITY
		 */
			// Send text message to both the service manager and to the Service Coordinators
			
			// Send text messages to Service Coordinators
			for (User oneuser : theusers) {
				if (oneuser.getUsername().equals("bill")
						|| oneuser.getUsername().equals("bobm")
						|| oneuser.getUsername().equals("allison")) {
					listsManager.sendTextMessageToPerson(
							oneuser.getCellphone(), "CCM: New "
									+ problemPriorityText
									+ " priority problem report: "
									+ problemItemDTO.getItemname()
									+ "; Submitted by " + submitter + ".");
				}
			}

			// Ignore the Service Coordinators since they already got a text
			// and if they are also the Service Manager, there are no more
			// messages to send. Thus drop out of this loop.
			if (serviceManager.equals("bill") || serviceManager.equals("bobm")
					|| serviceManager.equals("allison")) {
				return;
			}

			// Loop to find the user entry for the Service Manager which
			// is NOT a Service Coordinator
			for (User oneuser : theusers) {

				// Send text message to the Service Manager
				if (serviceManager.equals(oneuser.getUsername())) {
					listsManager.sendTextMessageToPerson(
							oneuser.getCellphone(), "CCM: New "
									+ problemPriorityText
									+ " priority problem report: "
									+ problemItemDTO.getItemname()
									+ ". Submitted by " + submitter + ".");

					// There is only one Service Manager, so all done.
					return;
				}
			}

		} else {
		
		/*
		 * MEDIUM/LOW PRIORITY
		 */
			// Medium and Low priority problems simply yield an email sent to
			// the Service Manager
			
			for (User oneuser : theusers) {

				// Send email to the Service Manager
				if (serviceManager.equals(oneuser.getUsername())) {
					if (oneuser.getEmail() == null
							|| oneuser.getEmail().isEmpty()) {
						logger.log(Level.WARNING,
								"Unable to determine an email for the Service Manager to notify");
						return;
					}
					SendEmail.sendEmailToAddress(
							oneuser.getEmail(),
							"New problem report just added",
							"CCM: New " + problemPriorityText
									+ " priority problem report: "
									+ problemItemDTO.getItemname()
									+ "\n Description: "
									+ problemItemDTO.getItemdescription()
									+ "\n Submitted by: " + submitter + ".");
					logger.log(Level.INFO, "Problem Notifying: Sending email to user " + oneuser.getEmail());
					
					// Return as there is only one Service Manager
					return;
				}

			}
		}
	}

	public List<ProblemItemDTO> getProblemItems(ProblemStateEnums problemStateEnum, SortEnums sortEnum) {
		return listsManager.getProblemItems(problemStateEnum, sortEnum);
	}

	public ProblemItemDTO getProblemItem(Long problemItemId) {
		return listsManager.getProblemItem(problemItemId);
	}

	public void updateProblemItem(ProblemItemDTO problemItemDTO)
			throws CohomanException {

		// Audit the updated report first
		String assignedTo = "";
		if (problemItemDTO.getAssignedTo() != null && problemItemDTO.getAssignedTo() != 0) {
			assignedTo = getUserFullname(problemItemDTO.getAssignedTo());
		}

		ProblemItemDTO originalProblemItemDTO = listsManager.getProblemItem(problemItemDTO.getProblemitemid());
		
		logger.info("AUDIT: Update Problem Report for "
				+ problemItemDTO.getItemname() + ", by "
				+ getUserFullname(problemItemDTO.getItemCreatedBy())
				+ ", description =\"" + problemItemDTO.getItemdescription()
				+ "\", priority = \"" + problemItemDTO.getPriority()
				+ "\", type = \"" + problemItemDTO.getProblemType()
				+ "\", location = \"" + problemItemDTO.getLocation()
				+ "\", status = \""
				+ problemItemDTO.getProblemStatus()
				+ "\", assigned to = \""
				+ assignedTo
				+ "\"");

		// Now do the actual update
		listsManager.updateProblemItem(problemItemDTO);
		
		// Check to see if status is completed or closed.
		// If so, notify the submitter that
		// the problem is "done".
		User submittedUser = userManager.getUser(problemItemDTO.getItemCreatedBy());

		// First make sure the status has actually changed. If not, just return.
		if (originalProblemItemDTO.getProblemStatus().equals(problemItemDTO.getProblemStatus())) {
			return;
		}
		if (problemItemDTO.getProblemStatus().equals(ProblemStatusEnums.COMPLETED.name()) ||
				problemItemDTO.getProblemStatus().equals(ProblemStatusEnums.CLOSED.name())) {
			listsManager.sendTextMessageToPerson(
					submittedUser.getCellphone(),
					"CCM: Problem Report \"" 
							+ problemItemDTO.getItemname()
							+ "\" has been marked "
							+ problemItemDTO.getProblemStatus().toString()
							+ ".");
		}
	}

	public void deleteProblemItem(ProblemItemDTO problemItemDTO)
			throws CohomanException {

		// Audit the delete
		logger.info("AUDIT: Delete Problem Report: "
				+ problemItemDTO.getItemname() + ", description = \""
				+ problemItemDTO.getItemdescription()
				+ "\", date created = "
				+ problemItemDTO.getPrintableCreatedDate()
				+ "\" deleted by "
				+ LoggingUtils.getCurrentUsername());

		try {
			listsManager.deleteProblemItem(problemItemDTO);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			throw new CohomanException("Unable to delete Problem Item = "
					+ problemItemDTO.getItemname());
		}

	}

	
	/*
	 * (non-Javadoc)
	 * @see org.cohoman.model.service.ListsService#createProblemUpdate(org.cohoman.model.dto.ProblemUpdateDTO)
	 * 
	 * Problem Update operations
	 */
	public void createProblemUpdate(ProblemUpdateDTO dto) throws CohomanException {
		
		// Audit the new problem update
		logger.info("AUDIT: Create Problem Report update for "
				+ getProblemItem(dto.getProblemitemid()).getItemname()
				+ ", by " + getUserFullname(dto.getItemCreatedBy())
				+ ", notes =\"" + dto.getNotes());

		// Now do the real update
		listsManager.createProblemUpdate(dto);
	}

	public List<ProblemUpdateDTO> getProblemUpdatesForProblemItem(Long problemItemId) {
		return listsManager.getProblemUpdatesForProblemItem(problemItemId);
	}
	
	public ProblemUpdateDTO getProblemUpdate(Long problemUpdateId) {
		return listsManager.getProblemUpdate(problemUpdateId);
	}

	public void updateProblemUpdate(ProblemUpdateDTO problemUpdateDTO) {
		
		// Audit the modified update
		try {
			logger.info("AUDIT: Modify Problem Report update for "
					+ getProblemItem(problemUpdateDTO.getProblemitemid())
							.getItemname() + ", by "
					+ getUserFullname(problemUpdateDTO.getItemCreatedBy())
					+ ", notes =\"" + problemUpdateDTO.getNotes());
		} catch (CohomanException ex) {
			// ???
		}

		// Now do the real update
		listsManager.updateProblemUpdate(problemUpdateDTO);

	}

	public void deleteProblemUpdate(Long problemUpdateId) {

		// Audit the delete of this update
		try {
		logger.info("AUDIT: delete Problem Report update added by "
				+ getUserFullname(getProblemUpdate(problemUpdateId).getItemCreatedBy())
				+ ", added on = " + getProblemUpdate(problemUpdateId).getUpdateDate()
				+ ", notes =\"" + getProblemUpdate(problemUpdateId).getNotes());
		} catch (CohomanException ex) {
			// ???
		}
		
		// Now do the delete
		listsManager.deleteProblemUpdate(problemUpdateId);
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.cohoman.model.service.ListsService#getTrashSchedule()
	 * 
	 * Trash
	 * 
	 */
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
