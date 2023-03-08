package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.builder.DatiIstruttoriaBuilder;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;

@Component("Bridu50ControlloAntimafia_DISACCOPPIATO")
public class Bridu50ControlloAntimafiaDisaccoppiato extends Bridu50ControlloAntimafia<DatiIstruttoria> {

	@Override
	protected DatiIstruttoria getA4gtDatiIstruttoriaPerCheckAntimafia(IstruttoriaModel istruttoria) {
		return DatiIstruttoriaBuilder.from(istruttoria.getDatiIstruttoreDisModel());
	}

	@Override
	protected Function<DatiIstruttoria, Boolean> checkAntimafia() {
		return (datiIstruttoria) -> {
			if (datiIstruttoria != null) {
				return Boolean.TRUE.equals(datiIstruttoria.getControlloAntimafia());
			}
			return Boolean.FALSE;
		};
	}

}
