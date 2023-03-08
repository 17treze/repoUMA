package it.tndigitale.a4g.proxy.dto.mapper;

import java.util.List;

import it.tndigitale.a4g.proxy.dto.persona.IndirizzoDto;
import it.tndigitale.a4g.proxy.dto.persona.IscrizioneRepertorioEconomicoDto;
import it.tndigitale.a4g.proxy.dto.persona.SedeDto;
import it.tndigitale.ws.wssanagraficaimprese.ATTIVITAISTAT;
import it.tndigitale.ws.wssanagraficaimprese.DATIIMPRESA;
import it.tndigitale.ws.wssanagraficaimprese.INDIRIZZO;
import it.tndigitale.ws.wssanagraficaimprese.INFORMAZIONISEDE;

public class SedeMapper {
	
	public static SedeDto from(DATIIMPRESA datiImpresa) {
		SedeDto sede = new SedeDto();
		INFORMAZIONISEDE informazioniSede = datiImpresa.getINFORMAZIONISEDE();
		INDIRIZZO indirizzo = informazioniSede.getINDIRIZZO();
		
		//Sede Legale
		sede.setIndirizzo(IndirizzoMapper.from(indirizzo));
		sede.setIndirizzoPec(indirizzo.getINDIRIZZOPEC());
		sede.setTelefono(indirizzo.getTELEFONO());
		
		//Iscrizione Camera di Commercio
		sede.setIscrizioneRegistroImprese(IscrizioneRegistroImpreseMapper.from(datiImpresa.getESTREMIIMPRESA()));
		
		//Lista Attivita economiche Ateco
		sede.setAttivitaAteco(AttivitaAtecoMapper.fromV2(datiImpresa.getINFORMAZIONISEDE().getCODICEATECOUL().getATTIVITAISTAT()));
		
		return sede;
	}
}
