package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ModuloRichiestaTrasferimentoOpDto {
	private Boolean costituzione;
	private Boolean trasferimento;
	
	private String nomeFirmatario;
	private String comuneNascitaFirmatario;
	private String dataNascitaFirmatario;
	private String comuneResidenzaFirmatario;
	private String provResidenzaFirmatario;
	private String indirizzoResidenzaFirmatario;
	private String codiceFiscaleFirmatario;

	private String cuaaAzienda;
	private String denominazioneAzienda;
	private String comuneSedeAzienda;
	private String provSedeAzienda;
	private String indirizzoSedeAzienda;
	private String emailAzienda;
	private String pecAzienda;
	
	private String descrizioneCaa;
	private String sedeCaa;
	private String codiTramCaa;
	private String telefonoCaa;
	private String emailCaa;
	private String pecCaa;

	private String data;

	public Boolean getCostituzione() {
		return costituzione;
	}

	public void setCostituzione(Boolean costituzione) {
		this.costituzione = costituzione;
	}

	public Boolean getTrasferimento() {
		return trasferimento;
	}

	public void setTrasferimento(Boolean trasferimento) {
		this.trasferimento = trasferimento;
	}

	public String getNomeFirmatario() {
		return nomeFirmatario;
	}

	public void setNomeFirmatario(String nomeFirmatario) {
		this.nomeFirmatario = nomeFirmatario;
	}

	public String getComuneNascitaFirmatario() {
		return comuneNascitaFirmatario;
	}

	public void setComuneNascitaFirmatario(String comuneNascitaFirmatario) {
		this.comuneNascitaFirmatario = comuneNascitaFirmatario;
	}

	public String getDataNascitaFirmatario() {
		return dataNascitaFirmatario;
	}

	public void setDataNascitaFirmatario(String dataNascitaFirmatario) {
		this.dataNascitaFirmatario = dataNascitaFirmatario;
	}

	public String getComuneResidenzaFirmatario() {
		return comuneResidenzaFirmatario;
	}

	public void setComuneResidenzaFirmatario(String comuneResidenzaFirmatario) {
		this.comuneResidenzaFirmatario = comuneResidenzaFirmatario;
	}

	public String getProvResidenzaFirmatario() {
		return provResidenzaFirmatario;
	}

	public void setProvResidenzaFirmatario(String provResidenzaFirmatario) {
		this.provResidenzaFirmatario = provResidenzaFirmatario;
	}

	public String getIndirizzoResidenzaFirmatario() {
		return indirizzoResidenzaFirmatario;
	}

	public void setIndirizzoResidenzaFirmatario(String indirizzoResidenzaFirmatario) {
		this.indirizzoResidenzaFirmatario = indirizzoResidenzaFirmatario;
	}

	public String getCodiceFiscaleFirmatario() {
		return codiceFiscaleFirmatario;
	}

	public void setCodiceFiscaleFirmatario(String codiceFiscaleFirmatario) {
		this.codiceFiscaleFirmatario = codiceFiscaleFirmatario;
	}

	public String getCuaaAzienda() {
		return cuaaAzienda;
	}

	public void setCuaaAzienda(String cuaaAzienda) {
		this.cuaaAzienda = cuaaAzienda;
	}

	public String getDenominazioneAzienda() {
		return denominazioneAzienda;
	}

	public void setDenominazioneAzienda(String denominazioneAzienda) {
		this.denominazioneAzienda = denominazioneAzienda;
	}

	public String getComuneSedeAzienda() {
		return comuneSedeAzienda;
	}

	public void setComuneSedeAzienda(String comuneSedeAzienda) {
		this.comuneSedeAzienda = comuneSedeAzienda;
	}

	public String getProvSedeAzienda() {
		return provSedeAzienda;
	}

	public void setProvSedeAzienda(String provSedeAzienda) {
		this.provSedeAzienda = provSedeAzienda;
	}

	public String getIndirizzoSedeAzienda() {
		return indirizzoSedeAzienda;
	}

	public void setIndirizzoSedeAzienda(String indirizzoSedeAzienda) {
		this.indirizzoSedeAzienda = indirizzoSedeAzienda;
	}

	public String getEmailAzienda() {
		return emailAzienda;
	}

	public void setEmailAzienda(String emailAzienda) {
		this.emailAzienda = emailAzienda;
	}

	public String getPecAzienda() {
		return pecAzienda;
	}

	public void setPecAzienda(String pecAzienda) {
		this.pecAzienda = pecAzienda;
	}

	public String getDescrizioneCaa() {
		return descrizioneCaa;
	}

	public void setDescrizioneCaa(String descrizioneCaa) {
		this.descrizioneCaa = descrizioneCaa;
	}

	public String getSedeCaa() {
		return sedeCaa;
	}

	public void setSedeCaa(String sedeCaa) {
		this.sedeCaa = sedeCaa;
	}

	public String getCodiTramCaa() {
		return codiTramCaa;
	}

	public void setCodiTramCaa(String codiTramCaa) {
		this.codiTramCaa = codiTramCaa;
	}

	public String getTelefonoCaa() {
		return telefonoCaa;
	}

	public void setTelefonoCaa(String telefonoCaa) {
		this.telefonoCaa = telefonoCaa;
	}

	public String getEmailCaa() {
		return emailCaa;
	}

	public void setEmailCaa(String emailCaa) {
		this.emailCaa = emailCaa;
	}

	public String getPecCaa() {
		return pecCaa;
	}

	public void setPecCaa(String pecCaa) {
		this.pecCaa = pecCaa;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
}