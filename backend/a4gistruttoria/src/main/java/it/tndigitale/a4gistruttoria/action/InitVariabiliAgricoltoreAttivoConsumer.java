package it.tndigitale.a4gistruttoria.action;

import java.math.BigDecimal;
import java.util.function.BiConsumer;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.AgricoltoreSIAN;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.service.DomandeService;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class InitVariabiliAgricoltoreAttivoConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	@Autowired
	private DomandeService serviceDomande;

	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliAgricoltoreAttivoConsumer.class);
	
	public static final TipoVariabile INFOAGRATT = TipoVariabile.INFOAGRATT;
	public static final TipoVariabile AGRATT = TipoVariabile.AGRATT;

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		logger.debug("Carico le informazioni dell'agricoltore attivo dal sian per l'istruttoria {}", istruttoria.getId());
		AgricoltoreSIAN infoAgricoltoreAttivo = getInfoAgricoltoreSIAN(istruttoria);
		// Agricoltore Attivo
		if (infoAgricoltoreAttivo != null && infoAgricoltoreAttivo.getInfoAgricoltoreSIAN() != null
				&& infoAgricoltoreAttivo.getInfoAgricoltoreSIAN().getFlagAgriAtti() != null) {
			handler.getVariabiliInput().add(new VariabileCalcolo(INFOAGRATT, true));
			boolean isAgricoltoreAttivo = infoAgricoltoreAttivo.getInfoAgricoltoreSIAN().getFlagAgriAtti()
					.equals(new BigDecimal(1));
			handler.getVariabiliInput().add(new VariabileCalcolo(AGRATT, isAgricoltoreAttivo));
		} else {
			handler.getVariabiliInput().add(new VariabileCalcolo(INFOAGRATT, false));
		}
	}


	/**
	 * Metodo per il recupero da AGS delle informazioni di Istruttoria necessarie
	 * per il calcolo di premio
	 * 
	 * @param istruttoria
	 * @return
	 */
	protected AgricoltoreSIAN getInfoAgricoltoreSIAN(IstruttoriaModel istruttoria)  {
		try {
			return serviceDomande.recuperaInfoAgricoltoreSIAN(istruttoria.getDomandaUnicaModel().getNumeroDomanda());
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			logger.debug("Errore recuperando i dati agricoltore SIAN per l'istruttoria " + istruttoria.getId(), e);
			throw new RuntimeException(e);
		}
	}
}
