package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoColtModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.UsoSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.CodDescCodificaSuoloDto;

@Mapper(componentModel = "spring", uses = {UsoSuoloMapper.class })
public interface CodificheSuoloMapper {
	@Mapping(target = "codice", source = "codUsoSuolo")
	public CodDescCodificaSuoloDto convertToDtoUsoSuolo(UsoSuoloModel r);

	default List<CodDescCodificaSuoloDto> fromUsoSuolo(List<UsoSuoloModel> page) {
		return page.stream().map(r -> convertToDtoUsoSuolo(r)).collect(Collectors.toList());
	}
	
	@Mapping(target = "codice", source = "statoColt")
	public CodDescCodificaSuoloDto convertToDtoStatoColt(StatoColtModel r);

	default List<CodDescCodificaSuoloDto> fromStatoColt(List<StatoColtModel> page) {
		return page.stream().map(r -> convertToDtoStatoColt(r)).collect(Collectors.toList());
	}
}
 