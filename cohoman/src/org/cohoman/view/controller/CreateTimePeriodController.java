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

import org.cohoman.model.integration.persistence.beans.TimePeriodBean;
import org.cohoman.model.integration.persistence.beans.TimePeriodTypeEnum;
import org.cohoman.model.service.ConfigurationService;
import org.cohoman.view.controller.utils.CalendarUtils;

@ManagedBean
@SessionScoped
public class CreateTimePeriodController implements Serializable {

	/**
	 * 
	 */
	private TimePeriodTypeEnum timePeriodTypeEnum;
	private Date periodStartDate;
	private Date periodEndDate;
	private String startYear;
	private String startMonth;
	private String startDay;
	private String endYear;
	private String endMonth;
	private String endDay;

	private ConfigurationService configurationService = null;
	
	public CreateTimePeriodController() {
	
		Calendar myCal = CalendarUtils.getNextMondayCal();
		startYear = new Integer(myCal.get(Calendar.YEAR)).toString();
		startMonth = new Integer(myCal.get(Calendar.MONTH)).toString();
		startDay = new Integer(myCal.get(Calendar.DAY_OF_MONTH)).toString();
		myCal.add(Calendar.DAY_OF_MONTH, 6 + 5 * 7);
		endYear = new Integer(myCal.get(Calendar.YEAR)).toString();
		endMonth = new Integer(myCal.get(Calendar.MONTH)).toString();
		endDay = new Integer(myCal.get(Calendar.DAY_OF_MONTH)).toString();
		
	}

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
*/
	
	public String[] getMondaysOfTheMonth() {
		// Advance past Mondays gone by
		return CalendarUtils.getSpecificDaysOfTheMonth(Integer.parseInt(startYear),
				Integer.parseInt(startMonth), Calendar.MONDAY);
	}
	public String[] getSundaysOfTheMonth() {
		// Advance past ??
		return CalendarUtils.getSpecificDaysOfTheMonth(Integer.parseInt(endYear),
				Integer.parseInt(endMonth), Calendar.SUNDAY);
	}
	
	/*
	public String[] getEndDaysOfTheMonth() {
		return CalendarUtils.getDaysOfTheMonth(Integer.parseInt(endYear),
				Integer.parseInt(endMonth));
	}
*/
	
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
	
	public String addMealTimePeriod() throws Exception {
		Calendar chosenStartDate = 
			new GregorianCalendar(Integer.parseInt(startYear),
					Integer.parseInt(startMonth), Integer.parseInt(startDay));
		Calendar chosenEndDate = 
			new GregorianCalendar(Integer.parseInt(endYear),
					Integer.parseInt(endMonth), Integer.parseInt(endDay));
		Calendar rightNow = Calendar.getInstance();
		if (chosenStartDate.before(rightNow) ||
				chosenEndDate.before(rightNow) ||
				chosenStartDate.equals(chosenEndDate) ||
				chosenStartDate.after(chosenEndDate))
		{
			// Dates are not valid
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage("Invalid dates: must be in the future and start date before end date"));
			return null;
		} 
		
		// Now make sure new period doesn't overlap with most recent time period
		List<TimePeriodBean> timePeriodList =
			configurationService.getTimePeriods(TimePeriodTypeEnum.MEALPERIOD);
		if (!timePeriodList.isEmpty()) {
			TimePeriodBean currentPeriod = timePeriodList.get(timePeriodList.size() - 1);
			//periodStartDate = currentPeriod.getPeriodstartdate();
			periodEndDate = currentPeriod.getPeriodenddate();
			Calendar periodEndDateCal = new GregorianCalendar();
			periodEndDateCal.setTime(periodEndDate);
			if (!chosenStartDate.after(periodEndDateCal)) {
				// Dates are not valid
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage("Invalid period: new period cannot overlap current period"));
				return null;	
			}
			
		} else {
			//TODO
			// throw exception!!
		}
		
		configurationService.createTimePeriod(TimePeriodTypeEnum.MEALPERIOD,
				// Odd way to 'lose" the time and just have the date using java.sql.Date
				new Date(chosenStartDate.getTime().getTime()), 
				new Date(chosenEndDate.getTime().getTime()));
		return "createdMealTimePeriod";
	}
	
}
