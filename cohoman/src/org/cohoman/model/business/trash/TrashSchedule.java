package org.cohoman.model.business.trash;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TrashSchedule {


	private List<TrashRow> trashRows;
	private List<TrashPerson> trashPersonListOrig;
	private List<TrashPerson> trashPersonList;
	private String startingUnit;
	private String nextPersonToSkip;

	public TrashSchedule(List<TrashPerson> trashPersonListOrig, String startingUnit, String nextPersonToSkip) {
		trashRows = new ArrayList<TrashRow>();
		trashPersonList = new ArrayList<TrashPerson>();
		this.trashPersonListOrig = trashPersonListOrig;
		this.startingUnit = startingUnit;
		this.nextPersonToSkip = nextPersonToSkip;
	}


	public List<TrashRow> getTrashRows() {
			
		SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");

		// Remove skipped people from list
		nextPersonToSkip = "gwenn";
		trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig, nextPersonToSkip);
		
		// Get all the printable rows, unformatted
		TrashCycle trashCycle = new TrashCycle(trashPersonList, startingUnit);			
		List<TrashTeam> trashTeams = trashCycle.getTrashTeams();
		
		
		// Remove skipped people from list
		trashPersonList.clear();
		nextPersonToSkip = "marianne";
		trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig, nextPersonToSkip);

		//startingUnit = incrementStartingUnit(startingUnit);
		TrashCycle trashCycle2 = new TrashCycle(trashPersonList, "102");
		List<TrashTeam> trashTeams2 = trashCycle2.getTrashTeams();
		trashTeams.addAll(trashTeams2);

		
		// Remove skipped people from list
		trashPersonList.clear();
		nextPersonToSkip = "katier";
		trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig, nextPersonToSkip);

		//startingUnit = incrementStartingUnit(startingUnit);
		TrashCycle trashCycle3 = new TrashCycle(trashPersonList, "103");
		List<TrashTeam> trashTeams3 = trashCycle3.getTrashTeams();
		trashTeams.addAll(trashTeams3);

		
		// Remove skipped people from list
		trashPersonList.clear();
		nextPersonToSkip = "ben";
		trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig, nextPersonToSkip);

		//startingUnit = incrementStartingUnit(startingUnit);
		TrashCycle trashCycle4 = new TrashCycle(trashPersonList, "104");
		List<TrashTeam> trashTeams4 = trashCycle4.getTrashTeams();
		trashTeams.addAll(trashTeams4);

		// Remove skipped people from list
		trashPersonList.clear();
		nextPersonToSkip = "kim";
		trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig, nextPersonToSkip);

		//startingUnit = incrementStartingUnit(startingUnit);
		TrashCycle trashCycle5 = new TrashCycle(trashPersonList, "104");
		List<TrashTeam> trashTeams5 = trashCycle5.getTrashTeams();
		trashTeams.addAll(trashTeams5);

		// Remove skipped people from list
		trashPersonList.clear();
		nextPersonToSkip = "jim";
		trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig, nextPersonToSkip);

		//startingUnit = incrementStartingUnit(startingUnit);
		TrashCycle trashCycle6 = new TrashCycle(trashPersonList, "104");
		List<TrashTeam> trashTeams6 = trashCycle6.getTrashTeams();
		trashTeams.addAll(trashTeams6);

		// Remove skipped people from list
		trashPersonList.clear();
		nextPersonToSkip = "joan";
		trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig, nextPersonToSkip);

		//startingUnit = incrementStartingUnit(startingUnit);
		TrashCycle trashCycle7 = new TrashCycle(trashPersonList, "104");
		List<TrashTeam> trashTeams7 = trashCycle7.getTrashTeams();
		trashTeams.addAll(trashTeams7);
	
		
		
		// Now make a list of printable rows
		for (TrashTeam oneTeam : trashTeams) {
			TrashRow trashRow = new TrashRow();
			trashRow.setSundayDate(formatter.format(oneTeam.getSundayDate().getTime()));
			if (oneTeam.getOrganizer() == null) {
				trashRow.setOrganizer("");
			} else {
				trashRow.setOrganizer(oneTeam.getOrganizer().getUnitnumber()
						+ " " + oneTeam.getOrganizer().getUsername());
			}
			if (oneTeam.getStrongPerson() == null) {
				trashRow.setStrongPerson("");
			} else {
				trashRow.setStrongPerson(oneTeam.getStrongPerson().getUnitnumber()
						+ " " + oneTeam.getStrongPerson().getUsername());
			}
			if (oneTeam.getTeamMember1() == null) {
				trashRow.setTeamMember1("");
			} else {
				trashRow.setTeamMember1(oneTeam.getTeamMember1().getUnitnumber()
					+ " " + oneTeam.getTeamMember1().getUsername());
			}
			if (oneTeam.getTeamMember2() == null) {
				trashRow.setTeamMember2("");
			} else {
				trashRow.setTeamMember2(oneTeam.getTeamMember2().getUnitnumber()
					+ " " + oneTeam.getTeamMember2().getUsername());
			}
			trashRows.add(trashRow);
		}
		return trashRows;
	}

	public void addTrashRow(TrashRow trashRow) {
		trashRows.add(trashRow);
	}
	

	private List<TrashPerson> removeSkippedPeopleFromList(List<TrashPerson> trashPersonListOrig, String nextPersonToSkip) {
		
		// Determine if extra people that must be skipped each cycle based on
		// mod of 4 people per team.
		int peopleToSkip = trashPersonListOrig.size() % 4;
		
		// Copy TrashPerson list to local copy removing any people to skip.
		boolean skippingMode = false;
		for (TrashPerson trashPerson : trashPersonListOrig) {
			if (skippingMode) {
				if (peopleToSkip != 0) {
					peopleToSkip--;
					continue;
				} else {
					skippingMode = false;
				}
			}
			if (peopleToSkip != 0) {
				if (trashPerson.getUsername().equalsIgnoreCase(nextPersonToSkip)) {
					peopleToSkip--;
					skippingMode = true;
				} else {
					trashPersonList.add(trashPerson);					
				}
			} else {
				trashPersonList.add(trashPerson);
			}
		}
		return trashPersonList;
	}
}
