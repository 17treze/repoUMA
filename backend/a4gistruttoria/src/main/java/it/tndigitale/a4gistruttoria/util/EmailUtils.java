package it.tndigitale.a4gistruttoria.util;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
public class EmailUtils {
	
	private static Logger log = LoggerFactory.getLogger(EmailUtils.class);

    @Autowired
    public JavaMailSender emailSender;
    
    @Autowired
    private Environment environment;
    
	public void sendSimpleMessage(String to, String subject, String text) {
        try {
        	log.debug("Invio mail A {}, Soggetto {}, Messaggio {}",to,subject,text);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            //message.setFrom(to); //CONFIGURATO SU JBOSS
            StringBuilder oggetto=new StringBuilder();
            oggetto.append("[");
            if (this.environment.getActiveProfiles()!=null && this.environment.getActiveProfiles().length>0) {
            	 oggetto.append(this.environment.getActiveProfiles()[0].toUpperCase());
            }
            oggetto.append("]").
            		append(" - ").
            		append(subject);
            message.setSubject(oggetto.toString());
            message.setText(text);
            emailSender.send(message);
        } catch (MailException exception) {
            log.error("Errore durante l'invio della mail",exception);
        }
	}

	public void sendMessageWithAttachment(String to, String subject, String fileName, byte[] file) {
        try {
			    MimeMessage message = emailSender.createMimeMessage();
			      
			    MimeMessageHelper helper = new MimeMessageHelper(message, true);
			     
			    helper.setTo(to);
			    helper.setSubject(subject);
			    helper.setText(subject);
			    InputStreamSource source = new  ByteArrayResource(file);
			    helper.addAttachment(fileName, source);
			 
			    emailSender.send(message);
			} catch (Exception exception) {
			    log.error("Errore durante l'invio della mail",exception);
			}
	}
}
