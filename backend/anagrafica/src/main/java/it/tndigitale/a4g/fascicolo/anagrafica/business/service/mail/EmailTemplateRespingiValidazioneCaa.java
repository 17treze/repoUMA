package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component(EmailTemplateRespingiValidazioneCaa.NOME_QUALIFICATORE)
public class EmailTemplateRespingiValidazioneCaa implements EmailTemplate {
	public static final String NOME_QUALIFICATORE = PREFISSO_NOME_QUALIFICATORE + "NOTIFICA_RESPINTA_VALIDAZIONE_CAA";
	private static final String OGGETTO = "Richiesta di ulteriore revisione del fascicolo di %s";
	private static final String TESTO = "Il Titolare rappresentante %s %s chiede di rivedere ancora il fascicolo %s con CUAA %s.";	

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
	
	@Override
	public void sendMail(String address, String[] oggettoArgs, String[] templateArgs ) {
		emailService.sendSimpleMessage(
				address, 
				String.format(OGGETTO, oggettoArgs), 
				String.format(TESTO, templateArgs)
				);
		
	}
}
