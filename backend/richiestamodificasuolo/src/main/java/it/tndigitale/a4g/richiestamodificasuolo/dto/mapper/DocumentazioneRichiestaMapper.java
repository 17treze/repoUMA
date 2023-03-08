package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.DocumentazioneRichiestaModificaSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.DocumentazioneRichiestaDto;

@Mapper(componentModel = "spring")
public interface DocumentazioneRichiestaMapper {
    
    default List<DocumentazioneRichiestaDto> from(Page<DocumentazioneRichiestaModificaSuoloModel> page) {
        return page.stream().map(d -> fromModelIgnoringContent(d)).collect(Collectors.toList());
    }

    @Mapping(target = "docContent", ignore = true)
    @Mapping(source = "suoloDichiarato.id", target = "idPoligonoDichiarato")
    public DocumentazioneRichiestaDto fromModelIgnoringContent(DocumentazioneRichiestaModificaSuoloModel documentazioneModel);
    
    @Mapping(source = "suoloDichiarato.id", target = "idPoligonoDichiarato")
    public DocumentazioneRichiestaDto fromModel(DocumentazioneRichiestaModificaSuoloModel documentazioneModel);
}
