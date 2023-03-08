package it.tndigitale.a4gistruttoria.action.acz;

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

import it.tndigitale.a4gistruttoria.component.acz.CalcoloImportoErogatoACZComponent;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class InitVariabiliImportiErogatiACZConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	@Autowired
	private CalcoloImportoErogatoACZComponent impEroComp;

	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliImportiErogatiACZConsumer.class);
	
	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		try {
			logger.debug("Carico gli importi gia' erogati per la domanda {}", domanda.getId());
			MapVariabili inputListaVariabiliCalcolo = handler.getVariabiliInput();
			Map<TipoVariabile, BigDecimal> mappaImporti = 
					impEroComp.calcolaSommePerIstruttoriePagate(domanda, 
							TipoVariabile.ACZIMPCALCTOT,
							TipoVariabile.ACZIMPCALC_310,
							TipoVariabile.ACZIMPCALC_311,
							TipoVariabile.ACZIMPCALC_313,
							TipoVariabile.ACZIMPCALC_322,
							TipoVariabile.ACZIMPCALC_315,
							TipoVariabile.ACZIMPCALC_316,
							TipoVariabile.ACZIMPCALC_318,
							TipoVariabile.ACZIMPCALC_320,
							TipoVariabile.ACZIMPCALC_321);
			
			inputListaVariabiliCalcolo.add(new VariabileCalcolo(TipoVariabile.ACZIMPEROTOT, mappaImporti.getOrDefault(TipoVariabile.ACZIMPCALCTOT, BigDecimal.ZERO)));
			addImportoErogatoMisura(mappaImporti,TipoVariabile.ACZIMPCALC_310, TipoVariabile.ACZIMPERO_310, inputListaVariabiliCalcolo);
			addImportoErogatoMisura(mappaImporti,TipoVariabile.ACZIMPCALC_311, TipoVariabile.ACZIMPERO_311, inputListaVariabiliCalcolo);
			addImportoErogatoMisura(mappaImporti,TipoVariabile.ACZIMPCALC_313, TipoVariabile.ACZIMPERO_313, inputListaVariabiliCalcolo);
			addImportoErogatoMisura(mappaImporti,TipoVariabile.ACZIMPCALC_322, TipoVariabile.ACZIMPERO_322, inputListaVariabiliCalcolo);
			addImportoErogatoMisura(mappaImporti,TipoVariabile.ACZIMPCALC_315, TipoVariabile.ACZIMPERO_315, inputListaVariabiliCalcolo);
			addImportoErogatoMisura(mappaImporti,TipoVariabile.ACZIMPCALC_316, TipoVariabile.ACZIMPERO_316, inputListaVariabiliCalcolo);
			addImportoErogatoMisura(mappaImporti,TipoVariabile.ACZIMPCALC_318, TipoVariabile.ACZIMPERO_318, inputListaVariabiliCalcolo);
			addImportoErogatoMisura(mappaImporti,TipoVariabile.ACZIMPCALC_320, TipoVariabile.ACZIMPERO_320, inputListaVariabiliCalcolo);
			addImportoErogatoMisura(mappaImporti,TipoVariabile.ACZIMPCALC_321, TipoVariabile.ACZIMPERO_321, inputListaVariabiliCalcolo);
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			logger.debug("Errore recuperando gli importi erogati per la domanda {}", domanda.getId(), e);
			throw new RuntimeException(e);
		}
	}
	
	protected void addImportoErogatoMisura(Map<TipoVariabile, BigDecimal> mappaImporti,
										   TipoVariabile tipoVariabileImporto,
										   TipoVariabile tipoVariabileErogato,
										   MapVariabili inputListaVariabiliCalcolo) {
		BigDecimal importo = mappaImporti.get(tipoVariabileImporto);
		if (importo != null) {
			inputListaVariabiliCalcolo.add(new VariabileCalcolo(tipoVariabileErogato, importo));
		}
	}
}
