package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component(EmailTemplateRichiestaRevocaImmediataAccettataAppag.NOME_QUALIFICATORE)
public class EmailTemplateRichiestaRevocaImmediataAccettataAppag implements EmailTemplate {
	public static final String NOME_QUALIFICATORE = PREFISSO_NOME_QUALIFICATORE + "NOTIFICA_ACCETTA_REVOCA_IMMEDIATA_A_APPAG";
	private static final String OGGETTO = "Richiesta Revoca Immediata accettata";
	private static final String TESTO = "Il CAA %s ha accettato la richiesta di revoca immediata inviata dall’azienda %s in data %s. Il mandato \u00E8 non è più in carico al CAA %s";
	
	@Autowired
	private EmailService emailService;
	
	@Value("${valuta-revoca-immediata.email-dto.to}")
	private String destinatarioMail;
	
	@Override
	public void sendMail(String[] templateArgs) {
		sendMail(destinatarioMail, templateArgs);
	}

	@Override
	public void sendMail(String address, String[] templateArgs) {
		emailService.sendSimpleMessageElseThrow(
				address, 
				OGGETTO, 
				String.format(TESTO, templateArgs)
				);
	}
}
