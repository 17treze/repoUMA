package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.RichiestaModificaSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.DatiAggiuntiviRichiestaModificaSuoloDto;

@Mapper(componentModel = "spring")
public interface DatiAggiuntiviRichiestaModificaSuoloMapper {

	@Mapping(source = "interventoInizio", target = "periodoIntervento.dataInizio")
	@Mapping(source = "interventoFine", target = "periodoIntervento.dataFine")
	@Mapping(source = "visibileInOrtofoto", target = "visibileOrtofoto")
	public DatiAggiuntiviRichiestaModificaSuoloDto getDatiAggiuntiviRichiestaModificaSuoloDto(
			RichiestaModificaSuoloModel modificaSuoloModel);
}
