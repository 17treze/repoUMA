package it.tndigitale.a4g.fascicolo.anagrafica.dto.mapper;

import org.springframework.beans.BeanUtils;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.SportelloModel;

public final class MandatoMapper {
	
	public static MandatoModel copyMandato(SportelloModel newSportello, MandatoModel detenzioneModelOld) {
		MandatoModel detenzioneModelNew = new MandatoModel();
		BeanUtils.copyProperties(detenzioneModelOld, detenzioneModelNew,
				"id","version","dataFine", "revocheImmediate");
		detenzioneModelNew.setSportello(newSportello);
		return detenzioneModelNew;
	}

}
