package org.cohoman.model.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.business.EventManager;
import org.cohoman.model.business.MealSchedule.MealScheduleText;
import org.cohoman.model.business.User;
import org.cohoman.model.business.UserManager;
import org.cohoman.model.dto.CohoEventDTO;
import org.cohoman.model.dto.MealEventDTO;
import org.cohoman.model.dto.PizzaEventDTO;
import org.cohoman.model.dto.PotluckEventDTO;
import org.cohoman.model.dto.PrivateEventDTO;
import org.cohoman.model.dto.SignupMealDTO;
import org.cohoman.model.dto.SignupPizzaDTO;
import org.cohoman.model.dto.SignupPotluckDTO;
import org.cohoman.model.dto.UserDTO;
import org.cohoman.model.integration.SMS.SmsSender;
import org.cohoman.model.integration.persistence.beans.CohoEvent;
import org.cohoman.model.integration.persistence.beans.EventTypeDefs;
import org.cohoman.model.integration.persistence.beans.MainCalendarEvent;
import org.cohoman.model.integration.persistence.beans.MealEvent;
import org.cohoman.model.integration.persistence.beans.PizzaEvent;
import org.cohoman.model.integration.persistence.beans.PotluckEvent;
import org.cohoman.model.integration.persistence.beans.PrivateEvent;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.model.integration.persistence.beans.SpaceBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.CalendarUtils.MealDate;
import org.cohoman.view.controller.utils.Validators;
import org.mindrot.jbcrypt.BCrypt;

public class EventServiceImpl implements EventService {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private EventManager eventManager = null;
	private UserManager userManager = null;

	// Dependency Injection Getters and Setters
	public EventManager getEventManager() {
		return eventManager;
	}

