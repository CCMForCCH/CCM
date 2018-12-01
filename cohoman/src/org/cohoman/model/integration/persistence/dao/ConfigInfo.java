package org.cohoman.model.integration.persistence.dao;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.cohoman.model.business.ConfigurationManagerImpl;
import org.cohoman.model.integration.persistence.beans.TimePeriodBean;
import org.cohoman.model.integration.persistence.beans.TimePeriodTypeEnum;

public class ConfigInfo {

	private int mealPeriodStartYear = 2010;
	private int mealPeriodStartMonth = 11;
	private int mealPeriodStartDay = 20;
	private int mealPeriodEndYear = 2011;
	private int mealPeriodEndMonth = 1;
	private int mealPeriodEndDay = 6;
	private int pizzaStarttimeHour = 18;
	private TimePeriodBean currentPeriod;
	
	public ConfigInfo() {
		//TODO: ugly to invoke an implementation??
		List<TimePeriodBean>timePeriodList =
			(new TimePeriodDaoImpl()).getTimePeriods(TimePeriodTypeEnum.MEALPERIOD.toString());
		if (!timePeriodList.isEmpty()) {
			currentPeriod = timePeriodList.get(timePeriodList.size() - 1);
			Date periodStartDate = currentPeriod.getPeriodstartdate();
			Date periodEndDate = currentPeriod.getPeriodenddate();
			Calendar startCal = new GregorianCalendar();
			startCal.setTime(periodStartDate);
			mealPeriodStartYear = startCal.get(Calendar.YEAR);
			mealPeriodStartMonth = startCal.get(Calendar.MONTH);
			mealPeriodStartDay = startCal.get(Calendar.DAY_OF_MONTH);
			Calendar endCal = new GregorianCalendar();
			endCal.setTime(periodEndDate);
			mealPeriodEndYear = endCal.get(Calendar.YEAR);
			mealPeriodEndMonth = endCal.get(Calendar.MONTH);
			mealPeriodEndDay = endCal.get(Calendar.DAY_OF_MONTH);
		} else {
			//TODO
			// throw exception!!
		}
	}
	
	public String getMealPeriodStartDate() {
		GregorianCalendar cal = new GregorianCalendar(mealPeriodStartYear,
				mealPeriodStartMonth, mealPeriodStartDay);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(cal.getTime());
		return dateString;
	}

	public String getMealPeriodEndDate() {
		GregorianCalendar cal = new GregorianCalendar(mealPeriodEndYear,
				mealPeriodEndMonth, mealPeriodEndDay);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(cal.getTime());
		return dateString;
	}

	public int getMealPeriodStartYear() {
		return mealPeriodStartYear;
	}

	public void setMealPeriodStartYear(int mealPeriodStartYear) {
		this.mealPeriodStartYear = mealPeriodStartYear;
	}

	public int getMealPeriodStartMonth() {
		return mealPeriodStartMonth;
	}

	public void setMealPeriodStartMonth(int mealPeriodStartMonth) {
		this.mealPeriodStartMonth = mealPeriodStartMonth;
	}

	public int getMealPeriodStartDay() {
		return mealPeriodStartDay;
	}

	public void setMealPeriodStartDay(int mealPeriodStartDay) {
		this.mealPeriodStartDay = mealPeriodStartDay;
	}

	public int getMealPeriodEndYear() {
		return mealPeriodEndYear;
	}

	public void setMealPeriodEndYear(int mealPeriodEndYear) {
		this.mealPeriodEndYear = mealPeriodEndYear;
	}

	public int getMealPeriodEndMonth() {
		return mealPeriodEndMonth;
	}

	public void setMealPeriodEndMonth(int mealPeriodEndMonth) {
		this.mealPeriodEndMonth = mealPeriodEndMonth;
	}

	public int getMealPeriodEndDay() {
		return mealPeriodEndDay;
	}

	public void setMealPeriodEndDay(int mealPeriodEndDay) {
		this.mealPeriodEndDay = mealPeriodEndDay;
	}

	public int getPizzaStarttimeHour() {
		return pizzaStarttimeHour;
	}

	public void setPizzaStarttimeHour(int pizzaStarttimeHour) {
		this.pizzaStarttimeHour = pizzaStarttimeHour;
	}

}
