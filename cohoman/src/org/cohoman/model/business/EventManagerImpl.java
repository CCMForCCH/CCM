package org.cohoman.model.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.cohoman.model.business.MealSchedule.MealScheduleText;
import org.cohoman.model.dto.CohoEventDTO;
import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.dto.MealEventDTO;
import org.cohoman.model.dto.PizzaEventDTO;
import org.cohoman.model.dto.PotluckEventDTO;
import org.cohoman.model.dto.PrivateEventDTO;
import org.cohoman.model.dto.SignupMealDTO;
import org.cohoman.model.dto.SignupPizzaDTO;
import org.cohoman.model.dto.SignupPotluckDTO;
import org.cohoman.model.integration.SMS.SmsSender;
import org.cohoman.model.integration.email.SendEmail;
import org.cohoman.model.integration.persistence.beans.CohoEvent;
import org.cohoman.model.integration.persistence.beans.EventTypeDefs;
import org.cohoman.model.integration.persistence.beans.MainCalendarEvent;
import org.cohoman.model.integration.persistence.beans.MealEvent;
import org.cohoman.model.integration.persistence.beans.PizzaEvent;
import org.cohoman.model.integration.persistence.beans.PotluckEvent;
import org.cohoman.model.integration.persistence.beans.PrivateEvent;
import org.cohoman.model.integration.persistence.beans.SpaceBean;
import org.cohoman.model.integration.persistence.dao.CohoEventDao;
import org.cohoman.model.integration.persistence.dao.ConfigInfo;
import org.cohoman.model.integration.persistence.dao.MaintenanceDao;
import org.cohoman.model.integration.persistence.dao.MealEventDao;
import org.cohoman.model.integration.persistence.dao.PizzaEventDao;
import org.cohoman.model.integration.persistence.dao.PotluckEventDao;
import org.cohoman.model.integration.persistence.dao.PrivateEventDao;
import org.cohoman.model.integration.persistence.dao.SignupMealDao;
import org.cohoman.model.integration.persistence.dao.SignupPizzaDao;
import org.cohoman.model.integration.persistence.dao.SignupPotluckDao;
import org.cohoman.model.integration.persistence.dao.SpacesDao;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.cohoman.view.controller.utils.CalendarUtils.MealDate;
import org.cohoman.view.controller.utils.CalendarUtils.OneMonth;
import org.cohoman.view.controller.utils.EventStateEnums;

public class EventManagerImpl implements EventManager {

	private MealEventDao mealEventDao = null;
	private PizzaEventDao pizzaEventDao = null;
	private PotluckEventDao potluckEventDao = null;
	private CohoEventDao cohoEventDao = null;
	private PrivateEventDao privateEventDao = null;
	private SpacesDao spacesDao = null;
	private SignupMealDao signupMealDao = null;
	private SignupPizzaDao signupPizzaDao = null;
	private SignupPotluckDao signupPotluckDao = null;

	private MealSchedule mealSchedule = null;

	public PizzaEventDao getPizzaEventDao() {
		return pizzaEventDao;
	}

	public void setPizzaEventDao(PizzaEventDao pizzaEventDao) {
		this.pizzaEventDao = pizzaEventDao;
	}

	public PotluckEventDao getPotluckEventDao() {
		return potluckEventDao;
	}

	public void setPotluckEventDao(PotluckEventDao potluckEventDao) {
		this.potluckEventDao = potluckEventDao;
	}

	public CohoEventDao getCohoEventDao() {
		return cohoEventDao;
	}

	public void setCohoEventDao(CohoEventDao cohoEventDao) {
		this.cohoEventDao = cohoEventDao;
	}

	public PrivateEventDao getPrivateEventDao() {
		return privateEventDao;
	}

	public void setPrivateEventDao(PrivateEventDao privateEventDao) {
		this.privateEventDao = privateEventDao;
	}

	public SpacesDao getSpacesDao() {
		return spacesDao;
	}

	public void setSpacesDao(SpacesDao spacesDao) {
		this.spacesDao = spacesDao;
	}

	public SignupMealDao getSignupMealDao() {
		return signupMealDao;
	}

	public void setSignupMealDao(SignupMealDao signupMealDao) {
		this.signupMealDao = signupMealDao;
	}

	public SignupPizzaDao getSignupPizzaDao() {
		return signupPizzaDao;
	}

