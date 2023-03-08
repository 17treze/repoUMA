package it.tndigitale.a4gistruttoria.action.acs;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class CalcoloOutputSuperficiRichiesteConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputSuperficiRichiesteConsumer.class);

	/**
	 * Mappa "statica" che collega gli interventi da gestire con le variabili da inizializzare
	 */
	private Map<TipoVariabile, TipoVariabile> mappaInputOutput = new HashMap<TipoVariabile, TipoVariabile>();
	
	
	
	public CalcoloOutputSuperficiRichiesteConsumer() {
		super();
		initMappaInterventoVariabile();
	}


	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		logger.debug("accept: inizio per la domanda {}", domanda.getId());		
		MapVariabili variabiliOutput = handler.getVariabiliOutput();
		MapVariabili variabiliInput = handler.getVariabiliInput();
		BigDecimal totaleSupRichiesta = BigDecimal.ZERO;
		BigDecimal currSupRichiesta = null;
		for (TipoVariabile varOutput : mappaInputOutput.keySet()) {
			VariabileCalcolo vc = variabiliInput.get(mappaInputOutput.get(varOutput));
			if (vc != null) {
				currSupRichiesta = vc.getValNumber();
				if (currSupRichiesta != null) {
					totaleSupRichiesta = totaleSupRichiesta.add(currSupRichiesta);
					variabiliOutput.add(new VariabileCalcolo(varOutput, currSupRichiesta));	
				}
			}
		}
		// Le superfici sono mq/1000
		variabiliOutput.add(new VariabileCalcolo(TipoVariabile.ACSSUPRICTOT, totaleSupRichiesta));
		logger.debug("accept: fine per la domanda {} con totale {}", domanda.getId(), totaleSupRichiesta);		
	}


	protected void initMappaInterventoVariabile() {
		mappaInputOutput.put(TipoVariabile.ACSSUPRIC_M8, TipoVariabile.ACSSUPIMP_M8);
		mappaInputOutput.put(TipoVariabile.ACSSUPRIC_M9, TipoVariabile.ACSSUPIMP_M9);
		mappaInputOutput.put(TipoVariabile.ACSSUPRIC_M10, TipoVariabile.ACSSUPIMP_M10);
		mappaInputOutput.put(TipoVariabile.ACSSUPRIC_M11, TipoVariabile.ACSSUPIMP_M11);
		mappaInputOutput.put(TipoVariabile.ACSSUPRIC_M14, TipoVariabile.ACSSUPIMP_M14);
		mappaInputOutput.put(TipoVariabile.ACSSUPRIC_M15, TipoVariabile.ACSSUPIMP_M15);
		mappaInputOutput.put(TipoVariabile.ACSSUPRIC_M16, TipoVariabile.ACSSUPIMP_M16);
		mappaInputOutput.put(TipoVariabile.ACSSUPRIC_M17, TipoVariabile.ACSSUPIMP_M17);
	}
}
