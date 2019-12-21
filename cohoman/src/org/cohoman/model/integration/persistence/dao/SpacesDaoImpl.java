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

		// 12/20/2019: hack to remove the kid's room from
		// the list of spaces w/o changing the DB
		spaceBeans.remove(2);
		
		session.flush();
		session.close();
		return spaceBeans;
	}

}
