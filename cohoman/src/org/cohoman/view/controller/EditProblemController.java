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
import org.cohoman.view.controller.utils.ProblemStateEnums;
import org.cohoman.view.controller.utils.ProblemStatusEnums;
import org.cohoman.view.controller.utils.SortEnums;
import org.cohoman.view.controller.utils.VendorEnums;

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
						ProblemStateEnums.ALLPROBLEMS, SortEnums.ORDERBYNAME);
		return problemItemDTOsList;
	}

	public ProblemStatusEnums[] getItemproblemstatus() {
		return ProblemStatusEnums.values();
	}

	public VendorEnums[] getVendors() {
		return VendorEnums.values();
	}
	
	public List<User> getUserList() {
		userList = userService.getUsersHereNow();
		return userList;
	}


	public String editProblemView() throws Exception {

		// Get the chosen DTO
		Long problemItemId = Long.valueOf(chosenProblemItemString);
		chosenProblemItemDTO = listsService.getProblemItem(problemItemId);
		
		// Set the default "assigned to" user in the DTO, if there is one.
		// Note we are starting with a Long and converting that Long to
		// a string equivalent of that number. In the method below, 
		// that string will be converted back to a Long (after a 
		// submit by the user).
		Long assignedTo = chosenProblemItemDTO.getAssignedTo();
		if (assignedTo != null) {
			chosenProblemItemDTO.setAssignedToString(assignedTo.toString());
		}
		
		// Do the same thing as above for the createdBy user.
		chosenProblemItemDTO.setUsername(chosenProblemItemDTO.getItemCreatedBy().toString());
		
		String returnValue = problemOperation;
		if (problemOperation.equals("deleteProblemItem")) {
			listsService.deleteProblemItem(chosenProblemItemDTO);	
		}
		return returnValue;
	}

	public String editProblemItemsView() throws Exception {

		// Compute the assigned user from the DTO user string and use that
		// userid to write into the AssignedTo field of the DB.
		 Long userid = Long.valueOf(chosenProblemItemDTO.getAssignedToString());
		 chosenProblemItemDTO.setAssignedTo(userid);

		 chosenProblemItemDTO.setItemCreatedBy(Long.valueOf(chosenProblemItemDTO.getUsername()));
		 
		// If changing status to COMPLETED or CLOSED, set to Completed On
		// date/time.
		// If status is anything but COMPLETED or CLOSED, reset the completion
		// date to null
		if (chosenProblemItemDTO.getProblemStatus().equals(
				ProblemStatusEnums.COMPLETED.name())
				|| chosenProblemItemDTO.getProblemStatus().equals(
						ProblemStatusEnums.CLOSED.name())) {
			GregorianCalendar now = new GregorianCalendar();
			chosenProblemItemDTO.setItemCompletedDate(now.getTime());
		} else {
			chosenProblemItemDTO.setItemCompletedDate(null);
		}

		try {
			listsService.updateProblemItem(chosenProblemItemDTO);
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		String returnValue = "listProblemItems";

		return returnValue;

	}

}
