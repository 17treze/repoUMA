package it.tndigitale.a4gistruttoria.util;

import it.tndigitale.a4gistruttoria.repository.model.A4gtDomandeCollegate;

public class AntimafiaDomandeCollegateHelper {
	private AntimafiaDomandeCollegateHelper() {}

	public static A4gtDomandeCollegate resetDomandaCollegata(A4gtDomandeCollegate a4gtDomandeCollegate) {
		a4gtDomandeCollegate.setDtInizioSilenzioAssenso(null);
		a4gtDomandeCollegate.setDtFineSilenzioAssenso(null);
		a4gtDomandeCollegate.setDtInizioEsitoNegativo(null);
		a4gtDomandeCollegate.setDtFineEsitoNegativo(null);
		a4gtDomandeCollegate.setDtBdna(null);
		a4gtDomandeCollegate.setProtocollo(null);
		a4gtDomandeCollegate.setDtBdnaOp(null);
		a4gtDomandeCollegate.setA4gtTrasmissioneBdna(null);
		return a4gtDomandeCollegate;
	}
} 