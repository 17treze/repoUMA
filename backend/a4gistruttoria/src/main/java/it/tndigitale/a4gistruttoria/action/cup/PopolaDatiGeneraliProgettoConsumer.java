package it.tndigitale.a4gistruttoria.action.cup;

import java.time.Year;
import java.util.function.BiConsumer;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.cup.dto.CUPGENERAZIONE;
import it.tndigitale.a4gistruttoria.cup.dto.DATIGENERALIPROGETTO;
import it.tndigitale.a4gistruttoria.cup.dto.ObjectFactory;
import it.tndigitale.a4gistruttoria.dto.cup.DatiCUP;

@Component
public class PopolaDatiGeneraliProgettoConsumer implements BiConsumer<CupHandler, CUPGENERAZIONE> {

	@Override
	public void accept(CupHandler handler, CUPGENERAZIONE generazione) {
		DatiCUP datiCupInput = handler.getDatiCUP();
		ObjectFactory of = new ObjectFactory();
		DATIGENERALIPROGETTO datiGeneraliProg = of.createDATIGENERALIPROGETTO();
		generazione.setDATIGENERALIPROGETTO(datiGeneraliProg);
		generazione.setIdProgetto(datiCupInput.getIdProgetto());
		datiGeneraliProg.setAnnoDecisione(Year.now().toString());
		datiGeneraliProg.setCumulativo("N");
		datiGeneraliProg.setCodificaLocale(datiCupInput.getIdProgetto());
		datiGeneraliProg.setNatura(datiCupInput.getNaturaCup());
		datiGeneraliProg.setTipologia(datiCupInput.getTipologiaCup());
		datiGeneraliProg.setSettore(datiCupInput.getSettoreCup());
		datiGeneraliProg.setSottosettore(datiCupInput.getSottosettoreCup());
		datiGeneraliProg.setCategoria(datiCupInput.getCategoriaCup());
	} 

}
