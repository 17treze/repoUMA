package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno.InizializzaDatiSostegno.InizializzaDatiSostegnoAccoppiatoSuperficie;
import it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno.InizializzaDatiSostegno.InizializzaDatiSostegnoAccoppiatoZootecnia;
import it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno.InizializzaDatiSostegno.InizializzaDatiSostegnoDisaccoppiato;

@Component
public class InizializzaDatiDomanda implements Function<DomandaUnicaModel, DatiDomanda> {

	static BiConsumer<DatiDomanda, DomandaUnicaModel> NOOP() {
	    return (dati, domanda) -> {};
	}

	@Autowired
	private InizializzaDatiSostegnoDisaccoppiato datiDisaccoppiato;
	@Autowired
	private InizializzaDatiSostegnoAccoppiatoSuperficie datiAccoppiatoSuperficie;
	@Autowired
	private InizializzaDatiSostegnoAccoppiatoZootecnia datiAccoppiatoZootecnia;
	
	@Override
	public DatiDomanda apply(DomandaUnicaModel domanda) {
		DatiDomanda result = new DatiDomanda();
		Stream<BiConsumer<DatiDomanda, DomandaUnicaModel>> inizializzatori = getInizializzatoriSostegno();
		inizializzatori.reduce(NOOP(), BiConsumer::andThen).accept(result, domanda);
		return result;
	}
	
	protected Stream<BiConsumer<DatiDomanda, DomandaUnicaModel>> getInizializzatoriSostegno() {
		return Stream.of(datiDisaccoppiato, datiAccoppiatoSuperficie, datiAccoppiatoZootecnia);
	}

}
