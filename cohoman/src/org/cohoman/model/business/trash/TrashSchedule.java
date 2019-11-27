package org.cohoman.model.business.trash;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.cohoman.model.integration.persistence.beans.TrashCycleBean;
import org.cohoman.model.integration.persistence.beans.TrashSubstitutesBean;

public class TrashSchedule {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private List<TrashRow> trashRows;
	private List<TrashPerson> trashPersonListOrig;
	private List<TrashPerson> trashPersonList;
	private List<TrashCycleBean> trashCycleBeans;
	private List<TrashSubstitutesBean> trashSubstitutions;
	private String newNextUseridToSkip;

	public TrashSchedule(List<TrashPerson> trashPersonListOrig,
			List<TrashCycleBean> trashCycleBeans,
			List<TrashSubstitutesBean> trashSubstitutions) {
		trashRows = new ArrayList<TrashRow>();
		trashPersonList = new ArrayList<TrashPerson>();
		this.trashPersonListOrig = trashPersonListOrig;
		this.trashCycleBeans = trashCycleBeans;
		this.trashSubstitutions = trashSubstitutions;
	}

	public List<TrashPerson> getTrashPersonListOrig() {
		return trashPersonListOrig;
	}

	public void setTrashPersonListOrig(List<TrashPerson> trashPersonListOrig) {
		this.trashPersonListOrig = trashPersonListOrig;
	}
		

	public List<TrashTeam> getTrashTeams(int numberOfCycles) {
		// Find first valid cycle based on the current date. Delete
		// each row that's expired along the way. Then once a good cycle
		// is found, use the startDate and nextPersonToSkip for that
		// cycle. When cycle is complete, need to get the next
		// startDate and nextPersonToSkip for subsequent cycle.
		// If no cycle exist, create it. Do this until we have 4 cycles.

		// Loop through the desired number of cycles.
		List<TrashTeam> trashTeams = null;

		// Set these for first time thru; assume there is at least one entry in
		// the table
		
		for (TrashCycleBean trashCycleBean : trashCycleBeans) {

			// Start by creating TrashPerson list with skipped people removed.
			// (Note: this method sets the nextUseridToSkip value for the next
			// cycle.)
			trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig,
					trashCycleBean.getNextusertoskip());

			// Get all the printable rows, unformatted
			TrashCycle trashCycle = new TrashCycle(trashPersonList,
					trashCycleBean.getTrashcyclestartdate());

			if (trashTeams == null) {
				trashTeams = trashCycle.getTrashTeams();
			} else {
				trashTeams.addAll(trashCycle.getTrashTeams());
			}

			// Remove skipped people from list
			trashPersonList.clear();
		}

