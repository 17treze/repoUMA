package it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloDto;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.AnagraficaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.DotazioneTecnicaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.TerritorioPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.ZootecniaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo.exception.FascicoloInvalidConditionException;
import it.tndigitale.a4g.fascicolo.mediator.dto.EsitoControlloDto;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;


@Service
public class ControlliCompletezzaFascicoloService {

	@Autowired private AnagraficaPrivateClient anagraficaPrivateClient;
	@Autowired private ZootecniaPrivateClient zootecniaPrivateClient;
	@Autowired private TerritorioPrivateClient territorioPrivateClient;
	@Autowired private DotazioneTecnicaPrivateClient dotazioneTecnicaPrivateClient;

	private void mergeControlloCompletezza_ControlliInCorso_InAggiornamento (HashMap<String, EsitoControlloDto> input) {
		if (input.get("IS_" + StatoFascicoloEnum.CONTROLLI_IN_CORSO).getEsito() == null
				&& input.get("IS_" + StatoFascicoloEnum.IN_AGGIORNAMENTO).getEsito() == null) {
			// 		CONTROLLI_IN_CORSO.getEsito() == null && IN_AGGIORNAMENTO.getEsito() == null --> rimuovere CONTROLLI_IN_CORSO
			input.remove("IS_" + StatoFascicoloEnum.CONTROLLI_IN_CORSO);
		} else if (input.get("IS_" + StatoFascicoloEnum.CONTROLLI_IN_CORSO).getEsito() != null
				&& input.get("IS_" + StatoFascicoloEnum.IN_AGGIORNAMENTO).getEsito() == null) {
			// 		CONTROLLI_IN_CORSO.getEsito() != null && IN_AGGIORNAMENTO.getEsito() == null --> copiare CONTROLLI_IN_CORSO in IN_AGGIORNAMENTO
			var target = new EsitoControlloDto();
			BeanUtils.copyProperties(input.get("IS_" + StatoFascicoloEnum.CONTROLLI_IN_CORSO), target);
			input.replace("IS_" + StatoFascicoloEnum.IN_AGGIORNAMENTO, target);
			input.remove("IS_" + StatoFascicoloEnum.CONTROLLI_IN_CORSO);
		} else if (input.get("IS_" + StatoFascicoloEnum.CONTROLLI_IN_CORSO).getEsito() == null
				&& input.get("IS_" + StatoFascicoloEnum.IN_AGGIORNAMENTO).getEsito() != null) {
			// 		CONTROLLI_IN_CORSO.getEsito() == null && IN_AGGIORNAMENTO.getEsito() != null --> rimuovere CONTROLLI_IN_CORSO
			input.remove("IS_" + StatoFascicoloEnum.CONTROLLI_IN_CORSO);
		} else {
			// 		CONTROLLI_IN_CORSO.getEsito() != null && IN_AGGIORNAMENTO.getEsito() != null --> controllo se almeno uno stato != -3
			//		Gli stati del fascicolo CONTROLLI_IN_CORSO e IN_AGGIORNAMENTO vengono verificati insieme, perche'
			//		passano solo se il fascicolo si trova nello stato CONTROLLI_IN_CORSO o IN_AGGIORNAMENTO
			if (input.get("IS_" + StatoFascicoloEnum.CONTROLLI_IN_CORSO).getEsito() != -3
					|| input.get("IS_" + StatoFascicoloEnum.IN_AGGIORNAMENTO).getEsito() != -3) {
				input.remove("IS_" + StatoFascicoloEnum.CONTROLLI_IN_CORSO);
				var target = new EsitoControlloDto();
				BeanUtils.copyProperties(input.get("IS_" + StatoFascicoloEnum.IN_AGGIORNAMENTO), target);
				target.setEsito(0);
				input.replace("IS_" + StatoFascicoloEnum.IN_AGGIORNAMENTO, target);
			} else {
				input.remove("IS_" + StatoFascicoloEnum.CONTROLLI_IN_CORSO);
			}
		}
	}

	@Transactional
	public void gestisciFascicoloControlloCompletezza() {
		List<FascicoloDto>  fascicoloDtoList = anagraficaPrivateClient.getElencoFascicoliInStatoControlliInCorso();
		if (fascicoloDtoList != null && !fascicoloDtoList.isEmpty()) {
			//			per ogni cuaa eseguire queryControlloCompletezzaFascicolo(final String cuaa)
			fascicoloDtoList.forEach(fascicoloDto -> {
				queryControlloCompletezzaFascicolo(fascicoloDto.getCuaa());
			});
		}
	}

