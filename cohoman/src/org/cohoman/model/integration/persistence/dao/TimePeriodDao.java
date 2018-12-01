package org.cohoman.model.integration.persistence.dao;

import java.sql.Date;
import java.util.List;

import org.cohoman.model.integration.persistence.beans.TimePeriodBean;

public interface TimePeriodDao {
	public void createTimePeriod(String timePeriodTypeEnum, Date periodStartDate,
			Date periodEndDate);
	
	public void updateTimePeriod(TimePeriodBean timePeriodBean);
	
	public List<TimePeriodBean> getTimePeriods(String timePeriodTypeEnum);

    public void deleteTimePeriod(Long timePeriodId);
    
	/*
	public void createUser(UserDTO theuser);
		
	public UserBean getUser(UserDTO theuser);
	public UserBean getUser(Long userid);

	public List<UserBean> getUsers();

	public List<Role> getRolesForUser(Long userId);

	public List<Role> getAvailableRolesForUser(Long userId);

	public void updateUser(User theUser);
	
	public void deleteUser(User theUser) throws Exception;
	
	public void addRole(Long userId, Long roleId);

	public void deleteRole(Long userId, Long roleId);
	*/

}
