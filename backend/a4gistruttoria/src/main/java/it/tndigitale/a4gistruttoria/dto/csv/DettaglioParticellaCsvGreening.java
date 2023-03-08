package it.tndigitale.a4gistruttoria.dto.csv;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import it.tndigitale.a4gistruttoria.dto.DettaglioParticellaDto;

public class DettaglioParticellaCsvGreening extends DettaglioParticellaCsv {

	@JsonProperty("SUPERFICIE DETERMINATA")
	private Float superficieDeterminata;
	
	@JsonProperty("TIPO COLTURA")
	private String tipoColtura;

	@JsonProperty("TIPO SEMINATIVO")
	private String tipoSeminativo;

	@JsonProperty("COLTURA PRINCIPALE")
	private String colturaPrincipale;

	@JsonProperty("SECONDA COLTURA")
	private String secondaColtura;

	@JsonProperty("AZOTO FISSATRICE")
	private String azotoFissatrice;

	public Float getSuperficieDeterminata() {
		return superficieDeterminata;
	}

	public void setSuperficieDeterminata(Float supDeterminata) {
		this.superficieDeterminata = supDeterminata;
	}

	public String getTipoColtura() {
		return tipoColtura;
	}

	public void setTipoColtura(String tipoColtura) {
		this.tipoColtura = tipoColtura;
	}

	public String getTipoSeminativo() {
		return tipoSeminativo;
	}

	public void setTipoSeminativo(String tipoSeminativo) {
		this.tipoSeminativo = tipoSeminativo;
	}

	public String getColturaPrincipale() {
		return colturaPrincipale;
	}

	public void setColturaPrincipale(String colturaPrincipale) {
		this.colturaPrincipale = colturaPrincipale;
	}

	public String getSecondaColtura() {
		return secondaColtura;
	}

	public void setSecondaColtura(String secondaColtura) {
		this.secondaColtura = secondaColtura;
	}

	public String getAzotoFissatrice() {
		return azotoFissatrice;
	}

	public void setAzotoFissatrice(String azotoFissatrice) {
		this.azotoFissatrice = azotoFissatrice;
	}
	
	public static List<DettaglioParticellaCsvGreening> fromDto(List<DettaglioParticellaDto> dtos) {
		List<DettaglioParticellaCsvGreening> csvList = new ArrayList<>();
		for (DettaglioParticellaDto dto : dtos) {
			csvList.add(DettaglioParticellaCsvGreening.fromDto(dto));
		}
		return csvList;
	}
	
	public static DettaglioParticellaCsvGreening fromDto(DettaglioParticellaDto dto) {
		DettaglioParticellaCsvGreening csv = new DettaglioParticellaCsvGreening();
		csv.buildFromDto(dto);
		csv.setSuperficieDeterminata(dto.getVariabiliCalcoloParticella().getSuperficieDeterminata());
		csv.setTipoColtura(dto.getVariabiliCalcoloParticella().getTipoColtura());
		csv.setTipoSeminativo(dto.getVariabiliCalcoloParticella().getTipoSeminativo());
		csv.setColturaPrincipale(boolToString(dto.getVariabiliCalcoloParticella().getColturaPrincipale()));
		csv.setSecondaColtura(boolToString(dto.getVariabiliCalcoloParticella().getSecondaColtura()));
		csv.setAzotoFissatrice(boolToString(dto.getVariabiliCalcoloParticella().getAzotoFissatrice()));
		return csv;
	}
	
	public static CsvSchema getSchema() {
		return DettaglioParticellaCsv.csvSchemaBuilder()
				.addColumn("SUPERFICIE DETERMINATA", CsvSchema.ColumnType.NUMBER)
				.addColumn("TIPO COLTURA", CsvSchema.ColumnType.STRING)
				.addColumn("TIPO SEMINATIVO", CsvSchema.ColumnType.STRING)
				.addColumn("COLTURA PRINCIPALE", CsvSchema.ColumnType.STRING)
				.addColumn("SECONDA COLTURA", CsvSchema.ColumnType.STRING)
				.addColumn("AZOTO FISSATRICE", CsvSchema.ColumnType.STRING)
				.build().withColumnSeparator(';').withoutQuoteChar().withHeader();
	}
}
