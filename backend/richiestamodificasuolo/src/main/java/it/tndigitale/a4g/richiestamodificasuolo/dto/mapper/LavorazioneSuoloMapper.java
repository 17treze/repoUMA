package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.LavorazioneSuoloDto;

@Mapper(componentModel = "spring")
public interface LavorazioneSuoloMapper {

	@Mapping(target = "suoloDichiaratoModel", expression = "java(clonaLista(lavorazioneModel.getSuoloDichiaratoModel()))")
	public LavorazioneSuoloModel clona(LavorazioneSuoloModel lavorazioneModel);

	@Mapping(target = "lavorazioneSuolo", ignore = true)
	SuoloDichiaratoModel clona(SuoloDichiaratoModel suoloDichiarato);

	List<SuoloDichiaratoModel> clonaLista(List<SuoloDichiaratoModel> b);

	public LavorazioneSuoloDto convertToDto(LavorazioneSuoloModel lavorazioneModel);

	public LavorazioneSuoloModel convertToModel(LavorazioneSuoloDto lavorazioneDto);

	public LavorazioneSuoloModel fromDtoToModel(LavorazioneSuoloDto lavorazioneSuoloDto, @MappingTarget LavorazioneSuoloModel lavo);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "utente", ignore = true)
	@Mapping(target = "dataInizioLavorazione", ignore = true)
	@Mapping(target = "stato", ignore = true)
	@Mapping(target = "modalitaADL", ignore = true)
	@Mapping(target = "xUltimoZoom", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "yUltimoZoom", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "scalaUltimoZoom", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	public LavorazioneSuoloModel fromDtoToModelUpdate(LavorazioneSuoloDto lavorazioneSuoloDto, @MappingTarget LavorazioneSuoloModel lavo);

	default List<LavorazioneSuoloDto> from(Page<LavorazioneSuoloModel> page) {
		return page.stream().map(r -> convertToDto(r)).collect(Collectors.toList());
	}
}
