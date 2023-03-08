package it.tndigitale.a4gistruttoria.component.dis;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import it.tndigitale.a4gistruttoria.service.businesslogic.configurazioneistruttoria.ConfigurazioneIstruttoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.CalcoloSostegnoException;
import it.tndigitale.a4gistruttoria.service.loader.ConfigurazioneIstruttoriaLoader;
import it.tndigitale.a4gistruttoria.util.DateSupport;

@Component
public class CalcolaGiorniRitardoComponent {

	@Autowired
	private ConfigurazioneIstruttoriaLoader loaderConfigurazioneIstruttoria;

	@Autowired
	private ConfigurazioneIstruttoriaService confIstruttoriaService;

	/**
	 * Metodo che ritorna il numero di giorni lavorativi intercorsi tra la data di protocollazione della domanda e la data di termine presentazione impostata per il settore di appartenenza. N.B. Nel
	 * caso in cui una domanda sia di Ritiro Parziale, il metodo ritorna il numero di giorni intercorsi tra la data di protocollazione della domanda originaria e la data di termine presentazione
	 * impostata per il settore di appartenenza.
	 * 
	 * @param domanda
	 * @return
	 * @throws CalcoloSostegnoException
	 */
	public int calcolaGiorniLavorativiRitardo(DomandaUnicaModel domanda) throws CalcoloSostegnoException {

		Date dtTerminePresDomandeIniziali = calcoloDataScadenzaDomandeIniziali(domanda.getCampagna());
		int numeMaxGiorniRitardo = calcolaMaxGiorniRitardo(domanda.getCampagna());

		Date dtProtocollazioneDomandaIniziale = new Date();
		if (domanda.getCodModuloDomanda().equals("BPS_" + domanda.getCampagna())) {
			// è una domanda iniziale
			dtProtocollazioneDomandaIniziale = domanda.getDtProtocollazione();
		} else {
			//è una domanda di modifica o di ritiro parziale
			dtProtocollazioneDomandaIniziale = domanda.getDtProtocollazOriginaria();
		}

		int numGiorniRitardo = DateSupport.calcolaGiorniLavorativi(dtTerminePresDomandeIniziali, dtProtocollazioneDomandaIniziale);

		// se è una domanda di modifica
		if (domanda.getCodModuloDomanda().equals("BPS_ART_15_" + domanda.getCampagna())) {
			Date dtTerminePresDomandeModifica = calcoloDataScadenzaDomandeModifica(domanda.getCampagna());
			numGiorniRitardo += DateSupport.calcolaGiorniLavorativi(dtTerminePresDomandeModifica, domanda.getDtProtocollazione());
		}

		// se è una domanda di ritiro parziale
		if (domanda.getCodModuloDomanda().equals("BPS_RITPRZ_" + domanda.getCampagna())) {
			Date dtTerminePresDomandeModifica = calcoloDataScadenzaDomandeModifica(domanda.getCampagna());
			numGiorniRitardo += DateSupport.calcolaGiorniLavorativi(dtTerminePresDomandeModifica, domanda.getDtProtocollazioneUltimaModifica());
		}

		if (numGiorniRitardo < numeMaxGiorniRitardo) {
			return numGiorniRitardo;
		}
		else {
			return numeMaxGiorniRitardo;
		}
	}

	private Integer calcolaMaxGiorniRitardo (Integer annoCampagna) {
		Date dtTerminePresDomandeIniziali = calcoloDataScadenzaDomandeIniziali(annoCampagna);
		Date dtTerminePresDomandeInizialiPrimoGiornoLavorativo = setFirstWorkDate(dtTerminePresDomandeIniziali);
		Date dtScadenzaDomandeInizialiRitardo = dataScadenzaDomandeInizialiRitardoRicevibilita(annoCampagna);
		return DateSupport.calcolaGiorniLavorativi(dtTerminePresDomandeInizialiPrimoGiornoLavorativo, dtScadenzaDomandeInizialiRitardo);
	}

	private Date setFirstWorkDate(Date dateToCheck) {
		Calendar start = Calendar.getInstance();
		start.setTime(dateToCheck);
		int day = start.get(Calendar.DAY_OF_WEEK);
		while ((day == Calendar.SATURDAY) || (day == Calendar.SUNDAY)) {
			start.add(Calendar.DATE, 1);
			day = start.get(Calendar.DAY_OF_WEEK);
		}
		return start.getTime();
	}

	private Date calcoloDataScadenzaDomandeIniziali(Integer annoCampagna) {
		LocalDate dataScadenza =  loaderConfigurazioneIstruttoria.loadBy(annoCampagna).getDtScadenzaDomandeIniziali();
		return DateSupport.convertToDateViaInstant(dataScadenza);
	}

	private Date dataScadenzaDomandeInizialiRitardoRicevibilita(Integer annoCampagna) {
		LocalDate dataScadenza =  confIstruttoriaService.getConfIstruttoriaRicevibilita(annoCampagna).getDataScadenzaDomandaInizialeInRitardo();
		return DateSupport.convertToDateViaInstant(dataScadenza);
	}


	private Date calcoloDataScadenzaDomandeModifica(Integer annoCampagna) {
		LocalDate dataScadenza =  loaderConfigurazioneIstruttoria.loadBy(annoCampagna).getDtScadenzaDomandeModifica();
		return DateSupport.convertToDateViaInstant(dataScadenza);
	}
}
