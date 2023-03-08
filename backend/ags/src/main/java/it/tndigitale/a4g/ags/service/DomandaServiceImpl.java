package it.tndigitale.a4g.ags.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.ags.dto.CalcoloSostegnoAgs;
import it.tndigitale.a4g.ags.dto.ControlliPresentazione;
import it.tndigitale.a4g.ags.dto.DatiComune;
import it.tndigitale.a4g.ags.dto.DatiErede;
import it.tndigitale.a4g.ags.dto.DatiSettore;
import it.tndigitale.a4g.ags.dto.DomandaFilter;
import it.tndigitale.a4g.ags.dto.DomandaPsr;
import it.tndigitale.a4g.ags.dto.DomandaUnica;
import it.tndigitale.a4g.ags.dto.DomandaUnicaFilter;
import it.tndigitale.a4g.ags.dto.ErroreControlloRicevibilitaDomanda;
import it.tndigitale.a4g.ags.dto.InfoEleggibilitaParticella;
import it.tndigitale.a4g.ags.dto.InfoGeneraliDomanda;
import it.tndigitale.a4g.ags.dto.InfoIstruttoriaDomanda;
import it.tndigitale.a4g.ags.dto.InfoLiquidabilita;
import it.tndigitale.a4g.ags.dto.Richieste;
import it.tndigitale.a4g.ags.dto.ScadenziarioRicevibilitaDto;
import it.tndigitale.a4g.ags.dto.SostegniSuperficie;
import it.tndigitale.a4g.ags.dto.SostegnoDisaccoppiato;
import it.tndigitale.a4g.ags.dto.SostegnoSuperfici;
import it.tndigitale.a4g.ags.dto.domandaunica.DatiPascolo;
import it.tndigitale.a4g.ags.dto.domandaunica.DatiRicevibilita;
import it.tndigitale.a4g.ags.dto.domandaunica.DichiarazioniDomandaUnica;
import it.tndigitale.a4g.ags.dto.domandaunica.SostegniAllevamento;
import it.tndigitale.a4g.ags.model.DomandeCollegatePsrFilter;
import it.tndigitale.a4g.ags.model.StatoDomanda;
import it.tndigitale.a4g.ags.repository.dao.DomandaDaoImpl;
import it.tndigitale.a4g.ags.utils.ExpandManager;
import it.tndigitale.a4g.framework.time.LocalDateConverter;

/**
 * @author A.Siravo
 *
 */
@Service
public class DomandaServiceImpl implements DomandaService {

	private static final Logger logger = LoggerFactory.getLogger(DomandaServiceImpl.class);

	private static final String NO = "N";
	private static final String MODULO_RITIRO_TOTALE = "BPS_RITTOT_";
	private static final String MOVIMENTO_ISTRUTTORIA = "A4GIST";
	private static final String MOVIMENTO_NON_RICEVIBILITA = "NONRIC";

	private static final String MODULO_RITIRO_PARZIALE = "BPS_RITPRZ_";
	private static final String MODULO_DOMANDE_DI_MODIFICA = "BPS_ART_15_";
	private static final String MODULO_DOMANDE_INIZIALI = "BPS_";

	private final String CALCOLO_BPS = "DU_2017_SALDO_BGG";
	private final String CALCOLO_ACS = "DU_2017_SALDO_ACS";
	private final String CALCOLO_ACZ = "DU_2017_SALDO_ACZ";

	private final String LIQUID_BPS = "APP_LQDBGG";
	private final String LIQUID_ACS = "APP_LQDACS";
	private final String LIQUID_ACZ = "APP_LQDACZ";

	@Autowired
	private DomandaDaoImpl daoDomanda;

	@Autowired
	private SostegnoService sostegnoService;

	@Autowired
	private ScadenziarioComponent scadenziarioComponent;

	@Transactional(readOnly = true)
	@Override
	public Long countDomande(DatiSettore datiSettore, String moduloRitiroTotale) {
		return daoDomanda.countDomande(datiSettore.getAnnoRiferimento(), datiSettore.getCodicePac(), datiSettore.getTipoDomanda(), moduloRitiroTotale);
	}

	@Transactional()
	@Override
	public String eseguiMovimentazioneDomanda(Long numeroDomanda, String tipoMovimento) throws NoResultException {
		return daoDomanda.eseguiMovimentazioneDomanda(numeroDomanda, tipoMovimento);
	}

