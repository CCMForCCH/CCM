package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.business.User;
import org.cohoman.model.business.trash.TrashRolesEnums;
import org.cohoman.model.integration.persistence.beans.UserTypeEnum;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.cohoman.view.controller.utils.PasswordCheckingUtil;
import org.cohoman.view.controller.utils.Validators;
import org.mindrot.jbcrypt.BCrypt;

@ManagedBean
@SessionScoped
public class EditUserController implements Serializable {

	private static final long serialVersionUID = 4678206276499587830L;

	private List<User> userList;
	private User chosenUser;
	private String chosenUserString;
	private String userOperation;
	private UserService userService = null;
	private String newpassword;
	private int birthday;
	private int birthmonth;
	private int birthyear;
	private String usertype;
	private boolean allowtexting;
	private TrashRolesEnums trashrole;

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

	public String getUserOperation() {
		return userOperation;
	}

	public void setUserOperation(String userOperation) {
		this.userOperation = userOperation;
	}

	public List<User> getUserList() {
		userList = userService.getAllUsers();
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public User getChosenUser() {
		return chosenUser;
	}

	public void setChosenUser(User chosenUser) {
		this.chosenUser = chosenUser;
	}

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	public int getBirthday() {
		return birthday;
	}

	public void setBirthday(int birthday) {
		this.birthday = birthday;
	}

	public int getBirthmonth() {
		return birthmonth;
	}

	public void setBirthmonth(int birthmonth) {
		this.birthmonth = birthmonth;
	}

	public int getBirthyear() {
		return birthyear;
	}

	public void setBirthyear(int birthyear) {
		this.birthyear = birthyear;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public CalendarUtils.OneMonth[] getMonthsOfTheYear() {
		return CalendarUtils.getMonthsOfTheYear();
	}

	public String[] getDaysOfTheMonth() {
		return CalendarUtils.getDaysOfTheMonth(birthyear, birthmonth);
	}

	public String[] getYears() {
		return CalendarUtils.getBirthYears();
	}

	public UserTypeEnum[] getUserTypes() {
		return UserTypeEnum.values();
	}

	public boolean isAllowtexting() {
		return allowtexting;
	}

	public void setAllowtexting(boolean allowtexting) {
		this.allowtexting = allowtexting;
	}

	public TrashRolesEnums getTrashrole() {
		return trashrole;
	}

	public void setTrashrole(TrashRolesEnums trashrole) {
		this.trashrole = trashrole;
	}

	public TrashRolesEnums[] getTrashroles() {
		return TrashRolesEnums.values();
	}

	public String editUserView() throws Exception {

		Long userId = Long.valueOf(chosenUserString);
		chosenUser = userService.getUser(userId);
		if (userOperation.equals("deleteUser")) {
			userService.deleteUser(chosenUser);
		}
		return userOperation;
	}

	public String editUserItemsView() throws Exception {

		// Already have actual User structure.
		// First set the new login date
		chosenUser.setLastlogin(new Date());

		// Next, check the newpassword field. If it's
		// empty, keep hashed password as is. But if there
		// is a new password, hash the plaintext password
		// and enter it into the User structure so the
		// new hashed password will be written to the DB.
		if (!newpassword.isEmpty()) {
			// Need to create new hashed password. But first, make sure
			// the new password is strong.
			FacesContext context = FacesContext.getCurrentInstance();

			// First check that the password is good
			if (!PasswordCheckingUtil.isPasswordGood(newpassword)) {
				FacesMessage message = new FacesMessage(
						"Password must have at least one number, one letter, one special character, be at least 8 characters, and not contain obvious words like \"password\". Choose another one.");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				context.addMessage(null, message);
				return null;

			}

			// Password is strong. Now hash it.
			String hashedNewPassword = BCrypt.hashpw(newpassword,
					BCrypt.gensalt());
			chosenUser.setPassword(hashedNewPassword);
		}

		// Validate email address (if given ...)
		if (chosenUser.getEmail() != null && chosenUser.getEmail().length() > 0
				&& !Validators.isValidEmailAddress(chosenUser.getEmail())) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					"Invalid email address format.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;
		}

		// Validate fields with phone numbers
		if (!Validators.isValidPhoneNumber(chosenUser.getHomephone())) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					"Invalid home phone number format. Must be of the format 999-999-9999");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;
		}
		if (!Validators.isValidPhoneNumber(chosenUser.getWorkphone())) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					"Invalid work phone number format. Must be of the format 999-999-9999");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;
		}
		if (!Validators.isValidPhoneNumber(chosenUser.getCellphone())) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					"Invalid cell phone number format. Must be of the format 999-999-9999");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;
		}

		// Write new user changes to DB
		userService.editUser(chosenUser);
		return "gohome";
	}

}
