package it.tndigitale.a4gistruttoria.service.businesslogic.domanda;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.ags.client.api.DomandeRestControllerApi;
import it.tndigitale.a4gistruttoria.A4gistruttoriaConfigurazione;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.DatiDetentore;
import it.tndigitale.a4gistruttoria.dto.DatiPascolo;
import it.tndigitale.a4gistruttoria.dto.DatiProprietario;
import it.tndigitale.a4gistruttoria.dto.DichiarazioniDomandaUnica;
import it.tndigitale.a4gistruttoria.dto.DomandaUnica;
import it.tndigitale.a4gistruttoria.dto.InfoGeneraliDomanda;
import it.tndigitale.a4gistruttoria.dto.InformazioniAllevamento;
import it.tndigitale.a4gistruttoria.dto.InformazioniColtivazione;
import it.tndigitale.a4gistruttoria.dto.Richieste;
import it.tndigitale.a4gistruttoria.dto.SintesiRichieste;
import it.tndigitale.a4gistruttoria.dto.SostegniAllevamentoDto;
import it.tndigitale.a4gistruttoria.dto.SostegniSuperficieDto;
import it.tndigitale.a4gistruttoria.dto.domandaunica.ErroreControlloRicevibilitaDomanda;
import it.tndigitale.a4gistruttoria.dto.lavorazione.RiferimentiCartografici;
import it.tndigitale.a4gistruttoria.repository.dao.AllevamentoImpegnatoDao;
import it.tndigitale.a4gistruttoria.repository.dao.DatiPascoloDao;
import it.tndigitale.a4gistruttoria.repository.dao.DichiarazioneDomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.ErroreRicevibilitaDao;
import it.tndigitale.a4gistruttoria.repository.dao.InterventoDao;
import it.tndigitale.a4gistruttoria.repository.dao.RichiestaSuperficieDao;
import it.tndigitale.a4gistruttoria.repository.dao.SostegnoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDatiPascolo;
import it.tndigitale.a4gistruttoria.repository.model.A4gtRichiestaSuperficie;
import it.tndigitale.a4gistruttoria.repository.model.AllevamentoImpegnatoModel;
import it.tndigitale.a4gistruttoria.repository.model.DichiarazioneDomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.ErroreRicevibilitaModel;
import it.tndigitale.a4gistruttoria.repository.model.Quadro;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.SostegnoModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;
import it.tndigitale.a4gistruttoria.service.ElencoPascoliService;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneDomandaException;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi;
import it.tndigitale.a4gistruttoria.util.LocalDateConverter;

@Service
public class RiceviDomandaService implements ElaborazioneDomanda {

	private static final Logger logger = LoggerFactory.getLogger(RiceviDomandaService.class);
	
