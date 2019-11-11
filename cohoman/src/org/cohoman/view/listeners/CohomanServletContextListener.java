package org.cohoman.view.listeners;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.cohoman.model.business.trash.TextTeamMembers;
import org.cohoman.model.integration.persistence.dao.HibernateUtil;
import org.cohoman.model.singletons.ConfigScalarValues;

@WebListener
public class CohomanServletContextListener implements ServletContextListener {

	private ScheduledExecutorService scheduler;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		HibernateUtil.shutdown();
		scheduler.shutdownNow();
		System.out.println("CohomanServletContextListener destroyed");
	}

	// Run this before web application is started
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ConfigScalarValues.getInstance(); // load config scalar values

	    // Figure out the delay to the nearest x minutes and then 
		// we will wait that amount time between each poll.
		// Start with Current time. Roundup to nearest minute.
		Calendar calNow = Calendar.getInstance();
		System.out.println("CohomanServletContextListener time started: "
				+ calNow.getTime());
		Calendar calRounded = Calendar.getInstance();
		calRounded.add(Calendar.SECOND, 30);
		calRounded.set(Calendar.SECOND, 0);
		calRounded.set(Calendar.MILLISECOND, 0);

		// Now round up to the nearest x_minutes minutes.
		int x_minutes = 5;
		int minutes_interval = calRounded.get(Calendar.MINUTE);
		minutes_interval = minutes_interval
				+ (x_minutes - (minutes_interval % x_minutes));
		calRounded.set(Calendar.MINUTE, minutes_interval);
		System.out.println("CohomanServletContextListener calRounded: "
				+ calRounded.getTime());

		// Figure how many seconds we have to wait to start
		// our x minute wake-ups
		int secondsBeforeStart = ((int) (calRounded.getTimeInMillis() - calNow
				.getTimeInMillis()) / 1000) + 1;
		System.out.println("CohomanServletContextListener secs to wait: "
				+ secondsBeforeStart);

		// Kickoff a separate thread (effectively) that sends text
		// messages to trash team members at the appropriate time.
		// That class is executed periodically.
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new TextTeamMembers(),
				secondsBeforeStart, x_minutes * 60, TimeUnit.SECONDS);
		
		System.out.println("CohomanServletContextListener started");
	}

}
