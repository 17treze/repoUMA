package it.tndigitale.a4g.proxy.dto.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import it.tndigitale.a4g.proxy.dto.persona.AnagraficaDto;
import it.tndigitale.a4g.proxy.dto.persona.CaricaDto;
import it.tndigitale.a4g.proxy.dto.persona.PersonaFisicaConCaricaDto;
import it.tndigitale.ws.wssanagraficaimprese.CARICA;
import it.tndigitale.ws.wssanagraficaimprese.PERSONA;
import it.tndigitale.ws.wssanagraficaimprese.PERSONAFISICA;

public class PersonaFisicaConCaricaMapper {
	
	private static final Logger log = LoggerFactory.getLogger(PersonaFisicaConCaricaMapper.class);
	
	private PersonaFisicaConCaricaMapper() {}

	static List<PersonaFisicaConCaricaDto> from(List<PERSONA> personeList) {
		List<PersonaFisicaConCaricaDto> personeFisicheConCarica = new ArrayList<>();
		for (PERSONA persona : personeList) {
			PERSONAFISICA personafisica = persona.getPERSONAFISICA();
			if (personafisica != null) {
				AnagraficaDto anagraficaDto = AnagraficaMapper.from(personafisica);
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
						log.warn("Formato data {} invalido per la persona fisica {}", carica.getDTINIZIO(), personafisica.getCODICEFISCALE());
					}
					
					caricaDtoList.add(caricaDto);
				}
				PersonaFisicaConCaricaDto personaFisicaConCaricaDto = new PersonaFisicaConCaricaDto();
				personaFisicaConCaricaDto.setListaCarica(caricaDtoList);
				personaFisicaConCaricaDto.setAnagrafica(anagraficaDto);
				personaFisicaConCaricaDto.setCodiceFiscale(personafisica.getCODICEFISCALE());
				personeFisicheConCarica.add(personaFisicaConCaricaDto);
			}
		}
		return personeFisicheConCarica;
	}
}
