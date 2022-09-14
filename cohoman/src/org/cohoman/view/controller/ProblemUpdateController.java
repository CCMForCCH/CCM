package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.dto.MtaskDTO;
import org.cohoman.model.dto.ProblemUpdateDTO;
import org.cohoman.model.service.ListsService;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.cohoman.view.controller.utils.MaintenanceTypeEnums;
import org.cohoman.view.controller.utils.TaskStatusEnums;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class ProblemUpdateController implements Serializable {

	private static final long serialVersionUID = 4678206276499587830L;

	private List<ProblemUpdateDTO> problemUpdateDTOList;
	private ListsService listsService = null;
	private UserService userService = null;

	private String chosenProblemUpdateIdAsLong;
	private String chosenProblemUpdateId;
	private String chosenProblemUpdateIdLast;
	private ProblemUpdateDTO currentProblemUpdateDTO;

	private Long problemUpdateid;
	private Date problemUpdateDate;
	private Long itemCreatedBy;
	private String notes;

	private Date chosenProblemUpdateDate;
	
	private String username;
	private Date printableProblemUpdateDate;

	public ProblemUpdateController() {
		// Give calendars a starting date of now
		//Calendar cal = Calendar.getInstance();
		//chosenTaskStartDate = cal.getTime();
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
	public String getChosenProblemUpdateId() {
		return chosenProblemUpdateId;
	}

	public void setChosenProblemUpdateId(String chosenProblemUpdateId) {
		this.chosenProblemUpdateId = chosenProblemUpdateId;
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

	/*
	public Date getChosenProblemUpdateDate() {
		
		if (isCreateTask()) {
			Calendar startingCal = Calendar.getInstance();
			chosenProblemUpdateDate = startingCal.getTime();
		}

		return chosenProblemUpdateDate;
	}
*/
	
	/*
	private boolean isCreateTask() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();

		// Set Item Name for subsequent pages to use
		String operation = requestParams
				.get("operation");
		if (operation == null || !operation.equalsIgnoreCase("CreateTask")) {
			// No operation or not CreateTask => just assume editMtask
			return false;
		} else {
			return true;
		}
	}
*/
	
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

	public String getChosenProblemUpdateIdAsLong() {
		return chosenProblemUpdateIdAsLong;
	}

	public void setChosenProblemUpdateIdAsLong(String chosenProblemUpdateIdAsLong) {
		this.chosenProblemUpdateIdAsLong = chosenProblemUpdateIdAsLong;
	}

	/*
	// More complicated getters and setters
	public String getMaintenanceItemName() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();

		// Set Item Name for subsequent pages to use
		String chosenMaintenanceItemNameTemp = requestParams
				.get("chosenMaintenanceItemName");
		if (chosenMaintenanceItemNameTemp == null) {
			// No parameter passed
			if (chosenMaintenanceItemName == null) {
				// No previous value to use
				throw new RuntimeException(
						"chosenMaintenanceItemName is null!!");
			} else {
				// just stick with the previous value
			}
		} else {
			// new parameter passed on the command line; use it!
			chosenMaintenanceItemName = chosenMaintenanceItemNameTemp;
		}
		return chosenMaintenanceItemName;
	}
*/
	public String getProblemUpdateName() {
		
		/*
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();

		// Set Item Name for subsequent pages to use
		String chosenMaintenanceItemNameTemp = requestParams
				.get("chosenMaintenanceItemName");
		if (chosenMaintenanceItemNameTemp == null) {
			// No parameter passed
			if (chosenMaintenanceItemName == null) {
				// No previous value to use
				throw new RuntimeException(
						"chosenMaintenanceItemName is null!!");
			} else {
				// just stick with the previous value
			}
		} else {
			// new parameter passed on the command line; use it!
			chosenMaintenanceItemName = chosenMaintenanceItemNameTemp;
		}
		return chosenMaintenanceItemName;
*/
		return "Broken lock on front door. <Just a sample>";
	}

/*
	public MtaskDTO getMtaskItem() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		chosenMtaskItemId = requestParams.get("chosenMtaskItemId");
		if (chosenMtaskItemId == null) {
			if (chosenMtaskItemIdLast == null) {
				throw new RuntimeException("chosenMtaskItemId is null!!");
			} else {
				chosenMtaskItemId = chosenMtaskItemIdLast;
			}
		}
		chosenMtaskItemIdLast = chosenMtaskItemId;
		Long chosenMtaskItemIdAsLong = Long.valueOf(chosenMtaskItemId);
		// save away the current Mtask entry so we can later edit it
		currentMtaskDTO = listsService.getMtask(chosenMtaskItemIdAsLong);
		return currentMtaskDTO;
	}
*/
	
/*
	public String getChosenMaintenanceItemId() {

		// Get the associated maintenance item id that's passe
		// as a parameter from the displayMaintenanceList page
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		chosenMaintenanceItemId = requestParams.get("chosenMaintenanceItemId");
		if (chosenMaintenanceItemId == null) {
			// If there's no value, just give it one. It seems that
			// it will be overwritten anyway from the form. 10/19/2017
			chosenMaintenanceItemId = "0";
		}

		// Save the Id of the chosen item so we can so it can be saved as
		// a foreign key in the Mtask entry.
		chosenMaintenanceItemIdAsLong = Long.valueOf(chosenMaintenanceItemId);

		return chosenMaintenanceItemId;
	}
*/
	
/*
	public void setChosenMaintenanceItemId(String chosenMaintenanceItemId) {
		this.chosenMaintenanceItemId = chosenMaintenanceItemId;
		chosenMaintenanceItemIdAsLong = Long.valueOf(chosenMaintenanceItemId);
	}
*/
	
	public List<ProblemUpdateDTO> getProblemUpdateDTOList() {

		List<ProblemUpdateDTO> problemUpdateDTOList = new ArrayList <ProblemUpdateDTO>();
		ProblemUpdateDTO problemUpdateDTO1 = new ProblemUpdateDTO();
		problemUpdateDTO1.setUsername("Bill");
		problemUpdateDTO1.setNotes("Called the vendor, but there was no answer. Left a message.");
		problemUpdateDTO1.setPrintableUpdateDate("Sept 5, 2022 12:30PM");
		problemUpdateDTOList.add(problemUpdateDTO1);
		
		ProblemUpdateDTO problemUpdateDTO2 = new ProblemUpdateDTO();
		problemUpdateDTO2.setUsername("Walter");
		problemUpdateDTO2.setNotes("This time got the vendor and he says he'll be over on Tuesday if something more important doesn't come up.");
		problemUpdateDTO2.setPrintableUpdateDate("Oct. 5, 2022 10:30PM");
		problemUpdateDTOList.add(problemUpdateDTO2);

		ProblemUpdateDTO problemUpdateDTO3 = new ProblemUpdateDTO();
		problemUpdateDTO3.setUsername("Allison");
		problemUpdateDTO3.setNotes("Vendor came but couldn't fix the problem. He didn't have the right parts. He's now order new parts.");
		problemUpdateDTO3.setPrintableUpdateDate("Nov 30, 2022 5:30PM");
		problemUpdateDTOList.add(problemUpdateDTO3);

		// Always start by trying to pick up the chosenMaintenanceItemId
		// so we know which Maintenance item this task is part of. But, JSF
		// repeatedly calls this for single operations and sometimes the
		// parameter comes back null. In that case, use the last value we
		// had which is squirrelled away.
/*
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		chosenMaintenanceItemId = requestParams.get("chosenMaintenanceItemId");

		// Save the Id of the chosen item so we can so it can be saved as
		// a foreign key in the Mtask entry.
		if (chosenMaintenanceItemId != null) {
			chosenMaintenanceItemIdAsLong = Long
					.valueOf(chosenMaintenanceItemId);
		} else {
			if (chosenMaintenanceItemIdAsLong == 0) {
				throw new RuntimeException("chosenMaintenanceItemId is null");
			}
		}

		mtaskDTOList = listsService
				.getMtasksForMaintenanceItem(chosenMaintenanceItemIdAsLong);
*/
		return problemUpdateDTOList;
	}

/*
	public List<MtaskDTO> getMtaskDTOList() {

		// Always start by trying to pick up the chosenMaintenanceItemId
		// so we know which Maintenance item this task is part of. But, JSF
		// repeatedly calls this for single operations and sometimes the
		// parameter comes back null. In that case, use the last value we
		// had which is squirrelled away.
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		chosenMaintenanceItemId = requestParams.get("chosenMaintenanceItemId");

		// Save the Id of the chosen item so we can so it can be saved as
		// a foreign key in the Mtask entry.
		if (chosenMaintenanceItemId != null) {
			chosenMaintenanceItemIdAsLong = Long
					.valueOf(chosenMaintenanceItemId);
		} else {
			if (chosenMaintenanceItemIdAsLong == 0) {
				throw new RuntimeException("chosenMaintenanceItemId is null");
			}
		}

		mtaskDTOList = listsService
				.getMtasksForMaintenanceItem(chosenMaintenanceItemIdAsLong);
		return mtaskDTOList;
	}
*/
	
/*
	public void setMtaskDTOList(List<MtaskDTO> mtaskDTOList) {
		this.mtaskDTOList = mtaskDTOList;
	}
*/
	
/*
	public String addMaintenanceTask() throws Exception {
		
		
		// Make sure start date is <= end date iff there is an end date
		if (chosenTaskEndDate != null) {
			if (!CalendarUtils.startDateBeforeOrEqualEndDate(
					chosenTaskStartDate, chosenTaskEndDate)) {
				FacesMessage message = new FacesMessage(
						"Invalid dates: Starting day must be equal or before completion day.");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, message);
				return null;
			}
		}
		
		// Create the DTO and perform the create by calling Lists Service
		// Since we are creting from scratch, use now as chosen time
		//Calendar startingCal = Calendar.getInstance();
		//chosenTaskStartDate = startingCal.getTime();
		MtaskDTO dto = createMtaskDTO();
		try {
			listsService.createMtask(dto);
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		
		// We just created a new Mtask. Mark the Task Status in the parent
		// item as INPROGRESS, assuming that it is not marked complete.
		MaintenanceItemDTO maintenanceItemDTO = listsService
				.getMaintenanceItem(chosenMaintenanceItemIdAsLong);
		if (!isTaskcomplete(dto)) {

			// Just started the task.
			maintenanceItemDTO.setTaskStatus(TaskStatusEnums.INPROGRESS.name());;
			
			try {
				listsService.updateMaintenanceItem(maintenanceItemDTO);
			} catch (CohomanException ex) {
				FacesMessage message = new FacesMessage(ex.getErrorText());
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, message);
				return null;
			}
		} else {
			// Did both: just started and finished the task.
			// Update the last performed date in the maintenance
			// item table.
			maintenanceItemDTO.setLastperformedDate(chosenTaskEndDate);
			
			// And also recompute the NextServiceDate
			Calendar nextServiceDateCal = Calendar.getInstance();
			nextServiceDateCal.setTime(chosenTaskEndDate);
			int freqInMonths = 
					Integer.valueOf(maintenanceItemDTO.getFrequencyOfItem());
			nextServiceDateCal.add(Calendar.MONTH, 
					freqInMonths);
			maintenanceItemDTO.setNextServiceDate(nextServiceDateCal.getTime());

			// Lastly, mark the status in the parent item as UPTODATE
			maintenanceItemDTO.setTaskStatus(TaskStatusEnums.UPTODATE.name());
			
			// Now write the maintenance item out to the DB
			try {
				listsService.updateMaintenanceItem(maintenanceItemDTO);
			} catch (CohomanException ex) {
				FacesMessage message = new FacesMessage(ex.getErrorText());
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, message);
				return null;
			}

		}
		
		// Kinda hack the returned operation so we can tell which list to
		// to display, Hofeller or Owner. (08/29/2020)
		String returnValue = "addMaintenanceTask";
		if (maintenanceItemDTO.getMaintenanceType().equals(MaintenanceTypeEnums.OWNER.name())) {
			returnValue += maintenanceItemDTO.getMaintenanceType();	
		}

		// clear out fields for return to the page in this session
		clearFormFields();
		return returnValue;
	}

	// method to create an MtaskDTO
	private MtaskDTO createMtaskDTO() {
		MtaskDTO dto = new MtaskDTO();
		dto.setVendorname(vendorname);
		dto.setTaskstartDate(chosenTaskStartDate);
		dto.setTaskendDate(chosenTaskEndDate);
		dto.setNotes(notes);
		dto.setMaintenanceitemid(chosenMaintenanceItemIdAsLong);

		// Find and set the user who added the entry
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(true);
		User dbUser = (User) session
				.getAttribute(AuthenticateController.SESSIONVAR_USER_NAME);
		dto.setItemCreatedBy(dbUser.getUserid());

		return dto;
	}
*/
	
/*
	public String editMtask() {

		// We already have the chosen DTO at this point. Just use it
		// to do a write to the DB.

		// If no entry chosen for task end date, use what we have
		// from the DTO as the chosen value (i.e. it didn't change).
		if (chosenTaskEndDate == null) {
			chosenTaskEndDate = currentMtaskDTO.getTaskendDate();
		}

		// Same point for task start date. If it's null, assume that
		// it didn't change so just use the old value from the DTO.
		if (chosenTaskStartDate == null) {
			chosenTaskStartDate = currentMtaskDTO.getTaskstartDate();
		}
		
		// Update the start date in the DTO
		currentMtaskDTO.setTaskstartDate(chosenTaskStartDate);

		MaintenanceItemDTO maintenanceItemDTO = listsService
				.getMaintenanceItem(chosenMaintenanceItemIdAsLong);
		if (chosenTaskEndDate != null) {
			
			// Set completed date to the one chosen
			currentMtaskDTO.setTaskendDate(chosenTaskEndDate);

			// If we've just completed a task, also update the
			// last performed date in the maintenance item table
			maintenanceItemDTO.setLastperformedDate(chosenTaskEndDate);

			// And also recompute the NextServiceDate
			Calendar nextServiceDateCal = Calendar.getInstance();
			nextServiceDateCal.setTime(chosenTaskEndDate);
			int freqInMonths = Integer.valueOf(maintenanceItemDTO
					.getFrequencyOfItem());
			nextServiceDateCal.add(Calendar.MONTH, freqInMonths);
			maintenanceItemDTO.setNextServiceDate(nextServiceDateCal.getTime());

			// Lastly, mark the status in the parent item as UPTODATE
			maintenanceItemDTO.setTaskStatus(TaskStatusEnums.UPTODATE.name());

			// Now write the maintenance item out to the DB
			try {
				listsService.updateMaintenanceItem(maintenanceItemDTO);
			} catch (CohomanException ex) {
				FacesMessage message = new FacesMessage(ex.getErrorText());
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, message);
				return null;
			}
		}
		
		// Update Mtask entry in DB
		listsService.updateMtask(currentMtaskDTO);

		// Kinda hack the returned operation so we can tell which list to
		// to display, Hofeller or Owner. (08/29/2020)
		String returnValue = "editMtask";
		if (maintenanceItemDTO.getMaintenanceType().equals(MaintenanceTypeEnums.OWNER.name())) {
			returnValue += maintenanceItemDTO.getMaintenanceType();	
		}
		
		// clear out fields for return to the page in this session
		clearFormFields();
		
		// Set Task start date to null so next time thru it will be either overwritten
		// by a user choosing a value or no value is given and then the 
		// value comes from the DTO which was determined by editMtask.xhtml.
		chosenTaskStartDate = null;

		return returnValue;
	}
*/
	
/*
	public void deleteMtask() throws Exception {

		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		String chosenMtaskItemId = requestParams.get("chosenMtaskItemId");
		if (chosenMtaskItemId == null || chosenMtaskItemId.length() == 0) {
			throw new RuntimeException("chosenMtaskItemId is null");
		}
		Long chosenMtaskItemIdAsLong = Long.valueOf(chosenMtaskItemId);
		
		// TODO arbitarily set maintenance item task status to UPTODATE
		MaintenanceItemDTO maintenanceItemDTO = listsService
				.getMaintenanceItem(chosenMaintenanceItemIdAsLong);
		maintenanceItemDTO.setTaskStatus(TaskStatusEnums.UPTODATE.name());;
		listsService.updateMaintenanceItem(maintenanceItemDTO);

		// Now delete the maintenance task
		listsService.deleteMtask(chosenMtaskItemIdAsLong);
	}
*/

	public void dateStartSelect(SelectEvent event) {
		setChosenProblemUpdateDate((Date)event.getObject());	
	}
	
	private void clearFormFields() {

		notes = "";
		Calendar cal = Calendar.getInstance();
		//chosenTaskStartDate = cal.getTime();

	}

}
