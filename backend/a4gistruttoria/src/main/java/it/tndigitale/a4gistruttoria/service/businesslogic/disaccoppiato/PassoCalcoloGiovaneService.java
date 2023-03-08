package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiPassoLavorazione;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.service.businesslogic.PassoDatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
/**
 * Classe di servizio che gestisce il calcolo del premio per il Giovane. Riferimento cartella https://drive.google.com/open?id=1PqLAzXMxXJU8SY4nx-uYjqlyafpm5mO9 Dettaglio Algoritmo 1.6_GIOVANE
 * 
 * @author A2AC0147
 *
 */
@Service
public class PassoCalcoloGiovaneService extends PassoDatiElaborazioneIstruttoria {

	BigDecimal perc100 = new BigDecimal(100).stripTrailingZeros();
	BigDecimal maxSupScostamento = BigDecimal.valueOf(2.0000);
	BigDecimal maxPercScostamento = BigDecimal.valueOf(0.030);
	BigDecimal maxPercScosSanzione = BigDecimal.valueOf(0.10);
	BigDecimal multiImpSanzione = BigDecimal.valueOf(1.50);
	BigDecimal multiImpSanzioneYC = BigDecimal.valueOf(0.75);
	
	private static final Logger logger = LoggerFactory.getLogger(PassoCalcoloGiovaneService.class);

	@Override
	protected DatiPassoLavorazione elaboraPasso(DatiElaborazioneIstruttoria dati, MapVariabili variabiliCalcolo,
			HashMap<TipoControllo, EsitoControllo> mappaEsiti) {
		logger.debug("Avvio calcolo per istruttoria {}", dati.getIstruttoria().getId());
		DatiPassoLavorazione passo = new DatiPassoLavorazione();
		passo.setIdTransizione(dati.getTransizione().getId());
		passo.setPasso(getPasso());

		boolean sanzionato = false;
		if (isGiovaneRichiesto(variabiliCalcolo)) {
			logger.debug("trovato giovane per domanda {}", dati.getIstruttoria().getDomandaUnicaModel().getId());

			sanzionato = eseguiAlgoritmoGiovaneRichiesto(variabiliCalcolo, mappaEsiti);

			valutaRisultatoAlgoritmoGiovane(passo, sanzionato, variabiliCalcolo, mappaEsiti);

		} else {
			logger.debug("eseguiPassoLavorazioneSostegno : no giovane");
			eseguiAlgoritmoNoGiovane();
			passo.setCodiceEsito(CodiceEsito.DUT_034.getCodiceEsito());
			passo.setEsito(true);
			// rimuove la variabile GIORIC per mostrare solo le variabili GIOYCIMPSANZAPREC e GIOYCSANZANNIPREC nel caso in cui non sia richiesto il Giovane
			passo.getDatiInput().setVariabiliCalcolo(
					passo.getDatiInput().getVariabiliCalcolo().stream().filter(v -> !v.getTipoVariabile().equals(TipoVariabile.GIORIC)).collect(Collectors.toList()));
		}

		passo.getDatiOutput().setVariabiliCalcolo(impostaVaribiliOutput(variabiliCalcolo, sanzionato, mappaEsiti));

		mappaEsiti.values().forEach(e -> passo.getDatiSintesi().getEsitiControlli().add(e));
		
		return passo;
	}

	// Esegue quanto descritto nella scheda Foglie dell'algoritmo
	private void valutaRisultatoAlgoritmoGiovane(DatiPassoLavorazione passo, boolean isSanzionato, MapVariabili variabiliCalcolo, HashMap<TipoControllo, EsitoControllo> mappaEsiti) {
		if (isRequisitiGiovane(variabiliCalcolo, mappaEsiti)) {

			if (isRiduzioneGiovane(variabiliCalcolo)) {

				if (isSanzionato) {

					if (isRecidiva(variabiliCalcolo)) {

						if (isSanzionePrecedente(variabiliCalcolo)) {
							passo.setCodiceEsito(CodiceEsito.DUT_041.getCodiceEsito());
							passo.setEsito(true);

						} else {
							passo.setCodiceEsito(CodiceEsito.DUT_040.getCodiceEsito());
							passo.setEsito(true);
						}

					} else {

						if (isScontoGiovane(variabiliCalcolo)) {
							passo.setCodiceEsito(CodiceEsito.DUT_038.getCodiceEsito());
							passo.setEsito(true);

						} else {
							passo.setCodiceEsito(CodiceEsito.DUT_039.getCodiceEsito());
							passo.setEsito(true);
						}
					}

				} else {
					passo.setCodiceEsito(CodiceEsito.DUT_037.getCodiceEsito());
					passo.setEsito(true);
				}

			} else {
				passo.setCodiceEsito(CodiceEsito.DUT_036.getCodiceEsito());
				passo.setEsito(true);
			}

		} else {
			passo.setCodiceEsito(CodiceEsito.DUT_035.getCodiceEsito());
			passo.setEsito(true);
		}
	}

