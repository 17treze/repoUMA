package it.tndigitale.a4gistruttoria.service.builder;

import java.util.Optional;

import it.tndigitale.a4gistruttoria.dto.istruttoria.SintesiElencoLiquidazione;
import it.tndigitale.a4gistruttoria.repository.model.ElencoLiquidazioneModel;
import it.tndigitale.a4gistruttoria.util.LocalDateConverter;

public class ElencoLiquidazioneBuilder {

	public static SintesiElencoLiquidazione sintesiFrom(ElencoLiquidazioneModel elencoModel) {
		return Optional.ofNullable(elencoModel).map(e -> new SintesiElencoLiquidazione()
				.setId(e.getId())
				.setCodElenco(e.getCodElenco())
				.setDtCreazione(e.getDtCreazione()))
			.orElse(null);
	}
}
