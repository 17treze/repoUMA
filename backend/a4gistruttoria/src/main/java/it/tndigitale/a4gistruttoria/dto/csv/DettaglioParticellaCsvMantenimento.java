package it.tndigitale.a4gistruttoria.dto.csv;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;

import it.tndigitale.a4gistruttoria.dto.DettaglioParticellaDto;

public class DettaglioParticellaCsvMantenimento extends DettaglioParticellaCsv {
	
	@JsonProperty("PASCOLO")
	private String pascolo;

	@JsonProperty("SUPERFICIE IMPEGNATA")
	private Float superficieImpegnata;

	@JsonProperty("SUPERFICIE ELEGGIBILE")
	private Float superficieEleggibile;

	@JsonProperty("SUPERFICIE SIGECO")
	private Float superficieSigeco;

	@JsonProperty("ANOMALIE MANTENIMENTO")
	private String anomalieMantenimento;

	public Float getSuperficieImpegnata() {
		return superficieImpegnata;
	}

	public void setSuperficieImpegnata(Float supImpegnata) {
		this.superficieImpegnata = supImpegnata;
	}

	public Float getSuperficieEleggibile() {
		return superficieEleggibile;
	}

	public void setSuperficieEleggibile(Float supEleggibile) {
		this.superficieEleggibile = supEleggibile;
	}

	public Float getSuperficieSigeco() {
		return superficieSigeco;
	}

	public void setSuperficieSigeco(Float supSigeco) {
		this.superficieSigeco = supSigeco;
	}

	public String getAnomalieMantenimento() {
		return anomalieMantenimento;
	}

	public void setAnomalieMantenimento(String anomalieMantenimento) {
		this.anomalieMantenimento = anomalieMantenimento;
	}

	public String getPascolo() {
		return pascolo;
	}

	public void setPascolo(String pascolo) {
		this.pascolo = pascolo;
	}

	public static List<DettaglioParticellaCsvMantenimento> fromDto(List<DettaglioParticellaDto> dtos) {
		List<DettaglioParticellaCsvMantenimento> csvList = new ArrayList<>();
		for (DettaglioParticellaDto dto : dtos) {
			csvList.add(DettaglioParticellaCsvMantenimento.fromDto(dto));
		}
		return csvList;
	}
	
	public static DettaglioParticellaCsvMantenimento fromDto(DettaglioParticellaDto dto) {
		DettaglioParticellaCsvMantenimento csv = new DettaglioParticellaCsvMantenimento();
		csv.buildFromDto(dto);
		csv.setPascolo(dto.getVariabiliCalcoloParticella().getPascolo());
		csv.setSuperficieImpegnata(dto.getVariabiliCalcoloParticella().getSuperficieImpegnata());
		csv.setSuperficieEleggibile(dto.getVariabiliCalcoloParticella().getSuperficieEleggibile());
		csv.setSuperficieSigeco(dto.getVariabiliCalcoloParticella().getSuperficieSigeco());
		csv.setAnomalieMantenimento(boolToString(dto.getVariabiliCalcoloParticella().getAnomalieMantenimento()));
		return csv;
	}
	
	static Builder csvSchemaBuilder() {
		return DettaglioParticellaCsv.csvSchemaBuilder()
				.addColumn("PASCOLO", CsvSchema.ColumnType.NUMBER)
				.addColumn("SUPERFICIE IMPEGNATA", CsvSchema.ColumnType.NUMBER)
				.addColumn("SUPERFICIE ELEGGIBILE", CsvSchema.ColumnType.NUMBER)
				.addColumn("SUPERFICIE SIGECO", CsvSchema.ColumnType.NUMBER)
				.addColumn("ANOMALIE MANTENIMENTO", CsvSchema.ColumnType.STRING);
	}
	
	public static CsvSchema getSchema() {
		return csvSchemaBuilder().build().withColumnSeparator(';').withoutQuoteChar().withHeader();
	}
}
