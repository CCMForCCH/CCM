package org.cohoman.view.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import org.cohoman.model.integration.persistence.beans.MainCalendarEvent;
import org.cohoman.model.service.EventService;
import org.cohoman.view.controller.utils.MainEventLink;

//@Named
@ManagedBean
@SessionScoped
public class DaysOfMonth implements Serializable {
		
		private String[] days  = new String[42];
		
		private List<String> daysEvents;
		
		private Calendar calendarEntryForMonth;
		private String titleMonthString;
		private EventService eventService = null;


		public EventService getEventService() {
			return eventService;
		}

		public void setEventService(EventService eventService) {
			this.eventService = eventService;
		}

		
		public String getTitleMonthString() {
			return titleMonthString;
		}

		public void setTitleMonthString(String titleMonthString) {
			this.titleMonthString = titleMonthString;
		}

		public List getNextEvents(String day) {
			
			daysEvents.add(day);
			return daysEvents;
		}
		
		public DaysOfMonth() {
			
			// Initialize all days to empty
			for (int i = 0; i < days.length; i++) {
				days[i] = "";
			}
			
			Calendar calendarEntryForMonthTmp = new GregorianCalendar();
			calendarEntryForMonth = new GregorianCalendar();   //TODO cleanup
			SimpleDateFormat formatter = new SimpleDateFormat("MMMMM, yyyy");
			titleMonthString = formatter.format(calendarEntryForMonth.getTime());
			
			int month = calendarEntryForMonthTmp.get(Calendar.MONTH);
			
			calendarEntryForMonthTmp.set(Calendar.DAY_OF_MONTH, 1);
			
			int weekday = calendarEntryForMonthTmp.get(Calendar.DAY_OF_WEEK);
			
			int currentDay = weekday - 1;
			do {
				days[currentDay++] = Integer.toString(calendarEntryForMonthTmp.get(Calendar.DAY_OF_MONTH));
				calendarEntryForMonthTmp.add(Calendar.DAY_OF_WEEK, 1);
			} while (calendarEntryForMonthTmp.get(Calendar.MONTH) == month);
		}
		
	    public String[] getDays() {
			return days;
		}

		public void setDays(String[] days) {
			this.days = days;
		}
		

		public List<WeekOfMainCalendarDaysList> getWeeks() {
			
			boolean finishedWithWeeks = false;
			List<WeekOfMainCalendarDaysList> weeks = new ArrayList<WeekOfMainCalendarDaysList>();
			
			// Make one week's worth of MainCalendarDay objects
			for (int j = 0; j < 6; j++) {
				ArrayList<MainCalendarDay> oneWeekList = new ArrayList<MainCalendarDay>();
				WeekOfMainCalendarDaysList weekOfMainCalendarDaysList = 
					new WeekOfMainCalendarDaysList();
				weekOfMainCalendarDaysList.setMainCalendarDayList(oneWeekList);
				
				for (int i = 0; i < 7; i++) {
					int dayIndex = j*7 + i;
					if (j > 0 && i == 0 && days[dayIndex].isEmpty()) {
						finishedWithWeeks = true;
						break;
					}
					MainCalendarDay mainCalendarDay = new MainCalendarDay();
					mainCalendarDay.setDayNumber(days[dayIndex]);
					
					//TODO: get all events for this day from event service
					// Get correct date
					// Compute day as a number
					if (!days[dayIndex].isEmpty()) {
						int dayAsInt = Integer.parseInt(days[dayIndex]);
						calendarEntryForMonth.set(Calendar.DAY_OF_MONTH, dayAsInt);
						List<MainCalendarEvent> mainCalendarEventList =
							eventService.getMainCalendarEventsForDay(calendarEntryForMonth.getTime());
						
						List<String> eventStrings = new ArrayList<String>();
						List<MainEventLink> eventLinks = new ArrayList<MainEventLink>();
						SimpleDateFormat formatter = new SimpleDateFormat("h:mmaa");

						for (MainCalendarEvent mainCalendarEvent:mainCalendarEventList) {
						
							String starttime = 
								formatter.format(mainCalendarEvent.getStartDate().getTime());
							String oneEventString =
								starttime + " " + mainCalendarEvent.getEventType();
							eventStrings.add(oneEventString);
							
							MainEventLink oneLink = new MainEventLink();
							oneLink.setEventname(oneEventString);
							oneLink.setEventlink(mainCalendarEvent.getEventlink());
							eventLinks.add(oneLink);
						}
						mainCalendarDay.setEventStrings(eventStrings);
						mainCalendarDay.setEventLinks(eventLinks);
					}
					oneWeekList.add(mainCalendarDay);
				} // for i
				if (finishedWithWeeks) {
					break;
				}
				weeks.add(weekOfMainCalendarDaysList);
			} // for j
			return weeks;
		}

		public String previousMonth() {
			changeMonth(false);
			return null;
		}

		public String nextMonth() {
			changeMonth(true);
			return null;
		}

		private void changeMonth(boolean nextMonth) {
			// Initialize all days to empty
			for (int i = 0; i < days.length; i++) {
				days[i] = "";
			}
		
		//Calendar calendarEntryForMonthTmp = new GregorianCalendar();
			// Get new calendar instance to work with from current calendar.
			Calendar calendarEntryForMonthTmp = Calendar.getInstance();
			calendarEntryForMonthTmp.setTime(calendarEntryForMonth.getTime());
			
			// Increment/decrement month
			if (nextMonth) {
				calendarEntryForMonth.add(Calendar.MONTH, 1);
				calendarEntryForMonthTmp.add(Calendar.MONTH, 1);
			} else {
				calendarEntryForMonth.add(Calendar.MONTH, -1);				
				calendarEntryForMonthTmp.add(Calendar.MONTH, -1);				
			}
		//calendarEntryForMonth = new GregorianCalendar();   //TODO cleanup
			SimpleDateFormat formatter = new SimpleDateFormat("MMMMM, yyyy");
			titleMonthString = formatter.format(calendarEntryForMonthTmp.getTime());
		
			int month = calendarEntryForMonthTmp.get(Calendar.MONTH);
		
			calendarEntryForMonthTmp.set(Calendar.DAY_OF_MONTH, 1);
		
			int weekday = calendarEntryForMonthTmp.get(Calendar.DAY_OF_WEEK);
		
			int currentDay = weekday - 1;
			do {
				days[currentDay++] = Integer.toString(calendarEntryForMonthTmp.get(Calendar.DAY_OF_MONTH));
				calendarEntryForMonthTmp.add(Calendar.DAY_OF_WEEK, 1);
			} while (calendarEntryForMonthTmp.get(Calendar.MONTH) == month);
		}

		
		public class WeekOfMainCalendarDaysList {
			
			private ArrayList<MainCalendarDay> mainCalendarDayList;

			public ArrayList<MainCalendarDay> getMainCalendarDayList() {
				return mainCalendarDayList;
			}

			public void setMainCalendarDayList(
					ArrayList<MainCalendarDay> mainCalendarDayList) {
				this.mainCalendarDayList = mainCalendarDayList;
			}			
			
			public void addToList(MainCalendarDay mainCalendarDay) {
				mainCalendarDayList.add(mainCalendarDay);
			}
		}
	
}
