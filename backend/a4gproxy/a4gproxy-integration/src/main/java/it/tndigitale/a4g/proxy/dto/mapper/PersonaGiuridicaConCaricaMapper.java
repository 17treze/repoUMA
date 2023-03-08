package it.tndigitale.a4g.proxy.dto.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import it.tndigitale.a4g.proxy.dto.persona.CaricaDto;
import it.tndigitale.a4g.proxy.dto.persona.PersonaGiuridicaConCaricaDto;
import it.tndigitale.ws.wssanagraficaimprese.CARICA;
import it.tndigitale.ws.wssanagraficaimprese.PERSONA;
import it.tndigitale.ws.wssanagraficaimprese.PERSONAGIURIDICA;

public class PersonaGiuridicaConCaricaMapper {
	
	private static final Logger log = LoggerFactory.getLogger(PersonaGiuridicaConCaricaMapper.class);
	
	private PersonaGiuridicaConCaricaMapper() {}

	static List<PersonaGiuridicaConCaricaDto> from(List<PERSONA> personeList) {
		List<PersonaGiuridicaConCaricaDto> personeGiuridicheConCarica = new ArrayList<>();
		for (PERSONA persona : personeList) {
			PERSONAGIURIDICA personaGiuridicaParix = persona.getPERSONAGIURIDICA();
			if (personaGiuridicaParix != null) {
				List<CaricaDto> caricaDtoList = new ArrayList<CaricaDto>();
				for (CARICA carica : persona.getCARICHE().getCARICA()) {
					CaricaDto caricaDto = new CaricaDto();
					caricaDto.setDescrizione(carica.getDESCRIZIONE().trim());
					caricaDto.setIdentificativo(carica.getCCARICA().trim());
					try {
						if(StringUtils.hasLength(carica.getDTINIZIO()) && carica.getDTINIZIO().trim().length() > 0) {
							caricaDto.setDataInizio(LocalDate.parse(carica.getDTINIZIO(), DateTimeFormatter.ofPattern("yyyyMMdd")));	
						}	
					}catch(DateTimeParseException e) {
						log.warn("Formato data {} invalido per la persona fisica {}", carica.getDTINIZIO(), personaGiuridicaParix.getCODICEFISCALE());
					}
					
					caricaDtoList.add(caricaDto);
				}
				PersonaGiuridicaConCaricaDto personaGiuridicaConCaricaDto = new PersonaGiuridicaConCaricaDto();
				personaGiuridicaConCaricaDto.setListaCarica(caricaDtoList);
				personaGiuridicaConCaricaDto.setCodiceFiscale(personaGiuridicaParix.getCODICEFISCALE());
				personaGiuridicaConCaricaDto.setDenominazione(personaGiuridicaParix.getDENOMINAZIONE());
				personeGiuridicheConCarica.add(personaGiuridicaConCaricaDto);
			}
		}
		return personeGiuridicheConCarica;
	}
}
