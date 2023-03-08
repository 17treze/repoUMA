package it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Iterables;

import it.tndigitale.a4gistruttoria.repository.dao.ElencoLiquidazioneDao;
import it.tndigitale.a4gistruttoria.repository.model.ElencoLiquidazioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElencoLiquidazioneException;

@Service
public class LiquidazioneService {

	private static final Logger logger = LoggerFactory.getLogger(LiquidazioneService.class);
	
	@Autowired
	private ElencoLiquidazioneService elencoLiquidazioneService;
	@Autowired
	private ElencoLiquidazioneDao daoElencoLiquidazione;
	@Autowired
	private LiquidazioneIstruttoriaService liquidazioneIstruttoria;

	/**
	 * Metodo che si occupa di liquidare le istruttorie per un certo anno e sostegno.
	 * Passi eseguiti:
	 * - Generazione elenco di liquidazione (con salvataggio del tracciato su elenco liquidazione)
	 * - Generazione verbale liquidazione (e persistenza)
	 * - Invio elenco al SOC
	 * - Passaggio di stato delle istruttorie
	 * 
	 * Questo metodo non gestisce la transazione! Ogni passo sarà autoconsistente
	 * 
	 * @param annoCampagna Anno campagna
	 * @param sostegno Sostegno da liquidare
	 * @param istruttorie Istruttorie da liquidare
	 * 
	 * @throws ElencoLiquidazioneException Se si verifica un errore
	 */
	public void liquidaIstruttorie(Integer annoCampagna, Sostegno sostegno, Set<Long> istruttorie)
			throws ElencoLiquidazioneException {
		// Divido lista istruttorie in sottoliste da 500 istruttorie
		for (List<Long> partition : Iterables.partition(istruttorie, 500)) {
			// 1. Genero elenco
			Long idElenco = elencoLiquidazioneService.generaElenco(annoCampagna, sostegno, partition);
			ElencoLiquidazioneModel elenco = daoElencoLiquidazione
					.findById(idElenco)
					.orElseThrow(() -> new ElencoLiquidazioneException("Elenco di liquidazione " + idElenco + " non trovato"));
			// 2. genero il verbale di liquidazione
			elencoLiquidazioneService.generaVerbale(annoCampagna, sostegno, elenco.getId());

			// 3. Invio il tracciato al SOC
			elencoLiquidazioneService.inviaElenco(annoCampagna, sostegno, idElenco);

			// 4. Passaggio di stato delle istruttorie 
			for (Long idIstruttoria : partition) {
				try {
					liquidazioneIstruttoria.elabora(idIstruttoria);
				} catch (ElaborazioneIstruttoriaException e) {
					logger.debug("Errore esegendo passaggio di stato per istruttoria {}", idIstruttoria);
					throw new ElencoLiquidazioneException("Errore esegendo passaggio di stato per istruttoria " + idIstruttoria, e);
				}
			}
		}
	}
	
	public Set<Long> caricaIdentificativiIstruttorieLiquidabili(Integer campagna, Sostegno sostegno) {
		return liquidazioneIstruttoria.caricaIdentificativiIstruttorieLiquidabili(campagna, sostegno);
	}
	
}
