package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.business.User;
import org.cohoman.model.dto.ProblemItemDTO;
import org.cohoman.model.service.ListsService;
import org.cohoman.model.service.UserService;
import org.cohoman.view.controller.utils.VendorEnums;
import org.cohoman.view.controller.utils.ProblemStatusEnums;
import org.cohoman.view.controller.utils.ProblemTypeEnums;
import org.cohoman.view.controller.utils.SortEnums;

@ManagedBean
@SessionScoped
public class EditProblemController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4678206276499587830L;

	private Long problemitemid;
	private String itemname;
	private String itemdescription;
	private Date itemCreatedDate;
	private Long itemCreatedBy;
	private String priority;
	private String printableCreatedDate;
	private String printableCompletedDate;
	private String problemStatus;
	private String problemType;
	private String username;
	private String vendor;
	private String duplicateproblemitemname;
	private String location;
	private String assignedTo;
	private String cost;
	private String invoiceLink;
	private String invoiceNumber;
	private Date itemCompletedDate;

	private List<ProblemItemDTO> problemItemDTOsList;
	private ProblemItemDTO chosenProblemItemDTO;
	private List<User> userList;
	private String chosenProblemItemString;
	private String problemOperation = "changeProblemItem";
	private ProblemItemDTO problemItemDTO;
	private UserService userService = null;
	private ListsService listsService = null;

	/*
	 * External service references
	 */
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

	/*
	 * Getters and Setters
	 */
	public Long getProblemitemid() {
		return problemitemid;
	}

	public void setProblemitemid(Long problemitemid) {
		this.problemitemid = problemitemid;
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

	public String getProblemStatus() {
		return problemStatus;
	}


	public String getProblemType() {
		return problemType;
	}

	public void setProblemType(String problemType) {
		this.problemType = problemType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getDuplicateproblemitemname() {
		return duplicateproblemitemname;
	}

	public void setDuplicateproblemitemname(String duplicateproblemitemname) {
		this.duplicateproblemitemname = duplicateproblemitemname;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getInvoiceLink() {
		return invoiceLink;
	}

	public void setInvoiceLink(String invoiceLink) {
		this.invoiceLink = invoiceLink;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public ProblemItemDTO getChosenProblemItemDTO() {
		return chosenProblemItemDTO;
	}

	public void setChosenProblemItemDTO(ProblemItemDTO chosenProblemItemDTO) {
		this.chosenProblemItemDTO = chosenProblemItemDTO;
	}

	public String getChosenProblemItemString() {
		return chosenProblemItemString;
	}

	public void setChosenProblemItemString(String chosenProblemItemString) {
		this.chosenProblemItemString = chosenProblemItemString;
	}

	public String getProblemOperation() {
		return problemOperation;
	}

	public void setProblemOperation(String problemOperation) {
		this.problemOperation = problemOperation;
	}

	public ProblemItemDTO getProblemItemDTO() {
		return problemItemDTO;
	}

	public void setProblemItemDTO(ProblemItemDTO problemItemDTO) {
		this.problemItemDTO = problemItemDTO;
	}

	public Date getItemCompletedDate() {
		return itemCompletedDate;
	}

	public void setItemCompletedDate(Date itemCompletedDate) {
		this.itemCompletedDate = itemCompletedDate;
	}
	
	
	/*
	 * Special Getters and Setters
	 */
	public void setProblemStatus(String problemStatus) {	
		this.problemStatus = problemStatus;
	}

	public String getPrintableCreatedDate() {
		return printableCreatedDate;
	}

	public void setPrintableCreatedDate(String printableCreatedDate) {
		this.printableCreatedDate = printableCreatedDate;
	}

	public String getPrintableCompletedDate() {
		return printableCompletedDate;
	}

	public void setPrintableCompletedDate(String printableCompletedDate) {
		this.printableCompletedDate = printableCompletedDate;
	}

	public List<ProblemItemDTO> getProblemItemDTOsList() {
		problemItemDTOsList = 
				listsService.getProblemItems(
						SortEnums.ORDERBYNAME);
		return problemItemDTOsList;
	}

	public ProblemStatusEnums[] getItemproblemstatus() {
		return ProblemStatusEnums.values();
	}

	public VendorEnums[] getVendors() {
		return VendorEnums.values();
	}
	
	public List<User> getUserList() {
		userList = userService.getAllUsers();
		return userList;
	}


	public String editProblemView() throws Exception {

		// Get the chosen DTO
		Long problemItemId = Long.valueOf(chosenProblemItemString);
		chosenProblemItemDTO = listsService.getProblemItem(problemItemId);
		
		// Set the default "assigned to" user in the DTO, if there is one.
		Long assignedTo = chosenProblemItemDTO.getAssignedTo();
		if (assignedTo != null) {
			chosenProblemItemDTO.setAssignedToString(assignedTo.toString());
		}

		String returnValue = problemOperation;
		if (problemOperation.equals("deleteProblemItem")) {
			listsService.deleteProblemItem(chosenProblemItemDTO);
			
			// Kinda hacky way to return different operation strings so page
			// displayed is either for Hofeller or Owner items (08/05/2020)
			//if (chosenMaintenanceItemDTO.getMaintenanceType().equals(MaintenanceTypeEnums.OWNER.name())) {
				//returnValue += chosenMaintenanceItemDTO.getMaintenanceType();	
			//}
		}
		return returnValue;
	}

	public String editProblemItemsView() throws Exception {

		// Compute the assigned user from the DTO user string and use that
		// userid to write into the AssignedTo field of the DB.
		 Long userid = Long.valueOf(chosenProblemItemDTO.getAssignedToString());
		 chosenProblemItemDTO.setAssignedTo(userid);

		// If changing status to COMPLETED, set to Completed On date/time.
		if (chosenProblemItemDTO.getItemCompletedDate() == null
				&& chosenProblemItemDTO.getProblemStatus().equals(ProblemStatusEnums.COMPLETED.name())) {
			GregorianCalendar now = new GregorianCalendar();
			chosenProblemItemDTO.setItemCompletedDate(now.getTime());
		}

		try {
			listsService.updateProblemItem(chosenProblemItemDTO);
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		// Kinda hack the returned operation so we can tell which list to
		// to display, Hofeller or Owner. (08/29/2020)
		String returnValue = "listProblemItems";
		//if (chosenMaintenanceItemDTO.getMaintenanceType().equals(MaintenanceTypeEnums.OWNER.name())) {
			//returnValue += chosenMaintenanceItemDTO.getMaintenanceType();	
		//}

		return returnValue;

	}

}
