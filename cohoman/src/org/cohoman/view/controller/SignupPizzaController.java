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
import org.cohoman.model.dto.PizzaEventDTO;
import org.cohoman.model.dto.SignupMealDTO;
import org.cohoman.model.dto.SignupPizzaDTO;
import org.cohoman.model.integration.persistence.beans.MealEvent;
import org.cohoman.model.integration.persistence.beans.PizzaEvent;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.model.integration.persistence.beans.UnitBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.model.service.ConfigurationService;
import org.cohoman.model.service.EventService;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.CalendarUtils;

@ManagedBean
@SessionScoped
public class SignupPizzaController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4678206276499587830L;
	
	Logger logger = Logger.getLogger(this.getClass().getName());

	private List<PizzaEvent> pizzaEventList;
	private PizzaEvent chosenPizzaEvent;
	private String chosenPizzaEventString;
	private String signupOperation = "doSignup";
	private PizzaEventDTO pizzaEventDTO;
	private String slotNumber;
	private int numberattendingpizza = 1; // set default to 0
	private int numberattendingpotluck = 0; // set default to 0
	private String pizzatopping1;
	private String pizzatopping2;
	private String pizzawillbring;
	private Long eventId;
	private String eventType;
	private String printableEventDate;
	private int totalPeopleAttending;
	private int totalPizzaMeals;
	private int totalPotluckMeals;

	private EventService eventService = null;
	private String chosenUserString;
	private UserService userService = null;
	private ConfigurationService configurationService = null;

	private void clearFormFields() {

		slotNumber = "";
		if (getMealClosed()) {
			signupOperation = "listSignups";
		} else {
			signupOperation = "doSignup";
		}
		numberattendingpizza = 1; // set default to 0
		numberattendingpotluck = 0; // set default to 0
		pizzatopping1 = "";
		pizzatopping2 = "";
		pizzawillbring = "";
		totalPeopleAttending = 0;
		totalPizzaMeals = 0;
		totalPotluckMeals = 0;
		// Commented this out on 4/28/2017 because if a back-button
		// was used, chosenUserString gets cleared and in the case
		// of adding a user other than myself, we would get a
		// NPE since the new "other" user never resets since the
		// drop-down for a user never displays. So, with this line
		// commented out, we keep the same user which kinda makes sense
		// if you use a back-button and then try again.
		// chosenUserString = "";
	}

	public String getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(String slotNumber) {
		this.slotNumber = slotNumber;
	}

	public PizzaEventDTO getPizzaEventDTO() {
		return pizzaEventDTO;
	}

	public void setPizzaEventDTO(PizzaEventDTO pizzaEventDTO) {
		this.pizzaEventDTO = pizzaEventDTO;
	}

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public String getChosenPizzaEventString() {
		return chosenPizzaEventString;
	}

	public void setChosenPizzaEventString(String chosenPizzaEventString) {
		this.chosenPizzaEventString = chosenPizzaEventString;
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

	public int getTotalPizzaMeals() {
		return totalPizzaMeals;
	}

	public void setTotalPizzaMeals(int totalPizzaMeals) {
		this.totalPizzaMeals = totalPizzaMeals;
	}

	public int getTotalPotluckMeals() {
		return totalPotluckMeals;
	}

	public void setTotalPotluckMeals(int totalPotluckMeals) {
		this.totalPotluckMeals = totalPotluckMeals;
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

	// Seems to be completely replaced satisfactorily by
	// getleaders(). Comment out for now (12/08/2017)
/*
	public Boolean getLeader1() {

		// get the userid of the current user to compare against the lead cook
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(true);
		User dbUser = (User) session
				.getAttribute(AuthenticateController.SESSIONVAR_USER_NAME);

		// Robustness check
		if (chosenPizzaEventString == null) {
			return false;
		}

		// Get the chosen Meal Event so we can display correct information
		// about the Meal before submittal
		eventId = Long.valueOf(chosenPizzaEventString);
		PizzaEvent chosenPizzaEvent = eventService.getPizzaEvent(eventId);

		// Need to check for null value in case event was just deleted
		// (3/31/17).
		// In which case, simply conclude it's not the leader
		if (chosenPizzaEvent == null) {
			return false;
		}

		// get lead cook based on chosen meal event
		if (chosenPizzaEvent.getLeader1() != null
				&& chosenPizzaEvent.getLeader1() > 0) {
			if (dbUser.getUserid() == chosenPizzaEvent.getLeader1()) {
				return true;
			}
		}

		return false;
	}
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
		if (chosenPizzaEventString == null) {
			return false;
		}

		// Special case hack: 1=> no meal has been set so just
		// assume this isn't a leader so page can be rendered.
		if (chosenPizzaEventString.equals("1")) {
			return false;
		}
		
		// Get the chosen Meal Event so we can display correct information
		// about the Meal before submittal
		eventId = Long.valueOf(chosenPizzaEventString);
		PizzaEvent chosenPizzaEvent = eventService.getPizzaEvent(eventId);

		// Need to check for null value in case event was just deleted
		// (3/31/17).
		// In which case, simply conclude it's not the leader
		if (chosenPizzaEvent == null) {
			return false;
		}

		// First, allow either leader1 or leader2
		if (chosenPizzaEvent.getLeader1() != null
				&& chosenPizzaEvent.getLeader1() > 0) {
			if (dbUser.getUserid() == chosenPizzaEvent.getLeader1()) {
				return true;
			}
		}
		if (chosenPizzaEvent.getLeader2() != null
				&& chosenPizzaEvent.getLeader2() > 0) {
			if (dbUser.getUserid() == chosenPizzaEvent.getLeader2()) {
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
		if (chosenPizzaEventString == null || chosenPizzaEventString.isEmpty()) {
			return "Error: unable to get name of team leader.";
		}

		eventId = Long.valueOf(chosenPizzaEventString);
		PizzaEvent chosenPizzaEvent = eventService.getPizzaEvent(eventId);
		User dbUser = userService.getUser(chosenPizzaEvent.getLeader1());

		return dbUser.getFirstname() + " " + dbUser.getLastname();
	}

	public List<PizzaEvent> getPizzaEventList() {
		pizzaEventList = eventService.getCurrentPizzaEvents();

		// Filter out past meals for all users except for the
		// meal admin who sees all for the period
		pizzaEventList = filterOutPastMeals(pizzaEventList);

		// First time thru, set meal to first of list as that's what's
		// displayed.
		if (pizzaEventList != null && !pizzaEventList.isEmpty()
				&& chosenPizzaEventString == null) {
			chosenPizzaEventString = "1";  //12/29/18 choose
			//chosenPizzaEventString = pizzaEventList.get(0).getEventid()
					//.toString();
		}
		return pizzaEventList;
		
	}

	/*
	 * Returns the list of people signedup for the meal but sneaks in two
	 * calculations in the process. First it calculates the total number of
	 * meals being made. And then it stuffs in the unit number so that can
	 * appear on the display sheet.
	 */
	public List<SignupPizzaDTO> getAllPizzaSignups() throws CohomanException {

		// Sequence through the list to get a count of
		// people signed-up for the meal.
		List<SignupPizzaDTO> signupDTOlist = null;
		totalPeopleAttending = 0;
		totalPizzaMeals = 0;
		totalPotluckMeals = 0;

		if (eventId != null) {
			signupDTOlist = eventService.getAllPizzaSignups(eventId);
			for (SignupPizzaDTO oneDTO : signupDTOlist) {
				User dbUser = userService.getUser(oneDTO.getUserid());
				oneDTO.setUnitnumber(dbUser.getUnit());
			}
		}

		// Make sure there are actually some signups, else give up and return
		// null
		if (signupDTOlist == null || signupDTOlist.isEmpty()) {
			return null;
		}

		List<SignupPizzaDTO> finalSignupPizzaDTOList = new ArrayList<SignupPizzaDTO>();
		List<User> usersList = userService.getUsersHereNow();
		List<UnitBean> unitBeanList = configurationService.getAllUnits();
		for (UnitBean unitBean : unitBeanList) {

			// Make a basic SignupPizzaDTO for every unit containing just
			// unit number, food restrictions, and user name(s)
			SignupPizzaDTO oneSignupPizzaDTO = new SignupPizzaDTO();
			oneSignupPizzaDTO.setUnitnumber(unitBean.getUnitnumber());

			for (User oneUser : usersList) {
				if (oneUser.getUnit()
						.equalsIgnoreCase(unitBean.getUnitnumber())) {
					if (oneSignupPizzaDTO.getFoodrestrictions() == null
							|| oneSignupPizzaDTO.getFoodrestrictions()
									.isEmpty()) {

						// No restrictions yet
						if (oneUser.getFoodrestrictions() != null
								&& !oneUser.getFoodrestrictions().isEmpty()) {
							oneSignupPizzaDTO.setFoodrestrictions("("
									+ oneUser.getFirstname().substring(0, 1)
									+ ")" + oneUser.getFoodrestrictions());
						}
					} else {

						// Add to an existing list of restrictions
						if (oneUser.getFoodrestrictions() != null
								&& !oneUser.getFoodrestrictions().isEmpty()) {
							oneSignupPizzaDTO
									.setFoodrestrictions(oneSignupPizzaDTO
											.getFoodrestrictions()
											+ ", ("
											+ oneUser.getFirstname().substring(
													0, 1)
											+ ")"
											+ oneUser.getFoodrestrictions());
						}
					}
					if (oneSignupPizzaDTO.getUsername() == null
							|| oneSignupPizzaDTO.getUsername().isEmpty()) {
						oneSignupPizzaDTO.setUsername(oneUser.getUsername());
					} else {
						oneSignupPizzaDTO.setUsername(oneSignupPizzaDTO
								.getUsername() + " & " + oneUser.getUsername());
					}
				}
			}

			// Now merge in actual info for those people who signed-up
			for (SignupPizzaDTO oneDTO : signupDTOlist) {
				if (oneDTO.getUnitnumber().equalsIgnoreCase(
						unitBean.getUnitnumber())) {

					if (oneSignupPizzaDTO.getPizzatopping1() == null
							|| oneSignupPizzaDTO.getPizzatopping1().isEmpty()) {
						oneSignupPizzaDTO.setPizzatopping1(oneDTO
								.getPizzatopping1());
					}
					if (oneSignupPizzaDTO.getPizzatopping2() == null
							|| oneSignupPizzaDTO.getPizzatopping2().isEmpty()) {
						oneSignupPizzaDTO.setPizzatopping2(oneDTO
								.getPizzatopping2());
					}
					if (oneSignupPizzaDTO.getPizzawillbring() == null
							|| oneSignupPizzaDTO.getPizzawillbring().isEmpty()) {
						oneSignupPizzaDTO.setPizzawillbring(oneDTO
								.getPizzawillbring());
					}

					// Carefully merge in values if multiple users sign up
					// from same unit.
					if (oneSignupPizzaDTO.getNumberattendingpizza() == 0) {
						oneSignupPizzaDTO.setNumberattendingpizza(oneDTO
								.getNumberattendingpizza());
					} else {
						oneSignupPizzaDTO
								.setNumberattendingpizza(oneSignupPizzaDTO
										.getNumberattendingpizza()
										+ oneDTO.getNumberattendingpizza());
					}
					if (oneSignupPizzaDTO.getNumberattendingpotluck() == 0) {
						oneSignupPizzaDTO.setNumberattendingpotluck(oneDTO
								.getNumberattendingpotluck());
					} else {
						oneSignupPizzaDTO
								.setNumberattendingpotluck(oneSignupPizzaDTO
										.getNumberattendingpotluck()
										+ oneDTO.getNumberattendingpotluck());
					}
				}
			}
			finalSignupPizzaDTOList.add(oneSignupPizzaDTO);

		}

		// Compute totals now that we're done making the individual unit counts.
		for (SignupPizzaDTO localSignupPizzaDTO : finalSignupPizzaDTOList) {
			totalPeopleAttending += localSignupPizzaDTO
					.getNumberattendingpizza()
					+ localSignupPizzaDTO.getNumberattendingpotluck();
			totalPizzaMeals += localSignupPizzaDTO.getNumberattendingpizza();
			totalPotluckMeals += localSignupPizzaDTO
					.getNumberattendingpotluck();
		}

		return finalSignupPizzaDTOList;
	}

	// Method for drop-down of number attending
	public List<String> getAttendingCount() {

		List<String> countList = new ArrayList<String>();
		for (int idx = 0; idx < 16; idx++) {
			countList.add(Integer.toString(idx));
		}
		return countList;
	}

	public void setPizzaEventList(List<PizzaEvent> pizzaEventList) {
		this.pizzaEventList = pizzaEventList;
	}

	public PizzaEvent getChosenPizzaEvent() {
		return chosenPizzaEvent;
	}

	public void setChosenPizzaEvent(PizzaEvent chosenPizzaEvent) {
		this.chosenPizzaEvent = chosenPizzaEvent;
	}

	public CalendarUtils.TimeSlot[] getTimeSlotsOfTheDay() {
		return CalendarUtils.getTimeSlotsOfTheDay(2010, 1);
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

		// Make sure chosenPizzaEventString is populated and
		// if not, call getPizzaEventList() to populate it
		// the first time around.
		if (chosenPizzaEventString == null) {
			getPizzaEventList();
		}

		// Robustness check
		if (chosenPizzaEventString == null) {
			return true;
		}

		// Special case hack: 1=> no meal has been set so just
		// assume this isn't a leader so page can be rendered.
		if (chosenPizzaEventString.equals("1")) {
			return false;
		}

		eventId = Long.valueOf(chosenPizzaEventString);
		PizzaEvent chosenPizzaEvent = eventService.getPizzaEvent(eventId);

		// Need to check for null value in case event was just deleted
		// (3/31/17).
		// In which case, simply conclude it's not closed
		if (chosenPizzaEvent == null) {
			return false;
		}
		return chosenPizzaEvent.isIsmealclosed();
	}

	public String signupPizzaView() throws CohomanException {

		// Get the chosen MealEvent
		if (chosenPizzaEventString == null || chosenPizzaEventString.length() == 0) {
			logger.log(Level.SEVERE,
					"Internal Error: invalid PizzaEventId parameter");
			FacesMessage message = new FacesMessage(
					"Internal Error: invalid PizzaEventId parameter. Click on Main Menu link.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		
		// Error check that a pizza/potluck has been chosen
		if (chosenPizzaEventString.equalsIgnoreCase("1")) {
			FacesMessage message = new FacesMessage(
					"User Error: you must choose a pizza/potluck to attend.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		// Get the chosen MealEvent
		eventId = Long.valueOf(chosenPizzaEventString);
		PizzaEvent chosenPizzaEvent = eventService.getPizzaEvent(eventId);
		if (chosenPizzaEvent == null) {
			logger.log(Level.SEVERE,
					"Internal Error: unable to find pizza/potluck event for eventId " + 
					eventId);
			FacesMessage message = new FacesMessage(
					"Internal Error: unable to find pizza/potluck event. Click on Main Menu link.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		eventType = chosenPizzaEvent.getEventtype();
		printableEventDate = chosenPizzaEvent.getPrintableEventDate();

		// Another hack added when we seem to lose the checked radio button
		if (signupOperation == null || signupOperation.isEmpty()) {
			signupOperation = "listSignups";
		}

		// Special hack to keep from doing a signup if all the radio buttons
		// are disabled except for list (because the meal has been marked
		// as closed), yet the signup is still checked.
		// So, all we can do is list, despite the signup being selected.
		if (getMealClosed()
				&& !signupOperation.equalsIgnoreCase("openPizzaNow")) {
			return "listSignups";
		}

		// Start making the DTO for use in doing a signup
		SignupPizzaDTO dto = new SignupPizzaDTO();
		dto.setEventid(eventId);
		dto.setNumberattendingpizza(numberattendingpizza);
		dto.setNumberattendingpotluck(numberattendingpotluck);
		dto.setPizzatopping1(pizzatopping1);
		dto.setPizzatopping2(pizzatopping2);
		dto.setPizzawillbring(pizzawillbring);

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

		//
		try {
			if (signupOperation.equalsIgnoreCase("doSignup")
					|| signupOperation.equalsIgnoreCase("doSignupSomeoneElse")) {
				// Always delete first, so create will always work.
				// Decided to have signup always work, so functionality
				// is that last signup always overwrites the previous signup.
				// (03/23/17)
				eventService.deleteSignupPizza(dto);
				if (numberattendingpizza > 0 || numberattendingpotluck > 0) {
					// call eventService with DTO
					eventService.signupPizza(dto);
				}
			} else if (signupOperation.equalsIgnoreCase("closePizzaNow")) {
				chosenPizzaEvent.setIsmealclosed(true);
				eventService.editPizzaEvent(chosenPizzaEvent);
			} else if (signupOperation.equalsIgnoreCase("openPizzaNow")) {
				chosenPizzaEvent.setIsmealclosed(false);
				eventService.editPizzaEvent(chosenPizzaEvent);
			}
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		// clear out fields for return to the page in this session
		clearFormFields();

		return signupOperation;
	}

	// Private method to remove meal events that shouldn't be displayed
	// to users in drop-downs since they've past.
	private List<PizzaEvent> filterOutPastMeals(List<PizzaEvent> pizzaList) {

		Calendar nowMinus1day = new GregorianCalendar();
		Calendar nowMinus7days = new GregorianCalendar();
		nowMinus1day.add(Calendar.DAY_OF_YEAR, -1);
		nowMinus7days.add(Calendar.DAY_OF_YEAR, -7);
		List<PizzaEvent> newPizzaEventList = new ArrayList<PizzaEvent>();
		for (PizzaEvent pizzaEvent : pizzaList) {
			Calendar pizzaEventCal = Calendar.getInstance();
			pizzaEventCal.setTime(pizzaEvent.getEventDate());

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
			User dbUser = userService.getUser(pizzaEvent.getLeader1());

			if (!(currentRole.getRoleid() == Role.MEALADMIN_ID || dbUser
					.getUsername().equalsIgnoreCase(
							LoggingUtils.getCurrentUsername()))) {

				// Here if not a special user
				// If the date of the event is more than one day old, don't
				// let people access the meal by eliminating it from
				// the drop-down
				if (pizzaEventCal.before(nowMinus1day)) {
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
					if (pizzaEventCal.before(nowMinus7days)) {
						// continue => don't add the meal to the drop-down list
						continue;
					}
				}
			}
			
			newPizzaEventList.add(pizzaEvent);
		}

		return newPizzaEventList;
	}

}