	@Override
	public List<DomandaPsr> getDomandePsr(DomandeCollegatePsrFilter domandeCollegatePsrFilter) {
		List<DomandaPsr> domandePsr = daoDomanda.getDomandePsr(domandeCollegatePsrFilter);
		if (CollectionUtils.isEmpty(domandePsr)) {
			return null;
		}
		return domandePsr.stream().filter(
				domanda -> domanda.getImportoRichiesto().compareTo(domandeCollegatePsrFilter.getImporto()) > 0 && domanda.getDataDomanda().after(domandeCollegatePsrFilter.getDataPresentazione()))
				.collect(Collectors.toList()); // cioè non rispecchia il filtro imposto dal business
	}

	@Override
	public CalcoloSostegnoAgs getCalcoloSostegnoDomandaPrecedente(String cuaa, String codCalcolo, String movLiquidazione) {
		return daoDomanda.getCalcoloSostegnoDomandaUnica(cuaa, codCalcolo, movLiquidazione);
	}

	@Override
	public Boolean checkExistDomandaPerSettore(String codicePac, String tipoDomanda, Long anno, String cuaa) {

		return daoDomanda.checkExistDomandaPerSettore(codicePac, tipoDomanda, anno, cuaa);
	}

	@Override
	@Transactional
	public boolean forzaMovimentazioneDomanda(Long numeroDomanda, String tipoMovimento) throws Exception {
		try {
			daoDomanda.forzaMovimentazioneDomanda(numeroDomanda, tipoMovimento);
		} catch (Exception e) {
			logger.error("Errore nella movimentazione in {} della domanda {}",tipoMovimento,numeroDomanda,e);
			return false;
		}
		return true;
	}

