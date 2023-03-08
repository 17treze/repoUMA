package it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto;

public enum TitoloConduzioneAgs {
	PROPRIETA("PROPRIETA"),
	AFFITTO("AFFITTO"),
	MEZZADRIA("MEZZADRIA"),
	ALTRE_FORME("ALTRE FORME"),
	NON_CONDOTTA("NON CONDOTTA"),
	NON_DEFINITO("NON DEFINITO"),
	COMODATO("COMODATO");
	
	public final String value;

    private TitoloConduzioneAgs(String value) {
        this.value = value;
    }
}
