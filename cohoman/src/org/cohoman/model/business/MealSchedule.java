package org.cohoman.model.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

import org.cohoman.model.integration.persistence.beans.MealEvent;
import org.cohoman.model.integration.persistence.beans.PizzaEvent;
import org.cohoman.model.integration.persistence.beans.PotluckEvent;
import org.cohoman.model.integration.persistence.dao.MealEventDao;
import org.cohoman.model.integration.persistence.dao.PizzaEventDao;
import org.cohoman.model.integration.persistence.dao.PotluckEventDao;

public class MealSchedule {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private Calendar startDate;
	private Calendar endDate;
	private List<MealEvent> mealEventList;
	private List<PizzaEvent> pizzaEventList;
	private List<PotluckEvent> potluckEventList;
	private List<MealScheduleText> mealScheduleTextRow;
	private List<List<MealScheduleText>> mealScheduleTextRows;

	private MealEventDao mealEventDao;
	private PizzaEventDao pizzaEventDao;
	private PotluckEventDao potluckEventDao;

	public PizzaEventDao getPizzaEventDao() {
		return pizzaEventDao;
	}

	public void setPizzaEventDao(PizzaEventDao pizzaEventDao) {
		this.pizzaEventDao = pizzaEventDao;
	}

	public MealEventDao getMealEventDao() {
		return mealEventDao;
	}

	public void setMealEventDao(MealEventDao mealEventDao) {
		this.mealEventDao = mealEventDao;
	}

	public PotluckEventDao getPotluckEventDao() {
		return potluckEventDao;
	}

	public void setPotluckEventDao(PotluckEventDao potluckEventDao) {
		this.potluckEventDao = potluckEventDao;
	}

	public MealSchedule() {
	}

	public void create(Calendar startDate, Calendar endDate) {
		this.startDate = (Calendar) startDate.clone();
		this.endDate = endDate;

		// get meal events
		mealEventList = mealEventDao.getMealEvents();
		pizzaEventList = pizzaEventDao.getPizzaEvents();
		potluckEventList = potluckEventDao.getPotluckEvents();

		// Sort lists of common meals and pizza/potlucks
		if (mealEventList != null) {
			Collections.sort(mealEventList);
		}
		if (pizzaEventList != null) {
			Collections.sort(pizzaEventList);
		}
		if (potluckEventList != null) {
			Collections.sort(potluckEventList);
		}
	}

