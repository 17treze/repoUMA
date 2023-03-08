package it.tndigitale.a4gistruttoria.strategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.RichiestaAllevamDu;
import it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi.CalcoloSostegnoOvicaprinoConsumer;
import it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi.CalcoloSostegnoVaccheLatteConsumer;
import it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi.CalcoloSostegnoVaccheLatteMontagnaConsumer;
import it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi.CalcoloSostegnoVaccheMacelloConsumer;

@Component
public class CalcoloZootecniaStrategy {
	
	@Autowired
	private CalcoloSostegnoVaccheLatteMontagnaConsumer calcoloSostegnoVaccheLatteMontagnaConsumer;
	@Autowired
	private CalcoloSostegnoVaccheLatteConsumer calcoloSostegnoVaccheLatteConsumer;
	@Autowired
	private CalcoloSostegnoVaccheMacelloConsumer calcoloSostegnoVaccheMacelloConsumer;
	@Autowired
	private CalcoloSostegnoOvicaprinoConsumer calcoloSostegnoOvicaprinoConsumer;
	@Value("${zootecnia.interventi.vacchenutricielatte}")
	private String[] interventiCodiciVaccheLatte;
	@Value("${zootecnia.interventi.vacchemacello}")
	private String[] interventiVacchemacello;
	@Value("${zootecnia.interventi.ovicaprini}")
	private String[] interventiOvicaprini;
	@Value("${zootecnia.interventi.vacchemontagna}")
	private String[] interventiCodiciVaccheLatteMontagna;
	
	
	public void executeStrategy(RichiestaAllevamDu richiestaAllevamDu) {
	      final Map<Predicate<String>, Consumer<RichiestaAllevamDu>> calcoloSostegni = new HashMap<>();
	      calcoloSostegni.put(s -> Arrays.asList(interventiCodiciVaccheLatte).contains(s), calcoloSostegnoVaccheLatteConsumer);
	      calcoloSostegni.put(s -> Arrays.asList(interventiVacchemacello).contains(s), calcoloSostegnoVaccheMacelloConsumer);
	      calcoloSostegni.put(s -> Arrays.asList(interventiOvicaprini).contains(s), calcoloSostegnoOvicaprinoConsumer);
	      calcoloSostegni.put(s -> Arrays.asList(interventiCodiciVaccheLatteMontagna).contains(s), calcoloSostegnoVaccheLatteMontagnaConsumer);
	      
	      calcoloSostegni.entrySet().stream()
	      	.filter(it -> it.getKey().test(richiestaAllevamDu.getCodiceIntervento()))
	      	.map(Map.Entry::getValue)
	      	.forEach(cons -> cons.accept(richiestaAllevamDu));
	}
}
