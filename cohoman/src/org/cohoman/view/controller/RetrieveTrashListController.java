package org.cohoman.view.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.business.trash.TrashPerson;
import org.cohoman.model.business.trash.TrashRolesEnums;
import org.cohoman.model.business.trash.TrashRow;
import org.cohoman.model.integration.persistence.beans.TrashSubstitutesBean;
import org.cohoman.model.service.ListsService;
import org.cohoman.model.service.UserService;

@ManagedBean
@SessionScoped
public class RetrieveTrashListController implements Serializable {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private static final long serialVersionUID = 4678206276499587830L;

	private ListsService listsService = null;
	private UserService userService = null;
	private String trashTeamStartDate;
	private String chosenTeamMember;
	private String chosenSubstitute;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ListsService getListsService() {
		return listsService;
	}

	public void setListsService(ListsService listsService) {
		this.listsService = listsService;
	}

	public List<TrashRow> getTrashList() {
		
		List<TrashRow> trashRows = null;
		try {
			trashRows = listsService.getTrashSchedule();

		} catch (CohomanException ex) {
			logger.severe("CohomanException from getTrashSchedule: " + ex.getCause());
		}

		return trashRows;
	}

	public String getChosenTeamMember() {

		// Get the substitution bean/row from the DB for the current
		// row based on date and team member. If there is no such row set or
		// in the database, this method will simply return the last set
		// value in this class for the chosenTeamMember.
		TrashSubstitutesBean theTrashSubstituteBean = listsService
				.getTrashSubstitute(trashTeamStartDate, chosenTeamMember);

		// Check if any substitutions exist. If so, access the DB to set
		// the value of the associated substitute.
		if (theTrashSubstituteBean != null) {
			chosenSubstitute = theTrashSubstituteBean.getSubstituteusername();
		}
		return chosenTeamMember;
	}

	public void setChosenTeamMember(String chosenTeamMember) {

		this.chosenTeamMember = chosenTeamMember;

		// new stuff: however not sure if this code does anything 10/23/2019
		List<TrashRow> trashRows = listsService.getTrashTeamsFromDB();
		for (int idx = 0; idx < trashRows.size(); idx++) {
			if (trashTeamStartDate.equals(trashRows.get(idx).getSundayDate())) {
				// found team. 
				TrashSubstitutesBean theTrashSubstituteBean = listsService
						.getTrashSubstitute(trashTeamStartDate, chosenTeamMember);

				// Check if any substitutions exist. If so, access the DB to set
				// the value of the associated substitute.
				if (theTrashSubstituteBean != null) {
					chosenSubstitute = theTrashSubstituteBean.getSubstituteusername();
				}
				break;
			}
		}
/*
		// If we just set the team member, also set the associated
		// substitute for that team member (if there is one).
		List<TrashTeam> trashTeams = listsService.getTrashTeams(NUMBER_OF_TRASH_CYCLES);
		for (int idx = 0; idx < trashTeams.size(); idx++) {
			if (trashTeamStartDate.equals(trashTeams.get(idx)
					.getPrintableDate())) {
				// Found team. Determine which user to check for the substitute
				String chosenSubstitutetmp = "";
				TrashTeam theTrashTeam = trashTeams.get(idx);

				if (theTrashTeam.getOrganizer().getUsername()
						.equals(chosenTeamMember)) {
					chosenSubstitutetmp = theTrashTeam.getOrganizerSub();
				} else if (theTrashTeam.getStrongPerson().getUsername()
						.equals(chosenTeamMember)) {
					chosenSubstitutetmp = theTrashTeam.getStrongPersonSub();
				} else if (theTrashTeam.getTeamMember1().getUsername()
						.equals(chosenTeamMember)) {
					chosenSubstitutetmp = theTrashTeam.getTeamMember1Sub();
				} else if (theTrashTeam.getTeamMember2().getUsername()
						.equals(chosenTeamMember)) {
					chosenSubstitutetmp = theTrashTeam.getTeamMember2Sub();
				}
				chosenSubstitute = chosenSubstitutetmp;
				break;
			}
		}
*/
	}

	public String getChosenSubstitute() {

		// Consult the database to see if there is a row for the specified
		// date and team member. If there is, get the substitute from the DB.
		TrashSubstitutesBean theTrashSubstituteBean = listsService
				.getTrashSubstitute(trashTeamStartDate, chosenTeamMember);
		if (theTrashSubstituteBean != null) {
			// Found a substitute. Set it in the class variable.
			chosenSubstitute = theTrashSubstituteBean.getSubstituteusername();
		} else {
			// No substitute for this team member => make it null
			chosenSubstitute = null;
		}
		return chosenSubstitute;
	}

	public void setChosenSubstitute(String chosenSubstitute) {
		this.chosenSubstitute = chosenSubstitute;
	}

