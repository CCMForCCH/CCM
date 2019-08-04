package org.cohoman.model.business.trash;

import java.util.Date;

public class TrashTeam {

	private Date sundayDate;
	private TrashPerson caller;
	private TrashPerson strongPerson;
	private TrashPerson worker1;
	private TrashPerson worker2;
	
	
	public Date getSundayDate() {
		return sundayDate;
	}
	public void setSundayDate(Date sundayDate) {
		this.sundayDate = sundayDate;
	}
	public TrashPerson getCaller() {
		return caller;
	}
	public void setCaller(TrashPerson caller) {
		this.caller = caller;
	}
	public TrashPerson getStrongPerson() {
		return strongPerson;
	}
	public void setStrongPerson(TrashPerson strongPerson) {
		this.strongPerson = strongPerson;
	}
	public TrashPerson getWorker1() {
		return worker1;
	}
	public void setWorker1(TrashPerson worker1) {
		this.worker1 = worker1;
	}
	public TrashPerson getWorker2() {
		return worker2;
	}
	public void setWorker2(TrashPerson worker2) {
		this.worker2 = worker2;
	}
	
	
}
