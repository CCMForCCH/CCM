package org.cohoman.model.business.trash;

import java.util.Date;

public class TrashTeam {

	private Date sundayDate;
	private TrashPerson organizer;
	private String organizerSub;
	private TrashPerson strongPerson;
	private String strongPersonSub;
	private TrashPerson teamMember1;
	private String teamMember1Sub;
	private TrashPerson teamMember2;
	private String teamMember2Sub;
	private String printableDate;
	
	
	public Date getSundayDate() {
		return sundayDate;
	}
	public void setSundayDate(Date sundayDate) {
		this.sundayDate = sundayDate;
	}
	public TrashPerson getOrganizer() {
		return organizer;
	}
	public void setOrganizer(TrashPerson organizer) {
		this.organizer = organizer;
	}
	public String getOrganizerSub() {
		return organizerSub;
	}
	public void setOrganizerSub(String organizerSub) {
		this.organizerSub = organizerSub;
	}
	public TrashPerson getStrongPerson() {
		return strongPerson;
	}
	public void setStrongPerson(TrashPerson strongPerson) {
		this.strongPerson = strongPerson;
	}
	public String getStrongPersonSub() {
		return strongPersonSub;
	}
	public void setStrongPersonSub(String strongPersonSub) {
		this.strongPersonSub = strongPersonSub;
	}
	public TrashPerson getTeamMember1() {
		return teamMember1;
	}
	public void setTeamMember1(TrashPerson teamMember1) {
		this.teamMember1 = teamMember1;
	}
	public String getTeamMember1Sub() {
		return teamMember1Sub;
	}
	public void setTeamMember1Sub(String teamMember1Sub) {
		this.teamMember1Sub = teamMember1Sub;
	}
	public TrashPerson getTeamMember2() {
		return teamMember2;
	}
	public void setTeamMember2(TrashPerson teamMember2) {
		this.teamMember2 = teamMember2;
	}
	public String getTeamMember2Sub() {
		return teamMember2Sub;
	}
	public void setTeamMember2Sub(String teamMember2Sub) {
		this.teamMember2Sub = teamMember2Sub;
	}
	public String getPrintableDate() {
		return printableDate;
	}
	public void setPrintableDate(String printableDate) {
		this.printableDate = printableDate;
	}
		
}
