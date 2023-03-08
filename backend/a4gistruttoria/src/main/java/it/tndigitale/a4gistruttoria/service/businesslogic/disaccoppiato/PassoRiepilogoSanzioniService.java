package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiPassoLavorazione;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.RichiestaSuperficieDao;
//import it.tndigitale.a4gistruttoria.repository.dao.TitoloDuDao;
import it.tndigitale.a4gistruttoria.service.DomandeService;
import it.tndigitale.a4gistruttoria.service.businesslogic.PassoDatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Service
public class PassoRiepilogoSanzioniService extends PassoDatiElaborazioneIstruttoria {

//	private static final Logger logger = LoggerFactory.getLogger(PassoCalcoloRiduzioniService.class);

	@Autowired
	DomandeService domandaService;
	@Autowired
    DomandaUnicaDao daoDomanda;
	@Autowired
	RichiestaSuperficieDao daoRichiestaSuperficie;
//	@Autowired
//	TitoloDuDao daoTitoliDu;

	@Override
	protected DatiPassoLavorazione elaboraPasso(DatiElaborazioneIstruttoria dati, MapVariabili variabiliCalcolo,
			HashMap<TipoControllo, EsitoControllo> mappaEsiti) {
		// lista variabili di calcolo
		variabiliCalcolo.addElements(TipoVariabile.SANZTOT, TipoVariabile.IMPSANZNORISC, TipoVariabile.BPSIMPCALCINT, TipoVariabile.GREIMPCALCINT, TipoVariabile.GREIMPCALC,
				TipoVariabile.GIOIMPCALCINT, TipoVariabile.IMPSANZINTERINT, TipoVariabile.BPSIMPCALC, TipoVariabile.GIOIMPCALC);
		DatiPassoLavorazione passo = new DatiPassoLavorazione();
		passo.setIdTransizione(dati.getTransizione().getId());
		passo.setPasso(getPasso());
		
		calcoloVariabiliRiepilogo(variabiliCalcolo);

		eseguiControlliRiepilogoSanzioni(variabiliCalcolo, mappaEsiti);

		valutaEsito(passo, variabiliCalcolo, mappaEsiti);

		return passo;
	}

	private void calcoloVariabiliRiepilogo(MapVariabili variabiliCalcolo) {

		// calcolo variabili di output

		if (variabiliCalcolo.get(TipoVariabile.BPSIMPSANZ) != null || variabiliCalcolo.get(TipoVariabile.BPSIMPSANZREC) != null || variabiliCalcolo.get(TipoVariabile.GIOIMPSANZREC) != null
				|| variabiliCalcolo.get(TipoVariabile.GREIMPSANZ) != null || variabiliCalcolo.get(TipoVariabile.GIOIMPSANZ) != null) {
			variabiliCalcolo.setVal(TipoVariabile.SANZTOT,
					variabiliCalcolo.add(TipoVariabile.BPSIMPSANZ, TipoVariabile.BPSIMPSANZREC, TipoVariabile.GIOIMPSANZREC, TipoVariabile.GREIMPSANZ, TipoVariabile.GIOIMPSANZ));
		} else {
			variabiliCalcolo.setVal(TipoVariabile.SANZTOT, BigDecimal.ZERO);
		}

		if (!variabiliCalcolo.get(TipoVariabile.SANZTOT).getValNumber().equals(BigDecimal.ZERO)) {
			if (variabiliCalcolo.subtract(TipoVariabile.SANZTOT, variabiliCalcolo.add(TipoVariabile.BPSIMPAMM, TipoVariabile.GREIMPAMM, TipoVariabile.GIOIMPAMM)).compareTo(BigDecimal.ZERO) <= 0) {
				variabiliCalcolo.setVal(TipoVariabile.IMPSANZNORISC, BigDecimal.ZERO);
			} else {
				variabiliCalcolo.setVal(TipoVariabile.IMPSANZNORISC,
						variabiliCalcolo.subtract(TipoVariabile.SANZTOT, variabiliCalcolo.add(TipoVariabile.BPSIMPAMM, TipoVariabile.GREIMPAMM, TipoVariabile.GIOIMPAMM)));
			}
		}

		// max(BPSIMPAMM-(NVL(BPSIMPSANZ;0)+NVL(BPSIMPSANZREC;0));0)
		if (variabiliCalcolo.get(TipoVariabile.BPSIMPAMM) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPAMM).getValNumber() != null) {
			variabiliCalcolo.setVal(TipoVariabile.BPSIMPCALCINT, getBpsImpCalcInt(variabiliCalcolo));
		}

