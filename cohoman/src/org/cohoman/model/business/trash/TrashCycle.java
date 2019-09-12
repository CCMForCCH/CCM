package org.cohoman.model.business.trash;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.cohoman.view.controller.utils.CalendarUtils;

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
	private Date startingDate;

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

	public List<TrashTeam> getTrashTeams() {

		// temp
		trashPersonList = new ArrayList<TrashPerson>();
		
		// Find the index of the first startingUnit in the sorted
		// TrashPerson "original" list.
		for (int idx = 0; idx < trashPersonListOrig.size(); idx++) {
			trashPersonList.add(trashPersonListOrig.get(idx));
		}

		//cloneAndAdjustTrashPersonList(startingUnit);
		
		// Compute number of teams in one cycle
		int teamsInOneCycle = trashPersonList.size();
		teamsInOneCycle = teamsInOneCycle / 4;

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
		Calendar workingDate = Calendar.getInstance();
		workingDate.setTime(startingDate);
		for (int teamIndex = 0; teamIndex < teamsInOneCycle; teamIndex++) {
			TrashTeam trashTeam = new TrashTeam();
			trashTeam.setSundayDate(workingDate.getTime());
			trashTeam.setPrintableDate(formatter.format(workingDate.getTime()));
			trashTeams.add(trashTeam);
			//workingDate.add(Calendar.DAY_OF_YEAR, 7); // advance to next date
			workingDate.add(Calendar.DAY_OF_YEAR, 1); // advance to next date
		}

		// First rule: add organizers with others in same household to same team

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
							&& trashPerson.getTrashRole() != TrashRolesEnums.ORGANIZER
									.name()) {

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

		int randomizedIndex; 
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

		// RANDOMLY, use Team members to fill any Organizer spots left
		if (!trashTeamMembers.isEmpty()) {
			for (TrashTeam thisTrashTeam : trashTeams) {
				if (thisTrashTeam.getOrganizer() == null) {
					// Get the next team member
					if (!trashTeamMembers.isEmpty()) {
						randomizedIndex = randomEntry(trashTeamMembers.size());
						thisTrashTeam.setOrganizer(trashTeamMembers.get(randomizedIndex));
						trashTeamMembers.remove(randomizedIndex);
					} else {
						break; // give up when list is depleted
					}
				}
			}
		}

		// RANDOMLY, use Strong members to fill any Organizer spots if any left
		if (!trashStrongPersons.isEmpty()) {
			for (TrashTeam thisTrashTeam : trashTeams) {
				if (thisTrashTeam.getOrganizer() == null) {
					// Get the next strong person
					if (!trashStrongPersons.isEmpty()) {
						randomizedIndex = randomEntry(trashStrongPersons.size());
						thisTrashTeam.setOrganizer(trashStrongPersons.get(randomizedIndex));
						trashStrongPersons.remove(randomizedIndex);
					} else {
						break; // give up when list is depleted
					}
				}
			}
		}

		// Next RANDOMLY, use strong people as team members, if any left
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
				// No.
				if (currentCount > 1) {
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

			// Try an organizer first to lessen the
			// likelihood that a subsequent strong person
			// will end up as the organizer.
			if (trashTeam.getOrganizer() == null) {
				trashTeam.setOrganizer(trashPerson);
				trashTeamMembers.remove(trashPerson);
				return true;
			}

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
			
			
			
		}

		return false;

	}

	/*
	private void cloneAndAdjustTrashPersonList(String startingUnit) {
		
		// Start by making a new TrashPerson list local to this class.
		trashPersonList = new ArrayList<TrashPerson>();
		
		// Find the index of the first startingUnit in the sorted
		// TrashPerson "original" list.
		int startingUnitIndex = -1;
		for (int idx = 0; idx < trashPersonListOrig.size(); idx++) {
			if (trashPersonListOrig.get(idx).getUnitnumber().equals(startingUnit)) {
				startingUnitIndex = idx;
				break;
			}
		}
		if (startingUnitIndex == -1) {
			logger.severe("Unit is not found in the TrashPerson list: " + startingUnit);

		}
		
		// Now add the TrashPersons from the starting unit to the end, to
		// the adjusted TrashPerson list.
		for (int idx = startingUnitIndex; idx < trashPersonListOrig.size(); idx++) {
			trashPersonList.add(trashPersonListOrig.get(idx));
		}
		
		// Lastly, add the TrashPersons from 0 to the starting unit, to the
		// adjusted TrashPerson list.
		for (int idx = 0; idx < startingUnitIndex; idx++) {
			trashPersonList.add(trashPersonListOrig.get(idx));
		}
	}
*/
	
	private int randomEntry(int listSize) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startingDate);
		return cal.get(Calendar.DAY_OF_YEAR) % listSize;
	}
}
