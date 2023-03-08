package it.tndigitale.a4g.zootecnia.business.persistence.entity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.zootecnia.dto.FascicoloDto;

@Component
public class FascicoloModelDtoConverter implements Converter<FascicoloModel, FascicoloDto> {

	@Override
	public FascicoloDto convert(final FascicoloModel source) {
		FascicoloDto output = new FascicoloDto();
		output.setCuaa(source.getCuaa());
		output.setId(source.getId());
		return output;
	}
}
