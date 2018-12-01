package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import org.cohoman.model.business.MealSchedule.MealScheduleText;
import org.cohoman.model.service.EventService;

//@Named
@ManagedBean
@SessionScoped
public class MealsScheduleController implements Serializable {

	// Action: call the service layer to build and return a schedule
	// which is basically a list of weeks of days containing events

	/**
	 * 
	 */
	private static final long serialVersionUID = 1401630953645986210L;

	Logger logger = Logger.getLogger(this.getClass().getName());

	private EventService eventService = null;

	private int column2showable;
	private int column3showable;
	private int column4showable;

	public int getColumn2showable() {
		return column2showable;
	}

	public void setColumn2showable(int column2showable) {
		this.column2showable = column2showable;
	}

	public int getColumn3showable() {
		return column3showable;
	}

	public void setColumn3showable(int column3showable) {
		this.column3showable = column3showable;
	}

	public int getColumn4showable() {
		return column4showable;
	}

	public void setColumn4showable(int column4showable) {
		this.column4showable = column4showable;
	}

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public MealsScheduleController() {

	}

	public List<List<MealScheduleText>> getMealTextRows() {
		// eventService.getCurrentMealScheduleRows()

		column2showable = 0;
		column3showable = 0;
		column4showable = 0;

		int maxRowSize = 0;
		List<List<MealScheduleText>> mealRows = eventService
				.getCurrentMealScheduleRows();
		for (List<MealScheduleText> oneMealRow : mealRows) {
			if (oneMealRow.size() > maxRowSize) {
				maxRowSize = oneMealRow.size();
			}
		}

		// Only allow 4 meals per week
		if (maxRowSize > 4) {
			maxRowSize = 4;
			logger.warning("WARNING: There are more than 5 meals scheduled for a week and the meals calendar cannot handle that many.");
		}
		
		// Need to set a value to indicate whether we
		// display a column (i.e. it has at least one
		// entry, or we don't (has no entries).
		switch (maxRowSize) {

		case 4:
			column4showable = 1;
		case 3:
			column3showable = 1;
		case 2:
			column2showable = 1;
		}

		return mealRows;
	}
}