	public void setEventManager(EventManager eventManager) {
		this.eventManager = eventManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	// Meal Event services
	public void createMealEvent(MealEventDTO mealEventDTO)
			throws CohomanException {
		logger.info("AUDIT: Create meal for " + mealEventDTO.getEventDate()
				+ ", lead cook = " + getUserFullname(mealEventDTO.getCook1())
				+ ", menu=\"" + mealEventDTO.getMenu() + "\", created by "
				+ LoggingUtils.getCurrentUsername());
		eventManager.createMealEvent(mealEventDTO,
				getUserFullname(mealEventDTO.getCook1()));
	}

	public void editMealEvent(MealEvent mealEvent) throws CohomanException {
		String cook2 = "";
		String cook3 = "";
		String cook4 = "";
		String cleaner1 = "";
		String cleaner2 = "";
		String cleaner3 = "";
		if (mealEvent.getCook2() != null) {
			cook2 = getUserFullname(mealEvent.getCook2());
		}
		if (mealEvent.getCook3() != null) {
			cook3 = getUserFullname(mealEvent.getCook3());
		}
		if (mealEvent.getCook4() != null) {
			cook4 = getUserFullname(mealEvent.getCook4());
		}
		if (mealEvent.getCleaner1() != null) {
			cleaner1 = getUserFullname(mealEvent.getCleaner1());
		}
		if (mealEvent.getCleaner2() != null) {
			cleaner2 = getUserFullname(mealEvent.getCleaner2());
		}
		if (mealEvent.getCleaner3() != null) {
			cleaner3 = getUserFullname(mealEvent.getCleaner3());
		}
		logger.info("AUDIT: Edit meal for " + mealEvent.getEventDate()
				+ ", menu = \"" + mealEvent.getMenu() + "\", lead cook = "
				+ getUserFullname(mealEvent.getCook1()) + ", cook2 = " + cook2
				+ ", cook3 = " + cook3 + ", cook4 = " + cook4 + ", cleaner1 = "
				+ cleaner1 + ", cleaner2 = " + cleaner2 + ", cleaner3 = "
				+ cleaner3 + ", maxattendees = " + mealEvent.getMaxattendees()
				+ ", ismealclosed = " + mealEvent.isIsmealclosed()
				+ " edited by " + LoggingUtils.getCurrentUsername());
		eventManager.editMealEvent(mealEvent);
	}

	public void deleteMealEvent(MealEvent mealEvent) throws Exception {

		String cook2 = "";
		String cook3 = "";
		String cook4 = "";
		String cleaner1 = "";
		String cleaner2 = "";
		String cleaner3 = "";
		if (mealEvent.getCook2() != null) {
			cook2 = getUserFullname(mealEvent.getCook2());
		}
		if (mealEvent.getCook3() != null) {
			cook3 = getUserFullname(mealEvent.getCook3());
		}
		if (mealEvent.getCook4() != null) {
			cook4 = getUserFullname(mealEvent.getCook4());
		}
		if (mealEvent.getCleaner1() != null) {
			cleaner1 = getUserFullname(mealEvent.getCleaner1());
		}
		if (mealEvent.getCleaner2() != null) {
			cleaner2 = getUserFullname(mealEvent.getCleaner2());
		}
		if (mealEvent.getCleaner3() != null) {
			cleaner3 = getUserFullname(mealEvent.getCleaner3());
		}

		logger.info("AUDIT: Delete meal for " + mealEvent.getEventDate()
				+ ", menu = \"" + mealEvent.getMenu() + "\", lead cook = "
				+ getUserFullname(mealEvent.getCook1()) + ", cook2 = " + cook2
				+ ", cook3 = " + cook3 + ", cook4 = " + cook4 + ", cleaner1 = "
				+ cleaner1 + ", cleaner2 = " + cleaner2 + ", cleaner3 = "
				+ cleaner3 + ", maxattendees = " + mealEvent.getMaxattendees()
				+ ", ismealclosed = " + mealEvent.isIsmealclosed() + " deleted by "
				+ LoggingUtils.getCurrentUsername());

		try {
			eventManager.deleteMealEvent(mealEvent);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			throw new CohomanException("Unable to delete Meal Event = "
					+ mealEvent.getEventDate());
		}
	}

	public List<MealEvent> getCurrentMealEvents() {
		return eventManager.getCurrentMealEvents();
	}

	public List<MealEvent> getAllMealEvents() {
		return eventManager.getAllMealEvents();
	}

	public MealEvent getMealEvent(Long eventId) {
		return eventManager.getMealEvent(eventId);
	}

	// Pizza Event services
	public void createPizzaEvent(PizzaEventDTO pizzaEventDTO)
			throws CohomanException {
		logger.info("AUDIT: Create pizza/potluck for "
				+ pizzaEventDTO.getEventDate() + " with leader1 "
				+ userManager.getUser(pizzaEventDTO.getLeader1()).getUsername()
				+ " created by " + LoggingUtils.getCurrentUsername());
		eventManager.createPizzaEvent(pizzaEventDTO,
				getUserFullname(pizzaEventDTO.getLeader1()));
	}

	public void editPizzaEvent(PizzaEvent pizzaEvent) throws CohomanException {

		String leader2String = "";
		if (pizzaEvent.getLeader2() != null) {
			leader2String = getUserFullname(pizzaEvent.getLeader2());
		}
		logger.info("AUDIT: Edit pizza/potluck for "
				+ pizzaEvent.getEventDate() + " with  leader1 "
				+ pizzaEvent.getLeader1String() + " and leader2 "
				+ leader2String + " edited by "
				+ LoggingUtils.getCurrentUsername());
		eventManager.editPizzaEvent(pizzaEvent);
	}

	public void deletePizzaEvent(PizzaEvent pizzaEvent) throws Exception {

		String leader2String = "";
		if (pizzaEvent.getLeader2() != null) {
			leader2String = getUserFullname(pizzaEvent.getLeader2());
		}

		logger.info("AUDIT: Delete pizza/potluck for "
				+ pizzaEvent.getEventDate() + " with  leader1 "
				+ pizzaEvent.getLeader1String() + " and leader2 "
				+ leader2String + " deleted by "
				+ LoggingUtils.getCurrentUsername());
		try {
			eventManager.deletePizzaEvent(pizzaEvent);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("Unable to delete Pizza/Potluck Event = "
					+ pizzaEvent.getEventDate());
		}
	}

	public List<PizzaEvent> getCurrentPizzaEvents() {
		return eventManager.getCurrentPizzaEvents();
	}

	public PizzaEvent getPizzaEvent(Long eventId) {
		return eventManager.getPizzaEvent(eventId);
	}

	// Potluck Event services
	public void createPotluckEvent(PotluckEventDTO potluckEventDTO)
			throws CohomanException {
		logger.info("AUDIT: Create potluck for "
				+ potluckEventDTO.getEventDate() + " with leader1 "
				+ userManager.getUser(potluckEventDTO.getLeader1()).getUsername()
				+ " and description \"" 
				+ potluckEventDTO.getEventinfo()
				+ "\" created by " + LoggingUtils.getCurrentUsername());
		eventManager.createPotluckEvent(potluckEventDTO,
				getUserFullname(potluckEventDTO.getLeader1()));
	}

	public void editPotluckEvent(PotluckEvent potluckEvent) throws CohomanException {

		String leader2String = "";
		if (potluckEvent.getLeader2() != null) {
			leader2String = getUserFullname(potluckEvent.getLeader2());
		}
		logger.info("AUDIT: Edit potluck for "
				+ potluckEvent.getEventDate() + " with  leader1 "
				+ potluckEvent.getLeader1String() + " and leader2 "
				+ leader2String + " edited by "
				+ LoggingUtils.getCurrentUsername());
		eventManager.editPotluckEvent(potluckEvent);
	}

	public void deletePotluckEvent(PotluckEvent potluckEvent) throws Exception {

		String leader2String = "";
		if (potluckEvent.getLeader2() != null) {
			leader2String = getUserFullname(potluckEvent.getLeader2());
		}

		logger.info("AUDIT: Delete pizza/potluck for "
				+ potluckEvent.getEventDate() + " with  leader1 "
				+ potluckEvent.getLeader1String() + " and leader2 "
				+ leader2String + " deleted by "
				+ LoggingUtils.getCurrentUsername());
		try {
			eventManager.deletePotluckEvent(potluckEvent);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("Unable to delete Potluck Event = "
					+ potluckEvent.getEventDate());
		}
	}

	public List<PotluckEvent> getCurrentPotluckEvents() {
		return eventManager.getCurrentPotluckEvents();
	}

	public PotluckEvent getPotluckEvent(Long eventId) {
		return eventManager.getPotluckEvent(eventId);
	}

	// Coho Event services
	public void createCohoEvent(CohoEventDTO cohoEventDTO)
			throws CohomanException {
		logger.info("AUDIT: Create Coho Event for "
				+ cohoEventDTO.getEventDate() + " until "
				+ cohoEventDTO.getEventdateend() + "  with event name \""
				+ cohoEventDTO.getEventName() + "\" entered by \""
				+ LoggingUtils.getCurrentUsername() + "\"");
		eventManager.createCohoEvent(cohoEventDTO);
	}

	public void editCohoEvent(CohoEvent cohoEvent) throws CohomanException {
		logger.info("AUDIT: Edit Coho Event for " + cohoEvent.getEventDate()
				+ " until " + cohoEvent.getEventdateend()
				+ "  with event name \"" + cohoEvent.getEventName()
				+ "\" modified by \"" + LoggingUtils.getCurrentUsername()
				+ "\"");
		eventManager.editCohoEvent(cohoEvent);
	}

	public void deleteCohoEvent(CohoEvent cohoEvent) throws Exception {

		logger.info("AUDIT: Delete Coho Event for " + cohoEvent.getEventDate()
				+ "  with event name " + cohoEvent.getEventName()
				+ " deleted by \"" + LoggingUtils.getCurrentUsername() + "\"");
		try {
			eventManager.deleteCohoEvent(cohoEvent);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("Unable to delete coho Event = "
					+ cohoEvent.getEventDate());
		}
	}

	public List<CohoEvent> getCurrentCohoEvents() {

		// Modify CohoEvent's in list to have printable field for user name
		List<CohoEvent> cohoEventsList = eventManager.getCurrentCohoEvents();
		List<CohoEvent> updatedCohoEventsList = new ArrayList<CohoEvent>();
		for (CohoEvent oneCohoEvent : cohoEventsList) {
			if (oneCohoEvent.getEnteredby() != null) {
				User theUser = userManager.getUser(oneCohoEvent.getEnteredby());
				oneCohoEvent.setEnteredbyUsername(theUser.getUsername());
			} else {
				// If enteredby was never filled in, return a blank
				// really just defensive since just added it, 3/15/17
				oneCohoEvent.setEnteredbyUsername("");
			}
			updatedCohoEventsList.add(oneCohoEvent);
		}
		return updatedCohoEventsList;
	}

	public CohoEvent getCohoEvent(Long eventId) {
		CohoEvent oneCohoEvent = eventManager.getCohoEvent(eventId);
		if (oneCohoEvent.getEnteredby() != null) {
			User theUser = userManager.getUser(oneCohoEvent.getEnteredby());
			oneCohoEvent.setEnteredbyUsername(theUser.getUsername());
		} else {
			// If enteredby was never filled in, return a blank
			// really just defensive since just added it, 3/15/17
			oneCohoEvent.setEnteredbyUsername("");
		}
		return oneCohoEvent;
	}

	// Private Event services
	public void createPrivateEvent(PrivateEventDTO privateEventDTO)
			throws CohomanException {
		logger.info("AUDIT: Create Private Event for "
				+ privateEventDTO.getEventDate() + " until "
				+ privateEventDTO.getEventdateend() + "  with event name \""
				+ privateEventDTO.getEventName() + "\"  with event info \""
				+ privateEventDTO.getEventinfo() + "\" requested by \""
				+ LoggingUtils.getCurrentUsername() + "\"");
		eventManager.createPrivateEvent(privateEventDTO);
		
		// determine text to send out to Space Admin's cell phones
		String textMsg = "CCM: new reserved space requested: \"" + 
				privateEventDTO.getEventName() + "\" by " + LoggingUtils.getCurrentUsername();
		
		// my phone number
		User userSpaceAdmin = userManager.getUserByUsername("bill");
		if (userSpaceAdmin == null) {
			logger.info("AUDIT: Unable to find user " + "bill" +
					" to notify that Space Administrator of request, but request has been made.");
		} else {
			String phoneNumber = userSpaceAdmin.getCellphone();
			if (!phoneNumber.isEmpty() && Validators.isValidPhoneNumber(phoneNumber)) {
				phoneNumber = phoneNumber.replace("-",  "");  // remove dashes from phone number
				SmsSender.sendtextMessage(phoneNumber, textMsg);
			} else {
				logger.info("AUDIT: phone number for " + "bill" +
					" is not valid: " + phoneNumber);
			}
		}
		
		// Joan's phone number
		userSpaceAdmin = userManager.getUserByUsername("joan");
		if (userSpaceAdmin == null) {
			logger.info("AUDIT: Unable to find user " + "joan" +
					" to notify that Space Administrator of request, but request has been made.");
		} else {
			String phoneNumber = userSpaceAdmin.getCellphone();
			if (!phoneNumber.isEmpty() && Validators.isValidPhoneNumber(phoneNumber)) {
				phoneNumber = phoneNumber.replace("-",  "");  // remove dashes from phone number
				SmsSender.sendtextMessage(phoneNumber, textMsg);
			} else {
				logger.info("AUDIT: phone number for " + "joan" +
					" is not valid: " + phoneNumber);
			}
		}

		// Laura's phone number
		userSpaceAdmin = userManager.getUserByUsername("laura");
		if (userSpaceAdmin == null) {
			logger.info("AUDIT: Unable to find user " + "laura" +
					" to notify that Space Administrator of request, but request has been made.");
		} else {
			String phoneNumber = userSpaceAdmin.getCellphone();
			if (!phoneNumber.isEmpty() && Validators.isValidPhoneNumber(phoneNumber)) {
				phoneNumber = phoneNumber.replace("-",  "");  // remove dashes from phone number
				SmsSender.sendtextMessage(phoneNumber, textMsg);
			} else {
				logger.info("AUDIT: phone number for " + "laura" +
					" is not valid: " + phoneNumber);
			}
		}

	}

	public void editPrivateEvent(PrivateEvent privateEvent)
			throws CohomanException {
		eventManager.editPrivateEvent(privateEvent);

		logger.info("AUDIT: Attempted Edit Private Event for "
				+ privateEvent.getEventDate() + " until "
				+ privateEvent.getEventdateend() + "  with event name \""
				+ privateEvent.getEventName() + "\"  with event info \""
				+ privateEvent.getEventinfo() + "\" edited by \""
				+ LoggingUtils.getCurrentUsername() + "\" with Characteristics \""
				+ getChosenEventCharacteristics(privateEvent) +  "\"");
		PrivateEvent updatedprivateEvent = getPrivateEvent(privateEvent.getEventid());
		logger.info("AUDIT: Successfully Edited Private Event for "
				+ privateEvent.getEventDate() + " until "
				+ privateEvent.getEventdateend() + "  with event name \""
				+ privateEvent.getEventName() + "\"  with event info \""
				+ privateEvent.getEventinfo() + "\" edited by \""
				+ LoggingUtils.getCurrentUsername() + "\" with Characteristics \""
				+ getChosenEventCharacteristics(updatedprivateEvent) + "\" for reserved spaces \"" 
				+ getReservedSpaces(updatedprivateEvent) + "\"");
	}

	public void deletePrivateEvent(Long privateEventId) throws Exception {

		PrivateEvent privateEvent = getPrivateEvent(privateEventId);
		
		// Robustness check 05/12/2018
		if (privateEvent == null) {
			logger.log(Level.SEVERE, "Delete private event failed because no event not found for event id "
					+ privateEventId);			
			throw new Exception("Unable to delete private Event = "
					+ privateEventId);
		}
		
		logger.info("AUDIT: Delete Private Event for "
				+ privateEvent.getEventDate() + " until "
				+ privateEvent.getEventdateend() + "  with event name \""
				+ privateEvent.getEventName() + "\" deleted by "
				+ LoggingUtils.getCurrentUsername() + "\"");
		try {
			eventManager.deletePrivateEvent(privateEventId);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			throw new Exception("Unable to delete private Event = "
					+ privateEventId);
		}
	}

	public PrivateEvent getPrivateEvent(Long eventId) throws CohomanException {
		PrivateEvent onePrivateEvent = eventManager.getPrivateEvent(eventId);
		
		// Robustness check 05/12/2018
		if (onePrivateEvent == null) {
			logger.log(Level.SEVERE, "Private event not found for event id "
					+ eventId);
			throw new CohomanException("Error: Private event not found for event id "
					+ eventId);
		}
		
		if (onePrivateEvent.getRequester() != null) {
			User theUser = userManager.getUser(onePrivateEvent.getRequester());
			onePrivateEvent.setRequesterUsername(theUser.getUsername());
		} else {
			// If enteredby was never filled in, return a blank
			// really just defensive since just added it, 3/15/17
			onePrivateEvent.setRequesterUsername("");
		}
		if (onePrivateEvent.getApprovedby() != null) {
			User theUser = userManager.getUser(onePrivateEvent.getApprovedby());
			onePrivateEvent.setApprovedByUsername(theUser.getUsername());
		} else {
			// If enteredby was never filled in, return a blank
			// really just defensive since just added it, 3/15/17
			onePrivateEvent.setApprovedByUsername("");
		}

		return onePrivateEvent;
	}

	public List<PrivateEvent> getAllPrivateEvents() {
		return eventManager.getAllPrivateEvents();
	}

	public List<PrivateEvent> getMyPrivateEvents() {
		return eventManager.getMyPrivateEvents();
	}

	public List<PrivateEvent> getUpcomingPrivateEvents() {
		return eventManager.getUpcomingPrivateEvents();
	}

	public List<PrivateEvent> getPendingPrivateEvents() {
		return eventManager.getPendingPrivateEvents();
	}

	// Meal signup
	public void signupMeal(SignupMealDTO dto) throws CohomanException {
		MealEvent mealEvent = getMealEvent(dto.getEventid());
		
		// Loop through all signup records to see if anyone else in the same unit
		// as the current user has already signed-up.
		User currentUser = userManager.getUser(dto.getUserid());
		User candidateUser = null;
		List<SignupMealDTO> signupMealDTOList = getAllMealSignups(dto.getEventid());
		for (SignupMealDTO signupMealDTO : signupMealDTOList) {
			candidateUser = userManager.getUser(signupMealDTO.getUserid()); 
			if (currentUser.getUnit().equalsIgnoreCase(candidateUser.getUnit())) {
				String errorString = "Error: User \"" + candidateUser.getUsername() +
					"\" has already signed-up for the meal on " + mealEvent.getEventDate() +
					". Only one user can enter signup information per unit for a given meal.";
				logger.log(Level.SEVERE, errorString);
				throw new CohomanException(errorString);
			}
		}
		
		eventManager.signupForMeal(dto);
		logger.info("AUDIT: Meal signup for " + mealEvent.getEventDate()
				+ "  with lead cook \"" + getUserFullname(mealEvent.getCook1())
				+ "\" requested by \"" + LoggingUtils.getCurrentUsername()
				+ "\" for user \"" + getUserFullname(dto.getUserid())
				+ "\" requesting " + dto.getNumberattending() + " meals.");
	}

	public List<SignupMealDTO> getAllMealSignups(Long eventid)
			throws CohomanException {

		// Modify DTO's in list to have printable field for user name
		List<SignupMealDTO> signupList = eventManager
				.getAllMealSignups(eventid);
		List<SignupMealDTO> updatedSignupList = new ArrayList<SignupMealDTO>();
		for (SignupMealDTO oneDTO : signupList) {
			User theUser = userManager.getUser(oneDTO.getUserid());
			oneDTO.setUsername(theUser.getUsername());
			oneDTO.setFoodrestrictions(theUser.getFoodrestrictions());
			updatedSignupList.add(oneDTO);
		}
		return updatedSignupList;
	}

	public void deleteSignupMeal(SignupMealDTO dto) throws CohomanException {
		MealEvent mealEvent = getMealEvent(dto.getEventid());
		logger.info("AUDIT: Deleted meal signup for "
				+ mealEvent.getEventDate() + "  with lead cook "
				+ getUserFullname(mealEvent.getCook1()) + " requested by "
				+ LoggingUtils.getCurrentUsername() + " for "
				+ dto.getNumberattending() + " meals.");
		eventManager.deleteSignupForMeal(dto);
	}

	// Pizza/potluck signup
	public void signupPizza(SignupPizzaDTO dto) throws CohomanException {
		PizzaEvent pizzaEvent = getPizzaEvent(dto.getEventid());
		
		// Loop through all signup records to see if anyone else in the same unit
		// as the current user has already signed-up.
		User currentUser = userManager.getUser(dto.getUserid());
		User candidateUser = null;
		List<SignupPizzaDTO> signupPizzaDTOList = getAllPizzaSignups(dto.getEventid());
		for (SignupPizzaDTO signupPizzaDTO : signupPizzaDTOList) {
			candidateUser = userManager.getUser(signupPizzaDTO.getUserid()); 
			if (currentUser.getUnit().equalsIgnoreCase(candidateUser.getUnit())) {
				String errorString = "Error: User \"" + candidateUser.getUsername() +
					"\" has already signed-up for the pizza/potluck on " + pizzaEvent.getEventDate() +
					". Only one user can enter signup information per unit for a given meal.";
				logger.log(Level.SEVERE, errorString);
				throw new CohomanException(errorString);
			}
		}
	
		eventManager.signupForPizza(dto);
		logger.info("AUDIT: Pizza/potluck signup for "
				+ pizzaEvent.getEventDate() + "  with leader \""
				+ pizzaEvent.getLeader1String() + "\" requested by \""
				+ LoggingUtils.getCurrentUsername() + "\" for user \""
				+ getUserFullname(dto.getUserid()) + "\" requesting "
				+ dto.getNumberattendingpizza() + " pizza's and "
				+ dto.getNumberattendingpotluck() + " potlucks.");
	}

	public List<SignupPizzaDTO> getAllPizzaSignups(Long eventid)
			throws CohomanException {

		// Modify DTO's in list to have printable field for user name
		List<SignupPizzaDTO> signupList = eventManager
				.getAllPizzaSignups(eventid);
		List<SignupPizzaDTO> updatedSignupList = new ArrayList<SignupPizzaDTO>();
		for (SignupPizzaDTO oneDTO : signupList) {
			User theUser = userManager.getUser(oneDTO.getUserid());
			oneDTO.setUsername(theUser.getUsername());
			oneDTO.setFoodrestrictions(theUser.getFoodrestrictions());
			updatedSignupList.add(oneDTO);
		}
		return updatedSignupList;
	}

	public void deleteSignupPizza(SignupPizzaDTO dto) throws CohomanException {
		PizzaEvent pizzaEvent = getPizzaEvent(dto.getEventid());
		logger.info("AUDIT: Deleted pizza/potluck signup for "
				+ pizzaEvent.getEventDate() + "  with leader "
				+ pizzaEvent.getLeader1String() + " requested by "
				+ LoggingUtils.getCurrentUsername() + " for "
				+ dto.getNumberattendingpizza() + " pizza's and "
				+ dto.getNumberattendingpotluck() + " potlucks.");
		eventManager.deleteSignupForPizza(dto);
	}

	// Potluck signup
	public void signupPotluck(SignupPotluckDTO dto) throws CohomanException {
		PotluckEvent potluckEvent = getPotluckEvent(dto.getEventid());
		
		// Loop through all signup records to see if anyone else in the same unit
		// as the current user has already signed-up.
		User currentUser = userManager.getUser(dto.getUserid());
		User candidateUser = null;
		List<SignupPotluckDTO> signupPotluckDTOList = getAllPotluckSignups(dto.getEventid());
		for (SignupPotluckDTO signupPotluckDTO : signupPotluckDTOList) {
			candidateUser = userManager.getUser(signupPotluckDTO.getUserid()); 
			if (currentUser.getUnit().equalsIgnoreCase(candidateUser.getUnit())) {
				String errorString = "Error: User \"" + candidateUser.getUsername() +
					"\" has already signed-up for the potluck on " + potluckEvent.getEventDate() +
					". Only one user can enter signup information per unit for a given meal.";
				logger.log(Level.SEVERE, errorString);
				throw new CohomanException(errorString);
			}
		}
	
		eventManager.signupForPotluck(dto);
		logger.info("AUDIT: Potluck signup for "
				+ potluckEvent.getEventDate() + "  with leader \""
				+ potluckEvent.getLeader1String() + "\" requested by \""
				+ LoggingUtils.getCurrentUsername() + "\" for user \""
				+ getUserFullname(dto.getUserid()) + "\" bringing "
				+ dto.getItemdescription() + " and "
				+ dto.getNumberattending() + " attending.");
	}

	public List<SignupPotluckDTO> getAllPotluckSignups(Long eventid)
			throws CohomanException {

		// Modify DTO's in list to have printable field for user name
		List<SignupPotluckDTO> signupList = eventManager
				.getAllPotluckSignups(eventid);
		List<SignupPotluckDTO> updatedSignupList = new ArrayList<SignupPotluckDTO>();
		for (SignupPotluckDTO oneDTO : signupList) {
			User theUser = userManager.getUser(oneDTO.getUserid());
			oneDTO.setUsername(theUser.getUsername());
			updatedSignupList.add(oneDTO);
		}
		return updatedSignupList;
	}

	public void deleteSignupPotluck(SignupPotluckDTO dto) throws CohomanException {
		PotluckEvent potluckEvent = getPotluckEvent(dto.getEventid());
		logger.info("AUDIT: Deleted potluck signup for "
				+ potluckEvent.getEventDate() + "  with leader "
				+ potluckEvent.getLeader1String() + " requested by "
				+ LoggingUtils.getCurrentUsername() + " bringing "
				+ dto.getItemdescription() + " and "
				+ dto.getNumberattending() + " attending.");
		eventManager.deleteSignupForPotluck(dto);
	}


	// Other basic getters
	public List<List<MealScheduleText>> getCurrentMealScheduleRows() {
		return eventManager.getCurrentMealScheduleRows();
	}

	public List<MealDate> getMealDaysForPeriod() {
		return eventManager.getMealDaysForPeriod();
	}

	public List<MealDate> getPizzaDaysForPeriod() {
		return eventManager.getPizzaDaysForPeriod();
	}

	public List<MealDate> getPotluckDaysForPeriod() {
		return eventManager.getPotluckDaysForPeriod();
	}

	public User authenticateUser(UserDTO theUser) throws CohomanException {

		// First, hash the password and replace the plaintext password
		// with the hashed value into the DTO
		String plaintextPassword = theUser.getPassword();

		// Populate the DTO with data from the DB if in
		// fact the user actually exists. This is a way to
		// effectively authenticate the user, i.e. if the
		// passed-in user and password are actually in the DB.
		User dbUser = userManager.getUser(theUser);
		if (dbUser == null) {

			// prevent massive guessing
			try {
				Thread.sleep(2000);
			} catch (InterruptedException iex) {
			}
			throw new CohomanException("No such user exists: "
					+ theUser.getUsername());
		}

		// Found user. Now check that the password hash from the
		// DB matches the password hash we just computed.
		// if (!dbUser.getUsername().equalsIgnoreCase("bill")) {
		if (!BCrypt.checkpw(plaintextPassword, dbUser.getPassword())) {

			// prevent massive guessing
			try {
				Thread.sleep(2000);
			} catch (InterruptedException iex) {
			}
			// special-case users here if needed ...
			// if (!dbUser.getUsername().equals("bill"))
			throw new CohomanException("Password is wrong for user "
					+ theUser.getUsername());
		}
		// }
		return dbUser;
	}

	public List<Role> getRolesForUser(Long userId) {
		return userManager.getRolesForUser(userId);
	}

	public List<MainCalendarEvent> getMonthsCalendarEvents(Date theMonth) {
		return eventManager.getMonthsCalendarEvents(theMonth);
	}

	public List<MainCalendarEvent> getMainCalendarEventsForDay(Date dateOfDay) {
		return eventManager.getMainCalendarEventsForDay(dateOfDay);
	}

	public List<SpaceBean> getAllSpaces() {
		return eventManager.getAllSpaces();
	}

	private String getUserFullname(Long userid) throws CohomanException {
		User dbUser = userManager.getUser(userid);
		if (dbUser == null) {
			throw new CohomanException("No such user exists for this userid: "
					+ userid);
		}
		String fullname = dbUser.getFirstname() + " " + dbUser.getLastname();
		return fullname;
	}

	private String getReservedSpaces(PrivateEvent theCurrentPrivateEvent) {

		String printableListOfSpaces = "";
		if (theCurrentPrivateEvent != null) {
			Set<SpaceBean> spaceBeanList = theCurrentPrivateEvent
					.getSpaceList();
			for (SpaceBean oneBean : spaceBeanList) {
				printableListOfSpaces += oneBean.getSpaceName() + ", ";
			}
			if (printableListOfSpaces.length() > 0) {
				printableListOfSpaces = printableListOfSpaces.substring(0,
						printableListOfSpaces.length() - 2);
			}
		}
		return printableListOfSpaces;
	}

	private String getChosenEventCharacteristics(PrivateEvent theCurrentPrivateEvent) {

		String chosenCharacteristics = "";
		if (theCurrentPrivateEvent != null) {
			if (theCurrentPrivateEvent.isIscohousingevent()) {
				chosenCharacteristics += EventTypeDefs.COHOUSINGEVENTTYPE
						+ ", ";
			}
			if (theCurrentPrivateEvent.isIsinclusiveevent()) {
				chosenCharacteristics += EventTypeDefs.INCLUSIVEEVENTTYPE
						+ ", ";
			}
			if (theCurrentPrivateEvent.isIsexclusiveevent()) {
				chosenCharacteristics += EventTypeDefs.EXCLUSIVEEVENTTYPE
						+ ", ";
			}
			if (theCurrentPrivateEvent.isIsincomeevent()) {
				chosenCharacteristics += EventTypeDefs.INCOMEEVENTTYPE + ", ";
			}
			if (theCurrentPrivateEvent.isIsphysicallyactiveevent()) {
				chosenCharacteristics += EventTypeDefs.PHYSICALLYACTIVEEVENTTYPE
						+ ", ";
			}
			if (chosenCharacteristics.length() > 0) {
				chosenCharacteristics = chosenCharacteristics.substring(0,
						chosenCharacteristics.length() - 2);
			}
		}
		return chosenCharacteristics;
	}
}
