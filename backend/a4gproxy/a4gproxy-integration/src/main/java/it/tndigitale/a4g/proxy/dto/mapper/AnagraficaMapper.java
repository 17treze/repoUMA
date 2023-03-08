package it.tndigitale.a4g.proxy.dto.mapper;

import it.tndigitale.a4g.proxy.dto.persona.AnagraficaDto;
import it.tndigitale.a4g.proxy.dto.persona.Sesso;
import it.tndigitale.a4g.proxy.utils.DateFormatUtils;
import it.tndigitale.ws.isvalidazioneanagrafe.ISPersonaFisicaPro2P;
import it.tndigitale.ws.wssanagraficaimprese.ESTREMINASCITA;
import it.tndigitale.ws.wssanagraficaimprese.PERSONAFISICA;

public class AnagraficaMapper {
	
	private final static String MASCHIO = "M";
	private final static String FEMMINA = "F";

	private AnagraficaMapper() {}

	public static AnagraficaDto from(ISPersonaFisicaPro2P persona) {
		AnagraficaDto anagrafica = new AnagraficaDto();
		anagrafica.setCognome(persona.getCognome().getValue());
		anagrafica.setNome(persona.getNome().getValue());
		anagrafica.setDataNascita(DateFormatUtils.convertiLocalDate(persona.getDatiNascita().getValue().getData()));
		anagrafica.setComuneNascita(persona.getDatiNascita().getValue().getComune().getValue());
		anagrafica.setProvinciaNascita(persona.getDatiNascita().getValue().getProvincia().getValue());
		
		String sesso = persona.getSesso().getValue().getValore().getValue();
		if (sesso.equals(MASCHIO)) {
			anagrafica.setSesso(Sesso.MASCHIO);
		}
		if (sesso.equals(FEMMINA)) {
			anagrafica.setSesso(Sesso.FEMMINA);
		}
		
		return anagrafica;
	}
	
	// PARIX
	public static AnagraficaDto from(PERSONAFISICA personaFisica) {
		AnagraficaDto anagrafica = new AnagraficaDto();
		anagrafica.setNome(personaFisica.getNOME());
		anagrafica.setCognome(personaFisica.getCOGNOME());
		
		ESTREMINASCITA estreminascita = personaFisica.getESTREMINASCITA();
		anagrafica.setComuneNascita(estreminascita.getCOMUNE());
		anagrafica.setDataNascita(DateFormatUtils.convertiDataParixLocalDate(estreminascita.getDATA()));
		anagrafica.setProvinciaNascita(estreminascita.getPROVINCIA());
		
		Sesso sesso;
		switch (personaFisica.getSESSO()) {
		case "MASCHILE":
			sesso = Sesso.MASCHIO;
			break;
		case "FEMMINILE":
			sesso = Sesso.FEMMINA;
			break;
		default:
			sesso = null;
		}
		anagrafica.setSesso(sesso);
		return anagrafica;
	}

}
