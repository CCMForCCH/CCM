package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.dto.ProblemItemDTO;
import org.cohoman.model.service.ListsService;
import org.cohoman.view.controller.utils.ProblemStateEnums;
import org.cohoman.view.controller.utils.SortEnums;

@ManagedBean
@SessionScoped
public class RetrieveProblemItemsListController implements Serializable {

	private static final long serialVersionUID = 4678206276499587830L;

	private List<ProblemItemDTO> problemItemList;
	private ListsService listsService = null;

	private List<ProblemItemDTO> problemItemDTOsList;
	private ProblemItemDTO chosenProblemItemDTO;
	private ProblemItemDTO problemItemDTO;
	private String chosenProblemItemId;


	public ListsService getListsService() {
		return listsService;
	}

	public void setListsService(ListsService listsService) {
		this.listsService = listsService;
	}

/*
	public String getchosenProblemItemId() {
		//return "0";
		return chosenProblemItemId;
	}
*/
	
	public String getChosenProblemItemId() {

		// Get the associated maintenance item id that's passe
		// as a parameter from the displayMaintenanceList page
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
		// a foreign key in the Mtask entry.
		//chosenProblemItemIdAsLong = Long.valueOf(chosenProblemItemId);

		return chosenProblemItemId;
	}
	
	public void setchosenProblemItemId(String chosenProblemItemId) {
		this.chosenProblemItemId = chosenProblemItemId;
	}


	public ProblemItemDTO getChosenProblemItemDTO() {
		
		// new stuff
		getChosenProblemItemId();
		long itemId = Long.parseLong(chosenProblemItemId);
		problemItemDTOsList = 
				listsService.getProblemItems(
						ProblemStateEnums.ALLPROBLEMS);
		for (ProblemItemDTO itemDTO:problemItemDTOsList) {
			if (itemDTO.getProblemitemid() == itemId) {
				return itemDTO;
			}
		}
		
		throw new RuntimeException(
				"chosenProblemItemId is not found in the list: " + itemId);		
		
	}
	
	public void setChosenProblemItemDTO(
			ProblemItemDTO chosenProblemItemDTO) {
		this.chosenProblemItemDTO = chosenProblemItemDTO;
	}


	public List<ProblemItemDTO> getProblemItemList() {

		problemItemList = 
				listsService.getProblemItems(
						ProblemStateEnums.ALLPROBLEMS);
		return problemItemList;

	}

	public List<ProblemItemDTO> getProblemItemListActive() {

		problemItemList = 
				listsService.getProblemItems(
						ProblemStateEnums.PROBLEMISACTIVE);
		return problemItemList;

	}

	public List<ProblemItemDTO> getProblemItemListInactive() {

		problemItemList = 
				listsService.getProblemItems(
						ProblemStateEnums.PROBLEMISINACTIVE);
		return problemItemList;

	}

/*
	public List<MaintenanceItemDTO> getMaintenanceItemListByNames() {
		maintenanceItemList = 
				listsService.getMaintenanceItems(
						SortEnums.ORDERBYNAME,
						MaintenanceTypeEnums.HOFELLER);
		return maintenanceItemList;
	}

	public List<MaintenanceItemDTO> getOwnerMaintenanceItemList() {
		maintenanceItemList = 
				listsService.getMaintenanceItems(
						SortEnums.ORDERBYNEXTSERVICEDATE,
						MaintenanceTypeEnums.OWNER);
		return maintenanceItemList;
	}

	public List<MaintenanceItemDTO> getOwnerMaintenanceItemListByNames() {
		maintenanceItemList = 
				listsService.getMaintenanceItems(
						SortEnums.ORDERBYNAME,
						MaintenanceTypeEnums.OWNER);
		return maintenanceItemList;
	}

	public List<MaintenanceItemDTO> getproblemItemDTOsList() {
		problemItemDTOsList = 
				listsService.getMaintenanceItems(
						SortEnums.ORDERBYNEXTSERVICEDATE,
						MaintenanceTypeEnums.HOFELLER);
		return problemItemDTOsList;
	}

	public MaintenanceItemDTO getMaintenanceItemDTO() {
		return maintenanceItemDTO;
	}

	public void setMaintenanceItemDTO(MaintenanceItemDTO maintenanceItemDTO) {
		this.maintenanceItemDTO = maintenanceItemDTO;
	}
*/
	
}