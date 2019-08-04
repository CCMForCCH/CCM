package org.cohoman.model.business.trash;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cohoman.view.controller.utils.CalendarUtils;

public class TrashCycle {

	private List<TrashTeam> trashTeams;
	private List<TrashPerson> trashPersonList;
	
	public TrashCycle(List<TrashPerson> trashPersonList) {
		trashTeams = new ArrayList<TrashTeam>();
		this.trashPersonList = trashPersonList;
	}

	//public void addTrashTeam(TrashTeam trashTeam) {
	//	trashTeams.add(trashTeam);
	//}
	
	public List<TrashTeam> getTrashTeams() {
		
		// Compute number of teams in one cycle
		int teamsInOneCycle = trashPersonList.size();
		teamsInOneCycle = teamsInOneCycle / 4;
		
		// Walk through the TrashPerson list making teams
		int personIndex = 0;
		Calendar workingDate = Calendar.getInstance();

		for (int teamIndex = 0; teamIndex < teamsInOneCycle; teamIndex++) {
			TrashTeam trashTeam = new TrashTeam();
			CalendarUtils.adjustToStartingSunday(workingDate);

			trashTeam.setSundayDate(workingDate.getTime());
			trashTeam.setCaller(trashPersonList.get(personIndex++));
			trashTeam.setStrongPerson(trashPersonList.get(personIndex++));
			trashTeam.setWorker1(trashPersonList.get(personIndex++));
			trashTeam.setWorker2(trashPersonList.get(personIndex++));
			trashTeams.add(trashTeam);	
			workingDate.add(Calendar.DAY_OF_YEAR, 7);  // advance to next date

		}
		return trashTeams;
	}
	
}
