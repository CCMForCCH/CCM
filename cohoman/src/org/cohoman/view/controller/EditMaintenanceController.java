package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.service.ListsService;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.SortEnums;

@ManagedBean
@SessionScoped
public class EditMaintenanceController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4678206276499587830L;

	private Long maintenanceitemid;
	private String itemname;
	private String itemdescription;
	private Date itemCreatedDate;
	private Long itemCreatedBy;
	private String priority;
	private String frequencyOfItem;
	private Date lastperformedDate;
	private String targetTimeOfyear;
	private String username;
	private String printableCreatedDate;
	private String printableLastperformedDate;

	private List<MaintenanceItemDTO> maintenanceItemDTOsList;
	private MaintenanceItemDTO chosenMaintenanceItemDTO;
	private String chosenMaintenanceItemString;
	//private String chosenUserString;
	private String maintenanceOperation = "changeMaintenanceItem";
	private MaintenanceItemDTO maintenanceItemDTO;
	private UserService userService = null;
	private ListsService listsService = null;

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

	public Long getMaintenanceitemid() {
		return maintenanceitemid;
	}

	public void setMaintenanceitemid(Long maintenanceitemid) {
		this.maintenanceitemid = maintenanceitemid;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPrintableCreatedDate() {
		return printableCreatedDate;
	}

	public void setPrintableCreatedDate(String printableCreatedDate) {
		this.printableCreatedDate = printableCreatedDate;
	}

	public String getPrintableLastperformedDate() {
		return printableLastperformedDate;
	}

	public void setPrintableLastperformedDate(String printableLastperformedDate) {
		this.printableLastperformedDate = printableLastperformedDate;
	}

	public List<MaintenanceItemDTO> getMaintenanceItemDTOsList() {
		maintenanceItemDTOsList = 
				listsService.getMaintenanceItems(SortEnums.ORDERBYNAME);
		return maintenanceItemDTOsList;
	}

	/*
	public void setMaintenanceItemDTOsList(
			List<MaintenanceItemDTO> maintenanceItemDTOsList) {
		this.maintenanceItemDTOsList = maintenanceItemDTOsList;
	}
*/

	public MaintenanceItemDTO getMaintenanceItemDTO() {
		return maintenanceItemDTO;
	}

	public void setMaintenanceItemDTO(MaintenanceItemDTO maintenanceItemDTO) {
		this.maintenanceItemDTO = maintenanceItemDTO;
	}

	public String getChosenMaintenanceItemString() {
		return chosenMaintenanceItemString;
	}

	public void setChosenMaintenanceItemString(String chosenMaintenanceItemString) {
		this.chosenMaintenanceItemString = chosenMaintenanceItemString;
	}

	public String getMaintenanceOperation() {
		return maintenanceOperation;
	}

	public void setMaintenanceOperation(String maintenanceOperation) {
		this.maintenanceOperation = maintenanceOperation;
	}


	public MaintenanceItemDTO getChosenMaintenanceItemDTO() {
		return chosenMaintenanceItemDTO;
	}

	public void setChosenMaintenanceItemDTO(MaintenanceItemDTO chosenMaintenanceItemDTO) {
		this.chosenMaintenanceItemDTO = chosenMaintenanceItemDTO;
	}


	public String editMaintenanceView() throws Exception {

		Long maintenanceItemId = Long.valueOf(chosenMaintenanceItemString);
		chosenMaintenanceItemDTO = listsService.getMaintenanceItem(maintenanceItemId);		

		if (maintenanceOperation.equals("deleteMaintenanceItem")) {
			listsService.deleteMaintenanceItem(chosenMaintenanceItemDTO);
		}
		return maintenanceOperation;
	}

	public String editMealItemsView() throws Exception {

		try {
			listsService.updateMaintenanceItem(chosenMaintenanceItemDTO);
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		return "listMaintenanceItems";
	}

}
