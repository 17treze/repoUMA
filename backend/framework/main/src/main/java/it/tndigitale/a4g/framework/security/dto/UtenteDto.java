package framework.main.src.main.java.it.tndigitale.a4g.framework.security.dto;

import java.io.Serializable;
import java.util.List;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

public class UtenteDto implements Serializable {

	private Long id;
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String email;
	private String telefono;
	private String pec;
	private String indirizzo;
	private String comune;
	private String provincia;
	private String cap;
	private List<RuoloAppDto> ruoli;
	private String aziendeDelegate;
	private String dataNascita;
	private String luogoNascita;
	private String stato;
	private String dataInizioValidita;
	private String dataFineValidita;
	private String dataAbilitazione;
	private String dataDisabilitazione;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getPec() {
		return pec;
	}
	public void setPec(String pec) {
		this.pec = pec;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public List<RuoloAppDto> getRuoli() {
		return ruoli;
	}
	public void setRuoli(List<RuoloAppDto> ruoli) {
		this.ruoli = ruoli;
	}
	public String getAziendeDelegate() {
		return aziendeDelegate;
	}
	public void setAziendeDelegate(String aziendeDelegate) {
		this.aziendeDelegate = aziendeDelegate;
	}
	public String getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getLuogoNascita() {
		return luogoNascita;
	}
	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getDataInizioValidita() {
		return dataInizioValidita;
	}
	public void setDataInizioValidita(String dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
	public String getDataFineValidita() {
		return dataFineValidita;
	}
	public void setDataFineValidita(String dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}
	public String getDataAbilitazione() {
		return dataAbilitazione;
	}
	public void setDataAbilitazione(String dataAbilitazione) {
		this.dataAbilitazione = dataAbilitazione;
	}
	public String getDataDisabilitazione() {
		return dataDisabilitazione;
	}
	public void setDataDisabilitazione(String dataDisabilitazione) {
		this.dataDisabilitazione = dataDisabilitazione;
	}
	
	@Override
	public String toString() {
		return "UtenteDto [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", codiceFiscale=" + codiceFiscale
				+ ", email=" + email + ", telefono=" + telefono + ", pec=" + pec + ", indirizzo=" + indirizzo
				+ ", comune=" + comune + ", provincia=" + provincia + ", cap=" + cap + ", ruoli=" + ruoli
				+ ", aziendeDelegate=" + aziendeDelegate + ", dataNascita=" + dataNascita + ", luogoNascita="
				+ luogoNascita + ", stato=" + stato + ", dataInizioValidita=" + dataInizioValidita
				+ ", dataFineValidita=" + dataFineValidita + ", dataAbilitazione=" + dataAbilitazione
				+ ", dataDisabilitazione=" + dataDisabilitazione + "]";
	}
	
}
