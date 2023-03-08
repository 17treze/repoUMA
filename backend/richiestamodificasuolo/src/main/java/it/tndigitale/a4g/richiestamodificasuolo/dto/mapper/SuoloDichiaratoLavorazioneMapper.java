package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Geometry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoLavorazioneModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.GisUtils;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloDichiaratoLavorazioneDto;

@Mapper(componentModel = "spring", uses = { AziendaAgricolaMapper.class })
public interface SuoloDichiaratoLavorazioneMapper {

	default List<SuoloDichiaratoLavorazioneDto> from(Page<SuoloDichiaratoLavorazioneModel> page) {
		return page.stream().map(s -> convertToDto(s)).collect(Collectors.toList());
	}

	@Mapping(target = "aziendaAgricola", source = ".")
	@Mapping(target = "visible", constant = "true")
	// @Mapping(target = "shapeWkt", expression = "java(getShapeString(usoSuoloModel.getShape()))")
	@Mapping(target = "extent", expression = "java(calculateExtent(suoloDichiaratoLavorazioneModel.getShape()))")
	public SuoloDichiaratoLavorazioneDto convertToDto(SuoloDichiaratoLavorazioneModel suoloDichiaratoLavorazioneModel);

	public SuoloDichiaratoLavorazioneModel convertToModel(SuoloDichiaratoLavorazioneDto suoloDichiaratoLavorazioneDto);
	
	public SuoloDichiaratoModel convertToSuoloDichiaratoModel(SuoloDichiaratoLavorazioneDto suoloDichiaratoLavorazioneDto);

	default List<SuoloDichiaratoLavorazioneDto> fromList(List<SuoloDichiaratoLavorazioneModel> list) {
		return list.stream().map(s -> convertToDto(s)).collect(Collectors.toList());
	}

	default String getShapeString(Geometry shape) {
		return GisUtils.getWKTGeometry(shape);
	}

	default Double[] calculateExtent(Geometry shape) {
		return GisUtils.calculateExtent(shape);
	}
}
