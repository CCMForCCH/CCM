package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.cohoman.model.business.User;
import org.cohoman.model.dto.SignupPotluckDTO;
import org.cohoman.model.dto.SignupPotluckRow;
import org.cohoman.model.integration.persistence.beans.PotluckEvent;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.model.integration.persistence.beans.UnitBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.model.service.ConfigurationService;
import org.cohoman.model.service.EventService;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.cohoman.view.controller.utils.PotluckCategoriesEnums;

@ManagedBean
@SessionScoped
public class SignupPotluckController implements Serializable {

	/**
	 * 
	 */
	Logger logger = Logger.getLogger(this.getClass().getName());

	private static final long serialVersionUID = 4678206276499587830L;

	private List<PotluckEvent> potluckEventList;
	private PotluckEvent chosenPotluckEvent;
	private String chosenPotluckEventString;
	private String signupOperation = "doSignup";
	private String slotNumber;
	private int numberattending = 1; // set default to 0
	private Long eventId;
	private String eventType;
	private PotluckCategoriesEnums itemtype;
	private String itemdescription = "";
	private String printableEventDate;
	private int totalPeopleAttending;
	private long lastPotluckListLoadedTime;

	private EventService eventService = null;
	private String chosenUserString;
	private UserService userService = null;
	private ConfigurationService configurationService = null;

	private void clearFormFields() {

		slotNumber = "";
		signupOperation = "doSignup";

		numberattending = 1; // set default to 0
		totalPeopleAttending = 0;

		// Commented this out on 4/28/2017 because if a back-button
		// was used, chosenUserString gets cleared and in the case
		// of adding a user other than myself, we would get a
		// NPE since the new "other" user never resets since the
		// drop-down for a user never displays. So, with this line
		// commented out, we keep the same user which kinda makes sense
		// if you use a back-button and then try again.
		// chosenUserString = "";
	}

	// 04/16/2020
	// Add this method to be called when a user deletes (or changes) a meal
	// so we don't use the cached value of the last chosen event since it
	// may no longer exist. Also, doesn't hurt to make them enter the
	// current event again.
	public void clearChosenEventString() {
		chosenPotluckEventString = "1";  
	}

	public String getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public String getChosenPotluckEventString() {
		return chosenPotluckEventString;
	}

	public void setChosenPotluckEventString(String chosenPotluckEventString) {
		// 06/05/2019 resetting the meal always starts with a signup
		signupOperation = "doSignup";
		this.chosenPotluckEventString = chosenPotluckEventString;
		logger.log(Level.INFO, "AUDIT: Setting chosenPotluckEventString to " + chosenPotluckEventString);
	}

	public String getSignupOperation() {
		return signupOperation;
	}

	public void setSignupOperation(String signupOperation) {
		this.signupOperation = signupOperation;
	}

	public int getTotalPeopleAttending() {
		return totalPeopleAttending;
	}

	public void setTotalPeopleAttending(int totalPeopleAttending) {
		this.totalPeopleAttending = totalPeopleAttending;
	}

	public String getChosenUserString() {
		return chosenUserString;
	}

