package it.tndigitale.a4g.proxy.dto.mapper;

import java.util.Optional;

import javax.xml.bind.JAXBElement;

import it.tndigitale.a4g.proxy.dto.persona.RappresentanteLegaleDto;
import it.tndigitale.ws.isvalidazioneanagrafe.ISRiferimentoPro2P;
import it.tndigitale.ws.wssanagraficaimprese.PERSONA;
import it.tndigitale.ws.wssanagraficaimprese.PERSONAFISICA;

public class RappresentanteLegaleMapper {

	private RappresentanteLegaleMapper() { }
	
	public static RappresentanteLegaleDto from (ISRiferimentoPro2P caricaAgenziaEntrate){
		ISRiferimentoPro2P carica = Optional.ofNullable(caricaAgenziaEntrate).orElse(null);

		RappresentanteLegaleDto rappresentante = new RappresentanteLegaleDto();

		rappresentante.setCodiceFiscale(Optional.ofNullable(carica).map(ISRiferimentoPro2P::getCodiceFiscale).map(JAXBElement::getValue).orElse(null));
		rappresentante.setNominativo(Optional.ofNullable(carica).map(ISRiferimentoPro2P::getDatiIdentificativi).map(JAXBElement::getValue).orElse(null));
		return rappresentante;
	}

	public static RappresentanteLegaleDto from(final PERSONA persona) {
		RappresentanteLegaleDto rappresentanteLegaleDto = new RappresentanteLegaleDto();
		PERSONAFISICA personafisica = persona.getPERSONAFISICA();
		rappresentanteLegaleDto.setCodiceFiscale(personafisica.getCODICEFISCALE());
		
		String nominativo = String.format("%s %s", personafisica.getNOME(), personafisica.getCOGNOME());
		rappresentanteLegaleDto.setNominativo(nominativo);
		return rappresentanteLegaleDto;
	}
}
