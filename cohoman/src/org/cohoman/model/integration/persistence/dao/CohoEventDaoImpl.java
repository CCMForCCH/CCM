package org.cohoman.model.integration.persistence.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.dto.CohoEventDTO;
import org.cohoman.model.integration.persistence.beans.CohoEvent;
import org.cohoman.model.integration.persistence.beans.CohoEventBean;
import org.cohoman.model.integration.persistence.beans.EventSpace;
import org.cohoman.model.integration.persistence.beans.SpaceBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CohoEventDaoImpl implements CohoEventDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private EventSpaceDao eventSpaceDao = null;

	public EventSpaceDao getEventSpaceDao() {
		return eventSpaceDao;
	}

	public void setEventSpaceDao(EventSpaceDao eventSpaceDao) {
		this.eventSpaceDao = eventSpaceDao;
	}
	
	@Override
	public void createCohoEvent(CohoEventDTO dto) throws CohomanException {
		// TODO Auto-generated method stub
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();
			
			// First, check that this event doesn't conflict with another
			String conflictingEvent;
			for (String spacebeanId : dto.getSpaceList()) {
				conflictingEvent = 
					DaoUtils.getSpaceForPeriod(dto.getEventDate(), dto.getEventdateend(),
							spacebeanId, dto.getEventName(), 0L, session);
				if (conflictingEvent != null) {
					throw new CohomanException("Error: Event \"" + conflictingEvent +
							"\" conflicts with your new event. Choose another time or reserved common space.");
				}
			}
			CohoEventBean cohoEventBean = new CohoEventBean(dto.getEventDate());
			cohoEventBean.setEventdateend(dto.getEventdateend());
			cohoEventBean.setEventtype(dto.getEventtype());
			cohoEventBean.setEventinfo(dto.getEventinfo());
			cohoEventBean.setEventName(dto.getEventName());
			cohoEventBean.setEnteredby(dto.getEnteredby());
			session.saveOrUpdate(cohoEventBean);
			
			// Add one entry for each space chosen
			// NOTE: have to convert String to Long since JSF only
			// returns Strings
			for (String spaceBeanId : dto.getSpaceList()) {
				EventSpace eventSpace = new EventSpace();
				eventSpace.setEventid(cohoEventBean.getEventid());
				eventSpace.setEventtype(dto.getEventtype());
				eventSpace.setSpaceId(Long.valueOf(spaceBeanId));
				session.saveOrUpdate(eventSpace);
			}
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

	/*
	private String getSpaceForPeriod(Date eventDate, Date eventdateend,
			String spaceName, Session session) {

		String returnString = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
		String startdateString = formatter.format(eventDate);
		String enddateString = formatter.format(eventdateend);

		CohoEvent oneEvent = null;
		String queryString = "SELECT t1.eventtype, t1.eventName " +
			"FROM cohoevent t1 " +
			"JOIN eventspace t2 ON t1.eventid = t2.eventid " +
			"AND t1.eventtype = t2.eventtype " +
			"WHERE timestamp( t1.eventdate ) " +
			"BETWEEN ? AND ? AND t2.spaceid = ? ";
		Query query = session.createSQLQuery(queryString).setString(0, startdateString).
				setString(1, enddateString).setString(2, spaceName);
		
		List<Object[]> result = query.list();
		if (result.size() != 0) {
			for (Object[] oneResult : result) {
				returnString = (String)oneResult[1] +
				    "(" + (String)oneResult[0] + ")";
				break;
			}
		}
        return returnString;
		
	}
	
	*/
	
	public List<CohoEvent> getCohoEvents(){
		Session session = HibernateUtil.getSessionFactory().openSession();

		String queryString = "from CohoEventBean where eventdate >= NOW() order by eventdate";
		Query query = session.createQuery(queryString);

		List<CohoEventBean> cohoEventBeans = query.list();
		
		//TODO perhaps get rid of this copying by combining MealEvent
		// and MealEventBean so no conversion is needed.
		List<CohoEvent> cohoEvents = new ArrayList<CohoEvent>();
		for (CohoEventBean onebean : cohoEventBeans) {
			CohoEvent oneEvent = new CohoEvent(onebean.getEventDate());
			oneEvent.setEventid(onebean.getEventid());
			oneEvent.setEventtype(onebean.getEventtype());
			oneEvent.setEventdateend(onebean.getEventdateend());
			oneEvent.setEventinfo(onebean.getEventinfo());
			oneEvent.setEventName(onebean.getEventName());
			oneEvent.setEnteredby(onebean.getEnteredby());
			
			// Get list of spaces for this event
			// Problem with string with spaces ...
			query = session.createQuery(
					"from EventSpace where eventid = ? and eventtype = '" 
					+ onebean.getEventtype() + "'")
			.setString(0, Long.toString(onebean.getEventid()));
			List<EventSpace> eventSpaceList = query.list();
			String printableSpaceList = "";
			boolean firsttime = true;
			for (EventSpace eventSpace : eventSpaceList) {
				query = session.createQuery("from SpaceBean where spaceId = ?")
				.setString(0, Long.toString(eventSpace.getSpaceId()));
				List<SpaceBean> spaceBeanListOfOne = query.list();
				if (firsttime) {
					printableSpaceList += spaceBeanListOfOne.get(0).getSpaceName();
					firsttime = false;
				} else {
					printableSpaceList += ", " + spaceBeanListOfOne.get(0).getSpaceName();
				}
			}
			oneEvent.setPrintableSpaceList(printableSpaceList);
			cohoEvents.add(oneEvent);	
		}
		session.flush();
		session.close();
		return cohoEvents;
	}

	public List<CohoEvent> getCohoEventsForDay(Date dateOfDay){
		Session session = HibernateUtil.getSessionFactory().openSession();

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

		String queryString = "from CohoEventBean where date(eventdate) between :startdate and :enddate order by eventdate";
		Query query = session.createQuery(queryString)
				.setString("startdate", startdateString)
				.setString("enddate", enddateString);

		List<CohoEventBean> cohoEventBeans = query.list();
		
		//TODO perhaps get rid of this copying by combining MealEvent
		// and MealEventBean so no conversion is needed.
		List<CohoEvent> cohoEvents = new ArrayList<CohoEvent>();
		for (CohoEventBean onebean : cohoEventBeans) {
			CohoEvent oneEvent = new CohoEvent(onebean.getEventDate());
			oneEvent.setEventid(onebean.getEventid());
			oneEvent.setEventtype(onebean.getEventtype());
			oneEvent.setEventdateend(onebean.getEventdateend());
			oneEvent.setEventinfo(onebean.getEventinfo());
			oneEvent.setEventName(onebean.getEventName());
			oneEvent.setEventtype(onebean.getEventtype());
			oneEvent.setEnteredby(onebean.getEnteredby());
			cohoEvents.add(oneEvent);	
		}
		session.flush();
		session.close();
		return cohoEvents;
	}
	
	public CohoEvent getCohoEvent(Long eventId) {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		CohoEvent oneEvent = null;
		try {
			tx = session.beginTransaction();

			String queryString ="from CohoEventBean " +
				"where eventid = ?";
			Query query = session.createQuery(queryString).setLong(0, eventId);
			List<CohoEventBean> cohoEventBeans = query.list();
			if (cohoEventBeans.size() != 1) {
				throw new RuntimeException("Expected just one bean but got " +
						cohoEventBeans.size());
			}
			CohoEventBean onebean = cohoEventBeans.iterator().next();
			oneEvent = new CohoEvent(onebean.getEventDate());
			oneEvent.setEventid(onebean.getEventid());
			oneEvent.setEventtype(onebean.getEventtype());
			oneEvent.setEventdateend(onebean.getEventdateend());
			oneEvent.setEventinfo(onebean.getEventinfo());
			oneEvent.setEventName(onebean.getEventName());
			oneEvent.setEnteredby(onebean.getEnteredby());
		
			// Now separate query to get the spaces list
			queryString = 
				"select t3.spaceName,t3.spaceId from cohoevent t1" + 
				" join eventspace t2 on t1.eventid = t2.eventid and t1.eventtype = t2.eventtype" +
				" join spaces t3 on t2.spaceId = t3.spaceId where t1.eventid = :eventId";
			query = session.createSQLQuery(queryString).setLong("eventId", oneEvent.getEventid());
			List<Object[]> result = query.list();
			Set<SpaceBean> spaceBeanSet = new LinkedHashSet();
			for (Object[] onespace : result) {
				SpaceBean oneSpaceBean = new SpaceBean();
				oneSpaceBean.setSpaceName((String)onespace[0]);
				oneSpaceBean.setSpaceId(((BigInteger)onespace[1]).longValue());
				spaceBeanSet.add(oneSpaceBean);
			}
			oneEvent.setSpaceList(spaceBeanSet);
			
			// Get list of spaces that we can print!
			// Problem with string with spaces ...
			query = session.createQuery(
					"from EventSpace where eventid = ? and eventtype = '" 
					+ onebean.getEventtype() + "'")
			.setString(0, Long.toString(onebean.getEventid()));
			List<EventSpace> eventSpaceList = query.list();
			String printableSpaceList = "";
			boolean firsttime = true;
			for (EventSpace eventSpace : eventSpaceList) {
				query = session.createQuery("from SpaceBean where spaceId = ?")
				.setString(0, Long.toString(eventSpace.getSpaceId()));
				List<SpaceBean> spaceBeanListOfOne = query.list();
				if (firsttime) {
					printableSpaceList += spaceBeanListOfOne.get(0).getSpaceName();
					firsttime = false;
				} else {
					printableSpaceList += ", " + spaceBeanListOfOne.get(0).getSpaceName();
				}
			}
			oneEvent.setPrintableSpaceList(printableSpaceList);

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

	public void updateCohoEvent(CohoEvent cohoEvent) throws CohomanException {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();
			
			// First, make sure this updated event doesn't conflict with another
			// Important: base decision on newly checked boxes from page (vs.
			// existingly-held spaces)
			String conflictingEvent;
			//for (SpaceBean spaceBean : cohoEvent.getSpaceList()) {
			for (String spaceId : cohoEvent.getChosenSpaceListStringInts()) {

				conflictingEvent = 
					DaoUtils.getSpaceForPeriod(cohoEvent.getEventDate(), 
							cohoEvent.getEventdateend(),
							//Long.toString(spaceBean.getSpaceId()), session, true);
							spaceId, cohoEvent.getEventName(), cohoEvent.getEventid(), session);
				if (conflictingEvent != null) {
					throw new CohomanException("Error: Event \"" + conflictingEvent +
							"\" conflicts with your updated event. Choose another time or reserved common space.");
				}
			}

			
			String queryString = "from EventSpace where eventid = ? AND eventtype = ?";
			Query query = session.createQuery(queryString).setLong(0, cohoEvent.getEventid()).setString(1, cohoEvent.getEventtype());
			List<EventSpace> eventspaces = query.list();

			// Start by deleting/clearing all existing already set spaces;
			// the idea is to next set only those spaces that are set in
			// the incoming page.
			for (EventSpace oneEventSpace : eventspaces) {
				EventSpace eventSpace = (EventSpace) session.load(
						EventSpace.class, oneEventSpace.getEventspaceid());
				session.delete(eventSpace);
			}
			
			// Create a new cohoeventbean based on the values entered in 
			// the UI 
			CohoEventBean cohoEventBean = 
				(CohoEventBean)session.load(CohoEventBean.class, cohoEvent.getEventid());
			cohoEventBean.setEventDate(cohoEvent.getEventDate());
			cohoEventBean.setEventdateend(cohoEvent.getEventdateend());
			cohoEventBean.setEventinfo(cohoEvent.getEventinfo());
			cohoEventBean.setEventName(cohoEvent.getEventName());
			cohoEventBean.setEventtype(cohoEvent.getEventtype());

			//TODO don't couple spaceId with UI list of positions
			// For every checkbox checked, create a new eventspace bean
			for (String spaceId : cohoEvent.getChosenSpaceListStringInts()) {
				EventSpace eventSpace = new EventSpace();
				eventSpace.setEventid(cohoEvent.getEventid());
				eventSpace.setEventtype(cohoEvent.getEventtype());
				eventSpace.setSpaceId(Long.valueOf(spaceId));
				session.saveOrUpdate(eventSpace);			
			}
			
			// Lastly, do a merge with the cohoEventBean
			session.merge(cohoEventBean);
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
	
	public void deleteCohoEvent(CohoEvent cohoEvent) {
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();
			CohoEventBean cohoEventBean = 
				(CohoEventBean)session.load(CohoEventBean.class, cohoEvent.getEventid());
		
			// Get list of spaces for this event so we can delete them
			// Problem with string with spaces ...
			Query query = session.createQuery(
				"from EventSpace where eventid = ? and eventtype = '" 
				+ cohoEventBean.getEventtype() + "'")
				.setString(0, Long.toString(cohoEventBean.getEventid()));
			List<EventSpace> eventSpaceList = query.list();
			for (EventSpace eventSpace : eventSpaceList) {
				session.delete(eventSpace);
			}

			session.delete(cohoEventBean);
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


}
