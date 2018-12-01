package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.cohoman.model.business.User;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.PasswordCheckingUtil;
import org.mindrot.jbcrypt.BCrypt;

@ManagedBean
@SessionScoped
public class ChangeUserPasswordController implements Serializable {

	private static final long serialVersionUID = -6645516510256723073L;

	Logger logger = Logger.getLogger(this.getClass().getName());

	private UserService userService = null;
	private User currentUser;
	private String currentPassword;
	private String newPassword1;
	private String newPassword2;

	public ChangeUserPasswordController() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(false);

		// populate fields initially with what's in the session from login
		currentUser = (User) session
				.getAttribute(AuthenticateController.SESSIONVAR_USER_NAME);
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public String getNewPassword1() {
		return newPassword1;
	}

	public void setNewPassword1(String newPassword1) {
		this.newPassword1 = newPassword1;
	}

	public String getNewPassword2() {
		return newPassword2;
	}

	public void setNewPassword2(String newPassword2) {
		this.newPassword2 = newPassword2;
	}

	public String changeUserPasswordView() throws Exception {

		// Perform error checking on passwords first.
		FacesContext context = FacesContext.getCurrentInstance();

		if (!BCrypt.checkpw(currentPassword, currentUser.getPassword())) {
			FacesMessage message = new FacesMessage(
					"Entered current password does not match actual current password. Enter it again.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;
		}
		if (!newPassword1.equals(newPassword2)) {
			FacesMessage message = new FacesMessage(
					"Entered New Password values don't match. Enter it again.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;
		}
		if (currentPassword.equals(newPassword1)) {
			FacesMessage message = new FacesMessage(
					"Cannot enter the existing password as a new password. Enter a new one.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;
		}

		// First check that the password is good
		if (!PasswordCheckingUtil.isPasswordGood(newPassword1)) {
			FacesMessage message = new FacesMessage(
					"New password must be at least 8 characters long, must not contain obvious words like \"password\", and must contain at least 3 of the following characters:" +
					"Uppercase letter, Lowercase letter, Number, or a Symbol." + "Choose another password.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;

		}

		logger.info("AUDIT: User " + currentUser.getUsername()
				+ " is changing their password.");

		// OK, hash new password using jBCrypt
		String hashedNewPassword = BCrypt
				.hashpw(newPassword1, BCrypt.gensalt());

		// Write out new password to the DB
		currentUser.setPassword(hashedNewPassword);
		userService.editUser(currentUser);
		return "gohome";
	}

}