	private Boolean isGiovaneRichiesto(MapVariabili variabiliCalcolo) {
		return variabiliCalcolo.get(TipoVariabile.GIORIC).getValBoolean();
	}

	private Boolean isRequisitiGiovane(MapVariabili variabiliCalcolo, HashMap<TipoControllo, EsitoControllo> mappaEsiti) {
		mappaEsiti.put(TipoControllo.BRIDUSDC028_requisitiGiovane, new EsitoControllo(TipoControllo.BRIDUSDC028_requisitiGiovane, variabiliCalcolo.get(TipoVariabile.REQGIOVANE).getValBoolean()));
		return variabiliCalcolo.get(TipoVariabile.REQGIOVANE).getValBoolean();
	}

	private Boolean isRiduzioneGiovane(MapVariabili variabiliCalcolo) {
		return variabiliCalcolo.get(TipoVariabile.GIOIMPRID).getValNumber().compareTo(new BigDecimal(0)) > 0;
	}

	private Boolean isRecidiva(MapVariabili variabiliCalcolo) {
		return variabiliCalcolo.get(TipoVariabile.GIORECIDIVA).getValBoolean();
	}

	private Boolean isScontoGiovane(MapVariabili variabiliCalcolo) {
		return variabiliCalcolo.get(TipoVariabile.GIOYELLOWCARD).getValBoolean();
	}

	private Boolean isSanzionePrecedente(MapVariabili variabiliCalcolo) {
		return variabiliCalcolo.get(TipoVariabile.GIOIMPSANZREC).getValNumber().compareTo(new BigDecimal(0)) > 0;
	}

	// Esegue quanto descritto nella scheda AlgoritmoNoGiovane$IDDOMANDA
	// In caso di non giovane basta mantenere i dati del calcolo 2017 in modo da slegarsi da AGS in vista della campagna 2019
	private void eseguiAlgoritmoNoGiovane() {
	}

	// Esegue quanto descritto nella scheda AlgoritmoGiovane$IDDOMANDA
	private boolean eseguiAlgoritmoGiovaneRichiesto(MapVariabili variabiliCalcolo, HashMap<TipoControllo, EsitoControllo> mappaEsiti) {
		if (isRequisitiGiovane(variabiliCalcolo, mappaEsiti))
			variabiliCalcolo.setVal(TipoVariabile.GIOSUPAMM, variabiliCalcolo.min(TipoVariabile.BPSSUPAMM, TipoVariabile.GIOSUPRIC));
		else
			variabiliCalcolo.setVal(TipoVariabile.GIOSUPAMM, BigDecimal.ZERO);

		variabiliCalcolo.setVal(TipoVariabile.GIOIMPAMM, variabiliCalcolo.multiply(TipoVariabile.GIOSUPAMM, TipoVariabile.TITVALGIORID, TipoVariabile.GIOPERC));

		if (isRequisitiGiovane(variabiliCalcolo, mappaEsiti)) {
			variabiliCalcolo.setVal(TipoVariabile.GIOSUPSCOST, variabiliCalcolo.subtract(TipoVariabile.GIOSUPRIC, TipoVariabile.GIOSUPAMM));
			if (variabiliCalcolo.get(TipoVariabile.GIOSUPAMM).getValNumber().compareTo(BigDecimal.ZERO) == 0)
				variabiliCalcolo.setVal(TipoVariabile.GIOPERCSCOST, BigDecimal.valueOf(1));
			else
				variabiliCalcolo.setVal(TipoVariabile.GIOPERCSCOST, variabiliCalcolo.divide(TipoVariabile.GIOSUPSCOST, TipoVariabile.GIOSUPAMM));
			isDomandaConRiduzioni(variabiliCalcolo, mappaEsiti);
		}
		variabiliCalcolo.setVal(TipoVariabile.GIOIMPRID, variabiliCalcolo.subtract(TipoVariabile.GIOIMPRIC, TipoVariabile.GIOIMPAMM));

		// Variabili da valorizzare solo se (GIOPERCSCOST> 3% OR GIOSCOST > 2,0000) AND REQGIOVANE is TRUE
		boolean sanzionata = isDomandaSanzionata(variabiliCalcolo, mappaEsiti); 
		if (sanzionata) {
			variabiliCalcolo.setVal(TipoVariabile.GIORECIDIVA, variabiliCalcolo.get(TipoVariabile.GIOYCSANZANNIPREC).getValBoolean());

			variabiliCalcolo.setVal(TipoVariabile.GIOYELLOWCARD,
					(variabiliCalcolo.get(TipoVariabile.GIOPERCSCOST).getValNumber().compareTo(maxPercScosSanzione) < 0) ? !variabiliCalcolo.get(TipoVariabile.GIORECIDIVA).getValBoolean()
							: false);

			variabiliCalcolo.setVal(TipoVariabile.GIOIMPSANZ, calcolaImportoSanzione(variabiliCalcolo));
			variabiliCalcolo.setVal(TipoVariabile.GIOIMPSANZREC, variabiliCalcolo.get(TipoVariabile.GIOYCIMPSANZAPREC).getValNumber());
			mappaEsiti.put(TipoControllo.BRIDUSDC057_yellowCardGiovane,
					new EsitoControllo(TipoControllo.BRIDUSDC057_yellowCardGiovane, variabiliCalcolo.get(TipoVariabile.GIOYELLOWCARD).getValBoolean()));

		}
		return sanzionata;
	}

