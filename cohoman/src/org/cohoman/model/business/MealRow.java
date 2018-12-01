package org.cohoman.model.business;

import java.util.Arrays;
import java.util.Calendar;

import org.cohoman.model.integration.persistence.beans.MealEvent;
import org.cohoman.model.integration.persistence.beans.PizzaEvent;

public class MealRow {

	// start date to end date, figure out a list of 3-tuple rows
	
	private Calendar startingMonday;
	

	private MealEvent weekendMealEvent;
	private PizzaEvent pizzaEvent;
	private MealEvent weekdayMealEvent;
	private static String noleaders = "*** No meal scheduled ***";
	
	MealRow(Calendar startingMonday) {
		this.startingMonday = startingMonday;
	}
	
	public void populate(PizzaEvent pizzaday, MealEvent weekday, MealEvent weekend) {
		
		//List<MealEvent> mealEventList = DBRead.getMealEvents();
		//Collections.sort(mealEventList);

		// Make empty row to start
/*
		Calendar pizzaDate = (Calendar)startingMonday.clone();
		pizzaDate.set(Calendar.HOUR_OF_DAY, 0);
		PizzaEvent emptyPizzaEvent = new PizzaEvent(pizzaDate.getTime());
		emptyPizzaEvent.setLeader1String(noleaders);
		pizzaEvent = emptyPizzaEvent;
		
		Calendar weekdayDate = (Calendar)startingMonday.clone();
		weekdayDate.set(Calendar.HOUR_OF_DAY, 0);
		weekdayDate.add(Calendar.DAY_OF_MONTH, 2);
		MealEvent emptyWeekdayMeal = new MealEvent(weekdayDate.getTime());
		emptyWeekdayMeal.setMenu("*** No meal scheduled ***");
		weekdayMealEvent = emptyWeekdayMeal;
		
		Calendar weekendDate = (Calendar)startingMonday.clone();
		weekendDate.set(Calendar.HOUR_OF_DAY, 0);
		weekendDate.add(Calendar.DAY_OF_MONTH, 5);
		MealEvent emptyWeekendMeal = new MealEvent(weekendDate.getTime());
		emptyWeekendMeal.setMenu("*** No meal scheduled ***");
		weekendMealEvent = emptyWeekendMeal;
*/
		if (pizzaday != null) {
			pizzaEvent = pizzaday;
		}
		if (weekday != null) {
			weekdayMealEvent = weekday;
		}
		
		if (weekend != null) {
			weekendMealEvent = weekend;
		}
	}

	public Calendar getStartingMonday() {
		return startingMonday;
	}

	public MealEvent getWeekendMealEvent() {
		return weekendMealEvent;
	}

	public MealEvent getWeekdayMealEvent() {
		return weekdayMealEvent;
	}
	
	public PizzaEvent getPizzaEvent() {
		return pizzaEvent;
	}

}