	public String getTrashTeamStartDate() {

		// If param value is passed in, use it. Otherwise, leave
		// as is.
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		String trashTeamStartDateTmp = requestParams.get("trashTeamStartDate");
		if (trashTeamStartDateTmp != null && !trashTeamStartDateTmp.isEmpty()) {
			trashTeamStartDate = trashTeamStartDateTmp;
		}

		if (trashTeamStartDate == null || trashTeamStartDate.isEmpty()) {
			String errorMsg = "RetrieveTrashListController: trashTeamStartDate isn't set";
			logger.severe(errorMsg);
			throw new RuntimeException(errorMsg);
		}

		return trashTeamStartDate;
	}

	public void setTrashTeamStartDate(String trashTeamStartDate) {
		this.trashTeamStartDate = trashTeamStartDate;
	}

	// Get team members for the drop-down.
	// Assumes team set in trashTeamStartDate
	public List<String> getTeamMembers() {

		List<TrashRow> trashRows = listsService.getTrashTeamsFromDB();
		List<String> teamMembersList = new ArrayList<String>();
		for (int idx = 0; idx < trashRows.size(); idx++) {
			if (trashTeamStartDate.equals(trashRows.get(idx).getSundayDate())) {
				teamMembersList.add(trashRows.get(idx).getOrganizer());
				teamMembersList.add(trashRows.get(idx).getStrongPerson());
				teamMembersList.add(trashRows.get(idx).getTeamMember1());
				teamMembersList.add(trashRows.get(idx).getTeamMember2());
				break;
			}
		}
		// sanity check
		if (teamMembersList.isEmpty()) {
			String errorMsg = "RetrieveTrashListController: No trash team found for date "
					+ trashTeamStartDate;
			logger.severe(errorMsg);
			throw new RuntimeException(errorMsg);
		}
		return teamMembersList;
		
	}

	public List<String> getTrashPeopleForSubstitutes() {

		// Create the list of people who can be a substitute
		List<TrashPerson> trashPersonList = listsService
				.getTrashPersonListOrig();

		// Exclude team members from the list
		List<String> teamMembers = getTeamMembers();

		List<String> userList = new ArrayList<String>();
		for (TrashPerson trashPerson : trashPersonList) {
			// Exclude people with no trash role
			if (trashPerson.getTrashRole() == TrashRolesEnums.NOROLE.name()) {
				continue;
			}

			// Exclude the team members here.
			boolean isTeamMember = false;
			for (String oneMember : teamMembers) {
				if (oneMember.equals(trashPerson.getUsername())) {
					isTeamMember = true;
					break;
				}
			}
			if (isTeamMember) {
				continue;
			}

			// Add user to list of possible substitutes
			userList.add(trashPerson.getUsername());
		}

		// Sort the list for the display
		Collections.sort(userList);
		return userList;
	}

	public String modifyTrashSubstituteTableView() throws Exception {

		// Perform actual update of the substitute table

		// Error check to make sure that the original team member has been
		// chosen
		if (chosenTeamMember == null || chosenTeamMember.isEmpty()) {
			FacesMessage message = new FacesMessage(
					"User Error: you must choose the original team member.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		// See if an entry already exists in the trashSubstitutes table for
		// the specified date and team member. If it does, delete it.
		TrashSubstitutesBean theTrashSubstituteBean = listsService
				.getTrashSubstitute(trashTeamStartDate, chosenTeamMember);
		if (theTrashSubstituteBean != null) {
			// already have an entry. Delete it first.
			listsService.deleteTrashSubstitute(
					theTrashSubstituteBean.getSubstitutesid(),
					theTrashSubstituteBean.getStartingdate(),
					theTrashSubstituteBean.getOrigusername());
		}

		if (chosenSubstitute == null || chosenSubstitute.isEmpty()) {
			// See if substitution is "No Substitute" (as shown by an empty
			// string). If so, delete the entry from the TrashSubstitutes table.
			// First, find out the SubstitutesId based on the date and username.
			long substitutesId = 0;
			Date startingDate = null;
			String origUsername = null;
			List<TrashSubstitutesBean> substituteBeans = listsService
					.getTrashSubstitutes();
			if (substituteBeans != null) {
				// May be a row to delete
				for (TrashSubstitutesBean trashSubstitutesBean : substituteBeans) {
					SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
					if (sdf.format(trashSubstitutesBean.getStartingdate())
							.equals(trashTeamStartDate)
							&& trashSubstitutesBean.getOrigusername().equals(
									chosenTeamMember)) {
						substitutesId = trashSubstitutesBean.getSubstitutesid();
						startingDate = trashSubstitutesBean.getStartingdate();
						origUsername = trashSubstitutesBean.getOrigusername();
						break;
					}
				}
				if (substitutesId != 0) {
					listsService.deleteTrashSubstitute(substitutesId,
							startingDate, origUsername);
				}
			}
			// else no rows whatsoever thus nothing to delete or add; drop thru
		} else {
			// Time to add a new substitute to the DB. Do it!
			Date startDateAsDate = new SimpleDateFormat("MMM d, yyyy")
					.parse(trashTeamStartDate);
			listsService.setTrashSubstitute(startDateAsDate, chosenTeamMember,
					chosenSubstitute);
		}
		return "createTrashSubstitution";
	}

}
