package org.cohoman.model.business;

import java.sql.Date;
import java.util.List;

import org.cohoman.model.dto.SecurityStartingPointDTO;
import org.cohoman.model.integration.persistence.beans.TimePeriodBean;
import org.cohoman.model.integration.persistence.beans.TimePeriodTypeEnum;
import org.cohoman.model.integration.persistence.beans.UnitBean;

public interface ConfigurationManager {
	
	public void createTimePeriod(TimePeriodTypeEnum timePeriodTypeEnum, Date periodStartDate,
			Date periodEndDate);
	public void editTimePeriod(TimePeriodBean timePeriodBean);
	public void deleteTimePeriod(Long timePeriodId);
	public List<TimePeriodBean> getTimePeriods(TimePeriodTypeEnum timePeriodTypeEnum);

	public List<UnitBean> getAllUnits();
	public List<UnitBean> getCommonhouseUnits();
	public List<UnitBean> getWestendUnits();
	
	public SecurityStartingPointDTO getChSecurityStart();
	public SecurityStartingPointDTO getWeSecurityStart();
	public void updateSecurityStart(SecurityStartingPointDTO securityStartingPointDTO);

}
