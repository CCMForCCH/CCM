package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import org.cohoman.model.integration.persistence.beans.CohoEvent;
import org.cohoman.model.service.EventService;

@ManagedBean
@SessionScoped
public class RetrieveCohoEventListController implements Serializable {

	private static final long serialVersionUID = 4678206276499587830L;

	private List<CohoEvent> cohoEventList;
	private EventService eventService = null;

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public List<CohoEvent> getCohoEventList() {
		cohoEventList = eventService.getCurrentCohoEvents();
		return cohoEventList;
	}

	public void setCohoEventList(List<CohoEvent> cohoEventList) {
		this.cohoEventList = cohoEventList;
	}

}
