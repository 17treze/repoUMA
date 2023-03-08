package it.tndigitale.a4gistruttoria.dto.zootecnia;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;


public class CapoDto {

	private String idCapo;
	private String marcaAuricolare;
	private String sesso;
	private LocalDate dataNascita;
	private String codiceRazza;
	private List<DetenzioneDto> detenzioni;

	public CapoDto() {}

	public CapoDto(JsonNode capoNode) {
		ZoneId systemDefault = ZoneId.systemDefault();

		codiceRazza = capoNode.get("razzaCodice").textValue();
		dataNascita = LocalDate.ofInstant(Instant.ofEpochMilli(capoNode.get("dtNascita").longValue()), systemDefault);
		idCapo = capoNode.get("capoId").asText();
		marcaAuricolare = capoNode.get("codice").asText();
		sesso = capoNode.get("sesso").asText();
	}

	public String getIdCapo() {
		return idCapo;
	}
	public CapoDto setIdCapo(String idCapo) {
		this.idCapo = idCapo;
		return this;
	}
	public String getMarcaAuricolare() {
		return marcaAuricolare;
	}
	public CapoDto setMarcaAuricolare(String marcaAuricolare) {
		this.marcaAuricolare = marcaAuricolare;
		return this;
	}
	public String getSesso() {
		return sesso;
	}
	public CapoDto setSesso(String sesso) {
		this.sesso = sesso;
		return this;
	}
	public LocalDate getDataNascita() {
		return dataNascita;
	}
	public CapoDto setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
		return this;
	}
	public String getCodiceRazza() {
		return codiceRazza;
	}
	public CapoDto setCodiceRazza(String codiceRazza) {
		this.codiceRazza = codiceRazza;
		return this;
	}
	public List<DetenzioneDto> getDetenzioni() {
		return detenzioni;
	}
	public CapoDto setDetenzioni(List<DetenzioneDto> detenzioni) {
		this.detenzioni = detenzioni;
		return this;
	}

	@Override
	public String toString() {
		return "CapoDto [idCapo=" + idCapo + ", marcaAuricolare=" + marcaAuricolare + ", sesso=" + sesso + ", dataNascita=" + dataNascita + ", codiceRazza=" + codiceRazza + ", detenzioni="
				+ detenzioni + "]";
	}

}
