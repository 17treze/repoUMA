package it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo.exception;

public class FascicoloInvalidConditionException extends Exception {

	private static final long serialVersionUID = 3525985286792984730L;

	public FascicoloInvalidConditionException(String controllo) {
		super(controllo);
	}
}
