package org.cohoman.model.business.trash;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.integration.utils.FederalHolidays;
import org.cohoman.view.controller.CohomanException;

public class TrashCycle {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private List<TrashTeam> trashTeams;
	private List<TrashPerson> trashPersonListOrig;
	private List<TrashPerson> trashPersonList;
	private List<TrashPerson> trashOrganizers;
	private List<TrashPerson> trashStrongPersons;
	private List<TrashPerson> trashTeamMembers;
	private List<String> multiplePersonUnits;
	private List<Integer> multiplePersonUnitsCounts;
	private Date startingDate;   // starting date for the cycle

	public TrashCycle(List<TrashPerson> trashPersonListOrig, Date startingDate) {
		trashTeams = new ArrayList<TrashTeam>();
		trashOrganizers = new ArrayList<TrashPerson>();
		trashStrongPersons = new ArrayList<TrashPerson>();
		trashTeamMembers = new ArrayList<TrashPerson>();
		multiplePersonUnits = new ArrayList<String>();
		multiplePersonUnitsCounts = new ArrayList<Integer>();
		this.trashPersonListOrig = trashPersonListOrig;
		this.startingDate = startingDate;
	}

	public List<TrashTeam> getTrashTeams() throws CohomanException {

		int randomizedIndex; 

		// Initialize an empty working list of TrashPersons
		trashPersonList = new ArrayList<TrashPerson>();
		
		// Fill in the TrashPerson list from the original list.
		for (int idx = 0; idx < trashPersonListOrig.size(); idx++) {
			trashPersonList.add(trashPersonListOrig.get(idx));
		}
		
		// Compute number of teams in one cycle
		int teamsInOneCycle = trashPersonList.size();
		teamsInOneCycle = teamsInOneCycle / 4;  // 4 people on each team

		// Figure out which units have multiple people in them.
		computeMultiplePersonUnits();

		// Walk through the TrashPerson list making lists of
		// TrashPersons based on role.
		for (TrashPerson oneTrashPerson : trashPersonList) {
			if (oneTrashPerson.getTrashRole().equalsIgnoreCase(
					TrashRolesEnums.ORGANIZER.name())) {
				trashOrganizers.add(oneTrashPerson);
			} else if (oneTrashPerson.getTrashRole().equalsIgnoreCase(
					TrashRolesEnums.STRONGPERSON.name())) {
				trashStrongPersons.add(oneTrashPerson);
			} else {
				trashTeamMembers.add(oneTrashPerson);
			}
		}

		// Sanity check the number of organizers
		if (trashOrganizers.size() > teamsInOneCycle) {
			logger.severe("Too many organizers identified for Trash Schedule"
					+ trashOrganizers.size());
		}

		// Create all teams with starting dates
		SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
		Calendar workingCal = Calendar.getInstance();
		workingCal.setTime(startingDate);
		for (int teamIndex = 0; teamIndex < teamsInOneCycle; teamIndex++) {
			
			// Adjust start date for team iff holiday
			workingCal = adjustToHoliday(workingCal);
			
			// Add team.
			TrashTeam trashTeam = new TrashTeam();
			trashTeam.setSundayDate(workingCal.getTime());
			trashTeam.setPrintableDate(formatter.format(workingCal.getTime()));
			trashTeams.add(trashTeam);
			
			// Advance to next trash day, typically 7 days but will be
			// 6 if current date is Monday.
			workingCal.add(Calendar.DAY_OF_YEAR,
					incrementForNextTrashDay(workingCal)); 
		}

		// Start by simply adding the organizers to each team in sequence
		for (TrashPerson oneTrashPerson : trashOrganizers) {
			for (TrashTeam thisTrashTeam : trashTeams) {
				if (thisTrashTeam.getOrganizer() == null) {
					thisTrashTeam.setOrganizer(oneTrashPerson);
					break;
				}
			}
		}

		// Empty the list of Organizers since they've all been added to teams.
		trashOrganizers.clear();

		// RANDOMLY fill remainder of Trash Teams with an organizer who has the role 
		// of Team Member. 10/25/2019
		for (TrashTeam thisTrashTeam : trashTeams) {
			if (thisTrashTeam.getOrganizer() == null) {
				// Get the next team member
				if (!trashTeamMembers.isEmpty()) {
					randomizedIndex = randomEntry(trashTeamMembers.size());
					// Special case: don't use jean as an organizer
					if (trashTeamMembers.get(randomizedIndex).getUsername().equals("jean")) {
						// User is jean. See if there's another teammember we can use.
						if (trashTeamMembers.size() >= 2 && randomizedIndex != 0) {
							// if there's another entry and we can use 0, use it
							thisTrashTeam.setOrganizer(trashTeamMembers.get(0));
							trashTeamMembers.remove(0);							
						} else {
							// Give up and use a strong person below.
							break;
						}
					} else {
						// user isn't jean. Just set the organizer.
						thisTrashTeam.setOrganizer(trashTeamMembers.get(randomizedIndex));
						trashTeamMembers.remove(randomizedIndex);
					}
				} else {
					break; // give up when list is depleted (highly unlikely)
				}
			}
		}		
		
		// In the most unlikely case, have to resort to strong people to finish 
		// filling the organizers for all teams:
		// Next RANDOMLY, finish out the strong person for all teams by walking through
		// the strong people. (10/29/2019)
		for (TrashTeam thisTrashTeam : trashTeams) {
			if (thisTrashTeam.getOrganizer() == null) {
				// Get the next strong person
				if (!trashStrongPersons.isEmpty()) {
					randomizedIndex = randomEntry(trashStrongPersons.size());
					thisTrashTeam.setOrganizer(trashStrongPersons.get(randomizedIndex));
					trashStrongPersons.remove(randomizedIndex);
				} else {
					logger.severe("Too few strong people identified for Trash Schedule");
				}
			}
		}

		// Make sure there aren't multiple people from the same unit
		// that are organizers
		checkForMultipleOrganizersInSameUnit();
		
		// Walk thru list of organizers checking if unit is multiperson.
		// If so, add other person(s) from same unit.
		String organizerUnit = "";
		for (TrashTeam thisTrashTeam : trashTeams) {

			if (thisTrashTeam.getOrganizer() == null) {
				break; // all done with teams with organizers
			}
			organizerUnit = thisTrashTeam.getOrganizer().getUnitnumber();
			if (multiplePersonUnits.contains(organizerUnit)) {

				// Organizer is multiperson unit. Search TrashPerson
				// list to get all the other persons and add them to the
				// team
				for (TrashPerson trashPerson : trashPersonList) {
					if (trashPerson.getUnitnumber().equals(organizerUnit)
							// Not the organizer we already have on a team
							&& !(trashPerson.getUsername()
									.equalsIgnoreCase(thisTrashTeam
											.getOrganizer().getUsername()))) {

						// Add this TrashPerson as either strong or member for
						// organizer's team
						addTrashPersonToSameTeam(thisTrashTeam, trashPerson);
					}
				}
			}

			// Remove the corresponding multiperson unit since we've added it to
			// the team.
			for (int mupIndex = 0; mupIndex < multiplePersonUnits.size(); mupIndex++) {
				if (multiplePersonUnits.get(mupIndex).equals(organizerUnit)) {
					multiplePersonUnits.remove(mupIndex);
					multiplePersonUnitsCounts.remove(mupIndex);
					break;
				}
			}
		}

		// Add units that have multiple people in them.
		for (int mupIndex = 0; mupIndex < multiplePersonUnits.size(); mupIndex++) {
			String multipleUnitnumber = multiplePersonUnits.get(mupIndex);
			int teamIndexToUse = findFirstTeamIndexWithEnoughOpenings(multiplePersonUnitsCounts
					.get(mupIndex));
			if (teamIndexToUse == -1) {
				// No room for multiple people in the current teams. So, break
				// out of this loop, assuming that the people will be added
				// to different teams.
				break;
			}

			// Sequence through the TrashPerson list adding all people to the
			// selected team that happen to be in the same unit.
			// Note: have to disallow 4 people in same unit (e.g. unit 107)
			int peopleInUnit = 0;
			for (TrashPerson trashPerson : trashPersonList) {
				
				if (trashPerson.getUnitnumber().equals(multipleUnitnumber) &&
						peopleInUnit < 3) {

					// Add this TrashPerson as either strong or member
					addTrashPersonToSameTeam(trashTeams.get(teamIndexToUse),
							trashPerson);

					// Remove person from role list (Strong or Member)
					if (trashPerson.getTrashRole() == TrashRolesEnums.STRONGPERSON
							.name()) {
						trashStrongPersons.remove(trashPerson);
					}
					if (trashPerson.getTrashRole() == TrashRolesEnums.TEAMMEMBER
							.name()) {
						trashTeamMembers.remove(trashPerson);
					}
					peopleInUnit++;
				}

			}

		}

		// Next RANDOMLY, finish out the strong person for all teams by walking through
		// the strong people.
		for (TrashTeam thisTrashTeam : trashTeams) {
			if (thisTrashTeam.getStrongPerson() == null) {
				// Get the next strong person
				if (!trashStrongPersons.isEmpty()) {
					randomizedIndex = randomEntry(trashStrongPersons.size());
					thisTrashTeam.setStrongPerson(trashStrongPersons.get(randomizedIndex));
					trashStrongPersons.remove(randomizedIndex);
				} else {
					logger.severe("Too few strong people identified for Trash Schedule");
				}
			}
		}

		// Next RANDOMLY, use strong people as team members, if any left
		// Start with TeamMember1
		if (!trashStrongPersons.isEmpty()) {
			for (TrashTeam thisTrashTeam : trashTeams) {
				if (thisTrashTeam.getTeamMember1() == null) {
					// Get the next strong person
					if (!trashStrongPersons.isEmpty()) {
						randomizedIndex = randomEntry(trashStrongPersons.size());
						thisTrashTeam.setTeamMember1(trashStrongPersons.get(randomizedIndex));
						trashStrongPersons.remove(randomizedIndex);
					} else {
						break; // give up when list is depleted
					}
				}
			}
		}
		// Then do TeamMember2
		if (!trashStrongPersons.isEmpty()) {
			for (TrashTeam thisTrashTeam : trashTeams) {
				if (thisTrashTeam.getTeamMember2() == null) {
					// Get the next strong person
					if (!trashStrongPersons.isEmpty()) {
						randomizedIndex = randomEntry(trashStrongPersons.size());
						thisTrashTeam.setTeamMember2(trashStrongPersons.get(randomizedIndex));
						trashStrongPersons.remove(randomizedIndex);
					} else {
						break; // give up when list is depleted
					}
				}
			}
		}

		// Lastly, RANDOMLY fill up team members slots with team members
		if (!trashTeamMembers.isEmpty()) {
			for (TrashTeam thisTrashTeam : trashTeams) {
				if (thisTrashTeam.getTeamMember1() == null) {
					// Get the next team member
					if (!trashTeamMembers.isEmpty()) {
						randomizedIndex = randomEntry(trashTeamMembers.size());
						thisTrashTeam.setTeamMember1(trashTeamMembers.get(randomizedIndex));
						trashTeamMembers.remove(randomizedIndex);
					} else {
						break; // give up when list is depleted
					}
				}
			}
		}
		if (!trashTeamMembers.isEmpty()) {
			for (TrashTeam thisTrashTeam : trashTeams) {
				if (thisTrashTeam.getTeamMember2() == null) {
					// Get the next team member
					if (!trashTeamMembers.isEmpty()) {
						randomizedIndex = randomEntry(trashTeamMembers.size());
						thisTrashTeam.setTeamMember2(trashTeamMembers.get(randomizedIndex));
						trashTeamMembers.remove(randomizedIndex);
					} else {
						break; // give up when list is depleted
					}
				}
			}
		}
	
		// Perform a robustness check that all role lists have been used.
		if (!trashOrganizers.isEmpty()) {
			String errorMsg = "TrashCycle error: leftover Organizers including " + 
					trashOrganizers.get(0).getUsername();
			logger.log(Level.SEVERE, errorMsg);
			throw new RuntimeException(errorMsg);
		}
		if (!trashStrongPersons.isEmpty()) {
			String errorMsg = "TrashCycle error: leftover Strong Persons including " + 
					trashStrongPersons.get(0).getUsername();
			logger.log(Level.SEVERE, errorMsg);
			throw new RuntimeException(errorMsg);
		}
		if (!trashTeamMembers.isEmpty()) {
			String errorMsg = "TrashCycle error: leftover Team members including " + 
					trashTeamMembers.get(0).getUsername();
			logger.log(Level.SEVERE, errorMsg);
			throw new RuntimeException(errorMsg);
		}

		// Also for robustness, make sure all the usernames in the trash teams
		// for this cycle are unique.
		String duplicateUsername = checkForTeamMembersAllUnique();
		if (duplicateUsername != null) {
			String errorMsg = "TrashCycle error: duplicate name found: " + 
					duplicateUsername;
			logger.log(Level.SEVERE, errorMsg);
			throw new RuntimeException(errorMsg);
		}
		return trashTeams;
	}

	
	private void computeMultiplePersonUnits() {

		// Figure out which units have multiple TrashPersons in them
		String currentUnit = "";
		int currentCount = 0;

		for (TrashPerson oneTrashPerson : trashPersonList) {
			// First, handle the case of the first unit in a sequence.
			if (currentUnit.length() == 0) {
				currentUnit = oneTrashPerson.getUnitnumber();
				currentCount++;
				continue;
			}
			
			// Not first in sequence. Does this one match the
			// previous unit number?
			if (oneTrashPerson.getUnitnumber().equals(currentUnit)) {
				// Yes, another one in the sequence. Increment count
				// and keep going.
				// However, max number per unit per team is 3 (107 case)
				if (currentCount == 3) {
					// 107 case. Don't allow 4th person in this unit.
					continue;
				} else {
					currentCount++;
				}
			} else {
				// No. Special case unit 103 so not together
				if (currentCount > 1 && !currentUnit.equals("103")) {
					// If count > 1, then save new entry in multiplePersonUnits
					// to indicate that we have a multiple person unit.
					multiplePersonUnits.add(currentUnit);
					multiplePersonUnitsCounts.add(currentCount);
				}

				// Reset values for possible new sequence.
				currentUnit = oneTrashPerson.getUnitnumber();
				currentCount = 1;
			}
		}

		// Save list entry multiplePersonUnit entry if we have one.
		if (currentCount > 1) {
			// If count > 1, then save new entry in multiplePersonUnits
			// to indicate that we have a multiple person unit.
			multiplePersonUnits.add(currentUnit);
			multiplePersonUnitsCounts.add(currentCount);
		}

	}

