package org.cohoman.model.service;

import java.util.List;
import java.util.logging.Logger;

import org.cohoman.model.business.User;
import org.cohoman.model.business.UserManager;
import org.cohoman.model.dto.UserDTO;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.view.controller.CohomanException;

public class UserServiceImpl implements UserService {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private UserManager userManager = null;

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void createUser(UserDTO theUser) throws CohomanException {
		logger.info("AUDIT: Creating user " + theUser.getUsername()
				+ " with the following: " + theUser.getFirstname() + " "
				+ theUser.getLastname() + ", " + theUser.getCellphone() + ", "
				+ theUser.getHomephone() + ", " + theUser.getWorkphone() + ", "
				+ theUser.getEmail());
		userManager.createUser(theUser);
	}

	public User authenticateUser(UserDTO theUser) throws Exception {
		User dbUser = userManager.getUser(theUser);
		if (dbUser == null) {
			throw new Exception("No such user exists: " + theUser.getUsername());
		}
		return dbUser;
	}

	public List<Role> getRolesForUser(Long userId) {
		return userManager.getRolesForUser(userId);
	}

	public List<Role> getAvailableRolesForUser(Long userId) {
		return userManager.getAvailableRolesForUser(userId);
	}

	public List<User> getAllUsers() {
		return userManager.getAllUsers();
	}
	public List<User> getUsersHereAndAway() {
		return userManager.getUsersHereAndAway();
	}
	public List<User> getUsersChildren() {
		return userManager.getUsersChildren();
	}
	public List<User> getUsersHereNow() {
		return userManager.getUsersHereNow();
	}
	public List<User> getUsersForBirthdays() {
		return userManager.getUsersForBirthdays();
	}


	public User getUser(Long userId) {
		return userManager.getUser(userId);
	}

	public void editUser(User theUser) throws Exception {

		logger.info("AUDIT: Editing user " + theUser.getUsername()
				+ " with the following: " + theUser.getFirstname() + " "
				+ theUser.getLastname() + ", " + theUser.getCellphone() + ", "
				+ theUser.getHomephone() + ", " + theUser.getWorkphone() + ", "
				+ theUser.getEmail());
		try {
			userManager.editUser(theUser);
		} catch (Exception ex) {
			logger.severe("Failure to edit user " + theUser.getUsername()
					+ " due to exception " + ex.toString());
			throw ex;
		}
	}

	public void deleteUser(User theUser) throws Exception {

		logger.info("AUDIT: Deleting user " + theUser.getUsername()
				+ " with the following: " + theUser.getFirstname() + " "
				+ theUser.getLastname() + ", " + theUser.getCellphone() + ", "
				+ theUser.getHomephone() + ", " + theUser.getWorkphone() + ", "
				+ theUser.getEmail());
		try {
			userManager.deleteUser(theUser);
		} catch (Exception ex) {
			logger.severe("Failure to delete user " + theUser.getUsername()
					+ " due to exception " + ex.toString());
			throw ex;
		}
	}

	public void addRole(Long userId, Long roleId) {
		logger.info("AUDIT: Adding role " + roleId + " for user " + userId);
		userManager.addRole(userId, roleId);
	}

	public void deleteRole(Long userId, Long roleId) {
		logger.info("AUDIT: Deleting role " + roleId + " for user " + userId);
		userManager.deleteRole(userId, roleId);
	}

}
