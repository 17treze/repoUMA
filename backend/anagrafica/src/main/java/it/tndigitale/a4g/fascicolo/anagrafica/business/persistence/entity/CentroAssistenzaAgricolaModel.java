package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4GT_CAA")
public class CentroAssistenzaAgricolaModel extends EntitaDominio {
	
	private static final long serialVersionUID = -7504821064824814535L;

	@Column(name = "CODICE_FISCALE", length = 16)
	protected String codiceFiscale;

	@Column(name = "PARTITA_IVA", length = 11)
	private String partitaIVA;

	@Column(name = "DENOMINAZIONE", length = 100)
	private String denominazione;

	@Column(name = "FORMA_GIURIDICA", length = 100)
	private String formaGiuridica;
	
	@Column(name = "ATTO_RICONOSCIMENTO", length = 500)
	private String attoRiconoscimento;
	
	@Column(name = "SOCIETA_SERVIZI", length = 100)
	private String societaServizi;
	
	@Column(name = "CF_SOCIETA_SERVIZI", length = 11)
	private String cfSocietaServizi;
	
	@Column(name = "EMAIL", length = 100)
	private String email;

	@Embedded 
	private IndirizzoModel indirizzo;
	
	@OneToMany(mappedBy="centroAssistenzaAgricola")
	private List<SportelloModel> sportelli;

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getPartitaIVA() {
		return partitaIVA;
	}

	public void setPartitaIVA(String partitaIVA) {
		this.partitaIVA = partitaIVA;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getFormaGiuridica() {
		return formaGiuridica;
	}

	public void setFormaGiuridica(String formaGiuridica) {
		this.formaGiuridica = formaGiuridica;
	}

	public String getAttoRiconoscimento() {
		return attoRiconoscimento;
	}

	public void setAttoRiconoscimento(String attoRiconoscimento) {
		this.attoRiconoscimento = attoRiconoscimento;
	}

	public String getSocietaServizi() {
		return societaServizi;
	}

	public void setSocietaServizi(String societaServizi) {
		this.societaServizi = societaServizi;
	}

	public String getCfSocietaServizi() {
		return cfSocietaServizi;
	}

	public void setCfSocietaServizi(String cfSocietaServizi) {
		this.cfSocietaServizi = cfSocietaServizi;
	}

	public IndirizzoModel getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(IndirizzoModel indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<SportelloModel> getSportelli() {
		return sportelli;
	}

	public void setSportelli(List<SportelloModel> sportelli) {
		this.sportelli = sportelli;
	}
	
}
