package it.tndigitale.a4gistruttoria.action.acz;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.proxy.client.model.ConsistenzaUbaOviniDto;
import it.tndigitale.a4gistruttoria.dto.InformazioniAllevamento;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.AllevamentoImpegnatoDao;
import it.tndigitale.a4gistruttoria.repository.model.AllevamentoImpegnatoModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi4Proxy;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class InitVariabileOviniAdultiConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	private static final Logger logger = LoggerFactory.getLogger(InitVariabileOviniAdultiConsumer.class);

	@Autowired
	private ConsumeExternalRestApi4Proxy consumeExternalRestApi4Proxy;
	@Autowired
	private AllevamentoImpegnatoDao allevamentoImpegnatoDao;
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		var domanda = istruttoria.getDomandaUnicaModel();
		BigDecimal oviniAdulti = calcoloNumeroOviniAdultiIntevento320(domanda.getCuaaIntestatario(), domanda.getCampagna(), domanda.getId());
		handler.getVariabiliInput().add(new VariabileCalcolo(TipoVariabile.ACZOVIADULTI_320, oviniAdulti));
		logger.debug("Numero Ovini Adulti recuperati da bdn ACZOVIADULTI_320 * 0.03: {}", oviniAdulti);
	}

	private BigDecimal calcoloNumeroOviniAdultiIntevento320(String cuaa, Integer campagna, Long idDomanda) {
		var intervento = "320";
		List<AllevamentoImpegnatoModel> allevamentiImpegnatiOvini = allevamentoImpegnatoDao.findByDomandaUnica_idAndIntervento_codiceAgea(idDomanda, intervento);
		List<String> idsAllevamentiOvini = new ArrayList<>();
		allevamentiImpegnatiOvini.stream().forEach(allevamento -> {
			try {
				InformazioniAllevamento readValue = objectMapper.readValue(allevamento.getDatiAllevamento(), InformazioniAllevamento.class);
				idsAllevamentiOvini.add(readValue.getCodiceAllevamentoBdn());
			} catch (IOException e) {
				logger.error("Impossibile deserializzare DatiAllevamento per allevamento {}", allevamento.getId());
			}
		});
		List<ConsistenzaUbaOviniDto> consistenzeUbaOviniDto = consumeExternalRestApi4Proxy.getConsistenzaUbaOvini(cuaa, campagna);
		consistenzeUbaOviniDto = !CollectionUtils.isEmpty(consistenzeUbaOviniDto) ? consistenzeUbaOviniDto : new ArrayList<>();
		List<ConsistenzaUbaOviniDto> consistenze124 = consistenzeUbaOviniDto.stream()
				.filter(ovino -> ovino.getCodiceSpecie().trim().equalsIgnoreCase("0124"))
				.filter(ovino -> idsAllevamentiOvini.contains(String.valueOf(ovino.getIdAllevamentoBdn())))
				.collect(Collectors.toList());
		BigDecimal k = BigDecimal.valueOf(1.33);
		return consistenze124.stream().map(consistenza -> {
			BigDecimal m = BigDecimal.valueOf(consistenza.getOviniMaschi().doubleValue());
			BigDecimal f = BigDecimal.valueOf(consistenza.getOviniFemmine().doubleValue());
			BigDecimal sum = m.add(f);
			return sum.multiply(k);
		}).reduce((a,b) -> a.add(b)).orElse(BigDecimal.ZERO);
	}
}
