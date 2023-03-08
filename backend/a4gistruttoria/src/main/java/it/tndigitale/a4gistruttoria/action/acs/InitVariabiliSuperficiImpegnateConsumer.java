package it.tndigitale.a4gistruttoria.action.acs;

import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.RichiestaSuperficieDao;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.ConversioniCalcoli;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.UnitaMisura;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.function.BiConsumer;

@Component
public class InitVariabiliSuperficiImpegnateConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	@Autowired
	private RichiestaSuperficieDao daoRichiestaSuperficie;

	private static final Logger logger = LoggerFactory.getLogger(InitVariabiliSuperficiImpegnateConsumer.class);
	
	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istrttoria) {
		DomandaUnicaModel domanda = istrttoria.getDomandaUnicaModel();

		logger.debug("Carico le superfici richieste per la domanda {}", domanda.getId());
		MapVariabili inputListaVariabiliCalcolo = handler.getVariabiliInput();
		addSumSuperficieImpegnataMisuraRichiesta(inputListaVariabiliCalcolo, domanda, TipoVariabile.ACSSUPIMP_M8,
				CodiceInterventoAgs.SOIA);
		addSumSuperficieImpegnataMisuraRichiesta(inputListaVariabiliCalcolo, domanda, TipoVariabile.ACSSUPIMP_M9,
				CodiceInterventoAgs.GDURO);
		addSumSuperficieImpegnataMisuraRichiesta(inputListaVariabiliCalcolo, domanda, TipoVariabile.ACSSUPIMP_M10,
				CodiceInterventoAgs.CPROT);
		addSumSuperficieImpegnataMisuraRichiesta(inputListaVariabiliCalcolo, domanda, TipoVariabile.ACSSUPIMP_M11,
				CodiceInterventoAgs.LEGUMIN);
		addSumSuperficieImpegnataMisuraRichiesta(inputListaVariabiliCalcolo, domanda, TipoVariabile.ACSSUPIMP_M14,
				CodiceInterventoAgs.POMOD);
		addSumSuperficieImpegnataMisuraRichiesta(inputListaVariabiliCalcolo, domanda, TipoVariabile.ACSSUPIMP_M15,
				CodiceInterventoAgs.OLIO);
		addSumSuperficieImpegnataMisuraRichiesta(inputListaVariabiliCalcolo, domanda, TipoVariabile.ACSSUPIMP_M16,
				CodiceInterventoAgs.OLIVE_PEND75);
		addSumSuperficieImpegnataMisuraRichiesta(inputListaVariabiliCalcolo, domanda, TipoVariabile.ACSSUPIMP_M17,
				CodiceInterventoAgs.OLIVE_DISC);
	}
	
	protected void addSumSuperficieImpegnataMisuraRichiesta(MapVariabili inputListaVariabiliCalcolo,
                                                            DomandaUnicaModel domanda, TipoVariabile tipoVariabile, CodiceInterventoAgs tipoIntervento) {
		BigDecimal totale = getSumSuperficieImpegnataByMisura(domanda, tipoIntervento);
		if (totale != null) {
			if (UnitaMisura.ETTARI.equals(tipoVariabile.getUnitaMisura())) {
				totale = ConversioniCalcoli.convertiMetriQuadriInEttaro(totale);
			}
			inputListaVariabiliCalcolo.add(new VariabileCalcolo(tipoVariabile, totale));			
		}
	}
	
	protected BigDecimal getSumSuperficieImpegnataByMisura(DomandaUnicaModel domanda,
                                                           CodiceInterventoAgs codiceInterventoAgs) {
		BigDecimal result = daoRichiestaSuperficie.sumSuperficieRichiestaNetta(domanda.getId(), codiceInterventoAgs);
		logger.debug("Per la domanda {} la superficie richiesta in ettari per l'intervento {} = {}", domanda.getId(), codiceInterventoAgs, result);
		return (result.compareTo(BigDecimal.ZERO) == 0) ? null : result;
	}
}
