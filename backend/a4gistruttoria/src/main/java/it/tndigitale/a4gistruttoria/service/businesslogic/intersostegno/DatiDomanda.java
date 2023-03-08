package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import java.util.HashMap;
import java.util.Map;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

class DatiDomanda {
	
	private Map<Sostegno, DatiPremioSostegno> mappaSostegni =
			new HashMap<Sostegno, DatiDomanda.DatiPremioSostegno>();
		
	Map<Sostegno, DatiPremioSostegno> getMappaSostegni() {
		return mappaSostegni;
	}
	
	void addSostegno(Sostegno sostegno, DatiPremioSostegno datiPremio) {
		mappaSostegni.put(sostegno, datiPremio);
	}
	
	DatiPremioSostegno getPremioSostegno(Sostegno sostegno) {
		return mappaSostegni.get(sostegno);
	}
	
	void createAddPremioSostegno(Sostegno sostegno, StatoIstruttoria stato, Double premio) {
		DatiPremioSostegno datiPremio = 
				new DatiPremioSostegno()
					.withStato(stato)
					.withPremio(premio);
		mappaSostegni.put(sostegno, datiPremio);
	}

	static class DatiPremioSostegno {
		private StatoIstruttoria stato;
		private Double premio;
		StatoIstruttoria getStato() {
			return stato;
		}
		Double getPremio() {
			return premio;
		}
		void setStato(StatoIstruttoria stato) {
			this.stato = stato;
		}
		void setPremio(Double premio) {
			this.premio = premio;
		}
		DatiPremioSostegno withStato(StatoIstruttoria stato) {
			this.stato = stato;
			return this;
		}
		DatiPremioSostegno withPremio(Double premio) {
			this.premio = premio;
			return this;
		}
	}
}

