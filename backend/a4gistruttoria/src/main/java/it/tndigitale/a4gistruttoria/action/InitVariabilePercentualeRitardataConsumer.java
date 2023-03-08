package it.tndigitale.a4gistruttoria.action;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import it.tndigitale.a4gistruttoria.repository.model.ConfigurazioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.service.loader.ConfigurazioneIstruttoriaLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.service.DomandeService;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class InitVariabilePercentualeRitardataConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {
	
	@Autowired
	DomandeService domandeService;
	
	@Autowired
	ConfigurazioneIstruttoriaLoader configurazioneIstruttoriaLoader;
	
	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		ConfigurazioneIstruttoriaModel configurazioneIstruttoriaModel = configurazioneIstruttoriaLoader.loadBy(domanda.getCampagna());
		LocalDate dataProtocollazioneDomanda =
				domandeService.getDataProtocollazioneDomanda(domanda).toLocalDate();
		BigDecimal giorniLavorativiRitardo = calcolaGiorniLavorativiRitardo(configurazioneIstruttoriaModel.getDtScadenzaDomandeIniziali(),
				dataProtocollazioneDomanda);
		BigDecimal percrit = giorniLavorativiRitardo.divide(new BigDecimal(100));
		VariabileCalcolo var = new VariabileCalcolo(TipoVariabile.PERCRIT, percrit);
		handler.getVariabiliInput().add(var);
		
		// if (domanda.getAnnulloRiduzione() != null) {
		if (istruttoria.getSostegno().equals(Sostegno.SUPERFICIE) 
				&& istruttoria.getDatiIstruttoreSuperficie() != null
				&& istruttoria.getDatiIstruttoreSuperficie().getAnnulloRiduzione() != null) {
			handler.getVariabiliInput().add(new VariabileCalcolo(TipoVariabile.PERCRITISTR, istruttoria.getDatiIstruttoreSuperficie().getAnnulloRiduzione()));
		}
		if (istruttoria.getSostegno().equals(Sostegno.ZOOTECNIA) 
				&& istruttoria.getDatiIstruttoreZootecnia() != null
				&& istruttoria.getDatiIstruttoreZootecnia().getAnnulloRiduzione() != null) {
			handler.getVariabiliInput().add(new VariabileCalcolo(TipoVariabile.PERCRITISTR, istruttoria.getDatiIstruttoreZootecnia().getAnnulloRiduzione()));
		}
	}

	protected BigDecimal calcolaGiorniLavorativiRitardo(LocalDate dataTerminePresentazione,
														LocalDate dataProtocollazioneDomanda) {

		// TODO: implementare gestione giorni lavorativi includendo festività?
		List<LocalDate> holidays = Collections.emptyList();
		long giorniLavorativi = 0;
		if (dataProtocollazioneDomanda.isAfter(dataTerminePresentazione)) {
			giorniLavorativi = Stream.iterate(dataTerminePresentazione, date -> date.plusDays(1))
					.limit(ChronoUnit.DAYS.between(dataTerminePresentazione, dataProtocollazioneDomanda))
					.filter(date -> date.getDayOfWeek() != DayOfWeek.SATURDAY
							&& date.getDayOfWeek() != DayOfWeek.SUNDAY)
					.filter(date -> !holidays.contains(date)).count();
		}
		return new BigDecimal(giorniLavorativi);
	}
}
