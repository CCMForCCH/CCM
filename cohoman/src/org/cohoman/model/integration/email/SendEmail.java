package org.cohoman.model.integration.email;

import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.cohoman.model.singletons.ConfigScalarValues;

public class SendEmail {

	static Logger logger = Logger.getLogger("SendEmail");

	public synchronized static void sendEmailToAddress(String recepient,
			String subject, String body) {

		String from = "ccmmailer@cambridgecohousing.net";
		String host = "mail.cambridgecohousing.net";

		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", 25);
		properties.put("mail.smtp.auth", "true");

		properties.put("mail.smtps.host", host);
		properties.put("mail.smtps.port", 587);
		properties.put("mail.smtps.auth", "true");
		properties.put("mail.smtps.starttls.enable", "true");
		// properties.put("mail.debug", "true");

		// Get the Session object and pass user name and password
		Session session = Session.getInstance(properties,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								"ccmmailer@cambridgecohousing.net",
								ConfigScalarValues.ccmmailer_pw);
					}
				});

		//session.setDebug(true);

		try {
			// Create a default MimeMessage object
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					recepient));

			// Set Subject: header field
			message.setSubject(subject);

			// Now set the actual message
			message.setText(body);

			// Send the message
			logger.info("Sending the email message to " + recepient);
			Transport.send(message);
			logger.info("Message sent successfully to " + recepient
					+ " , subject is " + subject + ", body is " + body);
		} catch (MessagingException mex) {
			logger.severe(mex.toString());
		}
	}

}
