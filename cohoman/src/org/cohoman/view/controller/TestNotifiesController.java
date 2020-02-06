package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.business.User;
import org.cohoman.model.service.ListsService;
import org.cohoman.model.service.UserService;

@ManagedBean
@SessionScoped
public class TestNotifiesController implements Serializable {

	private static final long serialVersionUID = 5716065327257928138L;

	private String emailSubject;
	private String emailBody;
	private String chosenUserString;
	private UserService userService = null;
	private ListsService listsService = null;

	public TestNotifiesController() {
	}

	private void clearFormFields() {
		chosenUserString = "";
		emailSubject = "";
		emailBody = "";
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ListsService getListsService() {
		return listsService;
	}

	public void setListsService(ListsService listsService) {
		this.listsService = listsService;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public String getChosenUserString() {
		return chosenUserString;
	}

	public void setChosenUserString(String chosenUserString) {
		this.chosenUserString = chosenUserString;
	}

	public String sendEmail() throws Exception {
		
		if (chosenUserString == null || chosenUserString.isEmpty()) {
			FacesMessage message = new FacesMessage(
					"User Error: you must choose a user.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		Long userid = Long.valueOf(chosenUserString);
		User theUser = userService.getUser(userid);
		
		listsService.sendEmailToAddress(theUser.getEmail(), emailSubject, emailBody);
		
		// clear out fields for return to the page in this session
		clearFormFields();

		return "sendEmail";
	}

	public List<User> getUserList() {
		List<User> fullUserList = userService.getUsersHereNow();
		return fullUserList;
	}

}
