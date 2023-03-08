package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import static java.util.stream.Collectors.*;

import it.tndigitale.a4gistruttoria.repository.dao.EsitoCalcoloCapoDao;
import it.tndigitale.a4gistruttoria.repository.dao.ImportoUnitarioInterventoDao;
import it.tndigitale.a4gistruttoria.repository.model.AllevamentoImpegnatoModel;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapo;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.repository.model.ImportoUnitarioInterventoModel;

@Service
public class CalcoloCapiRichiesti {
	private static Logger log = LoggerFactory.getLogger(CalcoloCapiRichiesti.class);
	@Value("${zootecnia.domandaintegrativa.interventi.bovini}")
	private String[] interventiCodiciVaccheLatte;
	@Value("${zootecnia.domandaintegrativa.interventi.vacchemacello}")
	private String[] interventiCodiciVaccheMacello;
	@Value("${zootecnia.domandaintegrativainterventi.ovicaprini}")
	private String[] interventiCodiciOvini;
	@Autowired
	private ImportoUnitarioInterventoDao importoUnitarioInterventoDao;
	@Autowired
	private EsitoCalcoloCapoDao esitoCalcoloCapoDao;

	@Transactional(propagation = Propagation.MANDATORY)
	public void eseguiCalcolo(Set<AllevamentoImpegnatoModel> sostegniZootecnici,Integer campagna) {
		log.debug("calcoli per interventi VaccheLatte");
		eseguiCalcoloPerInterventi(sostegniZootecnici, campagna, interventiCodiciVaccheLatte);
		log.debug("calcoli per interventi VaccheMacello");
		eseguiCalcoloPerInterventi(sostegniZootecnici, campagna, interventiCodiciVaccheMacello);
		log.debug("calcoli per interventi Ovini");
		eseguiCalcoloPerInterventi(sostegniZootecnici, campagna, interventiCodiciOvini);
	}

	private void eseguiCalcoloPerInterventi(Set<AllevamentoImpegnatoModel> sostegniZootecnici, Integer campagna, String[] interventi) {
		//filtro gli allevementi per interventi
		//ed eseguo il raggruppamento per Codice Capo
		//in questo modo per ogni capo ho i relativi interventi a cui è associato
		Map<String, List<EsitoCalcoloCapoModel>> capiByCodice = sostegniZootecnici.stream()
			.filter(allevamento -> Arrays.asList(interventi).contains(allevamento.getIntervento().getCodiceAgea()))
			.flatMap(allevamento -> allevamento.getEsitiCalcoloCapi().stream()) // Flattening la lista degli esiti
			.filter(esito -> !EsitoCalcoloCapo.NON_AMMISSIBILE.equals(esito.getEsito())) //filtro tutto quelli non ammissibili
			.collect(groupingBy(EsitoCalcoloCapoModel::getCodiceCapo));
		//Regole di assegnazione interventi macellazione
		capiByCodice.entrySet().stream()
			.forEach(e -> {
				List<EsitoCalcoloCapoModel> capoInterventi = e.getValue();
				//l’intervento richiesto  in domanda integrativa è quello che ha priorità maggiore 
				//ordino per priorità e prendo il primo
				//se presente viene settato il richiesto a TRUE e in seguito salvato
				capoInterventi.stream()
					.sorted((EsitoCalcoloCapoModel a, EsitoCalcoloCapoModel b ) -> {
						ImportoUnitarioInterventoModel importoUnitarioA = importoUnitarioInterventoDao.findByCampagnaAndIntervento_identificativoIntervento(
								campagna, 
								a.getAllevamentoImpegnato().getIntervento().getIdentificativoIntervento());
						ImportoUnitarioInterventoModel importoUnitarioB = importoUnitarioInterventoDao.findByCampagnaAndIntervento_identificativoIntervento(
								campagna, 
								b.getAllevamentoImpegnato().getIntervento().getIdentificativoIntervento());							
						return importoUnitarioA.getPriorita().compareTo(importoUnitarioB.getPriorita());
					})
					.findFirst()
					.map(capo -> {
						capo.setRichiesto(true);
						return capo;
					})
					.ifPresent(capo -> 
						esitoCalcoloCapoDao.save(capo)
					);
			});
	}

}
