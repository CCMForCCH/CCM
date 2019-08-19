package org.cohoman.model.business.trash;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.cohoman.view.controller.utils.CalendarUtils;

public class TrashSchedule {


	private List<TrashRow> trashRows;
	private List<TrashPerson> trashPersonListOrig;
	private List<TrashPerson> trashPersonList;
	private String nextPersonToSkip;

	public TrashSchedule(List<TrashPerson> trashPersonListOrig, String nextPersonToSkip) {
		trashRows = new ArrayList<TrashRow>();
		trashPersonList = new ArrayList<TrashPerson>();
		this.trashPersonListOrig = trashPersonListOrig;
		//this.nextPersonToSkip = nextPersonToSkip;
		this.nextPersonToSkip = trashPersonListOrig.get(0).getUsername(); //temp for now
	}


	public List<TrashRow> getTrashRows() {
			
		SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
		Calendar calToIncrement = Calendar.getInstance();
		calToIncrement = CalendarUtils.adjustToStartingSunday(calToIncrement);

		// Compute number of teams in one cycle
		int teamsInOneCycle = trashPersonListOrig.size();
		teamsInOneCycle = teamsInOneCycle / 4;
		int daysInCycle = teamsInOneCycle * 7;
		

		// Loop through the desired number of cycles.
		List<TrashTeam> trashTeams = null;
		for (int cycleIdx = 0; cycleIdx < 12; cycleIdx++) {
			
			// Start by creating TrashPerson list with skipped people removed.
			trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig, nextPersonToSkip);
		
			// Get all the printable rows, unformatted
			TrashCycle trashCycle = new TrashCycle(trashPersonList, calToIncrement.getTime());
			
			if (trashTeams == null) {
				trashTeams = trashCycle.getTrashTeams();
			} else {
				trashTeams.addAll(trashCycle.getTrashTeams());
			}
			
			// Remove skipped people from list
			trashPersonList.clear();
			
			calToIncrement.add(Calendar.DAY_OF_YEAR, daysInCycle);


		}
/*
		// Remove skipped people from list
		nextPersonToSkip = "gwenn";
		trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig, nextPersonToSkip);
		
		// Get all the printable rows, unformatted
		TrashCycle trashCycle = new TrashCycle(trashPersonList, startingCycleDate);			
		List<TrashTeam> trashTeams = trashCycle.getTrashTeams();
		
		
		// Remove skipped people from list
		trashPersonList.clear();
		nextPersonToSkip = "marianne";
		trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig, nextPersonToSkip);

		//startingUnit = incrementStartingUnit(startingUnit);
		calToIncrement.add(Calendar.DAY_OF_YEAR, daysInCycle);
		TrashCycle trashCycle2 = new TrashCycle(trashPersonList, calToIncrement.getTime());
		List<TrashTeam> trashTeams2 = trashCycle2.getTrashTeams();
		trashTeams.addAll(trashTeams2);

		
		// Remove skipped people from list
		trashPersonList.clear();
		nextPersonToSkip = "katier";
		trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig, nextPersonToSkip);

		//startingUnit = incrementStartingUnit(startingUnit);
		calToIncrement.add(Calendar.DAY_OF_YEAR, daysInCycle);
		TrashCycle trashCycle3 = new TrashCycle(trashPersonList, calToIncrement.getTime());
		List<TrashTeam> trashTeams3 = trashCycle3.getTrashTeams();
		trashTeams.addAll(trashTeams3);

		
		// Remove skipped people from list
		trashPersonList.clear();
		nextPersonToSkip = "ben";
		trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig, nextPersonToSkip);

		//startingUnit = incrementStartingUnit(startingUnit);
		calToIncrement.add(Calendar.DAY_OF_YEAR, daysInCycle);
		TrashCycle trashCycle4 = new TrashCycle(trashPersonList, calToIncrement.getTime());
		List<TrashTeam> trashTeams4 = trashCycle4.getTrashTeams();
		trashTeams.addAll(trashTeams4);

		// Remove skipped people from list
		trashPersonList.clear();
		nextPersonToSkip = "kim";
		trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig, nextPersonToSkip);

		//startingUnit = incrementStartingUnit(startingUnit);
		calToIncrement.add(Calendar.DAY_OF_YEAR, daysInCycle);
		TrashCycle trashCycle5 = new TrashCycle(trashPersonList, calToIncrement.getTime());
		List<TrashTeam> trashTeams5 = trashCycle5.getTrashTeams();
		trashTeams.addAll(trashTeams5);

		// Remove skipped people from list
		trashPersonList.clear();
		nextPersonToSkip = "jim";
		trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig, nextPersonToSkip);

		//startingUnit = incrementStartingUnit(startingUnit);
		calToIncrement.add(Calendar.DAY_OF_YEAR, daysInCycle);
		TrashCycle trashCycle6 = new TrashCycle(trashPersonList, calToIncrement.getTime());
		List<TrashTeam> trashTeams6 = trashCycle6.getTrashTeams();
		trashTeams.addAll(trashTeams6);

		// Remove skipped people from list
		trashPersonList.clear();
		nextPersonToSkip = "joan";
		trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig, nextPersonToSkip);

		//startingUnit = incrementStartingUnit(startingUnit);
		calToIncrement.add(Calendar.DAY_OF_YEAR, daysInCycle);
		TrashCycle trashCycle7 = new TrashCycle(trashPersonList, calToIncrement.getTime());
		List<TrashTeam> trashTeams7 = trashCycle7.getTrashTeams();
		trashTeams.addAll(trashTeams7);
*/			
		
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
		
