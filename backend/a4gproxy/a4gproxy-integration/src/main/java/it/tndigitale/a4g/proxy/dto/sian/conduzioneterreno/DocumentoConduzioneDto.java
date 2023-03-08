package it.tndigitale.a4g.proxy.dto.sian.conduzioneterreno;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(description = "DTO del documento giustificativo")
public class DocumentoConduzioneDto implements Serializable{

    @ApiModelProperty(value = "Tipo documento")
	private String tipoDocumento;
    @ApiModelProperty(value = "Numero documento")
	private String numeroDocumento;
    @ApiModelProperty(value = "Data rilascio")
    private String dataRilascio;
    @ApiModelProperty(value = "Data scadenza")
    private String dataScadenza;

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getDataRilascio() {
        return dataRilascio;
    }

    public void setDataRilascio(String dataRilascio) {
        this.dataRilascio = dataRilascio;
    }

    public String getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(String dataScadenza) {
        this.dataScadenza = dataScadenza;
    }
}
