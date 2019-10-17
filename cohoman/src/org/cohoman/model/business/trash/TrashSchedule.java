package org.cohoman.model.business.trash;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.cohoman.model.integration.persistence.beans.TrashCycleBean;
import org.cohoman.model.integration.persistence.beans.TrashCycleRow;
import org.cohoman.model.integration.persistence.beans.TrashSubstitutesBean;
import org.cohoman.model.integration.utils.LoggingUtils;

public class TrashSchedule {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private List<TrashRow> trashRows;
	private List<TrashPerson> trashPersonListOrig;
	private List<TrashPerson> trashPersonList;
	//private List<TrashCycleRow> trashCycleDBRows;
	private List<TrashCycleBean> trashCycleBeans;
	private List<TrashSubstitutesBean> trashSubstitutions;
	private String newNextUseridToSkip;
	private Date newCycleStartingDate;

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

/*
	public List<TrashTeam> getTrashTeamsOrig(int numberOfCycles) {
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
		this.newCycleStartingDate = trashCycleBeans.get(0)
				.getTrashcyclestartdate();
		this.newNextUseridToSkip = trashCycleBeans.get(0)
				.getNextusertoskip();

		for (int cycleIdx = 0; cycleIdx < numberOfCycles; cycleIdx++) {
			// for (int cycleIdx = 0; cycleIdx < 36; cycleIdx++) {

			Date localCycleStartingDate;
			String localNextUseridToSkip;

			if (trashCycleBeans.size() > cycleIdx) {
				// If there's an entry in the table
				localCycleStartingDate = trashCycleBeans.get(cycleIdx)
						.getTrashcyclestartdate();
				localNextUseridToSkip = trashCycleBeans.get(cycleIdx)
						.getNextusertoskip();
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

			if (cycleIdx >= trashCycleBeans.size() - 1 && cycleIdx < 3) {
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
		//trashCycleDBRows.addAll(trashCycleRowsToAdd);

		return trashTeams;

	}
*/
	
	

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

			// Before finishing up with teams, add any substitutions
/*
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
*/

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

		// Compute number of teams in one cycle
		int teamsInOneCycle = trashPersonListOrig.size();
		teamsInOneCycle = teamsInOneCycle / 4;
		int daysInCycle = teamsInOneCycle * 1;
		// int daysInCycle = teamsInOneCycle * 1;

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