	public void setSignupPizzaDao(SignupPizzaDao signupPizzaDao) {
		this.signupPizzaDao = signupPizzaDao;
	}
	
	public SignupPotluckDao getSignupPotluckDao() {
		return signupPotluckDao;
	}

	public void setSignupPotluckDao(SignupPotluckDao signupPotluckDao) {
		this.signupPotluckDao = signupPotluckDao;
	}

	public MealSchedule getMealSchedule() {
		return mealSchedule;
	}

	public void setMealSchedule(MealSchedule mealSchedule) {
		this.mealSchedule = mealSchedule;
	}

	public MealEventDao getMealEventDao() {
		return mealEventDao;
	}

	public void setMealEventDao(MealEventDao mealEventDao) {
		this.mealEventDao = mealEventDao;
	}

	public void createMealEvent(MealEventDTO mealEventDTO, String leaderFullname)
			throws CohomanException {
		mealEventDao.createMealEvent(mealEventDTO);
		
		// Send email notification of meal creation.
		String emailAddress = "cch-talk@googlegroups.com";
		String subject = "New common meal just added!";
		String menu = mealEventDTO.getMenu();
		String body = 
				"Hello Cambridge Cohousing resident,\n\n" +
				"A new common meal has just been added to the CCM meal schedule!\n\n" +
				"Meal Date: " + CalendarUtils.getPrintableEventDate(mealEventDTO.getEventDate()) + "\n" +
				"Leader: " + leaderFullname + "\n" +
				"Menu: " + menu + "\n\n" +			
				"Use CCM to signup to attend this common meal or to see more details about the meal.\n\n" +
				"(This is an automated message from CCM.)\n";

		// String subject = "Testing out a googlegroup email issue";
		// String body = "This is simply a test that you should ignore.";

		sendEmailToAddress(emailAddress, subject, body);

	}

	public void editMealEvent(MealEvent mealEvent) throws CohomanException {
		mealEventDao.updateMealEvent(mealEvent);
	}

	public void deleteMealEvent(MealEvent mealEvent) {
		mealEventDao.deleteMealEvent(mealEvent);
	}

	public void createPizzaEvent(PizzaEventDTO pizzaEventDTO, String leaderFullname)
			throws CohomanException {
		pizzaEventDao.createPizzaEvent(pizzaEventDTO);

		// Send email notification of meal creation.
		String emailAddress = "cch-talk@googlegroups.com";
		String subject = "New pizza/potluck just added!";
		String body = 
				"Hello Cambridge Cohousing resident,\n\n" +
				"A new pizza/potluck meal has just been added to the CCM meal schedule!\n\n" +
				"Meal Date: " + CalendarUtils.getPrintableEventDate(pizzaEventDTO.getEventDate()) + "\n" +
				"Leader: " + leaderFullname + "\n" +
				"Use CCM to signup to attend this pizza/potluck meal or to see more details about the meal.\n\n" +
				"(This is an automated message from CCM.)\n";

		sendEmailToAddress(emailAddress, subject, body);

	}

	public void editPizzaEvent(PizzaEvent pizzaEvent) throws CohomanException {
		pizzaEventDao.updatePizzaEvent(pizzaEvent);
	}

	public void deletePizzaEvent(PizzaEvent pizzaEvent) {
		pizzaEventDao.deletePizzaEvent(pizzaEvent);
	}

	public void createPotluckEvent(PotluckEventDTO potluckEventDTO,
			String leaderFullname) throws CohomanException {
		potluckEventDao.createPotluckEvent(potluckEventDTO);
		
		// Send email notification of meal creation.
		String emailAddress = "cch-talk@googlegroups.com";
		String subject = "New potluck just added!";
		String body = 
				"Hello Cambridge Cohousing resident,\n\n" +
				"A new potluck meal has just been added to the CCM meal schedule!\n\n" +
				"Meal Date: " + CalendarUtils.getPrintableEventDate(potluckEventDTO.getEventDate()) + "\n" +
				"Leader: " + leaderFullname + "\n" +
				"Use CCM to signup to attend this potluck meal or to see more details about the meal.\n\n" +
				"(This is an automated message from CCM.)\n";

		sendEmailToAddress(emailAddress, subject, body);

	}

	public void editPotluckEvent(PotluckEvent potluckEvent) throws CohomanException {
		potluckEventDao.updatePotluckEvent(potluckEvent);
	}

	public void deletePotluckEvent(PotluckEvent potluckEvent) {
			potluckEventDao.deletePotluckEvent(potluckEvent);
}

