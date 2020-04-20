package org.cohoman.model.integration.persistence.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.dto.MealEventDTO;
import org.cohoman.model.integration.persistence.beans.EventTypeDefs;
import org.cohoman.model.integration.persistence.beans.MealEvent;
import org.cohoman.model.integration.persistence.beans.MealEventBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MealEventDaoImpl implements MealEventDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private UserDao userDao = null;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public void createMealEvent(MealEventDTO dto) throws CohomanException {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			// First, check that this event doesn't conflict with another
			String conflictingEvent;
			for (String spacebeanId : dto.getSpaceList()) {
				conflictingEvent = DaoUtils.getSpaceForPeriod(
						dto.getEventDate(), dto.getEventdateend(), spacebeanId,
						"Common Meal", 0L, session); // TODO is empty string OK?
														// (=> won't find
				// "itself")
				if (conflictingEvent != null) {
					throw new CohomanException(
							"Error: Event \""
									+ conflictingEvent
									+ "\" conflicts with your new event. Choose another day or time.");
				}
			}
			MealEventBean mealEventBean = new MealEventBean(dto.getEventDate());
			mealEventBean.setEventdateend(dto.getEventdateend());
			mealEventBean.setEventtype(EventTypeDefs.COMMONMEALTYPE);
			mealEventBean.setEventinfo(dto.getEventinfo());

			mealEventBean.setMenu(dto.getMenu());
			mealEventBean.setCleaner1(dto.getCleaner1());
			mealEventBean.setCleaner2(dto.getCleaner2());
			mealEventBean.setCleaner3(dto.getCleaner3());
			mealEventBean.setCook1(dto.getCook1());
			mealEventBean.setCook2(dto.getCook2());
			mealEventBean.setCook3(dto.getCook3());
			mealEventBean.setCook4(dto.getCook4());
			mealEventBean.setMaxattendees(dto.getMaxattendees());
			mealEventBean.setIsmealclosed(dto.isIsmealclosed());
			session.saveOrUpdate(mealEventBean);

			tx.commit();
			
			logger.log(Level.INFO, "AUDIT: Just created a meal event with id " +
					mealEventBean.getEventid());

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

	public void updateMealEvent(MealEvent mealEvent) throws CohomanException {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			// First, check that this event doesn't conflict with another
			String conflictingEvent;
			List<String> spaceIdList = new ArrayList<String>();
			spaceIdList.add("1");
			spaceIdList.add("2");
			for (String spacebeanId : spaceIdList) {
				conflictingEvent = DaoUtils.getSpaceForPeriod(
						mealEvent.getEventDate(), mealEvent.getEventdateend(),
						spacebeanId, "Common Meal", mealEvent.getEventid(),
						session); // TODO is empty string OK?
				// (=> won't find "itself")
				if (conflictingEvent != null) {
					throw new CohomanException(
							"Error: Event \""
									+ conflictingEvent
									+ "\" conflicts with your new event. Choose another day or time.");
				}
			}

			MealEventBean mealEventBean = (MealEventBean) session.load(
					MealEventBean.class, mealEvent.getEventid());
			mealEventBean.setEventDate(mealEvent.getEventDate());
			mealEventBean.setEventdateend(mealEvent.getEventdateend());
			mealEventBean.setEventinfo(mealEvent.getEventinfo());

			mealEventBean.setMenu(mealEvent.getMenu());
			mealEventBean.setCleaner1(mealEvent.getCleaner1());
			mealEventBean.setCleaner2(mealEvent.getCleaner2());
			mealEventBean.setCleaner3(mealEvent.getCleaner3());
			mealEventBean.setCook1(mealEvent.getCook1());
			mealEventBean.setCook2(mealEvent.getCook2());
			mealEventBean.setCook3(mealEvent.getCook3());
			mealEventBean.setCook4(mealEvent.getCook4());
			mealEventBean.setMaxattendees(mealEvent.getMaxattendees());
			mealEventBean.setIsmealclosed(mealEvent.isIsmealclosed());
			session.merge(mealEventBean);

			tx.commit();
		} catch (CohomanException ex) {
			logger.log(Level.SEVERE, "Exception: " + ex.toString());
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

	public void deleteMealEvent(MealEvent mealEvent) {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			MealEventBean mealEventBean = (MealEventBean) session.load(
					MealEventBean.class, mealEvent.getEventid());
			session.delete(mealEventBean);

			tx.commit();
			
			logger.log(Level.INFO, "AUDIT: Just deleted a meal event with id " +
					mealEventBean.getEventid());

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

	public List<MealEvent> getMealEvents() {

		// TODO add start and end date parameters
		ConfigInfo configInfo = new ConfigInfo();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			String queryString = "from MealEventBean where date(eventdate) between :startdate and :enddate order by eventdate";
			Query query = session
					.createQuery(queryString)
					.setString("startdate", configInfo.getMealPeriodStartDate())
					.setString("enddate", configInfo.getMealPeriodEndDate());
			List<MealEventBean> mealEventBeans = query.list();

			// TODO perhaps get rid of this copying by combining MealEvent
			// and MealEventBean so no conversion is needed.
			List<MealEvent> mealEvents = new ArrayList<MealEvent>();
			for (MealEventBean onebean : mealEventBeans) {
				MealEvent oneEvent = new MealEvent(onebean.getEventDate());
				oneEvent.setEventid(onebean.getEventid());
				oneEvent.setEventtype(onebean.getEventtype());
				oneEvent.setEventdateend(onebean.getEventdateend());
				oneEvent.setEventinfo(onebean.getEventinfo());
				oneEvent.setMenu(onebean.getMenu());
				oneEvent.setCook1(onebean.getCook1());
				oneEvent.setCook1String(getFullname(onebean.getCook1()));
				oneEvent.setCook2(onebean.getCook2());
				oneEvent.setCook2String(getFullname(onebean.getCook2()));
				oneEvent.setCook3(onebean.getCook3());
				oneEvent.setCook3String(getFullname(onebean.getCook3()));
				oneEvent.setCook4(onebean.getCook4());
				oneEvent.setCook4String(getFullname(onebean.getCook4()));
				oneEvent.setCleaner1(onebean.getCleaner1());
				oneEvent.setCleaner1String(getFullname(onebean.getCleaner1()));
				oneEvent.setCleaner2(onebean.getCleaner2());
				oneEvent.setCleaner2String(getFullname(onebean.getCleaner2()));
				oneEvent.setCleaner3(onebean.getCleaner3());
				oneEvent.setCleaner3String(getFullname(onebean.getCleaner3()));
				oneEvent.setMaxattendees(onebean.getMaxattendees());
				oneEvent.setIsmealclosed(onebean.isIsmealclosed());
				mealEvents.add(oneEvent);
			}

			tx.commit();
			return mealEvents;
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

	public List<MealEvent> getAllMealEvents() {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			String queryString = "from MealEventBean order by eventdate";
			Query query = session.createQuery(queryString);
			List<MealEventBean> mealEventBeans = query.list();

			// TODO perhaps get rid of this copying by combining MealEvent
			// and MealEventBean so no conversion is needed.
			List<MealEvent> mealEvents = new ArrayList<MealEvent>();
			for (MealEventBean onebean : mealEventBeans) {
				MealEvent oneEvent = new MealEvent(onebean.getEventDate());
				oneEvent.setEventid(onebean.getEventid());
				oneEvent.setEventtype(onebean.getEventtype());
				oneEvent.setEventdateend(onebean.getEventdateend());
				oneEvent.setEventinfo(onebean.getEventinfo());
				oneEvent.setMenu(onebean.getMenu());
				oneEvent.setCook1(onebean.getCook1());
				oneEvent.setCook2(onebean.getCook2());
				oneEvent.setCook3(onebean.getCook3());
				oneEvent.setCook4(onebean.getCook4());
				oneEvent.setCleaner1(onebean.getCleaner1());
				oneEvent.setCleaner2(onebean.getCleaner2());
				oneEvent.setCleaner3(onebean.getCleaner3());
				oneEvent.setMaxattendees(onebean.getMaxattendees());
				oneEvent.setIsmealclosed(onebean.isIsmealclosed());
				mealEvents.add(oneEvent);
			}

			tx.commit();
			return mealEvents;
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

	public List<MealEvent> getMealEventsForDay(Date dateOfDay) {

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
		try {
			tx = session.beginTransaction();

			String queryString = "from MealEventBean where date(eventdate) between :startdate and :enddate order by eventdate";
			Query query = session.createQuery(queryString)
					.setString("startdate", startdateString)
					.setString("enddate", enddateString);
			List<MealEventBean> mealEventBeans = query.list();

			// TODO perhaps get rid of this copying by combining MealEvent
			// and MealEventBean so no conversion is needed.
			List<MealEvent> mealEvents = new ArrayList<MealEvent>();
			for (MealEventBean onebean : mealEventBeans) {
				MealEvent oneEvent = new MealEvent(onebean.getEventDate());
				oneEvent.setEventid(onebean.getEventid());
				oneEvent.setEventtype(onebean.getEventtype());
				oneEvent.setEventdateend(onebean.getEventdateend());
				oneEvent.setEventinfo(onebean.getEventinfo());
				oneEvent.setMenu(onebean.getMenu());
				oneEvent.setCook1(onebean.getCook1());
				oneEvent.setCook2(onebean.getCook2());
				oneEvent.setCook3(onebean.getCook3());
				oneEvent.setCook4(onebean.getCook4());
				oneEvent.setCleaner1(onebean.getCleaner1());
				oneEvent.setCleaner2(onebean.getCleaner2());
				oneEvent.setCleaner3(onebean.getCleaner3());
				oneEvent.setMaxattendees(onebean.getMaxattendees());
				oneEvent.setIsmealclosed(onebean.isIsmealclosed());
				mealEvents.add(oneEvent);
			}

			tx.commit();
			return mealEvents;
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

	public MealEvent getMealEvent(Long eventId) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			String queryString = "from MealEventBean " + "where eventid = ?";
			Query query = session.createQuery(queryString).setLong(0, eventId);
			List<MealEventBean> mealEventBeans = query.list();
			if (mealEventBeans.size() != 1) {
				logger.log(Level.SEVERE, "Expected just one bean but got "
						+ mealEventBeans.size() + " beans for event id " + eventId);
				if (tx != null) {
					tx.rollback();
				}
				return null;
			}
			MealEventBean onebean = mealEventBeans.iterator().next();
			MealEvent oneEvent = new MealEvent(onebean.getEventDate());
			oneEvent.setEventid(onebean.getEventid());
			oneEvent.setEventtype(onebean.getEventtype());
			oneEvent.setEventdateend(onebean.getEventdateend());
			oneEvent.setEventinfo(onebean.getEventinfo());
			oneEvent.setMenu(onebean.getMenu());
			oneEvent.setCook1(onebean.getCook1());
			oneEvent.setCook1String(getFullname(onebean.getCook1()));
			oneEvent.setCook2(onebean.getCook2());
			oneEvent.setCook2String(getFullname(onebean.getCook2()));
			oneEvent.setCook3(onebean.getCook3());
			oneEvent.setCook3String(getFullname(onebean.getCook3()));
			oneEvent.setCook4(onebean.getCook4());
			oneEvent.setCook4String(getFullname(onebean.getCook4()));
			oneEvent.setCleaner1(onebean.getCleaner1());
			oneEvent.setCleaner1String(getFullname(onebean.getCleaner1()));
			oneEvent.setCleaner2(onebean.getCleaner2());
			oneEvent.setCleaner2String(getFullname(onebean.getCleaner2()));
			oneEvent.setCleaner3(onebean.getCleaner3());
			oneEvent.setCleaner3String(getFullname(onebean.getCleaner3()));
			oneEvent.setMaxattendees(onebean.getMaxattendees());
			oneEvent.setIsmealclosed(onebean.isIsmealclosed());
			tx.commit();
			return oneEvent;
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
	
	private String getFullname(Long userid) {

		String fullname = "";
		if (userid != null) {
			fullname = userDao.getUser(userid).getFirstname() + " "
					+ userDao.getUser(userid).getLastname();
		}
		return fullname;
	}

}
