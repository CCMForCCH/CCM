package org.cohoman.view.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import org.cohoman.model.business.User;
import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.model.integration.persistence.beans.UnitBean;
import org.cohoman.model.service.UserService;

@ManagedBean
@SessionScoped
public class RetrieveUserListController implements Serializable {

	private static final long serialVersionUID = 4678206276499587830L;

	private List<User> userList;
	private UserService userService = null;
	private List<Role> roleList;
	private List<UnitBean> unitBeanList;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<User> getUsersChildrenList() {
		List<User> updatedUserList = new ArrayList<User>();
		userList = userService.getUsersChildren();
		for (User oneUser : userList) {
			oneUser.setHomephone(age(oneUser));
			oneUser.setWorkphone(printableBirthday(oneUser));
			updatedUserList.add(oneUser);
		}
		return updatedUserList;
	}

	public List<User> getCurrentUserList() {
		userList = userService.getUsersHereAndAway();
		return userList;
	}

	public List<User> getAllUserList() {
		userList = userService.getAllUsers();
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public List<UnitBean> getUnitBeanList() {
		return unitBeanList;
	}

	public void setUnitBeanList(List<UnitBean> unitBeanList) {
		this.unitBeanList = unitBeanList;
	}

	private String age(User aUser) {
		Calendar now = new GregorianCalendar();
		Date dateNow = now.getTime();
		Calendar birthcal = new GregorianCalendar(aUser.getBirthyear(),
				aUser.getBirthmonth(), aUser.getBirthday());
		Date birthday = birthcal.getTime();
		DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		int d1 = Integer.parseInt(formatter.format(birthday));
		int d2 = Integer.parseInt(formatter.format(dateNow));
		int age = (d2 - d1) / 10000;
		if (age == 0) {
			return "<1";
		} else {
			return Integer.toString(age);
		}
	}

	private String printableBirthday(User aUser) {
		Calendar birthcal = new GregorianCalendar(aUser.getBirthyear(),
				aUser.getBirthmonth(), aUser.getBirthday());
		DateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
		return formatter.format(birthcal.getTime());
	}

}
