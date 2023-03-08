package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Geometry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.RichiestaModificaSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.GisUtils;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.RichiestaModificaSuoloDto;

@Mapper(componentModel = "spring", uses = { AziendaAgricolaMapper.class, ComuneCatastaleMapper.class, DatiAggiuntiviRichiestaModificaSuoloMapper.class })
public interface RichiestaModificaSuoloMapper {

	@Mapping(target = "data", source = "dataRichiesta")
	@Mapping(target = "aziendaAgricola", source = ".")
	@Mapping(target = "datiAggiuntivi", source = ".")
	@Mapping(target = "sezioniCatastali", source = "suoloDichiaratoModel")
	@Mapping(target = "extent", expression = "java(getExtentShape(modificaSuoloModel.getSuoloDichiaratoModel()))")
	public RichiestaModificaSuoloDto fromRichiestaModificaSuolo(RichiestaModificaSuoloModel modificaSuoloModel);

	@Mapping(target = "interventoInizio", source = "datiAggiuntivi.periodoIntervento.dataInizio")
	@Mapping(target = "interventoFine", source = "datiAggiuntivi.periodoIntervento.dataFine")
	@Mapping(target = "visibileInOrtofoto", source = "datiAggiuntivi.visibileOrtofoto")
	@Mapping(target = "tipoInterventoColturale", source = "datiAggiuntivi.tipoInterventoColturale")
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "tipo", ignore = true)
	@Mapping(target = "esito", ignore = true)
	@Mapping(target = "utente", ignore = true)
	@Mapping(target = "campagna", ignore = true)
	public RichiestaModificaSuoloModel fromRichiestaModificaSuoloDtoToModel(@MappingTarget RichiestaModificaSuoloModel richiestaModificaSuoloModelToUpdate,
			RichiestaModificaSuoloDto richiestaModificaSuoloDto);

	default List<RichiestaModificaSuoloDto> from(Page<RichiestaModificaSuoloModel> page) {
		return page.stream().map(r -> fromRichiestaModificaSuolo(r)).collect(Collectors.toList());
	}

	default Double[] getExtentShape(List<SuoloDichiaratoModel> suoli) {
		Double[] result = null;
		if (suoli != null && !suoli.isEmpty()) {

			Geometry shapeUnion = suoli.get(0).getShape();

			for (SuoloDichiaratoModel suolo : suoli) {
				if (suolo.getShape() != null) {
					shapeUnion = shapeUnion.union(suolo.getShape());
				}
				result = GisUtils.calculateExtent(shapeUnion);
			}
		}
		return result;
	}
}
