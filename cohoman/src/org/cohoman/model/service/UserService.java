package org.cohoman.model.service;

import java.util.List;

import org.cohoman.model.business.User;
import org.cohoman.model.dto.UserDTO;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.view.controller.CohomanException;

public interface UserService {
	public void createUser(UserDTO theuser) throws CohomanException ;
	
	public User authenticateUser(UserDTO theUser) throws Exception;
	
	public List<Role> getRolesForUser(Long userId);
	
	public List<Role> getAvailableRolesForUser(Long userId);
	
	public List<User> getAllUsers();
	public List<User> getUsersHereAndAway();
	public List<User> getUsersChildren();
	public List<User> getUsersHereNow();
	public List<User> getUsersForBirthdays();

	public User getUser(Long userId);

	public void editUser(User theUser) throws Exception;
	
	public void deleteUser(User theUser) throws Exception;
	
	public void addRole(Long userId, Long roleId);

	public void deleteRole(Long userId, Long roleId);
	
}
