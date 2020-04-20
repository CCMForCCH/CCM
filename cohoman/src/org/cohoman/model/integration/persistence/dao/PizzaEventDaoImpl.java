package org.cohoman.model.integration.persistence.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.dto.PizzaEventDTO;
import org.cohoman.model.integration.persistence.beans.EventTypeDefs;
import org.cohoman.model.integration.persistence.beans.PizzaEvent;
import org.cohoman.model.integration.persistence.beans.PizzaEventBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PizzaEventDaoImpl implements PizzaEventDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private UserDao userDao = null;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public void createPizzaEvent(PizzaEventDTO dto) throws CohomanException {
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
						"Pizza/Potluck", 0L, session); // TODO is empty string OK? (=> won't find
										// "itself")
				if (conflictingEvent != null) {
					throw new CohomanException(
							"Error: Event \""
									+ conflictingEvent
									+ "\" conflicts with your new event. Choose another day or time.");
				}
			}

			PizzaEventBean pizzaEventBean = new PizzaEventBean(
					dto.getEventDate());
			pizzaEventBean.setEventdateend(dto.getEventdateend());
			pizzaEventBean.setEventtype(EventTypeDefs.PIZZAPOTLUCKTYPE);
			pizzaEventBean.setEventName(dto.getEventName());
			pizzaEventBean.setEventinfo(dto.getEventinfo());
			pizzaEventBean.setLeader1(dto.getLeader1());
			pizzaEventBean.setIsmealclosed(dto.isIsmealclosed());
			session.saveOrUpdate(pizzaEventBean);

			tx.commit();
			
			logger.log(Level.INFO, "AUDIT: Just created a pizza event with id " +
					pizzaEventBean.getEventid());

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

	public List<PizzaEvent> getPizzaEvents() {
		List<PizzaEvent> pizzaEvents = null;
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			ConfigInfo configInfo = new ConfigInfo();

			// Query query =
			// session.createQuery("from PizzaEventBean order by eventdate");
			String queryString = "from PizzaEventBean where date(eventdate) between :startdate and :enddate order by eventdate";
			Query query = session
					.createQuery(queryString)
					.setString("startdate", configInfo.getMealPeriodStartDate())
					.setString("enddate", configInfo.getMealPeriodEndDate());

			List<PizzaEventBean> pizzaEventBeans = query.list();

			// TODO perhaps get rid of this copying by combining MealEvent
			// and MealEventBean so no conversion is needed.
			pizzaEvents = new ArrayList<PizzaEvent>();
			for (PizzaEventBean onebean : pizzaEventBeans) {
				PizzaEvent oneEvent = new PizzaEvent(onebean.getEventDate());
				oneEvent.setEventid(onebean.getEventid());
				oneEvent.setEventtype(onebean.getEventtype());
				oneEvent.setEventdateend(onebean.getEventdateend());
				oneEvent.setEventinfo(onebean.getEventinfo());
				oneEvent.setEventName(onebean.getEventName());
				oneEvent.setLeader1(onebean.getLeader1());
				oneEvent.setLeader1String(getFullname(onebean.getLeader1()));
				oneEvent.setLeader2(onebean.getLeader2());
				if (onebean.getLeader2() != null) {
					oneEvent.setLeader2String(getFullname(onebean.getLeader2()));
				}
				oneEvent.setIsmealclosed(onebean.isIsmealclosed());
				pizzaEvents.add(oneEvent);
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
		return pizzaEvents;
	}

	public List<PizzaEvent> getPizzaEventsForDay(Date dateOfDay) {

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
		List<PizzaEventBean> pizzaEventBeans = null;
		List<PizzaEvent> pizzaEvents = null;
		try {
			tx = session.beginTransaction();

			String queryString = "from PizzaEventBean where date(eventdate) between :startdate and :enddate order by eventdate";
			Query query = session.createQuery(queryString)
				.setString("startdate", startdateString)
				.setString("enddate", enddateString);

			pizzaEventBeans = query.list();

			// TODO perhaps get rid of this copying by combining MealEvent
			// and MealEventBean so no conversion is needed.
			pizzaEvents = new ArrayList<PizzaEvent>();
			for (PizzaEventBean onebean : pizzaEventBeans) {
				PizzaEvent oneEvent = new PizzaEvent(onebean.getEventDate());
				oneEvent.setEventid(onebean.getEventid());
				oneEvent.setEventtype(onebean.getEventtype());
				oneEvent.setEventdateend(onebean.getEventdateend());
				oneEvent.setEventinfo(onebean.getEventinfo());
				oneEvent.setEventName(onebean.getEventName());
				oneEvent.setLeader1(onebean.getLeader1());
				oneEvent.setLeader1String(getFullname(onebean.getLeader1()));
				oneEvent.setLeader2(onebean.getLeader2());
				oneEvent.setLeader2String(getFullname(onebean.getLeader2()));
				oneEvent.setIsmealclosed(onebean.isIsmealclosed());
				pizzaEvents.add(oneEvent);
			}
			tx.commit();
			return pizzaEvents;
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
			if (tx != null) {
				tx.rollback();
			}
			return pizzaEvents;
		} finally {
			session.flush();
			session.close();
		}
			
	}

	public PizzaEvent getPizzaEvent(Long eventId) {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		PizzaEvent oneEvent = null;
		try {
			tx = session.beginTransaction();

			String queryString = "from PizzaEventBean " + "where eventid = ?";
			Query query = session.createQuery(queryString).setLong(0, eventId);
			List<PizzaEventBean> pizzaEventBeans = query.list();
			if (pizzaEventBeans.size() != 1) {
				logger.log(Level.SEVERE, "Expected just one bean but got "
						+ pizzaEventBeans.size() + " for eventId " + eventId);
				throw new CohomanException(LoggingUtils.INTERNAL_ERROR);
			}
			PizzaEventBean onebean = pizzaEventBeans.iterator().next();
			oneEvent = new PizzaEvent(onebean.getEventDate());
			oneEvent.setEventid(onebean.getEventid());
			oneEvent.setEventtype(onebean.getEventtype());
			oneEvent.setEventdateend(onebean.getEventdateend());
			oneEvent.setEventinfo(onebean.getEventinfo());
			oneEvent.setEventName(onebean.getEventName());
			oneEvent.setLeader1(onebean.getLeader1());
			oneEvent.setLeader1String(getFullname(onebean.getLeader1()));
			oneEvent.setLeader2(onebean.getLeader2());
			oneEvent.setLeader2String(getFullname(onebean.getLeader2()));
			oneEvent.setIsmealclosed(onebean.isIsmealclosed());

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

	public void updatePizzaEvent(PizzaEvent pizzaEvent) {

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
						pizzaEvent.getEventDate(), pizzaEvent.getEventdateend(),
						spacebeanId, "Pizza/Potluck", pizzaEvent.getEventid(), session); // TODO is empty string OK?
													// (=> won't find "itself")
				if (conflictingEvent != null) {
					throw new CohomanException(
							"Error: Event \""
									+ conflictingEvent
									+ "\" conflicts with your new event. Choose another day or time.");
				}
			}

			PizzaEventBean pizzaEventBean = (PizzaEventBean) session.load(
					PizzaEventBean.class, pizzaEvent.getEventid());
			pizzaEventBean.setEventdateend(pizzaEvent.getEventdateend());
			pizzaEventBean.setEventinfo(pizzaEvent.getEventinfo());
			pizzaEventBean.setEventName(pizzaEvent.getEventName());

			pizzaEventBean.setLeader1(pizzaEvent.getLeader1());
			pizzaEventBean.setLeader2(pizzaEvent.getLeader2());
			pizzaEventBean.setIsmealclosed(pizzaEvent.isIsmealclosed());
			session.merge(pizzaEventBean);
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

	public void deletePizzaEvent(PizzaEvent pizzaEvent) {
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			// Long eventid = mealEvent.getEventid();
			PizzaEventBean pizzaEventBean = (PizzaEventBean) session.load(
					PizzaEventBean.class, pizzaEvent.getEventid());
			session.delete(pizzaEventBean);

			tx.commit();

			logger.log(Level.INFO, "AUDIT: Just deleted a pizza event with id " +
					pizzaEventBean.getEventid());

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
