package org.cohoman.model.integration.persistence.beans;

import java.util.Date;

public class PotluckEvent extends EventBean {

	private Long potluckEventId = null;
	private String leader1String = null;
	private String leader2String = null;
	private Long leader1 = 0L;
	private Long leader2 = null;
	private String description = null;

	public PotluckEvent(Date eventDate) {
		super(eventDate);
	}

	public int getUsableEventid() {
		return super.getEventid().intValue();
	}

	public String getChoosableEventDate() {
		String potluckChoice = super.getPrintableEventDate() + ",  Lead: " + leader1String;
		return potluckChoice;
	}

	public Long getPotluckEventId() {
		return potluckEventId;
	}

	public void setPotluckEventId(Long potluckEventId) {
		this.potluckEventId = potluckEventId;
	}

	public String getPrintableLeaders() {
		String leaders = leader1String;
		if (this.leader2String != null) {
			leaders += ", " + leader2String;
		}
		return "Leaders: " + leaders;
	}

	public String getLeader1String() {
		return leader1String;
	}

	public void setLeader1String(String leader1String) {
		this.leader1String = leader1String;
	}

	public String getLeader2String() {
		return leader2String;
	}

	public void setLeader2String(String leader2String) {
		this.leader2String = leader2String;
	}

	public Long getLeader1() {
		return leader1;
	}

	public void setLeader1(Long leader1) {
		this.leader1 = leader1;
	}

	public Long getLeader2() {
		return leader2;
	}

	public void setLeader2(Long leader2) {
		this.leader2 = leader2;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
