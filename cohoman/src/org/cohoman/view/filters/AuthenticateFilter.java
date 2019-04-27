package org.cohoman.view.filters;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cohoman.model.business.User;
import org.cohoman.view.controller.AuthenticateController;

public class AuthenticateFilter implements Filter {

	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		
		// Added to stop PWC4011: Unable to set request character encoding to UTF-8
		request.setCharacterEncoding("UTF-8");
		
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;

		HttpSession session = servletRequest.getSession(true);
		String servletPath = servletRequest.getServletPath();

		User currentUser = (User) session
				.getAttribute(AuthenticateController.SESSIONVAR_USER_NAME);
		String targetUrl = (String) session
				.getAttribute(AuthenticateController.SESSIONVAR_TARGET_URL);
		String kioskAttribute = (String) session
				.getAttribute(AuthenticateController.SESSIONVAR_KIOSK_KEY);
		String kioskKeyParameter = request
				.getParameter(AuthenticateController.SESSIONVAR_KIOSK_KEY);

		// First and typical case: have we already figured out whether
		// this is on the kiosk because we have a value in the current
		// session?
		// If we never set the kiosk attribute in the current session,
		// do that now. The attribute will exist in the current session
		// if either a parameter was read from the URL or if we have a
		// cookie indicating that this is the kiosk. Otherwise, no
		// attribute => not the kiosk.
		if (kioskAttribute == null) {

			// Log the information about any Kiosk cookie encountered
			Cookie cookies[] = servletRequest.getCookies();
			if (cookies != null && cookies.length > 0) {

				String cookieLine = "";
				for (int idx = 0; idx < cookies.length; idx++) {
					if (cookies[idx].getName().equalsIgnoreCase(
							AuthenticateController.SESSIONVAR_KIOSK_KEY)) {
						cookieLine += cookies[idx].getName() + "="
								+ cookies[idx].getValue() + ", ";
						logger.log(Level.INFO, "DEBUG: kiosk cookie: "
								+ cookieLine);
						break;
					}
				}
			}

			// No. nothing in the current session. OK, then next thing
			// to do is see if we were passed a Kiosk parameter in the
			// URL. If so, then save it away in a persistent cookie.
			if (kioskKeyParameter != null) {
				// Got a kiosk parameter. So, create the cookie and
				// save a value in the current session. But first make
				// sure the parameter is "correct".
				if (kioskKeyParameter
						.equalsIgnoreCase(AuthenticateController.SESSIONVAR_KIOSK_VALUE)
						&& servletRequest.getRemoteAddr().equals(
								"64.112.178.26")) {
					logger.log(Level.INFO, "DEBUG: remote address (client) = "
							+ servletRequest.getRemoteAddr());

					// OK, create the permanent cookie and save it in the
					// current session as well

					Cookie cookie = new Cookie(
							AuthenticateController.SESSIONVAR_KIOSK_KEY,
							AuthenticateController.SESSIONVAR_KIOSK_VALUE);
					cookie.setMaxAge(60 * 60 * 24 * 365);
					cookie.setPath("/");
					cookie.setSecure(true); // only valid over https
					cookie.setHttpOnly(true);
					servletResponse.addCookie(cookie);
					session.setAttribute(
							AuthenticateController.SESSIONVAR_KIOSK_KEY,
							AuthenticateController.SESSIONVAR_KIOSK_VALUE);
				} else {
					// Invalid key given for the cookie. Ignore it. Not Kiosk.
				}
			} else {
				// No parameter. Well, check the returned cookies to see if we
				// have one for the Kiosk.
				cookies = servletRequest.getCookies();
				if (cookies != null && cookies.length > 0) {
					for (int idx = 0; idx < cookies.length; idx++) {
						if (cookies[idx].getName().equalsIgnoreCase(
								AuthenticateController.SESSIONVAR_KIOSK_KEY)
								&& cookies[idx]
										.getValue()
										.equalsIgnoreCase(
												AuthenticateController.SESSIONVAR_KIOSK_VALUE)) {
							// Found our Kiosk cookie. Set that in the session.
							session.setAttribute(
									AuthenticateController.SESSIONVAR_KIOSK_KEY,
									AuthenticateController.SESSIONVAR_KIOSK_VALUE);
							break;
						}
					}
				}
			}
		}

		servletResponse.setHeader("Cache-Control",
				"no-cache, no-store, must-revalidate"); // HTTP 1.1.
		servletResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		servletResponse.setDateHeader("Expires", 0); // Proxies.

		// TODO: fix this
		if (servletPath.endsWith(".css")) {
			filterChain.doFilter(request, response);
			return;
		}

		if (servletPath.equals(AuthenticateController.LOGIN_PATH)) {
			if (currentUser != null && targetUrl != null) {
				// We're here because we just logged-in and we have the URL
				// of a page we now need to redirect to.
				session.setAttribute(
						AuthenticateController.SESSIONVAR_TARGET_URL, null);
				logger.log(Level.INFO,
						"DEBUG: User " + currentUser.getUsername()
								+ " just logged in and is "
								+ " redirecting to " + targetUrl + ".");

				servletResponse.sendRedirect(targetUrl);
				return;
			}

			// if (currentUser != null && targetUrl == null) {
			// Logged-in and must have hit back to get here. Therefore
			// force the user back to the index page
			// servletResponse.sendRedirect(servletRequest.getContextPath()
			// + AuthenticateController.LOGIN_PATH);
			// return;
			// }

			// Not-logged-in case: allow access to the page.
			filterChain.doFilter(request, response);
			return;
		}

		if (currentUser == null) {
			// session.invalidate(); ???????
			session = servletRequest.getSession(true);
			// if (targetUrl == null) {
			// Only get the original URL once per session ????
			// String requestURI = servletRequest.getRequestURI();
			//logger.log(Level.INFO, "DEBUG: saving targetUrl of "
					//+ servletRequest.getRequestURI());
			session.setAttribute(AuthenticateController.SESSIONVAR_TARGET_URL,
					servletRequest.getRequestURI());
			// }
			String loginURL = servletRequest.getContextPath()
					+ AuthenticateController.LOGIN_PATH;
			servletResponse.sendRedirect(servletResponse
					.encodeRedirectURL(loginURL));
			return;
		} else {
			filterChain.doFilter(request, response);
			return;
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
