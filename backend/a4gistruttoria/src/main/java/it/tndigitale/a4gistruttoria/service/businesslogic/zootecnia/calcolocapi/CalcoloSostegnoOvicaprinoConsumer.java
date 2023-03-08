package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import it.tndigitale.a4gistruttoria.action.CallElencoCapiAction;
import it.tndigitale.a4gistruttoria.dto.InformazioniAllevamento;
import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDu;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapiAziendaPerInterventoFilter;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoBdnWrapper;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoOvicaprinoDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneBuilder;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneDto;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.repository.model.InterventoZootecnico;

@Component
public class CalcoloSostegnoOvicaprinoConsumer extends CalcoloSostegnoVaccheAbstract implements Consumer<RichiestaAllevamDu> {

	private static Logger log = LoggerFactory.getLogger(CalcoloSostegnoOvicaprinoConsumer.class);

	@Autowired
	private ControlloCapoOvicaprino controlloCapoOvicaprino;
	@Autowired
	private CallElencoCapiAction callElencoCapiAction;

	public void accept(RichiestaAllevamDu richiestaAllevamDu) {
		try {
			InformazioniAllevamento informazioniAllevamento = recuperaInfoAllevamento(richiestaAllevamDu.getDatiAllevamento());
			CapiAziendaPerInterventoFilter filter=new CapiAziendaPerInterventoFilter();
			filter.setCuaa(richiestaAllevamDu.getCuaaIntestatario());
			filter.setCampagna(richiestaAllevamDu.getCampagna());
			filter.setIntervento((InterventoZootecnico) richiestaAllevamDu.getIntervento().getInterventoType());
			filter.setCuaaSubentrante(richiestaAllevamDu.getCuaaSubentrante());
			filter.setIdAllevamento(Integer.valueOf(informazioniAllevamento.getCodiceAllevamentoBdn()));
			JsonNode jsonResponseElencoCapi=callElencoCapiAction.getElencoOviCaprini(filter);			
			Map<String, List<JsonNode>> listaVacche = 
					StreamSupport.stream(jsonResponseElencoCapi.path("clsCapoOvicaprino").spliterator(), false)
					.filter(clsCapoVacca -> informazioniAllevamento.getCodiceAllevamento().equals(clsCapoVacca.path("aziendaCodice").textValue()))	
					.collect(
							Collectors.groupingBy(clsCapoVacca ->  clsCapoVacca.path("codice").textValue())
							);
			List<CapoBdnWrapper> listaCapi = creaDto(listaVacche);
			listaCapi.stream()
			.forEach(eseguiControlliSuiCapi(richiestaAllevamDu));
		} catch (Exception e) {
			log.error("Errore generico calcolo sostengo zootecnico per allevamento ".concat(richiestaAllevamDu.getId().toString()), e);
			throw new RuntimeException("Errore generico calcolo sostengo zootecnico per allevamento ".concat(richiestaAllevamDu.getId().toString()),e);
		}
	}

	@Override
	public BiFunction<RichiestaAllevamDu, CapoDto, EsitoCalcoloCapoModel> getComponentControlli() {
		return controlloCapoOvicaprino;
	}

	@Override
	public CapoDto buildCapo(List<JsonNode> listaDetenzioni) {
		ZoneId systemDefault = ZoneId.systemDefault();
		List<DetenzioneDto> detenzioni = listaDetenzioni.stream()
				//Bruno C.: 08/06/2022 esclude le detenzioni che iniziano e si concludono nella stessa giornata
				.filter(capoNode -> !capoNode.get("dtInizioDetenzione").equals(capoNode.get("dtFineDetenzione")))
				.collect(Collectors.groupingBy((JsonNode capoNode) -> capoNode.get("dtInizioDetenzione")))
				.entrySet()
				.stream()
				.map(entry -> new DetenzioneBuilder().fromOvicaprino(entry.getValue().get(0)))
				.collect(Collectors.toList());

		JsonNode infoCapo  = listaDetenzioni.get(0);

		return new CapoOvicaprinoDto(infoCapo)
				.setDataApplicazioneMarchio(infoCapo.path("dtApplMarchio").isMissingNode() ? null : LocalDate.ofInstant(Instant.ofEpochMilli(infoCapo.get("dtApplMarchio").longValue()), systemDefault))
				.setDataInserimentoBdnNascita(infoCapo.path("dtInserimentoBdnNascita").isMissingNode() ? null : LocalDate.ofInstant(Instant.ofEpochMilli(infoCapo.get("dtInserimentoBdnNascita").longValue()), systemDefault))
				.setDetenzioni(detenzioni);
	}
}