	public void createCohoEvent(CohoEventDTO cohoEventDTO)
			throws CohomanException {
		cohoEventDao.createCohoEvent(cohoEventDTO);
	}

	public void editCohoEvent(CohoEvent cohoEvent) throws CohomanException {
		cohoEventDao.updateCohoEvent(cohoEvent);
	}

	public void deleteCohoEvent(CohoEvent cohoEvent) {
		cohoEventDao.deleteCohoEvent(cohoEvent);
	}

	public void createPrivateEvent(PrivateEventDTO privateEventDTO)
			throws CohomanException {
		privateEventDao.createPrivateEvent(privateEventDTO);
	}

	public void editPrivateEvent(PrivateEvent privateEvent)
			throws CohomanException {
		privateEventDao.updatePrivateEvent(privateEvent);
	}

	public void deletePrivateEvent(Long privateEventId) {
		privateEventDao.deletePrivateEvent(privateEventId);
	}
	
	public void signupForMeal(SignupMealDTO dto) throws CohomanException {
		signupMealDao.create(dto);
	}

	public List<SignupMealDTO> getAllMealSignups(Long eventid) throws CohomanException {
				
		return signupMealDao.getAllMealSignups(eventid);
	}

	public void deleteSignupForMeal(SignupMealDTO dto) throws CohomanException {
		signupMealDao.delete(dto);
	}

	public void signupForPizza(SignupPizzaDTO dto) throws CohomanException {
		signupPizzaDao.create(dto);
	}

	public List<SignupPizzaDTO> getAllPizzaSignups(Long eventid) throws CohomanException {
				
		return signupPizzaDao.getAllPizzaSignups(eventid);
	}

	public void deleteSignupForPizza(SignupPizzaDTO dto) throws CohomanException {
		signupPizzaDao.delete(dto);
	}

	public void signupForPotluck(SignupPotluckDTO dto) throws CohomanException {
		signupPotluckDao.create(dto);
	}

	public List<SignupPotluckDTO> getAllPotluckSignups(Long eventid) throws CohomanException {
				
		return signupPotluckDao.getAllPotluckSignups(eventid);
	}

	public void deleteSignupForPotluck(SignupPotluckDTO dto) throws CohomanException {
		signupPotluckDao.delete(dto);
	}

	public List<List<MealScheduleText>> getCurrentMealScheduleRows() {

		// create MealScedule instance
		// do this each for idempotency????
		ConfigInfo configInfo = new ConfigInfo();
		Calendar firstMonday = new GregorianCalendar(
				configInfo.getMealPeriodStartYear(),
				configInfo.getMealPeriodStartMonth(),
				configInfo.getMealPeriodStartDay());
		Calendar lastSunday = new GregorianCalendar(
				configInfo.getMealPeriodEndYear(),
				configInfo.getMealPeriodEndMonth(),
				configInfo.getMealPeriodEndDay());

		mealSchedule.create(firstMonday, lastSunday);

		//return mealSchedule.getMealRows();
		return mealSchedule.getMealTextRowsForUI();
	}

