package org.cohoman.view.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.business.ListsManagerImpl.SecurityDataForRow;
import org.cohoman.model.business.ListsManagerImpl.SecurityRow;
import org.cohoman.model.business.User;
import org.cohoman.model.integration.persistence.beans.CchSectionTypeEnum;
import org.cohoman.model.integration.persistence.beans.SubstitutesBean;
import org.cohoman.model.service.ListsService;
import org.cohoman.model.service.UserService;

@ManagedBean
@SessionScoped
public class RetrieveSecurityListController implements Serializable {

	private static final long serialVersionUID = 4678206276499587830L;

	private List<SecurityRow> chSecurityList;
	private List<SecurityDataForRow> chSecurityListWithSubs;
	private ListsService listsService = null;
	private String securityStartDate;
	private String originalSecurityPerson;
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

	public ListsService getListsService() {
		return listsService;
	}

	public void setListsService(ListsService listsService) {
		this.listsService = listsService;
	}

	public String getSecurityStartDate() {

		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		securityStartDate = requestParams.get("securityStartDate");
		if (securityStartDate != null && securityStartDate.length() > 0) {
			// Have a new chosenPrivateEventId to set
		} else {
			// Don't have a new chosenPrivate EventId to set: use old one
			// unless it's also not set (don't know why 8/21/16)
			throw new RuntimeException("securityStartDate isn't set");
		}

		return securityStartDate;
	}

	public void setSecurityStartDate(String securityStartDate) {
		this.securityStartDate = securityStartDate;
	}

	public String getOriginalSecurityPerson() {

		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		originalSecurityPerson = requestParams.get("originalSecurityPerson");
		if (originalSecurityPerson != null
				&& originalSecurityPerson.length() > 0) {
			// Have a new chosenPrivateEventId to set
		} else {
			// Don't have a new chosenPrivate EventId to set: use old one
			// unless it's also not set (don't know why 8/21/16)
			throw new RuntimeException("originalSecurityPerson isn't set");
		}

		return originalSecurityPerson;
	}

	public void setOriginalSecurityPerson(String originalSecurityPerson) {
		this.originalSecurityPerson = originalSecurityPerson;
	}

	public List<SecurityRow> getChSecurityList() {
		chSecurityList = listsService
				.getSecurityList(CchSectionTypeEnum.COMMONHOUSE);
		return chSecurityList;
	}

	public List<SecurityDataForRow> getChSecurityListWithSubs() {
		chSecurityListWithSubs = listsService
				.getSecurityListWithSubs(CchSectionTypeEnum.COMMONHOUSE);
		return chSecurityListWithSubs;
	}

	public List<SecurityRow> getWeSecurityList() {
		chSecurityList = listsService
				.getSecurityList(CchSectionTypeEnum.WESTEND);
		return chSecurityList;
	}

	public List<User> getUserList() {
		List<User> fullUserList = userService.getUsersHereNow();
		// First time thru, set user to first of list as that's what's
		// displayed.
		if (chosenUserString == null) {
			chosenUserString = fullUserList.get(0).getUserid().toString();
		}

		return fullUserList;
	}

	public String modifySubstituteTableView() throws Exception {

		// Perform actual update of the substitute table

		// Special-case an empty string. It will cause any
		// entry to be deleted.
		SubstitutesBean substitutesBean;
		Date securityStartDateAsDate;
		if (chosenUserString == null || chosenUserString.isEmpty()) {
			
			// Start with date in Jan 1, 2017 but need to convert it to
			// a SQL format, namely 2017-01-01.
			SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
			securityStartDateAsDate = formatter.parse(securityStartDate);
		
			SimpleDateFormat formatterForSQL = new SimpleDateFormat("yyyy-MM-dd");
			String startingDateAsStringForSQL = formatterForSQL.format(securityStartDateAsDate);
			
			substitutesBean = listsService.getSubstitute(startingDateAsStringForSQL);
			if (substitutesBean != null) {
				listsService.deleteSubstitute(substitutesBean
						.getSubstitutesid());
			}
		} else {
			Long userId = Long.parseLong(chosenUserString);

			SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
			securityStartDateAsDate = formatter.parse(securityStartDate);

			listsService.setSubstitute(securityStartDateAsDate, userId);
		}

		return "displayChSecurityList";
	}

}