	private int findFirstTeamIndexWithEnoughOpenings(int desiredOpenings) {

		int actualCount;

		for (int teamIndex = 0; teamIndex < trashTeams.size(); teamIndex++) {

			// Compute number of team slots available
			actualCount = 0;
			TrashTeam trashTeam = trashTeams.get(teamIndex);
			if (trashTeam.getOrganizer()== null) {
				actualCount++;
			}
			if (trashTeam.getStrongPerson() == null) {
				actualCount++;
			}
			if (trashTeam.getTeamMember1() == null) {
				actualCount++;
			}
			if (trashTeam.getTeamMember2() == null) {
				actualCount++;
			}

			if (desiredOpenings <= actualCount) {
				return teamIndex;
			}
		}
		return -1;
	}

	private boolean addTrashPersonToSameTeam(TrashTeam trashTeam,
			TrashPerson trashPerson) {

		// Start with StrongPerson
		if (trashPerson.getTrashRole().equalsIgnoreCase(
				TrashRolesEnums.STRONGPERSON.name())) {

			// It is strong. Try to take "strong' slot.
			if (trashTeam.getStrongPerson() == null) {

				// Add strong person to team
				trashTeam.setStrongPerson(trashPerson);

				// But, also remove it from the list of strong persons
				trashStrongPersons.remove(trashPerson);
				return true;
			}

			// No strong slot. How about a member slot?
			// Try member slot 1
			if (trashTeam.getTeamMember1() == null) {
				trashTeam.setTeamMember1(trashPerson);
				trashStrongPersons.remove(trashPerson);
				return true;
			}

			// Try member slot 2
			if (trashTeam.getTeamMember2() == null) {
				trashTeam.setTeamMember2(trashPerson);
				trashStrongPersons.remove(trashPerson);
				return true;
			}

			// Last try: an organizer
			if (trashTeam.getOrganizer() == null) {
				trashTeam.setOrganizer(trashPerson);
				trashStrongPersons.remove(trashPerson);
				return true;
			}

		}

		// Not a Strong slot. Try member1 and then member2.
		if (trashPerson.getTrashRole().equalsIgnoreCase(
				TrashRolesEnums.TEAMMEMBER.name())) {


			// Try member slot 1
			if (trashTeam.getTeamMember1() == null) {
				trashTeam.setTeamMember1(trashPerson);
				trashTeamMembers.remove(trashPerson);
				return true;
			}

			// Try member slot 2
			if (trashTeam.getTeamMember2() == null) {
				trashTeam.setTeamMember2(trashPerson);
				trashTeamMembers.remove(trashPerson);
				return true;
			}
	
			// TODO
			// Try an organizer first to lessen the??????????
			// likelihood that a subsequent strong person
			// will end up as the organizer.
			if (trashTeam.getOrganizer() == null) {
				trashTeam.setOrganizer(trashPerson);
				trashTeamMembers.remove(trashPerson);
				return true;
			}

			
		}

		return false;

	}
	
