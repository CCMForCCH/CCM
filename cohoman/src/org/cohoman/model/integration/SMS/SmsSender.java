package org.cohoman.model.integration.SMS;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.cohoman.model.integration.utils.LoggingUtils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {

	private final String ACCOUNT_SID = "ACed28b09501481b9ede347f18787800ac";
	private final String AUTH_TOKEN = "69e563300f61f5b1f3625024db10d802";
	private final String MY_TEXT_PHONE_NUMBER = "7812062364";
	Logger logger = Logger.getLogger(this.getClass().getName());

	public void sendtextMessage(String phoneNumber, String textMessage) {

		Message message = null;

		try {
			Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

			logger.info("AUDIT: Sending a text message to phone number "
					+ phoneNumber + " with message \"" + textMessage + "\".");

			message = Message.creator(new PhoneNumber("+1" + phoneNumber), // to
					new PhoneNumber("+1" + MY_TEXT_PHONE_NUMBER), // from
					textMessage).create();
			if (message != null && message.getErrorMessage() != null) {
				logger.log(Level.SEVERE, message.getErrorMessage());
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, LoggingUtils.displayExceptionInfo(ex));
		}

	}

}
