package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;

public class FascicoloStatoReadOnlyException extends Exception {
	private static final long serialVersionUID = 314995271834527864L;
	
	public FascicoloStatoReadOnlyException(final StatoFascicoloEnum statoFascicolo) {
		super(String.format("Non e' possibile modificare un fascicolo in stato '%s'", statoFascicolo.name()));
	}
}
