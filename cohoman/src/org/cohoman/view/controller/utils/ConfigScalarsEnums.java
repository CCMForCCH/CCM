package org.cohoman.view.controller.utils;

public enum ConfigScalarsEnums {
	
	TWILIO_ACCOUNT_SID("TWILIO_ACCOUNT_SID"), TWILIO_AUTH_TOKEN("TWILIO_AUTH_TOKEN"), TWILIO_SMS_PHONE_NUMBER("TWILIO_SMS_PHONE_NUMBER");
	
    private final String text;

    private ConfigScalarsEnums(final String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return text;
    }

}
