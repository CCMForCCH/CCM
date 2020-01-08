package org.cohoman.view.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import org.cohoman.model.business.User;
import org.cohoman.model.service.UserService;

@ManagedBean
@SessionScoped
public class RetrieveBirthdayListController implements Serializable {

	private static final long serialVersionUID = 4678206276499587830L;

	private UserService userService = null;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<BirthdayListRow> getBirthdayList() {

		// Get list of users for birthday list
		List<User> fullUserList = userService.getUsersForBirthdays();

		// Create new list of rows for birthday table (i.e. date|person)
		List<BirthdayListRow> birthdayList = new ArrayList<BirthdayListRow>();

		// Sort list of people by their birthdays (ignoring year)
		Collections.sort(fullUserList, new SortByUserDate());

		// Populate the list of printable birthday rows using the
		// information from the User entry.
		for (User oneUser : fullUserList) {

			// ignore entries that have a year == 1920
			// a hacky way to ignore people who have been
			// added without knowing their birthday
			if (oneUser.getBirthyear() == 1920) {
				continue;
			}

			BirthdayListRow birthdayListRow = new BirthdayListRow();
			Calendar calBirthday = Calendar.getInstance();
			calBirthday.set(Calendar.DAY_OF_MONTH, oneUser.getBirthday());
			calBirthday.set(Calendar.MONTH, oneUser.getBirthmonth());
			if (oneUser.getBirthyear() != 0) {
				calBirthday.set(Calendar.YEAR, oneUser.getBirthyear());
			}
			SimpleDateFormat formatter;
			if (oneUser.getBirthyear() != 0) {
				formatter = new SimpleDateFormat("MMM d, yyyy");
			} else {
				formatter = new SimpleDateFormat("MMM d,  ----");
			}
			birthdayListRow.birthday = formatter.format(calBirthday.getTime());
			birthdayListRow.person = oneUser.getFirstname() + " "
					+ oneUser.getLastname();
			birthdayList.add(birthdayListRow);
		}

		return birthdayList;
	}

	class SortByUserDate implements Comparator<User> {

		public int compare(User usera, User userb) {
			if (usera.getBirthmonth() != userb.getBirthmonth()) {
				return usera.getBirthmonth() - userb.getBirthmonth();
			} else {
				return usera.getBirthday() - userb.getBirthday();
			}
		}
	}

	public class BirthdayListRow {

		private String birthday;
		private String person;

		public String getBirthday() {
			return birthday;
		}

		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}

		public String getPerson() {
			return person;
		}

		public void setPerson(String person) {
			this.person = person;
		}

	}

}
