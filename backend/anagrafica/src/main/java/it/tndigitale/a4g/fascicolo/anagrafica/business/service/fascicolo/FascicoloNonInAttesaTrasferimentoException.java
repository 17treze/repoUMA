package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;

public class FascicoloNonInAttesaTrasferimentoException extends Exception {
	private static final long serialVersionUID = -7261834123452724426L;
	
	public FascicoloNonInAttesaTrasferimentoException() {
		super(String.format("Il fascicolo non è nello stato '%s'", StatoFascicoloEnum.IN_ATTESA_TRASFERIMENTO));
	}
}
