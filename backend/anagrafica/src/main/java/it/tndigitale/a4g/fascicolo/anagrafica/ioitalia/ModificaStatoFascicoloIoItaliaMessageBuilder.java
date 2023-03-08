package it.tndigitale.a4g.fascicolo.anagrafica.ioitalia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;


@Component
public class ModificaStatoFascicoloIoItaliaMessageBuilder {
	private static final String SET_STATO_ALLA_FIRMA_AZIENDA = "set-stato-allafirmaazienda";
	private static final String SET_STATO_IN_AGGIORNAMENTO = "set-stato-in-aggiornamento";
	
	@Autowired private IoItaliaProperties ioItaliaProperties;

	public IoItaliaMessage buildMessage(
			final String codiceFiscale,
			final String denominazioneFacicolo,
			final StatoFascicoloEnum statoFascicolo) {
		String messageType = null;
		if (statoFascicolo.equals(StatoFascicoloEnum.IN_AGGIORNAMENTO)) {
			messageType = SET_STATO_IN_AGGIORNAMENTO;
		} else if (statoFascicolo.equals(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA)) {
			messageType = SET_STATO_ALLA_FIRMA_AZIENDA;
		}
		var ioItaliaMessage = ioItaliaProperties.getWithProperties(messageType);
		ioItaliaMessage.setOggetto(String.format(ioItaliaMessage.getOggetto(), denominazioneFacicolo));
		ioItaliaMessage.setCodiceFiscale(codiceFiscale);
		return ioItaliaMessage;
	}
}
