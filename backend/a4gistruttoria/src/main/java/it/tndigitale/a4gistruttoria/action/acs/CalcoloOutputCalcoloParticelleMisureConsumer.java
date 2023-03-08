package it.tndigitale.a4gistruttoria.action.acs;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import it.tndigitale.a4gistruttoria.dto.lavorazione.RichiestaSuperficiePerCalcoloDto;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.InfoIstruttoriaDomanda;
import it.tndigitale.a4gistruttoria.repository.dao.RichiestaSuperficieDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtRichiestaSuperficie;
import it.tndigitale.a4gistruttoria.service.DomandeService;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.RichiestaSuperficiePerCalcoloConverter;

@Component
public class CalcoloOutputCalcoloParticelleMisureConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputCalcoloParticelleMisureConsumer.class);

	@Autowired
	private RichiestaSuperficieDao daoRichiestaSuperficie;
	
	@Autowired
	private RichiestaSuperficiePerCalcoloConverter richiestaSuperficiePerCalcoloConverter;
	
	@Autowired
	private DomandeService domandeService;
	
	@Autowired
	private CalcoloSostegnoM8SoiaConsumer calcoloM8;
	
	@Autowired
	private CalcoloSostegnoM9GranoConsumer calcoloM9;
	
	@Autowired
	private CalcoloSostegnoM10ProteaginoseConsumer calcoloM10;
	
	@Autowired
	private CalcoloSostegnoM11LeguminoseConsumer calcoloM11;
	
	@Autowired
	private CalcoloSostegnoM14PomodoroConsumer calcoloM14;
	
	@Autowired
	private CalcoloSostegnoM15OlivoConsumer calcoloM15;
	
	@Autowired
	private CalcoloSostegnoM16OlivoPendenzaConsumer calcoloM16;
	
	@Autowired
	private CalcoloSostegnoM17OlivoQualitaConsumer calcoloM17;
	
	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		List<A4gtRichiestaSuperficie> richieste = daoRichiestaSuperficie.findByDomandaUnicaModel(domanda);
		handler.setInfoIstruttoriaDomanda(retrieveInfoIstruttoriaDomanda(domanda));
		richieste
			.stream()
			.map(richiestaSuperficiePerCalcoloConverter::convert)
			.forEach(item -> {
				eseguiCalcoloPerMisura(item, handler, CodiceInterventoAgs.SOIA::equals, calcoloM8);
				eseguiCalcoloPerMisura(item, handler, CodiceInterventoAgs.GDURO::equals, calcoloM9);
				eseguiCalcoloPerMisura(item, handler, CodiceInterventoAgs.CPROT::equals, calcoloM10);
				eseguiCalcoloPerMisura(item, handler, CodiceInterventoAgs.LEGUMIN::equals, calcoloM11);
				eseguiCalcoloPerMisura(item, handler, CodiceInterventoAgs.POMOD::equals, calcoloM14);
				eseguiCalcoloPerMisura(item, handler, CodiceInterventoAgs.OLIO::equals, calcoloM15);
				eseguiCalcoloPerMisura(item, handler, CodiceInterventoAgs.OLIVE_PEND75::equals, calcoloM16);
				eseguiCalcoloPerMisura(item, handler, CodiceInterventoAgs.OLIVE_DISC::equals, calcoloM17);
			});
	}

	protected InfoIstruttoriaDomanda retrieveInfoIstruttoriaDomanda(DomandaUnicaModel domanda) {
		try {
			return domandeService.recuperaInfoIstruttoriaDomanda(domanda);
		} catch (Exception e) {
			logger.error("Impossibile recuperare InfoIstruttoriaDomanda da AGS per la domanda numero".concat(domanda.getNumeroDomanda().toString()), e);
			throw new RuntimeException(e);
		}
	}


	protected static void eseguiCalcoloPerMisura(
			RichiestaSuperficiePerCalcoloDto richiestaSuperficie,
			CalcoloAccoppiatoHandler infoCalcolo,
			Predicate<CodiceInterventoAgs> filter,
			CalcoloSostegnoAccoppiatoSuperficieConsumer calcolo) {
		if (filter.test(richiestaSuperficie.getCodiceInterventoAgs())) {
			calcolo.accept(richiestaSuperficie, infoCalcolo);
		}
	}
}
