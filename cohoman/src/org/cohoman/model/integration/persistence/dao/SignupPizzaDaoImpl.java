package org.cohoman.model.integration.persistence.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.dto.SignupPizzaDTO;
import org.cohoman.model.integration.persistence.beans.SignupPizzaBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SignupPizzaDaoImpl implements SignupPizzaDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void create(SignupPizzaDTO dto) throws CohomanException {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			SignupPizzaBean signupPizzaBean = new SignupPizzaBean();
			signupPizzaBean.setEventid(dto.getEventid());
			signupPizzaBean.setUserid(dto.getUserid());
			signupPizzaBean.setNumberattendingpizza(dto.getNumberattendingpizza());
			signupPizzaBean.setNumberattendingpotluck(dto.getNumberattendingpotluck());
			signupPizzaBean.setPizzatopping1(dto.getPizzatopping1());
			signupPizzaBean.setPizzatopping2(dto.getPizzatopping2());
			signupPizzaBean.setPizzawillbring(dto.getPizzawillbring());
			session.saveOrUpdate(signupPizzaBean);

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

	public void delete(SignupPizzaDTO dto) {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			// Need to find the correct row first before performing the
			// delete.
			String queryString = "from SignupPizzaBean where eventid = ? and userid = ?";
			Query query = session.createQuery(queryString)
					.setLong(0, dto.getEventid()).setLong(1, dto.getUserid());
			List<SignupPizzaBean> signupPizzaBeans = query.list();

			// Loop through all beans deleting all entries even though there
			// really should be just one (or none is OK too).
			for (SignupPizzaBean oneBean : signupPizzaBeans) {
				SignupPizzaBean signupPizzaBean = (SignupPizzaBean) session.load(
						SignupPizzaBean.class, oneBean.getSignuppizzaid());
				session.delete(signupPizzaBean);
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

	public List<SignupPizzaDTO> getAllPizzaSignups(Long eventId) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			String queryString = "from SignupPizzaBean where eventid = ?";
			Query query = session.createQuery(queryString).setLong(0, eventId);
			List<SignupPizzaBean> signupPizzaBeans = query.list();

			// TODO perhaps get rid of this copying by combining MealEvent
			// and MealEventBean so no conversion is needed.
			List<SignupPizzaDTO> signupPizzaDTOs = new ArrayList<SignupPizzaDTO>();
			for (SignupPizzaBean onebean : signupPizzaBeans) {
				SignupPizzaDTO oneSignup = new SignupPizzaDTO();
				oneSignup.setEventid(onebean.getEventid());
				oneSignup.setUserid(onebean.getUserid());
				oneSignup.setNumberattendingpizza(onebean.getNumberattendingpizza());
				oneSignup.setNumberattendingpotluck(onebean.getNumberattendingpotluck());
				oneSignup.setPizzatopping1(onebean.getPizzatopping1());
				oneSignup.setPizzatopping2(onebean.getPizzatopping2());
				oneSignup.setPizzawillbring(onebean.getPizzawillbring());
				signupPizzaDTOs.add(oneSignup);
			}

			tx.commit();
			return signupPizzaDTOs;
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
