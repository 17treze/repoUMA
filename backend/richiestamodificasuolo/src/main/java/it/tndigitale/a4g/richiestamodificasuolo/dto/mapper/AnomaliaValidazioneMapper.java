package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Geometry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.AnomaliaValidazioneModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.GisUtils;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.AnomaliaValidazioneDto;

@Mapper(componentModel = "spring", uses = { WorkspaceLavSuoloMapper.class })
public interface AnomaliaValidazioneMapper {

	@Mapping(target = "extent", expression = "java(calculateExtent(anomaliaValidazioneModel.getShape()))")
	// @Mapping(target = "workspaceDto", source = "anomaliaWorkspaceRel")
	@Mapping(target = "idLavorazione", expression = "java(anomaliaValidazioneModel.getLavorazioneSuoloInAnomaliaValidazione().getId())")
	public AnomaliaValidazioneDto convertToDto(AnomaliaValidazioneModel anomaliaValidazioneModel);

	default List<AnomaliaValidazioneDto> fromList(List<AnomaliaValidazioneModel> list) {
		return list.stream().map(r -> convertToDto(r)).collect(Collectors.toList());
	}

	default String getShapeString(Geometry shape) {
		return GisUtils.getWKTGeometry(shape);
	}

	default Double[] calculateExtent(Geometry shape) {
		return GisUtils.calculateExtent(shape);
	}
}
