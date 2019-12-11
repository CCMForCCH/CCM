package org.cohoman.model.integration.persistence.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.business.User;
import org.cohoman.model.dto.UserDTO;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.model.integration.persistence.beans.UnitBean;
import org.cohoman.model.integration.persistence.beans.UserBean;
import org.cohoman.model.integration.persistence.beans.UsersRoles;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDaoImpl implements UserDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void createUser(UserDTO theuser) throws CohomanException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			// Find unit number string from units table
			Query query = session.createQuery(
					"from UnitBean where unitnumber = ?").setString(0,
					theuser.getUnit());

			List<UnitBean> unitBeanList = query.list();
			if (unitBeanList == null || unitBeanList.isEmpty()) {
				logger.log(Level.SEVERE,
						"No such unit found: " + theuser.getUnit());
				throw new CohomanException("No such unit found: "
						+ theuser.getUnit());
			}
			UnitBean unitBean = null;
			for (UnitBean oneUnitBean : unitBeanList) {
				// Just one unit (???)
				unitBean = (UnitBean) session.load(UnitBean.class,
						oneUnitBean.getUnitid());
			}

			UserBean userBean = makeUserBeanFromUserDTO(theuser,
					unitBean.getUnitid());
			System.out.println("wsh: createUser: lastlogin = "
					+ theuser.getLastlogin());
			session.saveOrUpdate(userBean);

			// Give the newly added user a basicuser role to start with
			UsersRoles usersRolesRow = new UsersRoles();
			usersRolesRow.setRoleid(Role.BASICUSER_ID);
			usersRolesRow.setUserid(userBean.getUserid());
			session.saveOrUpdate(usersRolesRow);
			tx.commit();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			if (tx != null) {
				tx.rollback();
			}
			throw new CohomanException(LoggingUtils.INTERNAL_ERROR);
		} finally {
			session.flush();
			session.close();
		}
	}

	public void updateUser(User theUser) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();

			// Find unit number string from units table
			Query query = session.createQuery(
					"from UnitBean where unitnumber = ?").setString(0,
					theUser.getUnit());

			List<UnitBean> unitBeanList = query.list();
			if (unitBeanList.isEmpty()) {
				throw new CohomanException("Unit number does not exist: "
						+ theUser.getUnit());
			}

			UnitBean unitBean = null;
			for (UnitBean oneUnitBean : unitBeanList) {
				// Just one unit (???)
				unitBean = (UnitBean) session.load(UnitBean.class,
						oneUnitBean.getUnitid());
			}

			UserBean userBean = (UserBean) session.load(UserBean.class,
					theUser.getUserid());
			userBean.setCellphone(theUser.getCellphone());
			userBean.setEmail(theUser.getEmail());
			userBean.setFirstname(theUser.getFirstname());
			userBean.setHomephone(theUser.getHomephone());
			userBean.setLastlogin(theUser.getLastlogin().toString());
			userBean.setLastname(theUser.getLastname());
			userBean.setPassword(theUser.getPassword());
			userBean.setUnit(unitBean.getUnitid());
			userBean.setUsername(theUser.getUsername());
			userBean.setWorkphone(theUser.getWorkphone());
			userBean.setEmergencyinfo(theUser.getEmergencyinfo());
			userBean.setFoodrestrictions(theUser.getFoodrestrictions());
			userBean.setBirthday(theUser.getBirthday());
			userBean.setBirthmonth(theUser.getBirthmonth());
			userBean.setBirthyear(theUser.getBirthyear());
			userBean.setUsertype(theUser.getUsertype());
			userBean.setTrashrole(theUser.getTrashrole());
			userBean.setAllowtexting(theUser.isAllowtexting());

			// UserBean mergedUserBean = (UserBean)session.merge(userBean);
			session.merge(userBean);
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

	public void deleteUser(User theUser) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();

			UserBean oneUserBean = (UserBean) session.load(UserBean.class,
					theUser.getUserid());

			// Find role associations to delete
			// x Query query = session.createQuery(
			// x"from UsersRoles where userid = ?").setLong(0,
			// xtheUser.getUserid());

			// xList<UsersRoles> usersRolesList = query.list();

			// x for (UsersRoles oneUsersRoles : usersRolesList) {
			// xSystem.out.println("wsh: deleteUser: userroleid = "
			// x + oneUsersRoles.getUsersrolesid());

			// UsersRoles usersRoles = (UsersRoles) session.load(
			// UsersRoles.class, oneUsersRoles.getUsersrolesid());
			// session.delete(usersRoles);
			// x session.delete(oneUsersRoles);
			// x}
			// rely on CASCADING delete for userrole rows to be ...
			// Now delete the user entry from the table.
			session.delete(oneUserBean);

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

	private UnitBean getUnitBeanGivenUserUnitid(Session session, Long unitid) {

		// Find unit number string from units table
		Query query = session.createQuery("from UnitBean where unitid = ?")
				.setString(0, unitid.toString());

		List<UnitBean> unitBeanList = query.list();

		// TODO: if empty() throw exception??
		UnitBean unitBean = unitBeanList.iterator().next();
		return unitBean;
	}

	private List<UserDTO> getUsersCommon(String queryString) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery(queryString);
		List<UserBean> userBeans = query.list();

		List userDTOList = new ArrayList();
		for (UserBean oneUserBean : userBeans) {
			UnitBean unitBean = getUnitBeanGivenUserUnitid(session,
					oneUserBean.getUnit());
			UserDTO userDTO = new UserDTO();
			userDTO = makeUserDTOFromUserBean(oneUserBean,
					unitBean.getUnitnumber());
			userDTOList.add(userDTO);
		}

		session.flush();
		session.close();
		return userDTOList;
	}

	public List<UserDTO> getAllUsers() {
		String queryString = 
			"from UserBean order by firstname";
		return getUsersCommon(queryString);
	}

	public List<UserDTO> getUsersHereAndAway() {
		String queryString = 
			"from UserBean where usertype = 'OWNER' OR usertype = 'LANDLORD' OR usertype = 'RENTING' order by firstname";
		return getUsersCommon(queryString);		
	}

	public List<UserDTO> getUsersChildren() {
		String queryString = 
			"from UserBean where usertype = 'CHILD' order by firstname";
		return getUsersCommon(queryString);			
	}
	
	public List<UserDTO> getUsersHereNow() {
		String queryString = 
			"from UserBean where usertype = 'OWNER' OR usertype = 'RENTING' order by firstname";		
		return getUsersCommon(queryString);			
	}

	public List<UserDTO> getUsersForBirthdays() {
		String queryString = 
			"from UserBean where usertype = 'OWNER' OR usertype = 'CHILD' OR usertype = 'LANDLORD' OR usertype = 'RENTING' order by firstname";		
		return getUsersCommon(queryString);			
	}


	public List<String> getUserLastNamesAtUnit(String unitnumber) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String queryString = "SELECT users.lastname "
				+ "FROM users JOIN units " + "WHERE users.unit = units.unitid "
				+ "AND units.unitnumber = ? AND (users.usertype = 'OWNER' OR users.usertype = 'RENTING')";
		Query query = session.createSQLQuery(queryString).setString(0,
				unitnumber);

		List<String> lastNameList = query.list();

		session.flush();
		session.close();
		return lastNameList;
	}

	public List<String> getUserFirstNamesAtUnit(String unitnumber) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String queryString = "SELECT users.firstname "
				+ "FROM users JOIN units " + "WHERE users.unit = units.unitid "
				+ "AND units.unitnumber = ? AND (users.usertype = 'OWNER' OR users.usertype = 'RENTING')";
		Query query = session.createSQLQuery(queryString).setString(0,
				unitnumber);

		List<String> lastNameList = query.list();

		session.flush();
		session.close();
		return lastNameList;
	}

	public List<String> getUserUsernamesAtUnit(String unitnumber) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String queryString = "SELECT users.username "
				+ "FROM users JOIN units " + "WHERE users.unit = units.unitid "
				+ "AND units.unitnumber = ? AND (users.usertype = 'OWNER' OR users.usertype = 'RENTING')";
		Query query = session.createSQLQuery(queryString).setString(0,
				unitnumber);

		List<String> usernameList = query.list();

		session.flush();
		session.close();
		return usernameList;
	}

	public UserDTO getUserForLogin(UserDTO theUser) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		String queryString = "from UserBean " + "where USERNAME = ? " +
			"AND (usertype = '' OR usertype = 'OWNER' OR usertype = 'LANDLORD' OR usertype = 'RENTING')";
		List<UserBean> userCollection = null;
		
		try {
			Query query = session.createQuery(queryString).setString(0,
					theUser.getUsername());
			userCollection = query.list();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			return null;
		} 
			
		if (userCollection.size() != 1) {
			return null;
		}
		UserBean oneUser = userCollection.iterator().next();
		UnitBean unitBean = getUnitBeanGivenUserUnitid(session,
				oneUser.getUnit());
		UserDTO userDTO = new UserDTO();
		userDTO = makeUserDTOFromUserBean(oneUser, unitBean.getUnitnumber());

		session.flush();
		session.close();
		return userDTO;
	}

	public UserDTO getUser(Long userid) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		String queryString = "from UserBean " + "where userid = ?";
		Query query = null;
		try {
			query = session.createQuery(queryString).setLong(0, userid);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		List<UserBean> userCollection = query.list();
		if (userCollection.size() != 1) {
			return null;
		}
		UserBean oneUser = userCollection.iterator().next();
		UnitBean unitBean = getUnitBeanGivenUserUnitid(session,
				oneUser.getUnit());
		UserDTO userDTO = new UserDTO();
		userDTO = makeUserDTOFromUserBean(oneUser, unitBean.getUnitnumber());
		session.flush();
		session.close();
		return userDTO;
	}

	public UserDTO getUserByUsername(String username) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		String queryString = "from UserBean " + "where username = ?";
		Query query = null;
		try {
			query = session.createQuery(queryString).setString(0, username);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		List<UserBean> userCollection = query.list();
		if (userCollection.size() != 1) {
			return null;
		}
		UserBean oneUser = userCollection.iterator().next();
		UnitBean unitBean = getUnitBeanGivenUserUnitid(session,
				oneUser.getUnit());
		UserDTO userDTO = new UserDTO();
		userDTO = makeUserDTOFromUserBean(oneUser, unitBean.getUnitnumber());
		session.flush();
		session.close();
		return userDTO;
	}
	
	public void addRole(Long userId, Long roleId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			UsersRoles usersRolesRow = new UsersRoles();
			usersRolesRow.setRoleid(roleId);
			usersRolesRow.setUserid(userId);
			session.saveOrUpdate(usersRolesRow);
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

	public void deleteRole(Long userId, Long roleId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			// Determine the usersroles id of the entries one of
			// which is to be deleted.
			String queryString = "select usersrolesid, userid, roleid from usersroles "
					+ "where userid = ?";

			Query query = session.createSQLQuery(queryString).setString(0,
					Long.toString(userId));

			List<Object[]> usersrolesObjects = query.list();
			if (usersrolesObjects.size() == 1) {
				throw new CohomanException(
						"Trying to delete last role for user id = " + userId);
			}
			if (usersrolesObjects.size() == 0) {
				throw new CohomanException("No roles for user id = " + userId);
			}

			// Got here if OK to delete role. Now figure out
			// which entry and use usersrolesid to do so.
			long theFoundUsersRolesId = 0;
			for (Object[] anObjectArray : usersrolesObjects) {
				long theUsersRolesId = Long.parseLong(anObjectArray[0]
						.toString());
				long theUserId = Long.parseLong(anObjectArray[1].toString());
				long theRoleId = Long.parseLong(anObjectArray[2].toString());
				if (theRoleId == roleId) {
					theFoundUsersRolesId = theUsersRolesId;
					break;
				}
			}

			if (theFoundUsersRolesId == 0) {
				throw new CohomanException(
						"Cannot find the role id to delete for role id = "
								+ roleId);
			}

			UsersRoles oneUsersRoles = (UsersRoles) session.load(
					UsersRoles.class, theFoundUsersRolesId);
			session.delete(oneUsersRoles);

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

	public List<Role> getRolesForUser(Long userId) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		String queryString = "select roles.roleid, roles.rolename, roles.roledesc "
				+ "from usersroles inner join roles "
				+ "where (usersroles.roleid = roles.roleid) and "
				+ "usersroles.userid = ?";
		Query query = session.createSQLQuery(queryString).setString(0,
				Long.toString(userId));

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
		Session session = HibernateUtil.getSessionFactory().openSession();

		String queryString = "select roles.roleid, roles.rolename, roles.roledesc from ("
				+ "select * from usersroles where userid = ?) "
				+ "as u1 right outer join roles "
				+ "on u1.roleid = roles.roleid " + "where userid is NULL ";

		Query query = session.createSQLQuery(queryString).setString(0,
				Long.toString(userId));

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

	private UserBean makeUserBeanFromUserDTO(UserDTO theUserDTO, Long unitid) {
		UserBean userBean = new UserBean();
		userBean.setCellphone(theUserDTO.getCellphone());
		userBean.setEmail(theUserDTO.getEmail());
		userBean.setFirstname(theUserDTO.getFirstname());
		userBean.setHomephone(theUserDTO.getHomephone());
		userBean.setLastlogin(theUserDTO.getLastlogin().toString());
		userBean.setLastname(theUserDTO.getLastname());
		userBean.setPassword(theUserDTO.getPassword());
		userBean.setUnit(unitid);
		userBean.setUsername(theUserDTO.getUsername());
		userBean.setWorkphone(theUserDTO.getWorkphone());
		userBean.setEmergencyinfo(theUserDTO.getEmergencyinfo());
		userBean.setFoodrestrictions(theUserDTO.getFoodrestrictions());
		userBean.setBirthday(theUserDTO.getBirthday());
		userBean.setBirthmonth(theUserDTO.getBirthmonth());
		userBean.setBirthyear(theUserDTO.getBirthyear());
		userBean.setUsertype(theUserDTO.getUsertype());
		userBean.setTrashrole(theUserDTO.getTrashrole());
		userBean.setAllowtexting(theUserDTO.isAllowtexting());
		return userBean;
	}

	private UserDTO makeUserDTOFromUserBean(UserBean theUserBean,
			String unitnumber) {
		UserDTO userDTO = new UserDTO();
		try {
			userDTO.setUserid(theUserBean.getUserid());
			userDTO.setCellphone(theUserBean.getCellphone());
			userDTO.setEmail(theUserBean.getEmail());
			userDTO.setFirstname(theUserBean.getFirstname());
			userDTO.setHomephone(theUserBean.getHomephone());
			// TODO: get date as a string and convert to a real Date 3/26/16
			// userDTO.setLastlogin(DateFormat.(theUserBean.getLastlogin()));
			userDTO.setLastlogin(new Date());
			userDTO.setLastname(theUserBean.getLastname());
			userDTO.setPassword(theUserBean.getPassword());
			userDTO.setUnit(unitnumber);
			userDTO.setUsername(theUserBean.getUsername());
			userDTO.setWorkphone(theUserBean.getWorkphone());
			userDTO.setEmergencyinfo(theUserBean.getEmergencyinfo());
			userDTO.setFoodrestrictions(theUserBean.getFoodrestrictions());
			userDTO.setBirthday(theUserBean.getBirthday());
			userDTO.setBirthmonth(theUserBean.getBirthmonth());
			userDTO.setBirthyear(theUserBean.getBirthyear());
			userDTO.setUsertype(theUserBean.getUsertype());
			userDTO.setTrashrole(theUserBean.getTrashrole());
			userDTO.setAllowtexting(theUserBean.isAllowtexting());
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			return null;
		}
		return userDTO;
	}

}
