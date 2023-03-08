package it.tndigitale.a4gistruttoria.strategy;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.AccoppiatoZootecnia;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;

@Component
public class DatiAZDichiarazioni extends DatiDichiarazioniAbstract {

	private final List<String> codiciDichiarazioni = 
			Arrays.asList("DUDICH_16", "DUDICH_17", "DUDICH_18", "DUDICH_19", "ZOO_ENTE_QUALITA", "ZOO_ENTE_ETICH", "ZOO_AREE_MONT_6", "ZOO_AREE_MONT_5", "ZOO_AREE_MONT_4","ZOO_AREE_MONT_3");

	@Override
	protected List<String> getCodiciDichiarazioni() {
		return codiciDichiarazioni;
	}

	public void recupera(AccoppiatoZootecnia accoppiatoZootecnia) {
		DomandaUnicaModel domanda = caricaDomanda(accoppiatoZootecnia.getIdDomanda());
		accoppiatoZootecnia.setDichiarazioni(recuperaDichiarazioni(domanda));
	}
}
