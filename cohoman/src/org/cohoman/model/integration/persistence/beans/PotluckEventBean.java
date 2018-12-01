package org.cohoman.model.integration.persistence.beans;

import java.util.Date;

public class PotluckEventBean extends EventBean {

	private Long potluckEventId = null;
	private Long leader1 = 0L;
	private Long leader2 = null;
	private String description = null;

	public PotluckEventBean() {
		super(new Date());
	}

	public PotluckEventBean(Date eventDate) {
		super(eventDate);
	}

	public int getUsableEventid() {
		return super.getEventid().intValue();
	}

	public Long getPotluckEventId() {
		return potluckEventId;
	}

	public void setPotluckEventId(Long potluckEventId) {
		this.potluckEventId = potluckEventId;
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
