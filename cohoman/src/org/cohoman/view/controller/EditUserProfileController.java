package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.cohoman.model.business.User;
import org.cohoman.model.integration.persistence.beans.UnitBean;
import org.cohoman.model.service.ConfigurationService;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.Validators;

@ManagedBean
@SessionScoped
public class EditUserProfileController implements Serializable {

	private static final long serialVersionUID = -6645516510256723073L;
	
	private UserService userService = null;
	private ConfigurationService configurationService = null;
	private User currentUser;
	private String chosenUnitString;
	private List<UnitBean> unitBeanList;

	public EditUserProfileController() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession)ctx.getExternalContext().getSession(false); 
		
		// populate fields initially with what's in the session from login
		currentUser = 
			(User)session.getAttribute(AuthenticateController.SESSIONVAR_USER_NAME);
		chosenUnitString = currentUser.getUnit();
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

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public String getChosenUnitString() {
		return chosenUnitString;
	}

	public void setChosenUnitString(String chosenUnitString) {
		this.chosenUnitString = chosenUnitString;
	}

	public List<UnitBean> getUnitBeanList() {
		unitBeanList = configurationService.getAllUnits();
		return unitBeanList;
	}
/*
	public void setUnitBeanList(List<UnitBean> unitBeanList) {
		this.unitBeanList = unitBeanList;
	}
*/
	public String editUserProfileView() throws Exception {

		// Validate email address
		if (!Validators.isValidEmailAddress(currentUser.getEmail())) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					"Invalid email address format.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;
		}

		// Validate fields with phone numbers
		if (!Validators.isValidPhoneNumber(currentUser.getHomephone())) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					"Invalid home phone number format. Must be of the format 999-999-9999");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;
		}
		if (!Validators.isValidPhoneNumber(currentUser.getWorkphone())) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					"Invalid work phone number format. Must be of the format 999-999-9999");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;
		}
		if (!Validators.isValidPhoneNumber(currentUser.getCellphone())) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(
					"Invalid cell phone number format. Must be of the format 999-999-9999");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;
		}
		
		userService.editUser(currentUser);	
		return "gohome";
	}

}
