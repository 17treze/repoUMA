package it.tndigitale.a4gistruttoria.service.businesslogic.processo.istruttoria;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.istruttoria.InputProcessoIstruttorieDto;
import it.tndigitale.a4gistruttoria.dto.istruttoria.InputProcessoLiquidazioneIstruttorieDto;
import it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno.ControlloIntersostegnoService;
import it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione.LiquidazioneService;

@Component("LIQUIDAZIONE")
public class ElaboraLiquidazioneIstruttorie extends ElaboraIstruttoria {
	
	private static final Logger logger = LoggerFactory.getLogger(ElaboraLiquidazioneIstruttorie.class);

	@Autowired
	private ControlloIntersostegnoService service;
	@Autowired
	private LiquidazioneService liquidazioneService;

	@Override
	protected ControlloIntersostegnoService getElaborazioneIstruttoriaService() {
		return service;
	}

	@Override
	protected void elaborazioneTerminataConSuccesso(InputProcessoIstruttorieDto datiInputProcesso) throws Exception {
		
		InputProcessoLiquidazioneIstruttorieDto inputLiquidazione = (InputProcessoLiquidazioneIstruttorieDto)datiInputProcesso;
		Set<Long> istruttorieLiquidabili = caricaIstruttorieLiquidabili(inputLiquidazione);
		if (istruttorieLiquidabili != null && !istruttorieLiquidabili.isEmpty()) {
			logger.info("Trovate {} istruttorie liquidabili per anno {} e sostegno {}", istruttorieLiquidabili.size(), inputLiquidazione.getCampagna(), inputLiquidazione.getSostegno());
			// 1. Genero elenco
			liquidazioneService.liquidaIstruttorie(inputLiquidazione.getCampagna(), inputLiquidazione.getSostegno(), istruttorieLiquidabili);
		}
	}
	
	protected Set<Long> caricaIstruttorieLiquidabili(InputProcessoLiquidazioneIstruttorieDto datiInputProcesso) {
		// cerco tutte le istruttorie potenzialmente liquidabili, poi vanno filtrate con quelle in input
		Set<Long> istruttorieLiquidabili = liquidazioneService.caricaIdentificativiIstruttorieLiquidabili(datiInputProcesso.getCampagna(), datiInputProcesso.getSostegno());
		
		// interseco le liquidabili con quelle della richiesta
		if (istruttorieLiquidabili != null && !istruttorieLiquidabili.isEmpty()) {
			istruttorieLiquidabili = istruttorieLiquidabili.stream()
				.filter(istruttoria -> datiInputProcesso.getIdIstruttorie().contains(istruttoria))
				.collect(Collectors.toSet());
		}
		
		
		return istruttorieLiquidabili;
	}
}
