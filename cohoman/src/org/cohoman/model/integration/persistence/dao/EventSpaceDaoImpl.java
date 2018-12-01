package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.integration.persistence.beans.EventSpace;
import org.hibernate.Query;
import org.hibernate.Session;

public class EventSpaceDaoImpl implements EventSpaceDao {

	public List<EventSpace> getEventSpaces(Long eventid, String eventtype) {

		Session session = HibernateUtil.getSessionFactory().openSession();
//		String queryString = "from EventSpace where eventid = ? AND eventtype = ?";
		String queryString = "from EventSpace where eventid = ?";
//		Query query = session.createQuery(queryString).setLong(0, eventid).setString(1, eventtype);
		Query query = session.createQuery(queryString).setLong(0, eventid);
		List<EventSpace> eventspaces = query.list();

		session.flush();
		session.close();
		return eventspaces;
	}

}