		return trashTeams;

	}

	
	public List<TrashTeam> getTrashTeamsForCycle(TrashCycleBean trashCycleBean) {
		
		// Find first valid cycle based on the current date. Delete
		// each row that's expired along the way. Then once a good cycle
		// is found, use the startDate and nextPersonToSkip for that
		// cycle. When cycle is complete, need to get the next
		// startDate and nextPersonToSkip for subsequent cycle.
		// If no cycle exist, create it. Do this until we have 4 cycles.

		// Loop through the desired number of cycles.
		List<TrashTeam> trashTeams = null;

		// Set these for first time thru; assume there is at least one entry in
		// the table
		Date localCycleStartingDate = trashCycleBean.getTrashcyclestartdate();
		String localNextUseridToSkip = trashCycleBean.getNextusertoskip();

			// Start by creating TrashPerson list with skipped people removed.
			// (Note: this method sets the nextUseridToSkip value for the next
			// cycle.)
			trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig,
					localNextUseridToSkip);

			// Get all the printable rows, unformatted
			TrashCycle trashCycle = new TrashCycle(trashPersonList,
					localCycleStartingDate);

			trashTeams = trashCycle.getTrashTeams();

			// Remove skipped people from list
			trashPersonList.clear();

		return trashTeams;

	}

	
	public TrashCycleBean getNextTrashCycleDBRow(TrashCycleBean trashCycleBeanIn) {

		// Find first valid cycle based on the current date. Delete
		// each row that's expired along the way. Then once a good cycle
		// is found, use the startDate and nextPersonToSkip for that
		// cycle. When cycle is complete, need to get the next
		// startDate and nextPersonToSkip for subsequent cycle.
		// If no cycle exist, create it. Do this until we have 4 cycles.

		// Compute number of teams in one cycle
		int teamsInOneCycle = trashPersonListOrig.size();
		int peopleToSkip = trashPersonListOrig.size() % 4;

		teamsInOneCycle = teamsInOneCycle / 4;
		int daysInCycle = teamsInOneCycle * 7;

		// See if there "ever" was a nextPersonToSkip so we can continue 
		// with that user.
		String oldNextUserToSkip = null;
		if (trashCycleBeanIn.getNextusertoskip() == null && peopleToSkip > 0) {
			oldNextUserToSkip = retrieveOldNextUserToSkip(trashCycleBeanIn.getTrashcycleid());
		}

		// Base next cycle on values from the last/current cycle
		Date currentCycleStartingDate = trashCycleBeanIn.getTrashcyclestartdate();
		String currentNextUseridToSkip = trashCycleBeanIn.getNextusertoskip();
		if (currentNextUseridToSkip == null) {
			currentNextUseridToSkip = oldNextUserToSkip;
		}

		// Make sure the prospective NextUseridToSkip actually exists in the
		// list of TrashPeople. Do this by calling a method to guess the
		// next TrashPerson based on the unit number of the "gone" user.
		currentNextUseridToSkip = chooseAnotherUserIfGone(trashCycleBeanIn);
		
		// Start by creating TrashPerson list with skipped people removed.
		// (Note: this method sets the nextUseridToSkip value for the next
		// cycle.)
		trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig,
				currentNextUseridToSkip);

		// Create a new TrashCycle bean for another cycle.
		Calendar calCycleStartingDate = Calendar.getInstance();
		calCycleStartingDate.setTime(currentCycleStartingDate);
		calCycleStartingDate.add(Calendar.DAY_OF_YEAR, daysInCycle);
		Date newCycleStartingDate = calCycleStartingDate.getTime();

		TrashCycleBean trashCycleBeanOut = new TrashCycleBean();
		trashCycleBeanOut.setTrashcyclestartdate(newCycleStartingDate);
		trashCycleBeanOut.setTrashcycleenddate(newCycleStartingDate);
		trashCycleBeanOut.setNextusertoskip(this.newNextUseridToSkip);
		trashCycleBeanOut.setLastuserskipped(currentNextUseridToSkip);
		String unitNumber = 
				lookupUsersUnitNumber(trashPersonListOrig, currentNextUseridToSkip);
		trashCycleBeanOut.setLastunitskipped(unitNumber);
		
		// Advance date to show the last row in the cycle
		calCycleStartingDate.add(Calendar.DAY_OF_YEAR, daysInCycle - 1);
		trashCycleBeanOut.setTrashcycleenddate(calCycleStartingDate.getTime());
		return trashCycleBeanOut;

	}

	/*
	 * The purpose of this method is to handle the case where we don't know
	 * the nextUserToSkip for a CycleBean that we're about to create because
	 * the previous one(s) have a null value for nextUserToSkip. This isn't
	 * an error case. A null value simply indicates that there are no users
	 * to skip since the number of users fits perfectly into a certain number
	 * of teams (i.e. are mod 4).
	 */
	private String retrieveOldNextUserToSkip(long currentCycleid) {
		
		// Search the list of beans in reverse until we find the one we're
		// starting with in our quest to find the earlier bean that might
		// have had an earlier user to skip. If none is found, just start
		// with the first person on the list.
		boolean beanFound = false;
		for (int beanIdx = (trashCycleBeans.size() - 1); beanIdx >= 0; beanIdx--) {
			
			if (!beanFound) {
				if (trashCycleBeans.get(beanIdx).getTrashcycleid() == currentCycleid) {
					beanFound = true;
				}
				continue;		
			}
			
			// Found the bean. Now keep going to see if we can find a valid
			// nextUseridToSkip.
			if (trashCycleBeans.get(beanIdx).getNextusertoskip() != null) {
				return trashCycleBeans.get(beanIdx).getNextusertoskip();
			}
		}
		return trashPersonListOrig.get(0).getUsername();
	}
	
	private String chooseAnotherUserIfGone(TrashCycleBean trashCycleBeanIn) {
		
		// If that user already exists in the list, then just return it
		for (TrashPerson onePerson : trashPersonListOrig) {
			if (onePerson.getUsername().equalsIgnoreCase(
					trashCycleBeanIn.getNextusertoskip())) {
				return trashCycleBeanIn.getNextusertoskip();
			}
		}
		
		// Can't find the user. Use the last user unit to sequence through
		// the TrashPerson list and choose the next person.
		boolean foundIt = false;
		for (TrashPerson onePerson : trashPersonListOrig) {
			if (foundIt) {
				// Now we've advanced since we found the entry so
				// return that entry's username.
				logger.info("Next TrashPerson not found. Looked for " + 
						trashCycleBeanIn.getNextusertoskip() +
						", but will use the following instead " +
						onePerson.getUsername());
				return onePerson.getUsername();
			}
			
			// Check for a match on unit number
			if (onePerson.getUnitnumber().equals(
					trashCycleBeanIn.getLastunitskipped())) {
				// Found an entry with matching unit. Advance to the next
				// TrashPerson entry.
				foundIt = true;
			}
			
		}
		
		// Nothing found. Give-up and start again.
		logger.info("Giving up on finding a TrashPerson entry for " + 
				trashCycleBeanIn.getNextusertoskip());
		return trashPersonListOrig.get(0).getUsername();
		
	}

	public List<TrashRow> getTrashRowsNew(TrashCycleBean trashCycleBean) {

		SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");

		// Now make a list of printable rows
		List<TrashTeam> trashTeams = getTrashTeamsForCycle(trashCycleBean);
		for (TrashTeam oneTeam : trashTeams) {

			// Make each row for printing
			TrashRow trashRow = new TrashRow();
			trashRow.setSundayDate(formatter.format(oneTeam.getSundayDate()
					.getTime()));
			if (oneTeam.getOrganizer() == null) {
				trashRow.setOrganizer("");
			} else {
				String organizerUsername = oneTeam.getOrganizer().getUsername();
				trashRow.setOrganizer(organizerUsername);
			}
			
			if (oneTeam.getStrongPerson() == null) {
				trashRow.setStrongPerson("");
			} else {
				String strongUsername = oneTeam.getStrongPerson().getUsername();
				trashRow.setStrongPerson(strongUsername);
			}
			
			if (oneTeam.getTeamMember1() == null) {
				trashRow.setTeamMember1("");
			} else {
				String member1Username = oneTeam.getTeamMember1().getUsername();
				trashRow.setTeamMember1(member1Username);
			}
			
			if (oneTeam.getTeamMember2() == null) {
				trashRow.setTeamMember2("");
			} else {
				String member2Username = oneTeam.getTeamMember2().getUsername();
				trashRow.setTeamMember2(member2Username);
			}
			trashRows.add(trashRow);
		}

		return trashRows;
	}

		
	public void addTrashRow(TrashRow trashRow) {
		trashRows.add(trashRow);
	}
	
	private List<TrashPerson> removeSkippedPeopleFromList(
			List<TrashPerson> trashPersonListOrig, String nextPersonToSkip) {

		// Probably make this local not global ????
		trashPersonList.clear();
		
		// Determine if extra people must be skipped for each cycle based on
		// mod of 4 people per team.
		int peopleToSkip = trashPersonListOrig.size() % 4;

		// Copy TrashPerson list to local copy removing any people to skip.
		boolean skippingMode = false;
		for (int tpIdx = 0; tpIdx < trashPersonListOrig.size(); tpIdx++) {
			if (skippingMode) {
				// Already in skipping mode. Count them down until 0.
				if (peopleToSkip != 0) {
					peopleToSkip--;
					continue;
				} else {
					// Have now skipped them all. We can now know who will
					// be the next person to skip.
					skippingMode = false;
					this.newNextUseridToSkip = trashPersonListOrig.get(tpIdx)
							.getUsername();
				}
			}
			
			
			if (peopleToSkip != 0) {
				// We have people to skip and we next check if we've hit
				// the exact person that we need to skip.
				if (trashPersonListOrig.get(tpIdx).getUsername()
						.equalsIgnoreCase(nextPersonToSkip)) {
					// OK, we hit the person that marks the start of
					// skipping people. Indicate that we are in 
					// skipping mode and proceed to the next entry
					// thereby skipping our first (and maybe only)
					// person.
					peopleToSkip--;
					skippingMode = true;
				} else {
					// This is not a person to skip. Just copy to the
					// list.
					trashPersonList.add(trashPersonListOrig.get(tpIdx));
				}
			} else {
				// No people to skip at all. This is either because
				// there aren't any or because they've already been
				// skipped.
				trashPersonList.add(trashPersonListOrig.get(tpIdx));
			}
		}

		// If finished loop without having skipped all the entries in the
		// first pass, must wraparound and remove those entries from the
		// beginning
		// of the TrashPersonList that we're creating.
		if (skippingMode) {
			int tpIdx = 0;
			while (peopleToSkip > 0) {
				trashPersonList.remove(tpIdx);
				tpIdx++;
				peopleToSkip--;
			}
			this.newNextUseridToSkip = trashPersonListOrig.get(tpIdx)
					.getUsername();
		}

		return trashPersonList;
	}
	
	private String lookupUsersUnitNumber(List<TrashPerson> trashPersonList, String username) {
		
		for (TrashPerson trashPerson : trashPersonList) {
			if (trashPerson.getUsername().equalsIgnoreCase(username)) {
				return trashPerson.getUnitnumber();
			}
		}
		return "";
	}
}
