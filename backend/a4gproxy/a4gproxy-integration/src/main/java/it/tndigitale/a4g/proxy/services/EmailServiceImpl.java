package it.tndigitale.a4g.proxy.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {
	private static Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    public JavaMailSender emailSender;
    
	@Override
	public void sendSimpleMessage(String to, String subject, String text) {
        try {
        	log.info("Invio mail A {}, Soggetto {}, Messaggio {}",to,subject,text);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            //message.setFrom(to); //CONFIGURATO SU JBOSS
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        } catch (MailException exception) {
            log.error("Errore durante l'invio della mail",exception);
        }
	}

}
