package it.tndigitale.a4gistruttoria.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum StatoDichiarazioneEnum {
	BOZZA("BOZZA"),
	FIRMATA("FIRMATA"),
	PROTOCOLLATA("PROTOCOLLATA"),
	RETTIFICATA("RETTIFICATA"),
	CONTROLLO_MANUALE("CONTROLLO_MANUALE"),
	RIFIUTATA("RIFIUTATA"),
	CONTROLLATA("CONTROLLATA"),
	POSITIVO("POSITIVO"),
	NEGATIVO("NEGATIVO"),
	VERIFICA_PERIODICA("VERIFICA_PERIODICA"),
	CHIUSA("CHIUSA");
	
	private String identificativoStato;
	
	private StatoDichiarazioneEnum(final String identificativoStato) {
		this.identificativoStato = identificativoStato;
    }

	public String getIdentificativoStato() {
		return identificativoStato;
	}

	void setIdentificativoStato(String identificativoStato) {
		this.identificativoStato = identificativoStato;
	}
	 
	public static StatoDichiarazioneEnum findByIdentificativo(String identificativo) {
		if (identificativo != null) {
			return Arrays.asList(StatoDichiarazioneEnum.values())
						 .stream()
						 .filter(item -> item.getIdentificativoStato().equals(identificativo))
						 .findFirst()
						 .orElseGet(null);
		}
		return null;
	}


	public static String getStringFormatted() {
		return Arrays.stream(StatoDichiarazioneEnum.values())
					 .filter(p -> !p.equals(StatoDichiarazioneEnum.CHIUSA))
					 .map(StatoDichiarazioneEnum::getIdentificativoStato)
					 .collect(Collectors.joining("\",\"","[\"","\"]"));
	}


}
