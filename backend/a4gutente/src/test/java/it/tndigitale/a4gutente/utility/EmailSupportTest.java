package it.tndigitale.a4gutente.utility;

import it.tndigitale.a4gutente.dto.Attachment;
import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;
import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class EmailSupportTest {

    public JavaMailSender emailSender;
    private EmailSupport emailSupport;
    private Environment environment;
    private static final String APPAG_FIRMA = "\n\nUnità Informatizzazione e Sviluppo piattaforme Informatiche\n\n" +
            "PROVINCIA AUTONOMA DI TRENTO \n" +
            "AGENZIA PROVINCIALE PER I PAGAMENTI\n" +
            "IT - Via G.B. Trener, 3 - 38121 Trento\n" +
            "T. +39 0461 494909\n" +
            "F. +39 0461 495810\n" +
            "@ siap@provincia.tn.it\n" +
            "@ appag@provincia.tn.it";

    public EmailSupportTest() {
        emailSender = mock(JavaMailSender.class);
        environment = mock(Environment.class);

        emailSupport = new EmailSupport().setEmailSender(emailSender, environment);
    }

    @Test
    public void forSendSimpleMailIfToNullThenThrowing() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                emailSupport.sendSimpleMessage(null, "subject", "text"));
    }

    @Test
    public void forSendSimpleMailIfToNotNullThenSendMail() throws Exception {
        emailSupport.sendSimpleMessage("xxx@yyy.com", "subject", "text");

        verify(emailSender).send(aSimpleMailMessage());
    }

    @Test
    public void forSendMessageWithAttachmentIfToNullThenThrowing() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                emailSupport.sendMessageWithAttachment(null, "subject", "text", moreAttachments()));
    }

    @Test
    public void forSendMessageWithAttachmentIfAttachmentNullThenThrowing() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                emailSupport.sendMessageWithAttachment("xxx@yyy.com", "subject", "text", null));
    }


    @Test
    public void forSendMessageWithAttachmentIfAttachmentEmptyThenThrowing() throws Exception {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                emailSupport.sendMessageWithAttachment("xxx@yyy.com", "subject", "text", emptyList()));
    }

    @Test
    public void forSendMessageWithAttachmentIfAllIsSpecifiedThenSendMail() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailSupport.sendMessageWithAttachment("xxx@yyy.com", "subject", "text", moreAttachments());

        verify(emailSender).createMimeMessage();
        verify(emailSender).send(mimeMessage);
    }

    @Test
    public void forSendMessageWithAttachmentIfExistOneAttachmentNullThenThrowing() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                emailSupport.sendMessageWithAttachment("xxx@yyy.com", "subject", "text",
                                                           Collections.singletonList(null)));
        verify(emailSender).createMimeMessage();
    }

    @Test
    public void forSendMessageWithAttachmentIfExistOneAttachmentWithFileNullThenThrowing() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                emailSupport.sendMessageWithAttachment("xxx@yyy.com", "subject", "text",
                        Collections.singletonList(new Attachment().setFileName("xxxx.txt"))));
        verify(emailSender).createMimeMessage();
    }

    @Test
    public void forSendMessageWithAttachmentIfExistOneAttachmentWithFileNameNullThenThrowing() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                emailSupport.sendMessageWithAttachment("xxx@yyy.com", "subject", "text",
                        Collections.singletonList(new Attachment().setFile(new byte[10]).setFileName(null))));
        verify(emailSender).createMimeMessage();
    }

    @Test
    public void forSendMessageWithAttachmentIfExistOneAttachmentWithFileNameEmptyThenThrowing() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                emailSupport.sendMessageWithAttachment("xxx@yyy.com", "subject", "text",
                        Collections.singletonList(new Attachment().setFile(new byte[10]).setFileName(""))));
        verify(emailSender).createMimeMessage();
    }

    @Test
    public void forGetFirmaAppagMailItReturnMailSignOfAppag() {
        String sign = EmailSupport.getFirmaAppagMail();

        assertThat(sign).isEqualTo(APPAG_FIRMA);
    }

    private SimpleMailMessage aSimpleMailMessage() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setText("text");
        mailMessage.setSubject("subject");
        mailMessage.setTo("xxx@yyy.com");
        return mailMessage;
    }

    private List<Attachment> moreAttachments() {
        return Arrays.asList(new Attachment().setFileName("file1.txt").setFile(new byte[10]),
                             new Attachment().setFileName("file1.txt").setFile(new byte[10]));
    }

}
