package it.tndigitale.a4g.richiestamodificasuolo.dto.mapper;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.ComuneCatastaleDto;

@Mapper(componentModel = "spring")
public interface ComuneCatastaleMapper {

	@Mapping(source = "codSezione", target = "codice")
	@Mapping(source = "codSezione", target = "descrizione")
	public ComuneCatastaleDto fromSuoloDichiarato(SuoloDichiaratoModel suoloDichiaratoModel);

	default List<ComuneCatastaleDto> from(List<SuoloDichiaratoModel> list) {
		return list.stream().map(s -> fromSuoloDichiarato(s)).filter(distinctByCodSezione(ComuneCatastaleDto::getCodice)).collect(Collectors.toList());
	}

	default <T> Predicate<T> distinctByCodSezione(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
}
