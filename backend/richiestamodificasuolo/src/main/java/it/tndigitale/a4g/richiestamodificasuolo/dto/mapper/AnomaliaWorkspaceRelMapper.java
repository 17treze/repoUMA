package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.AnomaliaValidazioneRelModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.AnomaliaWorkspaceRelDto;

@Mapper(componentModel = "spring", uses = { WorkspaceLavSuoloMapper.class, AnomaliaValidazioneMapper.class })
public interface AnomaliaWorkspaceRelMapper {
	@Mapping(target = "anomaliaDto", source = "anomaliaValidazioneModel")
	@Mapping(target = "workspaceDto", source = "workspaceLavSuoloModel")
	public AnomaliaWorkspaceRelDto convertToDto(AnomaliaValidazioneRelModel anomaliaValidazioneRelModel);

	default List<AnomaliaWorkspaceRelDto> fromList(Set<AnomaliaValidazioneRelModel> list) {
		return list.stream().map(r -> convertToDto(r)).collect(Collectors.toList());
	}
}
