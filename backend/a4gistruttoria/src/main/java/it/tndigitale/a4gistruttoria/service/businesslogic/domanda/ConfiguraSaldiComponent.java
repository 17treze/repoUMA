package it.tndigitale.a4gistruttoria.service.businesslogic.domanda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.dto.configurazioneistruttoria.ConfIstruttorieDto;
import it.tndigitale.a4gistruttoria.service.businesslogic.configurazioneistruttoria.ConfigurazioneIstruttoriaService;

@Component
public class ConfiguraSaldiComponent {
	
	private Logger logger = LoggerFactory.getLogger(ConfiguraSaldiComponent.class);

	@Autowired
	private ConfigurazioneIstruttoriaService confIstruttoriaService;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void configuraIstruttorie(int annoCorrente) {
		ConfIstruttorieDto configurazione =
				confIstruttoriaService.getConfIstruttorie(annoCorrente);
		if (configurazione == null) {
			logger.warn("Nessuna configurazione trovata per l'anno corrente {}", annoCorrente);
			return;
		}
		// configuro percentuale pagamento a 100%
		configurazione.setPercentualePagamento(1D);
		confIstruttoriaService.saveOrUpdateConfIstruttorie(configurazione);
	}
	
}
