package it.tndigitale.a4g.zootecnia.business.service;

public class FascicoloInvalidConditionException extends Exception {
	private static final long serialVersionUID = -4071954538953570364L;

	public FascicoloInvalidConditionException(ControlliFascicoloZootecniaCompletoEnum controllo) {
		super(controllo.name());
	}
}
