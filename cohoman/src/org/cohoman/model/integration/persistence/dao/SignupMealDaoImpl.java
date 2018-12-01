package org.cohoman.model.integration.persistence.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.dto.SignupMealDTO;
import org.cohoman.model.integration.persistence.beans.SignupMealBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SignupMealDaoImpl implements SignupMealDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void create(SignupMealDTO dto) throws CohomanException {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			// Check to see if there is already another entry for "me"
			String queryString = "from SignupMealBean where eventid = ? and userid = ?";
			Query query = session.createQuery(queryString)
					.setLong(0, dto.getEventid()).setLong(1, dto.getUserid());
			List<SignupMealBean> signupMealBeans = query.list();

			if (!signupMealBeans.isEmpty()) {
				int alreadyHaveNumber =
					signupMealBeans.get(0).getNumberattending();
			throw new CohomanException(
					"Error: You have already signed-up for " +
					alreadyHaveNumber + " meal(s). " +
					"Delete this entry and add a new one " +
					"if you want to sign-up for a different number of people.");
			}

			SignupMealBean signupMealBean = new SignupMealBean();
			signupMealBean.setEventid(dto.getEventid());
			signupMealBean.setUserid(dto.getUserid());
			signupMealBean.setNumberattending(dto.getNumberattending());
			session.saveOrUpdate(signupMealBean);

			tx.commit();
		} catch (CohomanException ex) {
			if (tx != null) {
				tx.rollback();
			}
			throw new CohomanException(ex.getErrorText());			
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

	public void delete(SignupMealDTO dto) {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			// Need to find the correct row first before performing the
			// delete.
			String queryString = "from SignupMealBean where eventid = ? and userid = ?";
			Query query = session.createQuery(queryString)
					.setLong(0, dto.getEventid()).setLong(1, dto.getUserid());
			List<SignupMealBean> signupMealBeans = query.list();

			// Loop through all beans deleting all entries even though there
			// really should be just one (or none is OK too).
			for (SignupMealBean oneBean : signupMealBeans) {
				SignupMealBean signupMealBean = (SignupMealBean) session.load(
						SignupMealBean.class, oneBean.getSignupmealid());
				session.delete(signupMealBean);
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

	public List<SignupMealDTO> getAllMealSignups(Long eventId) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			String queryString = "from SignupMealBean where eventid = ?";
			Query query = session.createQuery(queryString).setLong(0, eventId);
			List<SignupMealBean> signupMealBeans = query.list();

			// TODO perhaps get rid of this copying by combining MealEvent
			// and MealEventBean so no conversion is needed.
			List<SignupMealDTO> signupMealDTOs = new ArrayList<SignupMealDTO>();
			for (SignupMealBean onebean : signupMealBeans) {
				SignupMealDTO oneSignup = new SignupMealDTO();
				oneSignup.setEventid(onebean.getEventid());
				oneSignup.setUserid(onebean.getUserid());
				oneSignup.setNumberattending(onebean.getNumberattending());
				signupMealDTOs.add(oneSignup);
			}

			tx.commit();
			return signupMealDTOs;
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
