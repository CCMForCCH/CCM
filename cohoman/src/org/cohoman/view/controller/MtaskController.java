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
import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.dto.MtaskDTO;
import org.cohoman.model.service.ListsService;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.TaskStatusEnums;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class MtaskController implements Serializable {

	private static final long serialVersionUID = 4678206276499587830L;

	private List<MtaskDTO> mtaskDTOList;
	private ListsService listsService = null;
	private UserService userService = null;

	private Long chosenMaintenanceItemIdAsLong;
	private String chosenMaintenanceItemId;
	private String chosenMaintenanceItemName;
	private String chosenMtaskItemId;
	private String chosenMtaskItemIdAsLong;
	private String chosenMtaskItemIdLast;
	private MtaskDTO currentMtaskDTO;

	private Long mtaskitemid;
	private String vendorname;
	private Date taskstartDate;
	private Date taskendDate;
	private Long itemCreatedBy;
	private String notes;

	private Date chosenTaskStartDate;
	private Date chosenTaskEndDate;
	
	private String username;
	private Date printableStartdate;
	private Date printableEnddate;

	public MtaskController() {
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
	public String getChosenMtaskItemId() {
		return chosenMtaskItemId;
	}

	public void setChosenMtaskItemId(String chosenMtaskItemId) {
		this.chosenMtaskItemId = chosenMtaskItemId;
	}

	public MtaskDTO getCurrentMtaskDTO() {
		return currentMtaskDTO;
	}

	public void setCurrentMtaskDTO(MtaskDTO currentMtaskDTO) {
		this.currentMtaskDTO = currentMtaskDTO;
	}

	public boolean isTaskcomplete(MtaskDTO currentMtaskDTO) {
		
		// Don't persist value, just base it on
		// whether the task end date is filled in.
		if (currentMtaskDTO.getTaskendDate() == null) {
			return false;
		} else {
			return true;
		}
	}

	public String getVendorname() {
		return vendorname;
	}

	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}

	public Long getItemCreatedBy() {
		return itemCreatedBy;
	}

	public void setItemCreatedBy(Long itemCreatedBy) {
		this.itemCreatedBy = itemCreatedBy;
	}

	public Date getChosenTaskStartDate() {
		
		if (isCreateTask()) {
			Calendar startingCal = Calendar.getInstance();
			chosenTaskStartDate = startingCal.getTime();
		}

		return chosenTaskStartDate;
	}
	
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

	public void setChosenTaskStartDate(Date chosenTaskStartDate) {
		this.chosenTaskStartDate = chosenTaskStartDate;
	}

	public Date getChosenTaskEndDate() {
		
		if (isCreateTask()) {
			chosenTaskEndDate = null;
		}
		
		return chosenTaskEndDate;
	}

	public void setChosenTaskEndDate(Date chosenTaskEndDate) {
		this.chosenTaskEndDate = chosenTaskEndDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getPrintableStartdate() {
		return printableStartdate;
	}

	public void setPrintableStartdate(Date printableStartdate) {
		this.printableStartdate = printableStartdate;
	}

	public Date getPrintableEnddate() {
		return printableEnddate;
	}

	public void setPrintableEnddate(Date printableEnddate) {
		this.printableEnddate = printableEnddate;
	}

	public Long getMtaskitemid() {
		return mtaskitemid;
	}

	public void setMtaskitemid(Long mtaskitemid) {
		this.mtaskitemid = mtaskitemid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getTaskstartDate() {
		return taskstartDate;
	}

	public void setTaskstartDate(Date taskstartDate) {
		this.taskstartDate = taskstartDate;
	}

	public Date getTaskendDate() {
		return taskendDate;
	}

	public void setTaskendDate(Date taskendDate) {
		this.taskendDate = taskendDate;
	}

	public String getChosenMtaskItemIdAsLong() {
		return chosenMtaskItemIdAsLong;
	}

	public void setChosenMtaskItemIdAsLong(String chosenMtaskItemIdAsLong) {
		this.chosenMtaskItemIdAsLong = chosenMtaskItemIdAsLong;
	}

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

	public void setChosenMaintenanceItemId(String chosenMaintenanceItemId) {
		this.chosenMaintenanceItemId = chosenMaintenanceItemId;
		chosenMaintenanceItemIdAsLong = Long.valueOf(chosenMaintenanceItemId);
	}

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

	public void setMtaskDTOList(List<MtaskDTO> mtaskDTOList) {
		this.mtaskDTOList = mtaskDTOList;
	}

	public String addMaintenanceTask() throws Exception {
		
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
		if (!isTaskcomplete(dto)) {

			// Just started the task.
			MaintenanceItemDTO maintenanceItemDTO = listsService
					.getMaintenanceItem(chosenMaintenanceItemIdAsLong);
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
			MaintenanceItemDTO maintenanceItemDTO = listsService
					.getMaintenanceItem(chosenMaintenanceItemIdAsLong);
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
		
		// clear out fields for return to the page in this session
		clearFormFields();

		return "addMaintenanceTask";
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

		if (chosenTaskEndDate != null) {
			// Set completed date to the one chosen
			currentMtaskDTO.setTaskendDate(chosenTaskEndDate);

			// If we've just completed a task, also update the
			// last performed date in the maintenance item table
			MaintenanceItemDTO maintenanceItemDTO = listsService
					.getMaintenanceItem(chosenMaintenanceItemIdAsLong);
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

		// clear out fields for return to the page in this session
		clearFormFields();
		
		// Set Task start date to null so next time thru it will be either overwritten
		// by a user choosing a value or no value is given and then the 
		// value comes from the DTO which was determined by editMtask.xhtml.
		chosenTaskStartDate = null;

		return "editMtask";
	}

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


	public void dateStartSelect(SelectEvent event) {
		setChosenTaskStartDate((Date)event.getObject());	
	}

	public void dateEndSelect(SelectEvent event) {
		setChosenTaskEndDate((Date)event.getObject());	
	}
	
	private void clearFormFields() {

		vendorname = "";
		notes = "";
		chosenTaskEndDate = null;
		Calendar cal = Calendar.getInstance();
		//chosenTaskStartDate = cal.getTime();

	}

}
