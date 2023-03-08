/**
 * 
 */
package it.tndigitale.a4gistruttoria.component.dis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.CalcoloSostegnoAgs;
import it.tndigitale.a4gistruttoria.dto.VariabileSostegnoAgs;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.DomandeService;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.CalcoloSostegnoException;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

/**
 * 
 * Il Sistema deve verificare se per l'azienda in esame ci sono state domande uniche 
 * con sanzione negli anni precedenti.
 * Questo perché chi è recidivo viene punito (anche in base allo scostamento (gravità) 
 * della sovradichiarazione).
 * Al primo scostamento, se questo non supera il 10%, viene applicata non la sanzione piena 
 * ma la yellowcard (cartellino giallo).
 * Se l'anno successivo sono ancora in difetto (sovradichiarazione) allora il Sistema 
 * deve tener conto nella 
 * sanzione da applicare anche il recupero dello sconto fatto l'anno prima.
 * 
 * @author IT417
 *
 */
@Component
public class InitVariabiliInputSanzioniBPS extends VerificaPresenzaSanzioniComponent {

	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliInputSanzioniBPS.class);
	
	@Autowired
	private DomandeService serviceDomande;
	
	public List<VariabileCalcolo> initInputSanzioniBps(DatiElaborazioneIstruttoria datiElaborazioneIntermedi) throws CalcoloSostegnoException {
		final List<VariabileCalcolo> input = new ArrayList<>();
		IstruttoriaModel istruttoriaModel = datiElaborazioneIntermedi.getTransizione().getIstruttoria();
		input.add(datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.AMMISSIBILITA, TipoVariabile.TITVALRID));
		input.add(datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSIMPAMM));
		input.add(datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.AMMISSIBILITA, TipoVariabile.BPSIMPRIC));
		input.add(datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSSUPSCOST));
		input.add(datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSPERCSCOST));
		
		// Recupero sanzioni anni precedenti
		return calcolaSanzioni(input, istruttoriaModel);
	}

	protected List<VariabileCalcolo> calcolaPresenzaSanzioni(IstruttoriaModel istruttoriaModel, List<VariabileCalcolo> input) {
		DomandaUnicaModel domanda = istruttoriaModel.getDomandaUnicaModel();
		try {
			logger.debug("calcolaPresenzaSanzioni: per la domanda con id {} e numero {} calcolo se sono presenti sanzioni", domanda.getId(), domanda.getNumeroDomanda());

			// Verifico se anno precedente è stata presentata domanda => in ags
			boolean esisteDomandaAnnoPrecedente = 
					serviceDomande.checkDomandaPerSettore(
							annoPrecedente(domanda), 
							domanda.getCuaaIntestatario());
			// Controllo se anno precedente ha presentato domanda
			if (!esisteDomandaAnnoPrecedente) {
				return annoPrecedenteNonHaPresentatoDomanda(domanda, input);
			}
			logger.debug("calcolaPresenzaSanzioni: per la domanda con id {} e numero {} risultano presenti domande anno precedente", domanda.getId(), domanda.getNumeroDomanda());
			// verifico se l'istruttoria era prevista in a4g o meno
			boolean annoPrecedenteIstruitoInA4G = serviceDomande.getDomandaAnnoPrecedente(domanda) != null;
			// non è stata ricevuta domanda anno precedente => verifico istruttoria in ags
			if (!annoPrecedenteIstruitoInA4G) { // Istruttoria in ags
				return annoPrecedenteIstruitoInAGS(domanda, input);
			}
			logger.debug("calcolaPresenzaSanzioni: per la domanda con id {} e numero {} ha anno precedente in A4G", domanda.getId(), domanda.getNumeroDomanda());
			
			// istruttoria in A4G
			boolean hasDomAnnoPrecIstruttoriaDisaccoppiatoConclusa = 
					serviceDomande.checkDomandaAnnoPrecedenteIstruttoriaSostegnoConclusa(domanda, Sostegno.DISACCOPPIATO);
			logger.debug(
					"calcolaPresenzaSanzioni: per la domanda con id {} e numero {} ha istruttoria a4g conclusa = {}",
					domanda.getId(), domanda.getNumeroDomanda(), hasDomAnnoPrecIstruttoriaDisaccoppiatoConclusa);
			if (!hasDomAnnoPrecIstruttoriaDisaccoppiatoConclusa) { // istruttoria non conclusa
				input.add(new VariabileCalcolo(TipoVariabile.BPSDOMLIQANNOPREC, false));
				return input;
			}

			Float sanzioniAnnoPrecedente = serviceDomande.getSanzioneAnnoPrecedente(domanda, false);
			if (sanzioniAnnoPrecedente != null) { // presenza sanzioni
				return annoPrecedenteConSanzioni(sanzioniAnnoPrecedente, input);
			}
			// domanda pulita istruita e senza sanzioni
			return domandaDaConsiderareSenzaSanzioni(domanda, input);
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			logger.error("calcolaPresenzaSanzioni: errore calcolando le sanzioni per la domanda con id = {} e numero = {}",
					domanda.getId(), domanda.getNumeroDomanda(), e);
			throw new RuntimeException(e);
		}
	}
	
	protected List<VariabileCalcolo> annoPrecedenteConSanzioni(Float sanzioniAnnoPrecedente, List<VariabileCalcolo> input) throws Exception {
		input.add(new VariabileCalcolo(TipoVariabile.BPSYCSANZANNIPREC, true));
		input.add(new VariabileCalcolo(TipoVariabile.BPSYCIMPSANZAPREC, BigDecimal.valueOf(sanzioniAnnoPrecedente)));
		input.add(new VariabileCalcolo(TipoVariabile.BPSDOMLIQANNOPREC, true));
		return input;		
	}
	
	protected List<VariabileCalcolo> annoPrecedenteIstruitoInAGS(DomandaUnicaModel domanda, List<VariabileCalcolo> input) throws Exception {
		logger.debug(
				"annoPrecedenteIstruitoInAGS: anno precedente risulta istruito in AGS; domanda attuale: id = {} e numero = {}",
				domanda.getId(), domanda.getNumeroDomanda());
		CalcoloSostegnoAgs calcoloSostegnoAgs = serviceDomande.getDatiCalcoloSostegnoAgs(domanda);
		if (calcoloSostegnoAgs == null) {
			String errorMessage = "Impossibile procedere al recupero della sanzione dal calcolo dell'anno precedente   per il CUAA "
					+ domanda.getCuaaIntestatario() + " in quanto la domanda non risulta liquidata.";
			logger.info("annoPrecedenteIstruitoInAGS: " + errorMessage);
			input.add(new VariabileCalcolo(TipoVariabile.BPSDOMLIQANNOPREC, false));
			return input;
		}
		Optional<VariabileSostegnoAgs> optSanz40783 = calcoloSostegnoAgs.getVariabiliCalcolo().stream()
				.filter(v -> v.getVariabile().equalsIgnoreCase("SANZ_407_83")).findFirst();
		Optional<VariabileSostegnoAgs> optSanz40784 = calcoloSostegnoAgs.getVariabiliCalcolo().stream()
				.filter(v -> v.getVariabile().equalsIgnoreCase("SANZ_407_84")).findFirst();
		Optional<VariabileSostegnoAgs> optSanz40785 = calcoloSostegnoAgs.getVariabiliCalcolo().stream()
				.filter(v -> v.getVariabile().equalsIgnoreCase("SANZ_407_85")).findFirst();
		Optional<VariabileSostegnoAgs> optRid4073 = calcoloSostegnoAgs.getVariabiliCalcolo().stream()
				.filter(v -> v.getVariabile().equalsIgnoreCase("RID_407_3")).findFirst();

		if (optSanz40783.isPresent() && optSanz40784.isPresent() && optSanz40785.isPresent()
				&& optRid4073.isPresent()) {

			boolean hasSanzAnnoPrec = (optSanz40783.get().getValore() != null || optSanz40784.get().getValore() != null
					|| optSanz40785.get().getValore() != null);

			if (hasSanzAnnoPrec) {
				String errorMessage = "La variabile RID_407_3 non risulta valorizzata nel Calcolo Disaccoppiato dell'anno precedente per la domanda "
						+ domanda.getId();
				Float rid4073 = optRid4073.map(vs -> Float.valueOf(vs.getValore()))
						.orElseThrow(() -> new CalcoloSostegnoException(errorMessage));
				Float sanzioneAnnoPrecedente = 
						optSanz40783
						.filter(vs -> vs.getValore() != null)
						.map(vs -> Float.valueOf(vs.getValore()))
						.filter(sanz -> Float.compare(sanz, rid4073) < 0).orElse(Float.valueOf("0"));

				return annoPrecedenteConSanzioni(sanzioneAnnoPrecedente, input);
			} else {
				return domandaDaConsiderareSenzaSanzioni(domanda, input);
			}
		} else {
			logger.warn(
					"Impossibile procedere al recupero della sanzione dal calcolo dell'anno precedente per il CUAA {}; domanda attuale: id = {} e numero = {}",
					domanda.getCuaaIntestatario(), domanda.getId(), domanda.getNumeroDomanda());
			input.add(new VariabileCalcolo(TipoVariabile.BPSDOMLIQANNOPREC, false));
		}

		return input;
	}
	
	protected List<VariabileCalcolo> annoPrecedenteNonHaPresentatoDomanda(DomandaUnicaModel domanda, List<VariabileCalcolo> input) {
		logger.debug("annoPrecedenteNonHaPresentatoDomanda: per l'anno precedente non risulta presentata la domanda; domanda attuale: id = {} e numero = {}", domanda.getId(), domanda.getNumeroDomanda());
		return domandaDaConsiderareSenzaSanzioni(domanda, input);
	}
	
	protected List<VariabileCalcolo> domandaDaConsiderareSenzaSanzioni(DomandaUnicaModel domanda, List<VariabileCalcolo> input) {
		input.add(new VariabileCalcolo(TipoVariabile.BPSDOMLIQANNOPREC, true)); // indica che l'algoritmo può fare il calcolo delle eventuali sanzioni (ha tutte le informazioni)
		input.add(new VariabileCalcolo(TipoVariabile.BPSYCSANZANNIPREC, false));  // senza domanda == no sanzioni
		input.add(new VariabileCalcolo(TipoVariabile.BPSYCIMPSANZAPREC, BigDecimal.ZERO)); // senza domanda == no sanzioni
		return input;
	}
	
	/**
	 * Uso i dati dell'istruttore per compilare le variabili <code>BPSDOMLIQANNOPREC</code>, <code>BPSYCSANZANNIPREC</code> e <code>BPSYCIMPSANZAPREC</code>
	 * 
	 * @param datiIstruttore DatiIstruttoria Fonte dati
	 * @param input Lista delle variabili da popolare (viene aggiornata)
	 * @return La lista delle variabili di input aggiornata
	 */
	protected List<VariabileCalcolo> datiSanzioniDaIstruttore(DatiIstruttoria datiIstruttore, List<VariabileCalcolo> input) {
		logger.debug("datiSanzioniDaIstruttore: uso i dati dell'istruttore");
		input.add(new VariabileCalcolo(TipoVariabile.BPSDOMLIQANNOPREC, true)); // indica che l'algoritmo può fare il calcolo delle eventuali sanzioni (ha tutte le informazioni)
		Optional<Boolean> sanzioniAnnoPrecedente = Optional.ofNullable(datiIstruttore.getBpsSanzioniAnnoPrecedente()); // l'istruttore indica se ci sono state sanzioni o meno
		input.add(new VariabileCalcolo(TipoVariabile.BPSYCSANZANNIPREC, sanzioniAnnoPrecedente.map(Boolean::booleanValue).orElse(false))); 
		// L'importo della sanzione viene determinata dall'istruttore se ha indicato la presenza di sanzioni
		input.add(new VariabileCalcolo(TipoVariabile.BPSYCIMPSANZAPREC, sanzioniAnnoPrecedente
					.filter(Boolean::booleanValue) // l'istruttore ha specificato che ci sono sanzioni
					.map(s -> datiIstruttore.getBpsImportoSanzioniAnnoPrecedente()) // uso l'importo specificato dall'istruttore
					.orElse(BigDecimal.ZERO))); 
		return input;
	}
}
