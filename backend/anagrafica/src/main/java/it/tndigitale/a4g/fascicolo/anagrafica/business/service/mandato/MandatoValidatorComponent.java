package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.StatusMessagesEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaProxyClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloCreationAnomalyEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloOperationEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloValidazioneEnum;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.proxy.client.model.FascicoloSian;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Component
class MandatoValidatorComponent {

	@Autowired
	private FascicoloDao fascicoloDao;
	@Autowired
	private AnagraficaProxyClient anagraficaProxyClient;

	private static final Logger logger = LoggerFactory.getLogger(MandatoValidatorComponent.class);
	static final String PROV_TRENTO = "TN";

	FascicoloModel verificaFascicoloLocaleEsistenzaEInStatoNonChiuso(
			final String codiceFiscale) throws MandatoVerificaException {
		try {
			FascicoloModel foundFascicolo = fascicoloDao.findByCuaaAndIdValidazione(codiceFiscale, 0).orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));
			if (foundFascicolo.getStato() == StatoFascicoloEnum.CHIUSO) {
				throw new MandatoVerificaException(MandatoVerificaEnum.FASCICOLO_CHIUSO);
			}
			return foundFascicolo;
		} catch (EntityNotFoundException e) {
			throw new MandatoVerificaException(MandatoVerificaEnum.FASCICOLO_NON_PRESENTE);
		}
	}

	/**
	 * controlla codice fiscale passato da anagrafe tributaria,
	 * se uguale noop altrimenti esegui controllo in SIAN
	 * @throws MandatoVerificaException
	 */
	protected void controlliMismatchAnagrafeTributariaECaa(
			final List<FascicoloCreationAnomalyEnum> anomalies,
			final String codiceFiscaleDaAnagrafeTributaria,
			final String codiceFiscaleDaCaa) throws MandatoVerificaException {
		if (!codiceFiscaleDaCaa.equals(codiceFiscaleDaAnagrafeTributaria)) {
			anomalies.add(FascicoloCreationAnomalyEnum.CUAA_MISMATCH_ANAGRAFE_TRIBUTARIA_CAA);
			esisteFascicoloSianConOrganismoPagatoreDiverso(codiceFiscaleDaAnagrafeTributaria);
		}
	}

	protected void controlloMismatchAnagrafeTributariaESian(
			final List<FascicoloCreationAnomalyEnum> anomalies,
			final String codiceFiscaleDaAnagrafeTributaria,
			final String codiceFiscaleDaCaa) throws MandatoVerificaException {
		if (esisteFascicoloSianConOrganismoPagatoreDiverso(codiceFiscaleDaAnagrafeTributaria) == null
				&& esisteFascicoloSianConOrganismoPagatoreDiverso(codiceFiscaleDaCaa) != null) {
			anomalies.add(FascicoloCreationAnomalyEnum.CUAA_MISMATCH_ANAGRAFE_TRIBUTARIA_SIAN);
		}
	}

	// Verifica se il fascicolo sul SIAN ha un mandato valido di competenza di APPAG
	protected FascicoloSian esisteFascicoloSianConOrganismoPagatoreDiverso(final String codiceFiscale) throws MandatoVerificaException {
		logger.debug("Start - esisteFascicoloSian {}", codiceFiscale);
		FascicoloSian fascicoloSian = null;
		try {
			fascicoloSian = anagraficaProxyClient.verificaEsistenzaFascicolo(codiceFiscale);
		} catch (Exception e) {
			logger.warn("Il sistema informativo di SIAN non risponde per il codice fiscale {}", codiceFiscale);
			throw new MandatoVerificaException(FascicoloValidazioneEnum.SIAN_NON_DISPONIBILE);
		}
		if (fascicoloSian != null && !fascicoloSian.getOrganismoPagatore().equals(FascicoloSian.OrganismoPagatoreEnum.APPAG)) {
			logger.warn("Per il codice fiscale {} esiste in SIAN un fascicolo che e' di competenza di {}",
					codiceFiscale, fascicoloSian.getOrganismoPagatore());
			throw new MandatoVerificaException(FascicoloValidazioneEnum.COMPETENZA_ALTRO_OP);
		}
		return fascicoloSian;
	}
}
