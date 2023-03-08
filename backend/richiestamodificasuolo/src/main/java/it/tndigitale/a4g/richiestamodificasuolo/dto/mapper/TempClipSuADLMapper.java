package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Geometry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TempClipSuADLModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.GisUtils;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.TempClipSuADLDto;

@Mapper(componentModel = "spring", uses = { LavorazioneSuoloMapper.class, GrigliaSuoloMapper.class, StatoColtMapper.class, UsoSuoloMapper.class })
public interface TempClipSuADLMapper {

	@Mapping(target = "extent", expression = "java(calculateExtent(tempClipSuADL.getShape()))")
	@Mapping(target = "shape", expression = "java(getShapeString(tempClipSuADL.getShape()))")
	public TempClipSuADLDto convertToDto(TempClipSuADLModel tempClipSuADL);

	@Mapping(target = "shape", expression = "java(getShapeGeometry(tempClipSuDto.getShape()))")
	public TempClipSuADLModel convertToModel(TempClipSuADLDto tempClipSuDto);

	default List<TempClipSuADLDto> from(Page<TempClipSuADLModel> page) {
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