package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
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
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Service
public class PassoCalcoloSanzioniService extends PassoDatiElaborazioneIstruttoria {

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
		variabiliCalcolo.addElements(TipoVariabile.BPSRECIDIVA, TipoVariabile.BPSYELLOWCARD, TipoVariabile.BPSIMPSANZ, TipoVariabile.BPSIMPSANZREC);
		DatiPassoLavorazione passo = new DatiPassoLavorazione();
		passo.setIdTransizione(dati.getTransizione().getId());
		passo.setPasso(getPasso());

		// Verifico la liquidazione della domanda dell'anno precedente del CUAA per poter verificare l'eventuale recidiva.
		// N.B. Nel caso in cui la domanda dell'anno precedente non risulti pagata => il calcolo si interrompe.
		mappaEsiti.put(TipoControllo.BRIDUSDC109_isDomandaLiqAnnoPrec,
				new EsitoControllo(TipoControllo.BRIDUSDC109_isDomandaLiqAnnoPrec, variabiliCalcolo.get(TipoVariabile.BPSDOMLIQANNOPREC).getValBoolean()));

		if (variabiliCalcolo.get(TipoVariabile.BPSDOMLIQANNOPREC).getValBoolean()) {
			calcoloVariabiliSanzioni(variabiliCalcolo);
			eseguiControlliSanzioni(variabiliCalcolo, mappaEsiti);

		}

		valutaEsito(passo, variabiliCalcolo, mappaEsiti);

