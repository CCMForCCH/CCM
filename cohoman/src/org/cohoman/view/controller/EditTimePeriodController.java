package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.sql.Date;
import java.util.GregorianCalendar;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.cohoman.model.integration.persistence.beans.TimePeriodBean;
import org.cohoman.model.integration.persistence.beans.TimePeriodTypeEnum;
import org.cohoman.model.service.ConfigurationService;
import org.cohoman.view.controller.utils.CalendarUtils;

@ManagedBean
@SessionScoped
public class EditTimePeriodController implements Serializable {

	/**
	 * 
	 */
	private TimePeriodTypeEnum timePeriodTypeEnum;
	private Date periodStartDate;
	private Date periodEndDate;
	private String startYear = null;
	private String startMonth;
	private String startDay;
	private String endYear;
	private String endMonth;
	private String endDay;
	private List<TimePeriodBean> timePeriodList;
	private TimePeriodBean currentPeriod;
	private ConfigurationService configurationService = null;
	

	public CalendarUtils.OneMonth[] getMonthsOfTheYear() {
		return CalendarUtils.getMonthsOfTheYear();
	}

	public String[] getYears() {
		return CalendarUtils.getYears();
	}

	/*
	public String[] getStartDaysOfTheMonth() {
		return CalendarUtils.getDaysOfTheMonth(Integer.parseInt(startYear),
				Integer.parseInt(startMonth));
	}

	public String[] getEndDaysOfTheMonth() {
		return CalendarUtils.getDaysOfTheMonth(Integer.parseInt(endYear),
				Integer.parseInt(endMonth));
	}
*/
	public String[] getMondaysOfTheMonth() {
		
		String[] Mondays = CalendarUtils.getSpecificDaysOfTheMonthTotally(Integer.parseInt(startYear),
				Integer.parseInt(startMonth), Calendar.MONDAY);
		if (Mondays.length == 0) {
			// Dates are not valid
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage("Internal Error: empty list of Mondays encountered."));
			return null;
		}
		return Mondays;
	}
	
	public String[] getSundaysOfTheMonth() {
		
		String[] Sundays = CalendarUtils.getSpecificDaysOfTheMonthTotally(Integer.parseInt(endYear),
				Integer.parseInt(endMonth), Calendar.SUNDAY);
		if (Sundays.length == 0) {
			// Dates are not valid
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage("Internal Error: empty list of Sundays encountered."));
			return null;
		}
		return Sundays;
	}

	public TimePeriodTypeEnum getTimePeriodTypeEnum() {
		return timePeriodTypeEnum;
	}

	public void setTimePeriodTypeEnum(TimePeriodTypeEnum timePeriodTypeEnum) {
		this.timePeriodTypeEnum = timePeriodTypeEnum;
	}

	public Date getPeriodStartDate() {
		return periodStartDate;
	}

	public void setPeriodStartDate(Date periodStartDate) {
		this.periodStartDate = periodStartDate;
	}

	public Date getPeriodEndDate() {
		return periodEndDate;
	}

	public void setPeriodEndDate(Date periodEndDate) {
		this.periodEndDate = periodEndDate;
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
			setCurrentPeriodDates();
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

	public String getEndYear() {
		return endYear;
	}

	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}

	public String getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}

	public String getEndDay() {
		return endDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}
	
	public String editMealTimePeriod() throws Exception {
		Calendar chosenStartDate = 
			new GregorianCalendar(Integer.parseInt(startYear),
					Integer.parseInt(startMonth), Integer.parseInt(startDay));
		Calendar chosenEndDate = 
			new GregorianCalendar(Integer.parseInt(endYear),
					Integer.parseInt(endMonth), Integer.parseInt(endDay));
		currentPeriod.setPeriodstartdate(new Date(chosenStartDate.getTime().getTime()));
		currentPeriod.setPeriodenddate(new Date(chosenEndDate.getTime().getTime()));
		
		Calendar rightNow = Calendar.getInstance();
		if (chosenEndDate.before(rightNow) ||
				chosenStartDate.equals(chosenEndDate) ||
				chosenStartDate.after(chosenEndDate))
		{
			// Dates are not valid
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage("Invalid dates: end date must be in the future and start date before end date"));
			return null;
		} 

		configurationService.editTimePeriod(currentPeriod);
		return "editedMealTimePeriod";
	}
	
	private void setCurrentPeriodDates() {
		timePeriodList =
			configurationService.getTimePeriods(TimePeriodTypeEnum.MEALPERIOD);
		if (!timePeriodList.isEmpty()) {
			currentPeriod = timePeriodList.get(timePeriodList.size() - 1);
			periodStartDate = currentPeriod.getPeriodstartdate();
			periodEndDate = currentPeriod.getPeriodenddate();
			Calendar startCal = new GregorianCalendar();
			startCal.setTime(periodStartDate);
			startYear = new Integer(startCal.get(Calendar.YEAR)).toString();
			startMonth = new Integer(startCal.get(Calendar.MONTH)).toString();
			startDay = new Integer(startCal.get(Calendar.DAY_OF_MONTH)).toString();
			Calendar endCal = new GregorianCalendar();
			endCal.setTime(periodEndDate);
			endYear = new Integer(endCal.get(Calendar.YEAR)).toString();
			endMonth = new Integer(endCal.get(Calendar.MONTH)).toString();
			endDay = new Integer(endCal.get(Calendar.DAY_OF_MONTH)).toString();
		} else {
			//TODO
			// throw exception!!
		}
	}
}
