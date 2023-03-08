package it.tndigitale.a4gistruttoria.component.dis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.ConfigurazioneIstruttoriaDisaccoppiatoModel;
import it.tndigitale.a4gistruttoria.repository.model.ConfigurazioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.DatiIstruttoreDisModel;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.CalcoloSostegnoException;
import it.tndigitale.a4gistruttoria.service.loader.ConfigurazioneIstruttoriaDisaccoppiatoLoader;
import it.tndigitale.a4gistruttoria.service.loader.ConfigurazioneIstruttoriaLoader;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@Component
public class InitVariabiliInputControlliFinaliDisaccoppiatoConsumer {

	@Autowired
	private CalcoloImportoErogatoDisaccoppiatoComponent calcoloImportoErogatoACSComponent;
	@Autowired
	private CalcolaGiorniRitardoComponent calcoloRitardoComp;
	@Autowired
	private ConfigurazioneIstruttoriaDisaccoppiatoLoader loader;
	@Autowired
	private ConfigurazioneIstruttoriaLoader loaderConfigurazioneIstruttoria;

	
	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliInputControlliFinaliDisaccoppiatoConsumer.class);



	public List<VariabileCalcolo> initInputControlliFinali(DatiElaborazioneIstruttoria datiElaborazioneIntermedi) throws CalcoloSostegnoException {
		final List<VariabileCalcolo> inputListaVariabiliCalcolo = new ArrayList<>();
		IstruttoriaModel istruttoria = datiElaborazioneIntermedi.getTransizione().getIstruttoria();
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();

		try {
			logger.debug("Carico gli importi gia' erogati per la domanda {}", domanda.getId());

			addListaVariabiliCalcolo(datiElaborazioneIntermedi, inputListaVariabiliCalcolo, TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSIMPAMM);
			addListaVariabiliCalcolo(datiElaborazioneIntermedi, inputListaVariabiliCalcolo, TipologiaPassoTransizione.GREENING, TipoVariabile.GREIMPAMM);
			addListaVariabiliCalcolo(datiElaborazioneIntermedi, inputListaVariabiliCalcolo, TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOIMPAMM);
			addListaVariabiliCalcolo(datiElaborazioneIntermedi, inputListaVariabiliCalcolo, TipologiaPassoTransizione.RIEPILOGO_SANZIONI, TipoVariabile.BPSIMPCALC);
			addListaVariabiliCalcolo(datiElaborazioneIntermedi, inputListaVariabiliCalcolo, TipologiaPassoTransizione.RIEPILOGO_SANZIONI, TipoVariabile.GREIMPCALC);
			addListaVariabiliCalcolo(datiElaborazioneIntermedi, inputListaVariabiliCalcolo, TipologiaPassoTransizione.RIEPILOGO_SANZIONI, TipoVariabile.GIOIMPCALC);
			DatiIstruttoreDisModel datiIstruttoria = istruttoria.getDatiIstruttoreDisModel();
			BigDecimal impSalari = (datiIstruttoria == null || datiIstruttoria.getImportoSalari() == null) ? BigDecimal.ZERO :  datiIstruttoria.getImportoSalari();
			inputListaVariabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.IMPSALARI, impSalari));

			Date dtProtocollazione = domanda.getDtProtocollazOriginaria() != null ? domanda.getDtProtocollazOriginaria() : domanda.getDtProtocollazione();
			inputListaVariabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.DTPROTDOM, dtProtocollazione));

			BigDecimal percrit = new BigDecimal(calcoloRitardoComp.calcolaGiorniLavorativiRitardo(domanda)).divide(new BigDecimal(100));

			// if (domanda.getAnnulloRiduzione() != null) {
			if (istruttoria.getSostegno().equals(Sostegno.DISACCOPPIATO) 
					&& istruttoria.getDatiIstruttoreDisModel() != null
					&& istruttoria.getDatiIstruttoreDisModel().getAnnulloRiduzione() != null) {
				inputListaVariabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.PERCRITISTR, istruttoria.getDatiIstruttoreDisModel().getAnnulloRiduzione()));
			}
			
			
			inputListaVariabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.PERCRIT, percrit));


			ConfigurazioneIstruttoriaDisaccoppiatoModel configurazioneIstruttoriaDisaccoppiato = loader.loadBy(domanda.getCampagna());
			inputListaVariabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN1, configurazioneIstruttoriaDisaccoppiato.getPercentualeRiduzioneLineareArt51Par2()));
			inputListaVariabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN2, configurazioneIstruttoriaDisaccoppiato.getPercentualeRiduzioneLineareArt51Par3()));
			inputListaVariabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN3, configurazioneIstruttoriaDisaccoppiato.getPercentualeRiduzioneLineareMassimaleNetto()));

			ConfigurazioneIstruttoriaModel configurazioneIstruttoria = loaderConfigurazioneIstruttoria.loadBy(domanda.getCampagna());
			inputListaVariabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.PERCPAGAMENTO, configurazioneIstruttoria.getPercentualePagamento()));

			Map<TipoVariabile, BigDecimal> mappaImporti = 
					calcoloImportoErogatoACSComponent.calcolaSommePerIstruttoriePagate(domanda,
							TipoVariabile.BPSIMPCALCFIN,
							TipoVariabile.GREIMPCALCFIN,
							TipoVariabile.GIOIMPCALCFIN);
			
			inputListaVariabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.BPSIMPEROGATO, mappaImporti.getOrDefault(TipoVariabile.BPSIMPCALCFIN, BigDecimal.ZERO)));
			inputListaVariabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.GREIMPEROGATO, mappaImporti.getOrDefault(TipoVariabile.GREIMPCALCFIN, BigDecimal.ZERO)));
			inputListaVariabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.GIOIMPEROGATO, mappaImporti.getOrDefault(TipoVariabile.GIOIMPCALCFIN, BigDecimal.ZERO)));
			return inputListaVariabiliCalcolo;
		} catch (CalcoloSostegnoException cse) {
			logger.error("Errore caricando gli importi gia erogati per la domanda con id {}", domanda.getId());
			throw cse;
		} catch (Exception e) {
			return manageException(domanda, e);
		}
	}

	private List<VariabileCalcolo> manageException(DomandaUnicaModel domanda, Exception e) throws CalcoloSostegnoException {
		Throwable t = e.getCause();
		if (t instanceof CalcoloSostegnoException) {
			logger.error("Errore caricando gli importi gia erogati per la domanda con id {}", domanda.getId());
			throw (CalcoloSostegnoException)t;
		}
		String errorMessage = "Errore nel recupero dei dati di calcolo dell'anno precedente per la domanda " + domanda.getNumeroDomanda();
		logger.error(errorMessage, e);
		throw new CalcoloSostegnoException(errorMessage);
	}

	private void addListaVariabiliCalcolo(DatiElaborazioneIstruttoria datiElaborazioneIntermedi,
										  List<VariabileCalcolo> inputListaVariabiliCalcolo,
										  TipologiaPassoTransizione tipologiaPasso,
										  TipoVariabile tipoVariabile) {
		VariabileCalcolo variabileCalcolo = datiElaborazioneIntermedi.getVariabileOutput(tipologiaPasso, tipoVariabile);
		if (variabileCalcolo != null) {
			inputListaVariabiliCalcolo.add(variabileCalcolo);
		}
	}


}
