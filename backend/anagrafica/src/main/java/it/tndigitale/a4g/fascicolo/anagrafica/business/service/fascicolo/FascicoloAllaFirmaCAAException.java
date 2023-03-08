package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;

public class FascicoloAllaFirmaCAAException extends FascicoloStatoReadOnlyException {
	private static final long serialVersionUID = -1060188528904709580L;

	public FascicoloAllaFirmaCAAException() {
		super(StatoFascicoloEnum.ALLA_FIRMA_CAA);
	}
}
