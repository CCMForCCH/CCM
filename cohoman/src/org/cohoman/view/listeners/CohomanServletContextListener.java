package org.cohoman.view.listeners;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.cohoman.model.business.trash.TextTeamMembers;
import org.cohoman.model.integration.persistence.dao.HibernateUtil;
import org.cohoman.model.singletons.ConfigScalarValues;

public class CohomanServletContextListener implements ServletContextListener {

	private ScheduledExecutorService scheduler;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		HibernateUtil.shutdown();
		System.out.println("CohomanServletContextListener destroyed");
	}

	// Run this before web application is started
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ConfigScalarValues.getInstance(); // load config scalar values

	    
		// Kickoff a separate thread (effectively) that sends text
		// messages to trash team members at the appropriate time. 
		// That class is executed periodically.
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new TextTeamMembers(), 0, 3, TimeUnit.MINUTES);
		
		System.out.println("CohomanServletContextListener started");
	}

}
