package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LayerModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.layer.LayerSwitcherDto;

@Mapper(componentModel = "spring")
public interface LayerMapper {

	@Mapping(target = "proprieta", source = "listProprietaLayer")
	@Mapping(target = "attributi", source = "listAttributiLayer")
	public LayerSwitcherDto convertToDto(LayerModel layerModel);

	public LayerModel convertToModel(LayerSwitcherDto layerSwitcherDto);

	default List<LayerSwitcherDto> from(List<LayerModel> listLayer) {
		return listLayer.stream().map(r -> convertToDto(r)).collect(Collectors.toList());
	}

}