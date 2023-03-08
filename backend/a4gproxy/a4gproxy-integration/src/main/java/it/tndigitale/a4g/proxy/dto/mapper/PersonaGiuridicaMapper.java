package it.tndigitale.a4g.proxy.dto.mapper;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.tndigitale.a4g.proxy.dto.persona.IscrizioneSezioneDto;
import it.tndigitale.a4g.proxy.dto.persona.PersonaGiuridicaDto;
import it.tndigitale.a4g.proxy.dto.persona.SedeDto;
import it.tndigitale.a4g.proxy.utils.DateFormatUtils;
import it.tndigitale.ws.isvalidazioneanagrafe.ISSoggettoPro2P;
import it.tndigitale.ws.isvalidazioneanagrafe.RispostaRichiestaRispostaSincronaRicercaAnagraficaAll;
import it.tndigitale.ws.wssanagraficaimprese.DATIIMPRESA;
import it.tndigitale.ws.wssanagraficaimprese.ESTREMIIMPRESA;
import it.tndigitale.ws.wssanagraficaimprese.PERSONA;
import it.tndigitale.ws.wssanagraficaimprese.RISPOSTA;

public class PersonaGiuridicaMapper {
	
	private static final Logger log = LoggerFactory.getLogger(PersonaGiuridicaMapper.class);
	
	private PersonaGiuridicaMapper() { }
	
	public static PersonaGiuridicaDto fromAnagrafeTributaria(RispostaRichiestaRispostaSincronaRicercaAnagraficaAll response) {
		PersonaGiuridicaDto personaGiuridica = new PersonaGiuridicaDto();
		
		ISSoggettoPro2P persona = response.getRisposta().getSoggetto().getValue();
		personaGiuridica.setCodiceFiscale(persona.getCodiceFiscale().getValue());
		personaGiuridica.setFormaGiuridica(persona.getDenominazione().getValue().getNaturaGiuridica().getValue().getDescrizione().getValue());
		personaGiuridica.setDenominazione(persona.getDenominazione().getValue().getDenominazione().getValue());
		personaGiuridica.setPartitaIva(persona.getPartitaIva().getValue().getPiva().getValue());
		
		SedeDto sede = new SedeDto();
		sede.setIndirizzo(IndirizzoMapper.from(persona.getSede().getValue()));
		sede.setAttivitaAteco(AttivitaAtecoMapper.fromAgenziaEntrateV2(persona.getAttivita()));
		personaGiuridica.setSedeLegale(sede);
		
		personaGiuridica.setRappresentanteLegale(RappresentanteLegaleMapper.from(persona.getRappresentante().getValue().getRappresentante().getValue()));

		return personaGiuridica;
	}

	public static PersonaGiuridicaDto from(JAXBElement<RISPOSTA> dettaglioCompleto) throws Exception {
		if (dettaglioCompleto == null) {
			return null;
		}
		PersonaGiuridicaDto personaGiuridica = new PersonaGiuridicaDto();
		//Dati Identificativi
		DATIIMPRESA datiImpresa = dettaglioCompleto.getValue().getDATI().getDATIIMPRESA();
		ESTREMIIMPRESA estremiImpresa = datiImpresa.getESTREMIIMPRESA();
		
		String codiceFiscale = estremiImpresa.getCODICEFISCALE();
		personaGiuridica.setCodiceFiscale(codiceFiscale);
		
		personaGiuridica.setDenominazione(estremiImpresa.getDENOMINAZIONE());
		personaGiuridica.setFormaGiuridica(estremiImpresa.getFORMAGIURIDICA().getDESCRIZIONE());		
		personaGiuridica.setOggettoSociale(datiImpresa.getOGGETTOSOCIALE());
		personaGiuridica.setDataCostituzione((datiImpresa.getDURATASOCIETA() != null && datiImpresa.getDURATASOCIETA().getDTCOSTITUZIONE() != null) ? DateFormatUtils.convertiDataParixLocalDate(datiImpresa.getDURATASOCIETA().getDTCOSTITUZIONE()) : null);
		personaGiuridica.setDataTermine((datiImpresa.getDURATASOCIETA() != null && datiImpresa.getDURATASOCIETA().getDTTERMINE() != null) ? DateFormatUtils.convertiDataParixLocalDate(datiImpresa.getDURATASOCIETA().getDTTERMINE()) : null);
		List<IscrizioneSezioneDto> iscrizioniSezioneDto = IscrizioneSezioneMapper.from(datiImpresa);
		personaGiuridica.setIscrizioniSezione(iscrizioniSezioneDto);
		
		if (datiImpresa.getCAPITALI() != null && datiImpresa.getCAPITALI().getCAPITALESOCIALE() != null) {
			personaGiuridica.setCapitaleSocialeDeliberato(Double.valueOf(datiImpresa.getCAPITALI().getCAPITALESOCIALE().getDELIBERATO()));
		}
		personaGiuridica.setPartitaIva(estremiImpresa.getPARTITAIVA());
		
		SedeDto sedeLegale = SedeMapper.from(datiImpresa);
		personaGiuridica.setSedeLegale(sedeLegale);
		List<PERSONA> personeList = datiImpresa.getPERSONESEDE().getPERSONA();
		personaGiuridica.setPersoneFisicheConCarica(PersonaFisicaConCaricaMapper.from(personeList));
		personaGiuridica.setPersoneGiuridicheConCarica(PersonaGiuridicaConCaricaMapper.from(personeList));
		
//		arricchimento dati ute
		personaGiuridica.setUnitaLocali(UnitaLocaleMapper.from(dettaglioCompleto));
		return personaGiuridica;
	}
}
