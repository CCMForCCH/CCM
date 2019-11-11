package org.cohoman.model.integration.utils;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.cohoman.model.business.User;
import org.cohoman.view.controller.AuthenticateController;
import org.cohoman.view.controller.CohomanException;

public class LoggingUtils {

	public final static String INTERNAL_ERROR = "Internal Error Encountered. Report to your Administrator.";
	
	public static String displayExceptionInfo(Exception ex) {
		String info = "";

		if (ex instanceof CohomanException) {
			if (((CohomanException) ex).getErrorText() != null
					&& ((CohomanException) ex).getErrorText().length() > 0) {
				info += "  " + ((CohomanException) ex).getErrorText();
			}
		}
		if (ex.toString() != null && ex.toString().length() > 0) {
			info += "  " + ex.toString();
		}
		if (ex.getMessage() != null && ex.getMessage().length() > 0) {
			info += "  " + ex.getMessage();
		}
		if (ex.getCause() != null && ex.getCause().toString().length() > 0) {
			info += "  " + ex.getCause();
		}
		info += "  Occurred at " + ex.getStackTrace()[0].toString();
		return info;
	}
	
	public static String getCurrentUsername() {
		// get the userid of the current user to set the requester.
		FacesContext ctx = FacesContext.getCurrentInstance();
		if (ctx == null) {
			return null;
		}
		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(true);
		if (session == null) {
			return null;
		}
		User dbUser = (User) session
				.getAttribute(AuthenticateController.SESSIONVAR_USER_NAME);
		if (dbUser == null) {
			return null;
		}
		return dbUser.getUsername();
	}
}
