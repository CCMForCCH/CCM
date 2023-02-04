package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.cohoman.model.business.User;
import org.cohoman.model.dto.ProblemUpdateDTO;
import org.cohoman.model.service.ListsService;
import org.cohoman.model.service.UserService;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class ProblemUpdateController implements Serializable {

	private static final long serialVersionUID = 4678206276499587830L;

	private List<ProblemUpdateDTO> problemUpdateDTOList;
	private ListsService listsService = null;
	private UserService userService = null;

	private Long chosenProblemItemIdAsLong;
	private String chosenProblemItemId;

	private String chosenProblemUpdateId;
	private String chosenProblemUpdateIdLast;
	private String chosenProblemItemName;
	private Date chosenProblemUpdateDate;
	private Long chosenItemCreatedBy;
	
	private ProblemUpdateDTO currentProblemUpdateDTO;

	private Long problemUpdateid;
	private Date problemUpdateDate;
	private Long itemCreatedBy;
	private String notes;
	
	private String username;
	private Date printableProblemUpdateDate;

	private String chosenProblemUpdateItemId;
	private String callingPage;


	public ProblemUpdateController() {
		// Give calendars a starting date of now
		Calendar cal = Calendar.getInstance();
		//chosenProblemUpdateDate = cal.getTime();
	}
	
	// Services
	public ListsService getListsService() {
		return listsService;
	}

	public void setListsService(ListsService listsService) {
		this.listsService = listsService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	// Getters and Setters
	public String getChosenProblemItemId() {
		
		// Get the associated Problem Item id that's passed
		// as a parameter from the displayProblemsList page
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		chosenProblemItemId = requestParams.get("chosenProblemItemId");
		if (chosenProblemItemId == null) {
			// If there's no value, just give it one. It seems that
			// it will be overwritten anyway from the form. 10/19/2017
			chosenProblemItemId = "0";
		}

		// Save the Id of the chosen item so we can so it can be saved as
		// a foreign key in the ProblemUpdate entry.
		chosenProblemItemIdAsLong = Long.valueOf(chosenProblemItemId);

		return chosenProblemItemId;
	}

	public void setChosenProblemItemId(String chosenProblemItemId) {
		this.chosenProblemItemId = chosenProblemItemId;
		chosenProblemItemIdAsLong = Long.valueOf(chosenProblemItemId);
	}

	public String getCallingPage() {
		
		// Get the associated problem item id that's passed
		// as a parameter from the displayProblemReportList page
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		callingPage = requestParams.get("callingPage");
		if (callingPage == null) {
			// If there's no value, just give it one. It seems that
			// it will be overwritten anyway from the form. 10/19/2017
			callingPage = "displayProblemListActive.xhtml";
		}

		return callingPage;
	}

	public void setCallingPage(String callingPage) {
		this.callingPage = callingPage;
	}

	public ProblemUpdateDTO getCurrentProblemUpdateDTO() {
		return currentProblemUpdateDTO;
	}

	public void setCurrentProblemUpdateDTO(ProblemUpdateDTO currentProblemUpdateDTO) {
		this.currentProblemUpdateDTO = currentProblemUpdateDTO;
	}
	
	public Long getItemCreatedBy() {
		return itemCreatedBy;
	}

	public void setItemCreatedBy(Long itemCreatedBy) {
		this.itemCreatedBy = itemCreatedBy;
	}


	public Date getChosenProblemUpdateDate() {
		
		if (isCreateUpdate()) {
			Calendar startingCal = Calendar.getInstance();
			chosenProblemUpdateDate = startingCal.getTime();
		}

		return chosenProblemUpdateDate;
	}
	
	private boolean isCreateUpdate() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();

		// Set Item Name for subsequent pages to use
		String operation = requestParams
				.get("operation");
		if (operation == null || !operation.equalsIgnoreCase("CreateUpdate")) {
			// No operation or not CreateUpdate => just assume editProblemUpdate
			return false;
		} else {
			return true;
		}
	}

	
	public void setChosenProblemUpdateDate(Date chosenProblemUpdateDate) {
		this.chosenProblemUpdateDate = chosenProblemUpdateDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getPrintableProblemUpdatedate() {
		return printableProblemUpdateDate;
	}

	public void setPrintableProblemUpdateDate(Date printableUpdateDate) {
		this.printableProblemUpdateDate = printableProblemUpdateDate;
	}

	public Long getProblemUpdateid() {
		return problemUpdateid;
	}

	public void setProblemUpdateid(Long problemUpdateid) {
		this.problemUpdateid = problemUpdateid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getProblemUpdateDate() {
		return problemUpdateDate;
	}

	public void setProblemUpdateDate(Date problemUpdateDate) {
		this.problemUpdateDate = problemUpdateDate;
	}

	public Long getChosenItemCreatedBy() {
		return chosenItemCreatedBy;
	}

	public void setChosenItemCreatedBy(Long chosenItemCreatedBy) {
		this.chosenItemCreatedBy = chosenItemCreatedBy;
	}

	public String getProblemItemName() {
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();

		// Set Item Name for subsequent pages to use
		String chosenProblemItemNameTemp = requestParams
				.get("chosenProblemItemName");
		if (chosenProblemItemNameTemp == null) {
			// No parameter passed
			if (chosenProblemItemName == null) {
				// No previous value to use
				throw new RuntimeException(
						"chosenProblemItemName is null!!");
			} else {
				// just stick with the previous value
			}
		} else {
			// new parameter passed on the command line; use it!
			chosenProblemItemName = chosenProblemItemNameTemp;
		}
		return chosenProblemItemName;

	}

	public ProblemUpdateDTO getUpdateItem() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();

		// Get chosenProblemUpdateId from the parameter list.
		chosenProblemUpdateId = requestParams.get("chosenProblemUpdateId");
		if (chosenProblemUpdateId == null) {
			if (chosenProblemUpdateIdLast == null) {
				throw new RuntimeException(
						"chosenProblemUpdateIdLast is null!!");
			} else {
				chosenProblemUpdateId = chosenProblemUpdateIdLast;
			}
		}
		
		// Also get the user that created the entry, ItemCreatedBy
		// First time thru chosenItemCreatedBy will be null and thus
		// we have to get the value from the parameter passed in. 
		// But after that, subsequent calls needn't set it.
		String chosenItemCreatedByAsString = requestParams
				.get("chosenItemCreatedBy");
		if (chosenItemCreatedBy == null) {
			if (chosenItemCreatedByAsString == null) {
				throw new RuntimeException(
						"chosenItemCreatedByAsString is null!!");
			}
		}
		if (chosenItemCreatedByAsString != null) {
			chosenItemCreatedBy = Long.valueOf(chosenItemCreatedByAsString);
		}		
		
		chosenProblemUpdateIdLast = chosenProblemUpdateId;
		Long chosenProblemUpdateIdAsLong = Long.valueOf(chosenProblemUpdateId);
		
		// Get the current ProblemUpdate DTO entry so we can edit it
		currentProblemUpdateDTO = listsService
				.getProblemUpdate(chosenProblemUpdateIdAsLong);
		
		// Now set the user who created the update entry.
		currentProblemUpdateDTO.setItemCreatedBy(chosenItemCreatedBy);
		
		// Calculate the username for the DTO
		User theuser = userService.getUser(currentProblemUpdateDTO.getItemCreatedBy());
		currentProblemUpdateDTO.setUsername(theuser.getUsername());
				
		return currentProblemUpdateDTO;
	}
	
	
	public List<ProblemUpdateDTO> getProblemUpdateDTOList() {

		// Always start by trying to pick up the chosenMaintenanceItemId
		// so we know which Maintenance item this task is part of. But, JSF
		// repeatedly calls this for single operations and sometimes the
		// parameter comes back null. In that case, use the last value we
		// had which is squirrelled away.

		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		chosenProblemItemId = requestParams.get("chosenProblemItemId");

		// Save the Id of the chosen item so we can so it can be saved as
		// a foreign key in the Problem Update entry.
		if (chosenProblemItemId != null) {
			chosenProblemItemIdAsLong = Long
					.valueOf(chosenProblemItemId);
		} else {
			if (chosenProblemItemIdAsLong == 0) {
				throw new RuntimeException("chosenMaintenanceItemId is null");
			}
		}

		problemUpdateDTOList = listsService
				//.getMtasksForMaintenanceItem(chosenMaintenanceItemIdAsLong);
				.getProblemUpdatesForProblemItem(chosenProblemItemIdAsLong);

		return problemUpdateDTOList;
	}
	

	public String addProblemUpdate() throws Exception {
		
				
		// Create the DTO and perform the create by calling Lists Service
		// Since we are creating from scratch, use now as chosen time
		//Calendar startingCal = Calendar.getInstance();
		//chosenTaskStartDate = startingCal.getTime();
		ProblemUpdateDTO dto = createProblemUpdateDTO();
		try {
			listsService.createProblemUpdate(dto);
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		String returnValue = "addProblemUpdate";

		// clear out fields for return to the page in this session
		clearFormFields();
		return returnValue;
	}

	// method to create an ProblemUpdateDTO
	private ProblemUpdateDTO createProblemUpdateDTO() {
		ProblemUpdateDTO dto = new ProblemUpdateDTO();
		dto.setUpdateDate(chosenProblemUpdateDate);
		dto.setNotes(notes);
		//dto.setProblemupdateid(chosenProblemItemIdAsLong);
		dto.setProblemitemid(chosenProblemItemIdAsLong);
		
		// Find and set the user who added the entry
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(true);
		User dbUser = (User) session
				.getAttribute(AuthenticateController.SESSIONVAR_USER_NAME);
		dto.setItemCreatedBy(dbUser.getUserid());

		return dto;
	}

	
	public String editProblemUpdate() {

		// We already have the chosen DTO at this point. Just use it
		// to do a write to the DB.

		// If no entry chosen for date, use what we have
		// from the DTO as the chosen value (i.e. it didn't change).
		if (chosenProblemUpdateDate == null) {
			chosenProblemUpdateDate = currentProblemUpdateDTO.getUpdateDate();
		}
		
		// Update the date in the DTO
		currentProblemUpdateDTO.setUpdateDate(chosenProblemUpdateDate);
		
		currentProblemUpdateDTO.setItemCreatedBy(chosenItemCreatedBy);
		
		// Update ProblemUpdate entry in DB
		listsService.updateProblemUpdate(currentProblemUpdateDTO);

		// clear out fields for return to the page in this session
		clearFormFields();
		
		// Set Task start date to null so next time thru it will be either overwritten
		// by a user choosing a value or no value is given and then the 
		// value comes from the DTO which was determined by editMtask.xhtml.
		chosenProblemUpdateDate = null;

		return "editProblemUpdate";
	}

	

	public void deleteProblemUpdate() throws Exception {

		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		String chosenProblemUpdateId = requestParams.get("chosenProblemUpdateId");
		if (chosenProblemUpdateId == null || chosenProblemUpdateId.length() == 0) {
			throw new RuntimeException("chosenProblemUpdateId is null");
		}
		Long chosenProblemUpdateIdAsLong = Long.valueOf(chosenProblemUpdateId);
		
		// Now delete the ProblemUpdate
		listsService.deleteProblemUpdate(chosenProblemUpdateIdAsLong);
	}


	public void dateStartSelect(SelectEvent event) {
		setChosenProblemUpdateDate((Date)event.getObject());	
	}
	
	private void clearFormFields() {

		notes = "";
		//Calendar cal = Calendar.getInstance();
		//chosenTaskStartDate = cal.getTime();

	}

}
