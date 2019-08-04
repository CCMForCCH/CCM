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
import org.cohoman.model.business.trash.TrashRow;
import org.cohoman.model.business.trash.TrashSchedule;
import org.cohoman.model.integration.persistence.beans.CchSectionTypeEnum;
import org.cohoman.model.integration.persistence.beans.SubstitutesBean;
import org.cohoman.model.service.ListsService;
import org.cohoman.model.service.UserService;

@ManagedBean
@SessionScoped
public class RetrieveTrashListController implements Serializable {

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

	public List<TrashRow> getTrashList() {
		return listsService.getTrashSchedule();
	}

/*
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

*/

}
