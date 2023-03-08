package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import org.mapstruct.Mapper;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoColtModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.StatoColtDto;

@Mapper(componentModel = "spring")
public interface StatoColtMapper {

	public StatoColtDto convertToDto(StatoColtModel statoColtModel);

	public StatoColtModel convertToModel(StatoColtDto statoColtDto);
}
