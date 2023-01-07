package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.cohoman.model.business.User;
import org.cohoman.model.dto.ProblemItemDTO;
import org.cohoman.model.service.ListsService;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.MaintenanceTypeEnums;
import org.cohoman.view.controller.utils.ProblemLocationEnums;
import org.cohoman.view.controller.utils.ProblemStateEnums;
import org.cohoman.view.controller.utils.ProblemStatusEnums;
import org.cohoman.view.controller.utils.ProblemTypeEnums;
import org.cohoman.view.controller.utils.SortEnums;
import org.cohoman.view.controller.utils.ProblemPriorityEnums;
import org.cohoman.view.controller.utils.TaskStatusEnums;

@ManagedBean
@SessionScoped
public class CreateProblemItemController implements Serializable {

	// Action: call the service layer to build and return a schedule
	// which is basically a list of weeks of days containing events

	/**
	 * 
	 */
	private static final long serialVersionUID = 4678206276499587830L;
	private String itemname;
	private String itemdescription;
	private Date itemCreatedDate;
	private Long itemCreatedBy;
	private String priority;
	private UserService userService = null;
	private ListsService listsService = null;
	private ProblemPriorityEnums chosenpriority = null;
	private ProblemTypeEnums chosenproblemtype;
	private ProblemLocationEnums chosenproblemlocation;


	public CreateProblemItemController() {
	}

	private void clearFormFields() {
		
		itemname = "";
		itemdescription = "";
		priority = "";
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

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getItemdescription() {
		return itemdescription;
	}

	public void setItemdescription(String itemdescription) {
		this.itemdescription = itemdescription;
	}

	public Date getItemCreatedDate() {
		return itemCreatedDate;
	}

	public void setItemCreatedDate(Date itemCreatedDate) {
		this.itemCreatedDate = itemCreatedDate;
	}

	public Long getItemCreatedBy() {
		return itemCreatedBy;
	}

	public void setItemCreatedBy(Long itemCreatedBy) {
		this.itemCreatedBy = itemCreatedBy;
	}

	/*
	 * Priority
	 */
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public ProblemPriorityEnums getChosenpriority() {
		return chosenpriority;
	}

	public void setChosenpriority(ProblemPriorityEnums chosenpriority) {
		this.chosenpriority = chosenpriority;
	}

	public ProblemPriorityEnums[] getItempriorities() {
		return ProblemPriorityEnums.values();
	}
	
	/*
	 * Problem Type
	 */
	
	public ProblemTypeEnums getChosenproblemtype() {
		return chosenproblemtype;
	}

	public void setChosenproblemtype(ProblemTypeEnums chosenproblemtype) {
		this.chosenproblemtype = chosenproblemtype;
	}

	public ProblemTypeEnums[] getItemproblemtype() {
		return ProblemTypeEnums.values();
	}

	/*
	 * Problem Location
	 */
	
	public ProblemLocationEnums getChosenproblemlocation() {
		return chosenproblemlocation;
	}

	public void setChosenproblemlocation(ProblemLocationEnums chosenproblemlocation) {
		this.chosenproblemlocation = chosenproblemlocation;
	}


	public ProblemLocationEnums[] getItemproblemlocation() {
		return ProblemLocationEnums.values();
	}

	// Method for drop-down of expected number of months between 
	// item performances
	public List<String> getNumberOfMonths() {

		List<String> countList = new ArrayList<String>();
		for (int idx = 1; idx <= 36; idx++) {
			countList.add(Integer.toString(idx));
		}
		return countList;
	}

	public String addProblemItem() throws Exception {

		ProblemItemDTO dto = createProblemItemDTO();
		
		// Make sure that this is a unique problem item name
		List<ProblemItemDTO> allProblemItems = 
				listsService.getProblemItems(ProblemStateEnums.ALLPROBLEMS);
		for (ProblemItemDTO oneItem : allProblemItems) {
			if (oneItem.getItemname().equalsIgnoreCase(dto.getItemname())) {
				FacesMessage message = new FacesMessage(
						"Already have an item name of " + dto.getItemname());
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, message);
				return null;
			}
		}

		try {
			listsService.createProblemItem(dto);
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		// Determine if text messages need to be sent.
		String problemPriority = dto.getPriority();
		String problemPriorityText = ProblemPriorityEnums.valueOf(dto.getPriority()).toString();
		
		// Get all users
	/*
		List<User> theusers = userService.getAllUsers();
		User foundUser = null;
		if (problemPriority.equals(ProblemPriorityEnums.P3HIGH.name())) {
			foundUser = getUserStructure(theusers, "bill");
			if (foundUser != null) {
				listsService.sendTextMessageToPerson(
						foundUser.getCellphone(),
						"CCM: New " + problemPriorityText
								+ " priority problem report: "
								+ dto.getItemname());
			}
		} else if (problemPriority.equals(ProblemPriorityEnums.P2EMERGENCY
				.name())) {
			foundUser = getUserStructure(theusers, "bill");
			if (foundUser != null) {
				listsService.sendTextMessageToPerson(
						foundUser.getCellphone(),
						"CCM: New " + problemPriorityText
								+ " priority problem report: "
								+ dto.getItemname());
			}

		} else if (problemPriority.equals(ProblemPriorityEnums.P1CRITICAL
				.name())) {
	*/
			/*
			for (User oneuser : theusers) {
				foundUser = getUserStructure(theusers, oneuser.getUsername());
				if (foundUser != null) {
					listsService.sendTextMessageToPerson(
							foundUser.getCellphone(),
							"CCM: New " + problemPriorityText
									+ " priority problem report: "
									+ dto.getItemname());
					foundUser = null;
				} 
			}
			*/
		//}

		// clear out fields for return to the page in this session
		clearFormFields();
		return "addProblemItem";
	}
	
	private User getUserStructure(List<User> allusers, String person) {
		User returnedUser = null;
		for (User oneUser : allusers) {
			if (oneUser.getUsername().equals("bill")) {
				returnedUser = oneUser;
			}	
		}
		return returnedUser;
	}
	// method to create a Problem Item
	private ProblemItemDTO createProblemItemDTO() {
		ProblemItemDTO dto = new ProblemItemDTO();
		dto.setItemname(itemname);
		dto.setItemdescription(itemdescription);
		dto.setPriority(chosenpriority.name());
		dto.setLocation(chosenproblemlocation.name());
		dto.setProblemType(chosenproblemtype.name());
		dto.setProblemStatus(ProblemStatusEnums.NEW.name());

		GregorianCalendar now = new GregorianCalendar();
		dto.setItemCreatedDate(now.getTime());
		
		// get the userid of the current user to set the requester.
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession)ctx.getExternalContext().getSession(true); 
		User dbUser = (User) session.getAttribute(AuthenticateController.SESSIONVAR_USER_NAME);
		dto.setItemCreatedBy(dbUser.getUserid());
		return dto;
	}
		
}
