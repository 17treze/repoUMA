package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

public class FascicoloMigrazioneException extends Exception {
	private static final long serialVersionUID = -1060188528904709580L;

	public FascicoloMigrazioneException(FascicoloMigrazioneEnum msg) {
		super(msg.name());
	}
}