	private boolean isDomandaConRiduzioni(MapVariabili variabiliCalcolo, HashMap<TipoControllo, EsitoControllo> mappaEsiti) {

		boolean isRidotta = true;
		// controllo per BRIDU059
		if (variabiliCalcolo.get(TipoVariabile.GIOSUPSCOST).getValNumber().compareTo(BigDecimal.ZERO) == 0) {
			isRidotta = false;
		}
		mappaEsiti.put(TipoControllo.BRIDUSDC059_riduzioniGiovane, new EsitoControllo(TipoControllo.BRIDUSDC059_riduzioniGiovane, isRidotta));
		return isRidotta;

	}

	private boolean isDomandaSanzionata(MapVariabili variabiliCalcolo, HashMap<TipoControllo, EsitoControllo> mappaEsiti) {
		boolean isSanzionato = false;
		if (isRequisitiGiovane(variabiliCalcolo, mappaEsiti) && (variabiliCalcolo.get(TipoVariabile.GIOSUPSCOST).getValNumber().compareTo(maxSupScostamento) > 0
				|| variabiliCalcolo.get(TipoVariabile.GIOPERCSCOST).getValNumber().compareTo(maxPercScostamento) > 0))
			isSanzionato = true;
		mappaEsiti.put(TipoControllo.BRIDUSDC060_sanzioniGiovane, new EsitoControllo(TipoControllo.BRIDUSDC060_sanzioniGiovane, isSanzionato));
		return isSanzionato;
	}

	private BigDecimal calcolaImportoSanzione(MapVariabili variabiliCalcolo) {
		BigDecimal supScostamento = variabiliCalcolo.get(TipoVariabile.GIOSUPSCOST).getValNumber();
		BigDecimal titValGio = variabiliCalcolo.get(TipoVariabile.TITVALGIORID).getValNumber();
		BigDecimal gioImpRic = variabiliCalcolo.get(TipoVariabile.GIOIMPRIC).getValNumber();

		return (variabiliCalcolo.get(TipoVariabile.GIOYELLOWCARD).getValBoolean())
				? supScostamento.multiply(BigDecimal.valueOf(0.75)).multiply(titValGio).multiply(variabiliCalcolo.get(TipoVariabile.GIOPERC).getValNumber())
				: gioImpRic.min(supScostamento.multiply(BigDecimal.valueOf(1.5)).multiply(titValGio).multiply(variabiliCalcolo.get(TipoVariabile.GIOPERC).getValNumber()));

	}

	private List<VariabileCalcolo> impostaVaribiliOutput(MapVariabili variabiliCalcolo, boolean sanzionato, HashMap<TipoControllo, EsitoControllo> mappaEsiti) {
		List<VariabileCalcolo> variabiliCalcoloOutput = new ArrayList<VariabileCalcolo>();
		if (isGiovaneRichiesto(variabiliCalcolo)) {
			variabiliCalcoloOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPAMM));
			variabiliCalcoloOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPAMM));
			variabiliCalcoloOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPRID));

			if (sanzionato) {
				variabiliCalcoloOutput.add(variabiliCalcolo.get(TipoVariabile.GIORECIDIVA));
				variabiliCalcoloOutput.add(variabiliCalcolo.get(TipoVariabile.GIOYELLOWCARD));
				variabiliCalcoloOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPSANZ));
				variabiliCalcoloOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPSANZREC));
			}

			if (isRequisitiGiovane(variabiliCalcolo, mappaEsiti)) {
				variabiliCalcoloOutput.add(variabiliCalcolo.get(TipoVariabile.GIOSUPSCOST));
				variabiliCalcoloOutput.add(variabiliCalcolo.get(TipoVariabile.GIOPERCSCOST));
			}
		}
		return variabiliCalcoloOutput;
	}

	@Override
	public TipologiaPassoTransizione getPasso() {
		return TipologiaPassoTransizione.GIOVANE_AGRICOLTORE;
	}
}
