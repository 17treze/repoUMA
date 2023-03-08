package it.tndigitale.a4g.territorio.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filtro per ricercare le sezioni catastali; criteri opzionali; filtro generico in like")
public class SezioneCatastaleFilter {

	@Schema(required = false, example = "TN", description = "Sigla della provincia del comune amministrativo della sezione (tutto maiuscolo)")
	private String siglaProvincia;
	@Schema(required = false, example = "022", description = "Codice istat della provincia del comune amministrativo della sezione")
	private String istatProvincia;
	@Schema(required = false, example = "Andalo", description = "Parte della denominazione della sezione (si usa per contenuto, case insensitive)")
	private String denominazioneSezione;
	@Schema(required = false, example = "P630", description = "Codice esatto della sezione catastale (case sensitive)")
	private String codiceSezione;
	@Schema(required = false, example = "P630", description = "String generica usata per cercare sia per contenuto della denominazione della sezione che per codice")
	private String q;
	@Schema(required = false, example = "CAT", description = "Valori accettati AMM, CAT. Se AMM recupera comuni amministativi, se CAT recupera comuni catastali")
	private String tipoComune;

	public String getSiglaProvincia() {
		return siglaProvincia;
	}

	public void setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
	}

	public String getIstatProvincia() {
		return istatProvincia;
	}

	public void setIstatProvincia(String istatProvincia) {
		this.istatProvincia = istatProvincia;
	}

	public String getDenominazioneSezione() {
		return denominazioneSezione;
	}

	public void setDenominazioneSezione(String denominazioneSezione) {
		this.denominazioneSezione = denominazioneSezione;
	}

	public String getCodiceSezione() {
		return codiceSezione;
	}

	public void setCodiceSezione(String codiceSezione) {
		this.codiceSezione = codiceSezione;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getTipoComune() {
		return tipoComune;
	}

	public void setTipoComune(String tipoComune) {
		this.tipoComune = tipoComune;
	}

}
