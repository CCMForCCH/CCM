package org.cohoman.view.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import org.cohoman.model.business.User;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.model.service.UserService;

@ManagedBean
@SessionScoped
public class DeleteRoleController implements Serializable {

	private static final long serialVersionUID = 4678206276499587830L;

	private List<Role> rolesList;
	private String chosenUserString;
	private Role chosenRole;
	private String chosenRoleString;
	private UserService userService = null;
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getChosenUserString() {
		return chosenUserString;
	}

	public void setChosenUserString(String chosenUserString) {
		this.chosenUserString = chosenUserString;
	}

	public String getChosenRoleString() {
		return chosenRoleString;
	}

	public void setChosenRoleString(String chosenRoleString) {
		this.chosenRoleString = chosenRoleString;
	}

	/*
	 * Only want to show users who have already have at 
	 * least 2 roles already. And for those users, want to
	 * show the current roles.
	 */
	public List<User> getUserList() {
		List<User> fullUserList = userService.getUsersHereAndAway();
		// First time thru, set user to first of list as that's what's displayed.
		if (chosenUserString == null) {
			chosenUserString = fullUserList.get(0).getUserid().toString();
		}
		
		// Filtered the list by removing users who have only one role.
		List<User> filteredUserList = new ArrayList<User>();
		for (User oneUser : fullUserList) {
			List<Role> currentRoles = 
				userService.getRolesForUser(oneUser.getUserid());
			if (currentRoles.size() > 1) {
				filteredUserList.add(oneUser);
			}
		}
		return filteredUserList;
	}

	public List<Role> getRolesList() {
		Long userId = Long.valueOf(chosenUserString);
		rolesList = userService.getRolesForUser(userId);
		return rolesList;
	}

	public void setRolesList(List<Role> rolesList) {
		this.rolesList = rolesList;
	}

	public Role getChosenRole() {
		return chosenRole;
	}

	public void setChosenRole(Role chosenRole) {
		this.chosenRole = chosenRole;
	}
	
	public String deleteRoleView() throws Exception {
		Long userId = Long.valueOf(chosenUserString);
		Long roleId = Long.valueOf(chosenRoleString);
		userService.deleteRole(userId, roleId);
		chosenUserString = null;
		return "gohome";
	}

}
