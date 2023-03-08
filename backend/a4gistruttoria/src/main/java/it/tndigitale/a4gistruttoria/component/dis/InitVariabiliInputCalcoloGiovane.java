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
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
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
@Component
public class InitVariabiliInputCalcoloGiovane extends VerificaPresenzaSanzioniComponent {

	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliInputCalcoloGiovane.class);
	
	@Autowired
	private DomandeService serviceDomande;
	
	public List<VariabileCalcolo> initInputGiovaneAgricoltore(DatiElaborazioneIstruttoria datiElaborazioneIntermedi)
			throws CalcoloSostegnoException {
		final List<VariabileCalcolo> input = new ArrayList<>();
		IstruttoriaModel istruttoriaModel = datiElaborazioneIntermedi.getTransizione().getIstruttoria();

		VariabileCalcolo vcRichiestoGiovane = 
				datiElaborazioneIntermedi.getVariabileInput(TipologiaPassoTransizione.AMMISSIBILITA, TipoVariabile.GIORIC);
		boolean isGiovaneRichiesto = Optional.ofNullable(vcRichiestoGiovane).map(VariabileCalcolo::getValBoolean).orElse(false);
		if (isGiovaneRichiesto) {
			input.add(datiElaborazioneIntermedi.getVariabileInput(TipologiaPassoTransizione.AMMISSIBILITA, TipoVariabile.GIOPERC));
			popolaInputSeVariabileValoreNonNullo(datiElaborazioneIntermedi, input, TipologiaPassoTransizione.AMMISSIBILITA, TipoVariabile.GIOIMPRIC);
			popolaInputSeVariabileValoreNonNullo(datiElaborazioneIntermedi, input, TipologiaPassoTransizione.AMMISSIBILITA, TipoVariabile.GIOSUPRIC);
			input.add(datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSSUPAMM));
			input.add(new VariabileCalcolo(TipoVariabile.REQGIOVANE, BigDecimal.ONE.equals(datiElaborazioneIntermedi.getInfoSian().getFlagGiovAgri())));
			popolaInputSeVariabileValoreNonNullo(datiElaborazioneIntermedi, input, TipologiaPassoTransizione.AMMISSIBILITA, TipoVariabile.TITVALGIORID);
		}
		input.add(vcRichiestoGiovane);
		
		// Recupero sanzioni anni precedenti
		return calcolaSanzioni(input, istruttoriaModel);
	}


	protected void popolaInputSeVariabileValoreNonNullo(DatiElaborazioneIstruttoria datiElaborazioneIntermedi,
                                                        List<VariabileCalcolo> input, TipologiaPassoTransizione passo, TipoVariabile tipoV) {
		VariabileCalcolo v = datiElaborazioneIntermedi.getVariabileOutput(passo, tipoV);
		if (v != null) {
			input.add(v);
		}
	}
	
	protected List<VariabileCalcolo> calcolaPresenzaSanzioni(IstruttoriaModel istruttoriaModel, List<VariabileCalcolo> input) {
		DomandaUnicaModel domanda = istruttoriaModel.getDomandaUnicaModel();
		try {
			// Verifico se anno precedente è stata presentata domanda => in ags
			boolean esisteDomandaAnnoPrecedente = 
					isAnnoPrecedenteDomandaPresentata(domanda);
			// Controllo se anno precedente ha presentato domanda
			if (!esisteDomandaAnnoPrecedente) {
				return annoPrecedenteNonHaPresentatoDomanda(domanda, input);
			}
			// verifico se l'istruttoria era prevista in a4g o meno
			boolean annoPrecedenteIstruitoInA4G = isAnnoPrecedenteIstruttoriaA4G(domanda);
			// non è stata ricevuta domanda anno precedente => verifico istruttoria in ags
			if (!annoPrecedenteIstruitoInA4G) { // Istruttoria in ags
				return annoPrecedenteIstruitoInAGS(domanda, input);
			}
			
			// istruttoria in A4G
			boolean hasDomAnnoPrecIstruttoriaDisaccoppiatoConclusa = 
					serviceDomande.checkDomandaAnnoPrecedenteIstruttoriaSostegnoConclusa(domanda, Sostegno.DISACCOPPIATO);
			if (!hasDomAnnoPrecIstruttoriaDisaccoppiatoConclusa) { // istruttoria non conclusa
				return input;
			}

			Float sanzioniAnnoPrecedente = serviceDomande.getSanzioneAnnoPrecedente(domanda, true);
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
		input.add(new VariabileCalcolo(TipoVariabile.GIOYCSANZANNIPREC, true));
		input.add(new VariabileCalcolo(TipoVariabile.GIOYCIMPSANZAPREC, BigDecimal.valueOf(sanzioniAnnoPrecedente)));
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
			throw new CalcoloSostegnoException(errorMessage);
		}

		Optional<VariabileSostegnoAgs> optSanz49083 = 
				calcoloSostegnoAgs.getVariabiliCalcolo().stream().filter(v -> v.getVariabile().equalsIgnoreCase("SANZ_490_83")).findFirst();
		Optional<VariabileSostegnoAgs> optSanz49084 = 
				calcoloSostegnoAgs.getVariabiliCalcolo().stream().filter(v -> v.getVariabile().equalsIgnoreCase("SANZ_490_84")).findFirst();
		Optional<VariabileSostegnoAgs> optSanz49085 = 
				calcoloSostegnoAgs.getVariabiliCalcolo().stream().filter(v -> v.getVariabile().equalsIgnoreCase("SANZ_490_85")).findFirst();

		if (optSanz49083.isPresent() && optSanz49084.isPresent() && optSanz49085.isPresent()) {

			boolean hasSanzAnnoPrec = (optSanz49083.get().getValore() != null || optSanz49084.get().getValore() != null
					|| optSanz49085.get().getValore() != null);

			input.add(new VariabileCalcolo(TipoVariabile.GIOYCSANZANNIPREC, hasSanzAnnoPrec));
		} else {
			input.add(new VariabileCalcolo(TipoVariabile.GIOYCSANZANNIPREC, false));
		}
		input.add(new VariabileCalcolo(TipoVariabile.GIOYCIMPSANZAPREC, BigDecimal.ZERO));


		return input;
	}
	
	protected List<VariabileCalcolo> annoPrecedenteNonHaPresentatoDomanda(DomandaUnicaModel domanda, List<VariabileCalcolo> input) {
		logger.debug("annoPrecedenteNonHaPresentatoDomanda: per l'anno precedente non risulta presentata la domanda; domanda attuale: id = {} e numero = {}", domanda.getId(), domanda.getNumeroDomanda());
		return domandaDaConsiderareSenzaSanzioni(domanda, input);
	}
	
	protected List<VariabileCalcolo> domandaDaConsiderareSenzaSanzioni(DomandaUnicaModel domanda, List<VariabileCalcolo> input) {
		input.add(new VariabileCalcolo(TipoVariabile.GIOYCSANZANNIPREC, false));
		input.add(new VariabileCalcolo(TipoVariabile.GIOYCIMPSANZAPREC, BigDecimal.ZERO));
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
		Optional<Boolean> sanzioniAnnoPrecedente = Optional.ofNullable(datiIstruttore.getGioSanzioniAnnoPrecedente()); // l'istruttore indica se ci sono state sanzioni o meno
		input.add(new VariabileCalcolo(TipoVariabile.GIOYCSANZANNIPREC, sanzioniAnnoPrecedente.map(Boolean::booleanValue).orElse(false))); 
		// L'importo della sanzione viene determinata dall'istruttore se ha indicato la presenza di sanzioni
		input.add(new VariabileCalcolo(TipoVariabile.GIOYCIMPSANZAPREC, sanzioniAnnoPrecedente
					.filter(Boolean::booleanValue) // l'istruttore ha specificato che ci sono sanzioni
					.map(s -> datiIstruttore.getGioImportoSanzioniAnnoPrecedente()) // uso l'importo specificato dall'istruttore
					.orElse(BigDecimal.ZERO))); 
		return input;
	}
}
