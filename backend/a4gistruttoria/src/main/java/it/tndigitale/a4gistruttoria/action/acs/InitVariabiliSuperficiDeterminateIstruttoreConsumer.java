package it.tndigitale.a4gistruttoria.action.acs;

import java.math.BigDecimal;
import java.util.function.BiConsumer;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaACS;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.service.DatiIstruttoreService;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class InitVariabiliSuperficiDeterminateIstruttoreConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	@Autowired
	private DatiIstruttoreService datiIstruttoreService;
	
	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliSuperficiDeterminateIstruttoreConsumer.class);

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		logger.debug("Carico le superfici determinate per la domanda {}", domanda.getId());
		try {
			MapVariabili inputListaVariabiliCalcolo = handler.getVariabiliInput();
			DatiIstruttoriaACS datiIstruttoriaACS =
					datiIstruttoreService.getDatiIstruttoreSuperficie(istruttoria.getId());
			// valori da recuperare da dati inseriti in cruscotto dall'istruttore
			if (datiIstruttoriaACS != null) {
				addVariabileNotNullAndTransformMetri(inputListaVariabiliCalcolo, TipoVariabile.ACSSUPDETIST_M8, datiIstruttoriaACS.getSuperficieDeterminataSoiaM8());
				addVariabileNotNullAndTransformMetri(inputListaVariabiliCalcolo, TipoVariabile.ACSSUPDETIST_M9, datiIstruttoriaACS.getSuperficieDeterminataFrumentoM9());
				addVariabileNotNullAndTransformMetri(inputListaVariabiliCalcolo, TipoVariabile.ACSSUPDETIST_M10, datiIstruttoriaACS.getSuperficieDeterminataProteaginoseM10());
				addVariabileNotNullAndTransformMetri(inputListaVariabiliCalcolo, TipoVariabile.ACSSUPDETIST_M11, datiIstruttoriaACS.getSuperficieDeterminataLeguminoseM11());
				addVariabileNotNullAndTransformMetri(inputListaVariabiliCalcolo, TipoVariabile.ACSSUPDETIST_M14, datiIstruttoriaACS.getSuperficieDeterminataPomodoroM14());
				addVariabileNotNullAndTransformMetri(inputListaVariabiliCalcolo, TipoVariabile.ACSSUPDETIST_M15, datiIstruttoriaACS.getSuperficieDeterminataOlivoStandardM15());
				addVariabileNotNullAndTransformMetri(inputListaVariabiliCalcolo, TipoVariabile.ACSSUPDETIST_M16, datiIstruttoriaACS.getSuperficieDeterminataOlivoPendenzaM16());
				addVariabileNotNullAndTransformMetri(inputListaVariabiliCalcolo, TipoVariabile.ACSSUPDETIST_M17, datiIstruttoriaACS.getSuperficieDeterminataOlivoQualitaM17());
			}
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			logger.debug("Errore recuperando i dati di istruttoria acs per la domanda " + domanda.getId(), e);
			throw new RuntimeException(e);
		}
	}
	
	protected void addVariabileNotNullAndTransformMetri(MapVariabili inputListaVariabiliCalcolo, TipoVariabile tipoVaribile, BigDecimal supDetIstruttore) throws Exception {
		if (supDetIstruttore != null) {
			inputListaVariabiliCalcolo.add(new VariabileCalcolo(tipoVaribile, supDetIstruttore));
		}
	}
}
