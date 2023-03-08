package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaAccoppiati;
import it.tndigitale.a4gistruttoria.dto.lavorazione.builder.DatiIstruttoriaAczBuilder;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("Bridu50ControlloAntimafia_ZOOTECNIA")
public class Bridu50ControlloAntimafiaZootecnia extends Bridu50ControlloAntimafia<DatiIstruttoriaAccoppiati> {

	@Override
	protected DatiIstruttoriaAccoppiati getA4gtDatiIstruttoriaPerCheckAntimafia(IstruttoriaModel istruttoria) {
		return DatiIstruttoriaAczBuilder.from(istruttoria.getDatiIstruttoreZootecnia());
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
