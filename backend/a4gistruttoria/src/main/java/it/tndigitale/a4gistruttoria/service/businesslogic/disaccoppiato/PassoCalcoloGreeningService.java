package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import it.tndigitale.a4gistruttoria.util.*;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiPassoLavorazione;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.ParticellaColtura;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.service.businesslogic.PassoDatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@Service
public class PassoCalcoloGreeningService extends PassoDatiElaborazioneIstruttoria {
	BigDecimal sogliaImpegniPresenti = new BigDecimal(10);
	BigDecimal sogliaImpegniDiversificazione = new BigDecimal(15);

	BigDecimal perc5 = BigDecimal.valueOf(0.05).stripTrailingZeros();
	BigDecimal perc20 = BigDecimal.valueOf(0.2).stripTrailingZeros();
	BigDecimal perc50 = BigDecimal.valueOf(0.5).stripTrailingZeros();
	BigDecimal perc75 = BigDecimal.valueOf(0.75).stripTrailingZeros();
	BigDecimal perc95 = BigDecimal.valueOf(0.95).stripTrailingZeros();
	BigDecimal perc100 = BigDecimal.valueOf(1).stripTrailingZeros();
	BigDecimal bigDecimal0 = BigDecimal.valueOf(0).stripTrailingZeros();
	BigDecimal bigDecimal2 = BigDecimal.valueOf(2).stripTrailingZeros();
	BigDecimal bigDecimal3 = BigDecimal.valueOf(3).stripTrailingZeros();
	BigDecimal bigDecimal4 = BigDecimal.valueOf(4).stripTrailingZeros();
	BigDecimal bigDecimal100 = BigDecimal.valueOf(100).stripTrailingZeros();

	public static String esitoNoImpegni = "NO_IMPEGNI";
	public static String esitoDiversificazione = "DIV";
	public static String esitoDiversificazioneEfa = "DIV_EFA";

	// config
	MapVariabili valoriParticellaColturaGreening = VariabileValore.getValoriParticellaColturaGreening();


	@Override
	protected DatiPassoLavorazione elaboraPasso(DatiElaborazioneIstruttoria dati, MapVariabili variabiliCalcolo,
			HashMap<TipoControllo, EsitoControllo> mappaEsiti) {
		DatiPassoLavorazione passo = new DatiPassoLavorazione();
		passo.setIdTransizione(dati.getTransizione().getId());
		passo.setPasso(getPasso());

		List<VariabileCalcolo> listaOutput = new ArrayList<VariabileCalcolo>();
		List<VariabileCalcolo> listaVariabiliSintesi = new ArrayList<VariabileCalcolo>();
		CodiceEsito esito = esegui(variabiliCalcolo, mappaEsiti, listaOutput, listaVariabiliSintesi);

		// per ora 08/01/2019 per il greeniong è sempre true (sempre DUT)
		passo.setEsito(true);

		passo.setCodiceEsito(esito.getCodiceEsito());

		passo.getDatiOutput().setVariabiliCalcolo(listaOutput);
		passo.getDatiSintesi()
				.setVariabiliParticellaColtura(listaVariabiliSintesi.stream().filter(p -> !p.getTipoVariabile().equals(TipoVariabile.PFAZOTOIMP)).collect(Collectors.toList()));

		mappaEsiti.values().forEach(e -> passo.getDatiSintesi().getEsitiControlli().add(e));

		passo.getDatiInput()
				.setVariabiliCalcolo(passo.getDatiInput().getVariabiliCalcolo().stream().filter(v -> !v.getTipoVariabile().equals(TipoVariabile.GREAZIBIO)).collect(Collectors.toList()));

		return passo;
	}

	/**
	 * Metodo principale per l'esecuzione della logica di calcolo dell'algoritmo Greening
	 * 
	 * @return
	 */
	private CodiceEsito esegui(MapVariabili variabiliCalcolo,
			HashMap<TipoControllo, EsitoControllo> mappaEsiti, List<VariabileCalcolo> listaOutput,
			List<VariabileCalcolo> listaVariabiliSintesi) {
		// BRIDUSDC023 Verifico che la domanda abbia richiesto l'intervento del greening
		// "(Ad oggi tutte le domande hanno chiesto il greening in quanto con l'attuale modulo di domanda non è possibile rinunciarvi)
		// Quindi è sempre TRUE, in teoria se qualcuno chiedesse di rinunciare al greening a istruttoria in corso potremmo impostare questa informazione a FALSE"
		// se resBRIDUSDC023 == false BRIDUSDC023_infoGreening e CodiceEsito.DUT_024

		mappaEsiti.put(TipoControllo.BRIDUSDC023_infoGreening, new EsitoControllo(TipoControllo.BRIDUSDC023_infoGreening, true));

		// BRIDUSDC024 Verifico che l'azienda assolva il greening in quanto totalmente biologica - "IF flag compilato a SI THEN TRUE ELSE FALSE"

		// AlgoritmoGreeningBio$IDDOMANDA - Calcolo Esenzioni
		// GREAZIBIO Greening – Esenzione: Azienda totalmente biologica booleano (SI/NO) - SI
		boolean resGREAZIBIO = variabiliCalcolo.get(TipoVariabile.GREAZIBIO).getValBoolean();

		if (resGREAZIBIO) {
			listaOutput.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, resGREAZIBIO));

