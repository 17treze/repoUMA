package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

public class TrasferimentoSIANNonCompletatoException extends Exception {
	private static final long serialVersionUID = -7261834123452724426L;
	
	public TrasferimentoSIANNonCompletatoException() {
		super("L'iter di trasferimento in SIAN non è ancora completato");
	}
}
