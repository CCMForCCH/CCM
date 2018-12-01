package org.cohoman.model.business;

import java.sql.Date;
import java.util.List;

import org.cohoman.model.dto.SecurityStartingPointDTO;
import org.cohoman.model.integration.persistence.beans.CchSectionTypeEnum;
import org.cohoman.model.integration.persistence.beans.TimePeriodBean;
import org.cohoman.model.integration.persistence.beans.TimePeriodTypeEnum;
import org.cohoman.model.integration.persistence.beans.UnitBean;
import org.cohoman.model.integration.persistence.dao.SecurityDao;
import org.cohoman.model.integration.persistence.dao.TimePeriodDao;
import org.cohoman.model.integration.persistence.dao.UnitsDao;

public class ConfigurationManagerImpl implements ConfigurationManager {
	
	private TimePeriodDao timePeriodDao = null;
	private UnitsDao unitsDao = null;
	private SecurityDao securityDao = null;
	
	public TimePeriodDao getTimePeriodDao() {
		return timePeriodDao;
	}

	public void setTimePeriodDao(TimePeriodDao timePeriodDao) {
		this.timePeriodDao = timePeriodDao;
	}

	public UnitsDao getUnitsDao() {
		return unitsDao;
	}

	public void setUnitsDao(UnitsDao unitsDao) {
		this.unitsDao = unitsDao;
	}

	public SecurityDao getSecurityDao() {
		return securityDao;
	}

	public void setSecurityDao(SecurityDao securityDao) {
		this.securityDao = securityDao;
	}

	public void createTimePeriod(TimePeriodTypeEnum timePeriodTypeEnum, Date periodStartDate,
			Date periodEndDate) {
		timePeriodDao.createTimePeriod(timePeriodTypeEnum.toString(), periodStartDate, periodEndDate);
	}

	public void editTimePeriod(TimePeriodBean timePeriodBean) {
		timePeriodDao.updateTimePeriod(timePeriodBean);
	}
	
	public List<TimePeriodBean> getTimePeriods(TimePeriodTypeEnum timePeriodTypeEnum){
		List<TimePeriodBean> timePeriodList = timePeriodDao.getTimePeriods(timePeriodTypeEnum.toString());
		return timePeriodList;
	}

	public void deleteTimePeriod(Long timePeriodId) {
		timePeriodDao.deleteTimePeriod(timePeriodId);
	}

	public List<UnitBean> getAllUnits() {
		return unitsDao.getAllUnits();
	}

	public List<UnitBean> getCommonhouseUnits() {
		return unitsDao.getCommonhouseUnits();
	}

	public List<UnitBean> getWestendUnits() {
		return unitsDao.getWestendUnits();
	}

	public SecurityStartingPointDTO getChSecurityStart() {
		return securityDao.getSecurityStart("COMMONHOUSE");
	}

	public SecurityStartingPointDTO getWeSecurityStart() {
		return securityDao.getSecurityStart("WESTEND");
	}

	public void updateSecurityStart(SecurityStartingPointDTO securityStartingPointDTO) {
		securityDao.updateSecurityStart(securityStartingPointDTO);
	}

}