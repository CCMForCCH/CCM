package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import org.cohoman.model.integration.persistence.beans.TimePeriodBean;
import org.cohoman.model.integration.persistence.beans.TimePeriodTypeEnum;
import org.cohoman.model.service.ConfigurationService;

@ManagedBean
@SessionScoped
public class DeleteTimePeriodController implements Serializable {

	private List<TimePeriodBean> timePeriodList;
	private String chosenTimePeriodString;

	private ConfigurationService configurationService = null;


	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public List<TimePeriodBean> getTimePeriodList() {
		timePeriodList =
			configurationService.getTimePeriods(TimePeriodTypeEnum.MEALPERIOD);
		return timePeriodList;
	}

	public void setTimePeriodList(List<TimePeriodBean> timePeriodList) {
		this.timePeriodList = timePeriodList;
	}

	public String getChosenTimePeriodString() {
		return chosenTimePeriodString;
	}

	public void setChosenTimePeriodString(String chosenMealTimePeriodString) {
		this.chosenTimePeriodString = chosenMealTimePeriodString;
	}	

	public String deleteMealTimePeriodView() throws Exception {

		Long mealTimePeriodId = Long.valueOf(chosenTimePeriodString);
		configurationService.deleteTimePeriod(mealTimePeriodId);
		return "deletedMealTimePeriod";
	}
	
}
