package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import org.mapstruct.Mapper;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.GrigliaSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.GrigliaSuoloDto;

@Mapper(componentModel = "spring")
public interface GrigliaSuoloMapper {

	public GrigliaSuoloDto convertToDto(GrigliaSuoloModel grigliaSuoloModel);

	public GrigliaSuoloModel convertToModel(GrigliaSuoloDto grigliaSuoloDto);
}
