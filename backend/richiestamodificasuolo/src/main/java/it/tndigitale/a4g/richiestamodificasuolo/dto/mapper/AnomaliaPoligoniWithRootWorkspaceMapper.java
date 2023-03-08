package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Geometry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.WorkspaceLavSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.GisUtils;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.AnomaliaPoligoniWithRootWorkspaceDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = { AnomaliaValidazioneMapper.class })
public interface AnomaliaPoligoniWithRootWorkspaceMapper {

	@Mapping(target = "extent", expression = "java(calculateExtent(anomaliaValidazioneModel.getShape()))")
	@Mapping(target = "idLavorazione", expression = "java(anomaliaValidazioneModel.getIdLavorazioneWorkspaceLavSuolo().getId())")
	@Mapping(target = "anomaliaDescrizione", expression = "java(getAnomalia(anomaliaValidazioneModel))")
	@Mapping(target = "anomaliaDto", source = "anomaliaValidazione")
	public AnomaliaPoligoniWithRootWorkspaceDto convertToDto(WorkspaceLavSuoloModel anomaliaValidazioneModel);

	default List<AnomaliaPoligoniWithRootWorkspaceDto> fromList(Set<WorkspaceLavSuoloModel> set) {
		return set.stream().map(r -> convertToDto(r)).collect(Collectors.toList());
	}

	default Double[] calculateExtent(Geometry shape) {
		return GisUtils.calculateExtent(shape);
	}

	default String getAnomalia(WorkspaceLavSuoloModel anomaliaValidazioneModel) {
		var primaAnomalia = anomaliaValidazioneModel.getAnomaliaValidazione().stream().findFirst();
		if (primaAnomalia.isPresent()) {
			return primaAnomalia.get().getDettaglioAnomalia();
		}

		return "";
	}

}