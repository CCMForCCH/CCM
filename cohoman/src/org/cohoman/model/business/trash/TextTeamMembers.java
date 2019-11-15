package org.cohoman.model.business.trash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import org.cohoman.model.business.User;
import org.cohoman.model.integration.SMS.SmsSender;
import org.cohoman.model.service.ListsService;
import org.cohoman.model.service.UserService;
import org.cohoman.model.springcontext.AppContext;
import org.cohoman.view.controller.utils.Validators;
import org.springframework.context.ApplicationContext;

public class TextTeamMembers implements Runnable, Serializable {

	private static final long serialVersionUID = 4678206276499587830L;

	private ListsService listsService = null;
	private UserService userService = null;

	public ListsService getListsService() {
		return listsService;
	}

	public void setListsService(ListsService listsService) {
		this.listsService = listsService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	Logger logger = Logger.getLogger(this.getClass().getName());

	public void run() {

		try {

			// Get beans for Lists and User services from the
			// ApplicationContext.
			// Have to do this so we can access them from another task.
			ApplicationContext ctx = AppContext.getApplicationContext();
			listsService = (ListsService) ctx.getBean("listsService");
			userService = (UserService) ctx.getBean("userService");

			//
			Calendar cal = Calendar.getInstance();
			logger.info("timer woke up at: " + cal.getTime());

			List<TrashRow> trashRows = listsService.getTrashSchedule();
			List<User> usersHereNow = userService.getUsersHereNow();
			List<String> teamMembers0 = getUsernamesForTeam(trashRows
					.get(0));
			logger.info("Team members are: " + teamMembers0);
			List<String> teamMembers1 =
					getUsernamesForTeam(trashRows.get(1));

			if (isNowTheSelectedDayAndTime(Calendar.MONDAY, 20, 0)) {
				logger.info("Team members are: " + teamMembers0);
				logger.info("Team members are: " + teamMembers1);
				// sendTextMessageToTeam(teamMembers0, "trash message");
				sendTextMessageToTeam(teamMembers1, "next Sunday trash");
			}

			if (isNowTheSelectedDayAndTime(Calendar.SUNDAY, 20, 30)) {
				logger.info("Team members are: " + teamMembers0);
				sendTextMessageToTeam(teamMembers1, "next Sunday trash2");
			}

			if (isNowTheSelectedDayAndTime(Calendar.SUNDAY, 9, 0)) {
				logger.info("Team members are: " + teamMembers0);
				sendTextMessageToTeam(teamMembers0, "trash tonight");
			}

			if (isNowTheSelectedDayAndTime(Calendar.SUNDAY, 17, 30)) {
				logger.info("Team members are: " + teamMembers0);
				sendTextMessageToTeam(teamMembers0, "trash now");
			}

		} catch (Throwable th) {
			logger.severe(th.toString());
			th.printStackTrace();
		}

	}

	private boolean isNowTheSelectedDayAndTime(int dayOfWeek, int hour, int minutes) {
		
		// Start with Current time as a Calendar
		Calendar calNow = Calendar.getInstance();
		
		// Create desired time as a Calendar
		Calendar calDesired = Calendar.getInstance();
		//calDesired.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		calDesired.set(Calendar.HOUR_OF_DAY, hour);
		calDesired.set(Calendar.MINUTE, minutes);
		
		// Now see if the 2 times are within 10 min (600 sec.).
		int timeDiff = (int) (calNow.getTimeInMillis() - calDesired
				.getTimeInMillis());
		timeDiff = java.lang.Math.abs(timeDiff);
		if (timeDiff/1000 < 600) {
			return true;
		}
		return false;
	}
	private List<String> getUsernamesForTeam(TrashRow oneTrashRow) {

		// Build list from each role
		List<String> teamNames = new ArrayList<String>();

		String nameForPerson = oneTrashRow.getOrganizer();
		String[] splits = nameForPerson.split(" ");
		if (splits.length > 1) {
			nameForPerson = splits[0];
		}
		teamNames.add(nameForPerson);

		nameForPerson = oneTrashRow.getStrongPerson();
		splits = nameForPerson.split(" ");
		if (splits.length > 1) {
			nameForPerson = splits[0];
		}
		teamNames.add(nameForPerson);

		nameForPerson = oneTrashRow.getTeamMember1();
		splits = nameForPerson.split(" ");
		if (splits.length > 1) {
			nameForPerson = splits[0];
		}
		teamNames.add(nameForPerson);

		nameForPerson = oneTrashRow.getTeamMember2();
		splits = nameForPerson.split(" ");
		if (splits.length > 1) {
			nameForPerson = splits[0];
		}
		teamNames.add(nameForPerson);

		return teamNames;

	}

	private void sendTextMessageToTeam(List<String> teamMembers, String textMsg) {

		for (String oneMember : teamMembers) {

			// Find cell phone number for user
			List<User> allUsersHere = userService.getUsersHereNow();
			User theUser = null;
			for (User oneUser : allUsersHere) {
				if (oneMember.equalsIgnoreCase(oneUser.getUsername())) {
					theUser = oneUser;
					break;
				}
			}

			// Give-up on text message if don't know user.
			if (theUser == null) {
				logger.warning("AUDIT: Unable to find user "
						+ oneMember
						+ " to notify that trash team member of request; won't end a message.");
				return;
			}

			// OK, we know the user. Get the phone number and send the message.
			String phoneNumber = theUser.getCellphone();
			if (!phoneNumber.isEmpty()
					&& Validators.isValidPhoneNumber(phoneNumber)) {
				phoneNumber = phoneNumber.replace("-", ""); // remove dashes
															// from phone number
				// SmsSender.sendtextMessage(phoneNumber, textMsg);
				SmsSender.sendtextMessage("6179902631",
						"CCM: sending msg for " + oneMember
								+ " to phone number " + theUser.getCellphone());
			} else {
				logger.info("AUDIT: phone number for " + theUser.getUsername()
						+ " is not valid: " + phoneNumber);
			}
		}

	}

}
