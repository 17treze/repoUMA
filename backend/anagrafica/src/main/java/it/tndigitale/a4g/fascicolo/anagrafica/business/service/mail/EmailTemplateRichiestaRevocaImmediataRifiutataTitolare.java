package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component(EmailTemplateRichiestaRevocaImmediataRifiutataTitolare.NOME_QUALIFICATORE)
public class EmailTemplateRichiestaRevocaImmediataRifiutataTitolare implements EmailTemplate {
	public static final String NOME_QUALIFICATORE = PREFISSO_NOME_QUALIFICATORE + "NOTIFICA_RIFIUTA_REVOCA_IMMEDIATA_AL_TITOLARE";
	private static final String OGGETTO = "Richiesta Revoca Immediata rifiutata";
	private static final String TESTO = "Il CAA %s ha rifiutato la sua richiesta di revoca immediata in data %s. Il mandato \u00E8 ancora in carico al CAA %s (attuale CAA che detiene mandato)";	

	@Autowired
	private EmailService emailService;	

	@Override
	public void sendMail(String[] templateArgs) {
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
