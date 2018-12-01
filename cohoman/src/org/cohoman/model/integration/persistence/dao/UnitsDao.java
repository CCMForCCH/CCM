package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.integration.persistence.beans.UnitBean;

public interface UnitsDao {
	
	public List<UnitBean> getAllUnits();

	public List<UnitBean> getCommonhouseUnits();

	public List<UnitBean> getWestendUnits();

	/*
	public List<TimePeriodBean> getUnitsBySection(String timePeriodTypeEnum);
*/
    
}