		return passo;
	}


	public void calcoloVariabiliSanzioni(MapVariabili variabiliCalcolo) {

		// calcolo delle variabili di output

		variabiliCalcolo.add(TipoVariabile.BPSRECIDIVA, new VariabileCalcolo(TipoVariabile.BPSRECIDIVA, variabiliCalcolo.get(TipoVariabile.BPSYCSANZANNIPREC).getValBoolean()));
		if (variabiliCalcolo.get(TipoVariabile.BPSYCIMPSANZAPREC).getValNumber().compareTo(BigDecimal.ZERO) > 0) {
			variabiliCalcolo.add(TipoVariabile.BPSIMPSANZREC, new VariabileCalcolo(TipoVariabile.BPSIMPSANZREC, variabiliCalcolo.get(TipoVariabile.BPSYCIMPSANZAPREC).getValNumber()));
		}
		if (variabiliCalcolo.get(TipoVariabile.BPSPERCSCOST).getValNumber().compareTo(BigDecimal.valueOf(0.10)) <= 0) {
			variabiliCalcolo.setVal(TipoVariabile.BPSYELLOWCARD, !variabiliCalcolo.get(TipoVariabile.BPSRECIDIVA).getValBoolean());
		} else {
			variabiliCalcolo.setVal(TipoVariabile.BPSYELLOWCARD, false);

		}
		if (variabiliCalcolo.get(TipoVariabile.BPSYELLOWCARD).getValBoolean() == true) {
			variabiliCalcolo.setVal(TipoVariabile.BPSIMPSANZ, variabiliCalcolo.multiply(TipoVariabile.BPSSUPSCOST, TipoVariabile.TITVALRID).multiply(BigDecimal.valueOf(0.75)));
		} else {
			variabiliCalcolo.setVal(TipoVariabile.BPSIMPSANZ,
					variabiliCalcolo.min(TipoVariabile.BPSIMPRIC, variabiliCalcolo.multiply(TipoVariabile.BPSSUPSCOST, TipoVariabile.TITVALRID).multiply(BigDecimal.valueOf(1.5))));
		}
	}

	public void eseguiControlliSanzioni(MapVariabili variabiliCalcolo,
			HashMap<TipoControllo, EsitoControllo> mappaEsiti) {

		mappaEsiti.put(TipoControllo.BRIDUSDC029_recidiva, new EsitoControllo(TipoControllo.BRIDUSDC029_recidiva, getRecidiva(variabiliCalcolo)));

		mappaEsiti.put(TipoControllo.BRIDUSDC030_sanzioniAnnoPrec, new EsitoControllo(TipoControllo.BRIDUSDC030_sanzioniAnnoPrec, getSanzioniAnnoPrec(variabiliCalcolo)));
		if (!bridusdc021IsFalseOrNull(mappaEsiti)) {
			mappaEsiti.put(TipoControllo.BRIDUSDC032_yellowCard, new EsitoControllo(TipoControllo.BRIDUSDC032_yellowCard, getYellowCard(variabiliCalcolo)));
		}
	}



	public void valutaEsito(DatiPassoLavorazione passo, MapVariabili variabiliCalcolo,
			HashMap<TipoControllo, EsitoControllo> mapEsiti) {
		List<VariabileCalcolo> listaOutput = new ArrayList<VariabileCalcolo>();
		passo.getDatiOutput().setVariabiliCalcolo(listaOutput);

		if (mapEsiti.get(TipoControllo.BRIDUSDC109_isDomandaLiqAnnoPrec).getEsito()) {
			if (bridusdc021IsFalseOrNull(mapEsiti)) {
				passo.setCodiceEsito(CodiceEsito.DUT_019.getCodiceEsito());
				// se non ci sono sanzioni mostra solo BPSYCSANZANNIPREC e BPSYCIMPSANZAPREC
				passo.getDatiInput().setVariabiliCalcolo(passo.getDatiInput().getVariabiliCalcolo().stream()
						.filter(v -> v.getTipoVariabile().equals(TipoVariabile.BPSYCSANZANNIPREC) || v.getTipoVariabile().equals(TipoVariabile.BPSYCIMPSANZAPREC)).collect(Collectors.toList()));

			} else if (!bridusdc021IsFalseOrNull(mapEsiti) && mapEsiti.get(TipoControllo.BRIDUSDC029_recidiva).getEsito() == false
					&& mapEsiti.get(TipoControllo.BRIDUSDC032_yellowCard).getEsito() == true) {
				passo.setCodiceEsito(CodiceEsito.DUT_020.getCodiceEsito());

			} else if (!bridusdc021IsFalseOrNull(mapEsiti) && mapEsiti.get(TipoControllo.BRIDUSDC029_recidiva).getEsito() == false
					&& mapEsiti.get(TipoControllo.BRIDUSDC032_yellowCard).getEsito() == false) {
				passo.setCodiceEsito(CodiceEsito.DUT_021.getCodiceEsito());

			} else if (!bridusdc021IsFalseOrNull(mapEsiti) && mapEsiti.get(TipoControllo.BRIDUSDC029_recidiva).getEsito() == true
					&& mapEsiti.get(TipoControllo.BRIDUSDC030_sanzioniAnnoPrec).getEsito() == false) {
				passo.setCodiceEsito(CodiceEsito.DUT_022.getCodiceEsito());

			} else if (!bridusdc021IsFalseOrNull(mapEsiti) && mapEsiti.get(TipoControllo.BRIDUSDC029_recidiva).getEsito() == true
					&& mapEsiti.get(TipoControllo.BRIDUSDC030_sanzioniAnnoPrec).getEsito() == true) {
				passo.setCodiceEsito(CodiceEsito.DUT_023.getCodiceEsito());
			}

			if (!passo.getCodiceEsito().equals(CodiceEsito.DUT_019.getCodiceEsito())) {
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSRECIDIVA));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSYELLOWCARD));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPSANZ));
				listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPSANZREC));
			}

			passo.setEsito(true);

		} else {
			passo.setCodiceEsito(CodiceEsito.DUF_027.getCodiceEsito());
			passo.setEsito(false);
		}

		// Memorizzo lista esiti controlli effettuati
		mapEsiti.values().forEach(e -> {
			if ((e.getTipoControllo().equals(TipoControllo.BRIDUSDC030_sanzioniAnnoPrec) && !bridusdc021IsFalseOrNull(mapEsiti))
					|| !e.getTipoControllo().equals(TipoControllo.BRIDUSDC030_sanzioniAnnoPrec)) {

				passo.getDatiSintesi().getEsitiControlli().add(e);
			}

		});

	}

	public boolean getRecidiva(MapVariabili variabiliCalcolo) {
		if (variabiliCalcolo.get(TipoVariabile.BPSRECIDIVA).getValBoolean() && variabiliCalcolo.get(TipoVariabile.BPSIMPSANZ) != null
				&& variabiliCalcolo.get(TipoVariabile.BPSIMPSANZ).getValNumber() != null && variabiliCalcolo.get(TipoVariabile.BPSIMPSANZ).getValNumber().compareTo(BigDecimal.ZERO) > 0) {
			return true;
		} else
			return false;
	}

	public boolean getSanzioniAnnoPrec(MapVariabili variabiliCalcolo) {
		if (variabiliCalcolo.get(TipoVariabile.BPSIMPSANZREC) != null && variabiliCalcolo.get(TipoVariabile.BPSIMPSANZREC).getValNumber() != null
				&& variabiliCalcolo.get(TipoVariabile.BPSIMPSANZREC).getValNumber().compareTo(BigDecimal.ZERO) > 0) {
			return true;
		} else
			return false;
	}

	public boolean getYellowCard(MapVariabili variabiliCalcolo) {
		if (variabiliCalcolo.get(TipoVariabile.BPSYELLOWCARD).getValBoolean() == true) {
			return true;
		} else
			return false;
	}

	@Override
	public TipologiaPassoTransizione getPasso() {
		return TipologiaPassoTransizione.SANZIONI_BPS;
	}

	private boolean bridusdc021IsFalseOrNull(HashMap<TipoControllo, EsitoControllo> mappaEsiti) {
		EsitoControllo esitoControlloBridusdc021 = mappaEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni);
		return esitoControlloBridusdc021 == null || esitoControlloBridusdc021.getValString().equalsIgnoreCase("false");
	}
}
