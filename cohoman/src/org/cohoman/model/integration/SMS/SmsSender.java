package org.cohoman.model.integration.SMS;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.integration.utils.LoggingUtils;
import org.cohoman.model.singletons.ConfigScalarValues;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {

	static Logger logger = Logger.getLogger("SmsSender");

	public synchronized static void sendtextMessage(String phoneNumber, String textMessage) {

		Message message = null;

		try {

			Twilio.init(ConfigScalarValues.twilio_account_sid,
					ConfigScalarValues.twilio_auth_token);

			logger.info("AUDIT: Sending a text message to phone number "
					+ phoneNumber + " with message \"" + textMessage + "\".");

			message = Message.creator(
					new PhoneNumber("+1" + phoneNumber), // to
					new PhoneNumber("+1"
							+ ConfigScalarValues.twilio_sms_phone_number), // from
					textMessage).create();
			if (message != null && message.getErrorMessage() != null) {
				logger.log(Level.SEVERE, message.getErrorMessage());
			}
			
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
		}

	}

}
