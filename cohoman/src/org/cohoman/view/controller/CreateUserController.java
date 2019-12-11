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
import org.cohoman.model.dto.UserDTO;
import org.cohoman.model.integration.persistence.beans.UserTypeEnum;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.cohoman.view.controller.utils.PasswordCheckingUtil;
import org.cohoman.view.controller.utils.PotluckCategoriesEnums;
import org.cohoman.view.controller.utils.Validators;
import org.mindrot.jbcrypt.BCrypt;

@ManagedBean
@SessionScoped
public class CreateUserController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4678206276499587830L;
	private String firstname;
	private String lastname;
	private String homephone;
	private String cellphone;
	private String workphone;
	private String email;
	private String unit;
	private String password;
	private Date lastlogin;
	private String username;
	private String birthyear = "1970";
	private String birthmonth = "0"; // January starts at 0
	private String birthday = "1";
	private String chosenusertype;
	private boolean allowtexting;
	private TrashRolesEnums trashrole;


	private UserService userService = null;

	public CreateUserController() {
		lastlogin = new Date();
	}

	public String createUser() {

		// Perform error checking on passwords first.
		FacesContext context = FacesContext.getCurrentInstance();

		// First check that the password is good
		if (!PasswordCheckingUtil.isPasswordGood(password)) {
			FacesMessage message = new FacesMessage(
					"New password must be at least 8 characters long, must not contain obvious words like \"password\", and must contain at least 3 of the following characters:"
							+ "Uppercase letter, Lowercase letter, Number, or a Symbol."
							+ "Choose another password.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;

		}

		// Hash password before saving it into the user DB
		String hashedNewPassword = BCrypt.hashpw(password, BCrypt.gensalt());

		// Validate email address (if given ...)
		if (email != null && email.length() > 0
				&& !Validators.isValidEmailAddress(email)) {
			FacesMessage message = new FacesMessage(
					"Invalid email address format.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;
		}

		// Validate phone numbers
		if (!Validators.isValidPhoneNumber(homephone)) {
			FacesMessage message = new FacesMessage(
					"Invalid home phone number format. Must be of the format 999-999-9999");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;
		}
		if (!Validators.isValidPhoneNumber(workphone)) {
			FacesMessage message = new FacesMessage(
					"Invalid work phone number format. Must be of the format 999-999-9999");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;
		}
		if (!Validators.isValidPhoneNumber(cellphone)) {
			FacesMessage message = new FacesMessage(
					"Invalid cell phone number format. Must be of the format 999-999-9999");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;
		}

		// Pass the user info down to the user service
		UserDTO theUser = new UserDTO();
		theUser.setCellphone(cellphone);
		theUser.setEmail(email);
		theUser.setFirstname(firstname);
		theUser.setHomephone(homephone);
		theUser.setLastlogin(lastlogin);
		theUser.setLastname(lastname);
		theUser.setPassword(hashedNewPassword);
		theUser.setUnit(unit);
		theUser.setUsername(username);
		theUser.setWorkphone(workphone);
		theUser.setBirthday(Integer.parseInt(birthday));
		theUser.setBirthmonth(Integer.parseInt(birthmonth));
		theUser.setBirthyear(Integer.parseInt(birthyear));
		theUser.setUsertype(chosenusertype);
		theUser.setTrashrole(trashrole.name());
		theUser.setAllowtexting(allowtexting);
		List<User> currentUsers = userService.getAllUsers();
		for (User oneUser : currentUsers) {
			if (oneUser.getUsername().equalsIgnoreCase(username)) {
				FacesMessage message = new FacesMessage(
						"Already have a user name of " + username);
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, message);
				return null;
			}
		}

		try {
			userService.createUser(theUser);
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		} catch (Exception ex) {
			FacesMessage message = new FacesMessage(ex.getCause()
					+ ex.getMessage());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		return "createUser";
	}

	public CalendarUtils.OneMonth[] getMonthsOfTheYear() {
		return CalendarUtils.getMonthsOfTheYear();
	}

	public String[] getYears() {
		return CalendarUtils.getBirthYears();
	}

	public String[] getDaysOfTheMonth() {
		return CalendarUtils.getDaysOfTheMonth(Integer.parseInt(birthyear),
				Integer.parseInt(birthmonth));
	}

	public UserTypeEnum[] getUserTypes() {
		return UserTypeEnum.values();
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getHomephone() {
		return homephone;
	}

	public void setHomephone(String homephone) {
		this.homephone = homephone;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getWorkphone() {
		return workphone;
	}

	public void setWorkphone(String workphone) {
		this.workphone = workphone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getLastlogin() {
		return lastlogin;
	}

	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBirthyear() {
		return birthyear;
	}

	public void setBirthyear(String birthyear) {
		this.birthyear = birthyear;
	}

	public String getBirthmonth() {
		return birthmonth;
	}

	public void setBirthmonth(String birthmonth) {
		this.birthmonth = birthmonth;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getChosenusertype() {
		return chosenusertype;
	}

	public void setChosenusertype(String chosenusertype) {
		this.chosenusertype = chosenusertype;
	}

	public TrashRolesEnums[] getTrashroles() {
		return TrashRolesEnums.values();
	}

	public TrashRolesEnums getTrashrole() {
		return trashrole;
	}

	public void setTrashrole(TrashRolesEnums trashrole) {
		this.trashrole = trashrole;
	}

	public boolean isAllowtexting() {
		return allowtexting;
	}

	public void setAllowtexting(boolean allowtexting) {
		this.allowtexting = allowtexting;
	}

}