		// Determine if extra people must be skipped for each cycle based on
		// mod of 4 people per team.
		int peopleToSkip = trashPersonListOrig.size() % 4;
		
		// Copy TrashPerson list to local copy removing any people to skip.
		boolean skippingMode = false;
		//for (TrashPerson trashPerson : trashPersonListOrig) {
		for (int tpIdx = 0; tpIdx < trashPersonListOrig.size(); tpIdx++) {
			if (skippingMode) {
				if (peopleToSkip != 0) {
					peopleToSkip--;
					continue;
				} else {
					skippingMode = false;
					this.nextPersonToSkip = trashPersonListOrig.get(tpIdx).getUsername();
				}
			}
			if (peopleToSkip != 0) {
				if (trashPersonListOrig.get(tpIdx).getUsername().equalsIgnoreCase(nextPersonToSkip)) {
					peopleToSkip--;
					skippingMode = true;
				} else {
					trashPersonList.add(trashPersonListOrig.get(tpIdx));					
				}
			} else {
				trashPersonList.add(trashPersonListOrig.get(tpIdx));
			}
		}
		
		// If finished loop without having skipped all the entries in the
		// first pass, must start the list again since the entries to skip
		// have wrapped around to the beginning of the list.
		if (skippingMode) {
			//for (TrashPerson trashPerson : trashPersonListOrig) {
			for (int tpIdx = 0; tpIdx < trashPersonListOrig.size(); tpIdx++) {
				if (skippingMode) {
					if (peopleToSkip != 0) {
						peopleToSkip--;
						continue;
					} else {
						skippingMode = false;
						this.nextPersonToSkip = trashPersonListOrig.get(tpIdx).getUsername();
					}
				}
				if (peopleToSkip != 0) {
					if (trashPersonListOrig.get(tpIdx).getUsername().equalsIgnoreCase(nextPersonToSkip)) {
						peopleToSkip--;
						skippingMode = true;
					} else {
						trashPersonList.add(trashPersonListOrig.get(tpIdx));					
					}
				} else {
					trashPersonList.add(trashPersonListOrig.get(tpIdx));
				}
			}
		}
		
		return trashPersonList;
	}
}
