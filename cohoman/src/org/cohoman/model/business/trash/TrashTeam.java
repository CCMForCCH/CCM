package org.cohoman.model.business.trash;

import java.util.Date;

public class TrashTeam {

	private Date sundayDate;
	private TrashPerson organizer;
	private TrashPerson strongPerson;
	private TrashPerson teamMember1;
	private TrashPerson teamMember2;
	
	
	public Date getSundayDate() {
		return sundayDate;
	}
	public void setSundayDate(Date sundayDate) {
		this.sundayDate = sundayDate;
	}
	public TrashPerson getOrganizer() {
		return organizer;
	}
	public void setOrganizer(TrashPerson orgnizer) {
		this.organizer = orgnizer;
	}
	public TrashPerson getStrongPerson() {
		return strongPerson;
	}
	public void setStrongPerson(TrashPerson strongPerson) {
		this.strongPerson = strongPerson;
	}
	public TrashPerson getTeamMember1() {
		return teamMember1;
	}
	public void setTeamMember1(TrashPerson teamMember1) {
		this.teamMember1 = teamMember1;
	}
	public TrashPerson getTeamMember2() {
		return teamMember2;
	}
	public void setTeamMember2(TrashPerson teamMember2) {
		this.teamMember2 = teamMember2;
	}	
	
}