	/**
	 * Metodo per il recupero dei dati di ricevibilità dal sistema AGS in base all'identificativo univoco della domanda. 
	 * Consente di ottenere sia le informazioni generali che i dati di protocollazione
	 * 
	 * @param numeroDomanda Identificativo della domanda per la quale procedere al recupero dei dati di ricevibilità
	 * 
	 * @return DTO valorizzato con le informazioni recuperate dal sistema AGS
	 * 
	 * @throws NoResultException
	 */
	@Transactional(readOnly = true)
	@Override
	@Deprecated
	public DomandaUnica getDomandaUnica(Long numeroDomanda) throws NoResultException {
		List<String> expandParams = ExpandManager.getExpands();
		return getDomandaUnica(numeroDomanda, expandParams);
	}
	/**
	 * Metodo per il recupero dei dati di ricevibilità dal sistema AGS in base all'identificativo univoco della domanda. 
	 * Consente di ottenere sia le informazioni generali che i dati di protocollazione
	 * 
	 * @param numeroDomanda Identificativo della domanda per la quale procedere al recupero dei dati di ricevibilità
	 * 
	 * @return DTO valorizzato con le informazioni recuperate dal sistema AGS
	 * 
	 * @throws NoResultException
	 */
	@Transactional(readOnly = true)
	// @Override
	public DomandaUnica getDomandaUnica(Long numeroDomanda, List<String> expandParams) throws NoResultException {
		InfoGeneraliDomanda infoGeneraliDomanda = daoDomanda.getInfoGeneraliDomanda(numeroDomanda);
		ControlliPresentazione datiProtocollo = daoDomanda.getDatiProtocolloDomanda(numeroDomanda);
		Richieste richieste = new Richieste();
		richieste.setSintesiRichieste(daoDomanda.getSintesiRichiesteDomandaUnica(numeroDomanda, infoGeneraliDomanda));
		List<CalcoloSostegnoAgs> premiSostegno = new ArrayList<>();
		InfoIstruttoriaDomanda infoIstruttoria = null;
		InfoLiquidabilita infoLiquidabilita = null;
		DatiErede datiErede = null;

		if (expandParams.contains("sostegniSuperficie") || expandParams.contains("all")) {
			List<SostegniSuperficie> sostegniSuperficie = daoDomanda.getSostegniSuperficie(numeroDomanda);
			richieste.setSostegniSuperficie(sostegniSuperficie);
		}
		if (expandParams.contains("sostegniAllevamento") || expandParams.contains("all")) {
			List<SostegniAllevamento> sostegniAllevamento = daoDomanda.getSostegniAllevamento(numeroDomanda);
			richieste.setSostegniAllevamento(sostegniAllevamento);
		}
		if (expandParams.contains("datiPascolo") || expandParams.contains("all")) {
			List<DatiPascolo> datiPascolo = daoDomanda.getDatiPascolo(numeroDomanda);
			richieste.setDatiPascolo(datiPascolo);
		}
		if (expandParams.contains("dichiarazioni") || expandParams.contains("all")) {
			List<DichiarazioniDomandaUnica> dichiarazioniDU = daoDomanda.getDichiarazioniDomanda(numeroDomanda, infoGeneraliDomanda.getCampagna().intValue());
			richieste.setDichiarazioniDomandaUnica(dichiarazioniDU);
		}
		if (expandParams.contains("premiSostegno")) {
			CalcoloSostegnoAgs sostegnoBps = daoDomanda.getCalcoloSostegnoDomandaUnica(infoGeneraliDomanda.getCuaaIntestatario(), CALCOLO_BPS, LIQUID_BPS);
			CalcoloSostegnoAgs sostegnoAcs = daoDomanda.getCalcoloSostegnoDomandaUnica(infoGeneraliDomanda.getCuaaIntestatario(), CALCOLO_ACS, LIQUID_ACS);
			CalcoloSostegnoAgs sostegnoAcz = daoDomanda.getCalcoloSostegnoDomandaUnica(infoGeneraliDomanda.getCuaaIntestatario(), CALCOLO_ACZ, LIQUID_ACZ);
			if (sostegnoBps != null) {
				premiSostegno.add(sostegnoBps);
			}
			if (sostegnoAcs != null) {
				premiSostegno.add(sostegnoAcs);
			}
			if (sostegnoAcz != null) {
				premiSostegno.add(sostegnoAcz);
			}
		} else if (expandParams.contains("premiSostegno.BPS_2017") && infoGeneraliDomanda.getCampagna().equals(new BigDecimal("2018"))) {
			CalcoloSostegnoAgs sostegnoBps = daoDomanda.getCalcoloSostegnoDomandaUnica(infoGeneraliDomanda.getCuaaIntestatario(), CALCOLO_BPS, LIQUID_BPS);
			if (sostegnoBps != null) {
				premiSostegno.add(sostegnoBps);
			}
		}
		if (expandParams.contains("infoIstruttoria")) {
			List<InfoEleggibilitaParticella> infoElegDomanda = daoDomanda.getInfoEleggibilitaPartRichieste(numeroDomanda);
			if (infoElegDomanda != null) {
				infoIstruttoria = new InfoIstruttoriaDomanda();
				infoIstruttoria.setDatiEleggibilita(infoElegDomanda);
			}

		}

		if (expandParams.contains("infoLiquidabilita")) {
			infoLiquidabilita = new InfoLiquidabilita();
			infoLiquidabilita.setIbanValido(daoDomanda.getIbanValido(numeroDomanda) > 0);

			infoLiquidabilita.setTitolareDeceduto(daoDomanda.getDataMorte(numeroDomanda) != null);
			if (infoLiquidabilita.getTitolareDeceduto()) {
				// Dati di istruttoria relativi all'erede di un titolare deceduto
				datiErede = daoDomanda.getDatiErede(numeroDomanda);
				if (datiErede != null) {
					String iban = daoDomanda.getIbanErede(infoGeneraliDomanda.getCuaaIntestatario());
					datiErede.setIban(iban);
					datiErede.setIbanValido(iban != null);
					DatiComune datiComune = daoDomanda.getDatiComune(datiErede.getCodBelfResidenza());
					datiErede.setProvResidenza(datiComune != null ? datiComune.getSiglaProv() : null);
					datiErede.setCodiceIstat(datiComune != null ? datiComune.getCodiceIstat() : null);
					datiComune = daoDomanda.getDatiComune(datiErede.getCodBelfNascita());
					datiErede.setCodiceIstatNascita(datiComune != null ? datiComune.getCodiceIstat() : null);
					datiErede.setProvNascita(datiComune != null ? datiComune.getSiglaProv() : null);
					Long agea = daoDomanda.getDomandaSospesaAgeaBySoggetto(datiErede.getCodiceFiscale());
					datiErede.setDomandaSospesaAgea(agea > 0);
				}
				infoLiquidabilita.setDatiErede(datiErede);
			}

			infoLiquidabilita.setDomandaSospesaAgea(daoDomanda.getDomandaSospesaAgea(numeroDomanda) > 0);
			infoLiquidabilita.setIban(daoDomanda.getIban(numeroDomanda));
			infoLiquidabilita.setIbanFascicolo(daoDomanda.getIbanFascicolo(numeroDomanda));

		}

		if (infoGeneraliDomanda != null && datiProtocollo != null) {
			DomandaUnica domandaUnica = new DomandaUnica(infoGeneraliDomanda, datiProtocollo, richieste, premiSostegno, infoIstruttoria, infoLiquidabilita);
			caricaSostegniIn(domandaUnica);
			return domandaUnica;
		} else
			logger.warn("Dati incompleti per la domanda con n.ro {} info generali {} dati protocollo {}",
					numeroDomanda, (infoGeneraliDomanda != null), (datiProtocollo != null));
		throw new NoResultException("Dati incompleti per la domanda con n.ro " + numeroDomanda);
	}

