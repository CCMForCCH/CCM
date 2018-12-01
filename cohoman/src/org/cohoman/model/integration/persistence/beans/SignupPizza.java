package org.cohoman.model.integration.persistence.beans;

public class SignupPizza {

	private Long signuppizzaid = null;
	private Long eventid = null;
	private Long userid = null;
	private int numberattendingpizza = 0;
	private int numberattendingpotluck = 0;
	private String pizzatopping1 = "";
	private String pizzatopping2 = "";
	private String pizzawillbring = "";
	
	public Long getSignuppizzaid() {
		return signuppizzaid;
	}
	public void setSignuppizzaid(Long signuppizzaid) {
		this.signuppizzaid = signuppizzaid;
	}
	public Long getEventid() {
		return eventid;
	}
	public void setEventid(Long eventid) {
		this.eventid = eventid;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public int getNumberattendingpizza() {
		return numberattendingpizza;
	}
	public void setNumberattendingpizza(int numberattendingpizza) {
		this.numberattendingpizza = numberattendingpizza;
	}
	public int getNumberattendingpotluck() {
		return numberattendingpotluck;
	}
	public void setNumberattendingpotluck(int numberattendingpotluck) {
		this.numberattendingpotluck = numberattendingpotluck;
	}
	public String getPizzatopping1() {
		return pizzatopping1;
	}
	public void setPizzatopping1(String pizzatopping1) {
		this.pizzatopping1 = pizzatopping1;
	}
	public String getPizzatopping2() {
		return pizzatopping2;
	}
	public void setPizzatopping2(String pizzatopping2) {
		this.pizzatopping2 = pizzatopping2;
	}
	public String getPizzawillbring() {
		return pizzawillbring;
	}
	public void setPizzawillbring(String pizzawillbring) {
		this.pizzawillbring = pizzawillbring;
	}

}
