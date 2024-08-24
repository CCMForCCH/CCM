package org.cohoman.view.controller.utils;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarUtils implements Serializable {
	
	private static final long serialVersionUID = 4678206276499587830L;
	
	public static class OneMonth{
		private int monthOfYear;
		
		public OneMonth(int monthOfYear) {
			this.monthOfYear = monthOfYear;
		}
		
		public String getMonthName() {
			DateFormatSymbols symbols = new DateFormatSymbols();
			String[] months = symbols.getMonths();
			return months[monthOfYear];
		}
		
		public int getMonthNumber() {
			return monthOfYear;
		}
	}
	
	public static class MealDate {
		private int theYear;
		private OneMonth theMonth;
		private int theDay;
		private String printableDate;
		private int timestamp;
		
		public int getTheYear() {
			return theYear;
		}
		public void setTheYear(int theYear) {
			this.theYear = theYear;
		}
		public OneMonth getTheMonth() {
			return theMonth;
		}
		public void setTheMonth(OneMonth theMonth) {
			this.theMonth = theMonth;
		}
		public int getTheDay() {
			return theDay;
		}
		public void setTheDay(int theDay) {
			this.theDay = theDay;
		}
		public String getPrintableDate() {
			return printableDate;
		}
		public void setPrintableDate(String printableDate) {
			this.printableDate = printableDate;
		}
		public int getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(int timestamp) {
			this.timestamp = timestamp;
		}
		
	}
	
	private static OneMonth[] monthsOfTheYear;
	static {
		monthsOfTheYear = new OneMonth[12];
		for (int i = Calendar.JANUARY; i <= Calendar.DECEMBER; i++) {
			monthsOfTheYear[i - Calendar.JANUARY] = new OneMonth(i);
		}
	}
	
	public static String[] getYears() {
		int startingYear = 2011;
		int numberOfYears = 20;
		String [] years = new String[20];
		int idx = 0;
		for (int year = startingYear; year < startingYear + numberOfYears; year++) {
			years[idx++] = Integer.toString(year); 
		}
		return years;
	}

	public static String[] getBirthYears() {
		int startingYear = 1920;
		int numberOfYears = 110;
		String [] years = new String[111];
		years[0] = "0";
		int idx = 1;
		for (int year = startingYear; year < startingYear + numberOfYears; year++) {
			years[idx++] = Integer.toString(year); 
		}
		return years;
	}

	public static OneMonth[] getMonthsOfTheYear() {
		return monthsOfTheYear;
	}
	
	public static String[] getDaysOfTheMonth(int year, int month) {
		GregorianCalendar gcal = new GregorianCalendar(year, month, 1);
		int maxDayForMonth = gcal.getActualMaximum(Calendar.DAY_OF_MONTH);
		String[] days = new String[maxDayForMonth];
		for (int i = 0; i < maxDayForMonth; i++) {
			days[i] = Integer.toString(i + 1);
		}
		return days;
	}
	
	public static String[] getSpecificDaysOfTheMonth(int year, int month, int day_of_week) {
		GregorianCalendar now = new GregorianCalendar();
		GregorianCalendar gcal = new GregorianCalendar(year, month, 1);
		int maxDayForMonth = gcal.getActualMaximum(Calendar.DAY_OF_MONTH);
		List<String> days = new ArrayList<String>();
		for (int i = 0; i < maxDayForMonth; i++) {
			// Skip over days past or current or not the day
			if (gcal.after(now) && gcal.get(Calendar.DAY_OF_WEEK) == day_of_week) {
				days.add(Integer.toString(i + 1));
			}
			gcal.add(Calendar.DAY_OF_MONTH, 1);
		}
		String[] dayArray = (String[])days.toArray(new String[days.size()]);
		return dayArray;
	}

	public static String[] getSpecificDaysOfTheMonthTotally(int year, int month, int day_of_week) {
		GregorianCalendar gcal = new GregorianCalendar(year, month, 1);
		int maxDayForMonth = gcal.getActualMaximum(Calendar.DAY_OF_MONTH);
		List<String> days = new ArrayList<String>();
		for (int i = 0; i < maxDayForMonth; i++) {
			// Skip over days that don't match the day
			if (gcal.get(Calendar.DAY_OF_WEEK) == day_of_week) {
				days.add(Integer.toString(i + 1));
			}
			gcal.add(Calendar.DAY_OF_MONTH, 1);
		}
		String[] dayArray = (String[])days.toArray(new String[days.size()]);
		return dayArray;
	}

	public static Calendar getNextMondayCal() {
		Calendar theCal = Calendar.getInstance();
		for (int i = 0; i < 7; i++) {
			if (theCal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
				return theCal;
			}
			theCal.add(Calendar.DATE, 1);
		}
		Calendar badCal = new GregorianCalendar(1970, 1, 1);
		return badCal;
	}
	
	public static class TimeSlot{
		private int slotNumber;
		private int hour;
		private int minutes;
		private String slotString;
		
		public TimeSlot(int slotNumber, int hour, int minutes, String slotString) {
			this.slotNumber = slotNumber;
			this.hour = hour;
			this.minutes = minutes;
			this.slotString = slotString;
		}

		public int getSlotNumber() {
			return slotNumber;
		}
		
		public int getHour() {
			return hour;
		}

		public int getMinutes() {
			return minutes;
		}

		public String getSlotString() {
			return slotString;
		}
		
	}

	public static TimeSlot[] getTimeSlotsOfTheDay(int year, int month) {

		// Oddball way of creating the calendar in order to get around
		// a daylight savings time bug in the Java code that somehow messed
		// up the calendar by 1 hour (after 4 years working properly!?)
		// https://stackoverflow.com/questions/45463059/behaviour-of-gregoriancalendar-set-with-daylight-saving-times
		// 11/03/2020
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.add(Calendar.YEAR, -gcal.get(Calendar.YEAR) + year);
		gcal.add(Calendar.MONTH, -gcal.get(Calendar.MONTH) + month);
		gcal.add(Calendar.DAY_OF_YEAR, -gcal.get(Calendar.DAY_OF_YEAR) + 1);
		gcal.add(Calendar.HOUR, -gcal.get(Calendar.HOUR));
		gcal.add(Calendar.MINUTE, -gcal.get(Calendar.MINUTE));
		gcal.add(Calendar.SECOND, -gcal.get(Calendar.SECOND));
		gcal.add(Calendar.AM_PM, -gcal.get(Calendar.AM_PM));
		
		//GregorianCalendar gcal = new GregorianCalendar(year, month, 1, 0, 0, 0);
		TimeSlot[] timeSlots = new TimeSlot[36];
		// Initialize starting slot as 6AM
		gcal.add(Calendar.MINUTE, 12 * 30);
		for (int i = 0; i < 2 * 18; i++) {
			SimpleDateFormat formatter = new SimpleDateFormat("h:mm aa");
			timeSlots[i] = new TimeSlot(i, gcal.get(Calendar.HOUR_OF_DAY),
					gcal.get(Calendar.MINUTE), formatter.format(gcal.getTime()));
			gcal.add(Calendar.MINUTE, 30);
		}
		return timeSlots;
	}

	public static int getTimeSlotForDate(Date thedate) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(thedate);
		int desiredHour = cal.get(Calendar.HOUR_OF_DAY);
		int desiredMinutes = cal.get(Calendar.MINUTE);
		// Start the slots at 6AM
		Calendar calIter = new GregorianCalendar(2010, 0, 1, 6, 0);
		for (int slotNumber = 0; slotNumber < 36; slotNumber++) {
			int hour = calIter.get(Calendar.HOUR_OF_DAY);
			int minutes = calIter.get(Calendar.MINUTE);
			if (hour == desiredHour && minutes == desiredMinutes) {
				return slotNumber;
			}
			calIter.add(Calendar.MINUTE, 30);
		}
		return 0;
	}

	public static Date setTimeSlotForDate(Date theDate, int timeSlot) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(theDate);
		// Assume slots start at 6AM.
		int slotMinutes = (60 * 6) + (timeSlot * 30);
		int hour = slotMinutes / 60;
		int minutes = slotMinutes % 60;
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minutes);
		return cal.getTime();
	}
	
	public static Calendar adjustToStartingSunday(Calendar cal) {
		int [][] adjustAmountArray = {
				{Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY,
					Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY},
					{0, -1, -2, -3, -4, -5, -6}
				};
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int adjustAmount = 0;
		for (int idx = 0; idx < 7; idx++) {
			if (dayOfWeek == adjustAmountArray[0][idx]) {
				adjustAmount = adjustAmountArray[1][idx];
				break;
			}
		}
		cal.add(Calendar.DAY_OF_YEAR, adjustAmount);
		return cal;
	}

	public static Calendar adjustToStartingSaturday(Calendar cal) {
		int [][] adjustAmountArray = {
				{Calendar.SATURDAY, Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, 
					Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY},
					{0, -1, -2, -3, -4, -5, -6}
				};
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int adjustAmount = 0;
		for (int idx = 0; idx < 7; idx++) {
			if (dayOfWeek == adjustAmountArray[0][idx]) {
				adjustAmount = adjustAmountArray[1][idx];
				break;
			}
		}
		cal.add(Calendar.DAY_OF_YEAR, adjustAmount);
		return cal;
	}

	public static Date truncateTimeFromDate(Date dateForday) {
		Calendar caldate = Calendar.getInstance();
		caldate.setTime(dateForday);
		caldate.set(Calendar.HOUR_OF_DAY, 0);
		caldate.set(Calendar.MINUTE, 0);
		caldate.set(Calendar.SECOND, 0);		
		caldate.set(Calendar.MILLISECOND, 0);
		return caldate.getTime();
	}

	public static Boolean dayEarlier(Date date1, Date date2) {
		
		Date date1Day = truncateTimeFromDate(date1);
		Date date2Day = truncateTimeFromDate(date2);
		if (date1Day.compareTo(date2Day) < 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static Boolean sameDay(Date date1, Date date2) {
		
		Date date1Day = truncateTimeFromDate(date1);
		Date date2Day = truncateTimeFromDate(date2);
		if (date1Day.compareTo(date2Day) == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean startDateBeforeOrEqualEndDate(Date startDate, Date endDate) {
		
		Date startDay = truncateTimeFromDate(startDate);
		Date endDay = truncateTimeFromDate(endDate);
		
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(startDay);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(endDay);
		
		if (calStart.before(calEnd) || calStart.equals(calEnd)) {
			return true;
		} else {
			return false;
		}
		
	}
	
	public static String getPrintableEventDate(Date eventDate) {
		SimpleDateFormat formatter;
		Calendar cal = new GregorianCalendar();
		cal.setTime(eventDate);
		if (cal.get(Calendar.HOUR_OF_DAY) == 0) {
			formatter = new SimpleDateFormat("EEE, MMM d, yyyy");
		} else {
			formatter = new SimpleDateFormat("EEE, MMM d, yyyy h:mm aa");
		}
		return formatter.format(eventDate.getTime());
	}

}
