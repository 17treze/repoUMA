package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaAccoppiati;
import it.tndigitale.a4gistruttoria.dto.lavorazione.builder.DatiIstruttoriaAcsBuilder;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("Bridu50ControlloAntimafia_SUPERFICIE")
public class Bridu50ControlloAntimafiaSuperficie extends Bridu50ControlloAntimafia<DatiIstruttoriaAccoppiati> {

	@Override
	protected DatiIstruttoriaAccoppiati getA4gtDatiIstruttoriaPerCheckAntimafia(IstruttoriaModel istruttoria) {
		return DatiIstruttoriaAcsBuilder.from(istruttoria.getDatiIstruttoreSuperficie());
	}

	@Override
	protected Function<DatiIstruttoriaAccoppiati, Boolean> checkAntimafia() {
		return (datiIstruttoria) -> {
			if (datiIstruttoria != null) {
				return Boolean.TRUE.equals(datiIstruttoria.getControlloAntimafia());
			}
			return Boolean.FALSE;
		};
	}

}
