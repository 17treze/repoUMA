package it.tndigitale.a4g.fascicolo.antimafia;

public enum StatoDichiarazioneEnum {
	BOZZA("BOZZA"),
	FIRMATA("FIRMATA"),
	PROTOCOLLATA("PROTOCOLLATA"),
	RETTIFICATA("RETTIFICATA"),
	CONTROLLO_MANUALE("CONTROLLO_MANUALE"),
	RIFIUTATA("RIFIUTATA"),
	CONTROLLATA("CONTROLLATA"),
	POSITIVO("POSITIVO"),
	VERIFICA_PERIODICA("VERIFICA_PERIODICA"),
	CHIUSA("CHIUSA");
	
	private String identificativoStato;
	
	private StatoDichiarazioneEnum(final String identificativoStato) {
		this.identificativoStato = identificativoStato;
    }

	public String getIdentificativoStato() {
		return identificativoStato;
	}

}