			// Before finishing up with teams, add any substitutions
			for (TrashTeam oneTeam : trashTeams) {
				if (trashSubstitutions != null) {
					for (TrashSubstitutesBean oneSubBean : trashSubstitutions) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						if (sdf.format(oneTeam.getSundayDate()).equals(
								sdf.format(oneSubBean.getStartingdate()))) {
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
		teamsInOneCycle = teamsInOneCycle / 4;
		int daysInCycle = teamsInOneCycle * 1;
		// int daysInCycle = teamsInOneCycle * 1;


		// Set these for first time thru; assume there is at least one entry in
		// the table
		Date localCycleStartingDate = trashCycleBeanIn.getTrashcyclestartdate();
		String localNextUseridToSkip = trashCycleBeanIn.getNextusertoskip();

			// Start by creating TrashPerson list with skipped people removed.
			// (Note: this method sets the nextUseridToSkip value for the next
			// cycle.)
			trashPersonList = removeSkippedPeopleFromList(trashPersonListOrig,
					localNextUseridToSkip);

			// Check if there are already other defined rows and that they still
			// match. If not, create another trashCycleRow.
			// Start by computing the startDate for the next cycle.
			Calendar calCycleStartingDate = Calendar.getInstance();
			calCycleStartingDate.setTime(localCycleStartingDate);
			calCycleStartingDate.add(Calendar.DAY_OF_YEAR, daysInCycle);
			Date newCycleStartingDate = calCycleStartingDate.getTime();

			TrashCycleBean trashCycleBeanOut = new TrashCycleBean();
			trashCycleBeanOut.setTrashcyclestartdate(newCycleStartingDate);
			trashCycleBeanOut.setTrashcycleenddate(newCycleStartingDate);
			trashCycleBeanOut.setNextusertoskip(this.newNextUseridToSkip);

		return trashCycleBeanOut;

	}

	
	public List<TrashRow> getTrashRows(int numberOfCycles) {

		SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
		Calendar calDateNow = Calendar.getInstance();
		int rowsRemovedFromFirstCycle = 0;

		// Now make a list of printable rows
		for (TrashTeam oneTeam : getTrashTeams(numberOfCycles)) {

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
				String organizerUsername = oneTeam.getOrganizer().getUsername();
				if (organizerUsername.equalsIgnoreCase(LoggingUtils.getCurrentUsername())) {
					organizerUsername = organizerUsername.toUpperCase();
				}
				if (oneTeam.getOrganizerSub() == null) {
					trashRow.setOrganizer(organizerUsername);
				} else {
					String organizerSub = oneTeam.getOrganizerSub();
					if (organizerSub.equalsIgnoreCase(LoggingUtils.getCurrentUsername())) {
						organizerSub = organizerSub.toUpperCase();
					}
					trashRow.setOrganizer(organizerSub + " for "
							+ organizerUsername);
				}
			}
			if (oneTeam.getStrongPerson() == null) {
				trashRow.setStrongPerson("");
			} else {
				String strongUsername = oneTeam.getStrongPerson().getUsername();
				if (strongUsername.equalsIgnoreCase(LoggingUtils.getCurrentUsername())) {
					strongUsername = strongUsername.toUpperCase();
				}
				if (oneTeam.getStrongPersonSub() == null) {
					trashRow.setStrongPerson(strongUsername);
				} else {
					String strongSub = oneTeam.getStrongPersonSub();
					if (strongSub.equalsIgnoreCase(LoggingUtils.getCurrentUsername())) {
						strongSub = strongSub.toUpperCase();
					}
					trashRow.setStrongPerson(strongSub
							+ " for " + strongUsername);

				}
			}
			if (oneTeam.getTeamMember1() == null) {
				trashRow.setTeamMember1("");
			} else {
				String member1Username = oneTeam.getTeamMember1().getUsername();
				if (member1Username.equalsIgnoreCase(LoggingUtils.getCurrentUsername())) {
					member1Username = member1Username.toUpperCase();
				}
				if (oneTeam.getTeamMember1Sub() == null) {
					trashRow.setTeamMember1(member1Username);
				} else {
					String member1Sub = oneTeam.getTeamMember1Sub();
					if (member1Sub.equalsIgnoreCase(LoggingUtils.getCurrentUsername())) {
						member1Sub = member1Sub.toUpperCase();
					}
					trashRow.setTeamMember1(member1Sub
							+ " for " + member1Username);
				}
			}
			
			if (oneTeam.getTeamMember2() == null) {
				trashRow.setTeamMember2("");
			} else {
				String member2Username = oneTeam.getTeamMember2().getUsername();
				if (member2Username.equalsIgnoreCase(LoggingUtils.getCurrentUsername())) {
					member2Username = member2Username.toUpperCase();
				}
				if (oneTeam.getTeamMember2Sub() == null) {
					trashRow.setTeamMember2(member2Username);
				} else {
					String member2Sub = oneTeam.getTeamMember2Sub();
					if (member2Sub.equalsIgnoreCase(LoggingUtils.getCurrentUsername())) {
						member2Sub = member2Sub.toUpperCase();
					}
					trashRow.setTeamMember2(member2Sub
							+ " for " + member2Username);
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
				//if (organizerUsername.equalsIgnoreCase(LoggingUtils.getCurrentUsername())) {
					//organizerUsername = organizerUsername.toUpperCase();
				//}
				if (oneTeam.getOrganizerSub() == null) {
					trashRow.setOrganizer(organizerUsername);
				} else {
					//String organizerSub = oneTeam.getOrganizerSub();
					//if (organizerSub.equalsIgnoreCase(LoggingUtils.getCurrentUsername())) {
						//organizerSub = organizerSub.toUpperCase();
					//}
					//trashRow.setOrganizer(organizerSub + " for "
							//+ organizerUsername);
				}
			}
			if (oneTeam.getStrongPerson() == null) {
				trashRow.setStrongPerson("");
			} else {
				String strongUsername = oneTeam.getStrongPerson().getUsername();
				//if (strongUsername.equalsIgnoreCase(LoggingUtils.getCurrentUsername())) {
					//strongUsername = strongUsername.toUpperCase();
				//}
				if (oneTeam.getStrongPersonSub() == null) {
					trashRow.setStrongPerson(strongUsername);
				} else {
					//String strongSub = oneTeam.getStrongPersonSub();
					//if (strongSub.equalsIgnoreCase(LoggingUtils.getCurrentUsername())) {
						//strongSub = strongSub.toUpperCase();
					//}
					//trashRow.setStrongPerson(strongSub
							//+ " for " + strongUsername);

				}
			}
			if (oneTeam.getTeamMember1() == null) {
				trashRow.setTeamMember1("");
			} else {
				String member1Username = oneTeam.getTeamMember1().getUsername();
				//if (member1Username.equalsIgnoreCase(LoggingUtils.getCurrentUsername())) {
					//member1Username = member1Username.toUpperCase();
				//}
				if (oneTeam.getTeamMember1Sub() == null) {
					trashRow.setTeamMember1(member1Username);
				} else {
					//String member1Sub = oneTeam.getTeamMember1Sub();
					//if (member1Sub.equalsIgnoreCase(LoggingUtils.getCurrentUsername())) {
						//member1Sub = member1Sub.toUpperCase();
					//}
					//trashRow.setTeamMember1(member1Sub
							//+ " for " + member1Username);
				}
			}
			
			if (oneTeam.getTeamMember2() == null) {
				trashRow.setTeamMember2("");
			} else {
				String member2Username = oneTeam.getTeamMember2().getUsername();
				//if (member2Username.equalsIgnoreCase(LoggingUtils.getCurrentUsername())) {
					//member2Username = member2Username.toUpperCase();
				//}
				if (oneTeam.getTeamMember2Sub() == null) {
					trashRow.setTeamMember2(member2Username);
				} else {
					//String member2Sub = oneTeam.getTeamMember2Sub();
					//if (member2Sub.equalsIgnoreCase(LoggingUtils.getCurrentUsername())) {
						//member2Sub = member2Sub.toUpperCase();
					//}
					//trashRow.setTeamMember2(member2Sub
							//+ " for " + member2Username);
				}
			}
			trashRows.add(trashRow);
		}

		return trashRows;
	}

	
	
	public void addTrashRow(TrashRow trashRow) {
		trashRows.add(trashRow);
	}

/*
	public List<TrashCycleRow> getTrashCycleDBRows() {
		return trashCycleDBRows;
	}
*/
	
	private List<TrashPerson> removeSkippedPeopleFromList(
			List<TrashPerson> trashPersonListOrig, String nextPersonToSkip) {

		// Probably make this local not global ????
		trashPersonList.clear();
		
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
