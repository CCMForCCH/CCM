package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.dto.CohoEventDTO;
import org.cohoman.model.integration.persistence.beans.CohoEvent;
import org.cohoman.model.integration.persistence.beans.EventTypeDefs;
import org.cohoman.model.integration.persistence.beans.SpaceBean;
import org.cohoman.model.service.EventService;
import org.cohoman.view.controller.utils.CalendarUtils;

@ManagedBean
@SessionScoped
public class EditCohoEventController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4678206276499587830L;

	private List<CohoEvent> cohoEventList;
	private CohoEvent chosenCohoEvent;
	private String chosenCohoEventString;
	private String chosenCohoEventTypeString;
	private String cohoOperation;
	private CohoEventDTO cohoEventDTO;
	private String slotNumberStart;
	private String slotNumberEnd;
	private String cohoEventName;
	private String cohoEventInfo;
	private Set<SpaceBean> chosenSpaceList = new LinkedHashSet<SpaceBean>();
	private Set<String> chosenSpaceListStringInts = new LinkedHashSet<String>();

	public CohoEventDTO getCohoEventDTO() {
		return cohoEventDTO;
	}

	public void setCohoEventDTO(CohoEventDTO cohoEventDTO) {
		this.cohoEventDTO = cohoEventDTO;
	}

	private EventService eventService = null;

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public String getChosenCohoEventString() {
		return chosenCohoEventString;
	}

	public void setChosenCohoEventString(String chosenCohoEventString) {
		this.chosenCohoEventString = chosenCohoEventString;
	}

	public String getChosenCohoEventTypeString() {
		return chosenCohoEventTypeString;
	}

	public void setChosenCohoEventTypeString(String chosenCohoEventTypeString) {
		this.chosenCohoEventTypeString = chosenCohoEventTypeString;
	}

	public String getCohoOperation() {
		return cohoOperation;
	}

	public void setCohoOperation(String cohoOperation) {
		this.cohoOperation = cohoOperation;
	}

	public List<CohoEvent> getCohoEventList() {
		cohoEventList = eventService.getCurrentCohoEvents();
		return cohoEventList;
	}

	public void setCohoEventList(List<CohoEvent> cohoEventList) {
		this.cohoEventList = cohoEventList;
	}

	public CohoEvent getChosenCohoEvent() {
		return chosenCohoEvent;
	}

	public void setChosenCohoEvent(CohoEvent chosenCohoEvent) {
		this.chosenCohoEvent = chosenCohoEvent;
	}

	public String getSlotNumberStart() {
		return slotNumberStart;
	}

	public void setSlotNumberStart(String slotNumberStart) {
		this.slotNumberStart = slotNumberStart;

		// Set the ending time to be one hour more than the
		// start time as the default. Relies on Ajax
		int slotStartAsInt = 0;
		if (slotNumberStart != null) {
			slotStartAsInt = Integer.parseInt(slotNumberStart);
		}
		slotStartAsInt += 2;

		// Make sure slots don't wrap back to 0
		if (slotStartAsInt > 35) {
			slotStartAsInt = 35;
		}
		this.slotNumberEnd = String.valueOf(slotStartAsInt);

	}

	public String getSlotNumberEnd() {
		return slotNumberEnd;
	}

	public void setSlotNumberEnd(String slotNumberEnd) {

		// Only set the end of the slots if it is greater than the start
		int slotStartAsInt = 0;
		if (slotNumberStart != null) {
			slotStartAsInt = Integer.parseInt(slotNumberStart);
		}
		int slotEndAsInt = 0;
		if (slotNumberEnd != null) {
			slotEndAsInt = Integer.parseInt(slotNumberEnd);
		}

		if (slotEndAsInt > slotStartAsInt) {
			this.slotNumberEnd = slotNumberEnd;
		}
	}

	public String getCohoEventName() {
		return cohoEventName;
	}

	public void setCohoEventName(String cohoEventName) {
		this.cohoEventName = cohoEventName;
	}

	public String getCohoEventInfo() {
		return cohoEventInfo;
	}

	public void setCohoEventInfo(String cohoEventInfo) {
		this.cohoEventInfo = cohoEventInfo;
	}

	public Set<SpaceBean> getChosenSpaceList() {
		return chosenSpaceList;
	}

	public void setChosenSpaceList(Set<SpaceBean> chosenSpaceList) {
		this.chosenSpaceList = chosenSpaceList;
	}

	public Set<String> getChosenSpaceListStringInts() {
		return chosenSpaceListStringInts;
	}

	public void setChosenSpaceListStringInts(
			Set<String> chosenSpaceListStringInts) {
		this.chosenSpaceListStringInts = chosenSpaceListStringInts;
	}

	public CalendarUtils.TimeSlot[] getTimeSlotsOfTheDay() {
		return CalendarUtils.getTimeSlotsOfTheDay(2010, 1);
	}

	public String editCohoEventView() throws Exception {

		Long eventId = Long.valueOf(chosenCohoEventString);
		chosenCohoEvent = eventService.getCohoEvent(eventId);
		slotNumberStart = Integer.toString((CalendarUtils
				.getTimeSlotForDate(chosenCohoEvent.getEventDate())));
		slotNumberEnd = Integer.toString((CalendarUtils
				.getTimeSlotForDate(chosenCohoEvent.getEventdateend())));
		chosenCohoEventTypeString = chosenCohoEvent.getEventtype();
		cohoEventName = chosenCohoEvent.getEventName();
		cohoEventInfo = chosenCohoEvent.getEventinfo();
		chosenCohoEventTypeString = chosenCohoEvent.getEventtype();

		// Get list of checkboxes from database and set appropriate
		// checkboxes in UI
		chosenSpaceList = chosenCohoEvent.getSpaceList();
		chosenSpaceListStringInts.clear();
		for (SpaceBean oneSpaceBean : chosenSpaceList) {
			chosenSpaceListStringInts.add(oneSpaceBean.getSpaceId().toString());
		}
		if (cohoOperation.equals("deleteCohoEvent")) {
			eventService.deleteCohoEvent(chosenCohoEvent);
		}
		return cohoOperation;
	}

	public String editCohoEventItemsView() throws Exception {

		// Have to recompute Date based on potential time slot change
		int startSlotAsInt = Integer.parseInt(slotNumberStart);
		int endSlotAsInt = Integer.parseInt(slotNumberEnd);
		if (startSlotAsInt >= endSlotAsInt) {
			// Times are not valid
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									"Invalid times: starting time must be less than the ending time"));
			return null;
		}
		chosenCohoEvent.setEventDate(CalendarUtils.setTimeSlotForDate(
				chosenCohoEvent.getEventDate(), startSlotAsInt));
		chosenCohoEvent.setEventdateend(CalendarUtils.setTimeSlotForDate(
				chosenCohoEvent.getEventdateend(), endSlotAsInt));
		chosenCohoEvent.setEventName(cohoEventName);
		chosenCohoEvent.setEventinfo(cohoEventInfo);
		chosenCohoEvent.setEventtype(chosenCohoEventTypeString);
		chosenCohoEvent.setChosenSpaceListStringInts(chosenSpaceListStringInts);
		try {
			eventService.editCohoEvent(chosenCohoEvent);
		} catch (CohomanException ex) {
			FacesMessage message = new FacesMessage(ex.getErrorText());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
		return "gohome";
	}

	public String[] getEventTypeChoices() {
		return EventTypeDefs.eventTypeChoicesNoApproval;
	}

	public List<SpaceBean> getAllSpaces() {
		return eventService.getAllSpaces();
	}

}
