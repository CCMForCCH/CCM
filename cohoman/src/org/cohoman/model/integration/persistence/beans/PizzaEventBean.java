package org.cohoman.model.integration.persistence.beans;

import java.util.Date;

public class PizzaEventBean extends EventBean {

	private Long pizzaeventid = null;
	private Long leader1 = 0L;
	private Long leader2 = null;
	private boolean ismealclosed = false;
	private String eventName = null;
	private String eventinfo = null;

	public PizzaEventBean(Date eventDate) {
		super(eventDate);
	}

	//TODO make sense???? Yes needed. (else instantiation error)
	public PizzaEventBean() {
		super(new Date());
	}

	public Long getPizzaeventid() {
		return pizzaeventid;
	}

	public void setPizzaeventid(Long pizzaeventid) {
		this.pizzaeventid = pizzaeventid;
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

	public boolean isIsmealclosed() {
		return ismealclosed;
	}

	public void setIsmealclosed(boolean ismealclosed) {
		this.ismealclosed = ismealclosed;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventinfo() {
		return eventinfo;
	}

	public void setEventinfo(String eventinfo) {
		this.eventinfo = eventinfo;
	}

}
