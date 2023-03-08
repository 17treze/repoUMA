package it.tndigitale.a4g.fascicolo.anagrafica.ioitalia;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.proxy.client.model.ComunicationDto;


@Service
public class IoItaliaSenderService {
	@Autowired private IoItaliaConsumerApi ioItaliaConsumerApi;
	
	public void inviaNotificaSetStato(final IoItaliaMessage ioItaliaMessage) {
		ComunicationDto communication = new ComunicationDto();
		communication.setScadenza(LocalDate.now().plus(ioItaliaMessage.getPeriod()).atStartOfDay());
		communication.setCodiceFiscale(ioItaliaMessage.getCodiceFiscale());
		communication.setOggetto(ioItaliaMessage.getOggetto());
		communication.setMessaggio(ioItaliaMessage.getMessaggio());
		ioItaliaConsumerApi.inviaMessaggio(communication);
	}
}
