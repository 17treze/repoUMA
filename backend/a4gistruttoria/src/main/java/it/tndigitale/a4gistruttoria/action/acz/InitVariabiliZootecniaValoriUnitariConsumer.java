package it.tndigitale.a4gistruttoria.action.acz;

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
public class InitVariabiliZootecniaValoriUnitariConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {
	
	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliZootecniaValoriUnitariConsumer.class);
	
	private Map<CodiceInterventoAgs, TipoVariabile> mappaInterventoVariabile = new HashMap<>();

	@Autowired
	private ImportoUnitarioInterventoDao interventoPremiDao;

	public InitVariabiliZootecniaValoriUnitariConsumer() {
		super();
		initMappaInterventoVariabile();
	}

	@Override
	public void accept(CalcoloAccoppiatoHandler handler,IstruttoriaModel istruttoria) {
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
		mappaInterventoVariabile.put(CodiceInterventoAgs.LATTE, TipoVariabile.ACZVAL_310);
		mappaInterventoVariabile.put(CodiceInterventoAgs.LATTE_BMONT, TipoVariabile.ACZVAL_311);
		mappaInterventoVariabile.put(CodiceInterventoAgs.BOVINI_VAC, TipoVariabile.ACZVAL_313);
		mappaInterventoVariabile.put(CodiceInterventoAgs.BOVINI_VAC_NO_ISCR, TipoVariabile.ACZVAL_322);
		mappaInterventoVariabile.put(CodiceInterventoAgs.BOVINI_MAC, TipoVariabile.ACZVAL_315);
		mappaInterventoVariabile.put(CodiceInterventoAgs.BOVINI_MAC_12M, TipoVariabile.ACZVAL_316);
		mappaInterventoVariabile.put(CodiceInterventoAgs.BOVINI_MAC_ETIC, TipoVariabile.ACZVAL_318);
		mappaInterventoVariabile.put(CodiceInterventoAgs.OVICAP_AGN, TipoVariabile.ACZVAL_320);
		mappaInterventoVariabile.put(CodiceInterventoAgs.OVICAP_MAC, TipoVariabile.ACZVAL_321);
	}
}
