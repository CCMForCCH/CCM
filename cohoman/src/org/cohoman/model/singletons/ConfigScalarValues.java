package org.cohoman.model.singletons;

import java.util.logging.Logger;

import org.cohoman.model.integration.persistence.dao.ConfigScalarsDao;
import org.cohoman.view.controller.utils.ConfigScalarsEnums;

public class ConfigScalarValues {
	
	static Logger logger = Logger.getLogger("ConfigScalarValues");

	private  static ConfigScalarValues configScalarValues = null;
	public  static String twilio_account_sid = null;
	public  static String twilio_auth_token = null;
	public  static String twilio_sms_phone_number = null;
	public 	static String my_phone_number = null;
	public 	static String johnms_phone_number = null;
	public	static String ccmmailer_pw = null;
	
	private static ConfigScalarsDao configScalarsDao = null;

	public ConfigScalarsDao getConfigScalarsDao() {
		return configScalarsDao;
	}

	public void setConfigScalarsDao(ConfigScalarsDao configScalarsDao) {
		ConfigScalarValues.configScalarsDao = configScalarsDao;
	}

	private ConfigScalarValues() {
	}
	
	public static ConfigScalarValues getInstance() {

		if (configScalarValues == null) {
			configScalarValues = new ConfigScalarValues();
		}
		
		// load sensitive values from the database 
		twilio_account_sid = configScalarsDao.getConfigScalarValue(ConfigScalarsEnums.TWILIO_ACCOUNT_SID.name());
		twilio_auth_token = configScalarsDao.getConfigScalarValue(ConfigScalarsEnums.TWILIO_AUTH_TOKEN.name());
		twilio_sms_phone_number = configScalarsDao.getConfigScalarValue(ConfigScalarsEnums.TWILIO_SMS_PHONE_NUMBER.name());
		my_phone_number = configScalarsDao.getConfigScalarValue(ConfigScalarsEnums.MY_PHONE_NUMBER.name());
		johnms_phone_number = configScalarsDao.getConfigScalarValue(ConfigScalarsEnums.JOHNMS_PHONE_NUMBER.name());
		ccmmailer_pw = configScalarsDao.getConfigScalarValue(ConfigScalarsEnums.CCMMAILER_PW.name());

		logger.info("Value of twilio account sid is " + twilio_account_sid);		
		logger.info("Value of twilio auth token is " + twilio_auth_token);
		logger.info("Value of twilio text phone number is " + twilio_sms_phone_number);
		logger.info("Value of my phone number is " + my_phone_number);
		logger.info("Value of johnm's phone number is " + johnms_phone_number);
		logger.info("Value of ccm mailer password is " + ccmmailer_pw);
		
		return configScalarValues;
	}

}
