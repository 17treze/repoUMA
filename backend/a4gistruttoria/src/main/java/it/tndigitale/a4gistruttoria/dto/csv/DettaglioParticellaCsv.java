package it.tndigitale.a4gistruttoria.dto.csv;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;

import it.tndigitale.a4gistruttoria.dto.DettaglioParticellaDto;

public abstract class DettaglioParticellaCsv {
	
	@JsonProperty("CODICE COMUNE CATASTALE")
	private String codiceComune;

	@JsonProperty("COMUNE CATASTALE")
	private String codiceComuneNazionale;

	@JsonProperty("FOGLIO")
	private Long foglio;

	@JsonProperty("PARTICELLA")
	private String particella;
	
	@JsonProperty("SUB")
	private String sub;
	
	@JsonProperty("CODICE COLTURA")
	private String codColtura;

	@JsonProperty("DESCRIZIONE COLTURA")
	private String descrizioneColtura;

	
	public String getCodiceComuneNazionale() {
		return this.codiceComuneNazionale;
	}

	public void setCodiceComuneNazionale(String codiceComuneNazionale) {
		this.codiceComuneNazionale = codiceComuneNazionale;
	}
	
	
	public String getCodiceComune() {
		return codiceComune;
	}

	public void setCodiceComune(String codiceComune) {
		this.codiceComune = codiceComune;
	}

	public Long getFoglio() {
		return foglio;
	}

	public void setFoglio(Long foglio) {
		this.foglio = foglio;
	}
	
	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getCodColtura() {
		return codColtura;
	}

	public void setCodColtura(String codColtura) {
		this.codColtura = codColtura;
	}

	public String getDescrizioneColtura() {
		return descrizioneColtura;
	}

	public void setDescrizioneColtura(String descrizioneColtura) {
		this.descrizioneColtura = descrizioneColtura;
	}
	
	public void buildFromDto(DettaglioParticellaDto dto) {
		setCodiceComune(dto.getInfoCatastali().getCodNazionale());
		setCodiceComuneNazionale(dto.getInfoCatastali().getComune());
		setFoglio(dto.getInfoCatastali().getFoglio());
		setParticella(dto.getInfoCatastali().getParticella());
		setSub(dto.getInfoCatastali().getSub());
		setCodColtura(dto.getCodiceColtura3());
		setDescrizioneColtura(dto.getDescrizioneColtura());
	}
	
	static Builder csvSchemaBuilder() {
		return CsvSchema.builder()
				.addColumn("CODICE COMUNE CATASTALE", CsvSchema.ColumnType.STRING)
				.addColumn("COMUNE CATASTALE", CsvSchema.ColumnType.STRING)
				.addColumn("FOGLIO", CsvSchema.ColumnType.STRING)
				.addColumn("PARTICELLA", CsvSchema.ColumnType.STRING)
				.addColumn("SUB", CsvSchema.ColumnType.STRING)
				.addColumn("CODICE COLTURA", CsvSchema.ColumnType.STRING)
				.addColumn("DESCRIZIONE COLTURA", CsvSchema.ColumnType.STRING);
	}
	
	static String boolToString(final Boolean boolVal) {
		if (boolVal == null) {
			return "";
		} else if (boolVal) {
			return "SI";
		} else {
			return "NO";
		}
	}
}
