package it.tndigitale.a4gistruttoria.service.builder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gistruttoria.dto.istruttoria.IstruttoriaDomandaUnica;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;

public class IstruttoriaBuilder {
	
	public static IstruttoriaDomandaUnica from(IstruttoriaModel istruttoria) {
		return Optional.ofNullable(istruttoria).map(i -> new IstruttoriaDomandaUnica().setId(i.getId())
				.setDomanda(DomandaUnicaBuilder.sintesiFrom(i.getDomandaUnicaModel()))
				.setElencoLiquidazione(ElencoLiquidazioneBuilder.sintesiFrom(i.getElencoLiquidazione()))
				.setSostegno(i.getSostegno())
				.setStato(i.getStato())
				.setTipo(i.getTipologia())
				.setIsBloccata(i.getBloccataBool())
				.setIsErroreCalcolo(i.getErroreCalcolo())
				.setDataUltimoCalcolo(i.getDataUltimoCalcolo()))
			.orElse(null);
	}

    public static RisultatiPaginati<IstruttoriaDomandaUnica> from(Page<IstruttoriaModel> page) {
        List<IstruttoriaDomandaUnica> risultati = page.stream()
        		.map(i -> from(i))
                .collect(Collectors.toList());
        return RisultatiPaginati.of(risultati, page.getTotalElements());
    }

    public static List<IstruttoriaDomandaUnica> from(List<IstruttoriaModel> istruttorie) {
        return istruttorie.stream()
        		.map(i -> from(i))
                .collect(Collectors.toList());
    }
}
