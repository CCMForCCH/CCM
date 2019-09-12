package org.cohoman.model.business.trash;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.cohoman.model.integration.persistence.beans.TrashCycleRow;
import org.cohoman.model.integration.persistence.beans.TrashSubstitutesBean;

public class TrashSchedule {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private List<TrashRow> trashRows;
	private List<TrashPerson> trashPersonListOrig;
	private List<TrashPerson> trashPersonList;
	private List<TrashCycleRow> trashCycleDBRows;
	private List<TrashSubstitutesBean> trashSubstitutions;
	private String newNextUseridToSkip;
	private Date newCycleStartingDate;

	public TrashSchedule(List<TrashPerson> trashPersonListOrig,
			List<TrashCycleRow> trashCycleDBRows,
			List<TrashSubstitutesBean> trashSubstitutions) {
		trashRows = new ArrayList<TrashRow>();
		trashPersonList = new ArrayList<TrashPerson>();
		this.trashPersonListOrig = trashPersonListOrig;
		this.trashCycleDBRows = trashCycleDBRows;
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

		// Compute number of teams in one cycle
		int teamsInOneCycle = trashPersonListOrig.size();
		teamsInOneCycle = teamsInOneCycle / 4;
		int daysInCycle = teamsInOneCycle * 1;
		// int daysInCycle = teamsInOneCycle * 1;

		// Loop through the desired number of cycles.
		List<TrashTeam> trashTeams = null;
		List<TrashCycleRow> trashCycleRowsToAdd = new ArrayList<TrashCycleRow>();

		// Set these for first time thru; assume there is at least one entry in
		// the table
		this.newCycleStartingDate = trashCycleDBRows.get(0)
				.getTrashcyclestartdate();
		this.newNextUseridToSkip = trashCycleDBRows.get(0)
				.getNextuseridtoskip();

		for (int cycleIdx = 0; cycleIdx < numberOfCycles; cycleIdx++) {
			// for (int cycleIdx = 0; cycleIdx < 36; cycleIdx++) {

			Date localCycleStartingDate;
			String localNextUseridToSkip;

			if (trashCycleDBRows.size() > cycleIdx) {
				// If there's an entry in the table
				localCycleStartingDate = trashCycleDBRows.get(cycleIdx)
						.getTrashcyclestartdate();
				localNextUseridToSkip = trashCycleDBRows.get(cycleIdx)
						.getNextuseridtoskip();
			} else {
				// No entry in the table; use just previously computed values
				localCycleStartingDate = this.newCycleStartingDate;
				localNextUseridToSkip = this.newNextUseridToSkip;
			}
			// Start by creating TrashPerson list with skipped people removed.
			// (Note: this method sets the nextUseridToSkip value for the next
			// cycle.)
			trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig,
					localNextUseridToSkip);

			// Get all the printable rows, unformatted
			TrashCycle trashCycle = new TrashCycle(trashPersonList,
					localCycleStartingDate);

			if (trashTeams == null) {
				trashTeams = trashCycle.getTrashTeams();
			} else {
				trashTeams.addAll(trashCycle.getTrashTeams());
			}

			// Before finishing up with teams, add any substitutions
			for (TrashTeam oneTeam : trashTeams) {
				if (trashSubstitutions != null) {
					for (TrashSubstitutesBean oneSubBean : trashSubstitutions) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						if (sdf.format(oneTeam.getSundayDate()).equals(
								sdf.format(oneSubBean.getStartingdate()))) {
							// if (oneTeam.getSundayDate().getTime() ==
							// oneSubBean.getStartingdate().getTime()) {
							// dates match; place in correct field
							if (oneTeam.getOrganizer().getUsername()
									.equals(oneSubBean.getOrigusername())) {
								// subbing for organizer
								oneTeam.setOrganizerSub(oneSubBean
										.getSubstituteusername());
							}
							if (oneTeam.getStrongPerson().getUsername()
									.equals(oneSubBean.getOrigusername())) {
								// subbing for strong person
								oneTeam.setStrongPersonSub(oneSubBean
										.getSubstituteusername());
							}
							if (oneTeam.getTeamMember1().getUsername()
									.equals(oneSubBean.getOrigusername())) {
								// subbing for member 1
								oneTeam.setTeamMember1Sub(oneSubBean
										.getSubstituteusername());
							}
							if (oneTeam.getTeamMember2().getUsername()
									.equals(oneSubBean.getOrigusername())) {
								// subbing for member 2
								oneTeam.setTeamMember2Sub(oneSubBean
										.getSubstituteusername());
							}
						}
					}
				}
			}

			// Remove skipped people from list
			trashPersonList.clear();

			// Check if there are already other defined rows and that they still
			// match. If not, create another trashCycleRow.
			// Start by computing the startDate for the next cycle.
			Calendar calCycleStartingDate = Calendar.getInstance();
			calCycleStartingDate.setTime(localCycleStartingDate);
			calCycleStartingDate.add(Calendar.DAY_OF_YEAR, daysInCycle);
			this.newCycleStartingDate = calCycleStartingDate.getTime();

