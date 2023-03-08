package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

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
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoBdnWrapper;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.repository.model.InterventoZootecnico;

@Component
public class CalcoloSostegnoVaccheLatteMontagnaConsumer extends CalcoloSostegnoVaccheAbstract  implements Consumer<RichiestaAllevamDu> {

	private static Logger log = LoggerFactory.getLogger(CalcoloSostegnoVaccheLatteMontagnaConsumer.class);

	@Autowired
	private ControlloCapoVaccaLatteMontagna controlloCapoVaccaLatteMontagna;
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
			JsonNode jsonResponseElencoCapi=callElencoCapiAction.getElencoVaccheLatte(filter);

			Map<String, List<JsonNode>> listaVacche = 
					StreamSupport.stream(jsonResponseElencoCapi.path("clsCapoVacca").spliterator(), false)
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
		return controlloCapoVaccaLatteMontagna;
	}

	@Override
	public CapoDto buildCapo(List<JsonNode> listaDetenzioni) {
		return buildCapoBdnVaccheLatte(listaDetenzioni);
	}

}
