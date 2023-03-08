package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component(EmailTemplateRichiestaRevocaImmediataRifiutataAppag.NOME_QUALIFICATORE)
public class EmailTemplateRichiestaRevocaImmediataRifiutataAppag implements EmailTemplate {

	public static final String NOME_QUALIFICATORE = PREFISSO_NOME_QUALIFICATORE + "NOTIFICA_RIFIUTA_REVOCA_IMMEDIATA_A_APPAG";

	private static final String OGGETTO = "Richiesta Revoca Immediata rifiutata";
	private static final String TESTO = "Il CAA %s ha rifiutato la richiesta di revoca immediata inviata dall’azienda %s in data %s con la seguente motivazione %s. Il mandato \u00E8 ancora in carico al CAA %s (attuale CAA che detiene mandato)";
	
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
