package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService   {
	private static Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    public JavaMailSender emailSender;
    
	@Value("${it.tndigit.emailservice.enablemock:false}")
	private boolean emailServiceMockEnabled;
    
	public void sendSimpleMessage(String to, String subject, String text) {
		if (emailServiceMockEnabled) {
			log.warn("Anagrafica: Il servizio di invio mail è falsato - tutte le mail sono inviate a {}" , to);
			to = "a4g-test@tndigit.it";
		}
		
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

	public void sendSimpleMessageElseThrow(String to, String subject, String text) {
		if (emailServiceMockEnabled) {
			log.warn("Anagrafica: Il servizio di invio mail è falsato - tutte le mail sono inviate a {}" , to);
			to = "a4g-test@tndigit.it";
		}
		
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
            throw exception;
        }
	}
	
	
}
