package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.builder;

import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.FabbricatoModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.FabbricatoDto;

@Component
public class FabbricatoDtoBuilder {
	private FabbricatoDto fabbricatoDto;
	
	public FabbricatoDtoBuilder() {
		fabbricatoDto = new FabbricatoDto();
	}
	
	public FabbricatoDtoBuilder newDto() {
		fabbricatoDto = new FabbricatoDto();
		return this;
	}
	
	public FabbricatoDtoBuilder from(FabbricatoModel model) {
		fabbricatoDto
		.setId(model.getId())
		.setComune(model.getComune())
		.setVolume(model.getVolume())
		.setSuperficie(model.getSuperficie())
		.setTipologia(model.getSottotipo().getTipologia().getDescrizione()); 
		
		return this;
	}
	
	public FabbricatoDto build() {
		return fabbricatoDto;
	}
}
