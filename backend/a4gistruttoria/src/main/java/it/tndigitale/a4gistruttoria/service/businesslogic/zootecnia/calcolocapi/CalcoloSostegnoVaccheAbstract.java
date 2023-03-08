package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;

import com.fasterxml.jackson.databind.JsonNode;

import it.tndigitale.a4gistruttoria.dto.InformazioniAllevamento;
import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDu;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoBdnWrapper;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoLatteDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneBuilder;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.VitelloBuilder;
import it.tndigitale.a4gistruttoria.dto.zootecnia.VitelloDto;
import it.tndigitale.a4gistruttoria.repository.dao.AllevMontagnaDao;
import it.tndigitale.a4gistruttoria.repository.dao.AllevamentoImpegnatoDao;
import it.tndigitale.a4gistruttoria.repository.dao.CapoTrackingDao;
import it.tndigitale.a4gistruttoria.repository.dao.EsitoCalcoloCapoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtAllevMontagna;
import it.tndigitale.a4gistruttoria.repository.model.A4gtCapoTracking;
import it.tndigitale.a4gistruttoria.repository.model.AllevamentoImpegnatoModel;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.util.MapperWrapper;

public abstract class CalcoloSostegnoVaccheAbstract {

	private static Logger logger = LoggerFactory.getLogger(CalcoloSostegnoVaccheAbstract.class);


	@Value("${zootecnia.interventi.vacchenutricielatte}")
	private String[] interventiCodiciVaccheLatte;
	@Value("${zootecnia.interventi.vacchemacello}")
	private String[] interventiVacchemacello;
	@Value("${zootecnia.interventi.ovicaprini}")
	private String[] interventiOvicaprini;
	@Value("${zootecnia.interventi.vacchemontagna}")
	private String[] interventiCodiciVaccheLatteMontagna;

	@Autowired
	private AllevMontagnaDao alevMotagnaDao;

	@Value("${zootecnia.interventi.vacchemontagna.province}")
	private String[] provinceConsentite;

	@Autowired
	private MapperWrapper mapperWrapper;

	@Autowired
	private EsitoCalcoloCapoDao esitoCalcoloCapoDao;
	@Autowired
	private AllevamentoImpegnatoDao allevamentoImpegnatoDao;
	@Autowired
	private CapoTrackingDao capoTrackingDao;

	public InformazioniAllevamento recuperaInfoAllevamento(String datiAllevamento) {
		return mapperWrapper.readValue(datiAllevamento, InformazioniAllevamento.class);
	}

	public List<CapoBdnWrapper> creaDto(Map<String, List<JsonNode>> listaVacche){
		List<CapoBdnWrapper> listaCapi =  new ArrayList<>();
		listaVacche.forEach((k,v) -> {
			CapoBdnWrapper capoBdnWrapper = new CapoBdnWrapper();
			//creazione capo secondo il modello bdn
			capoBdnWrapper.setCapo(buildCapo(v));
			capoBdnWrapper.setListaVacche(v);
			listaCapi.add(capoBdnWrapper);
		});
		return listaCapi;
	}

	public abstract CapoDto buildCapo(List<JsonNode> listaDetenzioni);

	public Consumer<CapoBdnWrapper> eseguiControlliSuiCapi(RichiestaAllevamDu richiestaAllevamDu){
		return ((CapoBdnWrapper capoBdnWrapper) -> {
			EsitoCalcoloCapoModel esitoCalcoloCapo = getComponentControlli().apply(richiestaAllevamDu, capoBdnWrapper.getCapo());
			logger.debug("CAPO {} - Esito {} - Messaggio {}" , capoBdnWrapper.getCapo().getMarcaAuricolare(), esitoCalcoloCapo.getEsito() , esitoCalcoloCapo.getMessaggio());
			if (!EsitoCalcoloCapo.DA_SCARTARE.equals(esitoCalcoloCapo.getEsito())) {
				AllevamentoImpegnatoModel richiestaAllevamento = allevamentoImpegnatoDao.findById(richiestaAllevamDu.getId()).orElseThrow(() -> new EntityNotFoundException(richiestaAllevamDu.getId().toString()));
				esitoCalcoloCapo.setAllevamentoImpegnato(richiestaAllevamento);
				richiestaAllevamento.addEsitoCalcoloCapo(esitoCalcoloCapo);
				esitoCalcoloCapoDao.save(esitoCalcoloCapo);
				capoBdnWrapper.getListaVacche().forEach(capoVacca -> {
					A4gtCapoTracking capo=new A4gtCapoTracking();
					capo.setEsitoCalcoloCapo(esitoCalcoloCapo);
					capo.setDati(capoVacca.toString());
					capo.setDtUltimoAggiornamento(Calendar.getInstance().getTime());
					capoTrackingDao.save(capo);
				});
			}
		});
	}

	public abstract BiFunction<RichiestaAllevamDu,CapoDto,EsitoCalcoloCapoModel> getComponentControlli() ;


	//E' implementato in abstract perchè vale sia per vaccheLatte che per vaccheLatte In montagna
	public CapoLatteDto buildCapoBdnVaccheLatte(List<JsonNode> listaDetenzioni) {

		// raggruppa lista per codiceVitello. Costruisce l'oggetto per ogni chiave basandosi su un qualsiasi valore
		List<VitelloDto> vitelli = listaDetenzioni.stream()
				.collect(Collectors.groupingBy((JsonNode capoNode) -> capoNode.get("codiceVitello").asText()))
				.entrySet()
				.stream()
				.map(entry -> new VitelloBuilder().from(entry.getValue().get(0)))
				.collect(Collectors.toList());

		// raggruppa per data inizio detenzione per eliminare le detenzioni duplicate.
		List<DetenzioneDto> detenzioni = listaDetenzioni.stream()
				//Bruno C.: 08/06/2022 esclude le detenzioni che iniziano e si concludono nella stessa giornata
				.filter(capoNode -> !capoNode.get("dtInizioDetenzione").equals(capoNode.get("dtFineDetenzione")))
				.collect(Collectors.groupingBy((JsonNode capoNode) -> capoNode.get("dtInizioDetenzione")))
				.entrySet()
				.stream()
				.map(entry -> new DetenzioneBuilder().fromLatte(entry.getValue().get(0),valutaAllevamentoDiMontagna(entry.getValue().get(0).get("aziendaCodice").asText())))
				.collect(Collectors.toList());

		// si presuppone ogni capo in input abbia questi dati in comune
		JsonNode infoCapo = listaDetenzioni.get(0);

		return new CapoLatteDto(infoCapo)
				.setVitelli(vitelli)
				.setDetenzioni(detenzioni);

	}	

	private Boolean valutaAllevamentoDiMontagna(String codiceAllevamento) {
		Boolean allevamentoDiMontagna=Boolean.TRUE;
		if (!Arrays.asList(provinceConsentite).contains(codiceAllevamento.substring(3, 5))) {
			A4gtAllevMontagna a4gtAlevMotagna=new A4gtAllevMontagna();
			a4gtAlevMotagna.setCodiceAllevamento(codiceAllevamento);
			Optional<A4gtAllevMontagna> allevamentoMotagna = alevMotagnaDao.findOne(Example.of(a4gtAlevMotagna));
			if (!(allevamentoMotagna.isPresent() && allevamentoMotagna.get().getFlagMotagna())) {
				allevamentoDiMontagna=Boolean.FALSE;
			}
		}
		return allevamentoDiMontagna;
	}
}
