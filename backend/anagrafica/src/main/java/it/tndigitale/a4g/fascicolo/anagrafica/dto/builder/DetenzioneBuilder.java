package it.tndigitale.a4g.fascicolo.anagrafica.dto.builder;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DetenzioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.DetenzioneDto;

public class DetenzioneBuilder {
	
	private DetenzioneBuilder() {}
	
	public static DetenzioneDto fromAux(final DetenzioneDto detenzioneDto, final DetenzioneModel detenzioneModel) {
		if (detenzioneDto == null || detenzioneModel == null) return null;
		detenzioneDto.setId(detenzioneModel.getId());
		detenzioneDto.setDataInizio(detenzioneModel.getDataInizio());
		detenzioneDto.setDataFine(detenzioneModel.getDataFine());
		detenzioneDto.setCodiceFiscale(detenzioneModel.getFascicolo().getCuaa());
		detenzioneDto.setDenominazione(detenzioneModel.getFascicolo().getDenominazione());
		return detenzioneDto;
	}
}
