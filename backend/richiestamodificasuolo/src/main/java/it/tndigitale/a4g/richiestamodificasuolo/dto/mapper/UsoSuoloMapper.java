package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import org.mapstruct.Mapper;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.UsoSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.UsoSuoloDto;

@Mapper(componentModel = "spring", uses = { SuoloMapper.class })
public interface UsoSuoloMapper {

	public UsoSuoloDto convertToDto(UsoSuoloModel usoSuoloModel);

	public UsoSuoloModel convertToModel(UsoSuoloDto usoSuoloDto);

}