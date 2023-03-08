package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import java.util.function.BiConsumer;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.CaricaPremioSostegno;
import it.tndigitale.a4gistruttoria.component.acs.CaricaPremioAccoppiatoSuperficie;
import it.tndigitale.a4gistruttoria.component.acz.CaricaPremioAccoppiatoZootecnia;
import it.tndigitale.a4gistruttoria.component.dis.CaricaPremioDisaccoppiato;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;

public abstract class InizializzaDatiSostegno implements BiConsumer<DatiDomanda, DomandaUnicaModel> {
	
	@Override
	public void accept(DatiDomanda dati, DomandaUnicaModel domanda) {
		popolaDati(dati, domanda);
	}

	@Autowired
	private IstruttoriaComponent istruttoriaComponent;
	
	public void popolaDati(DatiDomanda dati, DomandaUnicaModel domanda) {
		Sostegno sostegno = getSostegno();
		IstruttoriaModel istruttoria = istruttoriaComponent.getUltimaIstruttoria(domanda, sostegno);
		popolaDatiDaIstruttoria(dati, istruttoria);
	}
	
	public void popolaDatiDaIstruttoria(DatiDomanda dati, IstruttoriaModel istruttoria) {
		if (istruttoria != null) {
			dati.createAddPremioSostegno(getSostegno(),
					StatoIstruttoria.valueOfByIdentificativo(istruttoria.getA4gdStatoLavSostegno().getIdentificativo()),
					caricaPremio(istruttoria));
		}
	}
	
	
	protected Double caricaPremio(IstruttoriaModel istruttoria) {
		CaricaPremioSostegno caricatore = getCaricatorePremio();
		return caricatore.getPremio(istruttoria);
	}
	
	protected abstract Sostegno getSostegno();
	
	protected abstract CaricaPremioSostegno getCaricatorePremio();

	@Component
	public static class InizializzaDatiSostegnoDisaccoppiato extends InizializzaDatiSostegno {

		@Autowired
		private CaricaPremioDisaccoppiato caricaPremio;

		@Override
		protected Sostegno getSostegno() {
			return Sostegno.DISACCOPPIATO;
		}

		@Override
		protected CaricaPremioDisaccoppiato getCaricatorePremio() {
			return caricaPremio;
		}

	}

	@Component
	public static class InizializzaDatiSostegnoAccoppiatoSuperficie extends InizializzaDatiSostegno {
		
		@Autowired
		private CaricaPremioAccoppiatoSuperficie caricaPremio;

		@Override
		protected Sostegno getSostegno() {
			return Sostegno.SUPERFICIE;
		}

		@Override
		protected CaricaPremioAccoppiatoSuperficie getCaricatorePremio() {
			return caricaPremio;
		}
	}

	@Component
	public static class InizializzaDatiSostegnoAccoppiatoZootecnia extends InizializzaDatiSostegno {
		
		@Autowired
		private CaricaPremioAccoppiatoZootecnia caricaPremio;

		@Override
		protected Sostegno getSostegno() {
			return Sostegno.ZOOTECNIA;
		}

		@Override
		protected CaricaPremioAccoppiatoZootecnia getCaricatorePremio() {
			return caricaPremio;
		}

	}
}
