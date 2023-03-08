package it.tndigitale.a4gistruttoria.util;

import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;

public class VariabileValore {
	private static MapVariabili valoriParticellaColturaGreening = null;

	public static MapVariabili getValoriParticellaColturaGreening() {
		if (valoriParticellaColturaGreening == null) {
			valoriParticellaColturaGreening = new MapVariabili();
			valoriParticellaColturaGreening.add(TipoVariabile.PFSUPDETARB, getVariabile(TipoVariabile.PFSUPDETARB, "12%"));
			valoriParticellaColturaGreening.add(TipoVariabile.PFSUPDETPP, getVariabile(TipoVariabile.PFSUPDETPP, "13%"));
			valoriParticellaColturaGreening.add(TipoVariabile.PFSUPDETSEM, getVariabile(TipoVariabile.PFSUPDETSEM, "11%"));
			valoriParticellaColturaGreening.add(TipoVariabile.PFSUPDETERBAI, getVariabile(TipoVariabile.PFSUPDETERBAI, "112%"));
			valoriParticellaColturaGreening.add(TipoVariabile.PFSUPDETRIPOSO, getVariabile(TipoVariabile.PFSUPDETRIPOSO, "113%"));
			valoriParticellaColturaGreening.add(TipoVariabile.PFSUPDETSOMM, getVariabile(TipoVariabile.PFSUPDETSOMM, "114%"));
			valoriParticellaColturaGreening.add(TipoVariabile.PFSUPDETPRIMA, getVariabile(TipoVariabile.PFSUPDETPRIMA, "11%"));
			valoriParticellaColturaGreening.add(TipoVariabile.PFSUPDETSECONDA, getVariabile(TipoVariabile.PFSUPDETSECONDA, "11%"));
			valoriParticellaColturaGreening.add(TipoVariabile.PFAZOTO, getVariabile(TipoVariabile.PFAZOTO, "1111"));
		}
		return valoriParticellaColturaGreening;
	}

	private static VariabileCalcolo getVariabile(TipoVariabile tipoVariabile, String valore) {
		VariabileCalcolo value = new VariabileCalcolo(tipoVariabile);
		value.setValString(valore);
		return value;
	}
}
