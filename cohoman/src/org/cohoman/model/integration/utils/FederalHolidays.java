package org.cohoman.model.integration.utils;

	import java.util.Calendar;
	import static java.util.Calendar.*;
	import java.util.Date;
	import java.util.Locale;
	import java.util.SortedSet;
	import java.util.TimeZone;
	import java.util.TreeSet;

	/**
	 * For a holiday that falls on Saturday, the Federal holiday will be observed the previous Friday. If it falls on Sunday, it will
	 * be observed the following Monday.
	 */
	public class FederalHolidays {

	    /**
	     * The list of Federal Observances, as per section 6103(a) of title 5 of the United States Code.
	     *
	     * @see http://www.law.cornell.edu/uscode/text/5/6103
	     */
	    public enum Observance {

	        /**
	         * January 1st.
	         */
	        NEW_YEARS_DAY(JANUARY, 1),
	        /**
	         * Third Monday in January.
	         */
	        BIRTHDAY_OF_MARTIN_LUTHER_KING_JR(JANUARY, MONDAY, 3),
	        /**
	         * Third Monday in February.
	         */
	        WASHINGTONS_BIRTHDAY(FEBRUARY, MONDAY, 3),
	        /**
	         * Third Monday in April. Added by wsh for MA 11/24/2019
	         */
	        PATRIOTS_DAY(APRIL, MONDAY, 3),
	        /**
	         * Last Monday in May.
	         */
	        MEMORIAL_DAY(MAY, MONDAY, -1),
	        /**
	         * July 4th.
	         */
	        INDEPENDENCE_DAY(JULY, 4),
	        /**
	         * First Monday in September.
	         */
	        LABOR_DAY(SEPTEMBER, MONDAY, 1),
	        /**
	         * Second Monday in October.
	         */
	        COLUMBUS_DAY(OCTOBER, MONDAY, 2),
	        /**
	         * November 11th.
	         */
	        VETERANS_DAY(NOVEMBER, 11),
	        /**
	         * Fourth Thursday in November.
	         */
	        THANKSGIVING_DAY(NOVEMBER, THURSDAY, 4),
	        /**
	         * December 25th.
	         */
	        CHIRSTMAS_DAY(DECEMBER, 25);

	        private final int month;
	        private final int dayOfMonth;
	        private final int dayOfWeek;
	        private final int weekOfMonth;
	        private static final int NA = 0;

	        private Observance(int month, int dayOfMonth) {
	            this.month = month;
	            this.dayOfMonth = dayOfMonth;
	            this.dayOfWeek = NA;
	            this.weekOfMonth = NA;
	        }

	        private Observance(int month, int dayOfWeek, int weekOfMonth) {
	            this.month = month;
	            this.dayOfMonth = NA;
	            this.dayOfWeek = dayOfWeek;
	            this.weekOfMonth = weekOfMonth;
	        }

	        /**
	         * Returns true if this observance is a fixed date, e.g. December 25th or January 1st.
	         * Non-fixed dates are those that are on a particular day of week and week of the month, e.g. 3rd Thursday in November.
	         */
	        boolean isFixedDate() {
	            return dayOfMonth != NA;
	        }
	    }

	    /**
	     * Note, it is possible for the New Year's Day observance to be in the year previous to the one provided. For example, New
	     * Years 2011 was observed on December 31st, 2010.
	     *
	     * @param observance
	     * @param year
	     * @return
	     */
	    public Date dateOf(Observance observance, int year) {
	        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("EST"), Locale.ENGLISH);
	        cal.set(YEAR, year);
	        cal.set(MONTH, observance.month);
	        cal.clear(HOUR);
	        if (observance.isFixedDate()) {
	            cal.set(DAY_OF_MONTH, observance.dayOfMonth);
	        } else {
	            setNthDayOfWeek(cal, observance.dayOfWeek, observance.weekOfMonth);
	        }
	        adjustForWeekendsIfNecessary(cal);
	        return cal.getTime();
	    }

	    public boolean isDateAHoliday(Date workingDate) {
	    	
	    	// Get year from Working Date
			Calendar workingCal = Calendar.getInstance();
			workingCal.setTime(workingDate);
			int workingYear = workingCal.get(Calendar.YEAR);
			
			// Walk through all known holidays to see if given date is 
			// actually a holiday
			for (Observance observance : Observance.values()) {
				Calendar holidayCal = Calendar.getInstance();
				holidayCal.setTime(dateOf(observance, workingYear));

				// Check each holiday's date to see if it matches given date
				if ((holidayCal.get(Calendar.YEAR) == workingCal.get(Calendar.YEAR)) &&
						(holidayCal.get(Calendar.MONTH) == workingCal.get(Calendar.MONTH)) &&
						(holidayCal.get(Calendar.DAY_OF_MONTH) == workingCal.get(Calendar.DAY_OF_MONTH))) {
					// match found!
					return true;					
				}
			}
			
			// No match found. Not a holiday.
			return false;
	    }
	    
	    public boolean isNextDayAHoliday(Date workingDate) {
	    	
	    	Calendar workingCal = Calendar.getInstance();
	    	workingCal.setTime(workingDate);
	    	workingCal.add(Calendar.DAY_OF_YEAR, 1);
	    	return isDateAHoliday(workingCal.getTime());
	    	
	    }
	    
	    /**
	     * Mutates the given calendar; sets the date so that it corresponds to the nth occurrence of the given day of the week.
	     *
	     * @param cal       - the calendar to set the date for
	     * @param dayOfWeek - based on the values of Calendar, e.g. Calendar.MONDAY
	     * @param n         - the occurrences of the dayOfWeek to set. Can be a value of 1 to 5 corresponding to the 1st through 5th
	     *                  occurrence. If -1, it means "last" occurrence.
	     */
	    private void setNthDayOfWeek(Calendar cal, int dayOfWeek, int n) {
	        int week = 0;
	        int lastDay = cal.getActualMaximum(DAY_OF_MONTH);
	        // if negative, we loop backwards and count the weeks backwards; if positive, count forward
	        int startDay = n > 0 ? 1 : lastDay;
	        int endDay = n > 0 ? lastDay : 1;
	        int incrementValue = n > 0 ? 1 : -1;
	        for (int day = startDay; day != endDay; day += incrementValue) {
	            cal.set(DAY_OF_MONTH, day);
	            if (cal.get(DAY_OF_WEEK) == dayOfWeek) {
	                week += incrementValue;
	                if (week == n) {
	                    return;
	                }
	            }
	        }
	    }
	    
	    /**
	     * If the calendar is on a Saturday, adjust the date back a day. If on Sunday, move it forward one day. If neither, leave
	     * the date unchanged. See Executive order 11582, February 11, 1971.
	     *
	     * @param cal - mutated if a weekend date
	     */
	    private void adjustForWeekendsIfNecessary(Calendar cal) {
	        int dayOfWeek = cal.get(DAY_OF_WEEK);
	        cal.add(DAY_OF_MONTH, dayOfWeek == SATURDAY ? -1 : dayOfWeek == SUNDAY ? 1 : 0);
	    }

}
