package it.tndigitale.a4gistruttoria.service.builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gistruttoria.dto.domandaunica.SintesiDomandaUnica;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.util.LocalDateConverter;

public class DomandaUnicaBuilder {
	
	public static SintesiDomandaUnica sintesiFrom(DomandaUnicaModel dModel) {
		return new SintesiDomandaUnica().setCampagna(dModel.getCampagna())
				.setCodEnteCompilatore(Optional.ofNullable(dModel.getCodEnteCompilatore()).map(BigDecimal::longValue).orElse(null))
				.setCodModuloDomanda(dModel.getCodModuloDomanda())
				.setCuaaIntestatario(dModel.getCuaaIntestatario())
				.setDescEnteCompilatore(dModel.getDescEnteCompilatore())
				.setDescModuloDomanda(dModel.getDescModuloDomanda())
				.setDtPresentazione(dModel.getDtPresentazione())
				.setDtPresentazOriginaria(LocalDateConverter.fromDateTime(dModel.getDtPresentazOriginaria()))
				.setDtProtocollazione(LocalDateConverter.fromDateTime(dModel.getDtProtocollazione()))
				.setDtProtocollazOriginaria(LocalDateConverter.fromDateTime(dModel.getDtPresentazOriginaria()))
				.setId(dModel.getId())
				.setNumDomandaRettificata(Optional.ofNullable(dModel.getNumDomandaRettificata()).map(BigDecimal::longValue).orElse(null))
				.setNumeroDomanda(Optional.ofNullable(dModel.getNumeroDomanda()).map(BigDecimal::longValue).orElse(null))
				.setRagioneSociale(dModel.getRagioneSociale())
				.setStato(dModel.getStato());
	}
	
	public static RisultatiPaginati<SintesiDomandaUnica> sintesiFrom(Page<DomandaUnicaModel> page) {
        List<SintesiDomandaUnica> risultati = page.stream()
        		.map(i -> sintesiFrom(i))
                .collect(Collectors.toList());
        return RisultatiPaginati.of(risultati, page.getTotalElements());
    }
	
	/*
	 * public static DomandaUnica from(DomandaUnicaModel domandaUnica) { return
	 * Optional.ofNullable(domandaUnica).map(i -> new
	 * DomandaUnica().setId(i.getId())
	 * .setDomanda(DomandaUnicaBuilder.sintesiFrom(i.getDomandaUnica()))
	 * .setElencoLiquidazione(ElencoLiquidazioneBuilder.sintesiFrom(i.
	 * getElencoLiquidazione())) .setSostegno(i.getSostegno())
	 * .setStato(i.getStato()) .setTipo(i.getTipologia())
	 * .setIsBloccata(i.getBloccataBool()) .setIsErroreCalcolo(i.getErroreCalcolo())
	 * .setDataUltimoCalcolo(i.getDataUltimoCalcolo())) .orElse(null); }
	 * 
	 * public static RisultatiPaginati<DomandaUnica> from(Page<DomandaUnicaModel>
	 * page) { List<DomandaUnica> risultati = page.stream() .map(i -> from(i))
	 * .collect(Collectors.toList()); return RisultatiPaginati.of(risultati,
	 * page.getTotalElements()); }
	 */
}
