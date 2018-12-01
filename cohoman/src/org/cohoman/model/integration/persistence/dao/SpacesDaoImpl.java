package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.integration.persistence.beans.SpaceBean;
import org.hibernate.Query;
import org.hibernate.Session;

public class SpacesDaoImpl implements SpacesDao {

	public List<SpaceBean> getAllSpaces() {

		Session session = HibernateUtil.getSessionFactory().openSession();
		String queryString = "from SpaceBean";
		Query query = session.createQuery(queryString);
		List<SpaceBean> spaceBeans = query.list();

		session.flush();
		session.close();
		return spaceBeans;
	}

}
