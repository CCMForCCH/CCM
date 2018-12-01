package org.cohoman.model.service;

import java.sql.Date;
import java.util.List;

import org.cohoman.model.business.ConfigurationManager;
import org.cohoman.model.dto.SecurityStartingPointDTO;
import org.cohoman.model.integration.persistence.beans.TimePeriodBean;
import org.cohoman.model.integration.persistence.beans.TimePeriodTypeEnum;
import org.cohoman.model.integration.persistence.beans.UnitBean;

public class ConfigurationServiceImpl implements ConfigurationService {

	private ConfigurationManager configurationManager = null;
	
	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}
	
	public void createTimePeriod(TimePeriodTypeEnum timePeriodTypeEnum, Date periodStartDate,
			Date periodEndDate) {
		configurationManager.createTimePeriod(timePeriodTypeEnum, periodStartDate, periodEndDate);	
	}

	public void editTimePeriod(TimePeriodBean timePeriodBean) {
		configurationManager.editTimePeriod(timePeriodBean);
	}
	
	public List<TimePeriodBean> getTimePeriods(TimePeriodTypeEnum timePeriodTypeEnum){
		List<TimePeriodBean> timePeriodList = configurationManager.getTimePeriods(timePeriodTypeEnum);
		return timePeriodList;
	}

	public void deleteTimePeriod(Long timePeriodId) {
		configurationManager.deleteTimePeriod(timePeriodId);
	}

	public List<UnitBean> getAllUnits() {
		return configurationManager.getAllUnits();
	}

	public List<UnitBean> getCommonhouseUnits() {
		return configurationManager.getCommonhouseUnits();
	}

	public List<UnitBean> getWestendUnits() {
		return configurationManager.getWestendUnits();
	}

	public SecurityStartingPointDTO getChSecurityStart() {
		return configurationManager.getChSecurityStart();
	}

	public SecurityStartingPointDTO getWeSecurityStart() {
		return configurationManager.getWeSecurityStart();
	}

	public void updateSecurityStart(SecurityStartingPointDTO securityStartingPointDTO) {
		configurationManager.updateSecurityStart(securityStartingPointDTO);
	}

}
