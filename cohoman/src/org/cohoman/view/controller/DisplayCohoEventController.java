package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.integration.persistence.beans.CohoEvent;
import org.cohoman.model.service.EventService;

@ManagedBean
@SessionScoped
public class DisplayCohoEventController implements Serializable {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private static final long serialVersionUID = 4678206276499587830L;	
	private CohoEvent cohoevent;
	private EventService eventService = null;
	
	public DisplayCohoEventController() {
	}

	public CohoEvent getCohoevent() {
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		String cohoEventIdAsString = requestParams
				.get("CohoEventId");
		if (cohoEventIdAsString == null
				|| cohoEventIdAsString.length() == 0) {
			logger.log(Level.SEVERE, "Internal Error: missing CohoEventId parameter");
			return null;
		}
		long cohoEventId = Long.valueOf(cohoEventIdAsString);

		cohoevent = eventService.getCohoEvent(cohoEventId);
		return cohoevent;
	}

	public EventService getEventService() {
		return eventService;
	}
	
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}


	
}
