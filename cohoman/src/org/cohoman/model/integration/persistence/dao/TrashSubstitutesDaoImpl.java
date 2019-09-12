package org.cohoman.model.integration.persistence.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.integration.persistence.beans.TrashSubstitutesBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TrashSubstitutesDaoImpl implements TrashSubstitutesDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	public void setTrashSubstitute(Date startingDate, String origUsername,
			String substituteUsername) throws CohomanException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			TrashSubstitutesBean trashSubstitutesBean = new TrashSubstitutesBean();
			trashSubstitutesBean.setStartingdate(startingDate);
			trashSubstitutesBean.setOrigusername(origUsername);
			trashSubstitutesBean.setSubstituteusername(substituteUsername);
			session.saveOrUpdate(trashSubstitutesBean);

			tx.commit();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.flush();
			session.close();
		}
	}

	public void deleteTrashSubstitute(Long substitutesId) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			TrashSubstitutesBean trashSubstitutesBean = (TrashSubstitutesBean) session
					.load(TrashSubstitutesBean.class, substitutesId);
			session.delete(trashSubstitutesBean);
			tx.commit();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.flush();
			session.close();
		}

	}

	public List<TrashSubstitutesBean> getTrashSubstitutes() {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<TrashSubstitutesBean> trashSubstitutesBeans = new ArrayList<TrashSubstitutesBean>();
		
		try {
			tx = session.beginTransaction();

			String queryString = "SELECT substitutesid, startingdate, origusername, substituteusername"
					+ " FROM trashsubstitutes";
			Query query = session.createSQLQuery(queryString);
			List<Object[]> result = query.list();
			if (result.size() > 0) {

				for (Object[] oneResult : result) {
					TrashSubstitutesBean oneTrashSubstitutesBean = new TrashSubstitutesBean();

					oneTrashSubstitutesBean
							.setSubstitutesid(((BigInteger) oneResult[0])
									.longValue());
					oneTrashSubstitutesBean
							.setStartingdate((Date) oneResult[1]);
					oneTrashSubstitutesBean
							.setOrigusername((String) oneResult[2]);
					oneTrashSubstitutesBean
							.setSubstituteusername((String) oneResult[3]);
					trashSubstitutesBeans.add(oneTrashSubstitutesBean);
				}
			} else {
				tx.commit();
				return null; // TODO
			}

			tx.commit();
			return trashSubstitutesBeans;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			if (tx != null) {
				tx.rollback();
			}
			return null;
		} finally {
			session.flush();
			session.close();
		}
	}

	public TrashSubstitutesBean getTrashSubstitute(String startingDate,
			String origUsername) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		List<TrashSubstitutesBean> trashSubstitutesBeans = new ArrayList<TrashSubstitutesBean>();
		Date startingDateDate = null;

		try {
			startingDateDate = new SimpleDateFormat("MMM d, yyyy")
					.parse(startingDate);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			throw new RuntimeException("Unable to parse date string " + startingDate);
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String startingDateString = formatter.format(startingDateDate);

		String queryString = "from TrashSubstitutesBean where date(startingdate) = :startingdatestring and origusername = :username";
		Query query = session.createQuery(queryString)
				.setString("startingdatestring", startingDateString)
				.setString("username", origUsername);

		trashSubstitutesBeans = query.list();

		TrashSubstitutesBean onebean = null;
		if (trashSubstitutesBeans.size() == 1) {
			onebean = trashSubstitutesBeans.iterator().next();
		}

		session.flush();
		session.close();
		return onebean;

	}

}
