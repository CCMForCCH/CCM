package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import org.cohoman.model.dto.MaintenanceItemDTO;
import org.cohoman.model.service.ListsService;

@ManagedBean
@SessionScoped
public class RetrieveMaintenanceItemsListController implements Serializable {

	private static final long serialVersionUID = 4678206276499587830L;

	private List<MaintenanceItemDTO> maintenanceItemList;
	private ListsService listsService = null;


	public ListsService getListsService() {
		return listsService;
	}

	public void setListsService(ListsService listsService) {
		this.listsService = listsService;
	}

	
	public List<MaintenanceItemDTO> getMaintenanceItemList() {
		maintenanceItemList = listsService.getMaintenanceItems();
		return maintenanceItemList;
	}

}
