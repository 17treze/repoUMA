package it.tndigitale.a4gistruttoria.action.acs;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiConsumer;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.component.acs.CalcoloImportoErogatoACSComponent;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class InitVariabiliImportiErogatiACSConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	@Autowired
	private CalcoloImportoErogatoACSComponent impEroComp;

	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliImportiErogatiACSConsumer.class);
	
	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		try {
			logger.debug("Carico gli importi gia' erogati per la domanda {}", domanda.getId());
			MapVariabili inputListaVariabiliCalcolo = handler.getVariabiliInput();
			Map<TipoVariabile, BigDecimal> mappaImporti = 
					impEroComp.calcolaSommePerIstruttoriePagate(domanda, 
							TipoVariabile.ACSIMPCALCTOT,
							TipoVariabile.ACSIMPCALC_M8,
							TipoVariabile.ACSIMPCALC_M9,
							TipoVariabile.ACSIMPCALC_M10,
							TipoVariabile.ACSIMPCALC_M11,
							TipoVariabile.ACSIMPCALC_M14,
							TipoVariabile.ACSIMPCALC_M15,
							TipoVariabile.ACSIMPCALC_M16,
							TipoVariabile.ACSIMPCALC_M17);
			
			inputListaVariabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.ACSIMPEROTOT, mappaImporti.getOrDefault(TipoVariabile.ACSIMPCALCTOT, BigDecimal.ZERO)));
			addImportoErogatoMisura(mappaImporti,TipoVariabile.ACSIMPCALC_M8, TipoVariabile.ACSIMPERO_M8, inputListaVariabiliCalcolo);
			addImportoErogatoMisura(mappaImporti,TipoVariabile.ACSIMPCALC_M9, TipoVariabile.ACSIMPERO_M9, inputListaVariabiliCalcolo);
			addImportoErogatoMisura(mappaImporti,TipoVariabile.ACSIMPCALC_M10, TipoVariabile.ACSIMPERO_M10, inputListaVariabiliCalcolo);
			addImportoErogatoMisura(mappaImporti,TipoVariabile.ACSIMPCALC_M11, TipoVariabile.ACSIMPERO_M11, inputListaVariabiliCalcolo);
			addImportoErogatoMisura(mappaImporti,TipoVariabile.ACSIMPCALC_M14, TipoVariabile.ACSIMPERO_M14, inputListaVariabiliCalcolo);
			addImportoErogatoMisura(mappaImporti,TipoVariabile.ACSIMPCALC_M15, TipoVariabile.ACSIMPERO_M15, inputListaVariabiliCalcolo);
			addImportoErogatoMisura(mappaImporti,TipoVariabile.ACSIMPCALC_M16, TipoVariabile.ACSIMPERO_M16, inputListaVariabiliCalcolo);
			addImportoErogatoMisura(mappaImporti,TipoVariabile.ACSIMPCALC_M17, TipoVariabile.ACSIMPERO_M17, inputListaVariabiliCalcolo);
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			logger.debug("Errore recuperando gli importi erogati per la domanda {}", domanda.getId(), e);
			throw new RuntimeException(e);
		}
	}
	
	protected void addImportoErogatoMisura(Map<TipoVariabile, BigDecimal> mappaImporti,
			TipoVariabile tipoVariabileImporto, TipoVariabile tipoVariabileErogato, MapVariabili inputListaVariabiliCalcolo) {
		
		BigDecimal importo = mappaImporti.get(tipoVariabileImporto);
		if (importo != null) {
			inputListaVariabiliCalcolo.add(new VariabileCalcolo(tipoVariabileErogato, importo));
		}
	}
}
