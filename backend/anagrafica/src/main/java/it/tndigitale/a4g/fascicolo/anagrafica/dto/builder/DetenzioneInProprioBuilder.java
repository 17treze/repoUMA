package it.tndigitale.a4g.fascicolo.anagrafica.dto.builder;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DetenzioneInProprioModel;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.DetenzioneInProprioDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.TipoDetenzioneEnum;

public class DetenzioneInProprioBuilder {
	
	private DetenzioneInProprioBuilder() {}

	public static DetenzioneInProprioDto from(final DetenzioneInProprioModel detenzioneModel)  {
		if (detenzioneModel == null) return null;
		DetenzioneInProprioDto detenzioneDto =  new DetenzioneInProprioDto();
		DetenzioneBuilder.fromAux(detenzioneDto, detenzioneModel);
		detenzioneDto.setTipoDetenzione(TipoDetenzioneEnum.DETENZIONE_IN_PROPRIO);
		return detenzioneDto;
	}
}
