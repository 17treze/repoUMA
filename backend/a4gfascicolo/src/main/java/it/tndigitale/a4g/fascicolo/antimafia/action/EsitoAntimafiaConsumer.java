package it.tndigitale.a4g.fascicolo.antimafia.action;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.fascicolo.antimafia.dto.Dichiarazione;
import it.tndigitale.a4g.fascicolo.antimafia.repository.dao.DichiarazioneAntimafiaDao;
import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gtDichiarazioneAntimafia;
import it.tndigitale.a4g.fascicolo.antimafia.service.ext.ConsumeExternalRestApi4Proxy;
import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4g.proxy.client.model.AntimafiaEsitoDto;

@Component
public class EsitoAntimafiaConsumer implements Consumer<Dichiarazione> {

	private static Logger log = LoggerFactory.getLogger(EsitoAntimafiaConsumer.class);

	@Autowired
	private ConsumeExternalRestApi4Proxy extProxy;

	@Autowired
	private DichiarazioneAntimafiaDao daoDichiarazioneAntimafia;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void accept(Dichiarazione dichiarazione) {
		
		AntimafiaEsitoDto esitoDto = extProxy.getEsitoAntimafia(dichiarazione.getAzienda().getCuaa());
		//Se la data elaborazione esito è successiva a quella di protocollazione della domanda
		//Allora il sistema visualizza la descrizione dell’esito della trasmissione
		if (esitoDto != null 
				&& esitoDto.getDtElaborazione() != null 
				&& dichiarazione.getDtProtocollazione() != null 
				&& dichiarazione.getDtProtocollazione().before(Date.from(esitoDto.getDtElaborazione().atZone(ZoneId.systemDefault()).toInstant()))) {
			log.debug("aggiornamento esito antimafia per domanda con CUAA {}",dichiarazione.getAzienda().getCuaa());
			
			//aggiorno l'esito solo se le date di elaborazioni sono diverse oppure la data elaborazione della dichiarazione è null(quindi mai valorizzata)
			if (
					dichiarazione.getEsitoDtElaborazione()==null || 
					!esitoDto.getDtElaborazione().isEqual(dichiarazione.getEsitoDtElaborazione())
				) {
				A4gtDichiarazioneAntimafia dichAntimafia = daoDichiarazioneAntimafia.getOne(dichiarazione.getId());
				//aggiorno esito
				dichAntimafia.setEsito(esitoDto.getEsitoTrasmissione());
				dichAntimafia.setEsitoDtElaborazione(esitoDto.getDtElaborazione());
				dichAntimafia.setEsitoDescrizione(esitoDto.getDescrizioneEsito());
				dichAntimafia.setEsitoInvioAgea(esitoDto.getEsitoInvioAgea());
				dichAntimafia.setEsitoInvioBdna(esitoDto.getEsitoInvioBdna());
				daoDichiarazioneAntimafia.save(dichAntimafia);
			}
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void process(List<Dichiarazione> dichiarazioni) {
		List<String> cuaaList = new ArrayList<String>();
		dichiarazioni.forEach(dichiarazione -> {
			cuaaList.add(dichiarazione.getAzienda().getCuaa());
		});
		
		List<AntimafiaEsitoDto> esitoDtoList = extProxy.getEsitiAntimafia(cuaaList);
		if(esitoDtoList != null) {
			esitoDtoList.forEach(esitoDto -> {
				if (esitoDto != null 
						&& esitoDto.getDtElaborazione() != null) {
	//					trovare la dichiarazione associata
						dichiarazioni.forEach(dichiarazione -> {
							if(dichiarazione.getAzienda().getCuaa().equals(esitoDto.getCuaa()) && esitoDto.getDtValidita() != null 
									&& LocalDateConverter.to(esitoDto.getDtValidita()).before(LocalDateConverter.to(esitoDto.getDtElaborazione()))) {
								//aggiorno l'esito solo se le date di elaborazioni sono diverse oppure la data elaborazione della dichiarazione è null(quindi mai valorizzata)
								if (
										dichiarazione.getEsitoDtElaborazione()==null || 
										!esitoDto.getDtElaborazione().isEqual(dichiarazione.getEsitoDtElaborazione())
									) {
									log.debug("aggiornamento esito antimafia per domanda {} con CUAA {}",dichiarazione.getId() , dichiarazione.getAzienda().getCuaa());
									A4gtDichiarazioneAntimafia dichAntimafia = daoDichiarazioneAntimafia.getOne(dichiarazione.getId());
									//aggiorno esito
									dichAntimafia.setEsito(esitoDto.getEsitoTrasmissione());
									dichAntimafia.setEsitoDtElaborazione(esitoDto.getDtElaborazione());
									dichAntimafia.setEsitoDescrizione(esitoDto.getDescrizioneEsito());
									dichAntimafia.setEsitoInvioAgea(esitoDto.getEsitoInvioAgea());
									dichAntimafia.setEsitoInvioBdna(esitoDto.getEsitoInvioBdna());
									daoDichiarazioneAntimafia.save(dichAntimafia);
								}
							}
						});				
				}
			});
		}
	}
}
