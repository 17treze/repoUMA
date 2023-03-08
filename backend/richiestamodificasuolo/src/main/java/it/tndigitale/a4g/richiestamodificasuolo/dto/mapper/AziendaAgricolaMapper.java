package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.RichiestaModificaSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.AziendaAgricolaDto;

@Mapper(componentModel = "spring")
public interface AziendaAgricolaMapper {
	
	@Mapping(source = "azienda", target = "ragioneSociale")
	public AziendaAgricolaDto getAziendaAgricolaDto(RichiestaModificaSuoloModel modificaSuoloModel);

}
