package org.cohoman.model.integration.persistence.dao;

import java.sql.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.integration.persistence.beans.TimePeriodBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TimePeriodDaoImpl implements TimePeriodDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	public void createTimePeriod(String timePeriodTypeEnum, Date periodStartDate,
			Date periodEndDate) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			TimePeriodBean timePeriodBean = new TimePeriodBean();
			timePeriodBean.setTimeperiodtypeenum(timePeriodTypeEnum);
			timePeriodBean.setPeriodstartdate(periodStartDate);
			timePeriodBean.setPeriodenddate(periodEndDate);
			session.saveOrUpdate(timePeriodBean);
			
			tx.commit();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.flush();
			session.close();
		}
	}


	public void updateTimePeriod(TimePeriodBean timePeriodBean) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			
			TimePeriodBean newTimePeriodBean = 
				(TimePeriodBean) session.load(
						TimePeriodBean.class, timePeriodBean.getTimeperiodid());
			newTimePeriodBean.setTimeperiodtypeenum(timePeriodBean.getTimeperiodtypeenum());
			newTimePeriodBean.setPeriodstartdate(timePeriodBean.getPeriodstartdate());
			newTimePeriodBean.setPeriodenddate(timePeriodBean.getPeriodenddate());

			session.merge(newTimePeriodBean);
			tx.commit();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.flush();
			session.close();
		}
	}

	public void deleteTimePeriod(Long timePeriodId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			
			TimePeriodBean timePeriodBean = 
				(TimePeriodBean) 
				session.load(TimePeriodBean.class, timePeriodId);
			session.delete(timePeriodBean);
				
			tx.commit();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.flush();
			session.close();
		}
		
	}

	/*

	public void deleteUser(User theUser) throws Exception {
		Session session = sessionFactory.openSession();
		
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();

			// Find role associations to delete
			Query query = session.createQuery("from UsersRoles where userid = ?")
				.setLong(0, theUser.getUserid());
			
			List<UsersRoles> usersRolesList = query.list();

			for (UsersRoles oneUsersRoles : usersRolesList) {
				System.out.println("wsh: deleteUser: userroleid = " +
						oneUsersRoles.getUsersrolesid());
				
				UsersRoles usersRoles = (UsersRoles) session.load(
						UsersRoles.class, oneUsersRoles.getUsersrolesid());
				session.delete(usersRoles);
			}
			
			// Now delete the user entry from the table.
			UserBean oneUserBean = (UserBean) session.load(
					UserBean.class, theUser.getUserid());
			session.delete(oneUserBean);
				
			tx.commit();
		} catch (Exception ex) {
			if (tx != null) {
				tx.rollback();
				System.out.println("wsh: deleteUser: exception = "
						+ ex.getMessage());
				System.out.println("wsh: deleteUser: exception = "
						+ ex.toString());
				System.out.println("wsh: deleteUser: exception = "
						+ ex.getCause());
				throw ex;
			}
		} finally {
			session.close();
		}
		
	}

	*/

	public List<TimePeriodBean> getTimePeriods(String timePeriodTypeEnum) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = null;
		
		List<TimePeriodBean> timePeriodBeans = null;
		try {
			tx = session.beginTransaction();

			String queryString = "from TimePeriodBean where timeperiodtypeenum = ? order by periodstartdate";
			Query query = session.createQuery(queryString)
				.setString(0, timePeriodTypeEnum);
			timePeriodBeans = query.list();

			tx.commit();
			return timePeriodBeans;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.flush();
			session.close();
		}
	}
	
	/*
	public UserBean getUser(UserDTO theUser) {
		Session session = sessionFactory.openSession();
		String queryString = "from UserBean " + "where USERNAME = ? and PASSWORD = ?";
		Query query = session.createQuery(queryString)
			.setString(0, theUser.getUsername())
			.setString(1,theUser.getPassword());
		List<UserBean> userCollection = query.list();
		if (userCollection.size() != 1) {
			return null;
		}
		UserBean oneUser = userCollection.iterator().next();
		session.flush();
		session.close();
		return oneUser;
	}
	
	public UserBean getUser(Long userid) {
		Session session = sessionFactory.openSession();
		String queryString = "from UserBean " + "where userid = ?";
		Query query = session.createQuery(queryString).setLong(0, userid);
			
		List<UserBean> userCollection = query.list();
		if (userCollection.size() != 1) {
			return null;
		}
		UserBean oneUser = userCollection.iterator().next();
		session.flush();
		session.close();
		return oneUser;
	}



	public List<Role> getRolesForUser(Long userId) {
		
		Session session = sessionFactory.openSession();
		
		String queryString = "select Roles.roleid, Roles.rolename, Roles.roledesc " +
		"from UsersRoles inner join Roles " +
		"where (UsersRoles.roleid = Roles.roleid) and " +
		"UsersRoles.userid = ?";
		Query query = session.createSQLQuery(queryString)
			.setString(0, Long.toString(userId));

		List<Object[]> rolesObjects = query.list();
		List<Role> rolesCollection = new ArrayList<Role>();
		for (Object[] anObjectArray : rolesObjects) {
			long theRoleId = Long.parseLong(anObjectArray[0].toString());
			String theRoleName = anObjectArray[1].toString();
			String theRoleDesc = anObjectArray[2].toString();
			Role oneRole = new Role();
			oneRole.setRoleid(theRoleId);
			oneRole.setRolename(theRoleName);
			oneRole.setRoledesc(theRoleDesc);
			rolesCollection.add(oneRole);
		}
		
		session.flush();
		session.close();

		return rolesCollection;
	}

	public List<Role> getAvailableRolesForUser(Long userId) {
		Session session = sessionFactory.openSession();
		
		String queryString = "select Roles.roleid, Roles.rolename, Roles.roledesc from (" +
		"select * from UsersRoles where userid = ?) " +
		"as u1 right outer join Roles " +
		"on u1.roleid = Roles.roleid " +
		"where userid is NULL ";

		Query query = session.createSQLQuery(queryString)
			.setString(0, Long.toString(userId));

		List<Object[]> rolesObjects = query.list();
		List<Role> rolesCollection = new ArrayList<Role>();
		for (Object[] anObjectArray : rolesObjects) {
			long theRoleId = Long.parseLong(anObjectArray[0].toString());
			String theRoleName = anObjectArray[1].toString();
			String theRoleDesc = anObjectArray[2].toString();
			Role oneRole = new Role();
			oneRole.setRoleid(theRoleId);
			oneRole.setRolename(theRoleName);
			oneRole.setRoledesc(theRoleDesc);
			rolesCollection.add(oneRole);
		}
		
		session.flush();
		session.close();

		return rolesCollection;
		
	}

	private UserBean makeUserBeanFromUserDTO(UserDTO theUserDTO) {
		UserBean userBean = new UserBean();
		userBean.setCellphone(theUserDTO.getCellphone());
		userBean.setEmail(theUserDTO.getEmail());
		userBean.setFirstname(theUserDTO.getFirstname());
		userBean.setHomephone(theUserDTO.getHomephone());
		userBean.setLastlogin(theUserDTO.getLastlogin());
		userBean.setLastname(theUserDTO.getLastname());
		userBean.setPassword(theUserDTO.getPassword());
		userBean.setUnit(theUserDTO.getUnit());
		userBean.setUsername(theUserDTO.getUsername());
		userBean.setWorkphone(theUserDTO.getWorkphone());
		return userBean;
	}
	*/
	
}