			if (cycleIdx >= trashCycleDBRows.size() - 1 && cycleIdx < 3) {
				// if (cycleIdx >= trashCycleDBRows.size() - 1 && cycleIdx < 35)
				// {
				// No more rows. Add the next one to the "to-add" table
				TrashCycleRow trashCycleRow = new TrashCycleRow();
				trashCycleRow.setNextuseridtoskip(this.newNextUseridToSkip);
				trashCycleRow.setTrashcyclestartdate(this.newCycleStartingDate);
				trashCycleRowsToAdd.add(trashCycleRow);
			}

		}

		// Add any rows that might have been added.
		trashCycleDBRows.addAll(trashCycleRowsToAdd);

		return trashTeams;

	}

	public List<TrashRow> getTrashRows() {

		SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
		Calendar calDateNow = Calendar.getInstance();
		int rowsRemovedFromFirstCycle = 0;

		// Now make a list of printable rows
		for (TrashTeam oneTeam : getTrashTeams(4)) {

			// skip over dates that have already past.
			Calendar calTeamDate = Calendar.getInstance();
			calTeamDate.setTime(oneTeam.getSundayDate());
			// If current year exceeds team year, skip it
			if (calDateNow.get(Calendar.YEAR) > calTeamDate.get(Calendar.YEAR)) {
				rowsRemovedFromFirstCycle++;
				continue;
			}
			// If the same year but the team day is in the past, skip it
			if (calTeamDate.get(Calendar.YEAR) == calDateNow.get(Calendar.YEAR)
					&& calDateNow.get(Calendar.DAY_OF_YEAR) > calTeamDate
							.get(Calendar.DAY_OF_YEAR)) {
				rowsRemovedFromFirstCycle++;
				continue;
			}

			// Make each row for printing
			TrashRow trashRow = new TrashRow();
			trashRow.setSundayDate(formatter.format(oneTeam.getSundayDate()
					.getTime()));
			if (oneTeam.getOrganizer() == null) {
				trashRow.setOrganizer("");
			} else {
				if (oneTeam.getOrganizerSub() == null) {
					trashRow.setOrganizer(oneTeam.getOrganizer().getUsername());
				} else {
					trashRow.setOrganizer(oneTeam.getOrganizerSub() + " for "
							+ oneTeam.getOrganizer().getUsername());
				}
			}
			if (oneTeam.getStrongPerson() == null) {
				trashRow.setStrongPerson("");
			} else {
				if (oneTeam.getStrongPersonSub() == null) {
					trashRow.setStrongPerson(oneTeam.getStrongPerson()
							.getUsername());
				} else {
					trashRow.setStrongPerson(oneTeam.getStrongPersonSub()
							+ " for " + oneTeam.getStrongPerson().getUsername());

				}
			}
			if (oneTeam.getTeamMember1() == null) {
				trashRow.setTeamMember1("");
			} else {
				if (oneTeam.getTeamMember1Sub() == null) {
					trashRow.setTeamMember1(oneTeam.getTeamMember1()
							.getUsername());
				} else {
					trashRow.setTeamMember1(oneTeam.getTeamMember1Sub()
							+ " for " + oneTeam.getTeamMember1().getUsername());
				}
			}
			if (oneTeam.getTeamMember2() == null) {
				trashRow.setTeamMember2("");
			} else {
				if (oneTeam.getTeamMember2Sub() == null) {
					trashRow.setTeamMember2(oneTeam.getTeamMember2()
							.getUsername());
				} else {
					trashRow.setTeamMember2(oneTeam.getTeamMember2Sub()
							+ " for " + oneTeam.getTeamMember2().getUsername());
				}
			}
			trashRows.add(trashRow);
		}

		// Compute number of teams in one cycle
		int teamsInOneCycle = trashPersonListOrig.size();
		teamsInOneCycle = teamsInOneCycle / 4;

		// OK, now remove entries from the last cycle based on those removed
		// from
		// the first cycle. It will be the inverse.
		int teamsToRemoveFromEnd = teamsInOneCycle - rowsRemovedFromFirstCycle;
		for (int idx = 1; idx <= teamsToRemoveFromEnd; idx++) {
			trashRows.remove(trashRows.size() - 1);
		}
		return trashRows;
	}

	public void addTrashRow(TrashRow trashRow) {
		trashRows.add(trashRow);
	}

	public List<TrashCycleRow> getTrashCycleDBRows() {
		return trashCycleDBRows;
	}

	private List<TrashPerson> removeSkippedPeopleFromList(
			List<TrashPerson> trashPersonListOrig, String nextPersonToSkip) {

		// Determine if extra people must be skipped for each cycle based on
		// mod of 4 people per team.
		int peopleToSkip = trashPersonListOrig.size() % 4;

		// Copy TrashPerson list to local copy removing any people to skip.
		boolean skippingMode = false;
		// for (TrashPerson trashPerson : trashPersonListOrig) {
		for (int tpIdx = 0; tpIdx < trashPersonListOrig.size(); tpIdx++) {
			if (skippingMode) {
				if (peopleToSkip != 0) {
					peopleToSkip--;
					continue;
				} else {
					skippingMode = false;
					this.newNextUseridToSkip = trashPersonListOrig.get(tpIdx)
							.getUsername();
				}
			}
			if (peopleToSkip != 0) {
				if (trashPersonListOrig.get(tpIdx).getUsername()
						.equalsIgnoreCase(nextPersonToSkip)) {
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
}
