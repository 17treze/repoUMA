package it.tndigitale.a4gistruttoria.strategy;

import java.util.Arrays;
import java.util.List;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.AccoppiatoSuperficie;

@Component
public class DatiASDichiarazioni extends DatiDichiarazioniAbstract {

	private final List<String> codiciDichiarazioni = 
			Arrays.asList("DUDICH_16", "DUDICH_17", "DUDICH_18", "DUDICH_19", "OLIVE_ENTE_DISCIP", "OLIVE_PUGLIA", "OLIVE_ENTE_BIOLOGICO");

	@Override
	protected List<String> getCodiciDichiarazioni() {
		return codiciDichiarazioni;
	}

	public void recupera(AccoppiatoSuperficie accoppiatoSuperficie) {
		DomandaUnicaModel domanda = caricaDomanda(accoppiatoSuperficie.getIdDomanda());
		accoppiatoSuperficie.setDichiarazioni(recuperaDichiarazioni(domanda));
	}
}