	private void checkForMultipleOrganizersInSameUnit() {
		
		// Build a sorted list of units for all organizers
		// (this may include people serving as organizers who
		// don't have a role as organizer.
		List<String> localOrgsList = new ArrayList<String>();
		for (TrashTeam thisTrashTeam : trashTeams) {
			localOrgsList.add(thisTrashTeam.getOrganizer().getUnitnumber());
		}
		Collections.sort(localOrgsList);
		
		// Search list of unit numbers to see if duplicates. If
		// so, will have to remove them from the multiperson units
		// so they won't be added again.
		for (int idx = 0; idx < localOrgsList.size(); idx++) {
			if (idx < (localOrgsList.size() - 1)) {
				if (localOrgsList.get(idx).equals(localOrgsList.get(idx + 1))) {
					// Found a duplicate. Find it in the multiperson unit 
					// array and delete it.
					for (int mupIndex = 0; mupIndex < multiplePersonUnits.size(); mupIndex++) {
						if (multiplePersonUnits.get(mupIndex).equals(localOrgsList.get(idx))) {
							multiplePersonUnits.remove(mupIndex);
							multiplePersonUnitsCounts.remove(mupIndex);
							break;
						}
					}
				}
			}
		}
	}
	
	private String checkForTeamMembersAllUnique() {
		
		// Put all the members of the trash team in a set to see if
		// any are not unique
		Set<String> usernamesSet = new HashSet<String>();
		for (TrashTeam thisTrashTeam : trashTeams) {
			if (!usernamesSet.add(thisTrashTeam.getOrganizer().getUsername())) {
				return thisTrashTeam.getOrganizer().getUsername();
			}
			if (!usernamesSet.add(thisTrashTeam.getStrongPerson().getUsername())) {
				return thisTrashTeam.getStrongPerson().getUsername();
			}
			if (!usernamesSet.add(thisTrashTeam.getTeamMember1().getUsername())) {
				return thisTrashTeam.getTeamMember1().getUsername();
			}
			if (!usernamesSet.add(thisTrashTeam.getTeamMember2().getUsername())) {
				return thisTrashTeam.getTeamMember2().getUsername();
			}
		}
		return null;
	}
	
	private int incrementForNextTrashDay(Calendar currentDate) {
				
		// Advance by 6 or 7 depending on whether current date is Monday
		if (currentDate.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			return 6;
		} else {
			return 7;
		}
	}
	
	private Calendar adjustToHoliday(Calendar workingCal) {
		
		// Check if Date is Holiday. If so, increment day to show that
		// the trash is to be taken out on Monday.
		FederalHolidays holidaysClass = new FederalHolidays();
		if (holidaysClass.isDateAHoliday(workingCal.getTime()) ||
				holidaysClass.isNextDayAHoliday(workingCal.getTime())) {
			workingCal.add(Calendar.DAY_OF_YEAR, 1);
		} 
		
		return workingCal;
	}
	
	private int randomEntry(int listSize) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startingDate);
		return cal.get(Calendar.DAY_OF_YEAR) % listSize;
	}
}
