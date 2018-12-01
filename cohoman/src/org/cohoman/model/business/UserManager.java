package org.cohoman.model.business;

import java.util.List;

import org.cohoman.model.dto.UserDTO;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.view.controller.CohomanException;

public interface UserManager {
	public void createUser(UserDTO theUser)throws CohomanException;
	//public void editMealEvent(MealEvent mealEvent);
	//public void deleteMealEvent(MealEvent mealEvent);
	public User getUser(UserDTO theUser);
	public User getUser(Long userid);

	public List<Role>  getRolesForUser(Long userId);

	public List<Role>  getAvailableRolesForUser(Long userId);

	public List<User> getAllUsers();
	public List<User> getUsersHereAndAway();
	public List<User> getUsersChildren();
	public List<User> getUsersHereNow();
	public List<User> getUsersForBirthdays();

	public void editUser(User theUser);
	
	public void deleteUser(User theUser) throws Exception ;
	
	public void addRole(Long userId, Long roleId);

	public void deleteRole(Long userId, Long roleId);

}
