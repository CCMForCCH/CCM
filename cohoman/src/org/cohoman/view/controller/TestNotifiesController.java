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
import org.cohoman.model.singletons.ConfigScalarValues;

@ManagedBean
@SessionScoped
public class TestNotifiesController implements Serializable {

	private static final long serialVersionUID = 5716065327257928138L;

	private String emailSubject;
	private String emailBody;
	private String chosenUserString;
	private String textMessage;
	private UserService userService = null;
	private ListsService listsService = null;

	public TestNotifiesController() {
	}

	private void clearFormFields() {
		chosenUserString = "";
		emailSubject = "";
		emailBody = "";
		textMessage = "";
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

	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
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
		if (theUser.getEmail() == null || theUser.getEmail().isEmpty()) {
			FacesMessage message = new FacesMessage(
					"User Error: this user has no valid email address.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		
		// Actually send the email!!
		listsService.sendEmailToAddress(theUser.getEmail(), emailSubject, emailBody);
		
		// clear out fields for return to the page in this session
		clearFormFields();

		return "sendEmail";
	}

	public String sendTextMessage() throws Exception {
		
		if (chosenUserString == null || chosenUserString.isEmpty()) {
			FacesMessage message = new FacesMessage(
					"User Error: you must choose a user.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		Long userid = Long.valueOf(chosenUserString);
		User theUser = userService.getUser(userid);

		if (theUser.getCellphone() == null || theUser.getCellphone().isEmpty()) {
			FacesMessage message = new FacesMessage(
					"User Error: this user has no valid cellphone number.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		
		// Actually send the text message!!
		listsService.sendTextMessageToPerson(theUser.getCellphone(), textMessage);
		
		// clear out fields for return to the page in this session
		clearFormFields();

		return "sendTextMessage";
	}

	public List<User> getUserList() {
		List<User> fullUserList = userService.getUsersHereNow();
		return fullUserList;
	}

}