	public List<MealDate> getMealDaysForPeriod() {
		List<MealDate> mealDates = new ArrayList<CalendarUtils.MealDate>();

		// Start with list of already scheduled meals. For each day
		// a meal is already scheduled, remove the time so we can
		// match. Also eliminate its "twin", i.e.
		// Sat or Sun => both Sat & Sun.
		List<MealEvent> mealEventList = mealEventDao.getMealEvents();
		List<Calendar> mealEventCalList = new ArrayList<Calendar>();
		for (MealEvent mealEvent : mealEventList) {
			Calendar mealCalDate = new GregorianCalendar();
			mealCalDate.setTime(mealEvent.getEventDate());
			mealCalDate.set(Calendar.HOUR_OF_DAY, 0);
			mealCalDate.set(Calendar.MINUTE, 0);
			mealCalDate.set(Calendar.SECOND, 0);
			mealCalDate.set(Calendar.MILLISECOND, 0);

			mealEventCalList.add(mealCalDate);

			// Now do "twin"
			Calendar mealCalDateTwin = (Calendar) mealCalDate.clone();
			if (mealCalDate.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY
					|| mealCalDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				// Wed. or Sat. means add 1
				mealCalDateTwin.add(Calendar.DAY_OF_MONTH, 1);
			} else {
				// Thurs. or Sun. means subtract 1
				mealCalDateTwin.add(Calendar.DAY_OF_MONTH, -1);
			}
			mealEventCalList.add(mealCalDateTwin);
		}

		ConfigInfo configInfo = new ConfigInfo();
		int year = configInfo.getMealPeriodStartYear();
		int month = configInfo.getMealPeriodStartMonth();
		int day = configInfo.getMealPeriodStartDay();
		int id = 1;

		Calendar theCalDate = new GregorianCalendar(year, month, day);
		Calendar theEndCalDate = new GregorianCalendar(
				configInfo.getMealPeriodEndYear(),
				configInfo.getMealPeriodEndMonth(),
				configInfo.getMealPeriodEndDay());
		// loop
		// while (!(theCalDate.equals(theEndCalDate))) {
		while (theCalDate.compareTo(theEndCalDate) <= 0) {

			// Eliminate non-meal days: Mon., Tues., Fri.
			int dayOfWeek = theCalDate.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek == Calendar.MONDAY || dayOfWeek == Calendar.TUESDAY
					|| dayOfWeek == Calendar.FRIDAY) {
				theCalDate.add(Calendar.DAY_OF_MONTH, 1);
				continue;
			}

			// Need to eliminate dates that already have scheduled
			// meals.
			if (mealEventCalList.contains(theCalDate)) {
				theCalDate.add(Calendar.DAY_OF_MONTH, 1);
				continue;
			}

			// We're going to show this date as free for a meal.
			MealDate mealDate = new MealDate();
			mealDate.setTimestamp(id++);
			mealDate.setTheYear(theCalDate.get(Calendar.YEAR));
			OneMonth oneMonth = new OneMonth(theCalDate.get(Calendar.MONDAY));
			mealDate.setTheMonth(oneMonth);
			mealDate.setTheDay(theCalDate.get(Calendar.DAY_OF_MONTH));
			Date theDate = theCalDate.getTime();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"EEEE, MMM. d, yyyy");
			mealDate.setPrintableDate(formatter.format(theDate.getTime()));
			mealDates.add(mealDate);