	private void caricaSostegniIn(DomandaUnica domandaUnica) {
		if (domandaUnica.getInfoGeneraliDomanda().getNumeroDomanda()!=null &&
				domandaUnica.getInfoGeneraliDomanda().getCampagna()!=null) {
			SostegnoDisaccoppiato sostegnoDisaccoppiato = sostegnoService.detailDisaccoppiato(domandaUnica.getInfoGeneraliDomanda().getNumeroDomanda(),
					domandaUnica.getInfoGeneraliDomanda().getCampagna().intValue());

			SostegnoSuperfici sostegnoSuperfici = null;
			if (domandaUnica.getRichieste()!=null &&
					domandaUnica.getRichieste().getSintesiRichieste()!=null &&
					domandaUnica.getRichieste().getSintesiRichieste().isRichiestaSuperfici()) {
				sostegnoSuperfici = sostegnoService.detailSuperfici(domandaUnica.getInfoGeneraliDomanda().getNumeroDomanda());
			} else {
				sostegnoSuperfici = new SostegnoSuperfici();
			}
			domandaUnica.setSostegnoDisaccoppiato(sostegnoDisaccoppiato)
			.setSostegnoSuperfici(sostegnoSuperfici);
		}
	}

	/**
	 * Metodo per il recupero dei dati di ricevibilità dal sistema AGS in base ad i criteri di ricerca. Consente di ottenere sia le informazioni generali che i dati di protocollazione
	 * 
	 * @param domandaFilter
	 *            criteri di ricerca
	 * @return DTO valorizzato con le informazioni recuperate dal sistema AGS
	 */
	@Transactional(readOnly = true)
	@Override
	public List<DomandaUnica> getDomande(DomandaFilter domandaFilter) throws NoResultException {
		String settore = "PI2014";
		return daoDomanda.getListaDomandePerStato(domandaFilter.getCampagna(), settore, domandaFilter.getStati(), MODULO_RITIRO_TOTALE).stream().map(id -> {

			DomandaUnica domandaUnica = new DomandaUnica();
			InfoGeneraliDomanda info = new InfoGeneraliDomanda();
			info.setNumeroDomanda(id.longValue());
			domandaUnica.setInfoGeneraliDomanda(info);
			return domandaUnica;
		}).collect(Collectors.toList());
	}

	/**
	 * Metodo per la ricerca dei dati di ricevibilità dal sistema AGS in base all'anno di campagna. Consente di ottenere la lista di ID delle domande protocollate
	 *
	 * @param campagna anno campagna di riferimento
	 * @return Lista valorizzata con gli ID delle domande protocollate, recuperate dal sistema AGS
	 */
	@Transactional(readOnly = true)
	@Override
	public List<Long> getDomandeProtocollate(Integer campagna) throws NoResultException {
		return daoDomanda.getListaDomandeProtocollate(campagna, MODULO_RITIRO_TOTALE);
	}

	@Override
	@Transactional
	public DomandaUnica riceviDomanda(Long numeroDomanda) throws NoResultException {
		DomandaUnica domandaUnica = getDomandaUnica(numeroDomanda, Arrays.asList("all"));
		verificaRicevibilita(domandaUnica);
		aggiornaStatoDomanda(domandaUnica);
		return domandaUnica;
	}

	protected void verificaRicevibilita(DomandaUnica domandaUnica) {
		List<ErroreControlloRicevibilitaDomanda> listaErrori = new ArrayList<>();

		ControlliPresentazione controlliPresentazione = domandaUnica.getControlliPresentazione();

		if (isFuoriTermine.test(domandaUnica.getInfoGeneraliDomanda())) {
			listaErrori.add(ErroreControlloRicevibilitaDomanda.PRESENTAZIONE_NEI_TERMINI);
		}

		if(controlliPresentazione.getAggiornamentoFascicolo().equalsIgnoreCase(NO)){
			listaErrori.add(ErroreControlloRicevibilitaDomanda.FASCICOLO_AGGIORNATO);
		}

		if (controlliPresentazione.getArchiviazioneDomanda().equalsIgnoreCase(NO)) {
			listaErrori.add(ErroreControlloRicevibilitaDomanda.ARCHIVIAZIONE_DOCUMENTI_OK);
		}
		if (controlliPresentazione.getFirmaDomanda().equalsIgnoreCase(NO)) {
			listaErrori.add(ErroreControlloRicevibilitaDomanda.DOMANDA_CARTACEA_FIRMATA);
		}
		if (controlliPresentazione.getVisioneAnomalie().equalsIgnoreCase(NO)) {
			listaErrori.add(ErroreControlloRicevibilitaDomanda.ANOMALIE_COMPILAZIONE_AGS);
		}

		if (!listaErrori.isEmpty()) {
			domandaUnica.setListaErroriRicevibilita(listaErrori);
		}
	}

