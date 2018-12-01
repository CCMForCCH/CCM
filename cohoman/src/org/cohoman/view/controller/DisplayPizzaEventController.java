package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.integration.persistence.beans.PizzaEvent;
import org.cohoman.model.service.EventService;

@ManagedBean
@SessionScoped
public class DisplayPizzaEventController implements Serializable {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private static final long serialVersionUID = 4678206276499587830L;	
	private PizzaEvent pizzaevent;
	private EventService eventService = null;
	
	public DisplayPizzaEventController() {
	}

	public PizzaEvent getPizzaevent() {
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		String pizzaEventIdAsString = requestParams
				.get("PizzaEventId");
		if (pizzaEventIdAsString == null
				|| pizzaEventIdAsString.length() == 0) {
			logger.log(Level.SEVERE, "Internal Error: missing PizzaEventId parameter");
			return null;
		}
		long pizzaEventId = Long.valueOf(pizzaEventIdAsString);

		pizzaevent = eventService.getPizzaEvent(pizzaEventId);
		return pizzaevent;
	}

	public EventService getEventService() {
		return eventService;
	}
	
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}


	
}
