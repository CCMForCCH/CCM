package org.cohoman.model.integration.persistence.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.dto.SignupPotluckDTO;
import org.cohoman.model.integration.persistence.beans.SignupPotluckBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SignupPotluckDaoImpl implements SignupPotluckDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void create(SignupPotluckDTO dto) throws CohomanException {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			SignupPotluckBean signupPotluckBean = new SignupPotluckBean();
			signupPotluckBean.setEventid(dto.getEventid());
			signupPotluckBean.setUserid(dto.getUserid());
			signupPotluckBean.setNumberattending(dto.getNumberattending());
			signupPotluckBean.setItemtype(dto.getItemtype());
			signupPotluckBean.setItemdescription(dto.getItemdescription());
			session.saveOrUpdate(signupPotluckBean);

			tx.commit();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			if (tx != null) {
				tx.rollback();
			}
			throw new CohomanException(LoggingUtils.INTERNAL_ERROR);
		} finally {
			session.flush();
			session.close();
		}
	}

	public void delete(SignupPotluckDTO dto) {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			// Need to find the correct row first before performing the
			// delete.
			String queryString = "from SignupPotluckBean where eventid = ? and userid = ?";
			Query query = session.createQuery(queryString)
					.setLong(0, dto.getEventid()).setLong(1, dto.getUserid());
			List<SignupPotluckBean> signupPotluckBeans = query.list();

			// Loop through all beans deleting all entries even though there
			// really should be just one (or none is OK too).
			for (SignupPotluckBean oneBean : signupPotluckBeans) {
				SignupPotluckBean signupPotluckBean = (SignupPotluckBean) session.load(
						SignupPotluckBean.class, oneBean.getSignuppotluckid());
				session.delete(signupPotluckBean);
			}

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

	public List<SignupPotluckDTO> getAllPotluckSignups(Long eventId) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			String queryString = "from SignupPotluckBean where eventid = ?";
			Query query = session.createQuery(queryString).setLong(0, eventId);
			List<SignupPotluckBean> signupPotluckBeans = query.list();

			// TODO perhaps get rid of this copying by combining MealEvent
			// and MealEventBean so no conversion is needed.
			List<SignupPotluckDTO> signupPotluckDTOs = new ArrayList<SignupPotluckDTO>();
			for (SignupPotluckBean onebean : signupPotluckBeans) {
				SignupPotluckDTO oneSignup = new SignupPotluckDTO();
				oneSignup.setEventid(onebean.getEventid());
				oneSignup.setUserid(onebean.getUserid());
				oneSignup.setNumberattending(onebean.getNumberattending());
				oneSignup.setItemtype(onebean.getItemtype());
				oneSignup.setItemdescription(onebean.getItemdescription());
				signupPotluckDTOs.add(oneSignup);
			}

			tx.commit();
			return signupPotluckDTOs;
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
