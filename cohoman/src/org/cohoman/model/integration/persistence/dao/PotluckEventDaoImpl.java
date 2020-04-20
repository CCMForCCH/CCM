package org.cohoman.model.integration.persistence.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.dto.PotluckEventDTO;
import org.cohoman.model.integration.persistence.beans.EventTypeDefs;
import org.cohoman.model.integration.persistence.beans.PotluckEvent;
import org.cohoman.model.integration.persistence.beans.PotluckEventBean;
import org.cohoman.model.integration.persistence.beans.SignupPotluckBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PotluckEventDaoImpl implements PotluckEventDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private UserDao userDao = null;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public void createPotluckEvent(PotluckEventDTO dto) throws CohomanException {
		// TODO Auto-generated method stub

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			// First, check that this event doesn't conflict with another
			String conflictingEvent;
			for (String spacebeanId : dto.getSpaceList()) {
				conflictingEvent = DaoUtils.getSpaceForPeriod(
						dto.getEventDate(), dto.getEventdateend(), spacebeanId,
						"Potluck", 0L, session); // TODO is empty string OK? (=> won't find
										// "itself")
				if (conflictingEvent != null) {
					throw new CohomanException(
							"Error: Event \""
									+ conflictingEvent
									+ "\" conflicts with your new event. Choose another day or time.");
				}
			}

			PotluckEventBean potluckEventBean = new PotluckEventBean(
					dto.getEventDate());
			potluckEventBean.setEventdateend(dto.getEventdateend());
			potluckEventBean.setEventtype(EventTypeDefs.POTLUCKTYPE);
			potluckEventBean.setEventinfo(dto.getEventinfo());
			potluckEventBean.setLeader1(dto.getLeader1());
			session.saveOrUpdate(potluckEventBean);

			tx.commit();
			
			logger.log(Level.INFO, "AUDIT: Just created a potluck event with id " +
					potluckEventBean.getEventid());

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

	public List<PotluckEvent> getPotluckEvents() {
		List<PotluckEvent> potluckEvents = null;
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			ConfigInfo configInfo = new ConfigInfo();

			// Query query =
			// session.createQuery("from PotluckEventBean order by eventdate");
			String queryString = "from PotluckEventBean where date(eventdate) between :startdate and :enddate order by eventdate";
			Query query = session
					.createQuery(queryString)
					.setString("startdate", configInfo.getMealPeriodStartDate())
					.setString("enddate", configInfo.getMealPeriodEndDate());

			List<PotluckEventBean> potluckEventBeans = query.list();

			// TODO perhaps get rid of this copying by combining MealEvent
			// and MealEventBean so no conversion is needed.
			potluckEvents = new ArrayList<PotluckEvent>();
			for (PotluckEventBean onebean : potluckEventBeans) {
				PotluckEvent oneEvent = new PotluckEvent(onebean.getEventDate());
				oneEvent.setEventid(onebean.getEventid());
				oneEvent.setEventtype(onebean.getEventtype());
				oneEvent.setEventdateend(onebean.getEventdateend());
				oneEvent.setEventinfo(onebean.getEventinfo());
				oneEvent.setLeader1(onebean.getLeader1());
				oneEvent.setLeader1String(getFullname(onebean.getLeader1()));
				oneEvent.setLeader2(onebean.getLeader2());
				if (onebean.getLeader2() != null) {
					oneEvent.setLeader2String(getFullname(onebean.getLeader2()));
				}
				potluckEvents.add(oneEvent);
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
		return potluckEvents;
	}

	public List<PotluckEvent> getPotluckEventsForDay(Date dateOfDay) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
		Date startdate = CalendarUtils.truncateTimeFromDate(dateOfDay);
		String startdateString = formatter.format(startdate);
		Calendar endDateCal = Calendar.getInstance();
		endDateCal.setTime(startdate);
		// here's where 23 hrs. + 59 min. doesn't work
		endDateCal.add(Calendar.DAY_OF_YEAR, 1);
		endDateCal.add(Calendar.MINUTE, -1);
		Date enddate = endDateCal.getTime();
		String enddateString = formatter.format(enddate);

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<PotluckEventBean> potluckEventBeans = null;
		List<PotluckEvent> potluckEvents = null;
		try {
			tx = session.beginTransaction();

			String queryString = "from PotluckEventBean where date(eventdate) between :startdate and :enddate order by eventdate";
			Query query = session.createQuery(queryString)
				.setString("startdate", startdateString)
				.setString("enddate", enddateString);

			potluckEventBeans = query.list();

			// TODO perhaps get rid of this copying by combining MealEvent
			// and MealEventBean so no conversion is needed.
			potluckEvents = new ArrayList<PotluckEvent>();
			for (PotluckEventBean onebean : potluckEventBeans) {
				PotluckEvent oneEvent = new PotluckEvent(onebean.getEventDate());
				oneEvent.setEventid(onebean.getEventid());
				oneEvent.setEventtype(onebean.getEventtype());
				oneEvent.setEventdateend(onebean.getEventdateend());
				oneEvent.setEventinfo(onebean.getEventinfo());
				oneEvent.setLeader1(onebean.getLeader1());
				oneEvent.setLeader1String(getFullname(onebean.getLeader1()));
				oneEvent.setLeader2(onebean.getLeader2());
				oneEvent.setLeader2String(getFullname(onebean.getLeader2()));
				potluckEvents.add(oneEvent);
			}
			tx.commit();
			return potluckEvents;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			if (tx != null) {
				tx.rollback();
			}
			return potluckEvents;
		} finally {
			session.flush();
			session.close();
		}
			
	}

	public PotluckEvent getPotluckEvent(Long eventId) {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		PotluckEvent oneEvent = null;
		try {
			tx = session.beginTransaction();

			String queryString = "from PotluckEventBean " + "where eventid = ?";
			Query query = session.createQuery(queryString).setLong(0, eventId);
			List<PotluckEventBean> potluckEventBeans = query.list();
			if (potluckEventBeans.size() != 1) {
				logger.log(Level.SEVERE, "Expected just one bean but got "
						+ potluckEventBeans.size() + " for eventId " + eventId);
				throw new CohomanException(LoggingUtils.INTERNAL_ERROR);
			}
			PotluckEventBean onebean = potluckEventBeans.iterator().next();
			oneEvent = new PotluckEvent(onebean.getEventDate());
			oneEvent.setEventid(onebean.getEventid());
			oneEvent.setEventtype(onebean.getEventtype());
			oneEvent.setEventdateend(onebean.getEventdateend());
			oneEvent.setEventinfo(onebean.getEventinfo());
			oneEvent.setLeader1(onebean.getLeader1());
			oneEvent.setLeader1String(getFullname(onebean.getLeader1()));
			oneEvent.setLeader2(onebean.getLeader2());
			oneEvent.setLeader2String(getFullname(onebean.getLeader2()));

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
		return oneEvent;

	}

	public void updatePotluckEvent(PotluckEvent potluckEvent) {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {

			tx = session.beginTransaction();

			// First, check that this event doesn't conflict with another
			String conflictingEvent;
			List<String> spaceIdList = new ArrayList<String>();
			
			// Assume kitchen and dining room
			spaceIdList.add("1");
			spaceIdList.add("2");
			for (String spacebeanId : spaceIdList) {
				conflictingEvent = DaoUtils.getSpaceForPeriod(
						potluckEvent.getEventDate(), potluckEvent.getEventdateend(),
						spacebeanId, "Potluck", potluckEvent.getEventid(), session); // TODO is empty string OK?
													// (=> won't find "itself")
				if (conflictingEvent != null) {
					throw new CohomanException(
							"Error: Event \""
									+ conflictingEvent
									+ "\" conflicts with your new event. Choose another day or time.");
				}
			}

			PotluckEventBean potluckEventBean = (PotluckEventBean) session.load(
					PotluckEventBean.class, potluckEvent.getEventid());
			potluckEventBean.setEventdateend(potluckEvent.getEventdateend());
			potluckEventBean.setEventinfo(potluckEvent.getEventinfo());

			potluckEventBean.setLeader1(potluckEvent.getLeader1());
			potluckEventBean.setLeader2(potluckEvent.getLeader2());
			session.merge(potluckEventBean);
			tx.commit();

		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			if (tx != null) {
				tx.rollback();
			}
			throw new RuntimeException(LoggingUtils.INTERNAL_ERROR);
		} finally {
			session.flush();
			session.close();
		}
	}

	public void deletePotluckEvent(PotluckEvent potluckEvent) {
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			// Delete the signups for this potluck first.
			String queryString = "from SignupPotluckBean where eventid = ?";
			Query query = session.createQuery(queryString)
					.setLong(0, potluckEvent.getEventid());
			List<SignupPotluckBean> signupPotluckBeans = query.list();

			// Loop through all beans deleting all entries .
			for (SignupPotluckBean oneBean : signupPotluckBeans) {
				SignupPotluckBean signupPotluckBean = (SignupPotluckBean) session.load(
						SignupPotluckBean.class, oneBean.getSignuppotluckid());
				session.delete(signupPotluckBean);
			}

			// Now there are no signups. OK to delete the meal
			// Long eventid = mealEvent.getEventid();
			PotluckEventBean potluckEventBean = (PotluckEventBean) session.load(
					PotluckEventBean.class, potluckEvent.getEventid());
			session.delete(potluckEventBean);

			tx.commit();
			
			logger.log(Level.INFO, "AUDIT: Just deleted a potluck event with id " +
					potluckEventBean.getEventid());

		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			if (tx != null) {
				tx.rollback();
			}
			throw new RuntimeException(LoggingUtils.INTERNAL_ERROR);
		} finally {
			session.flush();
			session.close();
		}

	}

	private String getFullname(Long userid) {

		String fullname = "";
		if (userid != null) {
			fullname = userDao.getUser(userid).getFirstname() + " "
					+ userDao.getUser(userid).getLastname();
		}
		return fullname;
	}

}
