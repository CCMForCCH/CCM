package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import org.cohoman.model.business.Meal;

//@Named
@ManagedBean
@SessionScoped
public class Meals implements Serializable {
		
		private String[] days  = new String[42];
		private String[] theMeal = {"Sunday, Nov. 7 6:00PM",
		"Menu: Chicken Pot Pie, vegetables, bread, dessert",
		"Cooks: Rowena(L), Anne, Dick",
		"Cleaners: Bill(L), Peg, Trish"};
		
		public Meal getMeal() {
			Meal meal = new Meal();
			meal.setEventDate("Sunday, Nov. 7 6:00PM");
			meal.setMenu("Menu: Chicken Pot Pie, vegetables, bread, dessert");
			List<String> cooks = new ArrayList<String>();
			cooks.add("Cooks: Rowena(L), Anne, Dick");
			meal.setCooks(cooks);
			List<String> cleanups = new ArrayList<String>();
			cleanups.add("Cleaners: Bill(L), Peg, Trish");
			meal.setCleaners(cleanups);
			//String [] retArray = {"theMeal"};
			return meal;
		}
		
		public String toString() {
			return "theValue";
		}
		public Meals() {
			
			// Initialize all days to empty
			for (int i = 0; i < days.length; i++) {
				days[i] = "";
			}
			
			GregorianCalendar d = new GregorianCalendar();
			
			int month = d.get(Calendar.MONTH);
			
			d.set(Calendar.DAY_OF_MONTH, 1);
			
			int weekday = d.get(Calendar.DAY_OF_WEEK);
			
			int currentDay = weekday;
			do {
				days[currentDay++] = Integer.toString(d.get(Calendar.DAY_OF_MONTH));
				d.add(Calendar.DAY_OF_WEEK, 1);
			} while (d.get(Calendar.MONTH) == month);
		}
		
	public String[] getDays() {
			return days;
		}

		public void setDays(String[] days) {
			this.days = days;
		}

		public List<String[]> getWeeks() {
			List<String[]> weeks = new ArrayList<String[]>();
			for (int j = 0; j < 5; j++) {
				String[] week = new String[7];
				for (int i = 0; i < 7; i++) {
					week[i] = days[j*7 + i];
				}
				weeks.add(week);
			}
			return weeks;
		}

}
