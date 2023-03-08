package it.tndigitale.a4gistruttoria.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.dto.EsitoControlliLatte;
import it.tndigitale.a4gistruttoria.dto.InformazioniAllevamento;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapiAziendaPerInterventoFilter;
import it.tndigitale.a4gistruttoria.repository.dao.CapoTrackingDao;
import it.tndigitale.a4gistruttoria.repository.dao.EsitoCalcoloCapoDao;
import it.tndigitale.a4gistruttoria.repository.dao.AllevamentoImpegnatoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtCapoTracking;
import it.tndigitale.a4gistruttoria.repository.model.AllevamentoImpegnatoModel;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.repository.model.InterventoZootecnico;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class FinalizzaControlliLatteConsumer  implements Consumer<EsitoControlliLatte>{
	private static Logger log = LoggerFactory.getLogger(FinalizzaControlliLatteConsumer.class);

	@Autowired
	private CallElencoCapiAction callElencoCapiAction;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private EsitoCalcoloCapoDao esitoCalcoloCapoDao;
	@Autowired
	private AllevamentoImpegnatoDao allevamentoImpegnatoDao;
	@Autowired
	private CapoTrackingDao capoTrackingDao;
	
	@Override
	public void accept(EsitoControlliLatte esitoControlliLatte) {
		try {
			InformazioniAllevamento informazioniAllevamento = objectMapper.readValue(esitoControlliLatte.getRichiestaAllevamDu().getDatiAllevamento(), InformazioniAllevamento.class);
			//String annoCampagna = esitoControlliLatte.getRichiestaAllevamDu().getCampagna().toString();
			//String params = "{".concat(MessageFormat.format("\"cuaa\":\"{0}\",\"annoCampagna\":{1},\"codiceIntervento\":{2},\"idAlleBDN\":{3}", esitoControlliLatte.getRichiestaAllevamDu().getCuaaIntestatario(),
			//		annoCampagna, esitoControlliLatte.getRichiestaAllevamDu().getCodiceIntervento(),informazioniAllevamento.getCodiceAllevamentoBdn())).concat("}");
			CapiAziendaPerInterventoFilter filter=new CapiAziendaPerInterventoFilter();
			filter.setCuaa(esitoControlliLatte.getRichiestaAllevamDu().getCuaaIntestatario());
			filter.setCampagna(esitoControlliLatte.getRichiestaAllevamDu().getCampagna());
			filter.setIntervento((InterventoZootecnico) esitoControlliLatte.getRichiestaAllevamDu().getIntervento().getInterventoType());
			filter.setCuaaSubentrante(esitoControlliLatte.getRichiestaAllevamDu().getCuaaSubentrante());
			filter.setIdAllevamento(Integer.valueOf(informazioniAllevamento.getCodiceAllevamentoBdn()));
			JsonNode jsonResponseElencoCapi=callElencoCapiAction.getElencoVaccheLatte(filter);
			Map<String, List<JsonNode>> listaVacche= StreamSupport.stream(jsonResponseElencoCapi.path("clsCapoVacca").spliterator(), false)
				.filter(clsCapoVacca -> informazioniAllevamento.getCodiceAllevamento().equals(clsCapoVacca.path("aziendaCodice").textValue()))
				.collect(Collectors.groupingBy(clsCapoVacca ->  clsCapoVacca.path("codice").textValue()));
			AllevamentoImpegnatoModel richiestaAllevamento = allevamentoImpegnatoDao.findById(esitoControlliLatte.getRichiestaAllevamDu().getId()).orElseThrow(() -> new EntityNotFoundException(esitoControlliLatte.getRichiestaAllevamDu().getId().toString()));
			listaVacche.forEach((k,clsCapiVacca) -> {
				EsitoCalcoloCapoModel esitoCalcoloCapo = new EsitoCalcoloCapoModel();
				esitoCalcoloCapo.setEsito(EsitoCalcoloCapo.NON_AMMISSIBILE);
				esitoCalcoloCapo.setMessaggio(esitoControlliLatte.getMessaggio());
				esitoCalcoloCapo.setAllevamentoImpegnato(richiestaAllevamento);
				richiestaAllevamento.addEsitoCalcoloCapo(esitoCalcoloCapo);
				esitoCalcoloCapo.setCapoId(clsCapiVacca.get(0).path("capoId").longValue());
				esitoCalcoloCapo.setCodiceCapo(clsCapiVacca.get(0).path("codice").textValue());
				esitoCalcoloCapoDao.save(esitoCalcoloCapo);
				clsCapiVacca.forEach(capoVacca -> {
					A4gtCapoTracking capo=new A4gtCapoTracking();
					capo.setEsitoCalcoloCapo(esitoCalcoloCapo);
					capo.setDati(capoVacca.toString());
					capo.setDtUltimoAggiornamento(Calendar.getInstance().getTime());
					capoTrackingDao.save(capo);
				});
			});
		} catch (IOException e) {
			log.error("Errore generico calcolo sostengo zootecnico per allevamento ".concat(esitoControlliLatte.getRichiestaAllevamDu().getId().toString()), e);
			throw new RuntimeException("Errore generico calcolo sostengo zootecnico per allevamento ".concat(esitoControlliLatte.getRichiestaAllevamDu().getId().toString()),e);
		}
		
	}

}
