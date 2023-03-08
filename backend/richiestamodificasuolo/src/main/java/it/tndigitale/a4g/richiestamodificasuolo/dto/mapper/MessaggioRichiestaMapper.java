package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.MessaggioRichiestaModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.MessaggioRichiestaDto;

@Mapper(componentModel = "spring")
public interface MessaggioRichiestaMapper {

	default List<MessaggioRichiestaDto> from(Page<MessaggioRichiestaModel> page) {
		return page.stream().map( m -> fromMessaggioRichiestaModel(m)).collect(Collectors.toList());
	}

	default List<MessaggioRichiestaDto> fromList(List<MessaggioRichiestaModel> list) {
		return list.stream().map(r -> convertToDto(r)).collect(Collectors.toList());
	}

	@Mapping(source = "relSuoloDichiarato.id", target = "idPoligonoDichiarato")
	public MessaggioRichiestaDto fromMessaggioRichiestaModel(MessaggioRichiestaModel messaggioRichiestaModel);
	@Mapping(source = "relSuoloDichiarato.id", target = "idPoligonoDichiarato")
	public MessaggioRichiestaDto convertToDto(MessaggioRichiestaModel messaggioRichiestaModel);

	@Mapping(target = "utente", ignore = true)
	@Mapping(source = "idPoligonoDichiarato", target = "relSuoloDichiarato.id")
	public MessaggioRichiestaModel convertToModel(MessaggioRichiestaDto dto);
}
