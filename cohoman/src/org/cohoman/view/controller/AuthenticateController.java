package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

import org.cohoman.model.business.User;
import org.cohoman.model.dto.UserDTO;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.model.service.EventService;
import org.cohoman.model.service.UserService;

public class AuthenticateController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2107273483425518900L;
	/**
	 * 
	 */

	Logger logger = Logger.getLogger(this.getClass().getName());

	public static final String SESSIONVAR_USER_NAME = "CCH_USER";
	public static final String SESSIONVAR_USER_NAME_ID = "CCH_USER_ID";
	public static final String SESSIONVAR_TARGET_URL = "target_URL";
	public static final String SESSIONVAR_ROLES = "roles";
	public static final String SESSIONVAR_CHOSEN_ROLE = "chosen_role";
	public static final String SESSIONVAR_KIOSK_KEY = "CCH_KIOSK_KEY";
	public static final String SESSIONVAR_KIOSK_VALUE = "Fx8oVKeLvhzKs5Gf";

	// unclear why login has to be .faces, but otherwise JSF servlet doesn't
	// recognize the page as being JSF even though page is named
	// login.xhtml
	public static final String LOGIN_PATH = "/login.faces";
	public static final String CHOOSEROLE_PATH = "/chooseRole.faces";
	private EventService eventService = null;
	private UserService userService = null;

	private String username;
	private String password;
	private boolean loggedin = false;
	private Collection<Role> rolesList;
	private String chosenRoleString;

	public String authenticate() throws Exception {
		String result = "failure";
		FacesContext ctx = FacesContext.getCurrentInstance();

		UserDTO aUser = new UserDTO();
		aUser.setUsername(getUsername());
		aUser.setPassword(getPassword());
		User dbUser = null;

		if (isTheKiosk()) {
			try {
				dbUser = userService.authenticateUser(aUser);
			} catch (Exception ex) {
				ctx.addMessage(null, new FacesMessage(""
						+ "You entered an invalid login. Please try again."));
				return result;
			}
			
			// Just allow cohouser user role
			rolesList = eventService.getRolesForUser(dbUser.getUserid());
			Role roleToKeep = null;
			for (Role oneRole : rolesList) {
				if (oneRole.getRolename().equals("basicuser")) {
					roleToKeep = oneRole;
				}
			}
			rolesList.clear();
			rolesList.add(roleToKeep);
			logger.log(Level.INFO, "AUDIT: User " + dbUser.getUsername()
					+ " just logged in on the kiosk.");
		} else {
			try {
				dbUser = eventService.authenticateUser(aUser);
			} catch (Exception ex) {
				ctx.addMessage(null, new FacesMessage(""
						+ "You entered an invalid login. Please try again."));
				return result;
			}

			rolesList = eventService.getRolesForUser(dbUser.getUserid());
			logger.log(Level.INFO, "AUDIT: User " + dbUser.getUsername()
					+ " just logged in.");
		}

		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(true);
		session.setAttribute(SESSIONVAR_USER_NAME, dbUser);
		session.setAttribute(SESSIONVAR_ROLES, rolesList);

		loggedin = true;

		result = "success";
		return result;
	}

	public String chooseRoleView() {
		for (Role oneRole : rolesList) {
			if (chosenRoleString.equals(Long.toString(oneRole.getRoleid()))) {
				// Set the chosen role in the session. That way it will be
				// seen by the ChooseRoleFilter ...
				FacesContext ctx = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) ctx.getExternalContext()
						.getSession(false);
				session.setAttribute(SESSIONVAR_CHOSEN_ROLE, oneRole);
				// return "success";
				return oneRole.getRolename();
			}
		}
		// TODO: handle better?
		return "failure";
	}

	public boolean isTheKiosk() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(false);
		String kioskKey = (String) session.getAttribute(SESSIONVAR_KIOSK_KEY);
		if (kioskKey == null) {
			return false;
		} else {
			return true;
		}
	}

	public void validateUsername(FacesContext context, UIComponent toValidate,
			Object value) throws ValidatorException {
		String username = (String) value;
		if (username == null || username.length() == 0) {
			FacesMessage message = new FacesMessage("Username cannot be empty");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public String logout() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(false);
		if (session != null) {
			User dbUser = (User) session.getAttribute(SESSIONVAR_USER_NAME);
			logger.log(Level.INFO, "AUDIT: User " + dbUser.getUsername()
					+ " just logged out.");
			session.invalidate();
		}
		loggedin = false;
		return "success";
	}

	public String getUsername() {
		if (!(username == null || username.isEmpty())) {
			return username.trim();
		} else {
			return username;
		}
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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

	public boolean isLoggedin() {
		return loggedin;
	}

	public void setLoggedin(boolean loggedin) {
		this.loggedin = loggedin;
	}

	public Collection<Role> getRolesList() {
		return rolesList;
	}

	public void setRolesList(Collection<Role> rolesList) {
		this.rolesList = rolesList;
	}

	public String getChosenRoleString() {
		return chosenRoleString;
	}

	public void setChosenRoleString(String chosenRoleString) {
		this.chosenRoleString = chosenRoleString;
	}

}
