package org.cohoman.view.controller.utils;

public class Validators {

	public static boolean isValidPhoneNumber(String phonenumber) {

		// phone numbers are optional so empty is OK
		if (phonenumber.isEmpty()) {
			return true;
		}

		String regex = "^\\d{3}-\\d{3}-\\d{4}$"; // XXX-XXX-XXXX
		return phonenumber.matches(regex);
	}

	public static boolean isValidEmailAddress(String emailaddress) {

		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		return emailaddress.matches(regex);
	}

}
