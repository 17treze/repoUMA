package it.tndigitale.a4gutente.dto;

import java.io.Serializable;
import javax.security.cert.X509Certificate;
import java.time.LocalDateTime;

import it.tndigitale.a4gutente.codici.TipoAutenticazione;

public class DatiAutenticazione implements Serializable {

	private static final long serialVersionUID = -6812647878969232474L;

	private LocalDateTime dataAutenticazione;
	private TipoAutenticazione tipoAuthenticazione;
	private String codiceSPID;
	private DatiAnagrafici datiAnagrafici;
	private X509Certificate datiCertificato;
	
	public LocalDateTime getDataAutenticazione() {
		return dataAutenticazione;
	}
	public void setDataAutenticazione(LocalDateTime dataAutenticazione) {
		this.dataAutenticazione = dataAutenticazione;
	}
	public String getCodiceSPID() {
		return codiceSPID;
	}
	public void setCodiceSPID(String codiceSPID) {
		this.codiceSPID = codiceSPID;
	}
	public DatiAnagrafici getDatiAnagrafici() {
		return datiAnagrafici;
	}
	public void setDatiAnagrafici(DatiAnagrafici datiAnagrafici) {
		this.datiAnagrafici = datiAnagrafici;
	}
	public X509Certificate getDatiCertificato() {
		return datiCertificato;
	}
	public void setDatiCertificato(X509Certificate datiCertificato) {
		this.datiCertificato = datiCertificato;
	}
	public TipoAutenticazione getTipoAuthenticazione() {
		return tipoAuthenticazione;
	}
	public void setTipoAuthenticazione(TipoAutenticazione tipoAuthenticazione) {
		this.tipoAuthenticazione = tipoAuthenticazione;
	}
}
