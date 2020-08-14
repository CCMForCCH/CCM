package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.service.ListsService;
import org.cohoman.view.controller.utils.MaintenanceTypeEnums;
import org.cohoman.view.controller.utils.SortEnums;

@ManagedBean
@SessionScoped
public class RetrieveMaintenanceItemsListController implements Serializable {

	private static final long serialVersionUID = 4678206276499587830L;

	private List<MaintenanceItemDTO> maintenanceItemList;
	private ListsService listsService = null;

	private List<MaintenanceItemDTO> maintenanceItemDTOsList;
	//private MaintenanceItemDTO chosenMaintenanceItemDTO;
	private MaintenanceItemDTO maintenanceItemDTO;
	private String chosenMaintenanceItemId;


	public ListsService getListsService() {
		return listsService;
	}

	public void setListsService(ListsService listsService) {
		this.listsService = listsService;
	}

	//public String getChosenMaintenanceItemId() {
		//return chosenMaintenanceItemId;
	//}
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
		//chosenMaintenanceItemIdAsLong = Long.valueOf(chosenMaintenanceItemId);

		return chosenMaintenanceItemId;
	}

	public void setChosenMaintenanceItemId(String chosenMaintenanceItemId) {
		this.chosenMaintenanceItemId = chosenMaintenanceItemId;
	}

	public MaintenanceItemDTO getChosenMaintenanceItemDTO() {
		
		// new stuff
		getChosenMaintenanceItemId();
		long itemId = Long.parseLong(chosenMaintenanceItemId);
		maintenanceItemDTOsList = 
				listsService.getMaintenanceItems(
						SortEnums.ORDERBYNEXTSERVICEDATE,
						MaintenanceTypeEnums.ALL);
		for (MaintenanceItemDTO itemDTO:maintenanceItemDTOsList) {
			if (itemDTO.getMaintenanceitemid() == itemId) {
				return itemDTO;
			}
		}
		
		throw new RuntimeException(
				"chosenMaintenanceItemId is not found in the list: " + itemId);		
		
	}

	/*
	public void setChosenMaintenanceItemDTO(
			MaintenanceItemDTO chosenMaintenanceItemDTO) {
		this.chosenMaintenanceItemDTO = chosenMaintenanceItemDTO;
	}
	*/

	public List<MaintenanceItemDTO> getMaintenanceItemList() {
		maintenanceItemList = 
				listsService.getMaintenanceItems(
						SortEnums.ORDERBYNEXTSERVICEDATE,
						MaintenanceTypeEnums.HOFELLER);
		return maintenanceItemList;
	}

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

	public List<MaintenanceItemDTO> getMaintenanceItemDTOsList() {
		maintenanceItemDTOsList = 
				listsService.getMaintenanceItems(
						SortEnums.ORDERBYNEXTSERVICEDATE,
						MaintenanceTypeEnums.HOFELLER);
		return maintenanceItemDTOsList;
	}

	public MaintenanceItemDTO getMaintenanceItemDTO() {
		return maintenanceItemDTO;
	}

	public void setMaintenanceItemDTO(MaintenanceItemDTO maintenanceItemDTO) {
		this.maintenanceItemDTO = maintenanceItemDTO;
	}

}
