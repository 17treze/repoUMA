/**
 * 
 */
package it.tndigitale.a4gistruttoria.strategy.processi;

import it.tndigitale.a4gistruttoria.dto.DatiElaborazioneProcesso;
import it.tndigitale.a4gistruttoria.dto.ProcessoAnnoCampagnaDomandaDto;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;
import it.tndigitale.a4gistruttoria.service.DomandeService;
import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author S.DeLuca
 *
 */
@Component(/*"AVVIO_ISTRUTTORIA"*/)
class ProcessoStrategyIstruttoria implements ProcessoStrategy {

	private static final Logger logger = LoggerFactory.getLogger(ProcessoStrategyIstruttoria.class);
	
	@Autowired
	private DomandeService domandaService;
	
	@Autowired
	private DomandaUnicaDao daoDomanda;
	
	@Autowired
	private ProcessoSyncronizeStatus sync;

	@Override
	public void avvia(ProcessoAnnoCampagnaDomandaDto processoDomanda) {
		logger.debug("ProcessoStrategyInputIstruttoria2019 starts");
		DatiElaborazioneProcesso datiProcesso = new DatiElaborazioneProcesso();
		ArrayList<String> domandeGestite = new ArrayList<>();
		ArrayList<String> domandeConProblemi = new ArrayList<>();
		try {
			// Recupero la lista di domande ricevibili nel sistema
			List<DomandaUnicaModel> domandeRicevibili = daoDomanda.findByCampagnaAndStatoNotPiccoliAgricoltori(
					processoDomanda.getCampagna(), StatoDomanda.RICEVIBILE);
			BigDecimal numeroDomande = BigDecimal.ZERO; //processoDomanda.getNumeroDomandeDaElaborare();
			if (numeroDomande != null && domandeRicevibili.size() > numeroDomande.intValue()) {
				domandeRicevibili = domandeRicevibili.subList(0, numeroDomande.intValue());
			} else if (numeroDomande == null && !domandeRicevibili.isEmpty()) {
				numeroDomande = BigDecimal.valueOf(domandeRicevibili.size());
			}
			// Betty correzione del 25/09/2019: evitiamo NullPointerException (caso in cui ho ricevibili solo domande di piccoli)
			if (numeroDomande == null) {
				numeroDomande = BigDecimal.ZERO;
			}

			datiProcesso.setTotale("" + numeroDomande);
			sync.aggiornaProcesso(processoDomanda.getIdProcesso(), 0, StatoProcesso.RUN, datiProcesso);

			// Limito le domande al numero impostato come parametro
			// Aggiorno lo stato della domanda ed aggiorna lo stato del processo ogni 50
			// domande
			for (int i = 0; i < numeroDomande.intValue(); i++) {
				DomandaUnicaModel d = domandeRicevibili.get(i);
				StringBuilder dettaglioErrore = new StringBuilder();
				try {
					domandaService.elaboraDomandaPerIstruttoria(d, dettaglioErrore);
					domandeGestite.add(d.getNumeroDomanda().toString());
					datiProcesso.setGestite(domandeGestite);
				} catch (Exception e) {
					if (dettaglioErrore.length() > 0) {
						logger.error(dettaglioErrore.toString(), e);
						dettaglioErrore.append(": ");
					}

					domandeConProblemi.add(dettaglioErrore.toString() + d.getNumeroDomanda().toString());
					datiProcesso.setConProblemi(domandeConProblemi);
				}
				if ((i % 50) == 0) {
					int avanzamento = (i * 100) / numeroDomande.intValue();
					datiProcesso.setParziale(String.valueOf(i));
					sync.aggiornaProcesso(processoDomanda.getIdProcesso(), avanzamento, StatoProcesso.RUN,
							datiProcesso);
				}
			}
			datiProcesso.setParziale(datiProcesso.getTotale());
			sync.aggiornaProcesso(processoDomanda.getIdProcesso(), 100, StatoProcesso.OK, datiProcesso);
		} catch (Exception e) {
			logger.error("avviaProcessoAvvioIstruttoria", e);
			sync.aggiornaProcesso(processoDomanda.getIdProcesso(), 100, StatoProcesso.KO, datiProcesso);
			throw new RestClientException("Errore durante il processo di avvio istruttoria");
		}

	}
}
