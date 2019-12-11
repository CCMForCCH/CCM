package org.cohoman.model.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cohoman.model.dto.UserDTO;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.model.integration.persistence.beans.UserBean;
import org.cohoman.model.integration.persistence.dao.UserDao;
import org.cohoman.view.controller.CohomanException;

public class UserManagerImpl implements UserManager {
	
	private UserDao userDao = null;
	

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void createUser(UserDTO theUser) throws CohomanException{
		userDao.createUser(theUser);
	}

	// pass in DTO with only user/pw; return filled
	public User getUser(UserDTO theUser) {
		UserDTO userDTO = userDao.getUserForLogin(theUser);
		if (userDTO == null) {
			return null;
		}
		return makeUserFromUserBean(userDTO);
	}

	public User getUser(Long userid) {
		UserDTO userDTO = userDao.getUser(userid);
		if (userDTO == null) {
			return null;
		}
		return makeUserFromUserBean(userDTO);
	}

	public User getUserByUsername(String username) {
		UserDTO userDTO = userDao.getUserByUsername(username);
		if (userDTO == null) {
			return null;
		}
		return makeUserFromUserBean(userDTO);
	}

	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		List<UserDTO> userDTOs = userDao.getAllUsers();
		for (UserDTO userDTO : userDTOs) {
			users.add(makeUserFromUserBean(userDTO));
		}
		return users;
	}
	
	public List<User> getUsersHereAndAway() {
		List<User> users = new ArrayList<User>();
		List<UserDTO> userDTOs = userDao.getUsersHereAndAway();
		for (UserDTO userDTO : userDTOs) {
			users.add(makeUserFromUserBean(userDTO));
		}
		return users;		
	}
	
	public List<User> getUsersChildren() {
		List<User> users = new ArrayList<User>();
		List<UserDTO> userDTOs = userDao.getUsersChildren();
		for (UserDTO userDTO : userDTOs) {
			users.add(makeUserFromUserBean(userDTO));
		}
		return users;
	}
	
	public List<User> getUsersHereNow() {
		List<User> users = new ArrayList<User>();
		List<UserDTO> userDTOs = userDao.getUsersHereNow();
		for (UserDTO userDTO : userDTOs) {
			users.add(makeUserFromUserBean(userDTO));
		}
		return users;		
	}
	public List<User> getUsersForBirthdays() {
		List<User> users = new ArrayList<User>();
		List<UserDTO> userDTOs = userDao.getUsersForBirthdays();
		for (UserDTO userDTO : userDTOs) {
			users.add(makeUserFromUserBean(userDTO));
		}
		return users;				
	}


	private User makeUserFromUserBean(UserDTO userDTO) {
		User oneUser = new User();
		oneUser.setUserid(userDTO.getUserid());
		oneUser.setCellphone(userDTO.getCellphone());
		oneUser.setEmail(userDTO.getEmail());
		oneUser.setFirstname(userDTO.getFirstname());
		oneUser.setHomephone(userDTO.getHomephone());
		oneUser.setLastlogin(userDTO.getLastlogin());
		oneUser.setLastname(userDTO.getLastname());
		oneUser.setPassword(userDTO.getPassword());
		oneUser.setUnit(userDTO.getUnit());
		oneUser.setUsername(userDTO.getUsername());
		oneUser.setWorkphone(userDTO.getWorkphone());
		oneUser.setEmergencyinfo(userDTO.getEmergencyinfo());
		oneUser.setFoodrestrictions(userDTO.getFoodrestrictions());
		oneUser.setBirthday(userDTO.getBirthday());
		oneUser.setBirthmonth(userDTO.getBirthmonth());
		oneUser.setBirthyear(userDTO.getBirthyear());
		oneUser.setUsertype(userDTO.getUsertype());
		oneUser.setTrashrole(userDTO.getTrashrole());
		oneUser.setAllowtexting(userDTO.isAllowtexting());
		return oneUser;
	}
	
	public List<Role> getRolesForUser(Long userId) {
		return userDao.getRolesForUser(userId);
	}

	public List<Role>  getAvailableRolesForUser(Long userId) {
		return userDao.getAvailableRolesForUser(userId);
	}

	public void editUser(User theUser) {
		userDao.updateUser(theUser);
	}

	public void deleteUser(User theUser) throws Exception {
		userDao.deleteUser(theUser);
	}

	public void addRole(Long userId, Long roleId) {
		userDao.addRole(userId, roleId);
	}

	public void deleteRole(Long userId, Long roleId) {
		userDao.deleteRole(userId, roleId);
	}

}
