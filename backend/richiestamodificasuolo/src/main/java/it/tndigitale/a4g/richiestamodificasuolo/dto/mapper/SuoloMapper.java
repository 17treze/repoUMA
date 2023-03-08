package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Geometry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.GisUtils;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloDto;

@Mapper(componentModel = "spring", uses = { LavorazioneSuoloMapper.class, GrigliaSuoloMapper.class, StatoColtMapper.class, UsoSuoloMapper.class })
public interface SuoloMapper {

	@Mapping(target = "extent", expression = "java(calculateExtent(usoSuoloModel.getShape()))")
	@Mapping(target = "shape", expression = "java(getShapeString(usoSuoloModel.getShape()))")
	public SuoloDto convertToDto(SuoloModel usoSuoloModel);

	@Mapping(target = "shape", expression = "java(getShapeGeometry(usoSuoloDto.getShape()))")
	public SuoloModel convertToModel(SuoloDto usoSuoloDto);

	default List<SuoloDto> from(Page<SuoloModel> page) {
		return page.stream().map(r -> convertToDto(r)).collect(Collectors.toList());
	}

	default String getShapeString(Geometry shape) {
		return GisUtils.getWKTGeometry(shape);
	}

	default Geometry getShapeGeometry(String wkt) {
		return GisUtils.getGeometry(wkt);
	}

	default Double[] calculateExtent(Geometry shape) {
		return GisUtils.calculateExtent(shape);
	}
}