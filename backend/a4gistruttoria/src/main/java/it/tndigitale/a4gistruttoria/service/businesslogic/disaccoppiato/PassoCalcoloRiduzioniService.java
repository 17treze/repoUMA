package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import it.tndigitale.a4gistruttoria.dto.lavorazione.*;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.RichiestaSuperficieDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.DomandeService;
import it.tndigitale.a4gistruttoria.service.businesslogic.PassoDatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class PassoCalcoloRiduzioniService extends PassoDatiElaborazioneIstruttoria {

	private static final Logger logger = LoggerFactory.getLogger(PassoCalcoloRiduzioniService.class);

	@Autowired
	DomandeService domandaService;
	@Autowired
    DomandaUnicaDao daoDomanda;
	@Autowired
	RichiestaSuperficieDao daoRichiestaSuperficie;

	public static final String SANZIONIFALSE = "false";
	public static final String SANZIONIINF10 = "inf_10";
	public static final String SANZIONISUP10 = "sup_10";


	@Override
	protected DatiPassoLavorazione elaboraPasso(final DatiElaborazioneIstruttoria dati, 
			final MapVariabili variabiliCalcolo,
			final HashMap<TipoControllo, EsitoControllo> mappaEsiti) {
		// lista variabili di calcolo
		variabiliCalcolo.addElements(TipoVariabile.BPSSUPDET, TipoVariabile.BPSIMPAMM, TipoVariabile.BPSPERCSCOST, TipoVariabile.BPSSUPSCOST, TipoVariabile.BPSSUPAMM, TipoVariabile.BPSIMPRID);
		DatiPassoLavorazione passo = new DatiPassoLavorazione();
		passo.setIdTransizione(dati.getTransizione().getId());
		passo.setPasso(getPasso());
		IstruttoriaModel istruttoria = dati.getIstruttoria();
		
		calcoloVariabiliBPS(variabiliCalcolo, istruttoria);

		eseguiControlliBPS(variabiliCalcolo, mappaEsiti, istruttoria);

		valutaEsito(passo, variabiliCalcolo, mappaEsiti);

		VariabileCalcolo pfSupDet = calcolaSupDetParticella(variabiliCalcolo, istruttoria);
		if (pfSupDet != null) {
			List<VariabileCalcolo> listaParticellaColtura = new ArrayList<>();
			listaParticellaColtura.add(pfSupDet);
			passo.getDatiSintesi().setVariabiliParticellaColtura(listaParticellaColtura);
			passo.getDatiSintesi().getVariabiliCalcolo().add(pfSupDet);
		}
		return passo;
	}

	public void calcoloVariabiliBPS(final MapVariabili variabiliCalcolo, final IstruttoriaModel istruttoria) {
		// calcolo delle variabili di output
		if (!isCampione(variabiliCalcolo, istruttoria)) {
			variabiliCalcolo.setVal(TipoVariabile.BPSSUPDET,
					variabiliCalcolo.subtract(TipoVariabile.BPSSUPELE, variabiliCalcolo.add(TipoVariabile.BPSSUPSCOSTMAN, TipoVariabile.BPSSUPSCOSTCOO)).max(BigDecimal.ZERO));
		} else {
			variabiliCalcolo.setVal(TipoVariabile.BPSSUPDET,
					variabiliCalcolo.subtract(TipoVariabile.BPSSUPSIGECO, variabiliCalcolo.add(TipoVariabile.BPSSUPSCOSTMAN, TipoVariabile.BPSSUPSCOSTCOO)).max(BigDecimal.ZERO));
		}

		if (variabiliCalcolo.get(TipoVariabile.BPSSUPDETIST) != null && variabiliCalcolo.get(TipoVariabile.BPSSUPDETIST).getValNumber() != null) {
			if (variabiliCalcolo.subtract(TipoVariabile.BPSSUPRIC, TipoVariabile.BPSSUPDETIST).compareTo(BigDecimal.valueOf(0.1000)) <= 0) {
				variabiliCalcolo.setVal(TipoVariabile.BPSSUPAMM, variabiliCalcolo.get(TipoVariabile.BPSSUPRIC).getValNumber());
			} else {
				variabiliCalcolo.setVal(TipoVariabile.BPSSUPAMM, variabiliCalcolo.min(TipoVariabile.BPSSUPRIC, TipoVariabile.BPSSUPDETIST));
			}
		} else if (variabiliCalcolo.subtract(TipoVariabile.BPSSUPRIC, TipoVariabile.BPSSUPDET).compareTo(BigDecimal.valueOf(0.1000)) <= 0) {
			variabiliCalcolo.setVal(TipoVariabile.BPSSUPAMM, variabiliCalcolo.get(TipoVariabile.BPSSUPRIC).getValNumber());
		} else {
			variabiliCalcolo.setVal(TipoVariabile.BPSSUPAMM, variabiliCalcolo.min(TipoVariabile.BPSSUPRIC, TipoVariabile.BPSSUPDET));
		}
		variabiliCalcolo.setVal(TipoVariabile.BPSIMPAMM, variabiliCalcolo.multiply(TipoVariabile.BPSSUPAMM, TipoVariabile.TITVALRID));
		variabiliCalcolo.setVal(TipoVariabile.BPSSUPSCOST, variabiliCalcolo.subtract(TipoVariabile.BPSSUPRIC, TipoVariabile.BPSSUPAMM));
		variabiliCalcolo.setVal(TipoVariabile.BPSIMPRID, variabiliCalcolo.multiply(TipoVariabile.BPSSUPSCOST, TipoVariabile.TITVALRID));
		if (variabiliCalcolo.get(TipoVariabile.BPSSUPAMM).getValNumber().compareTo(BigDecimal.ZERO) > 0) {
			variabiliCalcolo.setVal(TipoVariabile.BPSPERCSCOST, variabiliCalcolo.divide(TipoVariabile.BPSSUPSCOST, TipoVariabile.BPSSUPAMM));
		} else {
			variabiliCalcolo.setVal(TipoVariabile.BPSPERCSCOST, new BigDecimal(1));
		}
	}

	public void eseguiControlliBPS(final MapVariabili variabiliCalcolo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final IstruttoriaModel istruttoria) {
		boolean hasRiduzione = hasRiduzione(variabiliCalcolo);
		boolean hasAnomalieCoordinamento = hasAnomalieCoordinamento(variabiliCalcolo);
		mapEsiti.put(TipoControllo.BRIDUSDC020_idDomandaRiduzione, new EsitoControllo(TipoControllo.BRIDUSDC020_idDomandaRiduzione, hasRiduzione));
		mapEsiti.put(TipoControllo.BRIDUSDC022_idDomandaCampione, new EsitoControllo(TipoControllo.BRIDUSDC022_idDomandaCampione, isCampione(variabiliCalcolo, istruttoria)));
		mapEsiti.put(TipoControllo.BRIDUSDC035_supMinimaAmmessa, new EsitoControllo(TipoControllo.BRIDUSDC035_supMinimaAmmessa, isSupMinimaAmmissibile(variabiliCalcolo)));
		mapEsiti.put(TipoControllo.BRIDUSDC135_isAnomalieCoordinamento, new EsitoControllo(TipoControllo.BRIDUSDC135_isAnomalieCoordinamento, hasAnomalieCoordinamento));
		if (hasRiduzione) {
			String sanzioneDomanda = getSanzioneDomanda(variabiliCalcolo);
			mapEsiti.put(TipoControllo.BRIDUSDC021_idDomandaSanzioni,
					new EsitoControllo(TipoControllo.BRIDUSDC021_idDomandaSanzioni, !sanzioneDomanda.equalsIgnoreCase(SANZIONIFALSE), sanzioneDomanda));
		}
		// il controllo BRIDUSDC034_sigeco viene effettuato solo per le domande a campione
		if (mapEsiti.get(TipoControllo.BRIDUSDC022_idDomandaCampione).getEsito().booleanValue())
			mapEsiti.put(TipoControllo.BRIDUSDC034_sigeco, new EsitoControllo(TipoControllo.BRIDUSDC034_sigeco, getSuperficieSIGECO(variabiliCalcolo)));
	}
	
	private void valutaEsitoBr022Br034Br020Br135Br035(final DatiPassoLavorazione passo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final List<VariabileCalcolo> listaOutput) {
		if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONIFALSE)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_071.getCodiceEsito());
		} else if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONIINF10)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_072.getCodiceEsito());
		} else if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONISUP10)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_073.getCodiceEsito());
		}
	}
	
	private void valutaEsitoBr022Br034Br020Br135NotBr035(final DatiPassoLavorazione passo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final List<VariabileCalcolo> listaOutput) {
		if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONIFALSE)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_074.getCodiceEsito());
		} else if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONIINF10)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_075.getCodiceEsito());
		} else if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONISUP10)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_076.getCodiceEsito());
		}
	}
	
	private void valutaEsitoBr022Br034Br020Br135(final DatiPassoLavorazione passo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final List<VariabileCalcolo> listaOutput) {
		// 022 NUOVO RAMO
		if (mapEsiti.get(TipoControllo.BRIDUSDC035_supMinimaAmmessa).getEsito().booleanValue()) {
			valutaEsitoBr022Br034Br020Br135Br035(passo, mapEsiti, listaOutput);
		} else if (!mapEsiti.get(TipoControllo.BRIDUSDC035_supMinimaAmmessa).getEsito().booleanValue()) {
			valutaEsitoBr022Br034Br020Br135NotBr035(passo, mapEsiti, listaOutput);
		}
	}
	
	private void valutaEsitoBr022Br034Br020NotBr135Br035(final DatiPassoLavorazione passo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final List<VariabileCalcolo> listaOutput) {
		if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONIFALSE)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_013.getCodiceEsito());
		} else if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONIINF10)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_014.getCodiceEsito());
		} else if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONISUP10)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_015.getCodiceEsito());
		}
	}
	
	private void valutaEsitoBr022Br034Br020NotBr135NotBr035(final DatiPassoLavorazione passo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final List<VariabileCalcolo> listaOutput) {
		if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONIFALSE)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_016.getCodiceEsito());
		} else if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONIINF10)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_017.getCodiceEsito());
		} else if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONISUP10)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_018.getCodiceEsito());
		}
	}
	
	private void valutaEsitoBr022Br034Br020NotBr135(final DatiPassoLavorazione passo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final List<VariabileCalcolo> listaOutput) {
		// 022 VECCHIO RAMO
		if (mapEsiti.get(TipoControllo.BRIDUSDC035_supMinimaAmmessa).getEsito().booleanValue()) {
			valutaEsitoBr022Br034Br020NotBr135Br035(passo, mapEsiti, listaOutput);
		} else if (!mapEsiti.get(TipoControllo.BRIDUSDC035_supMinimaAmmessa).getEsito().booleanValue()) {
			valutaEsitoBr022Br034Br020NotBr135NotBr035(passo, mapEsiti, listaOutput);
		}
	}
	
	private void valutaEsitoBr022Br034Br020(final DatiPassoLavorazione passo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final List<VariabileCalcolo> listaOutput) {
		if (mapEsiti.get(TipoControllo.BRIDUSDC135_isAnomalieCoordinamento).getEsito().booleanValue()) {
			valutaEsitoBr022Br034Br020Br135(passo, mapEsiti, listaOutput);
		} else if (!mapEsiti.get(TipoControllo.BRIDUSDC135_isAnomalieCoordinamento).getEsito().booleanValue()) {
			valutaEsitoBr022Br034Br020NotBr135(passo, mapEsiti, listaOutput);
		}
	}
	
	private void valutaEsitoBr022Br034(final DatiPassoLavorazione passo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final List<VariabileCalcolo> listaOutput) {
		if (mapEsiti.get(TipoControllo.BRIDUSDC020_idDomandaRiduzione).getEsito().booleanValue()) {
			valutaEsitoBr022Br034Br020(passo, mapEsiti, listaOutput);
		} else if (!mapEsiti.get(TipoControllo.BRIDUSDC020_idDomandaRiduzione).getEsito().booleanValue()) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_012.getCodiceEsito());
		}
	}
	
	private void valutaEsitoBr022(final DatiPassoLavorazione passo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final List<VariabileCalcolo> listaOutput) {
		if (mapEsiti.get(TipoControllo.BRIDUSDC034_sigeco).getEsito().booleanValue()) {
			valutaEsitoBr022Br034(passo, mapEsiti, listaOutput);
		} else if (!mapEsiti.get(TipoControllo.BRIDUSDC034_sigeco).getEsito().booleanValue()) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUF_012.getCodiceEsito());
			passo.setEsito(false);
		}
	}
	
	private void valutaEsitoNotBr022Br020NotBr135Br035(final DatiPassoLavorazione passo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final List<VariabileCalcolo> listaOutput) {
		if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONIFALSE)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_006.getCodiceEsito());
		} else if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONIINF10)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_007.getCodiceEsito());
		} else if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONISUP10)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_008.getCodiceEsito());
		}
	}
	
	private void valutaEsitoNotBr022Br020NotBr135NotBr035(final DatiPassoLavorazione passo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final List<VariabileCalcolo> listaOutput) {
		if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONIFALSE)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_009.getCodiceEsito());
		} else if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONIINF10)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_010.getCodiceEsito());
		} else if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONISUP10)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_011.getCodiceEsito());
		}
	}
	

	private void valutaEsitoNotBr022Br020Br135Br035(final DatiPassoLavorazione passo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final List<VariabileCalcolo> listaOutput) {
		if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONIFALSE)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_065.getCodiceEsito());
		} else if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONIINF10)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_066.getCodiceEsito());
		} else if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONISUP10)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_067.getCodiceEsito());
		}
	}
	
	private void valutaEsitoNotBr022Br020Br135NotBr035(final DatiPassoLavorazione passo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final List<VariabileCalcolo> listaOutput) {
		if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONIFALSE)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_068.getCodiceEsito());
		} else if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONIINF10)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_069.getCodiceEsito());
		} else if (mapEsiti.get(TipoControllo.BRIDUSDC021_idDomandaSanzioni).getValString().equalsIgnoreCase(SANZIONISUP10)) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_070.getCodiceEsito());
		}
	}
	
	private void valutaEsitoNotBr022Br020Br135(final DatiPassoLavorazione passo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final List<VariabileCalcolo> listaOutput) {
		// !022 VECCHIO RAMO
		if (mapEsiti.get(TipoControllo.BRIDUSDC035_supMinimaAmmessa).getEsito().booleanValue()) {
			valutaEsitoNotBr022Br020Br135Br035(passo, mapEsiti, listaOutput);
		} else if (!mapEsiti.get(TipoControllo.BRIDUSDC035_supMinimaAmmessa).getEsito().booleanValue()) {
			valutaEsitoNotBr022Br020Br135NotBr035(passo, mapEsiti, listaOutput);
		}
	}
	
	private void valutaEsitoNotBr022Br020NotBr135(final DatiPassoLavorazione passo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final List<VariabileCalcolo> listaOutput) {
		// !022 NUOVO RAMO
		if (mapEsiti.get(TipoControllo.BRIDUSDC035_supMinimaAmmessa).getEsito().booleanValue()) {
			valutaEsitoNotBr022Br020NotBr135Br035(passo, mapEsiti, listaOutput);
		} else if (!mapEsiti.get(TipoControllo.BRIDUSDC035_supMinimaAmmessa).getEsito().booleanValue()) {
			valutaEsitoNotBr022Br020NotBr135NotBr035(passo, mapEsiti, listaOutput);
		}
	}
	
	
	private void valutaEsitoNotBr022Br020(final DatiPassoLavorazione passo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final List<VariabileCalcolo> listaOutput) {
		
		if (mapEsiti.get(TipoControllo.BRIDUSDC135_isAnomalieCoordinamento).getEsito().booleanValue()) {
			valutaEsitoNotBr022Br020Br135(passo, mapEsiti, listaOutput);
		} else if (!mapEsiti.get(TipoControllo.BRIDUSDC135_isAnomalieCoordinamento).getEsito().booleanValue()) {
			valutaEsitoNotBr022Br020NotBr135(passo, mapEsiti, listaOutput);
		}
		/*
		if (mapEsiti.get(TipoControllo.BRIDUSDC035_supMinimaAmmessa).getEsito().booleanValue()) {
			valutaEsitoNotBr022Br020Br035(passo, mapEsiti, listaOutput);
		} else if (!mapEsiti.get(TipoControllo.BRIDUSDC035_supMinimaAmmessa).getEsito().booleanValue()) {
			valutaEsitoNotBr022Br020NotBr035(passo, mapEsiti, listaOutput);
		}
		*/
	}
	
	private void valutaEsitoNotBr022(final DatiPassoLavorazione passo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti,
			final List<VariabileCalcolo> listaOutput) {
		if (!mapEsiti.get(TipoControllo.BRIDUSDC020_idDomandaRiduzione).getEsito().booleanValue()) {
			passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
			passo.setCodiceEsito(CodiceEsito.DUT_005.getCodiceEsito());
		} else if (mapEsiti.get(TipoControllo.BRIDUSDC020_idDomandaRiduzione).getEsito().booleanValue()) {
			valutaEsitoNotBr022Br020(passo, mapEsiti, listaOutput);
		}
	}

	public void valutaEsito(final DatiPassoLavorazione passo,
			final MapVariabili variabiliCalcolo,
			final HashMap<TipoControllo, EsitoControllo> mapEsiti) {
		List<VariabileCalcolo> listaOutput = new ArrayList<>();
		passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
		passo.setEsito(true);
		
		if (mapEsiti.get(TipoControllo.BRIDUSDC022_idDomandaCampione).getEsito().booleanValue()) {
			valutaEsitoBr022(passo, mapEsiti, listaOutput);
		} else if (!mapEsiti.get(TipoControllo.BRIDUSDC022_idDomandaCampione).getEsito().booleanValue()) {
			valutaEsitoNotBr022(passo, mapEsiti, listaOutput);
		}
		listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPDET));
		listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPAMM));
		listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPAMM));
		listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSSUPSCOST));
		listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSPERCSCOST));
		listaOutput.add(variabiliCalcolo.get(TipoVariabile.BPSIMPRID));

		passo.getDatiOutput().setVariabiliCalcolo(listaOutput);

		// Memorizzo lista esiti controlli effettuati
		mapEsiti.values().forEach(e -> passo.getDatiSintesi().getEsitiControlli().add(e));
	}

	private boolean hasRiduzione(MapVariabili variabiliCalcolo) {
		return variabiliCalcolo.get(TipoVariabile.BPSSUPSCOST).getValNumber().compareTo(BigDecimal.ZERO) != 0;
	}
	
	private boolean hasAnomalieCoordinamento(final MapVariabili variabiliCalcolo) {
		/* IF BPSSUPSCOSTCOO > 0 THEN TRUE ELSE FALSE */
		return variabiliCalcolo.get(TipoVariabile.BPSSUPSCOSTCOO).getValNumber().compareTo(BigDecimal.ZERO) > 0;
	}

	private boolean isCampione(MapVariabili variabiliCalcolo, IstruttoriaModel istruttoria) {
		return variabiliCalcolo.get(TipoVariabile.ISCAMP).getValBoolean().booleanValue()
				&& !isIstruttoriaAnticipo(istruttoria);
	}
	
	private boolean isIstruttoriaAnticipo(IstruttoriaModel istruttoria) {
		return TipoIstruttoria.ANTICIPO.equals(istruttoria.getTipologia());
	}

	private boolean isSupMinimaAmmissibile(MapVariabili variabiliCalcolo) {
		return variabiliCalcolo.get(TipoVariabile.BPSSUPAMM).getValNumber().compareTo(BigDecimal.valueOf(0.5000)) >= 0;
	}

	private String getSanzioneDomanda(MapVariabili variabiliCalcolo) {
		if (variabiliCalcolo.get(TipoVariabile.BPSPERCSCOST).getValNumber().compareTo(BigDecimal.valueOf(0.03)) <= 0
				&& variabiliCalcolo.get(TipoVariabile.BPSSUPSCOST).getValNumber().compareTo(BigDecimal.valueOf(2)) <= 0) {
			return SANZIONIFALSE;
		} else if ((variabiliCalcolo.get(TipoVariabile.BPSPERCSCOST).getValNumber().compareTo(BigDecimal.valueOf(0.03)) <= 0
				&& variabiliCalcolo.get(TipoVariabile.BPSSUPSCOST).getValNumber().compareTo(BigDecimal.valueOf(2)) > 0)
				|| (variabiliCalcolo.get(TipoVariabile.BPSPERCSCOST).getValNumber().compareTo(BigDecimal.valueOf(0.03)) > 0
						&& variabiliCalcolo.get(TipoVariabile.BPSPERCSCOST).getValNumber().compareTo(BigDecimal.valueOf(0.1)) <= 0)) {
			return SANZIONIINF10;
		} else {
			return SANZIONISUP10;
		}

	}

	private boolean getSuperficieSIGECO(MapVariabili variabiliCalcolo) {
		return variabiliCalcolo.get(TipoVariabile.ISCAMP).getValBoolean() && (variabiliCalcolo.get(TipoVariabile.DOMSIGECOCHIUSA).getValBoolean());
	}

	private VariabileCalcolo calcolaSupDetParticella(MapVariabili variabiliCalcolo, IstruttoriaModel istruttoria) {
		VariabileCalcolo pfSupDet = new VariabileCalcolo(TipoVariabile.PFSUPDET);
		ArrayList<ParticellaColtura> listaSupDet = new ArrayList<>();
		ArrayList<ParticellaColtura> listaParticelle;
		if (variabiliCalcolo.get(TipoVariabile.PFANOMMAN) != null) {
			listaParticelle = variabiliCalcolo.get(TipoVariabile.PFANOMMAN).getValList();
			listaParticelle.forEach(pc -> {
				ParticellaColtura particellaColtura = new ParticellaColtura();
				Long idParticella = pc.getParticella().getIdParticella();
				particellaColtura.setParticella(pc.getParticella());
				particellaColtura.setColtura(pc.getColtura());
				particellaColtura.setLivello(pc.getLivello());
				boolean valAnomMan = pc.getValBool();
				if (variabiliCalcolo.get(TipoVariabile.PFANOMCOORD) != null) {
					ArrayList<ParticellaColtura> listaAnomCoord = variabiliCalcolo.get(TipoVariabile.PFANOMCOORD).getValList();
					Optional<ParticellaColtura> partAnomCoord = listaAnomCoord.stream()
							.filter(p -> (p.getParticella().getIdParticella().equals(idParticella) && p.getColtura().equals(pc.getColtura()))).findFirst();
					if (partAnomCoord.isPresent()) {
						boolean valAnomCoord = partAnomCoord.get().getValBool();
						// se per la particella sono presenti anomalie di mantenimento o coordinamento la superficie determinata è 0
						if (valAnomMan) {
							ArrayList<ParticellaColtura> listaPfSupEle = variabiliCalcolo.get(TipoVariabile.PFSUPELE).getValList();
							Optional<ParticellaColtura> partSupEle = listaPfSupEle.stream()
									.filter(p -> (p.getParticella().getIdParticella().equals(idParticella) && p.getColtura().equals(pc.getColtura()))).findFirst();
							if (partSupEle.isPresent()) {
								particellaColtura.setValBool(partSupEle.get().getValBool());
								particellaColtura.setValString(partSupEle.get().getValString());
								particellaColtura.setValNum(0F);
							} else {
								logger.info("La particella " + idParticella + "non risulta presente all'interno della lista delle superfici eleggibili");
							}
						} else if(!valAnomMan && valAnomCoord) {
							if (!isCampione(variabiliCalcolo, istruttoria)) {
//								MAX ((PFSUPELE-PFSUPANCOORD,0)
								if (variabiliCalcolo.get(TipoVariabile.PFSUPELE) != null) {
									ArrayList<ParticellaColtura> listaPfSupEle = variabiliCalcolo.get(TipoVariabile.PFSUPELE).getValList();
									Optional<ParticellaColtura> partSupEle = listaPfSupEle.stream()
											.filter(p -> (p.getParticella().getIdParticella().equals(idParticella) && p.getColtura().equals(pc.getColtura()))).findFirst();
									ArrayList<ParticellaColtura> listaPfSupAnCoord = variabiliCalcolo.get(TipoVariabile.PFSUPANCOORD).getValList();
									Optional<ParticellaColtura> partSupAnCoord = listaPfSupAnCoord.stream()
											.filter(p -> (p.getParticella().getIdParticella().equals(idParticella) && p.getColtura().equals(pc.getColtura()))).findFirst();
									if (partSupEle.isPresent()) {
										// Aggiunto il setValBool e setValString a buon senso a fronte della segnalazione 277
										particellaColtura.setValNum(Math.max(partSupEle.get().getValNum() - partSupAnCoord.get().getValNum(), 0));
										particellaColtura.setValBool(partSupEle.get().getValBool());
										particellaColtura.setValString(partSupEle.get().getValString());
									} else {
										logger.info("La particella " + idParticella + "non risulta presente all'interno della lista delle superfici eleggibili");
									}
								} else {
									logger.info("La variabile PFSUPELE non è stata passata in input");
								}
							} else {
//								MAX ((PFSUPSIGECO-PFSUPANCOORD,0)
								if (variabiliCalcolo.get(TipoVariabile.PFSUPSIGECO) != null) {
									ArrayList<ParticellaColtura> listaPfSupSigeco = variabiliCalcolo.get(TipoVariabile.PFSUPSIGECO).getValList();
									Optional<ParticellaColtura> partSupSigeco = listaPfSupSigeco.stream()
											.filter(p -> (p.getParticella().getIdParticella().equals(idParticella) && p.getColtura().equals(pc.getColtura()))).findFirst();
									ArrayList<ParticellaColtura> listaPfSupAnCoord = variabiliCalcolo.get(TipoVariabile.PFSUPANCOORD).getValList();
									Optional<ParticellaColtura> partSupAnCoord = listaPfSupAnCoord.stream()
											.filter(p -> (p.getParticella().getIdParticella().equals(idParticella) && p.getColtura().equals(pc.getColtura()))).findFirst();
									if (partSupSigeco.isPresent()) {
										// Aggiunto il setValBool e setValString a buon senso a fronte della segnalazione 277
										particellaColtura.setValNum(Math.max(partSupSigeco.get().getValNum() - partSupAnCoord.get().getValNum(), 0));
										particellaColtura.setValBool(partSupSigeco.get().getValBool());
										particellaColtura.setValString(partSupSigeco.get().getValString());
									} else {
										logger.info("La particella " + idParticella + "non risulta presente all'interno della lista delle superfici sigeco");
									}
								} else {
									logger.info("La variabile PFSUPSIGECO non è stata passata in input");
								}
							}
						}
						else {
							if (!isCampione(variabiliCalcolo, istruttoria)) {
								// se per la particella non ci sono anomalie di mantenimento o coordinamento la superficie determinata è pari all'eleggibile se non è a campione
								if (variabiliCalcolo.get(TipoVariabile.PFSUPELE) != null) {
									ArrayList<ParticellaColtura> listaPfSupEle = variabiliCalcolo.get(TipoVariabile.PFSUPELE).getValList();
									Optional<ParticellaColtura> partSupEle = listaPfSupEle.stream()
											.filter(p -> (p.getParticella().getIdParticella().equals(idParticella) && p.getColtura().equals(pc.getColtura()))).findFirst();
									if (partSupEle.isPresent()) {
										particellaColtura.setValBool(partSupEle.get().getValBool());
										particellaColtura.setValString(partSupEle.get().getValString());
										particellaColtura.setValNum((partSupEle.get().getValNum()));
									} else {
										logger.info("La particella " + idParticella + "non risulta presente all'interno della lista delle superfici eleggibili");
									}
								} else {
									logger.info("La variabile PFSUPELE non è stata passata in input");
								}
							} else {
								// se per la particella non ci sono anomalie di mantenimento o coordinamento la superficie determinata è pari alla superficie sigeco se è a campione
								if (variabiliCalcolo.get(TipoVariabile.PFSUPSIGECO) != null) {
									ArrayList<ParticellaColtura> listaPfSupSigeco = variabiliCalcolo.get(TipoVariabile.PFSUPSIGECO).getValList();
									Optional<ParticellaColtura> partSupSigeco = listaPfSupSigeco.stream()
											.filter(p -> (p.getParticella().getIdParticella().equals(idParticella) && p.getColtura().equals(pc.getColtura()))).findFirst();
									if (partSupSigeco.isPresent()) {
										particellaColtura.setValBool(partSupSigeco.get().getValBool());
										particellaColtura.setValString(partSupSigeco.get().getValString());
										particellaColtura.setValNum((partSupSigeco.get().getValNum()));
									} else {
										logger.info("La particella " + idParticella + "non risulta presente all'interno della lista delle superfici sigeco");
									}
								} else {
									logger.info("La variabile PFSUPSIGECO non è stata passata in input");
								}
							}
						}
						listaSupDet.add(particellaColtura);
					} else {
						logger.info("La particella " + idParticella + " non risulta presente all'interno della lista delle anomalie di coordinamento");
					}

				} else {
					logger.info("La variabile PFANOMCOORD non è stata passata in input");
				}
			});

			pfSupDet.setValList(listaSupDet);
			return pfSupDet;
		} else {
			logger.info("La variabile PFANOMMAN non è stata passata in input");
			return null;
		}

	}

	@Override
	public TipologiaPassoTransizione getPasso() {
		return TipologiaPassoTransizione.RIDUZIONI_BPS;
	}
}
