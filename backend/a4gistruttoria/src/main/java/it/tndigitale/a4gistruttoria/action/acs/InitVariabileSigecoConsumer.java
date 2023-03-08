package it.tndigitale.a4gistruttoria.action.acs;

import java.util.function.BiConsumer;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaACS;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.service.DatiIstruttoreService;
import it.tndigitale.a4gistruttoria.service.DomandeService;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class InitVariabileSigecoConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	@Autowired
	private DomandeService serviceDomande;
	@Autowired
	private DatiIstruttoreService datiIstruttoreService;

	private static final Logger logger = LoggerFactory.getLogger(InitVariabileSigecoConsumer.class);

	public static final TipoVariabile DOMSIGECOCHIUSA = TipoVariabile.DOMSIGECOCHIUSA;

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		try {
			logger.debug("Carico le informazioni sigeco per la domanda {}", domanda.getId());
			// Domanda chiusa in SIGECO
			boolean esitoSigeco = serviceDomande.recuperaEsitoSigeco(domanda.getNumeroDomanda());

			DatiIstruttoriaACS datiIstruttoriaACS = datiIstruttoreService.getDatiIstruttoreSuperficie(istruttoria.getId());
			handler.getVariabiliInput()
					.add(new VariabileCalcolo(DOMSIGECOCHIUSA, esitoSigeco && (datiIstruttoriaACS != null
							&& Boolean.TRUE.equals(datiIstruttoriaACS.getControlloSigecoLoco()))));
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			logger.debug("Errore recuperando i dati di istruttoria acs per la domanda " + domanda.getId(), e);
			throw new RuntimeException(e);
		}
	}

}