	@Autowired
	private A4gistruttoriaConfigurazione configurazione;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private DomandaUnicaDao daoDomanda;
	@Autowired
	private ErroreRicevibilitaDao daoEsitoControllo;
	@Autowired
	private InterventoDao daoInterventoDu;
	@Autowired
	private RichiestaSuperficieDao daoRichiestaSuperficie;
	@Autowired
	private AllevamentoImpegnatoDao daoRichiestaAllevamDu;
	@Autowired
	private DatiPascoloDao daoDatiPascolo;
	@Autowired
	private DichiarazioneDomandaUnicaDao dichiarazioneDomandaUnicaDao;
	@Autowired
	private SostegnoDao daoSostegno;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private ElencoPascoliService elencoPascoliService;	
    @Autowired
    private ConsumeExternalRestApi consumeExternalRestApi;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ElaborazioneDomandaException.class)
	public void elabora(Long numeroDomanda) throws ElaborazioneDomandaException {
		DomandaUnica domandaUnica = recuperaDatiDomanda(numeroDomanda);
		Long idDomanda = saveDomandaUnicaAgs(domandaUnica);
		logger.info("Completata [Commit AGS OK; A4G in corso] ricevibilita' la domanda numero {} nella domanda con id {}", numeroDomanda, idDomanda);
	}

	/**
	 * Metodo che recupera da AGS i dati della domanda effettuando i controlli di ricevibilità
	 * @param numeroDomanda
	 * @return
	 * @throws EntityNotFoundException
	 */
	private DomandaUnica recuperaDatiDomanda(Long numeroDomanda) throws ElaborazioneDomandaException {
		try {
			String resource = configurazione.getUriAgs().concat(ApiUrls.AGS_DOMANDE_DU).concat("exportDomandaProtocollata/").concat(numeroDomanda.toString());
			DomandaUnica domandaUnica = restTemplate.getForObject(new URI(resource), DomandaUnica.class);
			return domandaUnica;
		} catch (URISyntaxException e) {
			logger.error("recuperaDatiDomanda: errore recuperando i dati della domanda " + numeroDomanda, e);
			throw new ElaborazioneDomandaException(e.getMessage());
		}
	}

	/**
	 * Metodo che salva tutti i dati della domanda recuperata da AGS
	 * @param domandaUnica
	 * @return
	 * @throws JsonProcessingException
	 */
	private Long saveDomandaUnicaAgs(DomandaUnica domandaUnica) throws ElaborazioneDomandaException{
		if (domandaUnica != null) {
			DomandaUnicaModel domandaUnicaModel = null;
			try {
				domandaUnicaModel = datiRicevibilitaDomUnicaDtoToModel(domandaUnica);
				domandaUnicaModel = daoDomanda.save(domandaUnicaModel);
				if (domandaUnicaModel.getStato().equals(StatoDomanda.NON_RICEVIBILE)) {
					saveErroriControlloRicevibilita(domandaUnicaModel, domandaUnica.getTipologiaControllo());
				} else {
					saveRichieste(domandaUnicaModel, domandaUnica.getRichieste());
				}
				saveSostegniDomanda(domandaUnicaModel, domandaUnica.getRichieste().getSintesiRichieste());
				return domandaUnicaModel.getId();
			}catch (DataIntegrityViolationException e){
				logger.error("saveDomandaUnicaAgs: errore per la domanda " + domandaUnicaModel.getId()+" perché già presente.", e);
				throw new ElaborazioneDomandaException("Errore di integrita salvando la domanda " + domandaUnicaModel.getId(), e);
			}
		} else {
			throw new EntityNotFoundException("Nessun dto DatiRicevibilita trovato");
		}
	}


	private DomandaUnicaModel datiRicevibilitaDomUnicaDtoToModel(DomandaUnica domandaUnica) throws ElaborazioneDomandaException {
		if (domandaUnica != null) {
			DomandaUnicaModel domandaUnicaModel = new DomandaUnicaModel();
			InfoGeneraliDomanda infoGeneraliDto = domandaUnica.getInfoGeneraliDomanda();
			domandaUnicaModel.setCodEnteCompilatore(new BigDecimal(infoGeneraliDto.getCodEnteCompilatore()));
			domandaUnicaModel.setCodModuloDomanda(infoGeneraliDto.getCodModulo());
			domandaUnicaModel.setCuaaIntestatario(infoGeneraliDto.getCuaaIntestatario());
			domandaUnicaModel.setDescEnteCompilatore(infoGeneraliDto.getEnteCompilatore());
			domandaUnicaModel.setDescModuloDomanda(infoGeneraliDto.getModulo());
			domandaUnicaModel.setDtPresentazione(LocalDateConverter.fromDate(infoGeneraliDto.getDataPresentazione()));
			domandaUnicaModel.setRagioneSociale(infoGeneraliDto.getRagioneSociale());
			domandaUnicaModel.setDtProtocollazione(infoGeneraliDto.getDataProtocollazione());
			domandaUnicaModel.setDtProtocollazioneUltimaModifica(infoGeneraliDto.getDtProtocollazioneUltimaModifica());
			domandaUnicaModel.setNumeroDomandaUltimaModifica(infoGeneraliDto.getNumeroDomandaUltimaModifica());
			if (infoGeneraliDto.getNumeroDomandaRettificata() != null) {
				domandaUnicaModel.setNumDomandaRettificata(infoGeneraliDto.getNumeroDomandaRettificata());
			}
			domandaUnicaModel.setNumeroDomanda(infoGeneraliDto.getNumeroDomanda());
			domandaUnicaModel.setDtPresentazOriginaria(infoGeneraliDto.getDataProtocollazOriginaria());
			domandaUnicaModel.setDtProtocollazOriginaria(infoGeneraliDto.getDataProtocollazOriginaria());
			domandaUnicaModel.setCampagna(infoGeneraliDto.getCampagna());
			String statoDomandaAgs = infoGeneraliDto.getStato();
			switch (statoDomandaAgs) {
				case "NON_RICEVIBILE":
					domandaUnicaModel.setStato(StatoDomanda.NON_RICEVIBILE);
					break;
				case "RICEVIBILE":
					domandaUnicaModel.setStato(StatoDomanda.RICEVIBILE);
					break;
				case "ACQUISITA":
					domandaUnicaModel.setStato(StatoDomanda.ACQUISITA);
					break;
				case "IN_ISTRUTTORIA":
					domandaUnicaModel.setStato(StatoDomanda.IN_ISTRUTTORIA);
					break;
				case "PROTOCOLLATA":
					domandaUnicaModel.setStato(StatoDomanda.PROTOCOLLATA);
					break;
			}
			return domandaUnicaModel;
		} else {
			return null;
		}
	}
	

	/**
	 * Metodo che salva gli eventuali errori rilevati dal controllo di ricevibilità della domanda recuperata da AGS
	 * @param domandaUnicaModel
	 * @param erroriRicevibilita
	 */
	private void saveErroriControlloRicevibilita(DomandaUnicaModel domandaUnicaModel, List<ErroreControlloRicevibilitaDomanda> erroriRicevibilita) {
		if(erroriRicevibilita!=null){
			for (ErroreControlloRicevibilitaDomanda erroreRicevibilita : erroriRicevibilita) {
				ErroreRicevibilitaModel nuovoEsito = new ErroreRicevibilitaModel();
				nuovoEsito.setDtEsecuzione(new Date());
				nuovoEsito.setTipologiaControllo(erroreRicevibilita);
				nuovoEsito.setDomandaUnicaModel(domandaUnicaModel);
				daoEsitoControllo.save(nuovoEsito);
			}
		}
	}

	/**
	 * Metodo che salva le richieste della domanda recuperata da AGS
	 * @param domandaUnicaModel
	 * @param richieste
	 */
	private void saveRichieste(DomandaUnicaModel domandaUnicaModel, Richieste richieste) {
		if (richieste != null) {
			saveSostegniSuperficie(domandaUnicaModel, richieste.getSostegniSuperficie());
			saveSostegniAllevamento(domandaUnicaModel, richieste.getSostegniAllevamento());
			saveDatiPascolo(domandaUnicaModel, richieste.getDatiPascolo());
			saveDichiarazioniDomanda(domandaUnicaModel, richieste.getDichiarazioniDomandaUnica());
		} else {
			throw new EntityNotFoundException("Nessun dto Richieste trovato");
		}
	}

	/**
	 * Metodo che salva i sostegni superficie della domanda recuperata da AGS
	 * @param domandaUnicaModel
	 * @param sostegniSuperficie
	 */
	private void saveSostegniSuperficie(DomandaUnicaModel domandaUnicaModel, List<SostegniSuperficieDto> sostegniSuperficie) {
		if (sostegniSuperficie != null) {
			sostegniSuperficie.forEach(sostegnoSuperficie -> {
				try {
					Long value = persistiSostegniSuperfici(domandaUnicaModel, sostegnoSuperficie);
					logger.debug("Persistenza sostegno superficie: {}", value);
				} catch (JsonProcessingException e) {
					logger.error("recuperaSostegniDomandaDU: errore per la domanda " + domandaUnicaModel.getId(), e);
					throw new RuntimeException(e);
				}
			});
		}
	}

	/**
	 * Metodo che salva i sostegni allevamento della domanda recuperata da AGS
	 * @param domandaUnicaModel
	 * @param sostegniAllevamento
	 */
	private void saveSostegniAllevamento(DomandaUnicaModel domandaUnicaModel, List<SostegniAllevamentoDto> sostegniAllevamento) {
		if (sostegniAllevamento != null) {
			sostegniAllevamento.forEach(sostegnoAllevamento -> {
				try {
					Long value = persistiSostegniAllevamento(domandaUnicaModel, sostegnoAllevamento);
					logger.debug("Persistenza sostegno allevamento: {}", value);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			});
		}
	}

	/**
	 * Metodo che salva i dati pascolo della domanda recuperata da AGS
	 * @param domandaUnicaModel
	 * @param datiPascolo
	 */
	private void saveDatiPascolo(DomandaUnicaModel domandaUnicaModel, List<DatiPascolo> datiPascolo) {
		if (datiPascolo != null) {
			datiPascolo.forEach(datoPascolo -> {
				try {
					Long value = persistiDatiPascolo(domandaUnicaModel, datoPascolo);
					logger.debug("Persistenza dati pascolo: {}", value);
				} catch (JsonProcessingException e) {
					logger.debug("Erore salvando dati per pascolo: {}", datoPascolo.getCodPascolo(), e);
					throw new RuntimeException(e);
				}
			});
			try {
				elencoPascoliService.estraiParticellePascolo(domandaUnicaModel.getId());
			} catch (IOException e) {
				logger.debug("Erore salvando particelle pascolo per la domanda : {}", domandaUnicaModel.getId(), e);
				throw new RuntimeException(e);
			}
			
		}
		try {
			elencoPascoliService.estraiPascoliAziendali(domandaUnicaModel);
		} catch (IOException e) {
			logger.error("Erore salvando pascoli aziendali per la domanda : {}", domandaUnicaModel.getId(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Metodo che salva le dichiarazioni della domanda recuperata da AGS
	 * @param domandaUnicaModel
	 * @param dichiarazioniDomandaUnica
	 */
	private void saveDichiarazioniDomanda(DomandaUnicaModel domandaUnicaModel, List<DichiarazioniDomandaUnica> dichiarazioniDomandaUnica) {
		if (dichiarazioniDomandaUnica != null) {
			dichiarazioniDomandaUnica.forEach(dichirazione -> {
				Long value = persistiDichiarazioniDomandaUnica(domandaUnicaModel, dichirazione);
				logger.debug("Persistenza dichirazione: {}", value);
			});
		}
	}

	/**
	 * Metodo che salva i sostegni della domdanda recuperata da AGS
	 * @param domandaUnicaModel
	 */
	private void saveSostegniDomanda(DomandaUnicaModel domandaUnicaModel, SintesiRichieste sostegniRichiesti) {
		if (sostegniRichiesti != null) {
			if (sostegniRichiesti.isRichiestaDisaccoppiato()) {
				SostegnoModel sostegnoModel = new SostegnoModel();
				sostegnoModel.setDomandaUnicaModel(domandaUnicaModel);
				sostegnoModel.setSostegno(Sostegno.DISACCOPPIATO);
				daoSostegno.save(sostegnoModel);				
			}
			if (sostegniRichiesti.isRichiestaSuperfici()) {
				SostegnoModel sostegnoModel = new SostegnoModel();
				sostegnoModel.setDomandaUnicaModel(domandaUnicaModel);
				sostegnoModel.setSostegno(Sostegno.SUPERFICIE);
				daoSostegno.save(sostegnoModel);				
			}
			if (sostegniRichiesti.isRichiestaZootecnia()) {
				SostegnoModel sostegnoModel = new SostegnoModel();
				sostegnoModel.setDomandaUnicaModel(domandaUnicaModel);
				sostegnoModel.setSostegno(Sostegno.ZOOTECNIA);
				daoSostegno.save(sostegnoModel);				
			}
		}
	}

	// Salvataggio dei dati in AG4T_RICHIESTE_SUPERFICIE
	private Long persistiSostegniSuperfici(DomandaUnicaModel domanda, SostegniSuperficieDto sostegniSuperficie) throws JsonProcessingException {

		A4gtRichiestaSuperficie richSuperficie = new A4gtRichiestaSuperficie();
		richSuperficie.setDomandaUnicaModel(domanda);
		richSuperficie.setIntervento(daoInterventoDu.findByIdentificativoIntervento(sostegniSuperficie.getCodIntervento()));
		richSuperficie.setSupRichiesta(new BigDecimal(sostegniSuperficie.getSupImpegnata()));
		richSuperficie.setSupRichiestaNetta(new BigDecimal(sostegniSuperficie.getSupImpegnata() * sostegniSuperficie.getCoeffTara()));
		richSuperficie.setCodiceColtura3(sostegniSuperficie.getCodColtura3());
		richSuperficie.setCodiceColtura5(sostegniSuperficie.getCodColtura5());
		richSuperficie.setInfoCatastali(mapper.writeValueAsString(sostegniSuperficie.getParticella()));
		InformazioniColtivazione infoColtivazione = new InformazioniColtivazione();
		infoColtivazione.setCodColtura3(sostegniSuperficie.getCodColtura3());
		infoColtivazione.setCodColtura5(sostegniSuperficie.getCodColtura5());
		infoColtivazione.setCodLivello(sostegniSuperficie.getCodLivello());
		infoColtivazione.setCoefficienteTara(sostegniSuperficie.getCoeffTara());
		infoColtivazione.setDescrizioneColtura(sostegniSuperficie.getDescColtura());
		infoColtivazione.setIdColtura(sostegniSuperficie.getIdColtura());
		infoColtivazione.setIdPianoColture(sostegniSuperficie.getIdPianoColture());
		infoColtivazione.setSuperficieDichiarata(sostegniSuperficie.getSupDichiarata());
		infoColtivazione.setDescMantenimento(sostegniSuperficie.getDescMantenimento());
		richSuperficie.setInfoColtivazione(mapper.writeValueAsString(infoColtivazione));
		RiferimentiCartografici rifCartografici = new RiferimentiCartografici();
		rifCartografici.setIdParcella(sostegniSuperficie.getIdParcella());
		rifCartografici.setIdIsola(sostegniSuperficie.getIdIsola());
		rifCartografici.setCodIsola(sostegniSuperficie.getCodIsola());
		richSuperficie.setRiferimentiCartografici(mapper.writeValueAsString(rifCartografici));
		daoRichiestaSuperficie.save(richSuperficie);
		return richSuperficie.getId();
	}

	// Salvataggio dei dati in A4GT_RICHIESTE_ALLEVAM_DU
	private Long persistiSostegniAllevamento(DomandaUnicaModel domanda, SostegniAllevamentoDto sostegniAllevamenti) throws JsonProcessingException {
		AllevamentoImpegnatoModel allevamentoImpegnato = new AllevamentoImpegnatoModel();
		allevamentoImpegnato.setDomandaUnica(domanda);
		allevamentoImpegnato.setIntervento(daoInterventoDu.findByIdentificativoIntervento(sostegniAllevamenti.getCodIntervento()));
		allevamentoImpegnato.setCodiceSpecie(sostegniAllevamenti.getSpecie());
		InformazioniAllevamento infoAllevamento = new InformazioniAllevamento();
		infoAllevamento.setCodiceAllevamento(sostegniAllevamenti.getCodIdAllevamento());
		infoAllevamento.setCodiceAllevamentoBdn(sostegniAllevamenti.getCodIdBdn());
		infoAllevamento.setComune(sostegniAllevamenti.getComune());
		infoAllevamento.setDescrizioneAllevamento(sostegniAllevamenti.getDescAllevamento());
		infoAllevamento.setIdAllevamento(sostegniAllevamenti.getIdAllevamento());
		infoAllevamento.setIndirizzo(sostegniAllevamenti.getIndirizzo());
		allevamentoImpegnato.setDatiAllevamento(mapper.writeValueAsString(infoAllevamento));

		DatiProprietario datiProprietario = new DatiProprietario();
		datiProprietario.setCodFiscaleProprietario(sostegniAllevamenti.getCodFiscaleProprietario());
		datiProprietario.setDenominazioneProprietario(sostegniAllevamenti.getDenominazioneProprietario());
		allevamentoImpegnato.setDatiProprietario(mapper.writeValueAsString(datiProprietario));

		DatiDetentore datiDetentore = new DatiDetentore();
		datiDetentore.setCodFiscaleDetentore(sostegniAllevamenti.getCodFiscaleDetentore());
		datiDetentore.setDenominazioneDetentore(sostegniAllevamenti.getDenominazioneDetentore());
		allevamentoImpegnato.setDatiDetentore(mapper.writeValueAsString(datiDetentore));
		daoRichiestaAllevamDu.save(allevamentoImpegnato);
		return allevamentoImpegnato.getId();
	}

	// Salvataggio dei dati in A4GT_DATI_PASCOLO
	private Long persistiDatiPascolo(DomandaUnicaModel domanda, DatiPascolo datiPascolo) throws JsonProcessingException {
		A4gtDatiPascolo datiPascoloDomanda = new A4gtDatiPascolo();
		datiPascoloDomanda.setDomandaUnicaModel(domanda);
		datiPascoloDomanda.setCodicePascolo(datiPascolo.getCodPascolo());
		datiPascoloDomanda.setDescrizionePascolo(datiPascolo.getDescPascolo());
		datiPascoloDomanda.setUbaDichiarate(new BigDecimal(datiPascolo.getUba()));
		datiPascoloDomanda.setParticelleCatastali(mapper.writeValueAsString(datiPascolo.getParticelle()));
		daoDatiPascolo.save(datiPascoloDomanda);
		return datiPascoloDomanda.getId();
	}

	// Salvataggio dei dati in A4GT_DICHIARAZIONI_DU
	private Long persistiDichiarazioniDomandaUnica(DomandaUnicaModel domanda, DichiarazioniDomandaUnica dichiarazioniDomandaUnica) {
		DichiarazioneDomandaUnicaModel dichDu = new DichiarazioneDomandaUnicaModel();
		dichDu.setDomandaUnicaModel(domanda);
		// TODO Lorenzo: questo set va sicuramente in errore: in fase di ricevibilita' in asg si traduce CodDocumento in Quadro e lo si ritorna ad istruttoria
		dichDu.setQuadro(Quadro.valueOf(dichiarazioniDomandaUnica.getCodDocumento()));
		dichDu.setCodice(dichiarazioniDomandaUnica.getIdCampo());
		dichDu.setDescrizione(dichiarazioniDomandaUnica.getDescCampo());
		if (dichiarazioniDomandaUnica.getValCheck() != null) {
			dichDu.setValoreBool(dichiarazioniDomandaUnica.getValCheck() ? new BigDecimal(1) : new BigDecimal(0));
		}
		if (dichiarazioniDomandaUnica.getValDate() != null) {
			dichDu.setValoreData(dichiarazioniDomandaUnica.getValDate());
		}
		if (dichiarazioniDomandaUnica.getValNumber() != null) {
			dichDu.setValoreNumero(new BigDecimal(dichiarazioniDomandaUnica.getValNumber()));
		}
		if (dichiarazioniDomandaUnica.getValString() != null) {
			dichDu.setValoreStringa(dichiarazioniDomandaUnica.getValString());
		}

		dichDu.setOrdine(dichiarazioniDomandaUnica.getOrdine());

		dichiarazioneDomandaUnicaDao.save(dichDu);
		return dichDu.getId();
	}

	@Override
	public List<Long> caricaIdDaElaborare(
			Integer annoCampagna) throws ElaborazioneDomandaException {
        DomandeRestControllerApi domandeRestControllerApi = consumeExternalRestApi.restClientDomandaUnica(DomandeRestControllerApi.class);
        return domandeRestControllerApi.getDomandeProtocollateUsingGET(annoCampagna);
	}
}
