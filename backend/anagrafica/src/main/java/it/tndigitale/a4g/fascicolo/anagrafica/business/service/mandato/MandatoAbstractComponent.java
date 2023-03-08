package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DetenzioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaProxyClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloCreationAnomalyEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloValidazioneException;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAperturaFascicoloDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
abstract class MandatoAbstractComponent<P extends PersonaModel> {
	@Autowired
	private MandatoValidatorComponent validator;

	@Autowired
	private AnagraficaProxyClient anagraficaProxyClient;
	
	@Autowired
	DetenzioneService detenzioneService;

	private String codiceFiscaleDaCaa;

	protected abstract List<FascicoloCreationAnomalyEnum> verificaAperturaMandato() throws MandatoVerificaException;

	protected abstract DatiAperturaFascicoloDto getDatiPerAcquisizioneMandato() throws MandatoVerificaException;

	String getCodiceFiscaleDaCaa() {
		return codiceFiscaleDaCaa;
	}

	MandatoAbstractComponent<P> setCodiceFiscaleDaCaa(final String codiceFiscale) {
		this.codiceFiscaleDaCaa = codiceFiscale;
		return this;
	}

	protected void verificaDetenzioneNonPresenteSuFascicolo(FascicoloModel fascicoloModel) throws MandatoVerificaException {
		Optional<DetenzioneModel> detenzioneModelOptional = detenzioneService.getDetenzioneCorrente(fascicoloModel);
		if (!detenzioneModelOptional.isEmpty()) {
			throw new MandatoVerificaException(MandatoVerificaEnum.DETENZIONE_PRESENTE);
		}
	}
	
	protected AnagraficaProxyClient getAnagraficaProxyClient() {
		return anagraficaProxyClient;
	}
	
	protected MandatoValidatorComponent getValidator() {
		return validator;
	}
	
}
