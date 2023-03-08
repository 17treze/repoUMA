package it.tndigitale.a4gistruttoria.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.soc.client.api.LiquidazioneControllerApi;
import it.tndigitale.a4g.soc.client.model.ImportoLiquidato;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.ElencoLiquidazioneModel;

@Component
public class ConsumeExternalRestApi4Soc extends ConsumeExternalRestApiAbstract {

	@Value("${a4gistruttoria.soc.uri}")
	private String urlSoc;


	private LiquidazioneControllerApi getLiquidazioneControllerApi() {
		return restClientProxy(LiquidazioneControllerApi.class,urlSoc);
	}


	/**
	 * Chiamata verso il servizio esterno per il recupero delle informaizoni da SOC
	 * @param domandaUnica
	 * @param elenco
	 * @return oggento contente le informaizoni degli importi richiesti
	 */
	public List<ImportoLiquidato> retrieveImpLiquidazioneByApi(DomandaUnicaModel domandaUnica, ElencoLiquidazioneModel elenco) {
		return getLiquidazioneControllerApi().getImportiLiquidazioneUsingGET(
				domandaUnica.getCampagna(), 
				domandaUnica.getCuaaIntestatario(), 
				domandaUnica.getNumeroDomanda(), 
				"DOMANDA_UNICA",
				elenco==null ? null : elenco.getId());
	}

	/**
	 * Chiamata verso il servizio esterno per il recupero delle informaizoni da SOC
	 * @param domandaUnica
	 * @param elenco
	 * @return oggento contente le informaizoni degli importi richiesti
	 */
	public List<ImportoLiquidato> retrieveImpLiquidazioneByApi(DomandaUnicaModel domandaUnica) {
		return retrieveImpLiquidazioneByApi(domandaUnica,null);
	}


}
