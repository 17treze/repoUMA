package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;

public class FascicoloChiusoException extends FascicoloStatoReadOnlyException {
	private static final long serialVersionUID = 2301792469799172295L;

	public FascicoloChiusoException() {
		super(StatoFascicoloEnum.CHIUSO);
	}
}
