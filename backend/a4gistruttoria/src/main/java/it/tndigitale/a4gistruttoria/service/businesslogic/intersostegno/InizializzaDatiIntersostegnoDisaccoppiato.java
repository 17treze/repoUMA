package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno.DatiDomanda.DatiPremioSostegno;

@Component("InizializzaDatiIntersostegno_DISACCOPPIATO")
public class InizializzaDatiIntersostegnoDisaccoppiato implements InizializzaDatiIntersostegno {
	
	public DatiLavorazioneControlloIntersostegno trasforma(DatiDomanda datiInput) {
		DatiLavorazioneControlloIntersostegno datiOutput = new DatiLavorazioneControlloIntersostegno();
		// getSostegno interessato: disaccoppiato
		DatiPremioSostegno datiPremioSostegno = datiInput.getPremioSostegno(Sostegno.DISACCOPPIATO);
		datiOutput.setImportoSostegno(datiPremioSostegno.getPremio());
		// getSostegno 2: accoppiato zootecnia
		datiPremioSostegno = datiInput.getPremioSostegno(Sostegno.ZOOTECNIA);
		datiOutput.setHasSostegno2(datiPremioSostegno != null);
		if (datiPremioSostegno != null) {
			datiOutput.setImportoSostegno2(datiPremioSostegno.getPremio());
			datiOutput.setStatoLavorazioneSostegno2(datiPremioSostegno.getStato());
		}
		// getSostegno 3: accoppiato superficie
		datiPremioSostegno = datiInput.getPremioSostegno(Sostegno.SUPERFICIE);
		datiOutput.setHasSostegno3(datiPremioSostegno != null);
		if (datiPremioSostegno != null) {
			datiOutput.setImportoSostegno3(datiPremioSostegno.getPremio());
			datiOutput.setStatoLavorazioneSostegno3(datiPremioSostegno.getStato());
		}
		return datiOutput;
	}
}
