package org.cohoman.view.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.model.service.EventService;

@ManagedBean
@SessionScoped
public class EnterWebsiteController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4678206276499587830L;
	private Role role;
	private EventService eventService = null;

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

	// Navigate to the proper home page for the chosen role
	public String chooseViewIndex() throws Exception {
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext().getSession(false);
		role = (Role) session.getAttribute(AuthenticateController.SESSIONVAR_CHOSEN_ROLE);

		// TODO: check for valid role???
		return role.getRolename();
		//return "indexForBasicuser";
	}
	
}
