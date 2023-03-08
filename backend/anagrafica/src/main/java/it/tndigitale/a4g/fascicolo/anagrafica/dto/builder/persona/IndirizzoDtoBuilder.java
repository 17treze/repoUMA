package it.tndigitale.a4g.fascicolo.anagrafica.dto.builder.persona;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.IndirizzoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.IndirizzoDto;

public class IndirizzoDtoBuilder {

	private IndirizzoDto indirizzoDto;

	public IndirizzoDtoBuilder() {
		indirizzoDto = new IndirizzoDto();
	}

	public IndirizzoDtoBuilder withIndirizzo(IndirizzoModel indirizzoModel) {
		if (indirizzoModel != null) {
			indirizzoDto.setCap(indirizzoModel.getCap());
			indirizzoDto.setCivico(indirizzoModel.getNumeroCivico());
			indirizzoDto.setCodiceIstat(indirizzoModel.getCodiceIstat());
			indirizzoDto.setComune(indirizzoModel.getComune());
			indirizzoDto.setFrazione(indirizzoModel.getFrazione());
			indirizzoDto.setLocalita(indirizzoModel.getFrazione());
			indirizzoDto.setProvincia(indirizzoModel.getProvincia());
			indirizzoDto.setToponimo(indirizzoModel.getDescrizioneEstesa());
			indirizzoDto.setVia(indirizzoModel.getVia());
		}
		return this;
	}

	public IndirizzoDto build() {
		return indirizzoDto;
	}
}
