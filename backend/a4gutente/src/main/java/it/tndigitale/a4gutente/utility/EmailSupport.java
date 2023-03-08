package it.tndigitale.a4gutente.utility;

import it.tndigitale.a4g.framework.support.StringSupport;
import it.tndigitale.a4gutente.dto.Attachment;
import it.tndigitale.a4gutente.dto.DatiAnagrafici;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import javax.validation.ValidationException;
import java.util.List;

@Component
public class EmailSupport {

    public JavaMailSender emailSender;
    @Autowired
    private Environment environment;


    @Autowired
    public EmailSupport setEmailSender(JavaMailSender emailSender, Environment environmnent) {
        this.emailSender = emailSender;
        this.environment = environmnent;
        return this;
    }
    public void sendSimpleMessage(String to, String subject, String text) throws Exception {
        boolean isDevelop = environment != null && Boolean.parseBoolean(environment.getProperty("is.develop", "false"));
        if (isDevelop) {
            return;
        }

        if (StringSupport.isEmptyOrNull(to)) {
            throw new ValidationException("Inpossibile inviare la mail: Destinatario non specificato");
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendMessageWithAttachment(String to,
                                          String subject,
                                          String text,
                                          List<Attachment> attachments) throws Exception {
        if (StringSupport.isEmptyOrNull(to) || attachments == null || attachments.isEmpty()) {
            throw new ValidationException("Inpossibile inviare la mail con allegati: Destinatario non specificato oppure lista allegati vuota");
        }
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, Boolean.TRUE);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        addAttachmentoTo(helper, attachments);

        emailSender.send(message);
    }

    public static String getFirmaAppagMail() {
        return "\n\nUnità Informatizzazione e Sviluppo piattaforme Informatiche\n\n" +
                "PROVINCIA AUTONOMA DI TRENTO \n" +
                "AGENZIA PROVINCIALE PER I PAGAMENTI\n" +
                "IT - Via G.B. Trener, 3 - 38121 Trento\n" +
                "T. +39 0461 494909\n" +
                "F. +39 0461 495810\n" +
                "@ siap@provincia.tn.it\n" +
                "@ appag@provincia.tn.it";
    }
    
    public static String getTestoMail(DatiAnagrafici datiAnagrafici) {
    	return "Gentile " + datiAnagrafici.getNome() + ' ' + datiAnagrafici.getCognome() + ",\n" +
	            "come da richiesta si è provveduto ad abilitarla al Sistema Informativo A4G al quale potrà accedere nei seguenti modi:\n\n" +
    			"- Da smartphone attraverso il seguente link: https://myappag.it accedendo con CPS/CNS o con SPID\n\n" +
    			"- Da personal computer o tablet attraverso il seguente link: https://a4g.provincia.tn.it\n\n" +
	            "Accedendo “come cittadino” potrà scegliere di autenticarsi con CPS/CNS o con SPID.\n" +
	            "Se possiede un'utenza della Provincia Autonoma di Trento, potrà accedere “come dipendente”.\n" +
	            "Dopo l’autenticazione, nella sezione A4G - Nuovo Sistema Informativo Agricoltura, dovrà selezionare il profilo interessato per poter operare.\n" +
	            "Cordiali saluti.";
    }

    private void addAttachmentoTo(MimeMessageHelper helper, List<Attachment> attachments) throws Exception {
        for (Attachment attachment : attachments) {
            if (!Attachment.isValid(attachment)) {
                throw new ValidationException("Attachment non valido");
            }
            InputStreamSource source = new ByteArrayResource(attachment.getFile());
            helper.addAttachment(attachment.getFileName(), source);
        }
    }

}
