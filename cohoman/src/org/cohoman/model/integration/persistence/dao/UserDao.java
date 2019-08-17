package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.business.User;
import org.cohoman.model.dto.UserDTO;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.view.controller.CohomanException;

public interface UserDao {
	public void createUser(UserDTO theuser) throws CohomanException;
		
	public UserDTO getUserForLogin(UserDTO theuser);
	public UserDTO getUser(Long userid);
	public UserDTO getUserByUsername(String username);
	

	// *******  Various flavors of User lists ****************************
	// First, general one
	// Includes OWNER, LANDLORD, and RENTING
	public List<UserDTO> getUsersHereAndAway();

	// For admin editing and listing of users
	// Includes every type
	public List<UserDTO> getAllUsers();
	
	// For Children's list
	// Includes CHILD
	public List<UserDTO> getUsersChildren();

	// For meals, security, trash
	// Includes OWNER, RENTING
	public List<UserDTO> getUsersHereNow();

	// For birthday printing
	// Includes OWNER, CHILD, LANDLORD, RENTING
	public List<UserDTO> getUsersForBirthdays();
	// *******************************************************************

	public List<Role> getRolesForUser(Long userId);

	public List<Role> getAvailableRolesForUser(Long userId);

	public void updateUser(User theUser);
	
	public void deleteUser(User theUser) throws Exception;
	
	public void addRole(Long userId, Long roleId);

	public void deleteRole(Long userId, Long roleId);
	
	public List<String> getUserLastNamesAtUnit(String unitnumber);

	public List<String> getUserFirstNamesAtUnit(String unitnumber);
	
	public List<String> getUserUsernamesAtUnit(String unitnumber);
}
