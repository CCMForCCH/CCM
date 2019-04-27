package org.cohoman.view.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.cohoman.model.integration.persistence.dao.HibernateUtil;
import org.cohoman.model.singletons.ConfigScalarValues;

public class CohomanServletContextListener implements ServletContextListener {

		@Override
		public void contextDestroyed(ServletContextEvent arg0) {
			HibernateUtil.shutdown();
			System.out.println("CohomanServletContextListener destroyed");
		}

	        //Run this before web application is started
		@Override
		public void contextInitialized(ServletContextEvent arg0) {
			ConfigScalarValues.getInstance(); // load config scalar values
			System.out.println("CohomanServletContextListener started");
		}
	
}
