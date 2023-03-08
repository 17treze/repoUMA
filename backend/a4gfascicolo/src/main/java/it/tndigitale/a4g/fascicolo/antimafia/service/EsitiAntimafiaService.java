package it.tndigitale.a4g.fascicolo.antimafia.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.antimafia.StatoDichiarazioneEnum;
import it.tndigitale.a4g.fascicolo.antimafia.action.EsitoAntimafiaConsumer;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Dichiarazione;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DichiarazionePaginataFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.PageResultWrapper;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Pagination;
import it.tndigitale.a4g.fascicolo.antimafia.utente.AbilitazioneUtenzaTecnica;

@Service
public class EsitiAntimafiaService {

	private static Logger log = LoggerFactory.getLogger(EsitiAntimafiaService.class);

	@Autowired
	AntimafiaServiceImpl antimafiaService;
	@Autowired
	private EsitoAntimafiaConsumer esitoAntimafiaConsumer;
	@Autowired
	private AbilitazioneUtenzaTecnica abilitazioneUtenzaTecnica;
	
	@Scheduled(cron = "${cron.expression.antimafia.esiti}")
	public void jobRecuperoEsiti() throws Exception {
		log.info("INIZIO jobRecuperoEsiti Antimafia: {}", new Date());
		abilitazioneUtenzaTecnica.configuraUtenzaTecnica();
		List<StatoDichiarazioneEnum> filtroListaStati = 
				Arrays.asList(
						StatoDichiarazioneEnum.PROTOCOLLATA,
						StatoDichiarazioneEnum.CONTROLLATA,
						StatoDichiarazioneEnum.CONTROLLO_MANUALE,
						StatoDichiarazioneEnum.RIFIUTATA,
						StatoDichiarazioneEnum.POSITIVO,
						StatoDichiarazioneEnum.VERIFICA_PERIODICA
						);
		Pagination pagination=new Pagination("50", "0");
		DichiarazionePaginataFilter dichiarazioneInput=new DichiarazionePaginataFilter();
		dichiarazioneInput.setStatiDichiarazione(filtroListaStati);
		PageResultWrapper<Dichiarazione> dichiarazioniPaginata = antimafiaService.getDichiarazioniPaginata(dichiarazioneInput, pagination, null);
		//elaborazione dei dati di 50 in 50 utilizzando la paginazione
		while (!CollectionUtils.isEmpty(dichiarazioniPaginata.getResults())) {
			log.info("elaborazione pagina {}", pagination);
//			dichiarazioniPaginata.getResults().forEach(esitoAntimafiaConsumer);
			esitoAntimafiaConsumer.process(dichiarazioniPaginata.getResults());
			
			// Prendo gli esiti e li mappo nelle dichiarazioni
			int nextPage=Integer.valueOf(pagination.getPagSize()) + Integer.valueOf(pagination.getPagStart());
			pagination.setPagStart(String.valueOf(nextPage));
			dichiarazioniPaginata = antimafiaService.getDichiarazioniPaginata(dichiarazioneInput, pagination, null);
		}
		log.info("FINE jobRecuperoEsiti Antimafia: {}", new Date());
	} 
}
