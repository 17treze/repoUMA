package it.tndigitale.a4gistruttoria.dto.csv;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import it.tndigitale.a4gistruttoria.dto.DettaglioParticellaDto;

public class DettaglioParticellaCsvEleggibilita extends DettaglioParticellaCsv {

	@JsonProperty("SUPERFICIE IMPEGNATA AL NETTO DELLE TARE")
	private Float superficieImpegnata;

	@JsonProperty("SUPERFICIE ELEGGIBILE")
	private Float superficieEleggibile;

	@JsonProperty("SUPERFICIE SIGECO")
	private Float superficieSigeco;

	@JsonProperty("SUPERFICIE DETERMINATA")
	private Float superficieDeterminata;
	
	@JsonProperty("SUPERFICIE SCOSTAMENTO")
	private Float superficieScostamento;

	@JsonProperty("ANOMALIE MANTENIMENTO")
	private String anomalieMantenimento;

	@JsonProperty("ANOMALIE COORDINAMENTO")
	private String anomalieCoordinamento;
	
	@JsonProperty("SUPERFICIE ANOMALIE COORDINAMENTO")
	private Float superficieAnomalieCoordinamento;

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

	public Float getSuperficieDeterminata() {
		return superficieDeterminata;
	}

	public void setSuperficieDeterminata(Float supDeterminata) {
		this.superficieDeterminata = supDeterminata;
	}

	public String getAnomalieMantenimento() {
		return anomalieMantenimento;
	}

	public void setAnomalieMantenimento(String anomalieMantenimento) {
		this.anomalieMantenimento = anomalieMantenimento;
	}

	public String getAnomalieCoordinamento() {
		return anomalieCoordinamento;
	}

	public void setAnomalieCoordinamento(String anomalieCoordinamento) {
		this.anomalieCoordinamento = anomalieCoordinamento;
	}

	public Float getSuperficieAnomalieCoordinamento() {
		return superficieAnomalieCoordinamento;
	}

	public void setSuperficieAnomalieCoordinamento(Float superficieAnomalieCoordinamento) {
		this.superficieAnomalieCoordinamento = superficieAnomalieCoordinamento;
	}

	public Float getSuperficieScostamento() {
		return superficieScostamento;
	}

	public void setSuperficieScostamento(Float superficieScostamento) {
		this.superficieScostamento = superficieScostamento;
	}
	
	public static List<DettaglioParticellaCsvEleggibilita> fromDto(List<DettaglioParticellaDto> dtos) {
		List<DettaglioParticellaCsvEleggibilita> csvList = new ArrayList<>();
		for (DettaglioParticellaDto dto : dtos) {
			csvList.add(DettaglioParticellaCsvEleggibilita.fromDto(dto));
		}
		return csvList;
	}
	
	public static DettaglioParticellaCsvEleggibilita fromDto(DettaglioParticellaDto dto) {
		DettaglioParticellaCsvEleggibilita csv = new DettaglioParticellaCsvEleggibilita();
		csv.buildFromDto(dto);
		csv.setSuperficieImpegnata(dto.getVariabiliCalcoloParticella().getSuperficieImpegnata());
		csv.setSuperficieEleggibile(dto.getVariabiliCalcoloParticella().getSuperficieEleggibile());
		csv.setSuperficieSigeco(dto.getVariabiliCalcoloParticella().getSuperficieSigeco());
		csv.setSuperficieDeterminata(dto.getVariabiliCalcoloParticella().getSuperficieDeterminata());
		csv.setSuperficieAnomalieCoordinamento(dto.getVariabiliCalcoloParticella().getSuperficieAnomalieCoordinamento());
		csv.setSuperficieScostamento(csv.getSuperficieImpegnata() - csv.getSuperficieDeterminata());
		csv.setAnomalieMantenimento(boolToString(dto.getVariabiliCalcoloParticella().getAnomalieMantenimento()));
		csv.setAnomalieCoordinamento(boolToString(dto.getVariabiliCalcoloParticella().getAnomalieCoordinamento()));
		return csv;
	}
	
	public static CsvSchema getSchema() {
		return DettaglioParticellaCsv.csvSchemaBuilder()
				.addColumn("SUPERFICIE IMPEGNATA AL NETTO DELLE TARE", CsvSchema.ColumnType.NUMBER)
				.addColumn("SUPERFICIE ELEGGIBILE", CsvSchema.ColumnType.NUMBER)
				.addColumn("SUPERFICIE SIGECO", CsvSchema.ColumnType.NUMBER)
				.addColumn("ANOMALIE MANTENIMENTO", CsvSchema.ColumnType.STRING)
				.addColumn("ANOMALIE COORDINAMENTO", CsvSchema.ColumnType.STRING)
				.addColumn("SUPERFICIE ANOMALIE COORDINAMENTO", CsvSchema.ColumnType.NUMBER)
				.addColumn("SUPERFICIE DETERMINATA", CsvSchema.ColumnType.NUMBER)
				.addColumn("SUPERFICIE SCOSTAMENTO", CsvSchema.ColumnType.NUMBER)
				.build().withColumnSeparator(';').withoutQuoteChar().withHeader();
	}
}
