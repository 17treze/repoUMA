package it.tndigitale.a4g.proxy.dto.mapper;

import it.tndigitale.a4g.proxy.dto.persona.IndirizzoDto;
import it.tndigitale.ws.isvalidazioneanagrafe.ISDittaIndividualePro2P;
import it.tndigitale.ws.isvalidazioneanagrafe.ISLuogoPro2P;
import it.tndigitale.ws.isvalidazioneanagrafe.ISUbicazionePro2P;
import it.tndigitale.ws.wssanagraficaimprese.INDIRIZZO;

import javax.xml.bind.JAXBElement;
import java.util.Optional;

public class IndirizzoMapper {

	private IndirizzoMapper() {}

	public static IndirizzoDto from(ISUbicazionePro2P ubicazione) {
		IndirizzoDto indirizzo = new IndirizzoDto();
		indirizzo.setCap(Optional.ofNullable(ubicazione).map(ISUbicazionePro2P::getCap).map(JAXBElement::getValue).orElse(""));
		indirizzo.setCodiceIstat(Optional.ofNullable(ubicazione).map(ISUbicazionePro2P::getCodiceCatastale).map(JAXBElement::getValue).orElse(null));
		indirizzo.setComune(Optional.ofNullable(ubicazione).map(ISUbicazionePro2P::getComune).map(JAXBElement::getValue).orElse(null));
		indirizzo.setProvincia(Optional.ofNullable(ubicazione).map(ISUbicazionePro2P::getProvincia).map(JAXBElement::getValue).orElse(null));
		// Non effettuo il set di toponimo, via, civico e frazione poiché AT restituisce queste informazioni in un unico campo (toponimo) che salvo in denominazioneEstesa
		indirizzo.setDenominazioneEstesa(Optional.ofNullable(ubicazione).map(ISUbicazionePro2P::getToponimo).map(JAXBElement::getValue).orElse(null));
		return indirizzo;
	}
	
	public static IndirizzoDto from(ISLuogoPro2P indirizzoAgenziaEntrate) {
		ISUbicazionePro2P ubicazione = Optional.ofNullable(indirizzoAgenziaEntrate)
				.map(ISLuogoPro2P::getUbicazione).map(JAXBElement::getValue).orElse(null);
		
		IndirizzoDto indirizzo = new IndirizzoDto();
		indirizzo.setCap(Optional.ofNullable(ubicazione).map(ISUbicazionePro2P::getCap).map(JAXBElement::getValue).orElse(null));
		indirizzo.setCodiceIstat(Optional.ofNullable(ubicazione).map(ISUbicazionePro2P::getCodiceCatastale).map(JAXBElement::getValue).orElse(null));
		indirizzo.setComune(Optional.ofNullable(ubicazione).map(ISUbicazionePro2P::getComune).map(JAXBElement::getValue).orElse(null));
		indirizzo.setProvincia(Optional.ofNullable(ubicazione).map(ISUbicazionePro2P::getProvincia).map(JAXBElement::getValue).orElse(null));
		// Non effettuo il set di toponimo, via, civico e frazione poiché AT restituisce queste informazioni in un unico campo (toponimo) che salvo in denominazioneEstesa
		indirizzo.setDenominazioneEstesa(Optional.ofNullable(ubicazione).map(ISUbicazionePro2P::getToponimo).map(JAXBElement::getValue).orElse(null));
		return indirizzo;
	}
	
	public static IndirizzoDto from(INDIRIZZO indirizzo) {
		IndirizzoDto indirizzoDto = new IndirizzoDto();
		indirizzoDto
				.setFrazione(indirizzo.getFRAZIONE())
				.setCap(indirizzo.getCAP())
				.setVia(indirizzo.getVIA())
				.setProvincia(indirizzo.getPROVINCIA())
				.setToponimo(indirizzo.getTOPONIMO())
				.setComune(indirizzo.getCOMUNE())
				.setCodiceIstat(indirizzo.getCCOMUNE())
				.setCivico(indirizzo.getNCIVICO());

		// Lorenzo Martinelli: cambio strategia. Per ora lo commento: mostro i dati che ricevo da PARIX SENZA adattamenti
		// Effettuo il set di toponimo, via, civico e frazione poiché PARIX non restituisce queste informazioni in un unico campo
		/* String frazioneComune;
		if (indirizzo.getFRAZIONE() != null && !indirizzo.getFRAZIONE().isEmpty()) {
			frazioneComune = String.format("%s, %s", indirizzo.getFRAZIONE(), indirizzo.getCOMUNE());
		} else {
			frazioneComune = indirizzo.getCOMUNE();
		}
		String denominazioneEstesa =
				String.format("%s %s %s, %s, %s %s",
						indirizzo.getTOPONIMO(), indirizzo.getVIA(), indirizzo.getNCIVICO(),
						frazioneComune, indirizzo.getCAP(), indirizzo.getPROVINCIA());
		indirizzoDto.setDenominazioneEstesa(denominazioneEstesa); */
		return indirizzoDto;
	}
}
