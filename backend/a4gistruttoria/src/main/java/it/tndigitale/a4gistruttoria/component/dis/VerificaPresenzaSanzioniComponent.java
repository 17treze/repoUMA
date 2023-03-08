/**
 * 
 */
package it.tndigitale.a4gistruttoria.component.dis;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.builder.DatiIstruttoriaBuilder;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.DatiIstruttoreService;
import it.tndigitale.a4gistruttoria.service.DomandeService;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.CalcoloSostegnoException;

/**
 * 
 * Il Sistema deve verificare se per l'azienda in esame ci sono state domande uniche con sanzione negli anni precedenti.
 * Questo perché chi è recidivo viene punito (anche in base allo scostamento (gravità) della sovradichiarazione).
 * Al primo scostamento, se questo non supera il 10%, viene applicata non la sanzione piena ma la yellowcard (cartellino giallo).
 * Se l'anno successivo sono ancora in difetto (sovradichiarazione) allora il Sistema deve tener conto nella 
 * sanzione da applicare anche il recupero dello sconto fatto l'anno prima.
 * 
 * @author IT417
 *
 */
public abstract class VerificaPresenzaSanzioniComponent {

	private static final Logger logger = LoggerFactory.getLogger(VerificaPresenzaSanzioniComponent.class);
	
	@Autowired
	private DomandeService serviceDomande;
	
	@Autowired
	private DatiIstruttoreService datiIstruttoreService;

	protected Integer annoPrecedente(final DomandaUnicaModel domanda) {
		if (domanda.getCampagna() != null) {
			return domanda.getCampagna() - 1;
		} else {
			throw new EntityNotFoundException(
					String.format("Impossibile ricavare l'anno campagna a partire dalla domanda con id '%s'",
							domanda.getId()));
		}
	}
	
	/**
	 * Carica i dati dell'istruttore per l'istruttoria del disaccoppiato
	 * agganciati alla domanda 
	 * 
	 * @param istruttoria
	 * @return
	 */
	protected Optional<DatiIstruttoria> caricaDatiIstruttore(IstruttoriaModel istruttoria) {
		DatiIstruttoria datiIstruttoria = DatiIstruttoriaBuilder.from(istruttoria.getDatiIstruttoreDisModel());
		return Optional.ofNullable(datiIstruttoria);
	}
	
	protected boolean isAnnoPrecedenteDomandaPresentata(DomandaUnicaModel domanda) throws CalcoloSostegnoException {
		return serviceDomande.checkDomandaPerSettore(
				annoPrecedente(domanda), 
				domanda.getCuaaIntestatario());
	}
	
	protected boolean isAnnoPrecedenteIstruttoriaA4G(DomandaUnicaModel domanda) {
		return serviceDomande.getDomandaAnnoPrecedente(domanda) != null;		
	}

	protected abstract List<VariabileCalcolo> datiSanzioniDaIstruttore(DatiIstruttoria datiIstruttore, List<VariabileCalcolo> input);

	protected abstract List<VariabileCalcolo> calcolaPresenzaSanzioni(IstruttoriaModel istruttoriaModel, List<VariabileCalcolo> input);

	protected List<VariabileCalcolo> calcolaSanzioni(List<VariabileCalcolo> input, IstruttoriaModel istruttoriaModel) throws CalcoloSostegnoException {
		try {
			/**
			 * L'istruttore ha la possibilità di forzare alcuni dati per la gestione di situazioni peculiari in cui il Sistema
			 * non è in grado autonomamente di capire la casistica in cui si trova.
			 * Pertanto se l'istruttore indica che la domanda anno precedente non è liquidabile, i controlli
			 * vengono fatti con i dati compilati dall'istruttore. Programmaticamente altrimenti.
			 */
			Optional<DatiIstruttoria> datiIstruttore = caricaDatiIstruttore(istruttoriaModel);
			return datiIstruttore
					.filter(istruttore -> Boolean.TRUE.equals(istruttore.getDomAnnoPrecNonLiquidabile())) // l'istruttore vuole forzare i controlli
					.map(di -> datiSanzioniDaIstruttore(di, input)) // uso i dati di istruttoria
					.orElseGet(() -> calcolaPresenzaSanzioni(istruttoriaModel, input)); // calcolo le sanzioni
		} catch (RuntimeException re) {
			Throwable t = re.getCause();
			if (t instanceof CalcoloSostegnoException) {
				throw (CalcoloSostegnoException)t;
			}
			String errorMessage = String.format(
					"Errore nel recupero dei dati di calcolo dell'anno precedente per l'istruttoria con id %s",
					istruttoriaModel.getId());
			logger.error(errorMessage, re);
			throw new CalcoloSostegnoException(errorMessage);
		}
	}
}
