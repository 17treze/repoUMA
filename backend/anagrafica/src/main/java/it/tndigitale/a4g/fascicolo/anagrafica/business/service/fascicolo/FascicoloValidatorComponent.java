package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.EredeModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.EredeDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaFisicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.StatusMessagesEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaProxyClient;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.proxy.client.model.FascicoloSian;
import it.tndigitale.a4g.proxy.client.model.PersonaFisicaDto;
import it.tndigitale.a4g.proxy.client.model.FascicoloSian.OrganismoPagatoreEnum;

@Component
class FascicoloValidatorComponent {

	@Autowired
	private FascicoloDao fascicoloDao;

	@Autowired
	private EredeDao eredeDao;
	
	@Autowired
	private AnagraficaProxyClient anagraficaProxyClient;
	
	@Autowired
	private PersonaFisicaDao personaFisicaDao;

	private static final Logger logger = LoggerFactory.getLogger(FascicoloValidatorComponent.class);
	static final String PROV_TRENTO = "TN";
	
	// BR1 - Verifica che NON esista fascicolo e mandato valido sui sistemi locali
	protected void nonEsisteFascicoloOrganismoPagatoreLocale(final String codiceFiscale) throws FascicoloValidazioneException {
		if (fascicoloDao.existsByCuaaAndIdValidazioneAndStatoChiuso(codiceFiscale, 0)) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.FASCICOLO_LOCALE_ESISTENTE);
		}
	}
	
	/**
	 * controlla codice fiscale passato da anagrafe tributaria,
	 * se uguale noop altrimenti esegui controllo in SIAN
	 * @throws FascicoloValidazioneException 
	 */
	protected void controlliMismatchAnagrafeTributariaECaa(
			final FascicoloOperationEnum fascicoloOperationEnum,
			final List<FascicoloCreationAnomalyEnum> anomalies,
			final String codiceFiscaleDaAnagrafeTributaria,
			final String codiceFiscaleDaCaa) throws FascicoloValidazioneException {
		if (!codiceFiscaleDaCaa.equals(codiceFiscaleDaAnagrafeTributaria)) {
			anomalies.add(FascicoloCreationAnomalyEnum.CUAA_MISMATCH_ANAGRAFE_TRIBUTARIA_CAA);
			esisteFascicoloSianConOrganismoPagatoreDiverso(codiceFiscaleDaAnagrafeTributaria);
			if (fascicoloOperationEnum.equals(FascicoloOperationEnum.APRI)) {
				nonEsisteFascicoloOrganismoPagatoreLocale(codiceFiscaleDaAnagrafeTributaria);
			}
		}
	}
	
	protected void controlloMismatchAnagrafeTributariaESian(
			final List<FascicoloCreationAnomalyEnum> anomalies,
			final String codiceFiscaleDaAnagrafeTributaria,
			final String codiceFiscaleDaCaa) throws FascicoloValidazioneException {
		if (esisteFascicoloSianConOrganismoPagatoreDiverso(codiceFiscaleDaAnagrafeTributaria) == null
				&& esisteFascicoloSianConOrganismoPagatoreDiverso(codiceFiscaleDaCaa) != null) {
			anomalies.add(FascicoloCreationAnomalyEnum.CUAA_MISMATCH_ANAGRAFE_TRIBUTARIA_SIAN);
		}
	}
	
	// BR2 - BR3 - Verifica esistenza fascicolo sul SIAN - Verifica se il fascicolo sul SIAN
	// ha un mandato valido di competenza di APPAG
	protected FascicoloSian esisteFascicoloSianConOrganismoPagatoreDiverso(final String codiceFiscale) throws FascicoloValidazioneException {
		logger.debug("Start - esisteFascicoloSian {}", codiceFiscale);
		FascicoloSian fascicoloSian = null;
		try {
			fascicoloSian = anagraficaProxyClient.verificaEsistenzaFascicolo(codiceFiscale);
		} catch (Exception e) {
			logger.warn("Il sistema informativo di SIAN non risponde per il codice fiscale {}", codiceFiscale);
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.SIAN_NON_DISPONIBILE);
		}
		if (fascicoloSian != null && !fascicoloSian.getOrganismoPagatore().equals(FascicoloSian.OrganismoPagatoreEnum.APPAG)) {
			logger.warn("Per il codice fiscale {} esiste in SIAN un fascicolo che e' di competenza di {}",
					codiceFiscale, fascicoloSian.getOrganismoPagatore());
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.COMPETENZA_ALTRO_OP);
		}
		return fascicoloSian;
	}

	void verificaRequisitiAperturaTrasferimentoFascicolo(
			final String codiceFiscale,
			final String provinciaSedeLegale) throws FascicoloValidazioneException {
		if (provinciaSedeLegale.equals(PROV_TRENTO)) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.PROVINCIA_ATTUALE_TRENTO);
		}
		// verifica fascicolo non gia' presente in A4G
		Optional<FascicoloModel> foundFascicolo = fascicoloDao.findByCuaaAndIdValidazione(codiceFiscale, 0);
		if (foundFascicolo.isPresent()) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.FASCICOLO_LOCALE_ESISTENTE);
		}
		// verifica fascicolo non gia' presente in SIAN
		if (anagraficaProxyClient.verificaEsistenzaFascicolo(codiceFiscale) != null) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.FASCICOLO_SIAN_ESISTENTE);
		}
	}
	
	void verificaRequisitiTrasferimentoFascicolo(
			final String codiceFiscale,
			final String provinciaSedeLegale) throws FascicoloValidazioneException {
		if (provinciaSedeLegale.equals(PROV_TRENTO)) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.PROVINCIA_ATTUALE_TRENTO);
		}
		// verifica fascicolo non gia' presente in A4G
		Optional<FascicoloModel> foundFascicolo = fascicoloDao.findByCuaaAndIdValidazione(codiceFiscale, 0);
		if (foundFascicolo.isPresent()) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.FASCICOLO_LOCALE_ESISTENTE);
		}
		// verifica fascicolo non gia' presente in SIAN
		FascicoloSian fascicoloSian = anagraficaProxyClient.verificaEsistenzaFascicolo(codiceFiscale);
		if (fascicoloSian == null) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.FASCICOLO_SIAN_NON_ESISTENTE);
		}
		if (fascicoloSian.getOrganismoPagatore().equals(OrganismoPagatoreEnum.APPAG)) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.COMPETENZA_APPAG);
		}
	}
	
	void verificaTrasferimentoChiusuraFascicolo(final String codiceFiscale) throws FascicoloValidazioneException {
		FascicoloSian fascicoloSian = anagraficaProxyClient.verificaEsistenzaFascicolo(codiceFiscale);
		if (fascicoloSian == null) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.FASCICOLO_SIAN_NON_ESISTENTE);
		}
		if (fascicoloSian.getOrganismoPagatore().equals(OrganismoPagatoreEnum.APPAG)) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.COMPETENZA_APPAG);
		}
	}

	void verificaOperatoreAppagFascicoloLocale(
			final String codiceFiscale) throws FascicoloValidazioneException {
		FascicoloModel foundFascicolo = fascicoloDao.findByCuaaAndIdValidazione(codiceFiscale, 0).orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));		
		if (foundFascicolo.getOrganismoPagatore() == null || (foundFascicolo.getOrganismoPagatore() != null && !foundFascicolo.getOrganismoPagatore().equals(it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.OrganismoPagatoreEnum.APPAG))) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.COMPETENZA_ALTRO_OP);
		}
	}
	
	void verificaStatoChiusoFascicoloLocale(
			final String codiceFiscale) throws FascicoloValidazioneException {
		FascicoloModel foundFascicolo = fascicoloDao.findByCuaaAndIdValidazione(codiceFiscale, 0).orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));		
		if (foundFascicolo.getStato() != StatoFascicoloEnum.CHIUSO) {
			throw new FascicoloValidazioneException(FascicoloValidazioneEnum.FASCIOLO_LOCALE_DIVERSO_DA_CHIUSO);
		}
	}

	void verificaFascicoloInChiusura(
			final PersonaFisicaDto personaFisicaDto) throws FascicoloValidazioneException {
		Optional<FascicoloModel> fascicoloOpt = fascicoloDao.findByCuaaAndIdValidazione(personaFisicaDto.getCodiceFiscale(), 0);
		LocalDate today = LocalDate.now();
		if (fascicoloOpt.isPresent()) {
			if (fascicoloOpt.get().getStato() == StatoFascicoloEnum.IN_CHIUSURA) {
				// check se e' passato un anno dalla morte
				if (personaFisicaDto.getDataMorte().isAfter(today.minusYears(1))) {
					List<EredeModel> eredi = eredeDao.findByFascicolo_Cuaa(personaFisicaDto.getCodiceFiscale());
					if (eredi == null || eredi.isEmpty()) {
						throw new FascicoloValidazioneException(FascicoloValidazioneEnum.FASCICOLO_IN_CHIUSURA_SENZA_EREDI);
					}
				}
				else {
					throw new FascicoloValidazioneException(FascicoloValidazioneEnum.DECEDUTO_DA_OLTRE_UN_ANNO);
				}
			} else if (fascicoloOpt.get().getStato() == StatoFascicoloEnum.IN_AGGIORNAMENTO) {
				if (personaFisicaDto.isDeceduta().booleanValue()) {
					Optional<PersonaFisicaModel> personaFisicaModel = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(personaFisicaDto.getCodiceFiscale(), 0);
					if (!(personaFisicaModel.isPresent() && !personaFisicaModel.get().getDeceduto())) {
						throw new FascicoloValidazioneException(FascicoloValidazioneEnum.DECEDUTO);
					}
				}
			} else {
				if (personaFisicaDto.isDeceduta().booleanValue()) {
					throw new FascicoloValidazioneException(FascicoloValidazioneEnum.DECEDUTO);
				}
			}
		} else {
			if (personaFisicaDto.isDeceduta().booleanValue()) {
				throw new FascicoloValidazioneException(FascicoloValidazioneEnum.DECEDUTO);
			}
		}
	}
}
