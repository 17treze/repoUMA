package it.tndigitale.a4g.proxy.dto.zootecnia;

import java.io.Serializable;
import java.math.BigDecimal;

public class StrutturaAllevamentoDto implements Serializable {
    
	protected String identificativo;
    
    protected String indirizzo;
    
    protected String cap;
    
    protected String localita;
    
    protected String comune;
    
    protected BigDecimal latitudine;
    
    protected BigDecimal longitudine;
    
    protected String foglioCatastale;
    
    protected String sezione;
    
    protected String particella;
    
    protected String subalterno;

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

	public BigDecimal getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(BigDecimal latitudine) {
		this.latitudine = latitudine;
	}

	public BigDecimal getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(BigDecimal longitudine) {
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
