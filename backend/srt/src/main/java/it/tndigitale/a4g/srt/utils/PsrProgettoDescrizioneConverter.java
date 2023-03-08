package it.tndigitale.a4g.srt.utils;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import it.tndigitale.a4g.srt.dto.PsrProgettoDescrizioneDto;

@Component
public class PsrProgettoDescrizioneConverter {
	
	public PsrProgettoDescrizioneDto fromDb(Object[] result) {
		PsrProgettoDescrizioneDto dto = new PsrProgettoDescrizioneDto();
		dto.setIdProgetto((Integer)result[0]);
		dto.setCodificaDescrizione((String)result[1]);
		dto.setDettaglioDescrizione((String)result[2]);
		dto.setCostoRichiesto((BigDecimal)result[3]);
		dto.setContributoRichiesto((BigDecimal)result[4]);
		dto.setContributoAmmesso((BigDecimal)result[5]);
		return dto;
	}
}
