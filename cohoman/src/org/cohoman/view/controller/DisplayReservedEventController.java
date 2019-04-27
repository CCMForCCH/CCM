package org.cohoman.view.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.integration.persistence.beans.EventTypeDefs;
import org.cohoman.model.integration.persistence.beans.PrivateEvent;
import org.cohoman.model.service.EventService;

@ManagedBean
@SessionScoped
public class DisplayReservedEventController implements Serializable {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private static final long serialVersionUID = 4678206276499587830L;	
	private PrivateEvent privateevent;
	private EventService eventService = null;
	
	public DisplayReservedEventController() {
	}

	public PrivateEvent getPrivateevent() {
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		Map<String, String> requestParams = ctx.getExternalContext()
				.getRequestParameterMap();
		String privateEventIdAsString = requestParams
				.get("PrivateEventId");
		if (privateEventIdAsString == null
				|| privateEventIdAsString.length() == 0) {
			logger.log(Level.SEVERE, "Internal Error: missing PrivateEventId parameter");
			return null;
		}
		long privateEventId = Long.valueOf(privateEventIdAsString);

		try {
			privateevent = eventService.getPrivateEvent(privateEventId);
		} catch (CohomanException cex) {
			logger.log(Level.SEVERE,
					"Internal Error: private event not found for event id "
							+ privateEventId);
			return null;
		}
		
		// One adjustment to returned reserved event: set the "For Whom" value
		String whoComes = "";
		if (privateevent.isIsexclusiveevent()) {
			whoComes = "private";
		} else if (privateevent.isIsinclusiveevent()) {
			whoComes = "cohousers invited";
		} else if (privateevent.isIscohousingevent()) {
			whoComes = "for cohousers";
		} else {
			whoComes = "private";  // remaining choices are assumed to be private
		}
		privateevent.setWhoComes(whoComes);

		return privateevent;
	}

	public String getEventCharacteristics() {

		privateevent = getPrivateevent();

		String chosenCharacteristics = "";
		if (privateevent.isIscohousingevent()) {
			chosenCharacteristics += EventTypeDefs.COHOUSINGEVENTTYPE + ", ";
		}
		if (privateevent.isIsinclusiveevent()) {
			chosenCharacteristics += EventTypeDefs.INCLUSIVEEVENTTYPE + ", ";
		}
		if (privateevent.isIsexclusiveevent()) {
			chosenCharacteristics += EventTypeDefs.EXCLUSIVEEVENTTYPE + ", ";
		}
		if (privateevent.isIsincomeevent()) {
			chosenCharacteristics += EventTypeDefs.INCOMEEVENTTYPE + ", ";
		}
		if (privateevent.isIsphysicallyactiveevent()) {
			chosenCharacteristics += EventTypeDefs.PHYSICALLYACTIVEEVENTTYPE
					+ ", ";
		}
		if (chosenCharacteristics.length() > 0) {
			chosenCharacteristics = chosenCharacteristics.substring(0,
					chosenCharacteristics.length() - 2);
		}
		return chosenCharacteristics;
	}

	public String getIncomeEventCharacteristics() {
		privateevent = getPrivateevent();
		String incomeCharacteristics = "";
		if (privateevent.isAremajorityresidents()) {
			incomeCharacteristics += EventTypeDefs.AMAJORITYRESIDENTS + ", ";
		} else {
			incomeCharacteristics += EventTypeDefs.AMAJORITYNOTRESIDENTS + ", ";
		}
		int donation = privateevent.getDonation();
		incomeCharacteristics += "Donation = $" + donation;
		return incomeCharacteristics;
	}

	public String getPhysicalCharacteristics() {
		privateevent = getPrivateevent();
		String physicalCharacteristics = "";
		if (privateevent.isIsonetimeparty()) {
			physicalCharacteristics += EventTypeDefs.ONETIMEPARTY + ", ";
		} else {
			physicalCharacteristics += EventTypeDefs.NOTONETIMEPARTY + ", ";
		}
		if (privateevent.isIsclassorworkshop()) {
			physicalCharacteristics += EventTypeDefs.CLASSORWORKSHOP + ", ";
		} else {
			physicalCharacteristics += EventTypeDefs.NOTCLASSORWORKSHOP + ", ";
		}
		if (physicalCharacteristics.length() > 0) {
			physicalCharacteristics = physicalCharacteristics.substring(0,
					physicalCharacteristics.length() - 2);
		}
		return physicalCharacteristics;
	}
	
	public String getPrintableApprovedDate() {
		privateevent = getPrivateevent();
		SimpleDateFormat formatter;
		Calendar cal = new GregorianCalendar();
		cal.setTime(privateevent.getApprovaldate());
		if (cal.get(Calendar.HOUR_OF_DAY) == 0) {
			formatter = new SimpleDateFormat("EEE, MMM d, yyyy");
		} else {
			formatter = new SimpleDateFormat("EEE, MMM d, yyyy h:mm aa");
		}
		return formatter.format(privateevent.getApprovaldate().getTime());
	}

	public EventService getEventService() {
		return eventService;
	}
	
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}

}
