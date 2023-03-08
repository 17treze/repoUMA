package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Geometry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TempPoligoniInOutAdlModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.WorkspaceLavSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.GisUtils;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.WorkspaceLavSuoloDto;

@Mapper(componentModel = "spring", uses = { LavorazioneSuoloMapper.class, StatoColtMapper.class, UsoSuoloMapper.class, AnomaliaValidazioneMapper.class })
public interface WorkspaceLavSuoloMapper {

	@Mapping(target = "extent", expression = "java(calculateExtent(workspaceLavSuolo.getShape()))")
	@Mapping(target = "idLavorazione", expression = "java(workspaceLavSuolo.getIdLavorazioneWorkspaceLavSuolo().getId())")
	public WorkspaceLavSuoloDto convertToDto(WorkspaceLavSuoloModel workspaceLavSuolo);

	public WorkspaceLavSuoloModel convertToModel(WorkspaceLavSuoloDto workspaceLavSuolo);

	@Mapping(target = "idGridWorkspace", source = "grid")
	@Mapping(target = "codUsoSuoloWorkspaceLavSuolo", source = "codUsoSuolo")
	@Mapping(target = "statoColtWorkspaceLavSuolo", source = "statoColt")
	@Mapping(target = "idLavorazioneWorkspaceLavSuolo", source = "lavorazioneSuolo")
	public WorkspaceLavSuoloModel convertTempPoligoniInOutAdlModelToModel(TempPoligoniInOutAdlModel tempPoligoniInOutAdlModel);

	default List<WorkspaceLavSuoloDto> fromList(List<WorkspaceLavSuoloModel> list) {
		return list.stream().map(r -> convertToDto(r)).collect(Collectors.toList());
	}

	default Double[] calculateExtent(Geometry shape) {
		return GisUtils.calculateExtent(shape);
	}
}
