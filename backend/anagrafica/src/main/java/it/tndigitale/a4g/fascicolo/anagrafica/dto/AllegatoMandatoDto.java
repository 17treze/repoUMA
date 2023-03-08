package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Allegato al contratto con cui un'azienda da mandato ad un CAA di gestire il proprio fascicolo")
public class AllegatoMandatoDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "File allegato", required = true)
    private MultipartFile file;

    @ApiModelProperty(value = "Tipologia di allegato", required = true)
    private TipologiaFileMandato tipologia;
    
    @ApiModelProperty(value = "Descrizione allegato", required = false)
    private String descrizione;
    
    public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public TipologiaFileMandato getTipologia() {
		return tipologia;
	}

	public void setTipologia(TipologiaFileMandato tipologia) {
		this.tipologia = tipologia;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
    
}
