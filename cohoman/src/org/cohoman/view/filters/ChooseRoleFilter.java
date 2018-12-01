package org.cohoman.view.filters;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cohoman.model.integration.persistence.beans.Role;
import org.cohoman.view.controller.AuthenticateController;

public class ChooseRoleFilter implements Filter {


	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		
		HttpSession session = servletRequest.getSession(false);
		String servletPath = servletRequest.getServletPath();
				
		servletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		servletResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		servletResponse.setDateHeader("Expires", 0); // Proxies.

		//TODO: fix this
		if (servletPath.endsWith(".css") || servletPath.endsWith(AuthenticateController.CHOOSEROLE_PATH)) {
			// allow any css pages and the chooserole page to pass thru
			filterChain.doFilter(request, response);
			return;			
		}
		
		Collection<Role> roles = (Collection<Role>)session.getAttribute(AuthenticateController.SESSIONVAR_ROLES);
		Role chosenRole = (Role)session.getAttribute(AuthenticateController.SESSIONVAR_CHOSEN_ROLE);
		if (roles == null || roles.size() == 0) {
			// No roles even set yet; just continue.
			filterChain.doFilter(request, response);
			return;				
		}
		
		// TODO: check for error case of zero roles.
		
		// Roles have been read in.
		// Check for role already established.
		if (chosenRole != null) {
			// Role already set; just continue.
			filterChain.doFilter(request, response);
			return;	
		}
		
		// Role not established. Can establish if only one role.			
		if (roles.size() == 1) {
			// Only one role. Set it in session and exit filter.
			session.setAttribute(AuthenticateController.SESSIONVAR_CHOSEN_ROLE, 
					roles.iterator().next());
			filterChain.doFilter(request, response);
			return;	
		}
		
		// TODO fix so that css page isn't lost after logout + login
		// Need to forward to a page to have the user choose a role.
		//String chooseRoleUrl = "/faces/chooseRole.xhtml";
		//String chooseRoleUrl = "chooseRole.faces";
		//RequestDispatcher dispatcher =
			//request.getRequestDispatcher(chooseRoleUrl);
		//dispatcher.forward(request, response);
		//servletResponse.sendRedirect(chooseRoleUrl);
		String chooseRoleURL = servletRequest.getContextPath() + 
			AuthenticateController.CHOOSEROLE_PATH;
		servletResponse.sendRedirect(servletResponse.encodeRedirectURL(chooseRoleURL));

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
