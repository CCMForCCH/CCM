package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.integration.persistence.beans.MealEvent;
import org.cohoman.model.service.EventService;

@ManagedBean
@SessionScoped
public class DisplayMealEventController implements Serializable {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private static final long serialVersionUID = 4678206276499587830L;
	private MealEvent mealevent;
	private EventService eventService = null;

	public DisplayMealEventController() {
	}

	public MealEvent getMealevent() {

		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		String mealEventIdAsString = requestParams.get("MealEventId");
		if (mealEventIdAsString == null || mealEventIdAsString.length() == 0) {
			logger.log(Level.SEVERE,
					"Internal Error: missing MealEventId parameter");
			return null;
		}
		long mealEventId = Long.valueOf(mealEventIdAsString);

		mealevent = eventService.getMealEvent(mealEventId);
		if (mealevent == null) {
			logger.log(Level.SEVERE,
					"Internal Error: unable to find meal event for id " + mealEventId);
			return null;
		}

		return mealevent;
	}

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

}
