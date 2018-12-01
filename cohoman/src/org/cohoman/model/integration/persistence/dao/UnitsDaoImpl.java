package org.cohoman.model.integration.persistence.dao;

import java.util.List;

import org.cohoman.model.integration.persistence.beans.UnitBean;
import org.hibernate.Query;
import org.hibernate.Session;

public class UnitsDaoImpl implements UnitsDao {

	public List<UnitBean> getAllUnits() {

		Session session = HibernateUtil.getSessionFactory().openSession();
		String queryString = "from UnitBean order by unitnumber";
		Query query = session.createQuery(queryString);
		List<UnitBean> unitBeans = query.list();

		session.flush();
		session.close();
		return unitBeans;
	}

	public List<UnitBean> getCommonhouseUnits() {

		Session session = HibernateUtil.getSessionFactory().openSession();
		String queryString = "from UnitBean where section = 'COMMONHOUSE' order by unitnumber";
		Query query = session.createQuery(queryString);
		List<UnitBean> unitBeans = query.list();

		session.flush();
		session.close();
		return unitBeans;
	}

	public List<UnitBean> getWestendUnits() {

		Session session = HibernateUtil.getSessionFactory().openSession();
		String queryString = "from UnitBean where section = 'WESTEND' order by unitnumber";
		Query query = session.createQuery(queryString);
		List<UnitBean> unitBeans = query.list();

		session.flush();
		session.close();
		return unitBeans;
	}

}
