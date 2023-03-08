package it.tndigitale.a4g.zootecnia.dto;

import it.tndigitale.a4g.framework.repository.dto.EntitaDominioDto;

public class StrutturaAllevamentoDto  extends EntitaDominioDto {
    
	  private String identificativo;
	  
	  private String indirizzo;
	
	  private String cap;
	  
	  private String localita;
	
	  private String comune;
	  
	  private String latitudine;
	
	  private String longitudine;
	
	  private String foglioCatastale;
	
	  private String sezione;
	
	  private String particella;
	
	  private String subalterno;

	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getLocalita() {
		return localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(String latitudine) {
		this.latitudine = latitudine;
	}

	public String getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(String longitudine) {
		this.longitudine = longitudine;
	}

	public String getFoglioCatastale() {
		return foglioCatastale;
	}

	public void setFoglioCatastale(String foglioCatastale) {
		this.foglioCatastale = foglioCatastale;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public String getSubalterno() {
		return subalterno;
	}

	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}

}
