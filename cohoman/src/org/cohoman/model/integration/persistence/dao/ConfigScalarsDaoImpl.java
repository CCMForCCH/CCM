package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.integration.persistence.beans.ConfigScalarsBean;
import org.hibernate.Query;
import org.hibernate.Session;

public class ConfigScalarsDaoImpl implements ConfigScalarsDao {

	public String getConfigScalarValue(String scalarname) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		String queryString = "from ConfigScalarsBean where scalarname = ?";
		Query query = session.createQuery(queryString).setString(0, scalarname);
		List<ConfigScalarsBean> configScalarsBeans = query.list();
		if (configScalarsBeans.size() != 1) {
			throw new RuntimeException("Expected just one bean but got "
					+ configScalarsBeans.size() + "for scalarname " + scalarname);
		}
		ConfigScalarsBean onebean = configScalarsBeans.iterator().next();

		session.flush();
		session.close();
		return onebean.getScalarValue();
	}

}
