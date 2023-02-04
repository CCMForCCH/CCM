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
import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.service.ListsService;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.MaintenanceTypeEnums;
import org.cohoman.view.controller.utils.SortEnums;
import org.cohoman.view.controller.utils.TaskPriorityEnums;
import org.cohoman.view.controller.utils.TaskStatusEnums;

@ManagedBean
@SessionScoped
public class CreateMaintenanceItemController implements Serializable {

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
	private String frequencyOfItem;
	private Date lastperformedDate;
	private String targetTimeOfyear;
	private String assignedToString;
	private UserService userService = null;
	private ListsService listsService = null;
	private TaskPriorityEnums chosenpriority;
	private MaintenanceTypeEnums chosentype;
	private Long assignedTo; 
	private List<User> userList;


	public CreateMaintenanceItemController() {
	}

	private void clearFormFields() {
		
		itemname = "";
		itemdescription = "";
		priority = "";
		frequencyOfItem = "";
		lastperformedDate = null;;
		targetTimeOfyear = "";

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

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public TaskPriorityEnums getChosenpriority() {
		return chosenpriority;
	}

	public void setChosenpriority(TaskPriorityEnums chosenpriority) {
		this.chosenpriority = chosenpriority;
	}

	public String getFrequencyOfItem() {
		return frequencyOfItem;
	}

	public void setFrequencyOfItem(String frequencyOfItem) {
		this.frequencyOfItem = frequencyOfItem;
	}

	public Date getLastperformedDate() {
		return lastperformedDate;
	}

	public void setLastperformedDate(Date lastperformedDate) {
		this.lastperformedDate = lastperformedDate;
	}

	public String getTargetTimeOfyear() {
		return targetTimeOfyear;
	}

	public void setTargetTimeOfyear(String targetTimeOfyear) {
		this.targetTimeOfyear = targetTimeOfyear;
	}

	public TaskPriorityEnums[] getItempriorities() {
		return TaskPriorityEnums.values();
	}
	
	public MaintenanceTypeEnums getChosentype() {
		return chosentype;
	}

	public void setChosentype(MaintenanceTypeEnums chosentype) {
		this.chosentype = chosentype;
	}

	public MaintenanceTypeEnums[] getItemtypes() {
		MaintenanceTypeEnums[] types = { MaintenanceTypeEnums.HOFELLER,
				MaintenanceTypeEnums.OWNER };
		return types;
	}


	public Long getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(Long assignedTo) {
		this.assignedTo = assignedTo;
	}

	public List<User> getUserList() {
		userList = userService.getUsersHereNow();
		return userList;
	}

	public String getAssignedToString() {
		return assignedToString;
	}

	public void setAssignedToString(String assignedToString) {
		this.assignedToString = assignedToString;
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

	public String addMaintenanceItem() throws Exception {
/*
		Date eventDate = calculateTheDate();
		//TODO: calculate ending time from new duration field
		Calendar endDateCal = Calendar.getInstance();
		endDateCal.setTime(eventDate);
		endDateCal.add(Calendar.HOUR_OF_DAY, 2); // assume meals are 2 hrs. for now
		Date eventdateend = endDateCal.getTime();
		MealEventDTO dto = createMealEventDTO(eventDate);
		dto.setEventdateend(eventdateend);
		try {
			eventService.createMealEvent(dto);
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		// clear out fields for return to the page in this session
		clearFormFields();
*/
		MaintenanceItemDTO dto = createMaintenanceItemDTO();
		
		// Make sure that this is a unique maintenance item name
		List<MaintenanceItemDTO> allMaintenanceItems = 
				listsService.getMaintenanceItems(SortEnums.ORDERBYNAME, MaintenanceTypeEnums.ALL);
		for (MaintenanceItemDTO oneItem : allMaintenanceItems) {
			if (oneItem.getItemname().equalsIgnoreCase(dto.getItemname())) {
				FacesMessage message = new FacesMessage(
						"Already have an item name of " + dto.getItemname());
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, message);
				return null;
			}
		}

		//try {
			listsService.createMaintenanceItem(dto);
		//} catch (CohomanException ex) {
			//FacesMessage message = new FacesMessage(ex.getErrorText());
			//message.setSeverity(FacesMessage.SEVERITY_ERROR);
			//FacesContext.getCurrentInstance().addMessage(null, message);
			//return null;
		//}

		// Kinda hack the returned operation so we can tell which list to
		// to display, Hofeller or Owner. (08/03/2020)
		String returnValue = "addMaintenanceItem";
		if (dto.getMaintenanceType().equals(MaintenanceTypeEnums.OWNER.name())) {
			returnValue += dto.getMaintenanceType();	
		}

		// clear out fields for return to the page in this session
		clearFormFields();
		return returnValue;
	}
	
	// method to create a Maintenance Item DTO
	private MaintenanceItemDTO createMaintenanceItemDTO() {
		MaintenanceItemDTO dto = new MaintenanceItemDTO();
		dto.setItemname(itemname);
		dto.setItemdescription(itemdescription);
		dto.setFrequencyOfItem(frequencyOfItem);
		dto.setTargetTimeOfyear(targetTimeOfyear);
		//dto.setPriority(chosenpriority.name());
		dto.setMaintenanceType(chosentype.name());
		GregorianCalendar now = new GregorianCalendar();
		dto.setItemCreatedDate(now.getTime());
		dto.setNextServiceDate(now.getTime());   // keep this code or fix?????
		dto.setTaskStatus(TaskStatusEnums.UPTODATE.name());   // keep this code or fix?????
		dto.setAssignedToString(assignedToString);
		
		// get the userid of the current user to set the requester.
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession)ctx.getExternalContext().getSession(true); 
		User dbUser = (User) session.getAttribute(AuthenticateController.SESSIONVAR_USER_NAME);
		dto.setItemCreatedBy(dbUser.getUserid());
		
		return dto;
	}
		
}