		// max(GREIMPAMM-NVL(GREIMPSANZ;0);0)
		if (variabiliCalcolo.get(TipoVariabile.GREIMPAMM) != null && variabiliCalcolo.get(TipoVariabile.GREIMPAMM).getValNumber() != null)
			variabiliCalcolo.setVal(TipoVariabile.GREIMPCALCINT, getGreImpCalcInt(variabiliCalcolo));

		// max(GIOIMPAMM-(NVL(GIOIMPSANZ;0)+NVL(GIOIMPSANZREC;0));0)
		if (variabiliCalcolo.get(TipoVariabile.GIOIMPAMM) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPAMM).getValNumber() != null)
			variabiliCalcolo.setVal(TipoVariabile.GIOIMPCALCINT, getGioImpCalcInt(variabiliCalcolo));

		variabiliCalcolo.setVal(TipoVariabile.IMPSANZINTERINT, getImpSanzInteriInt(variabiliCalcolo));

		if (variabiliCalcolo.get(TipoVariabile.BPSIMPCALCINT) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPCALCINT).getValNumber() != null
				&& variabiliCalcolo.get(TipoVariabile.IMPSANZINTERINT) != null && variabiliCalcolo.get(TipoVariabile.IMPSANZINTERINT).getValNumber() != null)
			variabiliCalcolo.setVal(TipoVariabile.BPSIMPCALC, getBpsImpCalc(variabiliCalcolo));

		if (variabiliCalcolo.get(TipoVariabile.GREIMPCALCINT) != null && variabiliCalcolo.get(TipoVariabile.GREIMPCALCINT).getValNumber() != null
				&& variabiliCalcolo.get(TipoVariabile.IMPSANZINTERINT) != null && variabiliCalcolo.get(TipoVariabile.IMPSANZINTERINT).getValNumber() != null
				&& variabiliCalcolo.get(TipoVariabile.BPSIMPCALCINT) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPCALCINT).getValNumber() != null)
			variabiliCalcolo.setVal(TipoVariabile.GREIMPCALC, getGreImpCalc(variabiliCalcolo));

		if (variabiliCalcolo.get(TipoVariabile.GIOIMPCALCINT) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPCALCINT).getValNumber() != null
				&& variabiliCalcolo.get(TipoVariabile.IMPSANZINTERINT) != null && variabiliCalcolo.get(TipoVariabile.IMPSANZINTERINT).getValNumber() != null
				&& variabiliCalcolo.get(TipoVariabile.BPSIMPCALCINT) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPCALCINT).getValNumber() != null
				&& variabiliCalcolo.get(TipoVariabile.GREIMPCALCINT) != null && variabiliCalcolo.get(TipoVariabile.GREIMPCALCINT).getValNumber() != null)
			variabiliCalcolo.setVal(TipoVariabile.GIOIMPCALC, getGioImpCalc(variabiliCalcolo));

	}

	private void eseguiControlliRiepilogoSanzioni(MapVariabili variabiliCalcolo, HashMap<TipoControllo, EsitoControllo> mappaEsiti) {

		mappaEsiti.put(TipoControllo.BRIDUSDC031_sanzioniComminate, new EsitoControllo(TipoControllo.BRIDUSDC031_sanzioniComminate, getSanzioniCumminate(variabiliCalcolo)));
	}

	private void valutaEsito(DatiPassoLavorazione passo, MapVariabili variabiliCalcolo, HashMap<TipoControllo, EsitoControllo> mapEsiti) {
		List<VariabileCalcolo> listaOutput = new ArrayList<VariabileCalcolo>();
		passo.setEsito(true);
		if (bridusdc021IsFalseOrNull(mapEsiti) && mapEsiti.get(TipoControllo.BRIDUSDC055_sanzioneGreening).getEsito() == false
				&& mapEsiti.get(TipoControllo.BRIDUSDC060_sanzioniGiovane).getEsito() == false) {

			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_042.getCodiceEsito());
		}

		if ((!bridusdc021IsFalseOrNull(mapEsiti) || mapEsiti.get(TipoControllo.BRIDUSDC055_sanzioneGreening).getEsito() == true
				|| mapEsiti.get(TipoControllo.BRIDUSDC060_sanzioniGiovane).getEsito() == true) && mapEsiti.get(TipoControllo.BRIDUSDC031_sanzioniComminate).getEsito() == true) {

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.SANZTOT));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.IMPSANZNORISC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPCALCINT));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPCALCINT));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPCALCINT));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.IMPSANZINTERINT));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPCALC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPCALC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPCALC));
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_043.getCodiceEsito());
		}

		if ((!bridusdc021IsFalseOrNull(mapEsiti) || mapEsiti.get(TipoControllo.BRIDUSDC055_sanzioneGreening).getEsito() == true
				|| mapEsiti.get(TipoControllo.BRIDUSDC060_sanzioniGiovane).getEsito() == true) && mapEsiti.get(TipoControllo.BRIDUSDC031_sanzioniComminate).getEsito() == false) {

			listaOutput.add(variabiliCalcolo.get(TipoVariabile.SANZTOT));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.IMPSANZNORISC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPCALCINT));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPCALCINT));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPCALCINT));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.IMPSANZINTERINT));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPCALC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GREIMPCALC));
			listaOutput.add(variabiliCalcolo.get(TipoVariabile.GIOIMPCALC));
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUF_013.getCodiceEsito());
			passo.setEsito(false);
		}
		// Memorizzo lista esiti controlli effettuati
		mapEsiti.values().forEach(e -> {
			passo.getDatiSintesi().getEsitiControlli().add(e);
		});

	}

	private boolean getSanzioniCumminate(MapVariabili variabiliCalcolo) {
		if (variabiliCalcolo.get(TipoVariabile.IMPSANZNORISC).getValNumber().compareTo(BigDecimal.ZERO) == 0) {
			return true;

		} else
			return false;
	}

	private BigDecimal getBpsImpCalcInt(MapVariabili variabiliCalcolo) {

		return variabiliCalcolo.get(TipoVariabile.BPSIMPAMM).getValNumber()
				.subtract(variabiliCalcolo.add(
						variabiliCalcolo.get(TipoVariabile.BPSIMPSANZ) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPSANZ).getValNumber() != null
								? variabiliCalcolo.get(TipoVariabile.BPSIMPSANZ).getValNumber()
								: BigDecimal.ZERO,
						variabiliCalcolo.get(TipoVariabile.BPSIMPSANZREC) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPSANZREC).getValNumber() != null
								? variabiliCalcolo.get(TipoVariabile.BPSIMPSANZREC).getValNumber()
								: BigDecimal.ZERO))
				.max(BigDecimal.ZERO);

	}

	private BigDecimal getGreImpCalcInt(MapVariabili variabiliCalcolo) {

		return variabiliCalcolo.get(TipoVariabile.GREIMPAMM).getValNumber()
				.subtract(variabiliCalcolo.get(TipoVariabile.GREIMPSANZ) != null && variabiliCalcolo.get(TipoVariabile.GREIMPSANZ).getValNumber() != null
						? variabiliCalcolo.get(TipoVariabile.GREIMPSANZ).getValNumber()
						: BigDecimal.ZERO)
				.max(BigDecimal.ZERO);
	}

	private BigDecimal getGioImpCalcInt(MapVariabili variabiliCalcolo) {

		return variabiliCalcolo.get(TipoVariabile.GIOIMPAMM).getValNumber()
				.subtract(variabiliCalcolo.add(
						variabiliCalcolo.get(TipoVariabile.GIOIMPSANZ) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPSANZ).getValNumber() != null
								? variabiliCalcolo.get(TipoVariabile.GIOIMPSANZ).getValNumber()
								: BigDecimal.ZERO,
						variabiliCalcolo.get(TipoVariabile.GIOIMPSANZREC) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPSANZREC).getValNumber() != null
								? variabiliCalcolo.get(TipoVariabile.GIOIMPSANZREC).getValNumber()
								: BigDecimal.ZERO))
				.max(BigDecimal.ZERO);

	}

	private BigDecimal getImpSanzInteriInt(MapVariabili variabiliCalcolo) {
		BigDecimal sanzioneGreening = null;
		BigDecimal sanzioneGiovane = null;
		BigDecimal impSanzionatoBps = BigDecimal.ZERO;
		BigDecimal impSanzionatoGiovane = BigDecimal.ZERO;

		if (variabiliCalcolo.get(TipoVariabile.BPSIMPSANZ) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPSANZ).getValNumber() != null)
			impSanzionatoBps = variabiliCalcolo.get(TipoVariabile.BPSIMPAMM).getValNumber().subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPSANZ).getValNumber());

		if (variabiliCalcolo.get(TipoVariabile.BPSIMPSANZREC) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPSANZREC).getValNumber() != null)
			impSanzionatoBps = impSanzionatoBps.subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPSANZREC).getValNumber());

		BigDecimal sanzioneBps = impSanzionatoBps.min(BigDecimal.ZERO);

		if (variabiliCalcolo.get(TipoVariabile.GREIMPAMM) != null && variabiliCalcolo.get(TipoVariabile.GREIMPAMM).getValNumber() != null && variabiliCalcolo.get(TipoVariabile.GREIMPSANZ) != null
				&& variabiliCalcolo.get(TipoVariabile.GREIMPSANZ).getValNumber() != null)
			sanzioneGreening = variabiliCalcolo.subtract(TipoVariabile.GREIMPAMM, TipoVariabile.GREIMPSANZ).min(BigDecimal.ZERO);

		if (variabiliCalcolo.get(TipoVariabile.GIOIMPAMM) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPAMM).getValNumber() != null) {
			if (variabiliCalcolo.get(TipoVariabile.GIOIMPSANZ) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPSANZ).getValNumber() != null)
				impSanzionatoGiovane = variabiliCalcolo.get(TipoVariabile.GIOIMPAMM).getValNumber().subtract(variabiliCalcolo.get(TipoVariabile.GIOIMPSANZ).getValNumber());

			if (variabiliCalcolo.get(TipoVariabile.GIOIMPSANZREC) != null && variabiliCalcolo.get(TipoVariabile.GIOIMPSANZREC).getValNumber() != null)
				impSanzionatoGiovane = impSanzionatoGiovane.subtract(variabiliCalcolo.get(TipoVariabile.GIOIMPSANZREC).getValNumber());
		}
		sanzioneGiovane = impSanzionatoGiovane.min(BigDecimal.ZERO);

		BigDecimal risultato = sanzioneBps;
		if (sanzioneGreening != null)
			risultato = risultato.add(sanzioneGreening);

		if (sanzioneGiovane != null)
			risultato = risultato.add(sanzioneGiovane);

		return risultato.negate();
	}

	private BigDecimal getBpsImpCalc(MapVariabili variabiliCalcolo) {

		if (variabiliCalcolo.get(TipoVariabile.BPSIMPCALCINT).getValNumber().compareTo(BigDecimal.ZERO) > 0) {

			return BigDecimal.ZERO.max(variabiliCalcolo.max(TipoVariabile.BPSIMPCALCINT, TipoVariabile.IMPSANZINTERINT));
		} else {
			return BigDecimal.ZERO;

		}
	}

	private BigDecimal getGreImpCalc(MapVariabili variabiliCalcolo) {

		if (variabiliCalcolo.get(TipoVariabile.GREIMPCALCINT).getValNumber().compareTo(BigDecimal.ZERO) > 0) {

			return BigDecimal.ZERO.max(variabiliCalcolo.get(TipoVariabile.GREIMPCALCINT).getValNumber()
					.subtract(BigDecimal.ZERO.max(variabiliCalcolo.subtract(TipoVariabile.IMPSANZINTERINT, TipoVariabile.BPSIMPCALCINT))));
		} else {
			return BigDecimal.ZERO;
		}

	}

	private BigDecimal getGioImpCalc(MapVariabili variabiliCalcolo) {

		if (variabiliCalcolo.get(TipoVariabile.GIOIMPCALCINT).getValNumber().compareTo(BigDecimal.ZERO) > 0) {

			return BigDecimal.ZERO.max(variabiliCalcolo.get(TipoVariabile.GIOIMPCALCINT).getValNumber().subtract(BigDecimal.ZERO
					.max(variabiliCalcolo.subtract(TipoVariabile.IMPSANZINTERINT, TipoVariabile.BPSIMPCALCINT).subtract(variabiliCalcolo.get(TipoVariabile.BPSIMPCALCINT).getValNumber()))));

		} else {
			return BigDecimal.ZERO;
		}

	}

	@Override
	public TipologiaPassoTransizione getPasso() {
		return TipologiaPassoTransizione.RIEPILOGO_SANZIONI;
	}

	private boolean bridusdc021IsFalseOrNull(HashMap<TipoControllo, EsitoControllo> mappaEsiti) {
		EsitoControllo esitoControlloBridusdc021 = mappaEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni);
		return esitoControlloBridusdc021 == null || esitoControlloBridusdc021.getValString().equalsIgnoreCase("false");
	}
}
