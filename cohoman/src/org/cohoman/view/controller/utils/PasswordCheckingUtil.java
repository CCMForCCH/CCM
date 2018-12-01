package org.cohoman.view.controller.utils;

import java.util.Arrays;
import java.util.List;

public class PasswordCheckingUtil {

	private static final String[] badPasswords = { "password", "123456", "qwerty", "pass" };

	public static boolean isPasswordGood(String chosenPassword) {

		if (!isPasswordStrongEnough(chosenPassword)) {
			return false;
		}
		if (!isPasswordNotBad(chosenPassword)) {
			return false;
		}
		return true;
	}

	private static boolean isPasswordStrongEnough(String chosenPassword) {

		// Length must be 8 or more characters
		if (chosenPassword.length() < 8) {
			return false;
		}

		// Must contain a letter, number, and special character
		if (!isStrong(chosenPassword)) {
			return false;
		}
		return true;
	}

	private static boolean isStrong(String password) {
		// return password
		// .matches("^(?=.*[A-Z])(?=.*[!,@#$%^&*?_~])(?=.*[0-9])(?=.*[a-z])");
		int categoryCount = 0;
		for (int idx = 0; idx < password.length(); idx++) {
			char x = password.charAt(idx);
			if (Character.isUpperCase(x)) {
				categoryCount++;
			} else if (Character.isLowerCase(x)) {
				categoryCount++;				
			} else if (Character.isDigit(x)) {
				categoryCount++;
			} else if ("!@#$%^&*()_+-={}[]|\\:\";\'<>?".indexOf(x) != -1) {
				categoryCount++;
			}
		}

		if (categoryCount >= 3) {
			return true;
		} else {
			return false;
		}

	}

	private static boolean isPasswordNotBad(String chosenPassword) {
		List<String> badPasswordList = Arrays.asList(badPasswords);

		for (String onePassword : badPasswordList) {
			if (chosenPassword.contains(onePassword)) {
				return false; // bad
			}
		}
		return true; // Not bad
	}
}
