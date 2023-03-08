package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component(EmailTemplateCambioSportello.NOME_QUALIFICATORE)
public class EmailTemplateCambioSportello implements EmailTemplate{

	public static final String NOME_QUALIFICATORE = PREFISSO_NOME_QUALIFICATORE + "CAMBIO_SPORTELLO";

	private static final String OGGETTO = "Modifica sede sportello CAA";
	private static final String TESTO = "Il fascicolo dell\u2019azienda %s, con mandato al %s, \u00E8 stato assegnato allo sportello %s in data %s con la seguente motivazione: %s";
	@Autowired
	private EmailService emailService;

	@Value("${cambio-sportello.email-dto.to}")
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
