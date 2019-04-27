package org.cohoman.view.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import org.cohoman.model.business.User;
import org.cohoman.model.integration.persistence.beans.CohoEvent;
import org.cohoman.model.integration.persistence.beans.MainCalendarEvent;
import org.cohoman.model.service.EventService;
import org.cohoman.model.service.UserService;

@ManagedBean
@SessionScoped
public class RetrieveThisWeekForIndexController implements Serializable {

	private static final long serialVersionUID = 4678206276499587830L;

	private List<CohoEvent> cohoEventList;
	private EventService eventService = null;
	private UserService userService = null;

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<CohoEvent> getCohoEventList() {
		cohoEventList = eventService.getCurrentCohoEvents();
		return cohoEventList;
	}

	public void setCohoEventList(List<CohoEvent> cohoEventList) {
		this.cohoEventList = cohoEventList;
	}

	public String getDateForWeek() {
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM. d, yyyy");
		return formatter.format(cal.getTime());

	}

	public List<MainCalendarEvent> getWeeksEvents() {

		Calendar thisDay = GregorianCalendar.getInstance();
		List<MainCalendarEvent> weeksEvents = new ArrayList<MainCalendarEvent>();
		SimpleDateFormat dayFormatter = new SimpleDateFormat("EEEE");
		SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm aa");
		for (int idx = 0; idx < 7; idx++) {
			List<MainCalendarEvent> oneDaysEvents = eventService
					.getMainCalendarEventsForDay(thisDay.getTime());

			// Add all events for one day
			for (MainCalendarEvent oneEvent : oneDaysEvents) {

				// Meals have no name, just a type. So, copy the type
				// to the name field in the MainCalendarEvent for
				// display purposes of Meals and Pizza/Potlucks
				if (oneEvent.getEventName() == null
						|| oneEvent.getEventName().isEmpty()) {
					oneEvent.setEventName(oneEvent.getEventType());
				}

				// Adjust date format and store in field for display
				String formattedDay = dayFormatter.format(oneEvent
						.getStartDate());
				String formattedTime = timeFormatter.format(oneEvent
						.getStartDate());

				// Special-case date for today
				if (idx == 0) {
					formattedDay = "Today";
				}
				oneEvent.setThisweekPrintableDay(formattedDay);
				oneEvent.setThisweekPrintableTime(formattedTime);
				weeksEvents.add(oneEvent);
			}

			// Advance to the next day of the week
			thisDay.add(Calendar.DAY_OF_YEAR, 1);
		}

		// Return complete list of events for a week
		return weeksEvents;
	}

	public List<String> getBirthdayList() {

		List<User> fullUserList = userService.getUsersForBirthdays();
		List<String> birthdayList = new ArrayList<String>();
		GregorianCalendar workingDate = new GregorianCalendar();
		int day;
		int month;
		for (int idx = 1; idx < 8; idx++) {

			day = workingDate.get(Calendar.DAY_OF_MONTH);
			month = workingDate.get(Calendar.MONTH);
			for (User oneUser : fullUserList) {
				// ignore entries that have a year == 1920
				// a hacky way to ignore people who have been
				// added without knowing their birthday
				if (oneUser.getBirthyear() == 1920) {
					continue;
				}
				if (oneUser.getBirthday() == day
						&& oneUser.getBirthmonth() == month) {
					String birthdayLine = oneUser.getFirstname() + " "
							+ oneUser.getLastname() + ":  ";
					SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM. d");
					birthdayLine += formatter.format(workingDate.getTime());
					birthdayList.add(birthdayLine);
				}
			}
			workingDate.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		return birthdayList;
	}
}
