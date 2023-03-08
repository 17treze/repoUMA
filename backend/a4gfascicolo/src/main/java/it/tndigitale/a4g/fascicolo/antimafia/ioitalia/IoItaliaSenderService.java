package it.tndigitale.a4g.fascicolo.antimafia.ioitalia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.CaricaAgsDto;
import it.tndigitale.a4g.fascicolo.antimafia.StatoDichiarazioneEnum;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Dichiarazione;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DichiarazionePaginataFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.IoItaliaMessage;
import it.tndigitale.a4g.fascicolo.antimafia.service.AntimafiaService;
import it.tndigitale.a4g.fascicolo.antimafia.service.ext.ConsumeExternalRestApi4Anagrafica;
import it.tndigitale.a4g.fascicolo.antimafia.utente.AbilitazioneUtenzaTecnica;
import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4g.proxy.client.model.ComunicationDto;


@Service
public class IoItaliaSenderService {

	private static Logger logger = LoggerFactory.getLogger(IoItaliaSenderService.class);
	private static final String SCADENZA_ANTIMAFIA="scadenza-antimafia";

	@Autowired
	private AntimafiaService serviceAntimafia;
	@Autowired
	private ConsumeExternalRestApi4Anagrafica apiAnagrafica;
	@Autowired
	private IoItaliaConsumerApi ioItaliaConsumerApi;
	@Autowired
	private IoItaliaProperties ioItaliaProperties;
	@Autowired
	private AbilitazioneUtenzaTecnica abilitazioneUtenzaTecnica;

	@Scheduled(cron = "${cron.expression.antimafia.scadenza}")
	public void jobReminderScadenzaDichiarazioniAntimafia() throws Exception {
		abilitazioneUtenzaTecnica.configuraUtenzaTecnica();
		DichiarazionePaginataFilter filter = new DichiarazionePaginataFilter();
		filter.setStatiDichiarazione(Arrays.asList(StatoDichiarazioneEnum.PROTOCOLLATA,StatoDichiarazioneEnum.CONTROLLATA));
		List<Dichiarazione> dichiarazioni = serviceAntimafia.getDichiarazioniPaginata(filter, null, null).getResults();
		dichiarazioni.stream()
		.forEach(dichiarazione -> inviaNotificaScadenzaDichiarazioneAntimafia(dichiarazione));
	} 


	private void inviaNotificaScadenzaDichiarazioneAntimafia(Dichiarazione dichiarazione) {
		try {
			LocalDate dtScadenza = LocalDateConverter.fromDate(dichiarazione.getDtProtocollazione()).plusDays(180);

			if(!isCorrectTiming(dtScadenza).booleanValue()) {
				return;
			}
			IoItaliaMessage ioItaliaMessage = ioItaliaProperties.getProperties(SCADENZA_ANTIMAFIA);
			buildMessage(ioItaliaMessage,dichiarazione);
			ComunicationDto comunication=new ComunicationDto();
			BeanUtils.copyProperties(ioItaliaMessage, comunication);
			// setta scandenza messaggio io italia
			comunication.setScadenza(dtScadenza.atStartOfDay());
			//BR10
			//La comunicazione va inviata a tutti i soggetti registrati con ‘ruolo esteso’ di Titolare/Rappresentante Legale nel fascicolo aziendale 
			List<CaricaAgsDto> soggettiFascicoloAziendale = apiAnagrafica.getSoggettiFascicoloAziendale(dichiarazione.getAzienda().getCuaa());
			if (!CollectionUtils.isEmpty(soggettiFascicoloAziendale)) {
				soggettiFascicoloAziendale.stream()
				.map(CaricaAgsDto::getCodiceFiscale)
				.distinct()
				.forEach(cf -> {
					try {
						comunication.setCodiceFiscale(cf);
						ioItaliaConsumerApi.inviaMessaggio(comunication);
					} catch (Exception e) {
						logger.error("Non e' possibile inviare messaggio IoItalia a {}" , comunication.getCodiceFiscale() , e);
					}
				});
				logger.info("Tentativo Invio messaggio ioitalia: Avviso scadenza Dichiarazione Antimafia -  destinatario -{} - dichiarazione id {}", comunication.getCodiceFiscale(), dichiarazione.getId());
			} else {
				logger.warn("ioItalia - Non sono presenti soggetto a cui inviare il messaggio dichiarazione antimafia id {}", dichiarazione.getId());
			}
		} catch (Exception e) {
			logger.error("Errore invio messaggio IoItalia Scadenza Dichiarazione Antimafia id {}", dichiarazione.getId(), e);
		}
	}

	private void buildMessage(IoItaliaMessage ioItaliaMessage, Dichiarazione dichiarazione) {
		LocalDate dtScandenza = LocalDateConverter.fromDate(dichiarazione.getDtProtocollazione()).plusDays(180);
		ioItaliaMessage.setMessaggio(
				String.format(ioItaliaMessage.getMessaggio(), 
						dtScandenza.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
						dichiarazione.getAzienda().getCuaa(),
						dichiarazione.getDenominazioneImpresa()
						));
	}

	// La notifica deve essere mandata 30 - 20 - 10 - 5 - 1 gg prima della scadenza della dichiarazione antimafia
	private Boolean isCorrectTiming(LocalDate dtScandenza) {
		LocalDate now = LocalDate.now();
		if (
				now.isEqual(dtScandenza.minusDays(30)) || 
				now.isEqual(dtScandenza.minusDays(20)) ||
				now.isEqual(dtScandenza.minusDays(10)) ||
				now.isEqual(dtScandenza.minusDays(5))  ||
				now.isEqual(dtScandenza.minusDays(1))) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