	public void setChosenUserString(String chosenUserString) {
		this.chosenUserString = chosenUserString;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(
			ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public PotluckCategoriesEnums getItemtype() {
		return itemtype;
	}

	public void setItemtype(PotluckCategoriesEnums itemtype) {
		this.itemtype = itemtype;
	}

	public PotluckCategoriesEnums[] getItemtypes() {
		return PotluckCategoriesEnums.values();
	}

	public String getItemdescription() {
		return itemdescription;
	}

	public void setItemdescription(String itemdescription) {
		this.itemdescription = itemdescription;
	}

	public void setNumberattending(int numberattending) {
		this.numberattending = numberattending;
	}

	// probably completely replaced by getLeaders()
	// commented out for now (12/08/2017
	/*
	 * public Boolean getLeader1() {
	 * 
	 * // get the userid of the current user to compare against the lead cook
	 * FacesContext ctx = FacesContext.getCurrentInstance(); HttpSession session
	 * = (HttpSession) ctx.getExternalContext() .getSession(true); User dbUser =
	 * (User) session
	 * .getAttribute(AuthenticateController.SESSIONVAR_USER_NAME);
	 * 
	 * // Robustness check if (chosenPotluckEventString == null) { return false;
	 * }
	 * 
	 * // Get the chosen Meal Event so we can display correct information //
	 * about the Meal before submittal eventId =
	 * Long.valueOf(chosenPotluckEventString); PotluckEvent chosenPotluckEvent =
	 * eventService.getPotluckEvent(eventId);
	 * 
	 * // Need to check for null value in case event was just deleted //
	 * (3/31/17). // In which case, simply conclude it's not the leader if
	 * (chosenPotluckEvent == null) { return false; }
	 * 
	 * // get lead cook based on chosen meal event if
	 * (chosenPotluckEvent.getLeader1() != null &&
	 * chosenPotluckEvent.getLeader1() > 0) { if (dbUser.getUserid() ==
	 * chosenPotluckEvent.getLeader1()) { return true; } }
	 * 
	 * return false; }
	 */

	// A replacement for getLeader1() which will allow any of
	// leader1, leader2, or meal admin to signup other users
	// 12/08/2017
	public Boolean getLeaders() {

		// get the userid of the current user for comparisons
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(true);
		User dbUser = (User) session
				.getAttribute(AuthenticateController.SESSIONVAR_USER_NAME);

		// Robustness check
		if (chosenPotluckEventString == null) {
			return false;
		}

		// Special case hack: 1=> no meal has been set so just
		// assume this isn't a leader so page can be rendered.
		if (chosenPotluckEventString.equals("1")) {
			return false;
		}

		// Get the chosen Meal Event so we can display correct information
		// about the Meal before submittal
		eventId = Long.valueOf(chosenPotluckEventString);
		PotluckEvent chosenPotluckEvent = eventService.getPotluckEvent(eventId);

		// Need to check for null value in case event was just deleted
		// (3/31/17).
		// In which case, simply conclude it's not the leader
		if (chosenPotluckEvent == null) {
			return false;
		}

		// First, allow either leader1 or leader2
		if (chosenPotluckEvent.getLeader1() != null
				&& chosenPotluckEvent.getLeader1() > 0) {
			if (dbUser.getUserid() == chosenPotluckEvent.getLeader1()) {
				return true;
			}
		}
		if (chosenPotluckEvent.getLeader2() != null
				&& chosenPotluckEvent.getLeader2() > 0) {
			if (dbUser.getUserid() == chosenPotluckEvent.getLeader2()) {
				return true;
			}
		}

		// One other check to allow meal admin
		Role currentRole = (Role) session
				.getAttribute(AuthenticateController.SESSIONVAR_CHOSEN_ROLE);
		if (currentRole.getRoleid() == Role.MEALADMIN_ID) {
			return true;
		}

		return false;
	}

	public String getPrintableLeader1() {

		// get lead cook based on chosen meal event
		if (chosenPotluckEventString == null
				|| chosenPotluckEventString.isEmpty()
				|| chosenPotluckEventString.equals("1")) {  // 04/11/2020) {
			return "Error: unable to get name of team leader.";
		}

		eventId = Long.valueOf(chosenPotluckEventString);
		PotluckEvent chosenPotluckEvent = eventService.getPotluckEvent(eventId);
		User dbUser = userService.getUser(chosenPotluckEvent.getLeader1());

		return dbUser.getFirstname() + " " + dbUser.getLastname();
	}

	public List<PotluckEvent> getPotluckEventList() {

		// Only go to the database once per page. Do this by setting up
		// a cache of sorts. If more than 500 ms have passed since last
		// access to the database, read it in again. Otherwise, just
		// return the list we got from the database before.

		Logger logger = Logger.getLogger(this.getClass().getName());
		if (potluckEventList == null) {
			logger.info("AUDIT: inside getPotluckEventList() with getPotluckEventList being null.");
		} else {
			//logger.info("AUDIT: inside getPotluckEventList() with isEmpty = "
			//		+ potluckEventList.isEmpty());
		}

		Calendar calNow = Calendar.getInstance();
		long currentTimeInMS = calNow.getTimeInMillis();

		// If the list is empty or the 2 times differ by more
		// than 500 ms., read from the database. Otherwise, use
		// the list we just recently got from the database.
		long timeDiff = (currentTimeInMS - lastPotluckListLoadedTime);
		lastPotluckListLoadedTime = currentTimeInMS;
		//logger.info("AUDIT: inside getPotluckEventList() with timediff1 = "
		//		+ timeDiff);

		if (potluckEventList == null || potluckEventList.isEmpty()
				|| timeDiff > 500L) {

			logger.info("AUDIT: inside getPotluckEventList() with timediff2 = "
					+ timeDiff);

			// Get list of all potluck events from database.

			potluckEventList = eventService.getCurrentPotluckEvents();

			// Filter out past meals for all users except for the
			// meal admin who sees all for the period
			potluckEventList = filterOutPastMeals(potluckEventList);

		}
		
		// First time thru, set meal to first of list as that's what's
		// displayed.
		if (potluckEventList != null && !potluckEventList.isEmpty()
				&& chosenPotluckEventString == null) {
			chosenPotluckEventString = "1"; // 12/29/18 choose
			// chosenPotluckEventString =
			// potluckEventList.get(0).getEventid()
			// .toString();
		}

		// If we're returning an empty PizzaEvent list, set
		// chosenPizzaEventString to null to eliminate any stale value
		// since no existing value makes sense if we have
		// no list to return (04/10/2020)
		if (potluckEventList == null || potluckEventList.isEmpty()) {
			chosenPotluckEventString = null;
		}
		return potluckEventList;
	}

	/*
	 * Returns the list of people signedup for the meal but sneaks in two
	 * calculations in the process. First it calculates the total number of
	 * meals being made. And then it stuffs in the unit number so that can
	 * appear on the display sheet.
	 */
	public List<SignupPotluckDTO> getAllPotluckSignups()
			throws CohomanException {

		// Sequence through the list to get a count of
		// people signed-up for the meal.
		List<SignupPotluckDTO> signupDTOlist = null;
		totalPeopleAttending = 0;

		if (eventId != null) {
			signupDTOlist = eventService.getAllPotluckSignups(eventId);
			for (SignupPotluckDTO oneDTO : signupDTOlist) {
				User dbUser = userService.getUser(oneDTO.getUserid());
				oneDTO.setUnitnumber(dbUser.getUnit());
			}
		}

		// Make sure there are actually some signups, else give up and return
		// null
		if (signupDTOlist == null || signupDTOlist.isEmpty()) {
			return null;
		}

		List<SignupPotluckDTO> finalSignupPotluckDTOList = new ArrayList<SignupPotluckDTO>();
		List<User> usersList = userService.getUsersHereNow();
		List<UnitBean> unitBeanList = configurationService.getAllUnits();
		for (UnitBean unitBean : unitBeanList) {

			// Make a basic SignupPotluckDTO for every unit containing just
			// unit number and user name(s)
			SignupPotluckDTO oneSignupPotluckDTO = new SignupPotluckDTO();
			oneSignupPotluckDTO.setUnitnumber(unitBean.getUnitnumber());

			for (User oneUser : usersList) {
				if (oneUser.getUnit()
						.equalsIgnoreCase(unitBean.getUnitnumber())) {
					if (oneSignupPotluckDTO.getUsername() == null
							|| oneSignupPotluckDTO.getUsername().isEmpty()) {
						oneSignupPotluckDTO.setUsername(oneUser.getUsername());
					} else {
						oneSignupPotluckDTO.setUsername(oneSignupPotluckDTO
								.getUsername() + " & " + oneUser.getUsername());
					}
				}
			}

			// Now merge in actual info for those people who signed-up
			for (SignupPotluckDTO oneDTO : signupDTOlist) {
				if (oneDTO.getUnitnumber().equalsIgnoreCase(
						unitBean.getUnitnumber())) {

					// Carefully merge in values if multiple users sign up
					// from same unit.
					if (oneSignupPotluckDTO.getNumberattending() == 0) {
						oneSignupPotluckDTO.setNumberattending(oneDTO
								.getNumberattending());
					} else {
						oneSignupPotluckDTO
								.setNumberattending(oneSignupPotluckDTO
										.getNumberattending()
										+ oneDTO.getNumberattending());
					}
					oneSignupPotluckDTO.setItemdescription(oneDTO
							.getItemdescription());
				}
			}
			finalSignupPotluckDTOList.add(oneSignupPotluckDTO);

		}

		// Compute totals now that we're done making the individual unit counts.
		for (SignupPotluckDTO localSignupPotluckDTO : finalSignupPotluckDTOList) {
			totalPeopleAttending += localSignupPotluckDTO.getNumberattending();
		}

		return finalSignupPotluckDTOList;

	}

	// Method for displaying tables of dishes
	public List<SignupPotluckRow> getAllPotluckDishSignups()
			throws CohomanException {

		// Lists of dishes for each category
		List<String> entreesList = new ArrayList<String>();
		List<String> sidesList = new ArrayList<String>();
		List<String> saladsList = new ArrayList<String>();
		List<String> dessertsList = new ArrayList<String>();
		List<String> othersList = new ArrayList<String>();

		// Sequence through the signup list to build a list of each
		// meal category. Start with the DTO's from the signup table.
		List<SignupPotluckDTO> signupDTOlist = null;

		if (eventId != null) {
			signupDTOlist = eventService.getAllPotluckSignups(eventId);
			if (signupDTOlist == null || signupDTOlist.isEmpty()) {
				return null;
			}
			for (SignupPotluckDTO oneDTO : signupDTOlist) {
				User dbUser = userService.getUser(oneDTO.getUserid());
				String userString = "(" + dbUser.getUsername() + ")";

				// Add each signup to the appropriate list
				switch (Enum.valueOf(PotluckCategoriesEnums.class,
						oneDTO.getItemtype())) {

				case ENTREES:
					entreesList.add(oneDTO.getItemdescription() + userString);
					break;

				case SIDES:
					sidesList.add(oneDTO.getItemdescription() + userString);
					break;

				case SALADS:
					saladsList.add(oneDTO.getItemdescription() + userString);
					break;

				case DESSERTS:
					dessertsList.add(oneDTO.getItemdescription() + userString);
					break;

				case OTHERS:
					othersList.add(oneDTO.getItemdescription() + userString);
					break;

				default:
					throw new CohomanException(
							"Error: Potluck Category Enum value isn't handled.");

				}
			}

			// Now we have lists by itemtypes. Next make rows, with each row
			// having
			// an entry for each type.
			boolean entries_in_lists = true;
			int rowIdx = 0;
			List<SignupPotluckRow> allRows = new ArrayList<SignupPotluckRow>();
			while (entries_in_lists) {
				SignupPotluckRow oneRow = new SignupPotluckRow();
				if (rowIdx < entreesList.size()) {
					oneRow.setEntree(entreesList.get(rowIdx));
				}
				if (rowIdx < sidesList.size()) {
					oneRow.setSide(sidesList.get(rowIdx));
				}
				if (rowIdx < saladsList.size()) {
					oneRow.setSalad(saladsList.get(rowIdx));
				}
				if (rowIdx < dessertsList.size()) {
					oneRow.setDessert(dessertsList.get(rowIdx));
				}
				if (rowIdx < othersList.size()) {
					oneRow.setOther(othersList.get(rowIdx));
				}
				allRows.add(oneRow);
				rowIdx++;
				if (!(rowIdx < entreesList.size() || rowIdx < sidesList.size()
						|| rowIdx < saladsList.size()
						|| rowIdx < dessertsList.size() || rowIdx < othersList
						.size())) {
					entries_in_lists = false;
				}
			}
			return allRows;
		}
		// TODO??
		return null;
	}

	// Method for drop-down of number attending
	public List<String> getAttendingCount() {

		List<String> countList = new ArrayList<String>();
		for (int idx = 0; idx < 16; idx++) {
			countList.add(Integer.toString(idx));
		}
		return countList;
	}

	public void setPotluckEventList(List<PotluckEvent> potluckEventList) {
		this.potluckEventList = potluckEventList;
	}

	public PotluckEvent getChosenPotluckEvent() {
		return chosenPotluckEvent;
	}

	public void setChosenPotluckEvent(PotluckEvent chosenPotluckEvent) {
		this.chosenPotluckEvent = chosenPotluckEvent;
	}

	public CalendarUtils.TimeSlot[] getTimeSlotsOfTheDay() {
		return CalendarUtils.getTimeSlotsOfTheDay(2010, 1);
	}

	public int getNumberattending() {
		return numberattending;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getPrintableEventDate() {
		return printableEventDate;
	}

	public void setPrintableEventDate(String printableEventDate) {
		this.printableEventDate = printableEventDate;
	}

	public List<User> getUserList() {
		List<User> fullUserList = userService.getUsersHereNow();
		// First time thru, set user to first of list as that's what's
		// displayed.
		if (chosenUserString == null) {
			chosenUserString = fullUserList.get(0).getUserid().toString();
		}

		return fullUserList;
	}

	public String listPotluckAttendeesView() throws CohomanException {
		
		signupOperation = "listSignups";
		return signupPotluckView();
	}

	public String listPotluckDishesView() throws CohomanException {
		
		signupOperation = "listDishes";
		return signupPotluckView();
	}

	public String signupPotluckView() throws CohomanException {

		// Get the chosen MealEvent
		if (chosenPotluckEventString == null || chosenPotluckEventString.length() == 0) {
			logger.log(Level.SEVERE,
					"Internal Error: invalid PotluckEventId parameter");
			FacesMessage message = new FacesMessage(
					"Internal Error: invalid PotluckEventId parameter. Click on Main Menu link.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		
		// Error check that a potluck has been chosen
		if (chosenPotluckEventString.equalsIgnoreCase("1")) {
			FacesMessage message = new FacesMessage(
					"User Error: you must choose a potluck to attend or list.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		// Error check that a potluck item type has been chosen iff
		// this is a a real signup operation which implies that
		// an itemtype has been set.
		if ((signupOperation.equalsIgnoreCase("doSignupSomeoneElse") ||
				signupOperation.equalsIgnoreCase("doSignup")) && 
				(itemtype.name().equalsIgnoreCase("CHOOSE"))) {
			FacesMessage message = new FacesMessage(
					"User Error: you must choose the category of dish you're bringing.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		// Get the chosen MealEvent
		eventId = Long.valueOf(chosenPotluckEventString);
		PotluckEvent chosenPotluckEvent = eventService.getPotluckEvent(eventId);
		eventType = chosenPotluckEvent.getEventtype();
		printableEventDate = chosenPotluckEvent.getPrintableEventDate();

		// Another hack added when we seem to lose the checked radio button
		if (signupOperation == null || signupOperation.isEmpty()) {
			signupOperation = "listSignups";
		}

		// The rest of the logic here has to do with signing-up a user.
		// So, if it's just a list operation, cut out now.
		if (signupOperation.equalsIgnoreCase("listSignups")
				|| signupOperation.equalsIgnoreCase("listDishes")) {
			// clear out fields for return to the page in this session
			String signupOperationTmp = signupOperation;
			clearFormFields();
			return signupOperationTmp;
		}

		// Start making the DTO for use in doing a signup
		SignupPotluckDTO dto = new SignupPotluckDTO();

		dto.setEventid(eventId);
		dto.setNumberattending(numberattending);

		dto.setItemtype(itemtype.name());

		// require an item description unless the number attending
		// is zero. So complain if its not there.
		if (dto.getNumberattending() > 0
				&& (itemdescription == null || itemdescription.isEmpty())) {
			FacesMessage message = new FacesMessage(
					"You need to enter the description of the item you're bringing.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		dto.setItemdescription(itemdescription);

		// Determine the user being signed-up
		if (signupOperation.equalsIgnoreCase("doSignupSomeoneElse")) {
			// First check for a slow ajax race condition where the
			// chosenUserString is not yet populated before the form
			// is submitted.
			if (chosenUserString == null || chosenUserString.length() < 1) {
				FacesMessage message = new FacesMessage(
						"Error: The someone else user has not been set. Please try again.");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, message);
				return null;
			}

			// user chosen in the UI (lead cook only)
			Long userId = Long.parseLong(chosenUserString);
			dto.setUserid(userId);
		} else {
			// get the userid of the current user to set the requester.
			FacesContext ctx = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) ctx.getExternalContext()
					.getSession(true);
			User dbUser = (User) session
					.getAttribute(AuthenticateController.SESSIONVAR_USER_NAME);
			dto.setUserid(dbUser.getUserid());
		}

		try {
			if (signupOperation.equalsIgnoreCase("doSignup")
					|| signupOperation.equalsIgnoreCase("doSignupSomeoneElse")) {
				// Always delete first, so create will always work.
				// Decided to have signup always work, so functionality
				// is that last signup always overwrites the previous signup.
				// (03/23/17)
				eventService.deleteSignupPotluck(dto);
				if (numberattending > 0) {
					// call eventService with DTO
					eventService.signupPotluck(dto);
				}
			}

		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		// clearFormFields() resets signupOperation so save it to return here
		String signupOperationTmp = signupOperation; // clearFormFields resets
														// signupOperation

		// clear out fields for return to the page in this session
		clearFormFields();

		return signupOperationTmp;
	}

	// Private method to remove meal events that shouldn't be displayed
	// to users in drop-downs since they've past.
	private List<PotluckEvent> filterOutPastMeals(List<PotluckEvent> potluckList) {

		Calendar nowMinus1day = new GregorianCalendar();
		Calendar nowMinus7days = new GregorianCalendar();
		nowMinus1day.add(Calendar.DAY_OF_YEAR, -1);
		nowMinus7days.add(Calendar.DAY_OF_YEAR, -7);
		List<PotluckEvent> newPotluckEventList = new ArrayList<PotluckEvent>();
		for (PotluckEvent potluckEvent : potluckList) {
			Calendar potluckEventCal = Calendar.getInstance();
			potluckEventCal.setTime(potluckEvent.getEventDate());

			FacesContext ctx = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) ctx.getExternalContext()
					.getSession(true);
			Role currentRole = (Role) session
					.getAttribute(AuthenticateController.SESSIONVAR_CHOSEN_ROLE);

			// Don't filter anything if role is Admin user. But
			// otherwise make other checks to only return meal
			// events that a user might reasonably edit/delete. Also,
			// give the Meal Leader 7 days after the meal to make
			// changes.
			User dbUser = userService.getUser(potluckEvent.getLeader1());

			if (!(currentRole.getRoleid() == Role.MEALADMIN_ID || dbUser
					.getUsername().equalsIgnoreCase(
							LoggingUtils.getCurrentUsername()))) {

				// Here if not a special user
				// If the date of the event is more than one day old, don't
				// let people access the meal by eliminating it from
				// the drop-down
				if (potluckEventCal.before(nowMinus1day)) {
					// continue => don't add the meal to the drop-down list
					continue;
				}
			} else {

				// Here if Meal Leader or MEAL ADMIN
				if (dbUser.getUsername().equalsIgnoreCase(
						LoggingUtils.getCurrentUsername())
						&& currentRole.getRoleid() != Role.MEALADMIN_ID) {

					// Here only if Meal Leader. Need to filter out meals
					// that have occurred more than 7 days ago.
					if (potluckEventCal.before(nowMinus7days)) {
						// continue => don't add the meal to the drop-down list
						continue;
					}
				}
			}
			
			// Add the meal to the drop-down list
			newPotluckEventList.add(potluckEvent);
		}

		return newPotluckEventList;
	}

}
