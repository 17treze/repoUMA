package it.tndigitale.a4gistruttoria.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.TipoDettaglioDomanda;

@Component
public class DatiDomandaStrategy {

	@Autowired
	private DomandaLavorazioneSostegno domandaLavSostegno;
	
	@Autowired
	private DomandaDatiErede domandaDatiErede;

	public DatiDomanda getDatiDomanda(String datiDettaglioDomanda) {

		TipoDettaglioDomanda tipo = TipoDettaglioDomanda.valueOf(datiDettaglioDomanda);

		switch (tipo) {
		case LAVORAZIONE_SOSTEGNO:
			return domandaLavSostegno;
		case EREDE:
			return domandaDatiErede;
		default:
			return null;
		}
	}
}