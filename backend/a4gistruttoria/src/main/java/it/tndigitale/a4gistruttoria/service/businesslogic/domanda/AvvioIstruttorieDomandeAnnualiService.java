package it.tndigitale.a4gistruttoria.service.businesslogic.domanda;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneDomandaException;

@Service
public class AvvioIstruttorieDomandeAnnualiService {
	
	private Logger logger = LoggerFactory.getLogger(AvvioIstruttorieDomandeAnnualiService.class);

	@Autowired
	private IstruisciDomandaService istruisciDomandaService;
	@Autowired
	private ConfiguraSaldiComponent confIstruttoriaService;

	public void avvioSaldi(int annoCorrente) {
		confIstruttoriaService.configuraIstruttorie(annoCorrente);		
		avviaIstruttorie(annoCorrente);
	}
	
	protected void avviaIstruttorie(int annoCorrente) {
		try {
			List<Long> domande = istruisciDomandaService.caricaIdDaElaborare(annoCorrente);
			logger.info("Trovate {} domande da istruire per l'anno corrente {}", domande.size(), annoCorrente);
			for (Long idDomanda : domande) {
				try {
					istruisciDomandaService.elabora(idDomanda);
				} catch (ElaborazioneDomandaException e) {
					logger.error("Errore avviando le istruttorie per la domanda {}; proseguo le altre domande", idDomanda, e);
				}
			}
			logger.info("Terminato avvio istruttorie per {} domande", domande.size());
		} catch (ElaborazioneDomandaException e) {
			logger.error("Errore avviando le istruttorie per l'anno {}", annoCorrente, e);
		}
	}
	
}