	public List<MealRow> getMealRows() {

		Calendar localStartDate = (Calendar) startDate.clone();
		List<MealRow> mealRows = new ArrayList<MealRow>();
		mealScheduleTextRow = new ArrayList<MealScheduleText>();
		mealScheduleTextRows = new ArrayList<List<MealScheduleText>>();

		// walk thru events computing each MealRow until endDate
		// We want to know if each MealEvent falls into THIS week
		// and if so, it will either be saved as the weekend event
		// or the weekday event.
		while (localStartDate.compareTo(endDate) < 0) {
			PizzaEvent pizzaEvent = null;
			MealEvent weekdayEvent = null;
			MealEvent weekendEvent = null;
			PotluckEvent potluckEvent1 = null;
			PotluckEvent potluckEvent2 = null;

			// First handle common meals
			MealRow oneRow = new MealRow(localStartDate);
			for (MealEvent oneEvent : mealEventList) {
				Calendar theMealDate = new GregorianCalendar();
				theMealDate.setTime(oneEvent.getEventDate());
				Calendar weekdayDate1 = (Calendar) localStartDate.clone();
				weekdayDate1.add(Calendar.DAY_OF_MONTH, 2);
				Calendar weekdayDate2 = (Calendar) localStartDate.clone();
				weekdayDate2.add(Calendar.DAY_OF_MONTH, 3);
				Calendar weekendDate1 = (Calendar) localStartDate.clone();
				weekendDate1.add(Calendar.DAY_OF_MONTH, 5);
				Calendar weekendDate2 = (Calendar) localStartDate.clone();
				weekendDate2.add(Calendar.DAY_OF_MONTH, 6);

				if ((theMealDate.get(Calendar.DAY_OF_YEAR) == weekdayDate1
						.get(Calendar.DAY_OF_YEAR) && 
						theMealDate.get(Calendar.YEAR) == weekdayDate1.get(Calendar.YEAR))
						|| 
					(theMealDate.get(Calendar.DAY_OF_YEAR) == weekdayDate2
						.get(Calendar.DAY_OF_YEAR) && 
					theMealDate.get(Calendar.YEAR) == weekdayDate2.get(Calendar.YEAR))) 
				{
					weekdayEvent = oneEvent;
				}

				if ((theMealDate.get(Calendar.DAY_OF_YEAR) == weekendDate1
						.get(Calendar.DAY_OF_YEAR) && 
					theMealDate.get(Calendar.YEAR) == weekendDate1.get(Calendar.YEAR))
						|| 
					(theMealDate.get(Calendar.DAY_OF_YEAR) == weekendDate2
						.get(Calendar.DAY_OF_YEAR) &&
					theMealDate.get(Calendar.YEAR) == weekendDate2.get(Calendar.YEAR)))
				{
					weekendEvent = oneEvent;
				}
			}

			// Next, search thru the list of pizza entries for ones that fall
			// on THIS Monday as we sequence thru the list. Save away
			// that one pizza day.
			for (PizzaEvent oneEvent : pizzaEventList) {
				Calendar theMealDate = new GregorianCalendar();
				theMealDate.setTime(oneEvent.getEventDate());

				if (theMealDate.get(Calendar.DAY_OF_YEAR) == localStartDate
						.get(Calendar.DAY_OF_YEAR) && 
					theMealDate.get(Calendar.YEAR) == localStartDate
						.get(Calendar.YEAR)) 
				{
					pizzaEvent = oneEvent;
					break;
				}

			}

			// lastly, search thru the list of potluck entries as we sequence
			// thru the list. Save away the first then the second potluck for
			// the week. Generate an error or at least a log message if there
			// are more than 2 potluck meals for a week.
			for (PotluckEvent oneEvent : potluckEventList) {
				Calendar theMealDate = new GregorianCalendar();
				theMealDate.setTime(oneEvent.getEventDate());
				Calendar dayOfWeek = (Calendar) localStartDate.clone();

				// Check all events for the coming week.
				for (int daycount = 1; daycount < 8; daycount++) {
					if (theMealDate.get(Calendar.DAY_OF_YEAR) == dayOfWeek
							.get(Calendar.DAY_OF_YEAR) &&
							theMealDate.get(Calendar.YEAR) == dayOfWeek
							.get(Calendar.YEAR)) 
					{
						if (potluckEvent1 == null) {
							potluckEvent1 = oneEvent;
						} else if (potluckEvent2 == null) {
							potluckEvent2 = oneEvent;
						} else {
							// Error case: no room for more than 2 potlucks in a
							// week
							logger.warning("WARNING: There are more than 2 potlucks scheduled for a week and the meals calendar cannot handle that many."
									+ "The ignored meal was scheduled for "
									+ oneEvent.getPrintableEventDate());
						}
					}
					dayOfWeek.add(Calendar.DAY_OF_MONTH, 1);
				}
			}

			// If there are meal or pizza/potluck events for this week,
			// we have saved them in the 3 "Event" variables below.
			// Then they are "populated into a MealRow object which
			// saves those "Events" for each row in the Meal Calendar.
			// Then each MealRow is added to the list and we keep sequencing
			// through the weeks in the meal cycle.
			// These next 2 lines are no longer used???
			oneRow.populate(pizzaEvent, weekdayEvent, weekendEvent);
			mealRows.add(oneRow);

			// New code (6/9/2017) to handle changes for potlucks
			if (pizzaEvent != null) {
				mealScheduleTextRow.add(makePizzaIntoText(pizzaEvent));
			}
			if (weekdayEvent != null) {
				mealScheduleTextRow.add(makeMealIntoText(weekdayEvent));
			}
			if (weekendEvent != null) {
				mealScheduleTextRow.add(makeMealIntoText(weekendEvent));
			}
			if (potluckEvent1 != null) {
				mealScheduleTextRow.add(makePotluckIntoText(potluckEvent1));
			}
			if (potluckEvent2 != null) {
				mealScheduleTextRow.add(makePotluckIntoText(potluckEvent2));
			}
			if (!mealScheduleTextRow.isEmpty()) {
				Collections.sort(mealScheduleTextRow);
				mealScheduleTextRows.add(mealScheduleTextRow);

				// Create new row for next pass through loop.
				mealScheduleTextRow = new ArrayList<MealScheduleText>();
			}

			// Reset potluck events so new week's values are entered properly.
			potluckEvent1 = null;
			potluckEvent2 = null;

			// Move to next week and loop again.
			localStartDate.add(Calendar.DAY_OF_MONTH, 7);
		}

		return mealRows;

	}

	public List<List<MealScheduleText>> getMealTextRowsForUI() {

		getMealRows();
		return mealScheduleTextRows;
	}

	public List<MealScheduleText> getMealScheduleTextRow() {

		return mealScheduleTextRow;
	}

	public void setMealScheduleTextRow(
			List<MealScheduleText> mealScheduleTextRow) {
		this.mealScheduleTextRow = mealScheduleTextRow;
	}

	public List<List<MealScheduleText>> getMealScheduleTextRows() {
		return mealScheduleTextRows;
	}

