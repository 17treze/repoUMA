package it.tndigitale.a4gistruttoria.action.acs;

import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.ImportoUnitarioInterventoDao;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.ImportoUnitarioInterventoModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Component
public class InitVariabiliSuperficiValoriUnitariConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliSuperficiValoriUnitariConsumer.class);

	/**
	 * Mappa "statica" che collega gli interventi da gestire con le variabili da inizializzare
	 */
	private Map<CodiceInterventoAgs, TipoVariabile> mappaInterventoVariabile = new HashMap<CodiceInterventoAgs, TipoVariabile>();

	@Autowired
	private ImportoUnitarioInterventoDao interventoPremiDao;

	public InitVariabiliSuperficiValoriUnitariConsumer() {
		super();
		initMappaInterventoVariabile();
	}

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		logger.debug("Carico i valori unitari per la domanda {}", domanda.getId());
		// valori da recuperare da dati inseriti in cruscotto dall'istruttore
		for (CodiceInterventoAgs codiceIntervento : mappaInterventoVariabile.keySet()) {
			ImportoUnitarioInterventoModel premio = premio(domanda, codiceIntervento);
			if (premio != null) {
				handler.getVariabiliInput().add(new VariabileCalcolo(mappaInterventoVariabile.get(codiceIntervento), premio.getValoreUnitario()));
			}
		}
	}

	protected ImportoUnitarioInterventoModel premio(DomandaUnicaModel domanda, CodiceInterventoAgs codiceIntervento) {
		return interventoPremiDao.findByCampagnaAndIntervento_identificativoIntervento(
				domanda.getCampagna(), codiceIntervento);
	}

	protected void initMappaInterventoVariabile() {
		mappaInterventoVariabile.put(CodiceInterventoAgs.SOIA, TipoVariabile.ACSVAL_M8);
		mappaInterventoVariabile.put(CodiceInterventoAgs.GDURO, TipoVariabile.ACSVAL_M9);
		mappaInterventoVariabile.put(CodiceInterventoAgs.CPROT, TipoVariabile.ACSVAL_M10);
		mappaInterventoVariabile.put(CodiceInterventoAgs.LEGUMIN, TipoVariabile.ACSVAL_M11);
		mappaInterventoVariabile.put(CodiceInterventoAgs.POMOD, TipoVariabile.ACSVAL_M14);
		mappaInterventoVariabile.put(CodiceInterventoAgs.OLIO, TipoVariabile.ACSVAL_M15);
		mappaInterventoVariabile.put(CodiceInterventoAgs.OLIVE_PEND75, TipoVariabile.ACSVAL_M16);
		mappaInterventoVariabile.put(CodiceInterventoAgs.OLIVE_DISC, TipoVariabile.ACSVAL_M17);
	}
}
