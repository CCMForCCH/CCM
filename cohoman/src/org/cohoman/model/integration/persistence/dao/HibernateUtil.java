package org.cohoman.model.integration.persistence.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;

import org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;

public class HibernateUtil {

	static Logger logger = Logger.getLogger("HibernateUtil.java");

	private static SessionFactory sessionFactory;
	
	static {
		try {
			sessionFactory = new Configuration().configure("/hibernate_wsh.cfg.xml").buildSessionFactory();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
		}
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static void shutdown() {
		
		SessionFactoryImpl sessionFactory = (SessionFactoryImpl) getSessionFactory();
		ConnectionProvider conn = sessionFactory.getConnectionProvider();
		if (conn instanceof C3P0ConnectionProvider) {
			((C3P0ConnectionProvider) conn).close();
			logger.info("Cohoman: just closed the C3P0ConnectionProvider.");
		}
		
		// Close caches and connection pools
		getSessionFactory().close();
		logger.info("Cohoman: just closed the Hibernate Sessionfactory.");
	}
}