	@Transactional
	public Map<String, EsitoControlloDto> queryControlloCompletezzaFascicolo(final String cuaa)
			throws NoSuchElementException {
		//		gli esiti sono:
		//		0  = ok,
		//		-1 = non bloccante,
		//		-2 = no-data,
		//		-3 = bloccante
		var mapControlliCompletezza = new HashMap<String, EsitoControlloDto>();
		var retAnagraficaList = anagraficaPrivateClient.queryControlloCompletezzaFascicolo(cuaa);
		if (retAnagraficaList != null && !retAnagraficaList.isEmpty()) {
			mapControlliCompletezza.putAll(retAnagraficaList);
		} else {
			return mapControlliCompletezza;
		}
		var retZootecniaList = zootecniaPrivateClient.queryControlloCompletezzaFascicolo(cuaa);
		if (retZootecniaList != null && !retZootecniaList.isEmpty()) {
			mapControlliCompletezza.putAll(retZootecniaList);
		}
		var retTerritorioList = territorioPrivateClient.queryControlloCompletezzaFascicolo(cuaa);
		if (retTerritorioList != null && ! retTerritorioList.isEmpty()) {
			mapControlliCompletezza.putAll(retTerritorioList);
		}
		var retDotazioneTecnicaList = dotazioneTecnicaPrivateClient.queryControlloCompletezzaFascicolo(cuaa);
		if (retDotazioneTecnicaList != null && ! retDotazioneTecnicaList.isEmpty()) {
			mapControlliCompletezza.putAll(retDotazioneTecnicaList);
		}

		mergeControlloCompletezza_ControlliInCorso_InAggiornamento(mapControlliCompletezza);

		//		verificare se tutti gli esiti sono != null; solo in tal caso i controlli sul fascicolo si dicono completi (indipendentemente
		//		dagli esiti)
		List<String> esitiPendentiList = mapControlliCompletezza.entrySet().stream()
				.filter(e -> e.getValue().getEsito() == null)
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
		if (esitiPendentiList != null && !esitiPendentiList.isEmpty()) {
			return mapControlliCompletezza;
		}

		//		a questo punto i controlli sono tutti terminati. Si deve stabilire se ci sono errori bloccanti (CONTROLLI_IN_CORSO-> IN_AGGIORNAMENTO) o
		//		meno (CONTROLLI_IN_CORSO-> CONTROLLATO_OK).

		//		se lo stato del fascicolo è diverso da IN_AGGIORNAMENTO e CONTROLLI_IN_CORSO ritorna subito senza aggiornare lo stato
		var fascicoloLive = anagraficaPrivateClient.getByCuaaUsingGET1(cuaa, 0);
		if (!fascicoloLive.getStato().equals(FascicoloDto.StatoEnum.IN_AGGIORNAMENTO) && !fascicoloLive.getStato().equals(FascicoloDto.StatoEnum.CONTROLLI_IN_CORSO)) {
			return mapControlliCompletezza;
		}

		//		Una volta terminati i controlli si deve verificarne gli esiti.
		//		Se non c'è un errore bloccante (-3) il fascicolo verrà messo nello stato CONTROLLATO_OK; altrimenti verrà messo IN_AGGIORNAMENTO
		for (Map.Entry<String, EsitoControlloDto> entry : mapControlliCompletezza.entrySet()) {
			if (entry.getValue().getEsito() == -3) {
				//				se i controlli sono bloccanti si mette il fascicolo in stato IN_AGGIORNAMENTO se proviene da CONTROLLI_IN_CORSO
				if (fascicoloLive.getStato().equals(FascicoloDto.StatoEnum.CONTROLLI_IN_CORSO)) {
					anagraficaPrivateClient.putFascicoloStatoControlliInAggiornamentoUsingPUT(cuaa);
				}
				return mapControlliCompletezza;
			}
		}
		//		arrivati fin qui i controlli sono tutti con esito positivo o non bloccante.
		//		Per cui il fascicolo va messo nello stato CONTROLLATO_OK se proviene da CONTROLLI_IN_CORSO
		if (fascicoloLive.getStato().equals(FascicoloDto.StatoEnum.CONTROLLI_IN_CORSO)) {
			anagraficaPrivateClient.putFascicoloStatoControllatoOkUsingPUT(cuaa);
		}
		return mapControlliCompletezza;
	}

