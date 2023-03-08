package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno.DatiDomanda.DatiPremioSostegno;

@Component("InizializzaDatiIntersostegno_ZOOTECNIA")
public class InizializzaDatiIntersostegnoACZ implements InizializzaDatiIntersostegno {
	
	public DatiLavorazioneControlloIntersostegno trasforma(DatiDomanda datiInput) {
		DatiLavorazioneControlloIntersostegno datiOutput = new DatiLavorazioneControlloIntersostegno();
		// getSostegno interessato: accoppiato zootecnia
		DatiPremioSostegno datiPremioSostegno = datiInput.getPremioSostegno(Sostegno.ZOOTECNIA);
		datiOutput.setImportoSostegno(datiPremioSostegno.getPremio());
		// getSostegno 2: accoppiato superficie
		datiPremioSostegno = datiInput.getPremioSostegno(Sostegno.SUPERFICIE);
		datiOutput.setHasSostegno2(datiPremioSostegno != null);
		if (datiPremioSostegno != null) {
			datiOutput.setImportoSostegno2(datiPremioSostegno.getPremio());
			datiOutput.setStatoLavorazioneSostegno2(datiPremioSostegno.getStato());
		}
		// getSostegno 3: disaccoppiato
		datiPremioSostegno = datiInput.getPremioSostegno(Sostegno.DISACCOPPIATO);
		datiOutput.setHasSostegno3(datiPremioSostegno != null);
		if (datiPremioSostegno != null) {
			datiOutput.setImportoSostegno3(datiPremioSostegno.getPremio());
			datiOutput.setStatoLavorazioneSostegno3(datiPremioSostegno.getStato());
		}
		return datiOutput;
	}
}
