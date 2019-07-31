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
import org.cohoman.model.dto.MealEventDTO;
import org.cohoman.model.dto.SignupMealDTO;
import org.cohoman.model.integration.persistence.beans.MealEvent;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.model.integration.persistence.beans.UnitBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.model.service.ConfigurationService;
import org.cohoman.model.service.EventService;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.CalendarUtils;

@ManagedBean
@SessionScoped
public class SignupMealController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4678206276499587830L;

	Logger logger = Logger.getLogger(this.getClass().getName());

	private List<MealEvent> mealEventList;
	private MealEvent chosenMealEvent;
	private String chosenMealEventString;
	private String signupOperation = "doSignup";
	private MealEventDTO mealEventDTO;
	private String slotNumber;
	private int numberattending = 1; // set default to 1
	private int maxnumberattending;
	private Long eventId;
	private String eventType;
	private String printableEventDate;
	private int totalPeopleAttending;
	private EventService eventService = null;
	private String chosenUserString;
	private UserService userService = null;
	private ConfigurationService configurationService = null;
	
	private void clearFormFields() {

		slotNumber = "";
		signupOperation = "doSignup";
		numberattending = 1;
		
		// Commented this out on 4/28/2017 because if a back-button
		// was used, chosenUserString gets cleared and in the case
		// of adding a user other than myself, we would get a
		// NPE since the new "other" user never resets since the
		// drop-down for a user never displays. So, with this line
		// commented out, we keep the same user which kinda makes sense
		// if you use a back-button and then try again.
		// chosenUserString = "";
		totalPeopleAttending = 0;
		// Make the user choose again
		// uh, this fails because somehow this gets called multiple times by JSF???
		//chosenMealEventString = null;
	}

	public String getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}

	public MealEventDTO getMealEventDTO() {
		return mealEventDTO;
	}

	public void setMealEventDTO(MealEventDTO mealEventDTO) {
		this.mealEventDTO = mealEventDTO;
	}

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public String getChosenMealEventString() {
		return chosenMealEventString;
	}

	public void setChosenMealEventString(String chosenMealEventString) {
		// 06/05/2019 resetting the meal always starts with a signup
		signupOperation = "doSignup";
		this.chosenMealEventString = chosenMealEventString;
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

	public Boolean getLeadCook() {

		// get the userid of the current user to compare against the lead cook
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(true);
		User dbUser = (User) session
				.getAttribute(AuthenticateController.SESSIONVAR_USER_NAME);

		// Get the chosen Meal Event so we can display correct information
		// about the Meal before submittal

		// Robustness check
		if (chosenMealEventString == null) {
			logger.log(Level.WARNING,
					"Internal Error: unable to find chosen meal event");
			return false;
		}

		// Special case hack: 1=> no meal has been set so just
		// assume this isn't a lead cook so page can be rendered.
		if (chosenMealEventString.equals("1")) {
			return false;
		}

		eventId = Long.valueOf(chosenMealEventString);
		MealEvent chosenMealEvent = eventService.getMealEvent(eventId);

		// Need to check for null value in case event was just deleted
		// (3/31/17).
		// In which case, simply conclude it's not the leader
		if (chosenMealEvent == null) {
			logger.log(Level.SEVERE,
					"Internal Error: unable to find meal event for mealId " + 
					eventId);
			return false;
		}

		// get lead cook based on chosen meal event
		// also decided to set maxnumberattending since we can just get it
		// from the chosen MealEvent (snuck this in on 3/26)
		maxnumberattending = chosenMealEvent.getMaxattendees();
		if (chosenMealEvent.getCook1() != null
				&& chosenMealEvent.getCook1() > 0) {
			if (dbUser.getUserid() == chosenMealEvent.getCook1()) {
				return true;
			}
		}

		// Also sneak in a check to allow the meal admin
		// to modify signups. (12/08/2017)
		// One other check to allow meal admin
		Role currentRole = (Role) session
				.getAttribute(AuthenticateController.SESSIONVAR_CHOSEN_ROLE);
		if (currentRole.getRoleid() == Role.MEALADMIN_ID) {
			return true;
		}

		return false;
	}

	public String getPrintableLeadCook() {

		// get lead cook based on chosen meal event
		if (chosenMealEventString == null || chosenMealEventString.isEmpty()) {
			return "Error: unable to get name of lead cook.";
		}
		
		eventId = Long.valueOf(chosenMealEventString);
		MealEvent chosenMealEvent = eventService.getMealEvent(eventId);
		User dbUser = userService.getUser(chosenMealEvent.getCook1());

		return dbUser.getFirstname() + " " + dbUser.getLastname();
	}

	public String getMenu() {

		// get lead cook based on chosen meal event
		if (chosenMealEventString == null || chosenMealEventString.isEmpty()) {
			return "Error: unable to get menu.";
		}

		eventId = Long.valueOf(chosenMealEventString);
		MealEvent chosenMealEvent = eventService.getMealEvent(eventId);
		return chosenMealEvent.getMenu();
	}

	public List<MealEvent> getMealEventList() {
		mealEventList = eventService.getCurrentMealEvents();

		// remove past meals so users can't signup for them
		mealEventList = filterOutPastMeals(mealEventList);

		// First time thru, set meal to first of list as that's what's
		// displayed.
		if (mealEventList != null && !mealEventList.isEmpty()
				&& chosenMealEventString == null) {
			chosenMealEventString = "1";
			//chosenMealEventString = mealEventList.get(0).getEventid()
					//.toString();
		}
		return mealEventList;
	}

	/*
	 * Returns the list of people signedup for the meal but sneaks in two
	 * calculations in the process. First it calculates the total number of
	 * meals being made. And then it stuffs in the unit number so that can
	 * appear on the display sheet.
	 */
	public List<SignupMealDTO> getAllMealSignups() throws CohomanException {

		// Sequence through the list to get a count of
		// people signed-up for the meal.
		List<SignupMealDTO> signupDTOlist = null;

		totalPeopleAttending = 0;
		if (eventId != null) {
			signupDTOlist = eventService.getAllMealSignups(eventId);
			for (SignupMealDTO oneDTO : signupDTOlist) {
				User dbUser = userService.getUser(oneDTO.getUserid());
				oneDTO.setUnitnumber(dbUser.getUnit());
			}
		}

		// Make sure there are actually some signups, else give up and return
		// null
		if (signupDTOlist == null || signupDTOlist.isEmpty()) {

			return null;
		}

		List<SignupMealDTO> finalSignupMealDTOList = new ArrayList<SignupMealDTO>();
		List<User> usersList = userService.getUsersHereNow();
		List<UnitBean> unitBeanList = configurationService.getAllUnits();
		for (UnitBean unitBean : unitBeanList) {

			// Make a basic SignupMealDTO for every unit containing just
			// unit number, food restrictions, and user name(s)
			SignupMealDTO oneSignupMealDTO = new SignupMealDTO();
			oneSignupMealDTO.setUnitnumber(unitBean.getUnitnumber());

			for (User oneUser : usersList) {
				if (oneUser.getUnit()
						.equalsIgnoreCase(unitBean.getUnitnumber())) {
					if (oneSignupMealDTO.getFoodrestrictions() == null
							|| oneSignupMealDTO.getFoodrestrictions().isEmpty()) {

						// No restrictions yet
						if (oneUser.getFoodrestrictions() != null
								&& !oneUser.getFoodrestrictions().isEmpty()) {
							oneSignupMealDTO.setFoodrestrictions("("
									+ oneUser.getFirstname().substring(0, 1)
									+ ")" + oneUser.getFoodrestrictions());
						}
					} else {

						// Add to an existing list of restrictions
						if (oneUser.getFoodrestrictions() != null
								&& !oneUser.getFoodrestrictions().isEmpty()) {
							oneSignupMealDTO
									.setFoodrestrictions(oneSignupMealDTO
											.getFoodrestrictions()
											+ ", ("
											+ oneUser.getFirstname().substring(
													0, 1)
											+ ")"
											+ oneUser.getFoodrestrictions());
						}
					}
					if (oneSignupMealDTO.getUsername() == null
							|| oneSignupMealDTO.getUsername().isEmpty()) {
						oneSignupMealDTO.setUsername(oneUser.getUsername());
					} else {
						oneSignupMealDTO.setUsername(oneSignupMealDTO
								.getUsername() + " & " + oneUser.getUsername());
					}
				}
			}

			// Now merge in actual info for those people who signed-up
			for (SignupMealDTO oneDTO : signupDTOlist) {
				if (oneDTO.getUnitnumber().equalsIgnoreCase(
						unitBean.getUnitnumber())) {
					oneSignupMealDTO.setNumberattending(oneDTO
							.getNumberattending());
				}
			}
			finalSignupMealDTOList.add(oneSignupMealDTO);
		}

		// 05/19/2017: figure count just before we return the list
		for (SignupMealDTO oneSignupMealDTO : finalSignupMealDTOList) {
			totalPeopleAttending += oneSignupMealDTO.getNumberattending();
		}
		return finalSignupMealDTOList;

	}

	public void setTotalPeopleAttendingExceptCurrentUser(Long currentUserid)
			throws CohomanException {

		// Sequence through the list to get a count of
		// people signed-up for the meal.
		List<SignupMealDTO> signupDTOlist = eventService
				.getAllMealSignups(eventId);
		totalPeopleAttending = 0;
		for (SignupMealDTO oneDTO : signupDTOlist) {
			if (oneDTO.getUserid() != currentUserid) {
				totalPeopleAttending += oneDTO.getNumberattending();
			}
		}
	}

	// Method for drop-down of number attending
	public List<String> getAttendingCount() {

		List<String> countList = new ArrayList<String>();
		for (int idx = 0; idx < 16; idx++) {
			countList.add(Integer.toString(idx));
		}
		return countList;
	}

	// Method for drop-down of max number attending
	public List<String> getMaxattendingCount() {

		List<String> countList = new ArrayList<String>();
		for (int idx = 5; idx < 76; idx++) {
			countList.add(Integer.toString(idx));
		}
		return countList;
	}

	public void setMealEventList(List<MealEvent> mealEventList) {
		this.mealEventList = mealEventList;
	}

	public MealEvent getChosenMealEvent() {
		return chosenMealEvent;
	}

	public void setChosenMealEvent(MealEvent chosenMealEvent) {
		this.chosenMealEvent = chosenMealEvent;
	}

	public CalendarUtils.TimeSlot[] getTimeSlotsOfTheDay() {
		return CalendarUtils.getTimeSlotsOfTheDay(2010, 1);
	}

	public int getNumberattending() {
		return numberattending;
	}

	public void setNumberattending(int numberattending) {
		this.numberattending = numberattending;
	}

	public int getMaxnumberattending() {
		return maxnumberattending;
	}

	public void setMaxnumberattending(int maxnumberattending) {
		this.maxnumberattending = maxnumberattending;
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

	public boolean getMealClosed() {

		// Make sure chosenMealEventString is populated and
		// if not, call getMealEventList() to populate it
		// the first time around.
		if (chosenMealEventString == null) {
			getMealEventList();
		}

		// Robustness check
		if (chosenMealEventString == null) {
			return true;
		}

		// Special case hack: 1=> no meal has been set so just
		// assume this isn't a lead cook so page can be rendered.
		if (chosenMealEventString.equals("1")) {
			return false;
		}

		eventId = Long.valueOf(chosenMealEventString);
		MealEvent chosenMealEvent = eventService.getMealEvent(eventId);

		// Need to check for null value in case event was just deleted
		// (3/31/17).
		// In which case, simply conclude the meal isn't closed.
		if (chosenMealEvent == null) {
			return false;
		}

		return chosenMealEvent.isIsmealclosed();
	}

	public boolean getMaxReached() {
		try {
			getAllMealSignups(); // do this to populate
									// totalPeopleAttending
		} catch (CohomanException ce) {
			return true; // / uh, make it show a problem for now (3/23/17)
		}
		
		// Make sure we've got the max number if there is one set
		// in the DB event entry before we check against it (06/10/2019)
		if (maxnumberattending == 0 && chosenMealEventString != null &&
				!chosenMealEventString.equals("1")) {				
			eventId = Long.valueOf(chosenMealEventString);
			chosenMealEvent = eventService.getMealEvent(eventId);
			maxnumberattending = chosenMealEvent.getMaxattendees();
		}

		if (maxnumberattending != 0
				&& totalPeopleAttending >= maxnumberattending) {
			return true;
		}
		return false;
	}

	public String signupMealView() throws CohomanException {

		// Get the chosen MealEvent
		if (chosenMealEventString == null || chosenMealEventString.length() == 0) {
			logger.log(Level.SEVERE,
					"Internal Error: invalid MealEventId parameter");
			FacesMessage message = new FacesMessage(
					"Internal Error: invalid MealEventId parameter. Click on Main Menu link.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		
		// Error check that a meal has been chosen
		if (chosenMealEventString.equalsIgnoreCase("1")) {
			FacesMessage message = new FacesMessage(
					"User Error: you must choose a meal to attend.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		eventId = Long.valueOf(chosenMealEventString);


		MealEvent chosenMealEvent = eventService.getMealEvent(eventId);
		if (chosenMealEvent == null) {
			logger.log(Level.SEVERE,
					"Internal Error: unable to find meal event for mealId " + 
					eventId);
			FacesMessage message = new FacesMessage(
					"Internal Error: unable to find meal event. Click on Main Menu link.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		eventType = chosenMealEvent.getEventtype();
		printableEventDate = chosenMealEvent.getPrintableEventDate();

		// Another hack added when we seem to lose the checked radio button
		// Special case 06/10/2019: if max attendees hit, set the operation
		// to max
		if (signupOperation == null || signupOperation.isEmpty()) {
			if (totalPeopleAttending == maxnumberattending) {
				// If already hit max, this is the only valid operation
				signupOperation = "setMaxAttendees";
			} else {
				signupOperation = "doSignup";
			}
		}

		// If the UI didn't just set the maxnumberattending (and it can't set it to
		// 0), read it from the DB for the event (where it may or may not
		// have been previously set).
		if (maxnumberattending == 0) {
			maxnumberattending = chosenMealEvent.getMaxattendees();
		}

		// Special hack to keep from doing a signup if all the radio buttons
		// are disabled except for list (because the meal has been marked
		// as closed), yet the signup is still checked.
		// So, all we can do is set to open, despite the signup being selected.
		if (getMealClosed() && !signupOperation.equalsIgnoreCase("openMealNow")) {

			return "openMealNow";
		}

		// Start making the DTO for use in doing a signup
		SignupMealDTO dto = new SignupMealDTO();
		dto.setEventid(eventId);
		dto.setNumberattending(numberattending);

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

		// Check that new signup doesn't exceed the max number attending
		// if such a value is set. But to do that, must extract the
		// maxnumberattending from the chosen MealEvent, unless we are in
		// the process of setting the max number attending.
		if (signupOperation.equalsIgnoreCase("doSignup")
				|| signupOperation.equalsIgnoreCase("doSignupSomeoneElse")) {
			if (maxnumberattending > 0) {
				setTotalPeopleAttendingExceptCurrentUser(dto.getUserid()); // do
																			// this
																			// to
																			// populate
																			// totalPeopleAttending
				if (totalPeopleAttending + numberattending > maxnumberattending) {
					FacesMessage message = new FacesMessage(
							"Error: Cannot signup. The maximum number of sign-ups for this meal has been exceeded.");
					message.setSeverity(FacesMessage.SEVERITY_ERROR);
					FacesContext.getCurrentInstance().addMessage(null, message);
					return null;
				}
			}
		}

		try {
			if (signupOperation.equalsIgnoreCase("doSignup")
					|| signupOperation.equalsIgnoreCase("doSignupSomeoneElse")) {
				// Always delete first, so create will always work.
				// Decided to have signup always work, so functionality
				// is that last signup always overwrites the previous signup.
				// (03/23/17)
				eventService.deleteSignupMeal(dto);
				if (numberattending > 0) {
					// call eventService with DTO
					eventService.signupMeal(dto);
				}
			} else if (signupOperation.equalsIgnoreCase("setMaxAttendees")) {
				chosenMealEvent.setMaxattendees(maxnumberattending);
				eventService.editMealEvent(chosenMealEvent);
			} else if (signupOperation.equalsIgnoreCase("closeMealNow")) {
				chosenMealEvent.setIsmealclosed(true);
				eventService.editMealEvent(chosenMealEvent);
			} else if (signupOperation.equalsIgnoreCase("openMealNow")) {
				chosenMealEvent.setIsmealclosed(false);
				eventService.editMealEvent(chosenMealEvent);
			}
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		// Save operation before clearing so can return the right operation
		String currentOperation = signupOperation;
		
		// clear out fields for return to the page in this session
		clearFormFields();

		return currentOperation;
	}

	public String listMealView() throws CohomanException {

		// Get the chosen MealEvent
		if (chosenMealEventString == null || chosenMealEventString.length() == 0) {
			logger.log(Level.SEVERE,
					"Internal Error: invalid MealEventId parameter");
			FacesMessage message = new FacesMessage(
					"Internal Error: invalid MealEventId parameter. Click on Main Menu link.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		
		// Error check that a meal has been chosen
		if (chosenMealEventString.equalsIgnoreCase("1")) {
			FacesMessage message = new FacesMessage(
					"User Error: you must choose a meal to attend.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		eventId = Long.valueOf(chosenMealEventString);


		MealEvent chosenMealEvent = eventService.getMealEvent(eventId);
		if (chosenMealEvent == null) {
			logger.log(Level.SEVERE,
					"Internal Error: unable to find meal event for mealId " + 
					eventId);
			FacesMessage message = new FacesMessage(
					"Internal Error: unable to find meal event. Click on Main Menu link.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		eventType = chosenMealEvent.getEventtype();
		printableEventDate = chosenMealEvent.getPrintableEventDate();



		// clear out fields for return to the page in this session
		clearFormFields();
		
		
		// Reset operation to signup.
		signupOperation = "doSignup";

		// But, return the string to display the table
		return "listSignups";
	}

	// Private method to remove meal events that shouldn't be displayed
	// to users in drop-downs since they've past.
	private List<MealEvent> filterOutPastMeals(List<MealEvent> mealList) {

		Calendar nowMinus1day = new GregorianCalendar();
		Calendar nowMinus7days = new GregorianCalendar();
		nowMinus1day.add(Calendar.DAY_OF_YEAR, -1);
		nowMinus7days.add(Calendar.DAY_OF_YEAR, -14);
		List<MealEvent> newMealEventList = new ArrayList<MealEvent>();
		for (MealEvent mealEvent : mealList) {
			Calendar mealEventCal = Calendar.getInstance();
			mealEventCal.setTime(mealEvent.getEventDate());

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
			User dbUser = userService.getUser(mealEvent.getCook1());

			if (!(currentRole.getRoleid() == Role.MEALADMIN_ID || dbUser
					.getUsername().equalsIgnoreCase(
							LoggingUtils.getCurrentUsername()))) {

				// Here if not a special user
				// If the date of the event is more than one day old, don't
				// let people access the meal by eliminating it from
				// the drop-down
				if (mealEventCal.before(nowMinus1day)) {
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
					if (mealEventCal.before(nowMinus7days)) {
						// continue => don't add the meal to the drop-down list
						continue;
					}
				}
			}

			newMealEventList.add(mealEvent);
		}

		return newMealEventList;
	}

}