	public void setMealScheduleTextRows(
			List<List<MealScheduleText>> mealScheduleTextRows) {
		this.mealScheduleTextRows = mealScheduleTextRows;
	}

	private MealScheduleText makePizzaIntoText(PizzaEvent pizzaEvent) {

		MealScheduleText mealScheduleText = new MealScheduleText();
		mealScheduleText.setEventDate(pizzaEvent.getEventDate());
		mealScheduleText.setPrintableEventDate(pizzaEvent
				.getPrintableEventDate());
		mealScheduleText.setMealtype(pizzaEvent.getEventtype());
		String body = pizzaEvent.getPrintableLeaders();
		if (pizzaEvent.getEventName() != null
				&& pizzaEvent.getEventName().length() > 0) {
			body += "\n" + "Occasion: " + pizzaEvent.getEventName();
		}
		if (pizzaEvent.getEventinfo() != null
				&& pizzaEvent.getEventinfo().length() > 0) {
			body += "\n" + "Info: " + pizzaEvent.getEventinfo();
		}
		body = String.format(body);
		mealScheduleText.setMealbody(body);
		String weekstarting = convertToShortPrintabledDate(pizzaEvent
				.getEventDate());
		mealScheduleText.setWeekstarting(weekstarting);
		return mealScheduleText;

	}

	private MealScheduleText makePotluckIntoText(PotluckEvent potluckEvent) {

		MealScheduleText mealScheduleText = new MealScheduleText();
		mealScheduleText.setEventDate(potluckEvent.getEventDate());
		mealScheduleText.setPrintableEventDate(potluckEvent
				.getPrintableEventDate());
		mealScheduleText.setMealtype(potluckEvent.getEventtype());
		String body = potluckEvent.getPrintableLeaders() + "\n" + "Info: "
				+ potluckEvent.getEventinfo();
		body = String.format(body);
		mealScheduleText.setMealbody(body);
		String weekstarting = convertToShortPrintabledDate(potluckEvent
				.getEventDate());
		mealScheduleText.setWeekstarting(weekstarting);
		return mealScheduleText;

	}

	private MealScheduleText makeMealIntoText(MealEvent mealEvent) {

		MealScheduleText mealScheduleText = new MealScheduleText();
		mealScheduleText.setEventDate(mealEvent.getEventDate());
		mealScheduleText.setPrintableEventDate(mealEvent
				.getPrintableEventDate());
		mealScheduleText.setMealtype(mealEvent.getEventtype());
		String body = mealEvent.getPrintableMenu() + "\n"
				+ mealEvent.getPrintableCooks() + "\n"
				+ mealEvent.getPrintableCleaners();
		body = String.format(body);
		mealScheduleText.setMealbody(body);
		String weekstarting = convertToShortPrintabledDate(mealEvent
				.getEventDate());
		mealScheduleText.setWeekstarting(weekstarting);
		return mealScheduleText;

	}

	private String convertToShortPrintabledDate(Date theDate) {

		// adjust date "back" to Monday of this week
		String printableDate;
		SimpleDateFormat formatter;
		Calendar cal = new GregorianCalendar();
		cal.setTime(theDate);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		// Adjust for Sunday
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			cal.add(Calendar.DAY_OF_MONTH, -6);
		} else {
			cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek + 2);
		}
		formatter = new SimpleDateFormat("EEE");
		// printableDate = formatter.format(theDate.getTime());
		printableDate = formatter.format(cal.getTime());
		formatter = new SimpleDateFormat("MMM d");
		printableDate += ",\n" + formatter.format(cal.getTime());
		printableDate = String.format(printableDate);
		return printableDate;
	}

	public class MealScheduleText implements Comparable<MealScheduleText> {

		private Date eventDate;
		private String printableEventDate;
		private String mealtype;
		private String mealbody;
		private String weekstarting;

		public Date getEventDate() {
			return eventDate;
		}

		public void setEventDate(Date eventDate) {
			this.eventDate = eventDate;
		}

		public String getPrintableEventDate() {
			return printableEventDate;
		}

		public void setPrintableEventDate(String printableEventDate) {
			this.printableEventDate = printableEventDate;
		}

		public String getMealtype() {
			return mealtype;
		}

		public void setMealtype(String mealtype) {
			this.mealtype = mealtype;
		}

		public String getMealbody() {
			return mealbody;
		}

		public void setMealbody(String mealbody) {
			this.mealbody = mealbody;
		}

		public String getWeekstarting() {
			return weekstarting;
		}

		public void setWeekstarting(String weekstarting) {
			this.weekstarting = weekstarting;
		}

		public int compareTo(MealScheduleText anotherMealEvent) {
			return eventDate.compareTo(anotherMealEvent.getEventDate());
		}

	}
}
