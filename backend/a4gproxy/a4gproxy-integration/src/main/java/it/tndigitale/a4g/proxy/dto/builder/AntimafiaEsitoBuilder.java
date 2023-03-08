package it.tndigitale.a4g.proxy.dto.builder;

import it.tndigitale.a4g.proxy.dto.AntimafiaEsitoDto;
import it.tndigitale.a4g.proxy.repository.esiti.model.AntimafiaDescrizioneEsitiModel;
import it.tndigitale.a4g.proxy.repository.esiti.model.AntimafiaEsitiModel;

public final class AntimafiaEsitoBuilder {
	
	private static final String NOT_DEFINED = "ND";
	
	public static AntimafiaEsitoDto from(AntimafiaEsitiModel esito,AntimafiaDescrizioneEsitiModel esitoDescrizione ) {
		AntimafiaEsitoDto esitoDto = new AntimafiaEsitoDto();
		if (esitoDescrizione == null) {
			esitoDto.setDescrizioneEsito(esito.getDescrizione());
			esitoDto.setEsitoInvioAgea(NOT_DEFINED);
			esitoDto.setEsitoInvioBdna(NOT_DEFINED);
		} else {
			esitoDto.setDescrizioneEsito(esitoDescrizione.getDescrizione());
			esitoDto.setEsitoInvioAgea(esitoDescrizione.getEsitoInvioAgea());
			esitoDto.setEsitoInvioBdna(esitoDescrizione.getEsitoInvioBdna());
		}
		esitoDto.setEsitoTrasmissione(esito.getEsito());
		esitoDto.setDtElaborazione(esito.getId().getDtElaborazione());
		esitoDto.setCuaa(esito.getId().getCuaa());
		return esitoDto;
	}

}
