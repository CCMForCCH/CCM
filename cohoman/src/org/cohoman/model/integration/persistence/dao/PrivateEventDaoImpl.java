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

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.cohoman.model.business.User;
import org.cohoman.model.dto.PrivateEventDTO;
import org.cohoman.model.integration.persistence.beans.EventTypeDefs;
import org.cohoman.model.integration.persistence.beans.PrivateEvent;
import org.cohoman.model.integration.persistence.beans.PrivateEventBean;
import org.cohoman.model.integration.persistence.beans.EventSpace;
import org.cohoman.model.integration.persistence.beans.SpaceBean;
import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.view.controller.CohomanException;
import org.cohoman.view.controller.utils.CalendarUtils;
import org.cohoman.view.controller.utils.EventStateEnums;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PrivateEventDaoImpl implements PrivateEventDao {

	Logger logger = Logger.getLogger(this.getClass().getName());

	private EventSpaceDao eventSpaceDao = null;

	public EventSpaceDao getEventSpaceDao() {
		return eventSpaceDao;
	}

	public void setEventSpaceDao(EventSpaceDao eventSpaceDao) {
		this.eventSpaceDao = eventSpaceDao;
	}

	@Override
	public void createPrivateEvent(PrivateEventDTO dto) throws CohomanException {
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
						dto.getEventName(), 0L, session);
				if (conflictingEvent != null) {
					SimpleDateFormat formatter = new SimpleDateFormat(
							"EEE, MMM d, yyyy h:mm aa");
					String eventDateString = formatter.format(dto.getEventDate());

					throw new CohomanException(
							"Error: Event \""
									+ conflictingEvent
									+ " conflicts with your new event on "
									+ eventDateString
									+ ". Choose another time or reserved common space on that date.");
				}
			}
			PrivateEventBean privateEventBean = new PrivateEventBean(
					dto.getEventDate());
			privateEventBean.setEventdateend(dto.getEventdateend());
			dto.setEventtype(EventTypeDefs.RESERVEDSPACETYPE);
			privateEventBean.setEventtype(dto.getEventtype());
			privateEventBean.setEventinfo(dto.getEventinfo());
			privateEventBean.setEventName(dto.getEventName());
			privateEventBean.setCreatedate(dto.getCreatedate());
			privateEventBean.setRequester(dto.getRequester());
			privateEventBean.setExpectednumberofadults(dto
					.getExpectednumberofadults());
			privateEventBean.setExpectednumberofchildren(dto
					.getExpectednumberofchildren());
			privateEventBean.setOrganization(dto.getOrganization());
			privateEventBean.setIscohousingevent(dto.isIscohousingevent());
			privateEventBean.setIsinclusiveevent(dto.isIsinclusiveevent());
			privateEventBean.setIsexclusiveevent(dto.isIsexclusiveevent());
			privateEventBean.setIsincomeevent(dto.isIsincomeevent());
			privateEventBean.setAremajorityresidents(dto
					.isAremajorityresidents());
			privateEventBean.setDonation(dto.getDonation());
			privateEventBean.setIsphysicallyactiveevent(dto
					.isIsphysicallyactiveevent());
			privateEventBean.setIsonetimeparty(dto.isIsonetimeparty());
			privateEventBean.setIsclassorworkshop(dto.isIsclassorworkshop());
			privateEventBean.setState(dto.getState());
			privateEventBean.setRejectreason(dto.getRejectreason());
			privateEventBean.setApprovedby(dto.getApprovedby());
			privateEventBean.setApprovaldate(dto.getApprovaldate());

			session.save(privateEventBean);

			// Add one entry for each space chosen
			// NOTE: have to convert String to Long since JSF only
			// returns Strings
			for (String spaceBeanId : dto.getSpaceList()) {
				EventSpace eventSpace = new EventSpace();
				eventSpace.setEventid(privateEventBean.getEventid());
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

	public List<PrivateEvent> getAllPrivateEvents() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from PrivateEventBean order by eventdate");
		List<PrivateEvent> privateEvents = getPrivateEvents(session, query);
		session.flush();
		session.close();
		return privateEvents;
	}

	public List<PrivateEvent> getUpcomingPrivateEvents() {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery(
				"from PrivateEventBean where state = 'approved' AND " +
				"eventdate >= NOW() order by eventdate");
		List<PrivateEvent> privateEvents = getPrivateEvents(session, query);
		session.flush();
		session.close();
		return privateEvents;
	}

	public List<PrivateEvent> getPendingPrivateEvents() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from PrivateEventBean where state = 'requested' order by eventdate");
		List<PrivateEvent> privateEvents = getPrivateEvents(session, query);
		session.flush();
		session.close();
		return privateEvents;
	}

	// 7/29 Probably want to have a utility/service that return the current
	// userid
	public static final String SESSIONVAR_USER_NAME = "CCH_USER";

	public List<PrivateEvent> getMyPrivateEvents() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession httpSession = (HttpSession) ctx.getExternalContext()
				.getSession(true);
		User dbUser = (User) httpSession.getAttribute(SESSIONVAR_USER_NAME);
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from PrivateEventBean where requester = ? order by eventdate")
			.setString(0, Long.toString(dbUser.getUserid()));
		
		List<PrivateEvent> privateEvents = getPrivateEvents(session, query);
		session.flush();
		session.close();
		return privateEvents;
	}

	private List<PrivateEvent> getPrivateEvents(Session session, Query query) {
		List<PrivateEventBean> privateEventBeans = query.list();

		
		// TODO perhaps get rid of this copying by combining MealEvent
		// and MealEventBean so no conversion is needed.
		List<PrivateEvent> privateEvents = new ArrayList<PrivateEvent>();
		for (PrivateEventBean onebean : privateEventBeans) {

			PrivateEvent oneEvent = populatePrivateEventFromBean(onebean);
			// Get list of spaces for this event
			// Problem with string with spaces ...
			query = session.createQuery(
					"from EventSpace where eventid = ? and eventtype = '"
							+ onebean.getEventtype() + "'").setString(0,
					Long.toString(onebean.getEventid()));
			List<EventSpace> eventSpaceList = query.list();
			String printableSpaceList = "";
			boolean firsttime = true;
			for (EventSpace eventSpace : eventSpaceList) {
				query = session.createQuery("from SpaceBean where spaceId = ?")
						.setString(0, Long.toString(eventSpace.getSpaceId()));
				List<SpaceBean> spaceBeanListOfOne = query.list();
				if (firsttime) {
					printableSpaceList += spaceBeanListOfOne.get(0)
							.getSpaceName();
					firsttime = false;
				} else {
					printableSpaceList += ", "
							+ spaceBeanListOfOne.get(0).getSpaceName();
				}
			}
			oneEvent.setPrintableSpaceList(printableSpaceList);
			privateEvents.add(oneEvent);
		}
		//session.flush();
		//session.close();
		return privateEvents;
	}

	private PrivateEvent populatePrivateEventFromBean(PrivateEventBean onebean) {
	
		PrivateEvent oneEvent = new PrivateEvent(onebean.getEventDate());
		oneEvent.setEventid(onebean.getEventid());
		oneEvent.setEventtype(onebean.getEventtype());
		oneEvent.setEventdateend(onebean.getEventdateend());
		oneEvent.setEventinfo(onebean.getEventinfo());
		oneEvent.setEventName(onebean.getEventName());
		oneEvent.setCreatedate(onebean.getCreatedate());
		oneEvent.setRequester(onebean.getRequester());
		oneEvent.setExpectednumberofadults(onebean
				.getExpectednumberofadults());
		oneEvent.setExpectednumberofchildren(onebean
				.getExpectednumberofchildren());
		oneEvent.setOrganization(onebean.getOrganization());
		oneEvent.setIscohousingevent(onebean.isIscohousingevent());
		oneEvent.setIsinclusiveevent(onebean.isIsinclusiveevent());
		oneEvent.setIsexclusiveevent(onebean.isIsexclusiveevent());
		oneEvent.setIsincomeevent(onebean.isIsincomeevent());
		oneEvent.setAremajorityresidents(onebean.isAremajorityresidents());
		oneEvent.setDonation(onebean.getDonation());
		oneEvent.setIsphysicallyactiveevent(onebean
				.isIsphysicallyactiveevent());
		oneEvent.setIsonetimeparty(onebean.isIsonetimeparty());
		oneEvent.setIsclassorworkshop(onebean.isIsclassorworkshop());
		oneEvent.setState(onebean.getState());
		oneEvent.setRejectreason(onebean.getRejectreason());
		oneEvent.setApprovedby(onebean.getApprovedby());
		oneEvent.setApprovaldate(onebean.getApprovaldate());

		return oneEvent;
	}
	public List<PrivateEvent> getPrivateEventsForDay(Date dateOfDay) {

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
		String queryString = "from PrivateEventBean where date(eventdate) between :startdate and :enddate order by eventdate";
		Query query = session.createQuery(queryString)
				.setString("startdate", startdateString)
				.setString("enddate", enddateString);

		List<PrivateEvent> privateEvents = getPrivateEvents(session, query);
		session.flush();
		session.close();
		return privateEvents;

	}
	
	public PrivateEvent getPrivateEvent(Long eventId) {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		PrivateEvent oneEvent = null;
		try {
			tx = session.beginTransaction();

			String queryString = "from PrivateEventBean " + "where eventid = ?";
			Query query = session.createQuery(queryString).setLong(0, eventId);
			List<PrivateEventBean> privateEventBeans = query.list();
			if (privateEventBeans.size() != 1) {
				throw new RuntimeException("Expected just one bean but got "
						+ privateEventBeans.size());
			}
			PrivateEventBean onebean = privateEventBeans.iterator().next();
			oneEvent = new PrivateEvent(onebean.getEventDate());
			oneEvent.setEventid(onebean.getEventid());
			oneEvent.setEventtype(onebean.getEventtype());
			oneEvent.setEventdateend(onebean.getEventdateend());
			oneEvent.setEventinfo(onebean.getEventinfo());
			oneEvent.setEventName(onebean.getEventName());
			oneEvent.setCreatedate(onebean.getCreatedate());
			oneEvent.setRequester(onebean.getRequester());
			oneEvent.setExpectednumberofadults(onebean
					.getExpectednumberofadults());
			oneEvent.setExpectednumberofchildren(onebean
					.getExpectednumberofchildren());
			oneEvent.setOrganization(onebean.getOrganization());
			oneEvent.setIscohousingevent(onebean.isIscohousingevent());
			oneEvent.setIsinclusiveevent(onebean.isIsinclusiveevent());
			oneEvent.setIsexclusiveevent(onebean.isIsexclusiveevent());
			oneEvent.setIsincomeevent(onebean.isIsincomeevent());
			oneEvent.setAremajorityresidents(onebean.isAremajorityresidents());
			oneEvent.setDonation(onebean.getDonation());
			oneEvent.setIsphysicallyactiveevent(onebean
					.isIsphysicallyactiveevent());
			oneEvent.setIsonetimeparty(onebean.isIsonetimeparty());
			oneEvent.setIsclassorworkshop(onebean.isIsclassorworkshop());
			oneEvent.setState(onebean.getState());
			oneEvent.setRejectreason(onebean.getRejectreason());
			oneEvent.setApprovedby(onebean.getApprovedby());
			oneEvent.setApprovaldate(onebean.getApprovaldate());

			// Now separate query to get the spaces list
			queryString = "select t3.spaceName,t3.spaceId from privateevent t1"
					+ " join eventspace t2 on t1.eventid = t2.eventid and t1.eventtype = t2.eventtype"
					+ " join spaces t3 on t2.spaceId = t3.spaceId where t1.eventid = :eventId";
			query = session.createSQLQuery(queryString).setLong("eventId",
					oneEvent.getEventid());
			List<Object[]> result = query.list();
			Set<SpaceBean> spaceBeanSet = new LinkedHashSet();
			for (Object[] onespace : result) {
				SpaceBean oneSpaceBean = new SpaceBean();
				oneSpaceBean.setSpaceName((String) onespace[0]);
				oneSpaceBean.setSpaceId(((BigInteger) onespace[1]).longValue());
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

	public void updatePrivateEvent(PrivateEvent privateEvent)
			throws CohomanException {

		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();

			// First, make sure this updated event doesn't conflict with another
			// Important: base decision on newly checked boxes from page (vs.
			// existingly-held spaces)
			// BUT, only check if we're not doing this for a REJECTED request
			if (!privateEvent.getState().equals(EventStateEnums.REJECTED.name())){
				String conflictingEvent;
				// for (SpaceBean spaceBean : privateEvent.getSpaceList()) {
				for (String spaceId : privateEvent.getChosenSpaceListStringInts()) {

					conflictingEvent = DaoUtils.getSpaceForPeriod(
							privateEvent.getEventDate(),
							privateEvent.getEventdateend(),
							// Long.toString(spaceBean.getSpaceId()), session,
							// true);
							spaceId, privateEvent.getEventName(),
							privateEvent.getEventid(), session);
					if (conflictingEvent != null) {
						throw new CohomanException(
							"Error: Event \""
									+ conflictingEvent
									+ "\" conflicts with your updated event. Choose another time or reserved common space.");
					}
				}
			}

			String queryString = "from EventSpace where eventid = ? AND eventtype = ?";
			Query query = session.createQuery(queryString)
					.setLong(0, privateEvent.getEventid())
					.setString(1, privateEvent.getEventtype());
			List<EventSpace> eventspaces = query.list();

			// Start by deleting/clearing all existing already set spaces;
			// the idea is to next set only those spaces that are set in
			// the incoming page.
			for (EventSpace oneEventSpace : eventspaces) {
				EventSpace eventSpace = (EventSpace) session.load(
						EventSpace.class, oneEventSpace.getEventspaceid());
				session.delete(eventSpace);
			}

			// Create a new privateeventbean based on the values entered in
			// the UI
			PrivateEventBean privateEventBean = (PrivateEventBean) session
					.load(PrivateEventBean.class, privateEvent.getEventid());
			privateEventBean.setEventDate(privateEvent.getEventDate());
			privateEventBean.setEventdateend(privateEvent.getEventdateend());
			privateEventBean.setEventinfo(privateEvent.getEventinfo());
			privateEventBean.setEventName(privateEvent.getEventName());
			privateEventBean.setEventtype(privateEvent.getEventtype());

			privateEventBean.setCreatedate(privateEvent.getCreatedate());
			privateEventBean.setExpectednumberofadults(privateEvent
					.getExpectednumberofadults());
			privateEventBean.setExpectednumberofchildren(privateEvent
					.getExpectednumberofchildren());
			privateEventBean.setOrganization(privateEvent.getOrganization());
			privateEventBean.setIscohousingevent(privateEvent
					.isIscohousingevent());
			privateEventBean.setIsinclusiveevent(privateEvent
					.isIsinclusiveevent());
			privateEventBean.setIsexclusiveevent(privateEvent
					.isIsexclusiveevent());
			privateEventBean.setIsincomeevent(privateEvent.isIsincomeevent());
			privateEventBean.setAremajorityresidents(privateEvent
					.isAremajorityresidents());
			privateEventBean.setDonation(privateEvent.getDonation());
			privateEventBean.setIsphysicallyactiveevent(privateEvent
					.isIsphysicallyactiveevent());
			privateEventBean.setIsonetimeparty(privateEvent.isIsonetimeparty());
			privateEventBean.setIsclassorworkshop(privateEvent
					.isIsclassorworkshop());
			privateEventBean.setRequester(privateEvent.getRequester());
			privateEventBean.setState(privateEvent.getState());
			privateEventBean.setRejectreason(privateEvent.getRejectreason());
			privateEventBean.setApprovedby(privateEvent.getApprovedby());
			privateEventBean.setApprovaldate(privateEvent.getApprovaldate());

			// TODO don't couple spaceId with UI list of positions
			// For every checkbox checked, create a new eventspace bean
			for (String spaceId : privateEvent.getChosenSpaceListStringInts()) {
				EventSpace eventSpace = new EventSpace();
				eventSpace.setEventid(privateEvent.getEventid());
				eventSpace.setEventtype(privateEvent.getEventtype());
				eventSpace.setSpaceId(Long.valueOf(spaceId));
				session.saveOrUpdate(eventSpace);
			}

			// Lastly, do a merge with the privateEventBean
			session.merge(privateEventBean);
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

	public void deletePrivateEvent(Long privateEventId) {
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();
			PrivateEventBean privateEventBean = (PrivateEventBean) session
					.load(PrivateEventBean.class, privateEventId);

			// Get list of spaces for this event so we can delete them
			// Problem with string with spaces ...
			Query query = session.createQuery(
					"from EventSpace where eventid = ? and eventtype = '"
							+ privateEventBean.getEventtype() + "'").setString(
					0, Long.toString(privateEventBean.getEventid()));
			List<EventSpace> eventSpaceList = query.list();
			for (EventSpace eventSpace : eventSpaceList) {
				session.delete(eventSpace);
			}

			session.delete(privateEventBean);
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
