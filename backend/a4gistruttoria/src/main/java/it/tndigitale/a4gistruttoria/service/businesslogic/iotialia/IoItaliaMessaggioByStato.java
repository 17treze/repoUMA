package it.tndigitale.a4gistruttoria.service.businesslogic.iotialia;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.CaricaAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.client.model.PersonaFisicaConCaricaDto;
import it.tndigitale.a4g.proxy.client.model.ComunicationDto;
import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.dto.IoItaliaMessage;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi4Anagrafica;

public abstract class IoItaliaMessaggioByStato  {

	public static final String PREFISSO_NOME_QUALIFICATORE = "IO_ITALIA_MESSAGE";
	private static Logger logger = LoggerFactory.getLogger(IoItaliaMessaggioByStato.class);

	@Autowired
	protected IstruttoriaComponent istruttoriaComponent;
	@Autowired
	private IoItaliaProperties	ioItaliaProperties;
	@Autowired
	private IoItaliaConsumerApi ioItaliaSenderService;
	@Autowired
	private ConsumeExternalRestApi4Anagrafica apiAnagrafica;

	public void inviaMessaggio(Long idIstruttoria) {
		IstruttoriaModel istruttoriaModel = istruttoriaComponent.load(idIstruttoria);
		IoItaliaMessage ioItaliaMessage = ioItaliaProperties.getProperties(getPropPrefix());

		try {
			// blocca i messaggi che non superano una validazione
			if (!checkInvio(istruttoriaModel).booleanValue()) {
				logger.info("Il messaggio IoItalia per l'istruttoria {} non è invitato perchè non ha superato la validazione" , idIstruttoria );
				return;
			}
		} catch (Exception e1) {
			logger.info("Il messaggio IoItalia per l'istruttoria {} non è invitato perchè non ha superato la validazione" , idIstruttoria , e1);
			return;
		}

		buildMessage(ioItaliaMessage,istruttoriaModel);
		ComunicationDto comunication=new ComunicationDto();
		BeanUtils.copyProperties(ioItaliaMessage, comunication);
		//BR10
		//La comunicazione va inviata a tutti i soggetti registrati con ‘ruolo esteso’ di Titolare/Rappresentante Legale nel fascicolo aziendale 
		List<CaricaAgsDto> soggettiFascicoloAziendale = apiAnagrafica.getSoggettiFascicoloAziendale(istruttoriaModel.getDomandaUnicaModel().getCuaaIntestatario());
		if (!CollectionUtils.isEmpty(soggettiFascicoloAziendale)) {
			soggettiFascicoloAziendale.stream()
			.map(CaricaAgsDto::getCodiceFiscale)
			.distinct()
			.forEach(cf -> {
				try {
					comunication.setCodiceFiscale(cf);
					ioItaliaSenderService.inviaMessaggio(comunication);
				} catch (Exception e) {
					logger.error("Non e' possibile inviare messaggio IoItalia a {}" , comunication.getCodiceFiscale() , e);
				}
			});
		}

	}

	protected abstract Boolean checkInvio(IstruttoriaModel istruttoriaModel);

	protected abstract void buildMessage(IoItaliaMessage ioItaliaMessage, IstruttoriaModel istruttoriaModel);


	protected abstract String getPropPrefix();

	public static String getNomeQualificatore(StatoIstruttoria statoIstruttoria) {
		return PREFISSO_NOME_QUALIFICATORE + statoIstruttoria.name();
	}
}
