package it.tndigitale.a4gistruttoria.dto;

import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabiliParticellaColtura;

public class DettaglioParticellaDto {

	private String codiceColtura3;

	private String descrizioneColtura;

	private Particella infoCatastali;

	private VariabiliParticellaColtura variabiliCalcoloParticella;

	public DettaglioParticellaDto() {
		// costruttore vuoto
	}

	public String getCodiceColtura3() {
		return codiceColtura3;
	}

	public void setCodiceColtura3(String codiceColtura3) {
		this.codiceColtura3 = codiceColtura3;
	}

	public Particella getInfoCatastali() {
		return infoCatastali;
	}

	public void setInfoCatastali(Particella infoCatastali) {
		this.infoCatastali = infoCatastali;
	}

	public VariabiliParticellaColtura getVariabiliCalcoloParticella() {
		return variabiliCalcoloParticella;
	}

	public void setVariabiliCalcoloParticella(VariabiliParticellaColtura variabiliCalcoloParticella) {
		this.variabiliCalcoloParticella = variabiliCalcoloParticella;
	}

	public String getDescrizioneColtura() {
		return descrizioneColtura;
	}

	public void setDescrizioneColtura(String descrizioneColtura) {
		this.descrizioneColtura = descrizioneColtura;
	}
}
