package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

public class DatiIstruttoriaPascoli {
	// public enum ESITO {
	// POSITIVO, NEGATIVO
	// }
	//
	// public enum DOCUMENTAZIONE {
	// PRESENTE, INCOMPLETO, ASSENTE
	// }
	private Long id;
	private Integer version;
	private String descrizionePascolo;

	private BigDecimal superficieDeterminata;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String cuaaResponsabile;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String esitoControlloMantenimento;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String verificaDocumentazione;

	public BigDecimal getSuperficieDeterminata() {
		return superficieDeterminata;
	}

	public void setSuperficieDeterminata(BigDecimal superficieDeterminata) {
		this.superficieDeterminata = superficieDeterminata;
	}

	public String getCuaaResponsabile() {
		return cuaaResponsabile;
	}

	public void setCuaaResponsabile(String cuaaResponsabile) {
		this.cuaaResponsabile = cuaaResponsabile;
	}

	public String getEsitoControlloMantenimento() {
		return esitoControlloMantenimento;
	}

	public void setEsitoControlloMantenimento(String esitoControlloMantenimento) {
		this.esitoControlloMantenimento = esitoControlloMantenimento;
	}

	public String getVerificaDocumentazione() {
		return verificaDocumentazione;
	}

	public void setVerificaDocumentazione(String verificaDocumentazione) {
		this.verificaDocumentazione = verificaDocumentazione;
	}

	public String getDescrizionePascolo() {
		return descrizionePascolo;
	}

	public void setDescrizionePascolo(String descrizionePascolo) {
		this.descrizionePascolo = descrizionePascolo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
