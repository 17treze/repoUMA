package it.tndigitale.a4g.territorio.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.tndigitale.a4g.fascicolo.territorio.dto.TitoloConduzione;

@JsonInclude(Include.NON_EMPTY)
public class ConduzioneTerreniDto  implements Serializable {

	/**
	{
	  ambito: string;
	  sottotipo: id;
	  documento: [{
	    file: File;
	    id: number;
	    dataInizioValidita: Date;
	    dataFineValidita: Date;
	  }]
	  particelle: [{
	    particella: string;
	    foglio: string;
	    sub: string;
	    sezione: string;
	    comune: string:
	    superficie: number;
	    }]
	}	
	 */
	private static final long serialVersionUID = 1L;
	
	private TitoloConduzione ambito;
	private Long idSottotipo;
	private List<DocumentoConduzioneDto> documentiConduzione;
	private List<ParticellaFondiariaDto> particelleFondiarie;
	
	public TitoloConduzione getAmbito() {
		return ambito;
	}
	public void setAmbito(TitoloConduzione ambito) {
		this.ambito = ambito;
	}
	public Long getIdSottotipo() {
		return idSottotipo;
	}
	public void setIdSottotipo(Long idSottotipo) {
		this.idSottotipo = idSottotipo;
	}
	public List<DocumentoConduzioneDto> getDocumentiConduzione() {
		return documentiConduzione;
	}
	public void setDocumentiConduzione(List<DocumentoConduzioneDto> documentiConduzione) {
		this.documentiConduzione = documentiConduzione;
	}
	public List<ParticellaFondiariaDto> getParticelleFondiarie() {
		return particelleFondiarie;
	}
	public void setParticelleFondiarie(List<ParticellaFondiariaDto> particelleFondiarie) {
		this.particelleFondiarie = particelleFondiarie;
	}

}