	public void startControlloCompletezzaFascicolo(final String cuaa)
			throws NoSuchElementException, FascicoloInvalidConditionException {
		var fascicoloLive = anagraficaPrivateClient.getByCuaaUsingGET1(cuaa, 0);
		//		se lo stato è diverso da IN_AGGIORNAMENTO i controlli non possono essere avviati
		if (!fascicoloLive.getStato().equals(FascicoloDto.StatoEnum.IN_AGGIORNAMENTO)) {
			throw new FascicoloInvalidConditionException("IS_" + FascicoloDto.StatoEnum.IN_AGGIORNAMENTO.name());
		}
		//		impostazione stato fascicolo a CONTROLLI_IN_CORSO
		anagraficaPrivateClient.putFascicoloStatoControlliInCorsoUsingPUT(cuaa);
		//		avvio asincrono controlli
		anagraficaPrivateClient.startControlloCompletezzaFascicoloAsincronoUsingPUT(cuaa);
		zootecniaPrivateClient.startControlloCompletezzaFascicoloAsincrono(cuaa);
		territorioPrivateClient.startControlloCompletezzaFascicoloAsincrono(cuaa);
		dotazioneTecnicaPrivateClient.startControlloCompletezzaFascicoloAsincrono(cuaa);
	}

	/**
	 * Il nuovo servizio di controllo completezza verifica sulle tabelle di controllo completezza
	 * dei vari microservizi coinvolti.
	 * @param cuaa
	 * @throws FascicoloInvalidConditionException
	 */
	void checkControlliCompletezzaFascicolo(String cuaa) throws FascicoloInvalidConditionException {
		var fascicoloLive = anagraficaPrivateClient.getByCuaaUsingGET1(cuaa, 0);

		//		1. lancio eccezione se stato fascicolo = CONTROLLI_IN_CORSO
		if (fascicoloLive.getStato().equals(FascicoloDto.StatoEnum.CONTROLLI_IN_CORSO)) {
			throw new FascicoloInvalidConditionException(FascicoloDto.StatoEnum.CONTROLLI_IN_CORSO.name());
		}
		//		gli esiti sono:
		//		0  = ok,
		//		-1 = non bloccante,
		//		-2 = no-data,
		//		-3 = bloccante
		//		2. lancio eccezione se qualsiasi esito == -3 (bloccante) oppure esito == null (significa che il processo asincrono
		//		   non si è ancora concluso; in teoria in questo caso il fascicolo dovrebbe trovarsi in stato CONTROLLI_IN_CORSO)
		for (Map.Entry<String, EsitoControlloDto> entry : queryControlloCompletezzaFascicolo(cuaa).entrySet()) {
			if (entry.getValue().getEsito() == null || entry.getValue().getEsito() == -3) {
				throw new FascicoloInvalidConditionException(entry.getKey());
			}
		}
	}

	private Map<String, EsitoControlloDto> getControlloCompletezzaFascicoloSincrono(final String cuaa)
			throws NoSuchElementException {
		var mapControlliCompletezza = new HashMap<String, EsitoControlloDto>();
		var retAnagraficaList = anagraficaPrivateClient.controlloCompletezzaFascicoloSincronoUsingGET(cuaa);
		if (retAnagraficaList != null) {
			mapControlliCompletezza.putAll(retAnagraficaList);
		} else {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}
		var retZootecniaList = zootecniaPrivateClient.controlloCompletezzaFascicoloSincronoUsingGET(cuaa);
		if (retZootecniaList != null) {
			mapControlliCompletezza.putAll(retZootecniaList);
		}
		var retTerritorioList = territorioPrivateClient.controlloCompletezzaFascicoloSincronoUsingGET(cuaa);
		if (retTerritorioList != null) {
			mapControlliCompletezza.putAll(retTerritorioList);
		}
		return mapControlliCompletezza;
	}

	void checkControlliCompletezzaFascicoloSincrono(String cuaa) throws FascicoloInvalidConditionException {
		for (Map.Entry<String, EsitoControlloDto> entry : getControlloCompletezzaFascicoloSincrono(cuaa).entrySet()) {
			if (entry.getValue().getEsito() == null || entry.getValue().getEsito() == -3) {
				throw new FascicoloInvalidConditionException(entry.getKey());
			}
		}
	}

	public void rimozioneControlliCompletezza(String cuaa) {
		zootecniaPrivateClient.rimozioneControlliCompletezza(cuaa);
		anagraficaPrivateClient.rimozioneControlliCompletezza(cuaa);
		territorioPrivateClient.rimozioneControlliCompletezza(cuaa);
		dotazioneTecnicaPrivateClient.rimozioneControlliCompletezza(cuaa);
	}
}
