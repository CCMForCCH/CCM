package org.cohoman.model.integration.persistence.beans;

public class EventTypeDefs {
	public static final String COMMONMEALTYPE = "common meal";
	public static final String PIZZAPOTLUCKTYPE = "pizza/potluck";
	public static final String POTLUCKTYPE = "potluck";
	public static final String GENERALMEETINGTYPE = "general mtg";
	public static final String COMMITTEEMEETINGTYPE = "committee mtg";
	public static final String SPECIALMEETINGTYPE = "special mtg";
	public static final String RESERVEDSPACETYPE = "Reserved space";
	public static final String COHOUSINGEVENTTYPE = "For cohousers ";
	public static final String INCLUSIVEEVENTTYPE = "Cohousers invited ";
	public static final String EXCLUSIVEEVENTTYPE = "Private ";
	public static final String INCOMEEVENTTYPE = "Income-producing ";
	public static final String PHYSICALLYACTIVEEVENTTYPE = "Physically-active ";
	public static final String AREMAJORITYRESIDENTSSUBTYPE = "Majority of participants are residents?";
	public static final String INCOMEDONATIONSUBTYPE = "Donation ($10 - $100)";

	public static final String AMAJORITYRESIDENTS = "A majority of participants are residents";
	public static final String AMAJORITYNOTRESIDENTS = "A majority of participants are not residents";
	public static final String ONETIMEPARTY = "Is a one-time party";
	public static final String NOTONETIMEPARTY = "Is not a one-time party";
	public static final String CLASSORWORKSHOP = "Is a class or workshop";
	public static final String NOTCLASSORWORKSHOP = "Is not a class or workshop";

	public static final String[] eventTypeChoicesNoApproval = {
			GENERALMEETINGTYPE,
			COMMITTEEMEETINGTYPE,
			SPECIALMEETINGTYPE
	};

	public static final String[] eventAttributes = {
		COHOUSINGEVENTTYPE,
		INCLUSIVEEVENTTYPE,
		EXCLUSIVEEVENTTYPE,
		INCOMEEVENTTYPE,
		PHYSICALLYACTIVEEVENTTYPE
	};

	public static final String[] incomeAttributes = {
		AREMAJORITYRESIDENTSSUBTYPE,
		INCOMEDONATIONSUBTYPE
	};

}

