package org.cohoman.model.business.trash;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.cohoman.model.business.ListsManagerImpl.SecurityDataForRow;
import org.cohoman.model.business.User;
import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.integration.SMS.SmsSender;
import org.cohoman.model.integration.email.SendEmail;
import org.cohoman.model.integration.persistence.beans.CchSectionTypeEnum;
import org.cohoman.model.service.ListsService;
import org.cohoman.model.service.UserService;
import org.cohoman.model.singletons.ConfigScalarValues;
import org.cohoman.model.springcontext.AppContext;
import org.cohoman.view.controller.utils.MaintenanceTypeEnums;
import org.cohoman.view.controller.utils.SortEnums;
import org.cohoman.view.controller.utils.TaskStatusEnums;
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

			//Calendar cal = Calendar.getInstance();
			//logger.info("timer woke up at: " + cal.getTime());

			// Get trash schedule rows for date and team members
			List<TrashRow> trashRows = listsService.getTrashSchedule();
			List<String> teamMembers0 = getUsernamesForTeam(trashRows
					.get(0));
			List<String> teamMembers1 =
					getUsernamesForTeam(trashRows.get(1));

			// Convert trash date to figure out what date to send the messages
			String startDateString = trashRows.get(0).getSundayDate();
			Date startDate = new SimpleDateFormat("MMM d, yyyy")
					.parse(startDateString);
			
			// Send messages 1 week in advance to the team(1). Note
			// that using start date for team(0) is fine. We want to send it now.
			if (isNowTheSelectedDayAndTime(startDate, 20, 0)) {
				logger.info("Team0 members are: " + teamMembers0);
				logger.info("Team1 members are: " + teamMembers1);
				sendTextMessageToTeam(teamMembers1, "CCM: You are on the trash team next weekend.");
			}

			// Next, send messages that today is the day for trash. But
			// the day may be Sunday or Monday. Need to check which it is
			// check for the appropriate day and time.
			if (isNowTheSelectedDayAndTime(startDate, 9, 0)) {
				logger.info("Team members are: " + teamMembers0);
				sendTextMessageToTeam(teamMembers0, "CCM: You are on the trash team this evening.");				
			}

			if (isNowTheSelectedDayAndTime(startDate, 17, 0)) {
				logger.info("Team members are: " + teamMembers0);
				sendTextMessageToTeam(teamMembers0, "CCM: You are scheduled to do trash soon or now!");				
			}
			
			// Next get the Security schedule so we can send notices to them.
			List<SecurityDataForRow> securityRows = listsService
					.getSecurityListWithSubs(CchSectionTypeEnum.COMMONHOUSE);
			
			// Now we need to get the date and person for the security person
			Date securityDate = securityRows.get(0).getStartingDate();
			List<String> singleSecurityPersonList = new ArrayList<String>();
			
			// Check if there's a substitute, and if so, add that to the list 
			String substituteName = securityRows.get(0).getSubstitute();
			if (substituteName == null || substituteName.isEmpty()) {
				singleSecurityPersonList.add(securityRows.get(0).getUsername());
			} else {
				singleSecurityPersonList.add(substituteName);				
			}
			//singleSecurityPersonList.add("bill");
			//Calendar today = Calendar.getInstance();
			//today.set(Calendar.HOUR, 0);
			//today.set(Calendar.MINUTE, 0);
			//today.set(Calendar.SECOND, 0);
			//if (isNowTheSelectedDayAndTime(today.getTime(), 0, 14)) {
			if (isNowTheSelectedDayAndTime(securityDate, 10, 0)) {
				sendTextMessageToTeam(singleSecurityPersonList, "CCM: You are scheduled to do security starting tonight");
				singleSecurityPersonList.clear(); // empty the list
				substituteName = securityRows.get(1).getSubstitute();
				if (substituteName == null || substituteName.isEmpty()) {
					singleSecurityPersonList.add(securityRows.get(1).getUsername());
				} else {
					singleSecurityPersonList.add(substituteName);				
				}
				sendTextMessageToTeam(singleSecurityPersonList, "CCM: You are scheduled to do security starting one week from tonight");
			}
			
			// This is the checking for the periodic maintenance that 
			// probably won't be used. 09/25/2021
					
			// Find the Overdue Periodic Maintenance Tasks and if there are any,
			// build and send email to the appropriate people (e.g. Managing Board)
			// Note cheating by re-using the security date (i.e. Sunday)
			if (isNowTheSelectedDayAndTime(securityDate, 23, 0)) {
//				buildAndSendPeriodicMaintenanceEmail();
			}

		} catch (Throwable th) {
			logger.severe(th.toString());
			th.printStackTrace();
		}
	}

	private boolean isNowTheSelectedDayAndTime(Date dateOnly, int hour, int minutes) {
		
		// Start with Current time as a Calendar
		Calendar calNow = Calendar.getInstance();
		
		// Create desired time as a Calendar
		Calendar calDesired = Calendar.getInstance();
		calDesired.setTime(dateOnly);
	
		// Check if we are on the desired day. If not, return false.
		if (!(calDesired.get(Calendar.YEAR) == calNow.get(Calendar.YEAR) &&
				calDesired.get(Calendar.DAY_OF_YEAR) == calNow.get(Calendar.DAY_OF_YEAR))) {
			return false;
		} 
		
		// Date is right. Set the desired time.
		calDesired.set(Calendar.HOUR_OF_DAY, hour);
		calDesired.set(Calendar.MINUTE, minutes);
		calDesired.set(Calendar.SECOND, 0);
		
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
						+ " to notify that trash team member of request; won't send a message.");
				continue;
			}

			if (!theUser.isAllowtexting()) {
				logger.warning("AUDIT: Texting is disabled for "
						+ oneMember
						+ " preventing notification of trash duty.");
				continue;				
			}
			// OK, we know the user. Get the phone number and send the message.
			String phoneNumber = theUser.getCellphone();
			
			// Oh, special-case John M's phone number
			if (phoneNumber.isEmpty() && theUser.getUsername().equals("johnm")) {
				phoneNumber = ConfigScalarValues.johnms_phone_number;
			}
			
			// Oh, one more special-case. If the user is Meredith, use 
			// Robin's phone.
			if (theUser.getUsername().equalsIgnoreCase("meredith")) {
				phoneNumber = "617-999-7716";
			}
			
			if (!phoneNumber.isEmpty()
					&& Validators.isValidPhoneNumber(phoneNumber)) {
				phoneNumber = phoneNumber.replace("-", ""); // remove dashes
															// from phone number
				SmsSender.sendtextMessage(phoneNumber, textMsg);
				SmsSender.sendtextMessage(ConfigScalarValues.my_phone_number,
						"CCM: sending msg for " + oneMember
								+ " to phone number " + theUser.getCellphone() + " " + textMsg);
			} else {
				logger.info("AUDIT: phone number for " + theUser.getUsername()
						+ " is not valid: " + phoneNumber);
			}
		}

	}

	private void buildAndSendPeriodicMaintenanceEmail() {
	
		String cch_talk_emailAddress = "cch-talk@googlegroups.com";
		String cch_mb_emailAddress = "cch-mb@googlegroups.com";

		List<MaintenanceItemDTO> maintenanceItemDTOList = 
				listsService.getMaintenanceItems(SortEnums.ORDERBYNAME, MaintenanceTypeEnums.ALL);
		
		String hofMlines = "";
		String ownerMlines = "";
		int hofItemCnt = 0;
		int ownerItemCnt = 0;
		for (MaintenanceItemDTO mDTO : maintenanceItemDTOList) {
			if (mDTO.getTaskStatus().equalsIgnoreCase(TaskStatusEnums.OVERDUE.name())) {
				
				//Split based on Hofeller item vs. Owner item.
				if (mDTO.getMaintenanceType().equalsIgnoreCase(MaintenanceTypeEnums.HOFELLER.name()
						)) {
					
					// Item is for Hofeller
					if (hofMlines.length() == 0) {
						hofMlines = "\n <NOTE: testing without real data from Hofeller>\n \nCCM has discovered that the following CCH periodic maintenance items are due now to be performed again:\n";
					}
					String oneline = "\n" + ++hofItemCnt +".) ITEM: " + mDTO.getItemname() +
							", DATE LAST PERFORMED: " + mDTO.getPrintableLastperformedDate() +
							", DATE FOR NEXT SERVICE: " + mDTO.getPrintableNextServiceDate() + "\n";
					hofMlines += oneline;
					
				} else {
					
					// Item is for Owners.
					if (ownerMlines.length() == 0) {
						ownerMlines = "\n <NOTE: testing with actual data>\n \nCCM has discovered that the following CCH periodic maintenance items are due now to be performed again:\n";
					}
					String oneline = "\n" + ++ownerItemCnt +".) ITEM: " + mDTO.getItemname() +
							", DATE LAST PERFORMED: " + mDTO.getPrintableLastperformedDate() +
							", DATE FOR NEXT SERVICE: " + mDTO.getPrintableNextServiceDate() + "\n";
					ownerMlines += oneline;
					
				}
			}
		}
		
		hofMlines += "\n\nPlease initiate this work and notify the Managing Board when this task is completed. Login to CCM for more details.\n";
		ownerMlines += "\n\nPlease initiate this work and notify the Managing Board when this task is completed. Login to CCM for more details.\n";
		
		if (hofMlines.length() > 0) {
			SendEmail.sendEmailToAddress("billhuber01@yahoo.com", "Overdue Common Area periodic maintenance tasks", hofMlines);
			SendEmail.sendEmailToAddress(cch_mb_emailAddress, "Overdue Common Area periodic maintenance tasks", hofMlines);
		}
		if (ownerMlines.length() > 0) {
			SendEmail.sendEmailToAddress("billhuber01@yahoo.com", "Overdue CCH owner periodic maintenance tasks", ownerMlines);
			SendEmail.sendEmailToAddress(cch_mb_emailAddress, "Overdue CCH owner periodic maintenance tasks", ownerMlines);
		}

	}
}
