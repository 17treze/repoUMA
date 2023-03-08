package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component(EmailTemplateRevocaOrdinariaMandato.NOME_QUALIFICATORE)
public class EmailTemplateRevocaOrdinariaMandato implements EmailTemplate {

	public static final String NOME_QUALIFICATORE = PREFISSO_NOME_QUALIFICATORE + "REVOCA_ORDINARIA_MANDATO";

	private static final String OGGETTO = "Revoca Ordinaria Mandato";
	private static final String TESTO = "Buongiorno, vi informiamo che a partire dal %s il mandato dell\u2019azienda %s - %s non sar\u00E0 pi\u00F9 in carico al %s perch\u00E8 il mandato \u00E8 stato revocato con revoca ordinaria.";

	@Autowired
	private EmailService emailService;

	@Value("${revoca-ordinaria-mandato.email-dto.to}")
	private String destinatarioMail;

	@Override
	public void sendMail(String[] templateArgs) {
		sendMail(destinatarioMail, templateArgs);
	}

	@Override
	public void sendMail(String address, String[] templateArgs) {
		emailService.sendSimpleMessage(
				address, 
				OGGETTO, 
				String.format(TESTO, templateArgs)
				);
	}
}
