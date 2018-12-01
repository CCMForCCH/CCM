package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import org.cohoman.model.business.User;
import org.cohoman.model.service.UserService;

@ManagedBean
@SessionScoped
public class DisplayEmergencyInfoController implements Serializable {

	private static final long serialVersionUID = 4678206276499587830L;

	private String chosenUserString;
	private UserService userService = null;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getChosenUserString() {
		return chosenUserString;
	}

	public void setChosenUserString(String chosenUserString) {
		this.chosenUserString = chosenUserString;
	}

	public List<User> getUserList() {
		List<User> fullUserList = userService.getUsersHereAndAway();
		// First time thru, set user to first of list as that's what's
		// displayed.
		if (chosenUserString == null) {
			chosenUserString = fullUserList.get(0).getUserid().toString();
		}

		return fullUserList;
	}

	public String getUserToPrint() {
		Long userId = Long.parseLong(chosenUserString);
		User chosenUser = userService.getUser(userId);
		String userName = chosenUser.getFirstname() + " "
				+ chosenUser.getLastname();
		return userName;
	}

	public String getEmergencyInfoToPrint() {

		Long userId = Long.parseLong(chosenUserString);
		User chosenUser = userService.getUser(userId);
		String emergencyInfo = chosenUser.getEmergencyinfo();

		// Convert string into lines, add HTML line break, make back into
		// a string.
		if (emergencyInfo == null || emergencyInfo.isEmpty()) {
			emergencyInfo = 
				"No emergency information has been entered in this person's user profile.";
		}
		String[] parts = emergencyInfo.split("[\\r\\n]+");
		String modifiedEmergencyInfo = "";
		for (int i = 0; i < parts.length; i++) {
			modifiedEmergencyInfo += parts[i] + "<br />";
		}

		return modifiedEmergencyInfo;
	}

}
