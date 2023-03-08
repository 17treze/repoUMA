package it.tndigitale.a4gistruttoria.util;

public enum StatoDomandaIntegrativa {

	SALVATA("SALVATA"), PRESENTATA("PRESENTATA"), CALCOLATO("CALCOLATO");

	public String statoDomandaIntergrativa;

	public String getStatoDomandaIntergrativa() {
		return statoDomandaIntergrativa;
	}

	public void setStatoDomandaIntergrativa(String statoDomandaIntergrativa) {
		this.statoDomandaIntergrativa = statoDomandaIntergrativa;
	}

	private StatoDomandaIntegrativa(String statoDomandaIntergrativa) {
		this.statoDomandaIntergrativa = statoDomandaIntergrativa;
	}
}
