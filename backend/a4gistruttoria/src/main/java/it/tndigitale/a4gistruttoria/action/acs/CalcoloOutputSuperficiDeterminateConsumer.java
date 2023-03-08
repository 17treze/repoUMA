package it.tndigitale.a4gistruttoria.action.acs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.ParticellaColtura;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@Component
public class CalcoloOutputSuperficiDeterminateConsumer implements BiConsumer<CalcoloAccoppiatoHandler, IstruttoriaModel> {

	private static final Logger logger = LoggerFactory.getLogger(CalcoloOutputSuperficiDeterminateConsumer.class);

	@Override
	public void accept(CalcoloAccoppiatoHandler handler, IstruttoriaModel istruttoria) {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		logger.debug("accept: inizio per la domanda {}", domanda.getId());		
		MapVariabili variabiliOutput = handler.getVariabiliOutput();
		BigDecimal totaleSupDeterminata = BigDecimal.ZERO;
		BigDecimal superificieDeterminataMisura = null;

		superificieDeterminataMisura = calcoloSuperficieDeterminataMisura(handler, TipoVariabile.ACSSUPDETIST_M8,
				TipoVariabile.ACSPFSUPDET_M8, TipoVariabile.ACSSUPDET_M8);
		// se ho superficie determinata (o dall'istruttore o dal calcolo)
		if (superificieDeterminataMisura != null) {
			totaleSupDeterminata = totaleSupDeterminata.add(superificieDeterminataMisura);
			
		}
		superificieDeterminataMisura = calcoloSuperficieDeterminataMisura(handler, TipoVariabile.ACSSUPDETIST_M9,
				TipoVariabile.ACSPFSUPDET_M9, TipoVariabile.ACSSUPDET_M9);
		// se ho superficie determinata (o dall'istruttore o dal calcolo)
		if (superificieDeterminataMisura != null) {
			totaleSupDeterminata = totaleSupDeterminata.add(superificieDeterminataMisura);
			
		}
		superificieDeterminataMisura = calcoloSuperficieDeterminataMisura(handler, TipoVariabile.ACSSUPDETIST_M10,
				TipoVariabile.ACSPFSUPDET_M10, TipoVariabile.ACSSUPDET_M10);
		// se ho superficie determinata (o dall'istruttore o dal calcolo)
		if (superificieDeterminataMisura != null) {
			totaleSupDeterminata = totaleSupDeterminata.add(superificieDeterminataMisura);
			
		}
		superificieDeterminataMisura = calcoloSuperficieDeterminataMisura(handler, TipoVariabile.ACSSUPDETIST_M11,
				TipoVariabile.ACSPFSUPDET_M11, TipoVariabile.ACSSUPDET_M11);
		// se ho superficie determinata (o dall'istruttore o dal calcolo)
		if (superificieDeterminataMisura != null) {
			totaleSupDeterminata = totaleSupDeterminata.add(superificieDeterminataMisura);
			
		}
		superificieDeterminataMisura = calcoloSuperficieDeterminataMisura(handler, TipoVariabile.ACSSUPDETIST_M14,
				TipoVariabile.ACSPFSUPDET_M14, TipoVariabile.ACSSUPDET_M14);
		// se ho superficie determinata (o dall'istruttore o dal calcolo)
		if (superificieDeterminataMisura != null) {
			totaleSupDeterminata = totaleSupDeterminata.add(superificieDeterminataMisura);
			
		}
		superificieDeterminataMisura = calcoloSuperficieDeterminataMisura(handler, TipoVariabile.ACSSUPDETIST_M15,
				TipoVariabile.ACSPFSUPDET_M15, TipoVariabile.ACSSUPDET_M15);
		// se ho superficie determinata (o dall'istruttore o dal calcolo)
		if (superificieDeterminataMisura != null) {
			totaleSupDeterminata = totaleSupDeterminata.add(superificieDeterminataMisura);
			
		}
		superificieDeterminataMisura = calcoloSuperficieDeterminataMisura(handler, TipoVariabile.ACSSUPDETIST_M16,
				TipoVariabile.ACSPFSUPDET_M16, TipoVariabile.ACSSUPDET_M16);
		// se ho superficie determinata (o dall'istruttore o dal calcolo)
		if (superificieDeterminataMisura != null) {
			totaleSupDeterminata = totaleSupDeterminata.add(superificieDeterminataMisura);
			
		}
		superificieDeterminataMisura = calcoloSuperficieDeterminataMisura(handler, TipoVariabile.ACSSUPDETIST_M17,
				TipoVariabile.ACSPFSUPDET_M17, TipoVariabile.ACSSUPDET_M17);
		// se ho superficie determinata (o dall'istruttore o dal calcolo)
		if (superificieDeterminataMisura != null) {
			totaleSupDeterminata = totaleSupDeterminata.add(superificieDeterminataMisura);
			
		}
		variabiliOutput.add(new VariabileCalcolo(TipoVariabile.ACSSUPDETTOT, totaleSupDeterminata));
		logger.debug("accept: fine per la domanda {} con totale {}", domanda.getId(), totaleSupDeterminata);		
	}
	
	protected BigDecimal calcoloSuperficieDeterminataMisura(CalcoloAccoppiatoHandler handler, TipoVariabile tipoDatiIstruttore,
			TipoVariabile tipoDatiParticella, TipoVariabile tipoDatiSuperficieDeterminataMisura) {
		MapVariabili variabiliOutput = handler.getVariabiliOutput();
		MapVariabili variabiliInput = handler.getVariabiliInput();
		BigDecimal currSupRichiesta = null;
		
		// prendo la superficie determinata dall'istruttore
		VariabileCalcolo datiIstruttore = variabiliInput.get(tipoDatiIstruttore);
		if (datiIstruttore != null) {
			currSupRichiesta = datiIstruttore.getValNumber();
		}
		if (currSupRichiesta == null) { // se l'istruttore non ha determinato la superficie la calcolo
			VariabileCalcolo variabile = variabiliOutput.get(tipoDatiParticella);
			if (variabile != null) {
				Function<ParticellaColtura, BigDecimal> getSuperFicieDeterminataParticella = (p) -> {
					if (p.getValNum() != null) {
						return new BigDecimal(p.getValNum().toString());
					}
					return BigDecimal.ZERO;
				};
				ArrayList<ParticellaColtura> particelle = variabile.getValList();
				if (particelle != null && !particelle.isEmpty()) {
					currSupRichiesta = particelle.stream()
							.map(getSuperFicieDeterminataParticella)
							.reduce(BigDecimal.ZERO, BigDecimal::add);
				}
			}
		}
		// se ho superficie determinata (o dall'istruttore o dal calcolo)
		if (currSupRichiesta != null) {
			variabiliOutput.add(new VariabileCalcolo(tipoDatiSuperficieDeterminataMisura, currSupRichiesta));	
			
		}
		return currSupRichiesta;
	}

}
