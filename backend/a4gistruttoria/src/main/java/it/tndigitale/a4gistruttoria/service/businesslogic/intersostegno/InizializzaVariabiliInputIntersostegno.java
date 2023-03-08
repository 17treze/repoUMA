package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiConsumer;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno.DatiDomanda.DatiPremioSostegno;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class InizializzaVariabiliInputIntersostegno implements BiConsumer<List<VariabileCalcolo>, DatiDomanda> {

	protected BiConsumer<List<VariabileCalcolo>, DatiDomanda> disaccoppiato() {
		return (variabiliCalcolo, dati) -> {
			DatiPremioSostegno datiPremio = dati.getPremioSostegno(Sostegno.DISACCOPPIATO);
			variabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.HASRICBPS, (datiPremio != null)));
			if (datiPremio != null) {
				variabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.STATOBPS, datiPremio.getStato().getStatoIstruttoria()));
				Double premio = datiPremio.getPremio();
				if (premio != null) {
					variabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.DISIMPCALC, BigDecimal.valueOf(datiPremio.getPremio())));
				}
			}		
		};
	}

	protected BiConsumer<List<VariabileCalcolo>, DatiDomanda> acs() {
		return (variabiliCalcolo, dati) -> {
			DatiPremioSostegno datiPremio = dati.getPremioSostegno(Sostegno.SUPERFICIE);
			variabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.HASRICACS, (datiPremio != null)));
			if (datiPremio != null) {
				variabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.STATOACS, datiPremio.getStato().getStatoIstruttoria()));
				Double premio = datiPremio.getPremio();
				if (premio != null) {
					variabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.ACSIMPCALC, BigDecimal.valueOf(datiPremio.getPremio())));
				}
			}		
		};
	}

	protected BiConsumer<List<VariabileCalcolo>, DatiDomanda> acz() {
		return (variabiliCalcolo, dati) -> {
			DatiPremioSostegno datiPremio = dati.getPremioSostegno(Sostegno.ZOOTECNIA);
			variabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.HASRICACZ, (datiPremio != null)));
			if (datiPremio != null) {
				variabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.STATOACZ, datiPremio.getStato().getStatoIstruttoria()));
				Double premio = datiPremio.getPremio();
				if (premio != null) {
					variabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.ACZIMPCALC, BigDecimal.valueOf(datiPremio.getPremio())));
				}
			}		
		};
	}

	protected void popolaVariabiliInput(List<VariabileCalcolo> variabiliCalcolo, DatiDomanda dati) {
		disaccoppiato().andThen(acs()).andThen(acz()).accept(variabiliCalcolo, dati);
	}

	@Override
	public void accept(List<VariabileCalcolo> variabili, DatiDomanda dati) {
		popolaVariabiliInput(variabili, dati);
	}
	
}