			theCalDate.add(Calendar.DAY_OF_MONTH, 1);
		}

		return mealDates;
	}

	public List<MealDate> getPizzaDaysForPeriod() {
		List<MealDate> mealDates = new ArrayList<MealDate>();

		// Start with list of already scheduled meals. For each day
		// a meal is already scheduled, remove the time so we can
		// match. Also eliminate its "twin", i.e.
		// Sat or Sun => both Sat & Sun.
		List<PizzaEvent> pizzaEventList = pizzaEventDao.getPizzaEvents();
		List<Calendar> pizzaEventCalList = new ArrayList<Calendar>();
		for (PizzaEvent pizzaEvent : pizzaEventList) {
			Calendar pizzaCalDate = new GregorianCalendar();
			pizzaCalDate.setTime(pizzaEvent.getEventDate());
			pizzaCalDate.set(Calendar.HOUR_OF_DAY, 0);
			pizzaCalDate.set(Calendar.MINUTE, 0);
			pizzaCalDate.set(Calendar.SECOND, 0);
			pizzaCalDate.set(Calendar.MILLISECOND, 0);

			pizzaEventCalList.add(pizzaCalDate);

		}

		ConfigInfo configInfo = new ConfigInfo();
		int year = configInfo.getMealPeriodStartYear();
		int month = configInfo.getMealPeriodStartMonth();
		int day = configInfo.getMealPeriodStartDay();
		int id = 1;

		Calendar theCalDate = new GregorianCalendar(year, month, day);
		Calendar theEndCalDate = new GregorianCalendar(
				configInfo.getMealPeriodEndYear(),
				configInfo.getMealPeriodEndMonth(),
				configInfo.getMealPeriodEndDay());
		// loop
		while (theCalDate.compareTo(theEndCalDate) <= 0) {

			// Sanity check for Monday
			int dayOfWeek = theCalDate.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek != Calendar.MONDAY) {
				throw new RuntimeException(
						"Improper start date for pizza/potlucks: not a Monday");
			}

			// Need to eliminate dates that already have scheduled
			// meals.
			if (pizzaEventCalList.contains(theCalDate)) {
				theCalDate.add(Calendar.DAY_OF_MONTH, 7);
				continue;
			}

			// We're going to show this date as free for a meal.
			MealDate mealDate = new MealDate();
			mealDate.setTimestamp(id++);
			mealDate.setTheYear(theCalDate.get(Calendar.YEAR));
			OneMonth oneMonth = new OneMonth(theCalDate.get(Calendar.MONDAY));
			mealDate.setTheMonth(oneMonth);
			mealDate.setTheDay(theCalDate.get(Calendar.DAY_OF_MONTH));
			Date theDate = theCalDate.getTime();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"EEEE, MMM. d, yyyy");
			mealDate.setPrintableDate(formatter.format(theDate.getTime()));
			mealDates.add(mealDate);

			theCalDate.add(Calendar.DAY_OF_MONTH, 7);
		}

		return mealDates;
	}

	public List<MealDate> getPotluckDaysForPeriod() {
		List<MealDate> mealDates = new ArrayList<CalendarUtils.MealDate>();

		// Start with list of already scheduled meals. For each day
		// a meal is already scheduled, remove the time so we can
		// match. First eliminate days where meals are alreadt scheduled.
		List<MealEvent> mealEventList = mealEventDao.getMealEvents();
		List<Calendar> anyMealEventCalList = new ArrayList<Calendar>();
		for (MealEvent mealEvent : mealEventList) {
			Calendar mealCalDate = new GregorianCalendar();
			mealCalDate.setTime(mealEvent.getEventDate());
			mealCalDate.set(Calendar.HOUR_OF_DAY, 0);
			mealCalDate.set(Calendar.MINUTE, 0);
			mealCalDate.set(Calendar.SECOND, 0);
			mealCalDate.set(Calendar.MILLISECOND, 0);

			anyMealEventCalList.add(mealCalDate);
		}

		// Next, eliminate days with pizza/potlucks already
		// scheduled.
		List<PizzaEvent> pizzaEventList = pizzaEventDao.getPizzaEvents();
		for (PizzaEvent pizzaEvent : pizzaEventList) {
			Calendar pizzaCalDate = new GregorianCalendar();
			pizzaCalDate.setTime(pizzaEvent.getEventDate());
			pizzaCalDate.set(Calendar.HOUR_OF_DAY, 0);
			pizzaCalDate.set(Calendar.MINUTE, 0);
			pizzaCalDate.set(Calendar.SECOND, 0);
			pizzaCalDate.set(Calendar.MILLISECOND, 0);

			anyMealEventCalList.add(pizzaCalDate);

		}

		// Lastly, eliminate days with potlucks already
		// scheduled.
		List<PotluckEvent> potluckEventList = potluckEventDao.getPotluckEvents();
		for (PotluckEvent potluckEvent : potluckEventList) {
			Calendar potluckCalDate = new GregorianCalendar();
			potluckCalDate.setTime(potluckEvent.getEventDate());
			potluckCalDate.set(Calendar.HOUR_OF_DAY, 0);
			potluckCalDate.set(Calendar.MINUTE, 0);
			potluckCalDate.set(Calendar.SECOND, 0);
			potluckCalDate.set(Calendar.MILLISECOND, 0);

			anyMealEventCalList.add(potluckCalDate);

		}

		ConfigInfo configInfo = new ConfigInfo();
		int year = configInfo.getMealPeriodStartYear();
		int month = configInfo.getMealPeriodStartMonth();
		int day = configInfo.getMealPeriodStartDay();
		int id = 1;

		Calendar theCalDate = new GregorianCalendar(year, month, day);
		Calendar theEndCalDate = new GregorianCalendar(
				configInfo.getMealPeriodEndYear(),
				configInfo.getMealPeriodEndMonth(),
				configInfo.getMealPeriodEndDay());
		// loop
		// while (!(theCalDate.equals(theEndCalDate))) {
		while (theCalDate.compareTo(theEndCalDate) <= 0) {

			// Need to eliminate dates that already have scheduled
			// meals.
			if (anyMealEventCalList.contains(theCalDate)) {
				theCalDate.add(Calendar.DAY_OF_MONTH, 1);
				continue;
			}

			// We're going to show this date as free for a meal.
			MealDate mealDate = new MealDate();
			mealDate.setTimestamp(id++);
			mealDate.setTheYear(theCalDate.get(Calendar.YEAR));
			OneMonth oneMonth = new OneMonth(theCalDate.get(Calendar.MONDAY));
			mealDate.setTheMonth(oneMonth);
			mealDate.setTheDay(theCalDate.get(Calendar.DAY_OF_MONTH));
			Date theDate = theCalDate.getTime();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"EEEE, MMM. d, yyyy");
			mealDate.setPrintableDate(formatter.format(theDate.getTime()));
			mealDates.add(mealDate);

			theCalDate.add(Calendar.DAY_OF_MONTH, 1);
		}

		return mealDates;
	}

	public List<MealEvent> getCurrentMealEvents() {
		return mealEventDao.getMealEvents();
	}

	public List<MealEvent> getAllMealEvents() {
		return mealEventDao.getAllMealEvents();
	}

	public MealEvent getMealEvent(Long eventId) {
		return mealEventDao.getMealEvent(eventId);
	}

	public List<PizzaEvent> getCurrentPizzaEvents() {
		return pizzaEventDao.getPizzaEvents();
	}

	public PizzaEvent getPizzaEvent(Long eventId) {
		return pizzaEventDao.getPizzaEvent(eventId);
	}

	public List<PotluckEvent> getCurrentPotluckEvents() {
		return potluckEventDao.getPotluckEvents();
	}

	public PotluckEvent getPotluckEvent(Long eventId) {
		return potluckEventDao.getPotluckEvent(eventId);
	}

	public List<CohoEvent> getCurrentCohoEvents() {
		return cohoEventDao.getCohoEvents();
	}

	public CohoEvent getCohoEvent(Long eventId) {
		return cohoEventDao.getCohoEvent(eventId);
	}

	public List<PrivateEvent> getMyPrivateEvents() {
		return privateEventDao.getMyPrivateEvents();
	}

	public List<PrivateEvent> getPendingPrivateEvents() {
		return privateEventDao.getPendingPrivateEvents();
	}

	public List<PrivateEvent> getAllPrivateEvents() {
		return privateEventDao.getAllPrivateEvents();
	}

	public List<PrivateEvent> getUpcomingPrivateEvents() {
		return privateEventDao.getUpcomingPrivateEvents();
	}

	public PrivateEvent getPrivateEvent(Long eventId) {
		return privateEventDao.getPrivateEvent(eventId);
	}


	// is this used???????
	public List<MainCalendarEvent> getMonthsCalendarEvents(Date theMonth) {
		List<MainCalendarEvent> mainCalendarEventList = new ArrayList<MainCalendarEvent>();
		List<MealEvent> mealEvents = mealEventDao.getMealEvents();
		for (MealEvent oneMealEvent : mealEvents) {
			MainCalendarEvent oneMainCalendarEvent = new MainCalendarEvent();
			oneMainCalendarEvent.setStartDate(oneMealEvent.getEventDate());
			oneMainCalendarEvent.setEndDate(oneMealEvent.getEventdateend());
			oneMainCalendarEvent.setEventType(EventTypeDefs.COMMONMEALTYPE);
			oneMainCalendarEvent.setEventInfo(oneMealEvent.getEventinfo());
			mainCalendarEventList.add(oneMainCalendarEvent);
		}
		if (!mainCalendarEventList.isEmpty()) {
			Collections.sort(mainCalendarEventList);
		}
		return mainCalendarEventList;
	}

	public List<MainCalendarEvent> getMainCalendarEventsForDay(Date dateOfDay) {
		// TODO make new list and sort by event date

		// Create the list of MealCalendarEvents to return
		List<MainCalendarEvent> mainCalendarEventsList = new ArrayList<MainCalendarEvent>();

		// Add meal events
		List<MealEvent> mealEventList = mealEventDao
				.getMealEventsForDay(dateOfDay);
		for (MealEvent oneMealEvent : mealEventList) {
			MainCalendarEvent oneMainCalendarEvent = new MainCalendarEvent();
			oneMainCalendarEvent.setEventType(oneMealEvent.getEventtype());
			oneMainCalendarEvent.setStartDate(oneMealEvent.getEventDate());
			oneMainCalendarEvent.setEndDate(oneMealEvent.getEventdateend());
			oneMainCalendarEvent.setEventInfo(oneMealEvent.getEventinfo());
			oneMainCalendarEvent.setEventlink("displayMealEvent.faces?MealEventId=" 
					+ oneMealEvent.getEventid());
			mainCalendarEventsList.add(oneMainCalendarEvent);
		}

		// Add pizza events
		List<PizzaEvent> pizzaEventList = pizzaEventDao
				.getPizzaEventsForDay(dateOfDay);
		for (PizzaEvent onePizzaEvent : pizzaEventList) {
			MainCalendarEvent oneMainCalendarEvent = new MainCalendarEvent();
			// Change on 12/09/2017: use eventName rather than meal type
			if (onePizzaEvent.getEventName() != null && 
					onePizzaEvent.getEventName().length() > 0) {
				oneMainCalendarEvent.setEventType(onePizzaEvent.getEventName());
			} else {
				oneMainCalendarEvent.setEventType(onePizzaEvent.getEventtype());
			}
			oneMainCalendarEvent.setStartDate(onePizzaEvent.getEventDate());
			oneMainCalendarEvent.setEndDate(onePizzaEvent.getEventdateend());
			oneMainCalendarEvent.setEventInfo(onePizzaEvent.getEventinfo());
			oneMainCalendarEvent.setEventlink("displayPizzaEvent.faces?PizzaEventId=" 
					+ onePizzaEvent.getEventid());
			mainCalendarEventsList.add(oneMainCalendarEvent);
		}

		// Add potluck events
		List<PotluckEvent> potluckEventList = potluckEventDao
				.getPotluckEventsForDay(dateOfDay);
		for (PotluckEvent onePotluckEvent : potluckEventList) {
			MainCalendarEvent oneMainCalendarEvent = new MainCalendarEvent();
			oneMainCalendarEvent.setEventType(onePotluckEvent.getEventtype());
			oneMainCalendarEvent.setStartDate(onePotluckEvent.getEventDate());
			oneMainCalendarEvent.setEndDate(onePotluckEvent.getEventdateend());
			oneMainCalendarEvent.setEventInfo(onePotluckEvent.getEventinfo());
			oneMainCalendarEvent.setEventlink("displayPotluckEvent.faces?PotluckEventId=" 
					+ onePotluckEvent.getEventid());
			mainCalendarEventsList.add(oneMainCalendarEvent);
		}

		// Add coho events
		List<CohoEvent> cohoEventList = cohoEventDao
				.getCohoEventsForDay(dateOfDay);
		for (CohoEvent oneCohoEvent : cohoEventList) {
			MainCalendarEvent oneMainCalendarEvent = new MainCalendarEvent();
			// Note: event name is used rather than event type for display
			oneMainCalendarEvent.setEventType(oneCohoEvent.getEventName());
			oneMainCalendarEvent.setStartDate(oneCohoEvent.getEventDate());
			oneMainCalendarEvent.setEndDate(oneCohoEvent.getEventdateend());
			oneMainCalendarEvent.setEventInfo(oneCohoEvent.getEventinfo());
			oneMainCalendarEvent.setEventName(oneCohoEvent.getEventName());
			oneMainCalendarEvent.setEventlink("displayCohoEvent.faces?CohoEventId=" 
					+ oneCohoEvent.getEventid());
			mainCalendarEventsList.add(oneMainCalendarEvent);
		}

		// Add private events
		List<PrivateEvent> privateEventList = privateEventDao
				.getPrivateEventsForDay(dateOfDay);
		for (PrivateEvent onePrivateEvent : privateEventList) {
			if (onePrivateEvent.getState().equalsIgnoreCase(
					EventStateEnums.APPROVED.name())) {
				MainCalendarEvent oneMainCalendarEvent = new MainCalendarEvent();
				// Note: event name is used rather than event type for display
				oneMainCalendarEvent.setEventType(onePrivateEvent
						.getEventName()); 
				oneMainCalendarEvent.setStartDate(onePrivateEvent
						.getEventDate());
				oneMainCalendarEvent.setEndDate(onePrivateEvent
						.getEventdateend());
				oneMainCalendarEvent.setEventInfo(onePrivateEvent
						.getEventinfo());
				oneMainCalendarEvent.setEventName(onePrivateEvent
						.getEventName());
				oneMainCalendarEvent.setEventlink("displayPrivateEvent.faces?PrivateEventId=" 
						+ onePrivateEvent.getEventid());
				mainCalendarEventsList.add(oneMainCalendarEvent);
			}
		}

		Collections.sort(mainCalendarEventsList);
		return mainCalendarEventsList;
	}

	public List<SpaceBean> getAllSpaces() {
		return spacesDao.getAllSpaces();
	}

	private void sendEmailToAddress(String emailAddress, String subject, String body) {
		SendEmail.sendEmailToAddress(emailAddress, subject, body);
	}

}