	private Predicate<InfoGeneraliDomanda> isFuoriTermine = domanda -> {
		ScadenziarioRicevibilitaDto scadenziario = scadenziarioComponent.getScadenziarioRicevibilita(domanda.getCampagna().intValue());

		LocalDate dataScadenzaDomandaInRitardo = LocalDateConverter.fromDate(scadenziario.getScadenzaDomandaInizialeInRitardo());
		LocalDate dataScadenzaDomandaModificaInRitardo = LocalDateConverter.fromDate(scadenziario.getScadenzaDomandaModificaInRitardo());
		LocalDate dataScadenzaDomandaRitiroParziale = LocalDateConverter.fromDate(scadenziario.getScadenzaDomandaRitiroParziale());

		LocalDate dtProtocollazione = LocalDateConverter.fromDate(domanda.getDataProtocollazione());
		LocalDate dtProtocollazioneOriginaria = LocalDateConverter.fromDate(domanda.getDataProtocollazOriginaria());
		LocalDate dtProtocollazioneUltimaModifica = LocalDateConverter.fromDate(domanda.getDtProtocollazioneUltimaModifica());

		// scarta gli ultimi 4 caratteri corrispondenti all'anno: BPS_RITPRZ_<YYYY> - BPS_ART_15_<YYYY> - BPS_<YYYY>
		var codiceModulo = domanda.getCodModulo().substring(0 , domanda.getCodModulo().length() - 4);

		switch(codiceModulo) {
		case MODULO_DOMANDE_INIZIALI:
			return dtProtocollazione.isAfter(dataScadenzaDomandaInRitardo);
		case MODULO_DOMANDE_DI_MODIFICA:
			return dtProtocollazione.isAfter(dataScadenzaDomandaModificaInRitardo) || dtProtocollazioneOriginaria.isAfter(dataScadenzaDomandaInRitardo);
		case MODULO_RITIRO_PARZIALE:
			return dtProtocollazione.isAfter(dataScadenzaDomandaRitiroParziale) || dtProtocollazioneOriginaria.isAfter(dataScadenzaDomandaInRitardo) || dtProtocollazioneUltimaModifica.isAfter(dataScadenzaDomandaModificaInRitardo);
		default: 
			return Boolean.FALSE;
		}
	};

	/**
	 * Metodo per il controllo della ricevibilità della domanda ed esportazione dei dati ad A4g.
	 * @param numeroDomanda
	 * @return
	 * @throws NoResultException
	 */
	@Transactional(readOnly = true)
	@Override
	public DatiRicevibilita ricevi(Long numeroDomanda) throws Exception {
		ControlliPresentazione datiProtocollo = daoDomanda.getDatiProtocolloDomanda(numeroDomanda);

		return null;
	}

	@Transactional
	protected void aggiornaStatoDomanda(DomandaUnica domandaUnica) {
		Long numeroDomanda = domandaUnica.getInfoGeneraliDomanda().getNumeroDomanda();
		String tipoMovimento = "";
		try {
			if (domandaUnica.getListaErroriRicevibilita() == null) {
				domandaUnica.getInfoGeneraliDomanda().setStato(StatoDomanda.RICEVIBILE);
				tipoMovimento = MOVIMENTO_ISTRUTTORIA;
			} else {

				domandaUnica.getInfoGeneraliDomanda().setStato(StatoDomanda.NON_RICEVIBILE);
				tipoMovimento = MOVIMENTO_NON_RICEVIBILITA;
			}

			this.eseguiMovimentazioneDomanda(numeroDomanda, tipoMovimento);

		} catch (Exception e) {
			logger.error("verificaRicevibilitaDomanda: errore verificando la ricevibilita della domanda " + numeroDomanda, e);
			throw new RuntimeException("Mancato aggiornamento in AGS dello stato della domanda " + numeroDomanda);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<DomandaUnica> getDomandeUniche(DomandaUnicaFilter domandaUnicaFilter)  {
		if (domandaUnicaFilter != null) {
			return daoDomanda.getListaDomande(domandaUnicaFilter);			
		}
		return new ArrayList<>();
	}


}
