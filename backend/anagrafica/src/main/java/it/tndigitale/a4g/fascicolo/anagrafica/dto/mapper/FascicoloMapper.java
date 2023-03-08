package it.tndigitale.a4g.fascicolo.anagrafica.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.FascicoloDto;

public final class FascicoloMapper {
	
	private FascicoloMapper() {}

	public static FascicoloDto fromFascicolo(FascicoloModel fascicoloModel)  {
		if (fascicoloModel == null) return null;
		var fascicoloDto = new FascicoloDto();
		fascicoloDto.setCuaa(fascicoloModel.getCuaa());
		fascicoloDto.setId(fascicoloModel.getId());
		fascicoloDto.setDenominazione(fascicoloModel.getDenominazione());
		fascicoloDto.setStato(fascicoloModel.getStato());
		fascicoloDto.setOrganismoPagatore(fascicoloModel.getOrganismoPagatore());
		fascicoloDto.setDataApertura(fascicoloModel.getDataApertura());
		fascicoloDto.setDataValidazione(fascicoloModel.getDataValidazione());
		fascicoloDto.setIdSchedaValidazione(fascicoloModel.getIdSchedaValidazione());
		fascicoloDto.setSchedaValidazione(fascicoloModel.getSchedaValidazione());
		fascicoloDto.setDataModifica(fascicoloModel.getDataModifica());
		return fascicoloDto;
	}
	
	public static List<FascicoloDto> from(Page<FascicoloModel> page) {
		return page.stream()
				.map(FascicoloMapper::fromFascicolo)
				.collect(Collectors.toList());
	}

}
