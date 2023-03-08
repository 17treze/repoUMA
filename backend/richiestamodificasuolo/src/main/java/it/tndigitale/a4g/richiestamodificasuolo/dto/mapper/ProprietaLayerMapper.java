package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ProprietaLayerModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.layer.ProprietaLayerDto;

@Mapper(componentModel = "spring")
public interface ProprietaLayerMapper {

	public ProprietaLayerDto convertToDto(ProprietaLayerModel proprietaLayerModel);

	public ProprietaLayerModel convertToModel(ProprietaLayerDto proprietaLayerDto);

	default List<ProprietaLayerDto> from(List<ProprietaLayerModel> listLayer) {
		return listLayer.stream().map(r -> convertToDto(r)).collect(Collectors.toList());
	}

}