package org.cohoman.model.integration.persistence.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.integration.persistence.beans.SubstitutesBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SubstitutesDaoImpl implements SubstitutesDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	public void setSubstitute(Date startingDate, Long userid) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			SubstitutesBean substitutesBean = new SubstitutesBean();
			substitutesBean.setStartingdate(startingDate);
			substitutesBean.setUserid(userid);
			session.saveOrUpdate(substitutesBean);

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

	/*
	 * public void updateTimePeriod(TimePeriodBean timePeriodBean) { Session
	 * session = HibernateUtil.getSessionFactory().openSession();
	 * 
	 * Transaction tx = null;
	 * 
	 * try { tx = session.beginTransaction();
	 * 
	 * TimePeriodBean newTimePeriodBean = (TimePeriodBean) session.load(
	 * TimePeriodBean.class, timePeriodBean.getTimeperiodid());
	 * newTimePeriodBean
	 * .setTimeperiodtypeenum(timePeriodBean.getTimeperiodtypeenum());
	 * newTimePeriodBean
	 * .setPeriodstartdate(timePeriodBean.getPeriodstartdate());
	 * newTimePeriodBean.setPeriodenddate(timePeriodBean.getPeriodenddate());
	 * 
	 * session.merge(newTimePeriodBean); tx.commit(); } catch (Exception ex) {
	 * logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex)); if (tx
	 * != null) { tx.rollback(); } } finally { session.flush(); session.close();
	 * } }
	 */

	public void deleteSubstitute(Long substitutesId) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();

			SubstitutesBean substitutesBean = (SubstitutesBean) session.load(
					SubstitutesBean.class, substitutesId);
			session.delete(substitutesBean);

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

	public SubstitutesBean getSubstitute(String startingDate) {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Transaction tx = null;

		List<SubstitutesBean> substitutesBeans = null;
		try {
			tx = session.beginTransaction();

			String queryString = "SELECT  substitutesid, startingdate, userid"
					+ " FROM substitutes WHERE timestamp(startingdate) = timestamp(?)";
			Query query = session.createSQLQuery(queryString).setString(0,
					startingDate);
			List<Object[]> result = query.list();
			SubstitutesBean oneSubstitutesBean = new SubstitutesBean();
			if (result.size() > 0) {
				for (Object[] oneResult : result) {

					oneSubstitutesBean
							.setSubstitutesid(((BigInteger) oneResult[0])
									.longValue());
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd");
					Date startingDateAsDate = formatter.parse(startingDate);

					oneSubstitutesBean.setStartingdate(startingDateAsDate);
					oneSubstitutesBean.setUserid(((BigInteger) oneResult[2])
							.longValue());
				}
			} else {
				tx.commit();
				return null; // TODO
			}

			tx.commit();
			return oneSubstitutesBean;
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

}
