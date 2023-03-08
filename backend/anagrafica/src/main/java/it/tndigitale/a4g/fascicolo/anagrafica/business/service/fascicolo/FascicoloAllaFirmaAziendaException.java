package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;

public class FascicoloAllaFirmaAziendaException extends FascicoloStatoReadOnlyException {
	private static final long serialVersionUID = -7261834123452724426L;
	
	public FascicoloAllaFirmaAziendaException() {
		super(StatoFascicoloEnum.ALLA_FIRMA_AZIENDA);
	}
}
