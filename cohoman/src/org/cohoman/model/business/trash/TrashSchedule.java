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
			trashRow.setCaller(oneTeam.getCaller().getUnitnumber()
					+ " " + oneTeam.getCaller().getFirstname());
			trashRow.setStrong(oneTeam.getStrongPerson().getUnitnumber()
					+ " " + oneTeam.getStrongPerson().getFirstname());
			trashRow.setWorker1(oneTeam.getWorker1().getUnitnumber()
					+ " " + oneTeam.getWorker1().getFirstname());
			trashRow.setWorker2(oneTeam.getWorker2().getUnitnumber()
					+ " " + oneTeam.getWorker2().getFirstname());
			trashRows.add(trashRow);
		}
		return trashRows;
	}

	public void addTrashRow(TrashRow trashRow) {
		trashRows.add(trashRow);
	}
}
