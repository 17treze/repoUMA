package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import java.io.IOException;
import java.time.Instant;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.action.CallElencoCapiAction;
import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDu;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapiAziendaPerInterventoFilter;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoBdnWrapper;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoMacellatoDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneBuilder;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneDto;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.repository.model.InterventoZootecnico;

@Component
public class CalcoloSostegnoVaccheMacelloConsumer extends CalcoloSostegnoVaccheAbstract implements Consumer<RichiestaAllevamDu> {

	private static Logger logger = LoggerFactory.getLogger(CalcoloSostegnoVaccheMacelloConsumer.class);

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ControlloCapoMacellato controlloCapoMacellato;
	@Autowired
	private DomandaUnicaDao domandaUnicaDao;
	@Autowired
	private CallElencoCapiAction callElencoCapiAction;

	public void accept(RichiestaAllevamDu richiestaAllevamDu) {
		try {
			CapiAziendaPerInterventoFilter filter=new CapiAziendaPerInterventoFilter();
			filter.setCuaa(richiestaAllevamDu.getCuaaIntestatario());
			filter.setCampagna(richiestaAllevamDu.getCampagna());
			filter.setIntervento((InterventoZootecnico) richiestaAllevamDu.getIntervento().getInterventoType());
			filter.setCuaaSubentrante(richiestaAllevamDu.getCuaaSubentrante());
			JsonNode jsonResponseElencoCapi=callElencoCapiAction.getElencoCapiMacellati(filter);

			Map<String, List<JsonNode>> listaVacche = 
					StreamSupport.stream(jsonResponseElencoCapi.path("clsCapoMacellato").spliterator(), false)
					.collect(Collectors.groupingBy(clsCapoVacca ->  clsCapoVacca.path("codice").textValue()));
			//La vacca deve essere detenuta per un periodo continuativo di almeno sei mesi (dodici per l’intervento 316) prima della macellazione in allevamenti impegnati in domanda.
			//filtro gli allevamenti in base a quelli della domanda
			DomandaUnicaModel domandaUnicaModel = domandaUnicaDao.getOne(richiestaAllevamDu.getIdDomanda());
			List<String> listaAllevamentiInDomanda = 
					domandaUnicaModel.getAllevamentiImpegnati().stream()
					.map(a4gtRichiestaAllevamDu -> {
						try {
							return objectMapper.readTree(a4gtRichiestaAllevamDu.getDatiAllevamento()).path("codiceAllevamento").textValue();
						} catch (IOException e) {
							logger.error("Errore conversione json InformazioniAllevamento", e);
						}
						return null;
					})
					.collect(Collectors.toList());
			List<CapoBdnWrapper> listaCapi = creaDto(listaVacche);
			listaCapi.stream()
			.forEach(capoBdnWrapper -> {
				//filtro gli allevamenti in base a quelli della domanda
				List<DetenzioneDto> detenzioniFiltered =
						capoBdnWrapper.getCapo().getDetenzioni().stream()
						.filter(detenzione -> listaAllevamentiInDomanda.contains(detenzione.getAziendaCodice()))
						.collect(Collectors.toList());
				capoBdnWrapper.getCapo().setDetenzioni(detenzioniFiltered);
				if (!detenzioniFiltered.isEmpty()) {
					eseguiControlliSuiCapi(richiestaAllevamDu).accept(capoBdnWrapper);
				}
			});
		} catch (Exception e) {
			logger.error("Errore generico calcolo sostengo zootecnico per allevamento ".concat(richiestaAllevamDu.getId().toString()), e);
			throw new RuntimeException("Errore generico calcolo sostengo zootecnico per allevamento ".concat(richiestaAllevamDu.getId().toString()),e);
		}
	}

	@Override
	public BiFunction<RichiestaAllevamDu, CapoDto, EsitoCalcoloCapoModel> getComponentControlli() {
		return controlloCapoMacellato;
	}

	@Override
	public CapoDto buildCapo(List<JsonNode> listaDetenzioni) {

		List<DetenzioneDto> detenzioni = listaDetenzioni.stream()
				//Bruno C.: 08/06/2022 esclude le detenzioni che iniziano e si concludono nella stessa giornata
				.filter(capoNode -> !capoNode.get("dtInizioDetenzione").equals(capoNode.get("dtFineDetenzione")))
				.collect(Collectors.groupingBy((JsonNode capoNode) -> capoNode.get("dtInizioDetenzione")))
				.entrySet()
				.stream()
				.map(entry -> new DetenzioneBuilder().fromMacello(entry.getValue().get(0)))
				.collect(Collectors.toList());

		JsonNode capoMacellatoNode = listaDetenzioni.get(0);

		return new CapoMacellatoDto(capoMacellatoNode)
				.setDataMacellazione(Instant.ofEpochMilli(capoMacellatoNode.path("dtMacellazione").longValue()).atZone(ZoneId.systemDefault()).toLocalDate())
				.setDataApplicazioneMarchio(capoMacellatoNode.path("dtApplicazioneMarchio").isMissingNode() ? null : Instant.ofEpochMilli(capoMacellatoNode.get("dtApplicazioneMarchio").longValue()).atZone(ZoneId.systemDefault()).toLocalDate())
				.setDetenzioni(detenzioni);
	}

}