			// AlgoritmoGreeningBio$IDDOMANDA - Calcolo Importo Greening
			setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GRESUPBASE, variabiliCalcolo.get(TipoVariabile.BPSSUPAMM).getValNumber());
			setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREIMPBASE, variabiliCalcolo.multiply(TipoVariabile.GRESUPBASE, TipoVariabile.TITVALRID, TipoVariabile.GREPERC));
			setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GRESUPAMM, variabiliCalcolo.get(TipoVariabile.GRESUPBASE).getValNumber());
			setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREIMPAMM, variabiliCalcolo.get(TipoVariabile.GREIMPBASE).getValNumber());
			mappaEsiti.put(TipoControllo.BRIDUSDC024_aziendaBiologica, new EsitoControllo(TipoControllo.BRIDUSDC024_aziendaBiologica, true));
			return CodiceEsito.DUT_025;
		}
		mappaEsiti.put(TipoControllo.BRIDUSDC024_aziendaBiologica, new EsitoControllo(TipoControllo.BRIDUSDC024_aziendaBiologica, false));

		/* BRIDUSDC025 Verifico quali impegni per il greening l'azienda debba assolvere */

		BigDecimal resGRESUPSEM = getSumSupPF(calcoliGreeningParticellaColtura(listaOutput, Arrays.asList(TipoVariabile.PFSUPDETSEM), variabiliCalcolo), TipoVariabile.PFSUPDETSEM);
		setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GRESUPSEM, resGRESUPSEM);
		// valore arrotondato
		resGRESUPSEM = variabiliCalcolo.get(TipoVariabile.GRESUPSEM).getValNumber().stripTrailingZeros();

		// Calcolo Esenzioni - Greening – Esenzione: Azienda totalmente biologica
		listaOutput.add(new VariabileCalcolo(TipoVariabile.GREESEBIO, resGREAZIBIO));

		// esenzione seminativi se resSumPFSUPDET <= 10 ha
		boolean resGREESESEM = resGRESUPSEM.compareTo(sogliaImpegniPresenti) <= 0;
		setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREESESEM, resGREESESEM);

		if (resGREESESEM) {
			return noImpegni(listaOutput, variabiliCalcolo, mappaEsiti, listaVariabiliSintesi);
		} else if (resGRESUPSEM.compareTo(sogliaImpegniDiversificazione) <= 0 && resGRESUPSEM.compareTo(sogliaImpegniPresenti) > 0) {
			return diversificazione(listaOutput, variabiliCalcolo, mappaEsiti, listaVariabiliSintesi);
		} else {
			return diversificazioneEfa(listaOutput, listaVariabiliSintesi, variabiliCalcolo, mappaEsiti);
		}
	}

	/**
	 * Metodo per la valutazione dell'esito nel caso in cui non ci siano impegni (GRESUPSEM <= 10)
	 * 
	 * @return
	 */
	private CodiceEsito noImpegni(List<VariabileCalcolo> listaOutput, MapVariabili variabiliCalcolo,
			HashMap<TipoControllo, EsitoControllo> mappaEsiti, List<VariabileCalcolo> listaVariabiliSintesi) {
		List<VariabileCalcolo> listVariabiliParticella = eseguiAlgortimoNoImpegniParticellaColtura(listaOutput, variabiliCalcolo);
		eseguiAlgoritmoNoImpegni(listVariabiliParticella, listaOutput, variabiliCalcolo);
		listaVariabiliSintesi.addAll(listVariabiliParticella);
		mappaEsiti.put(TipoControllo.BRIDUSDC025_impegniGreening, new EsitoControllo(TipoControllo.BRIDUSDC025_impegniGreening, true, esitoNoImpegni));
		return CodiceEsito.DUT_026;
	}

	/**
	 * Metodo per l'esecuzione dell'algoritmo di calcolo "NO IMPEGNI" di tipo "PARTICELLA/COLTURA"
	 * 
	 * @return
	 */
	private List<VariabileCalcolo> eseguiAlgortimoNoImpegniParticellaColtura(List<VariabileCalcolo> listaOutput, MapVariabili variabiliCalcolo) {
		List<TipoVariabile> variabiliOut = new ArrayList<>();
		variabiliOut.addAll(Arrays.asList(TipoVariabile.PFSUPDETARB, TipoVariabile.PFSUPDETPP, TipoVariabile.PFSUPDETSEM));

		return calcoliGreeningParticellaColtura(listaOutput, variabiliOut, variabiliCalcolo);
	}

	/**
	 * Metodo per l'esecuzione dell'algoritmo di calcolo "NO IMPEGNI" di tipo "DOMANDA"
	 * 
	 * @param listaInput
	 */
	private void eseguiAlgoritmoNoImpegni(List<VariabileCalcolo> listaInput, List<VariabileCalcolo> listaOutput, MapVariabili variabiliCalcolo) {
		setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GRESUPBASE, variabiliCalcolo.get(TipoVariabile.BPSSUPAMM).getValNumber());
		setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREIMPBASE, variabiliCalcolo.multiply(TipoVariabile.GRESUPBASE, TipoVariabile.TITVALRID, TipoVariabile.GREPERC));
		setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GRESUPAMM, variabiliCalcolo.get(TipoVariabile.GRESUPBASE).getValNumber());
		setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREIMPAMM, variabiliCalcolo.get(TipoVariabile.GREIMPBASE).getValNumber());

		// GRESUPARB SUM ($PARTICELLA/COLTURA PFSUPDET) PER OGNI $PARTICELLA/COLTURA DOVE $PARTICELLA/COLTURA PFSUPDETARB IS TRUE
		setSumSupPF(listaOutput, listaInput, variabiliCalcolo, TipoVariabile.GRESUPARB, TipoVariabile.PFSUPDETARB);

		// GRESUPDET BPSSUPDET
		setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GRESUPDET, variabiliCalcolo.get(TipoVariabile.BPSSUPDET).getValNumber());

		// GRESUPPP SUM ($PARTICELLA/COLTURA PFSUPDET) PER OGNI $PARTICELLA/COLTURA DOVE $PARTICELLA/COLTURA PFSUPDETPP IS TRUE
		setSumSupPF(listaOutput, listaInput, variabiliCalcolo, TipoVariabile.GRESUPPP, TipoVariabile.PFSUPDETPP);

		// GRESUPSEM SUM ($PARTICELLA/COLTURA PFSUPDET) PER OGNI $PARTICELLA/COLTURA DOVE $PARTICELLA/COLTURA PFSUPDETSEM IS TRUE Settato prima
	}

	/**
	 * Metodo per la valutazione dell'esito nel caso in cui ci siano impegni da calcolare con algoritmo Diversificazione (10 < GRESUPSEM <= 15)
	 * 
	 * @return
	 */
	private CodiceEsito diversificazione(List<VariabileCalcolo> listaOutput, MapVariabili variabiliCalcolo,
			HashMap<TipoControllo, EsitoControllo> mappaEsiti, List<VariabileCalcolo> listaVariabiliSintesi) {
		mappaEsiti.put(TipoControllo.BRIDUSDC025_impegniGreening, new EsitoControllo(TipoControllo.BRIDUSDC025_impegniGreening, true, esitoDiversificazione));
		List<VariabileCalcolo> listVariabiliParticella = eseguiAlgortimoDiverisificazioneParticellaColtura(listaOutput, variabiliCalcolo, false);
		eseguiAlgortimoDomanda(listVariabiliParticella, listaOutput, variabiliCalcolo, false);
		listaVariabiliSintesi.addAll(listVariabiliParticella);

		/* BRIDUSDC033 - "Calcolo Greening: GREESEDIV ""Greening – Esenzione diversificazione""" "IF GREESEDIV THEN TRUE ELSE FALSE" */

		boolean gREESEDIV = variabiliCalcolo.get(TipoVariabile.GREESEDIV).getValBoolean();

		if (gREESEDIV) {
			mappaEsiti.put(TipoControllo.BRIDUSDC033_esenzioneDiversificazione, new EsitoControllo(TipoControllo.BRIDUSDC033_esenzioneDiversificazione, true));
			return CodiceEsito.DUT_027;
		} else {
			mappaEsiti.put(TipoControllo.BRIDUSDC033_esenzioneDiversificazione, new EsitoControllo(TipoControllo.BRIDUSDC033_esenzioneDiversificazione, false));
			/*
			 * BRIDUSDC026 - Verifico che ci siano riduzioni da greening "Calcolo Greening: GRESUPRID Greening – Sup non ammissibile mancato Rispetto Inverdimento" "IF GRESUPRID = 0 THEN FALSE ELSE
			 * TRUE"
			 */
			boolean riduzioniGreening = (variabiliCalcolo.get(TipoVariabile.GRESUPRID).getValNumber().stripTrailingZeros().compareTo(bigDecimal0) > 0);
			if (riduzioniGreening) {
				mappaEsiti.put(TipoControllo.BRIDUSDC026_riduzioniGreening, new EsitoControllo(TipoControllo.BRIDUSDC026_riduzioniGreening, true));
				/*
				 * BRIDUSDC055 - Verifico la presenza di sanzione greening "Calcolo Greening: GRESUPRID Greening – Sup non ammissibile mancato Rispetto Inverdimento GREPERCSCOST Greening - Percentuale
				 * Scostamento"	"IF GREPERCSCOST <= 3 AND GRESUPRID <=2 ha THEN FALSE ELSE TRUE"
				 */
				boolean sanzioniGreening = checkSanzioniGreening(listaOutput, variabiliCalcolo);
				if (sanzioniGreening) {
					mappaEsiti.put(TipoControllo.BRIDUSDC055_sanzioneGreening, new EsitoControllo(TipoControllo.BRIDUSDC055_sanzioneGreening, true));
					return CodiceEsito.DUT_030;
				} else {
					mappaEsiti.put(TipoControllo.BRIDUSDC055_sanzioneGreening, new EsitoControllo(TipoControllo.BRIDUSDC055_sanzioneGreening, false));
					return CodiceEsito.DUT_029;
				}
			} else {
				mappaEsiti.put(TipoControllo.BRIDUSDC026_riduzioniGreening, new EsitoControllo(TipoControllo.BRIDUSDC026_riduzioniGreening, false));
				return CodiceEsito.DUT_028;
			}
		}
	}

	/**
	 * Metodo per l'esecuzione dell'algoritimo di calcolo "DIVERSIFICAZIONE" di tipo "PARTICELLA/COLTURA"
	 * 
	 * @param isEfa:
	 *            determina se sia necessario calcolare anche le due variabili specifiche per l'algoritmo EFA (PFAZOTO e PFSUPDETSECONDA)
	 * @return
	 */
	private List<VariabileCalcolo> eseguiAlgortimoDiverisificazioneParticellaColtura(List<VariabileCalcolo> listaOutput, MapVariabili variabiliCalcolo, boolean isEfa) {
		List<TipoVariabile> variabiliOut = new ArrayList<>();
		variabiliOut.addAll(Arrays.asList(TipoVariabile.PFSUPDETARB, TipoVariabile.PFSUPDETPP, TipoVariabile.PFSUPDETSEM, TipoVariabile.PFSUPDETERBAI, TipoVariabile.PFSUPDETLEGUM,
				TipoVariabile.PFSUPDETRIPOSO, TipoVariabile.PFSUPDETSOMM, TipoVariabile.PFSUPDETPRIMA));

		if (isEfa)
			variabiliOut.addAll(Arrays.asList(TipoVariabile.PFAZOTO, TipoVariabile.PFAZOTOIMP, TipoVariabile.PFSUPDETSECONDA));

		return calcoliGreeningParticellaColtura(listaOutput, variabiliOut, variabiliCalcolo);
	}

	/**
	 * Metodo per l'esecuzione dell'algoritmo di calcolo "DIVERSIFICAZIONE" di tipo "DOMANDA"
	 * 
	 * @param listInput
	 */
	private void eseguiAlgortimoDomanda(List<VariabileCalcolo> listInput, List<VariabileCalcolo> listaOutput,
			MapVariabili variabiliCalcolo, boolean isEFA) {

		// 1. CARATTERISTICHE AZIENDA
		setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GRESUPDET, variabiliCalcolo.get(TipoVariabile.BPSSUPDET).getValNumber());

		setSumSupPF(listaOutput, listInput, variabiliCalcolo, TipoVariabile.GRESUPARB, TipoVariabile.PFSUPDETARB);
		setSumSupPF(listaOutput, listInput, variabiliCalcolo, TipoVariabile.GRESUPPP, TipoVariabile.PFSUPDETPP);
		setSumSupPF(listaOutput, listInput, variabiliCalcolo, TipoVariabile.GRESUPERBAI, TipoVariabile.PFSUPDETERBAI);
		setSumSupPF(listaOutput, listInput, variabiliCalcolo, TipoVariabile.GRESUPLEGUM, TipoVariabile.PFSUPDETLEGUM);
		setSumSupPF(listaOutput, listInput, variabiliCalcolo, TipoVariabile.GRESUPRIPOSO, TipoVariabile.PFSUPDETRIPOSO);
		setSumSupPF(listaOutput, listInput, variabiliCalcolo, TipoVariabile.GRESUPSOMM, TipoVariabile.PFSUPDETSOMM);
		setSumSupPF(listaOutput, listInput, variabiliCalcolo, TipoVariabile.GREDIVSUP1COL, TipoVariabile.PFSUPDETPRIMA);

		ParticellaColtura particellaSuperficieMax = getParticellCoturaMaxSup(listInput, TipoVariabile.PFSUPDETPRIMA);
		ParticellaColtura particellaSuperficieMaxEfa = getParticellCoturaMaxSup(listInput, TipoVariabile.PFSUPDETSECONDA);

		// GREDIVFGS Greening - Codici Famiglia-Genere-Specie della Coltura principale stringa CODCOLTDIVERSA delle $PARTICELLA/COLTURA con PFSUPDETPRIMA TRUE GREDIV1COL Greening - Coltura Principale
		setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREDIVFGS, particellaSuperficieMax.getValString());

		// GREDIV1COL Greening - Coltura Principale Rappresentativa stringa Descrizione del codice coltura a 3 con la maggiore superficie fra quelli dei $PARTICELLA/COLTURA con PFSUPDETPRIMA TRUE
		setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREDIV1COL, particellaSuperficieMax.getColtura());

		// 1.1 CARATTERISTICHE AZIENDA - EFA
		if (isEFA) {
			// GREDIVSUP2COL

			if (variabiliCalcolo.get(TipoVariabile.GRESUPSEM).getValNumber() != null && variabiliCalcolo.get(TipoVariabile.GRESUPSEM).getValNumber().compareTo(BigDecimal.valueOf(30)) > 0) {
				setSumSupPF(listaOutput, listInput, variabiliCalcolo, TipoVariabile.GREDIVSUP2COL, TipoVariabile.PFSUPDETSECONDA);
			}
			// GREDIV2COL - Rivedere il calcolo

			if (variabiliCalcolo.get(TipoVariabile.GRESUPSEM).getValNumber() != null && variabiliCalcolo.get(TipoVariabile.GRESUPSEM).getValNumber().compareTo(BigDecimal.valueOf(30)) > 0) {
				setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREDIV2COL, particellaSuperficieMaxEfa.getColtura());

			}

			// GRESUPAZOTODIC
			setSumSupPF(listaOutput, listInput, variabiliCalcolo, TipoVariabile.GRESUPAZOTODIC, TipoVariabile.PFAZOTOIMP);
			// GRESUPAZOTODET
			setSumSupPF(listaOutput, listInput, variabiliCalcolo, TipoVariabile.GRESUPAZOTODET, TipoVariabile.PFAZOTO);
			// GRESUPAZOTOPOND - min(GRESUPAZOTODIC;GRESUPAZOTODET)*GREFPAZOTO
			setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GRESUPAZOTOPOND,
					variabiliCalcolo.get(TipoVariabile.GREFPAZOTO).getValNumber().multiply(variabiliCalcolo.min(TipoVariabile.GRESUPAZOTODIC, TipoVariabile.GRESUPAZOTODET)));
		}

		// 2. CALCOLO ESENZIONI
		setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREPERCPES,
				perc100.multiply(variabiliCalcolo.divide(variabiliCalcolo.add(TipoVariabile.GRESUPPP, TipoVariabile.GRESUPERBAI, TipoVariabile.GRESUPSOMM), TipoVariabile.BPSSUPDET)));
		setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREPERCELR,
				perc100.multiply(variabiliCalcolo.divide(variabiliCalcolo.add(TipoVariabile.GRESUPERBAI, TipoVariabile.GRESUPRIPOSO, TipoVariabile.GRESUPLEGUM), TipoVariabile.GRESUPSEM)));

		setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREESEDIV, (variabiliCalcolo.get(TipoVariabile.GREPERCPES).getValNumber().stripTrailingZeros().compareTo(perc75) > 0
				|| variabiliCalcolo.get(TipoVariabile.GREPERCELR).getValNumber().stripTrailingZeros().compareTo(perc75) > 0));

		setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GRESUPBASE, variabiliCalcolo.get(TipoVariabile.BPSSUPAMM).getValNumber());
		setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREIMPBASE, variabiliCalcolo.multiply(TipoVariabile.GRESUPBASE, TipoVariabile.TITVALRID, TipoVariabile.GREPERC));

		boolean gREESEDIV = variabiliCalcolo.get(TipoVariabile.GREESEDIV).getValBoolean();

		// 2.1 CALCOLO ESENZIONI - EFA
		if (isEFA) {
			setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREESEEFA, (variabiliCalcolo.get(TipoVariabile.GREPERCPES).getValNumber().stripTrailingZeros().compareTo(perc75) > 0
					|| variabiliCalcolo.get(TipoVariabile.GREPERCELR).getValNumber().stripTrailingZeros().compareTo(perc75) > 0));
		}

		// 3. CALCOLO RIDUZIONI

		// Variabili da valorizzare solo se GREESEDIV = NO
		if (gREESEDIV) {
			setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GRESUPAMM, variabiliCalcolo.get(TipoVariabile.GRESUPBASE).getValNumber());
			setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREIMPAMM, variabiliCalcolo.multiply(TipoVariabile.GRESUPAMM, TipoVariabile.TITVALRID, TipoVariabile.GREPERC));
		} else {

			// GREDIVPERC1COL
			BigDecimal valGREDIVPERC1COL = perc100.multiply(variabiliCalcolo.divide(TipoVariabile.GREDIVSUP1COL, TipoVariabile.GRESUPSEM));
			setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREDIVPERC1COL, valGREDIVPERC1COL);

			if (isEFA) {

				// GREDIVPERC2COL
				if (variabiliCalcolo.get(TipoVariabile.GRESUPSEM).getValNumber().compareTo(BigDecimal.valueOf(30)) > 0) {
					BigDecimal test = variabiliCalcolo.add(TipoVariabile.GREDIVSUP1COL, TipoVariabile.GREDIVSUP2COL);
					setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREDIVPERC2COL, variabiliCalcolo.divide(test, TipoVariabile.GRESUPSEM));
				}

				// GREDIVSUPRID Efa
				BigDecimal variabileCalcoloCol1 = BigDecimal.ZERO;
				BigDecimal variabileCalcoloCol2 = BigDecimal.ZERO;
				BigDecimal variabileCalcoloCol3 = BigDecimal.ZERO;

				if (variabiliCalcolo.get(TipoVariabile.GREDIVPERC1COL).getValNumber().stripTrailingZeros().compareTo(perc75) > 0) {
					BigDecimal greSupSem075 = variabiliCalcolo.get(TipoVariabile.GRESUPSEM).getValNumber().multiply(BigDecimal.valueOf(0.75));
					// Betty [13.06.2019]: correttiva/evolutiva #65 - Michele Lugo aveva dimenticato un * 2 nelle specifiche
					variabileCalcoloCol1 = variabiliCalcolo.subtract(TipoVariabile.GREDIVSUP1COL, greSupSem075).multiply(BigDecimal.valueOf(2));
				} // else resta 0

				if (variabiliCalcolo.get(TipoVariabile.GREDIVPERC2COL) != null && variabiliCalcolo.get(TipoVariabile.GREDIVPERC2COL).getValNumber().stripTrailingZeros().compareTo(perc95) > 0) {
					BigDecimal greSupSem095 = variabiliCalcolo.get(TipoVariabile.GRESUPSEM).getValNumber().multiply(BigDecimal.valueOf(0.95));
					variabileCalcoloCol3 = variabiliCalcolo.add(TipoVariabile.GREDIVSUP1COL, TipoVariabile.GREDIVSUP2COL);
					variabileCalcoloCol2 = variabileCalcoloCol3.subtract(greSupSem095).multiply(BigDecimal.valueOf(5));
				} // else resta 0

				BigDecimal variabileFinale = variabileCalcoloCol1.add(variabileCalcoloCol2);

				setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREDIVSUPRID, variabiliCalcolo.min(TipoVariabile.GRESUPSEM, variabileFinale));

				// da valorizzare solo se GREESEEFA = NO
				boolean gRIDEFA = variabiliCalcolo.get(TipoVariabile.GREESEEFA).getValBoolean();

				if (!gRIDEFA) {
					// GREEFAPERCAZOTO
					setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREEFAPERCAZOTO, variabiliCalcolo.divide(TipoVariabile.GRESUPAZOTOPOND, TipoVariabile.GRESUPSEM));

					// GREEFASUPRID
					if (variabiliCalcolo.get(TipoVariabile.GREEFAPERCAZOTO).getValNumber().stripTrailingZeros().compareTo(perc5) < 0) {
						BigDecimal test = variabiliCalcolo.get(TipoVariabile.GRESUPSEM).getValNumber().multiply(BigDecimal.valueOf(0.05));
						setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREEFASUPRID, test.subtract(variabiliCalcolo.get(TipoVariabile.GRESUPAZOTOPOND).getValNumber()).multiply(BigDecimal.valueOf(10)));
					} else
						setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREEFASUPRID, BigDecimal.ZERO);
				}

				// GRESUPRID - EFA
				if (variabiliCalcolo.get(TipoVariabile.GRESUPRIDIST) == null) {
					BigDecimal test = variabiliCalcolo.min(TipoVariabile.GRESUPSEM, variabiliCalcolo.add(TipoVariabile.GREDIVSUPRID, TipoVariabile.GREEFASUPRID));
					setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GRESUPRID, variabiliCalcolo.min(TipoVariabile.GRESUPBASE, test));
				} else {
					BigDecimal test = variabiliCalcolo.min(TipoVariabile.GRESUPRIDIST, TipoVariabile.GRESUPSEM);
					setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GRESUPRID, variabiliCalcolo.min(TipoVariabile.GRESUPBASE, test));
				}

			} else {

				// GREDIVSUPRID Diversificazione
				if (valGREDIVPERC1COL.compareTo(perc75) > 0) {
					BigDecimal valGREDIVSUPRID = variabiliCalcolo.min(TipoVariabile.GRESUPSEM,
							(variabiliCalcolo.subtract(TipoVariabile.GREDIVSUP1COL, variabiliCalcolo.multiply(TipoVariabile.GRESUPSEM, perc75))).multiply(bigDecimal2));
					setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREDIVSUPRID, valGREDIVSUPRID);
				} else {
					setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREDIVSUPRID, bigDecimal0);
				}

				// GRESUPRID
				BigDecimal varGreSupRid;
				if (variabiliCalcolo.get(TipoVariabile.GRESUPRIDIST) != null && variabiliCalcolo.get(TipoVariabile.GRESUPRIDIST).getValNumber() != null)
					varGreSupRid = variabiliCalcolo.get(TipoVariabile.GRESUPRIDIST).getValNumber();
				else
					varGreSupRid = variabiliCalcolo.get(TipoVariabile.GREDIVSUPRID).getValNumber();

				setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GRESUPRID, variabiliCalcolo.min(TipoVariabile.GRESUPBASE, varGreSupRid));

			}

			setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREIMPRID, variabiliCalcolo.multiply(TipoVariabile.GRESUPRID, TipoVariabile.TITVALRID, TipoVariabile.GREPERC));
			setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GRESUPAMM, variabiliCalcolo.subtract(TipoVariabile.GRESUPBASE, TipoVariabile.GRESUPRID));
			setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREIMPAMM, variabiliCalcolo.multiply(TipoVariabile.GRESUPAMM, TipoVariabile.TITVALRID, TipoVariabile.GREPERC));
			if (variabiliCalcolo.get(TipoVariabile.GRESUPAMM).getValNumber().compareTo(bigDecimal0) > 0) {
				setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREPERCSCOST, perc100.multiply(variabiliCalcolo.divide(TipoVariabile.GRESUPRID, TipoVariabile.GRESUPAMM)));
			}else{
				setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREPERCSCOST, variabiliCalcolo.get(TipoVariabile.GRESUPRID).getValNumber());
			}
		}

	}

	/**
	 * Metodo per la valutazione dell'esito nel caso in cui ci siano impegni da calcolare con algoritmo Diversificazione + EFA (GRESUPSEM > 15)
	 * 
	 * @return
	 */
	private CodiceEsito diversificazioneEfa(List<VariabileCalcolo> listaOutput,
			List<VariabileCalcolo> listaVariabiliSintesi,
			MapVariabili variabiliCalcolo,
			HashMap<TipoControllo, EsitoControllo> mappaEsiti) {
		mappaEsiti.put(TipoControllo.BRIDUSDC025_impegniGreening, new EsitoControllo(TipoControllo.BRIDUSDC025_impegniGreening, true, esitoDiversificazioneEfa));
		List<VariabileCalcolo> listVariabiliParticella = eseguiAlgortimoDiverisificazioneEFAParticellaColtura(listaOutput, variabiliCalcolo);
		eseguiAlgortimoDomanda(listVariabiliParticella, listaOutput, variabiliCalcolo, true);
		listaVariabiliSintesi.addAll(listVariabiliParticella);

		// BRIDUSDC117 -"Verifico che l'azienda sia esente dalla diversificazione e dall'obbligo delle EFA

		boolean gREESEDIV = variabiliCalcolo.get(TipoVariabile.GREESEDIV).getValBoolean();
		boolean gREESEFA = variabiliCalcolo.get(TipoVariabile.GREESEEFA).getValBoolean();

		if (gREESEFA && gREESEDIV) {
			mappaEsiti.put(TipoControllo.BRIDUSDC117_diversificazioneEFA, new EsitoControllo(TipoControllo.BRIDUSDC117_diversificazioneEFA, true));
			return CodiceEsito.DUT_070;
		} else {
			mappaEsiti.put(TipoControllo.BRIDUSDC117_diversificazioneEFA, new EsitoControllo(TipoControllo.BRIDUSDC117_diversificazioneEFA, false));
			/*
			 * BRIDUSDC026 - Verifico che ci siano riduzioni da greening "Calcolo Greening: GRESUPRID Greening – Sup non ammissibile mancato Rispetto Inverdimento" "IF GRESUPRID = 0 THEN FALSE ELSE
			 * TRUE"
			 */
			boolean riduzioniGreening = (variabiliCalcolo.get(TipoVariabile.GRESUPRID).getValNumber().stripTrailingZeros().compareTo(bigDecimal0) > 0);
			if (riduzioniGreening) {
				mappaEsiti.put(TipoControllo.BRIDUSDC026_riduzioniGreening, new EsitoControllo(TipoControllo.BRIDUSDC026_riduzioniGreening, true));
				/*
				 * BRIDUSDC055 - Verifico la presenza di sanzione greening "Calcolo Greening: GRESUPRID Greening – Sup non ammissibile mancato Rispetto Inverdimento GREPERCSCOST Greening - Percentuale
				 * Scostamento"	"IF GREPERCSCOST <= 3 AND GRESUPRID <=2 ha THEN FALSE ELSE TRUE"
				 */
				boolean sanzioniGreening = checkSanzioniGreening(listaOutput, variabiliCalcolo);
				if (sanzioniGreening) {
					mappaEsiti.put(TipoControllo.BRIDUSDC055_sanzioneGreening, new EsitoControllo(TipoControllo.BRIDUSDC055_sanzioneGreening, true));
					return CodiceEsito.DUT_033;
				} else {
					mappaEsiti.put(TipoControllo.BRIDUSDC055_sanzioneGreening, new EsitoControllo(TipoControllo.BRIDUSDC055_sanzioneGreening, false));
					return CodiceEsito.DUT_032;
				}
			} else {
				mappaEsiti.put(TipoControllo.BRIDUSDC026_riduzioniGreening, new EsitoControllo(TipoControllo.BRIDUSDC026_riduzioniGreening, false));
				return CodiceEsito.DUT_031;
			}

		}
	}

	/**
	 * Metodo per l'esecuzione dell'algoritmo di calcolo "DIVERSIFICAZIONE + EFA" di tipo "PARTICELLA/COLTURA"
	 * 
	 * @return
	 */
	private List<VariabileCalcolo> eseguiAlgortimoDiverisificazioneEFAParticellaColtura(List<VariabileCalcolo> listaOutput, MapVariabili variabiliCalcolo) {
		return eseguiAlgortimoDiverisificazioneParticellaColtura(listaOutput, variabiliCalcolo, true);
	}

	/**
	 * 
	 * @param variabili
	 * @param tipoVariabileFilter
	 * @return
	 */
	private BigDecimal getSumSupPF(List<VariabileCalcolo> variabili, TipoVariabile tipoVariabileFilter) {
		Float supPF = Float.valueOf(0);

		List<VariabileCalcolo> variabilFilter = variabili.stream().filter(x -> x.getTipoVariabile() == tipoVariabileFilter).collect(Collectors.toList());

		if (!variabilFilter.isEmpty()) // solo 1
			for (ParticellaColtura resParticellaColtura : variabilFilter.get(0).getValList()) {
				// if (resParticellaColtura.getValBool())
				if (Boolean.TRUE.equals(resParticellaColtura.getValBool()))
					supPF += resParticellaColtura.getValNum();
			}
		return BigDecimal.valueOf(supPF);
	}

	/**
	 * 
	 * @param listaInput
	 * @param tipoVariabileOut
	 * @param tipoVariabileFilter
	 */
	private void setSumSupPF(List<VariabileCalcolo> listaOutput, List<VariabileCalcolo> listaInput,
			MapVariabili variabiliCalcolo, TipoVariabile tipoVariabileOut, TipoVariabile tipoVariabileFilter) {
		setValCalcoloOut(listaOutput, variabiliCalcolo,  tipoVariabileOut, getSumSupPF(listaInput, tipoVariabileFilter));
	}

	/**
	 * Metodo di utilita per la valorizzazione delle variabili di tipo Particella/coltura
	 * 
	 * @param variabiliOut:
	 *            lista di variabili da valorizzare
	 * 
	 * @return
	 */
	private List<VariabileCalcolo> calcoliGreeningParticellaColtura(List<VariabileCalcolo> listaOutput, List<TipoVariabile> variabiliOut, MapVariabili variabiliCalcolo) {

		ArrayList<ParticellaColtura> particelleColtura = variabiliCalcolo.get(TipoVariabile.PFSUPDET).getValList();

		List<VariabileCalcolo> variabiliParticellaColtura = new ArrayList<>();

		// valorizzazione delle varibili diverse da PFSUPDETLEGUM, PFSUPDETPRIMA e PFSUPDETSECONDA
		for (TipoVariabile tipoVariabile : variabiliOut.stream()
				.filter(x -> x != TipoVariabile.PFSUPDETLEGUM && x != TipoVariabile.PFSUPDETPRIMA && x != TipoVariabile.PFSUPDETSECONDA && x != TipoVariabile.PFAZOTOIMP)
				.collect(Collectors.toList())) {
			VariabileCalcolo variabile = new VariabileCalcolo(tipoVariabile);
			variabile.setValList(new ArrayList<ParticellaColtura>());

			for (ParticellaColtura pc : particelleColtura) {
				ParticellaColtura particelleColturaVariabile = new ParticellaColtura();
				particelleColturaVariabile.setParticella(pc.getParticella());
				particelleColturaVariabile.setColtura(pc.getColtura());
				particelleColturaVariabile.setLivello(pc.getLivello());
				particelleColturaVariabile.setValNum(pc.getValNum());
				particelleColturaVariabile.setValString(pc.getValString());
				particelleColturaVariabile.setValBool(getPF(pc.getLivello(), valoriParticellaColturaGreening.get(tipoVariabile).getValString()));

				variabile.getValList().add(particelleColturaVariabile);
			}
			variabiliParticellaColtura.add(variabile);
		}

		// valorizzazione variabile PFSUPDETLEGUM
		if (variabiliOut.contains(TipoVariabile.PFSUPDETLEGUM)) {
			VariabileCalcolo variabile = new VariabileCalcolo(TipoVariabile.PFSUPDETLEGUM);
			// "IF LEGUMINOSE is true nella matrice delle colture caricata in A4G THEN TRUE ELSE FALSE"
			variabile.setValList(new ArrayList<ParticellaColtura>());

			for (ParticellaColtura pc : particelleColtura) {
				ParticellaColtura particelleColturaVariabile = new ParticellaColtura();
				particelleColturaVariabile.setParticella(pc.getParticella());
				particelleColturaVariabile.setColtura(pc.getColtura());
				particelleColturaVariabile.setLivello(pc.getLivello());
				particelleColturaVariabile.setValNum(pc.getValNum());
				particelleColturaVariabile.setValBool(pc.getValBool());
				particelleColturaVariabile.setValString(pc.getValString());

				variabile.getValList().add(particelleColturaVariabile);
			}
			variabiliParticellaColtura.add(variabile);
		}

		// valorizzazione variabile PFSUPDETPRIMA
		String cODCOLTDIVERSASupMax = "";
		if (variabiliOut.contains(TipoVariabile.PFSUPDETPRIMA)) {

			cODCOLTDIVERSASupMax = getCODCOLTDIVERSASupMax(particelleColtura);

			/*
			 * Limitatamente ai soli seminativi (livello coltura is like '11%') si raggruppano le superfici a seconda della colonna CODCOLTDIVERSA nella matrice delle colture caricata in A4G e si
			 * seleziona il CODCOLTDIVERSA con la maggiore superficie impegnata. A quel punto: IF il CODCOLTDIVERSA del record dell'impegno è = al CODCOLTDIVERSA ottenuto con la procedura di cui sopra
			 * THEN TRUE ELSE FALSE
			 */
			VariabileCalcolo variabile = new VariabileCalcolo(TipoVariabile.PFSUPDETPRIMA);
			// "IF LEGUMINOSE is true nella matrice delle colture caricata in A4G THEN TRUE ELSE FALSE"
			variabile.setValList(new ArrayList<ParticellaColtura>());

			for (ParticellaColtura pc : particelleColtura) {
				ParticellaColtura particelleColturaVariabile = new ParticellaColtura();
				particelleColturaVariabile.setParticella(pc.getParticella());
				particelleColturaVariabile.setColtura(pc.getColtura());
				particelleColturaVariabile.setLivello(pc.getLivello());
				particelleColturaVariabile.setValNum(pc.getValNum());
				particelleColturaVariabile.setValString(pc.getValString());

				boolean seminativo = getPF(pc.getLivello(), valoriParticellaColturaGreening.get(TipoVariabile.PFSUPDETPRIMA).getValString());

				particelleColturaVariabile.setValBool(seminativo && pc.getValString() != null && pc.getValString().equals(cODCOLTDIVERSASupMax));

				variabile.getValList().add(particelleColturaVariabile);
			}
			variabiliParticellaColtura.add(variabile);
		}

		// valorizzazione variabile PFSUPDETSECONDA
		String cODCOLTDIVERSAEfaSupMax = "";
		if (variabiliOut.contains(TipoVariabile.PFSUPDETSECONDA)) {

			cODCOLTDIVERSAEfaSupMax = getCODCOLTDIVERSAEfaSupMax(particelleColtura, cODCOLTDIVERSASupMax);
			if (variabiliCalcolo.get(TipoVariabile.GRESUPSEM).getValNumber().compareTo(BigDecimal.valueOf(30)) > 0) {
				// Valorizzazione variabile GREDIV2FGS
				setValCalcoloOut(listaOutput, variabiliCalcolo, TipoVariabile.GREDIV2FGS, cODCOLTDIVERSAEfaSupMax);

			}

			/*
			 * Limitatamente ai soli seminativi (livello coltura is like '11%') si raggruppano le superfici a seconda della colonna CODCOLTDIVERSA nella matrice delle colture caricata in A4G e si
			 * seleziona il CODCOLTDIVERSA con la maggiore superficie impegnata. A quel punto: IF il CODCOLTDIVERSA del record dell'impegno è = al CODCOLTDIVERSA ottenuto con la procedura di cui sopra
			 * THEN TRUE ELSE FALSE
			 */
			VariabileCalcolo variabile = new VariabileCalcolo(TipoVariabile.PFSUPDETSECONDA);
			// "IF LEGUMINOSE is true nella matrice delle colture caricata in A4G THEN TRUE ELSE FALSE"
			variabile.setValList(new ArrayList<ParticellaColtura>());

			for (ParticellaColtura pc : particelleColtura) {
				ParticellaColtura particelleColturaVariabile = new ParticellaColtura();
				particelleColturaVariabile.setParticella(pc.getParticella());
				particelleColturaVariabile.setColtura(pc.getColtura());
				particelleColturaVariabile.setLivello(pc.getLivello());
				particelleColturaVariabile.setValNum(pc.getValNum());
				particelleColturaVariabile.setValString(pc.getValString());

				boolean seminativo = getPF(pc.getLivello(), valoriParticellaColturaGreening.get(TipoVariabile.PFSUPDETSECONDA).getValString());

				particelleColturaVariabile.setValBool(seminativo && pc.getValString() != null && pc.getValString().equals(cODCOLTDIVERSAEfaSupMax));

				variabile.getValList().add(particelleColturaVariabile);
			}
			variabiliParticellaColtura.add(variabile);
		}

		// valorizzazione variabile PFAZOTOIMP
		if (variabiliOut.contains(TipoVariabile.PFAZOTOIMP)) {
			ArrayList<ParticellaColtura> particelleColturaImp = variabiliCalcolo.get(TipoVariabile.PFSUPIMP).getValList();

			VariabileCalcolo variabile = new VariabileCalcolo(TipoVariabile.PFAZOTOIMP);
			variabile.setValList(new ArrayList<ParticellaColtura>());

			for (ParticellaColtura pc : particelleColturaImp) {
				ParticellaColtura particelleColturaVariabile = new ParticellaColtura();
				particelleColturaVariabile.setParticella(pc.getParticella());
				particelleColturaVariabile.setColtura(pc.getColtura());
				particelleColturaVariabile.setLivello(pc.getLivello());
				particelleColturaVariabile.setValNum(pc.getValNum());
				particelleColturaVariabile.setValString(pc.getValString());
				particelleColturaVariabile.setValBool(getPF(pc.getLivello(), valoriParticellaColturaGreening.get(TipoVariabile.PFAZOTO).getValString()));

				variabile.getValList().add(particelleColturaVariabile);
			}
			variabiliParticellaColtura.add(variabile);
		}

		return variabiliParticellaColtura;
	}

	/**
	 * 
	 * @return
	 */
	private boolean checkSanzioniGreening(List<VariabileCalcolo> listaOutput, MapVariabili variabiliCalcolo) {
		BigDecimal valGREPERCSCOST = variabiliCalcolo.get(TipoVariabile.GREPERCSCOST).getValNumber().stripTrailingZeros();
		BigDecimal valGRESUPRID = variabiliCalcolo.get(TipoVariabile.GRESUPRID).getValNumber().stripTrailingZeros();

		if (valGREPERCSCOST.compareTo(bigDecimal3) <= 0 && valGRESUPRID.compareTo(bigDecimal2) <= 0) {
			return false;
		} else {
			if (valGREPERCSCOST.compareTo(perc20) <= 0) {
				BigDecimal res1 = variabiliCalcolo.multiply(TipoVariabile.GRESUPRID, TipoVariabile.TITVALRID, TipoVariabile.GREPERC).multiply(bigDecimal2).divide(bigDecimal4);
				setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREIMPSANZ, res1.min(variabiliCalcolo.get(TipoVariabile.GREIMPBASE).getValNumber().divide(bigDecimal4)));
			} else if (valGREPERCSCOST.compareTo(perc20) > 0 && valGREPERCSCOST.compareTo(perc50) <= 0) {
				setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREIMPSANZ, variabiliCalcolo.get(TipoVariabile.GREIMPAMM).getValNumber());
			} else {
				BigDecimal res1 = variabiliCalcolo.multiply(TipoVariabile.GRESUPRID, TipoVariabile.TITVALRID, TipoVariabile.GREPERC).divide(bigDecimal4);
				setValCalcoloOut(listaOutput, variabiliCalcolo,  TipoVariabile.GREIMPSANZ,
						variabiliCalcolo.get(TipoVariabile.GREIMPAMM).getValNumber().add(res1.min(variabiliCalcolo.get(TipoVariabile.GREIMPBASE).getValNumber().divide(bigDecimal4))));
			}
			return true;
		}
	}

	/**
	 * Metodo di utilità per verificare la corrispondenza tra il livelloColtura e la chiave di ricerca
	 * 
	 * @param livelloColtura
	 * @param ricerca
	 * @return
	 */
	private Boolean getPF(String livelloColtura, String ricerca) {
		String perc = "%";

		if (ricerca.startsWith(perc))
			return (livelloColtura.endsWith(ricerca));
		else if (ricerca.endsWith(perc))
			return (livelloColtura.startsWith(ricerca.replace(perc, "")));
		else
			return (livelloColtura.equals(ricerca));
	}

	/**
	 * 
	 * @param variabili
	 * @return
	 */
	private ParticellaColtura getParticellCoturaMaxSup(List<VariabileCalcolo> variabili, TipoVariabile variabile) {
		Float superficieMax = Float.valueOf(0);
		ParticellaColtura particellaSuperficieMax = new ParticellaColtura();

		Optional<VariabileCalcolo> variabileColturaOpt = variabili.stream().filter(x -> x.getTipoVariabile() == variabile).findFirst();

		if (variabileColturaOpt.isPresent()) {
			for (ParticellaColtura entry : variabileColturaOpt.get().getValList().stream().filter(x -> x.getValBool().equals(true)).collect(Collectors.toList())) {
				Float superficie = entry.getValNum();
				if (superficie > superficieMax) {
					superficieMax = superficie;
					particellaSuperficieMax = entry;
				}
			}
			// for (ParticellaColtura entryFalse : variabileColturaOpt.get().getValList().stream().filter(x -> x.getValBool().equals(false)).collect(Collectors.toList())) {
			// particellaSuperficieMax.setValBool(entryFalse.getValBool());
			// particellaSuperficieMax.setValNum(entryFalse.getValNum());
			// particellaSuperficieMax.setValString(entryFalse.getValString());
			// }
		}
		return particellaSuperficieMax;
	}

	/**
	 * 
	 * Metodo che raggruppa le superfici sulla base del valore di CODCOLTURADIVERSA contenuta nella matrice di compatibilità; seleziona la coltura che ha la maggiore superficie impegnata.
	 * 
	 * @param particelleColtura:
	 *            Lista di particelle impegnate in domanda da raggruppare sulla base del valore CODCOLTURADIVERSA
	 * @return Codice CODCOLTURADIVERSA alla quale corrisponde la superficie impegnata maggiore.
	 */
	public String getCODCOLTDIVERSASupMax(List<ParticellaColtura> particelleColtura) {
		/*
		 * Limitatamente ai soli seminativi (livello coltura is like '11%') si raggruppano le superfici a seconda della colonna CODCOLTDIVERSA nella matrice delle colture caricata in A4G e si
		 * seleziona il CODCOLTDIVERSA con la maggiore superficie impegnata. A quel punto: IF il CODCOLTDIVERSA del record dell'impegno è = al CODCOLTDIVERSA ottenuto con la procedura di cui sopra
		 * THEN TRUE ELSE FALSE
		 */

		String seminativoFilter = valoriParticellaColturaGreening.get(TipoVariabile.PFSUPDETPRIMA).getValString();

		Set<Entry<String, Double>> a = particelleColtura.stream().filter(x -> getPF(x.getLivello(), seminativoFilter).equals(true)).collect(Collectors
				.groupingBy(w -> (w.getValString() != null) ? w.getValString() : (w.getParticella().getIdParticella() + "_" + w.getColtura()), Collectors.summingDouble(ParticellaColtura::getValNum)))
				.entrySet();

		Map.Entry<String, Double> cODCOLTDIVERSA = a.stream().max(Comparator.comparing(Map.Entry<String, Double>::getValue)).orElseThrow(NoSuchElementException::new);
		return cODCOLTDIVERSA.getKey();
	}

	/**
	 * 
	 * Metodo che raggruppa le superfici sulla base del valore di CODCOLTURADIVERSA contenuta nella matrice di compatibilità; esclude il CODCOLTURADIVERSA considerato nella Coltura Principale e
	 * seleziona la coltura che ha la maggiore superficie impegnata.
	 * 
	 * @param particelleColtura:
	 *            Lista di particelle impegnate in domanda da raggruppare sulla base del valore CODCOLTURADIVERSA
	 * 
	 * @param codColturaDiversaPrima:
	 *            CODCOLTURADIVERSA associata alla Coltura Principale
	 * @return Codice CODCOLTURADIVERSA alla quale corrisponde la superficie impegnata maggiore.
	 */
	public String getCODCOLTDIVERSAEfaSupMax(List<ParticellaColtura> particelleColtura, String codColturaDiversaPrima) {

		String seminativoFilter = valoriParticellaColturaGreening.get(TipoVariabile.PFSUPDETSECONDA).getValString();
		Set<Entry<String, Double>> a = particelleColtura.stream().filter(x -> getPF(x.getLivello(), seminativoFilter).equals(true) && !x.getValString().equals(codColturaDiversaPrima))
				.collect(Collectors.groupingBy(w -> (w.getValString() != null) ? w.getValString() : (w.getParticella().getIdParticella() + "_" + w.getColtura()),
						Collectors.summingDouble(ParticellaColtura::getValNum)))
				.entrySet();

		if (!a.isEmpty()) {
			Map.Entry<String, Double> cODCOLTDIVERSA = a.stream().max(Comparator.comparing(Map.Entry<String, Double>::getValue)).orElseThrow(NoSuchElementException::new);
			return cODCOLTDIVERSA.getKey();
		} else {
			return "";
		}
	}

	private void setValCalcoloOut(List<VariabileCalcolo> listaOutput, MapVariabili variabiliCalcolo, TipoVariabile tipo, BigDecimal value) {
		VariabileCalcolo var = new VariabileCalcolo(tipo, value);
		addVariabile(listaOutput, variabiliCalcolo, var);
	}

	private void setValCalcoloOut(List<VariabileCalcolo> listaOutput, MapVariabili variabiliCalcolo, TipoVariabile tipo, String value) {
		VariabileCalcolo var = new VariabileCalcolo(tipo, value);
		addVariabile(listaOutput, variabiliCalcolo, var);
	}

	private void setValCalcoloOut(List<VariabileCalcolo> listaOutput, MapVariabili variabiliCalcolo, TipoVariabile tipo, boolean value) {
		VariabileCalcolo var = new VariabileCalcolo(tipo, value);
		addVariabile(listaOutput, variabiliCalcolo, var);
	}
	
	private void addVariabile(List<VariabileCalcolo> listaOutput, MapVariabili variabiliCalcolo, VariabileCalcolo var) {
		listaOutput.add(var);
		variabiliCalcolo.add(var.getTipoVariabile(), var);
	}
	
	@Override
	public TipologiaPassoTransizione getPasso() {
		return TipologiaPassoTransizione.GREENING;
	}

}
