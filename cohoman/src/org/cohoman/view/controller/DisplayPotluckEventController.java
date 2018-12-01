package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.integration.persistence.beans.PotluckEvent;
import org.cohoman.model.service.EventService;

@ManagedBean
@SessionScoped
public class DisplayPotluckEventController implements Serializable {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private static final long serialVersionUID = 4678206276499587830L;	
	private PotluckEvent potluckevent;
	private EventService eventService = null;
	
	public DisplayPotluckEventController() {
	}

	public PotluckEvent getPotluckevent() {
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		String potluckEventIdAsString = requestParams
				.get("PotluckEventId");
		if (potluckEventIdAsString == null
				|| potluckEventIdAsString.length() == 0) {
			logger.log(Level.SEVERE, "Internal Error: missing PotluckEventId parameter");
			return null;
		}
		long potluckEventId = Long.valueOf(potluckEventIdAsString);

		potluckevent = eventService.getPotluckEvent(potluckEventId);
		return potluckevent;
	}

	public EventService getEventService() {
		return eventService;
	}
	
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}
	
}
