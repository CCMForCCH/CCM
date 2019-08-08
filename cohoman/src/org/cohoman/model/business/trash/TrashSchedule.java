package org.cohoman.model.business.trash;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.cohoman.model.integration.persistence.beans.UnitBean;
import org.cohoman.model.integration.persistence.dao.UnitsDao;
import org.cohoman.model.integration.persistence.dao.UserDao;

public class TrashSchedule {


	private List<TrashRow> trashRows;
	private List<TrashPerson> trashPersonList;

	public TrashSchedule(List<TrashPerson> trashPersonList) {
		trashRows = new ArrayList<TrashRow>();
		this.trashPersonList = trashPersonList;
	}


	public List<TrashRow> getTrashRows() {
			
		SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");

		// Get all the printable rows, unformatted
		TrashCycle trashCycle = new TrashCycle(trashPersonList);		
		List<TrashTeam> trashTeams = trashCycle.getTrashTeams();
		
		// Now make a list of printable rows
		for (TrashTeam oneTeam : trashTeams) {
			TrashRow trashRow = new TrashRow();
			trashRow.setSundayDate(formatter.format(oneTeam.getSundayDate().getTime()));
			if (oneTeam.getOrganizer() == null) {
				trashRow.setOrganizer("");
			} else {
				trashRow.setOrganizer(oneTeam.getOrganizer().getUnitnumber()
						+ " " + oneTeam.getOrganizer().getFirstname());
			}
			if (oneTeam.getStrongPerson() == null) {
				trashRow.setStrongPerson("");
			} else {
				trashRow.setStrongPerson(oneTeam.getStrongPerson().getUnitnumber()
						+ " " + oneTeam.getStrongPerson().getFirstname());
			}
			if (oneTeam.getTeamMember1() == null) {
				trashRow.setTeamMember1("");
			} else {
				trashRow.setTeamMember1(oneTeam.getTeamMember1().getUnitnumber()
					+ " " + oneTeam.getTeamMember1().getFirstname());
			}
			if (oneTeam.getTeamMember2() == null) {
				trashRow.setTeamMember2("");
			} else {
				trashRow.setTeamMember2(oneTeam.getTeamMember2().getUnitnumber()
					+ " " + oneTeam.getTeamMember2().getFirstname());
			}
			trashRows.add(trashRow);
		}
		return trashRows;
	}

	public void addTrashRow(TrashRow trashRow) {
		trashRows.add(trashRow);
	}
}
