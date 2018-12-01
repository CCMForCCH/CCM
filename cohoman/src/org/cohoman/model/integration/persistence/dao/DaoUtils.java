package org.cohoman.model.integration.persistence.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public class DaoUtils {

	public static String getSpaceForPeriod(Date eventDate, Date eventdateend,
			String spaceId, String eventName, Long eventId, Session session) {

		String returnString = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
		String startdateString = formatter.format(eventDate);
		String enddateString = formatter.format(eventdateend);

		// Note: 04/24/16 make all comparisons using timestamp so granularity is
		// to the nearest minute (vs. seconds as is present in Date).
		
		// define query strings for Coho events and Private events (they differ
		// only in the table name)
		String cohoEventQueryString = "SELECT t1.eventtype, t1.eventid, t1.eventname " +
		"FROM cohoevent t1 " +
		"JOIN eventspace t2 ON t1.eventid = t2.eventid " +
		"AND t1.eventtype = t2.eventtype " +
	    "WHERE (timestamp(?) > timestamp(eventdate) AND " +
	    "timestamp(?) < timestamp(eventdateend) AND t2.spaceid = ?) " +
	    "OR (timestamp(?) > timestamp(eventdate) AND " +
	    "timestamp(?) < timestamp(eventdateend) AND t2.spaceid = ?) " +
	    "OR (timestamp(?) < timestamp(eventdate) AND " +
	    "timestamp(?) > timestamp(eventdateend) AND t2.spaceid = ?) " +
	    "OR (timestamp(?) = timestamp(eventdate) AND t2.spaceid = ?) " +
	    "OR (timestamp(?) = timestamp(eventdateend) AND t2.spaceid = ?) ";

		String privateEventQueryString = "SELECT t1.eventtype, t1.eventid, t1.eventname " +
		"FROM privateevent t1 " +
		"JOIN eventspace t2 ON t1.eventid = t2.eventid " +
		"AND t1.eventtype = t2.eventtype " +
	    "WHERE ((timestamp(?) > timestamp(eventdate) AND " +
	    "timestamp(?) < timestamp(eventdateend) AND t2.spaceid = ?) " +
	    "OR (timestamp(?) > timestamp(eventdate) AND " +
	    "timestamp(?) < timestamp(eventdateend) AND t2.spaceid = ?) " +
	    "OR (timestamp(?) < timestamp(eventdate) AND " +
	    "timestamp(?) > timestamp(eventdateend) AND t2.spaceid = ?) " +
	    "OR (timestamp(?) = timestamp(eventdate) AND t2.spaceid = ?) " +
	    "OR (timestamp(?) = timestamp(eventdateend) AND t2.spaceid = ?)) " +
	    "AND t1.state = 'APPROVED'";

		/*
		 *  Check first for any conflicting non-meal coho events
		 */
		Query query = session.createSQLQuery(cohoEventQueryString).
				setString(0, startdateString).setString(1, startdateString).setString(2, spaceId).
				setString(3, enddateString).setString(4, enddateString).setString(5, spaceId).
				setString(6, startdateString).setString(7, enddateString).setString(8, spaceId).
				setString(9, startdateString).setString(10, spaceId).
				setString(11, enddateString).setString(12, spaceId);
		
		List<Object[]> result = query.list();
		if (result.size() > 0) {
			
			// Found an entry already in this time slot.
			for (Object[] oneResult : result) {
				if (eventId != 0 && eventId ==((BigInteger)oneResult[1]).longValue()) {
					// skip over the very entry I'm updating
					continue;
				} else {
					// found conflicting entry: report it!
					returnString = (String)oneResult[0] +
						"(" + (String)oneResult[2] + ")";
					return returnString;
				}
			}
		}

		/*
		 *  Next check for any conflicting private events
		 */
		query = session.createSQLQuery(privateEventQueryString).
				setString(0, startdateString).setString(1, startdateString).setString(2, spaceId).
				setString(3, enddateString).setString(4, enddateString).setString(5, spaceId).
				setString(6, startdateString).setString(7, enddateString).setString(8, spaceId).
				setString(9, startdateString).setString(10, spaceId).
				setString(11, enddateString).setString(12, spaceId);
		
		result = query.list();
		if (result.size() > 0) {
			
			// Found an entry already in this time slot.
			for (Object[] oneResult : result) {
				if (eventId != 0 && eventId ==((BigInteger)oneResult[1]).longValue()) {
					// skip over the very entry I'm updating
					continue;
				} else {
					// found conflicting entry: report it!
					returnString = (String)oneResult[0] +
						"(" + (String)oneResult[2] + ")";
					return returnString;
				}
			}
		}

		
		/*
		 *  Next check for any conflicting common meal events
		 */
		// TODO shouldn't use hard-coded values
		if (spaceId.equals("1") || (spaceId.equals("2"))) {
		    String queryString = "SELECT eventtype, eventid " +
			    "FROM mealevent " +
			    "WHERE (timestamp(?) > timestamp(eventdate) AND " +
			    "timestamp(?) < timestamp(eventdateend)) " +
			    "OR (timestamp(?) > timestamp(eventdate) AND " +
			    "timestamp(?) < timestamp(eventdateend)) " +
		    	"OR (timestamp(?) < timestamp(eventdate) AND " +
		    	"timestamp(?) > timestamp(eventdateend)) " +
		    	"OR (timestamp(?) = timestamp(eventdate)) " +
		    	"OR (timestamp(?) = timestamp(eventdateend)) ";
		    query = session.createSQLQuery(queryString).
				setString(0, startdateString).setString(1, startdateString).
				setString(2, enddateString).setString(3, enddateString).
				setString(4, startdateString).setString(5, enddateString).
				setString(6, startdateString).
				setString(7, enddateString);
		
		    result = query.list();
			if (result.size() > 0) {
				
				// Found an entry already in this time slot.
				for (Object[] oneResult : result) {
					if (eventId != 0 && eventId ==((BigInteger)oneResult[1]).longValue()) {
						// skip over the very entry I'm updating
						continue;
					} else {
						// found conflicting entry: report it!
						returnString = (String)oneResult[0];
						return returnString;
					}
				}
			}
		}
	
		/*
		 *  Next, check for any conflicting pizza/potluck events
		 */
		if (spaceId.equals("1") || (spaceId.equals("2"))) {
		    String queryString = "SELECT eventtype, eventid " +
		    "FROM pizzaevent " +
		    "WHERE (timestamp(?) > timestamp(eventdate) AND " +
		    "timestamp(?) < timestamp(eventdateend)) " +
		    "OR (timestamp(?) > timestamp(eventdate) AND " +
		    "timestamp(?) < timestamp(eventdateend)) " +
	    	"OR (timestamp(?) < timestamp(eventdate) AND " +
	    	"timestamp(?) > timestamp(eventdateend)) " +
	    	"OR (timestamp(?) = timestamp(eventdate)) " +
	    	"OR (timestamp(?) = timestamp(eventdateend)) ";
	    query = session.createSQLQuery(queryString).
			setString(0, startdateString).setString(1, startdateString).
			setString(2, enddateString).setString(3, enddateString).
			setString(4, startdateString).setString(5, enddateString).
			setString(6, startdateString).
			setString(7, enddateString);

		    result = query.list();
			if (result.size() > 0) {
				
				// Found an entry already in this time slot.
				for (Object[] oneResult : result) {
					if (eventId != 0 && eventId ==((BigInteger)oneResult[1]).longValue()) {
						// skip over the very entry I'm updating
						continue;
					} else {
						// found conflicting entry: report it!
						returnString = (String)oneResult[0];
						return returnString;
					}
				}
			}
		}

		/*
		 *  Finally, check for any conflicting potluck events
		 */
		if (spaceId.equals("1") || (spaceId.equals("2"))) {
		    String queryString = "SELECT eventtype, eventid " +
		    "FROM potluckevent " +
		    "WHERE (timestamp(?) > timestamp(eventdate) AND " +
		    "timestamp(?) < timestamp(eventdateend)) " +
		    "OR (timestamp(?) > timestamp(eventdate) AND " +
		    "timestamp(?) < timestamp(eventdateend)) " +
	    	"OR (timestamp(?) < timestamp(eventdate) AND " +
	    	"timestamp(?) > timestamp(eventdateend)) " +
	    	"OR (timestamp(?) = timestamp(eventdate)) " +
	    	"OR (timestamp(?) = timestamp(eventdateend)) ";
	    query = session.createSQLQuery(queryString).
			setString(0, startdateString).setString(1, startdateString).
			setString(2, enddateString).setString(3, enddateString).
			setString(4, startdateString).setString(5, enddateString).
			setString(6, startdateString).
			setString(7, enddateString);

		    result = query.list();
			if (result.size() > 0) {
				
				// Found an entry already in this time slot.
				for (Object[] oneResult : result) {
					if (eventId != 0 && eventId ==((BigInteger)oneResult[1]).longValue()) {
						// skip over the very entry I'm updating
						continue;
					} else {
						// found conflicting entry: report it!
						returnString = (String)oneResult[0];
						return returnString;
					}
				}
			}
		}

        return returnString;
		
	}

}
