package it.tndigitale.a4g.fascicolo.territorio.dto.conduzione;

import java.io.Serializable;

public class SottotipoDocumentoDto implements Serializable {

	private Long id;
	private Long sottotipoConduzione;
	private Long idDocumentoConduzione;
	private String descrizione;
    private String tipo;
    private int obbligatorio;
    private Long[] documentoDipendenza;
    
    
	public Long[] getDocumentoDipendenza() {
		return documentoDipendenza;
	}
	public void setDocumentoDipendenza(Long[] documentoDipendenza) {
		this.documentoDipendenza = documentoDipendenza;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSottotipoConduzione() {
		return sottotipoConduzione;
	}
	public void setSottotipoConduzione(Long sottotipoConduzione) {
		this.sottotipoConduzione = sottotipoConduzione;
	}
	public Long getIdDocumentoConduzione() {
		return idDocumentoConduzione;
	}
	public void setIdDocumentoConduzione(Long idDocumentoConduzione) {
		this.idDocumentoConduzione = idDocumentoConduzione;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public int getObbligatorio() {
		return obbligatorio;
	}
	public void setObbligatorio(int obbligatorieta) {
		this.obbligatorio = obbligatorieta;
	}
    
    
    
}
