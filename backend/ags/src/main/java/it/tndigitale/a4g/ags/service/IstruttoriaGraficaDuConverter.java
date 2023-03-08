package it.tndigitale.a4g.ags.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.ags.dto.IstruttoriaGraficaDuDto;
import it.tndigitale.a4g.ags.entity.ViewIstruttoriaGraficaDuEntity;

@Component
public class IstruttoriaGraficaDuConverter  implements Converter<ViewIstruttoriaGraficaDuEntity, IstruttoriaGraficaDuDto> {

	@Override
	public IstruttoriaGraficaDuDto convert(ViewIstruttoriaGraficaDuEntity source) {
		IstruttoriaGraficaDuDto dto = new IstruttoriaGraficaDuDto();
		dto.setCuaa(source.getCuaa());
		dto.setDataIstruttoriaGrafica(source.getDataIstruttoriaGrafica());
		dto.setDataRiferimento(source.getDataRiferimento());
		dto.setIdDomanda(source.getIdDomanda());
		dto.setAnno(source.getAnno());
		return dto;
	}
	
	public List<IstruttoriaGraficaDuDto> convert(List<ViewIstruttoriaGraficaDuEntity> sources) {
		List<IstruttoriaGraficaDuDto> dtos = new ArrayList<>();
		for (ViewIstruttoriaGraficaDuEntity source : sources) {
			dtos.add(convert(source));
		}
		return dtos;
	}
}
