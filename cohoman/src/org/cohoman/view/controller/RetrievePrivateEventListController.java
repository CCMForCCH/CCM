package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.dto.PrivateEventDTO;
import org.cohoman.model.integration.persistence.beans.PrivateEvent;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.model.service.EventService;
import org.cohoman.model.service.UserService;

@ManagedBean
@SessionScoped
public class RetrievePrivateEventListController implements Serializable {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private static final long serialVersionUID = 4678206276499587830L;

	private List<PrivateEvent> privateEventList;
	private EventService eventService = null;
	private UserService userService = null;

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<PrivateEvent> getMyPrivateEventList() {
		privateEventList = eventService.getMyPrivateEvents();
		return privateEventList;
	}

	public void setMyPrivateEventList(List<PrivateEvent> privateEventList) {
		this.privateEventList = privateEventList;
	}

	public List<PrivateEvent> getPendingPrivateEventList() {
		privateEventList = eventService.getPendingPrivateEvents();
		return privateEventList;
	}

	public void setPendingPrivateEventList(List<PrivateEvent> privateEventList) {
		this.privateEventList = privateEventList;
	}

	public List<PrivateEvent> getAllPrivateEventList() {
		privateEventList = eventService.getAllPrivateEvents();
		return privateEventList;
	}

	public void setAllPrivateEventList(List<PrivateEvent> privateEventList) {
		this.privateEventList = privateEventList;
	}

	public List<PrivateEventDTO> getUpcomingPrivateEventList() {
		privateEventList = eventService.getUpcomingPrivateEvents();
		List<PrivateEventDTO> privateEventDTOList = new ArrayList<PrivateEventDTO>();

		for (PrivateEvent privateEvent : privateEventList) {

			PrivateEventDTO dto = makePrivateEventDTO(privateEvent);
			
			// Compute and stuff requester name into the DTO
			dto.setRequesterName(userService.getUser(
					privateEvent.getRequester()).getUsername());
			privateEventDTOList.add(dto);
		}
		return privateEventDTOList;
	}

	public void setUpcomingPrivateEventList(List<PrivateEvent> privateEventList) {
		this.privateEventList = privateEventList;
	}

	public void deletePrivateEvent() {
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		String chosenPrivateEventIdAsString = requestParams
				.get("chosenPrivateEventId");
		if (chosenPrivateEventIdAsString == null
				|| chosenPrivateEventIdAsString.length() == 0) {
			logger.log(Level.SEVERE,
					"Invalid EventId value in RetrievePrivateEventController.");
			FacesMessage message = new FacesMessage(LoggingUtils.INTERNAL_ERROR);
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}
		long chosenPrivateEventId = Long.valueOf(chosenPrivateEventIdAsString);
		try {
			eventService.deletePrivateEvent(chosenPrivateEventId);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			FacesMessage message = new FacesMessage(LoggingUtils.INTERNAL_ERROR);
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;

		}

	}

	private PrivateEventDTO makePrivateEventDTO(PrivateEvent privateEvent) {

		PrivateEventDTO dto = new PrivateEventDTO();
		//dto.setEventDate(privateEvent.getEventDate());
		//dto.setEventdateend(privateEvent.getEventdateend());
		dto.setRequester(privateEvent.getRequester());
		dto.setRequesterName("");
		dto.setEventName(privateEvent.getEventName());
		dto.setEventinfo(privateEvent.getEventinfo());
		dto.setPrintableSpaceList(privateEvent.getPrintableSpaceList());
		dto.setPrintableEventDate(privateEvent.getPrintableEventDate());
		dto.setPrintableEventDateEnd(privateEvent.getPrintableEventDateEnd());
		dto.setPrintableEventDateDay(privateEvent.getPrintableEventDateDay());
		dto.setPrintableEventDateTimeStart(privateEvent.getPrintableEventDateTimeStart());
		dto.setPrintableEventDateTimeEnd(privateEvent.getPrintableEventDateTimeEnd());
		String whoComes = "";
		if (privateEvent.isIsexclusiveevent()) {
			whoComes = "private";
		} else if (privateEvent.isIsinclusiveevent()) {
			whoComes = "cohousers invited";
		} else if (privateEvent.isIscohousingevent()) {
			whoComes = "for cohousers";
		} else {
			whoComes = "private";  // remaining choices are assumed to be private
		}
		dto.setWhoComes(whoComes);
		return dto;

	}
}
