package org.cohoman.view.controller;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.dto.SecurityStartingPointDTO;
import org.cohoman.model.integration.persistence.beans.UnitBean;
import org.cohoman.model.service.ConfigurationService;
import org.cohoman.view.controller.utils.CalendarUtils;

@ManagedBean
@SessionScoped
public class EditWeSecurityStartingPointController implements Serializable {

	/**
	 * 
	 */
	private Date periodStartDate;
	private String startYear = null;
	private String startMonth;
	private String startDay;
	private String chosenUnitString;
	private SecurityStartingPointDTO securityStartingPointDTO;
	private ConfigurationService configurationService = null;
	private List<UnitBean> unitBeanList;

	public CalendarUtils.OneMonth[] getMonthsOfTheYear() {
		return CalendarUtils.getMonthsOfTheYear();
	}

	public String[] getYears() {
		return CalendarUtils.getYears();
	}

	public String[] getSundaysOfTheMonth() {
		// Advance past ??
		return CalendarUtils.getSpecificDaysOfTheMonthTotally(Integer.parseInt(startYear),
				Integer.parseInt(startMonth), Calendar.SUNDAY);
	}


	public String[] getSaturdaysOfTheMonth() {
		// Advance past ??
		return CalendarUtils.getSpecificDaysOfTheMonthTotally(Integer.parseInt(startYear),
				Integer.parseInt(startMonth), Calendar.SATURDAY);
	}

	public Date getPeriodStartDate() {
		return periodStartDate;
	}

	public void setPeriodStartDate(Date periodStartDate) {
		this.periodStartDate = periodStartDate;
	}

	public String getChosenUnitString() {
		return chosenUnitString;
	}

	public void setChosenUnitString(String chosenUnitString) {
		this.chosenUnitString = chosenUnitString;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public String getStartYear() {
		return startYear;
	}

	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	public String getStartMonth() {
		if (startMonth == null) {
			//TODO: improve this way of init'ing fields
			// relies on being called as 1st field in the page
			setCurrentPeriodDate();
		}
		return startMonth;
	}

	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}

	public String getStartDay() {
		return startDay;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}
	
	public String editWeSecurityStartingPoint() throws Exception {
		Calendar chosenStartDate = 
			new GregorianCalendar(Integer.parseInt(startYear),
					Integer.parseInt(startMonth), Integer.parseInt(startDay));

		Calendar currentSaturday = Calendar.getInstance();
		CalendarUtils.adjustToStartingSaturday(currentSaturday);
		if (chosenStartDate.after(currentSaturday))
		{
			// Future date is not valid
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage("Invalid date: cannot set security date beyond the current week"));
			return null;
		} 
		
		securityStartingPointDTO.setStartdate(new java.sql.Date((chosenStartDate.getTime()).getTime()));
		securityStartingPointDTO.setUnitnumber(chosenUnitString);
		configurationService.updateSecurityStart(securityStartingPointDTO);
		return "editedWeSecurityStartingPoint";
	}
	
	public List<UnitBean> getUnitBeanList() {
		unitBeanList = configurationService.getAllUnits();
		return unitBeanList;
	}

	public List<UnitBean> getWeUnitBeanList() {
		unitBeanList = configurationService.getWestendUnits();
		return unitBeanList;
	}

	private void setCurrentPeriodDate() {
		securityStartingPointDTO = 
			configurationService.getWeSecurityStart();
		periodStartDate = securityStartingPointDTO.getStartdate();
		Calendar startCal = new GregorianCalendar();
		startCal.setTime(periodStartDate);
		startYear = new Integer(startCal.get(Calendar.YEAR)).toString();
		startMonth = new Integer(startCal.get(Calendar.MONTH)).toString();
		startDay = new Integer(startCal.get(Calendar.DAY_OF_MONTH)).toString();
		chosenUnitString = securityStartingPointDTO.getUnitnumber();
	}
}
