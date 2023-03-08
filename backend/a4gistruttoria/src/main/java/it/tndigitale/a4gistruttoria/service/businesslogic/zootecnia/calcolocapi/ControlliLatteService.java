package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.partitioningBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.action.ControlliLatteConsumer;
import it.tndigitale.a4gistruttoria.action.FinalizzaControlliLatteConsumer;
import it.tndigitale.a4gistruttoria.dto.EsitoControlliLatte;
import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDu;
import it.tndigitale.a4gistruttoria.util.EsitoControlliLatteEnum;

@Service
public class ControlliLatteService {
	
	@Value("${zootecnia.interventi.controlli.latte}")
	private String[] interventiControlliLatte;
	@Autowired
	private ControlliLatteConsumer controlliLatteConsumer;
	@Autowired
	private FinalizzaControlliLatteConsumer finalizzaControlliLatteConsumer;
	
	/**
	 * @param richiesteAllevamDu= lista di tutti gli allevamenti
	 * @return restituisce gli allevamenti su cui continuare gli altri controlli(1-che hanno superato i controlli latte 2-on appartenenti a interventi Controlli Latte) 
	 */
	public List<RichiestaAllevamDu> eseguiControlliLatte(List<RichiestaAllevamDu> richiesteAllevamDu){
		// esecuzione controlli latte
		// partizioni i dati per intervento
		Map<Boolean, List<RichiestaAllevamDu>> richiesteAllevamDuPartitioningByIntervento = 
				richiesteAllevamDu.stream()
				.collect(partitioningByInterventi);
		// eseguo i controlli analisi del latte per gli interventi Controlli Latte e li partizioni per esito
		Map<Boolean, List<EsitoControlliLatte>> richiesteAllevamDuPartitioningByEsito = 
				richiesteAllevamDuPartitioningByIntervento
					.get(true)//con questo recupero gli allevamenti relativi interventiControlliLatte
					.stream()
					.map(controlliLatteConsumer)
					.collect(partitioningByEsitoControlliLatte);
		// salvo i dati dell'esito nel DB nel caso gli esiti sono !OK (diversi da OK)
		richiesteAllevamDuPartitioningByEsito
			.get(true)
			.forEach(finalizzaControlliLatteConsumer);	
		// valorizzo gli allevamento da controllare
		List<RichiestaAllevamDu> allevamentiDaControllare = new ArrayList<>();
		// aggiungo gli allevamenti che hanno superato i controlli latte
		allevamentiDaControllare.addAll(richiesteAllevamDuPartitioningByEsito.get(false).stream().map(EsitoControlliLatte::getRichiestaAllevamDu).collect(Collectors.toList()));
		// aggiungo gli allevamenti partizionati non appartenenti a interventi Controlli Latte
		allevamentiDaControllare.addAll(richiesteAllevamDuPartitioningByIntervento.get(false));
		return allevamentiDaControllare;
	}

	Collector<RichiestaAllevamDu, ?, Map<Boolean, List<RichiestaAllevamDu>>> partitioningByInterventi = 
			partitioningBy(
					richiestaAllevamDu -> asList(interventiControlliLatte)
											.contains(richiestaAllevamDu.getCodiceIntervento())
			);
	
	Collector<EsitoControlliLatte, ?, Map<Boolean, List<EsitoControlliLatte>>> partitioningByEsitoControlliLatte = 
			partitioningBy(
					esitoControlliLatte -> !EsitoControlliLatteEnum.OK.equals(esitoControlliLatte.getEsito())
			);
}
