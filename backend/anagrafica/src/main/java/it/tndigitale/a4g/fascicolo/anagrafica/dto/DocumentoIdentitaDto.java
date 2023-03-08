package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DocumentoIdentitaModel;

@ApiModel(description = "Documento di identita'")
public class DocumentoIdentitaDto implements Serializable{

	@ApiModelProperty(value = "Codice fiscale", required = true)
	private String codiceFiscale;

	@ApiModelProperty(value = "Numero del documento", required = true)
	private String numeroDocumento;

	@ApiModelProperty(value = "Tipo di documento", required = true)
	private TipoDocumentoIdentitaEnum tipoDocumento;

	@ApiModelProperty(value = "Data rilascio", required = true)
	private LocalDate dataRilascio;

	@ApiModelProperty(value = "Data scadenza", required = true)
	private LocalDate dataScadenza;

	@ApiModelProperty(value = "Scansione del documento", required = true)
	private byte[] documento;

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public TipoDocumentoIdentitaEnum getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumentoIdentitaEnum tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public LocalDate getDataRilascio() {
		return dataRilascio;
	}

	public void setDataRilascio(LocalDate dataRilascio) {
		this.dataRilascio = dataRilascio;
	}

	public LocalDate getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(LocalDate dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public byte[] getDocumento() {
		return documento;
	}

	public void setDocumento(byte[] documento) {
		this.documento = documento;
	}

	public static DocumentoIdentitaDto build(Optional<DocumentoIdentitaModel> documento) {
		DocumentoIdentitaDto documentoIdentitaDto = new DocumentoIdentitaDto();
		if(documento.isPresent()) {
			documentoIdentitaDto.setCodiceFiscale(documento.get().getCodiceFiscale());
			documentoIdentitaDto.setNumeroDocumento(documento.get().getNumero());
			documentoIdentitaDto.setTipoDocumento(TipoDocumentoIdentitaEnum.valueOf(documento.get().getTipologia()));
			documentoIdentitaDto.setDataRilascio(documento.get().getDataRilascio());
			documentoIdentitaDto.setDataScadenza(documento.get().getDataScadenza());
			documentoIdentitaDto.setDocumento(documento.get().getDocumento());
		}
		return documentoIdentitaDto;
	}
}
