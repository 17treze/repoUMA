package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

public class FascicoloInvalidConditionException extends Exception {
	private static final long serialVersionUID = 3525985286792984730L;

	public FascicoloInvalidConditionException(ControlliFascicoloAnagraficaCompletoEnum controllo) {
		super(controllo.name());
	}
	
	public FascicoloInvalidConditionException(String controlloName) {
		super(controlloName);
	}
}
