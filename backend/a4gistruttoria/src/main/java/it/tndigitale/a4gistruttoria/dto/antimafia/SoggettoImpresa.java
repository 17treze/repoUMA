
package it.tndigitale.a4gistruttoria.dto.antimafia;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "nome", "cognome", "codiceFiscale", "comuneNascita", "provinciaNascita", "dataNascita", "sesso",
		"indirizzoResidenza", "comuneResidenza", "provinciaResidenza", "capResidenza", "carica",
		"familiariConviventi" })
public class SoggettoImpresa {

	@JsonProperty("nome")
	public String nome;
	@JsonProperty("cognome")
	public String cognome;
	@JsonProperty("codiceFiscale")
	public String codiceFiscale;
	@JsonProperty("comuneNascita")
	public String comuneNascita;
	@JsonProperty("provinciaNascita")
	public String provinciaNascita;
	@JsonProperty("dataNascita")
	public String dataNascita;
	@JsonProperty("sesso")
	public String sesso;
	@JsonProperty("indirizzoResidenza")
	public String indirizzoResidenza;
	@JsonProperty("comuneResidenza")
	public String comuneResidenza;
	@JsonProperty("provinciaResidenza")
	public String provinciaResidenza;
	@JsonProperty("capResidenza")
	public String capResidenza;
	@JsonProperty("carica")
	public List<Carica> carica;
	@JsonProperty("familiariConviventi")
	public List<Familiare> familiariConviventi = null;

	public String civicoResidenza;
	public String nomeCognome;

	@JsonProperty("nome")
	public String getNome() {
		return nome;
	}

	@JsonProperty("nome")
	public void setNome(String nome) {
		this.nome = nome;
	}

	@JsonProperty("cognome")
	public String getCognome() {
		return cognome;
	}

	@JsonProperty("cognome")
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	@JsonProperty("codiceFiscale")
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	@JsonProperty("codiceFiscale")
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	@JsonProperty("estremiCCIAA")
	public String getComuneNascita() {
		return comuneNascita;
	}

	@JsonProperty("estremiCCIAA")
	public void setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
	}

	@JsonProperty("provinciaNascita")
	public String getProvinciaNascita() {
		return provinciaNascita;
	}

	@JsonProperty("provinciaNascita")
	public void setProvinciaNascita(String provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
	}

	@JsonProperty("dataNascita")
	public String getDataNascita() {
		return dataNascita;
	}

	@JsonProperty("dataNascita")
	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}

	@JsonProperty("sesso")
	public String getSesso() {
		return sesso;
	}

	@JsonProperty("sesso")
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	@JsonProperty("indirizzoResidenza")
	public String getIndirizzoResidenza() {
		return indirizzoResidenza;
	}

	@JsonProperty("indirizzoResidenza")
	public void setIndirizzoResidenza(String indirizzoResidenza) {
		this.indirizzoResidenza = indirizzoResidenza;
	}

	@JsonProperty("comuneResidenza")
	public String getComuneResidenza() {
		return comuneResidenza;
	}

	@JsonProperty("comuneResidenza")
	public void setComuneResidenza(String comuneResidenza) {
		this.comuneResidenza = comuneResidenza;
	}

	@JsonProperty("provinciaResidenza")
	public String getProvinciaResidenza() {
		return provinciaResidenza;
	}

	@JsonProperty("provinciaResidenza")
	public void setProvinciaResidenza(String provinciaResidenza) {
		this.provinciaResidenza = provinciaResidenza;
	}

	@JsonProperty("capResidenza")
	public String getCapResidenza() {
		return capResidenza;
	}

	@JsonProperty("capResidenza")
	public void setCapResidenza(String capResidenza) {
		this.capResidenza = capResidenza;
	}

	@JsonProperty("carica")
	public List<Carica> getCarica() {
		return carica;
	}

	@JsonProperty("carica")
	public void setCarica(List<Carica> carica) {
		this.carica = carica;
	}

	@JsonProperty("familiariConviventi")
	public List<Familiare> getFamiliariConviventi() {
		return familiariConviventi;
	}

	@JsonProperty("familiariConviventi")
	public void setFamiliariConviventi(List<Familiare> familiariConviventi) {
		this.familiariConviventi = familiariConviventi;
	}

	public String getCivicoResidenza() {
		return civicoResidenza;
	}

	public void setCivicoResidenza(String civicoResidenza) {
		this.civicoResidenza = civicoResidenza;
	}

	public String getNomeCognome() {
		return nomeCognome;
	}

	public void setNomeCognome(String nomeCognome) {
		this.nomeCognome = nomeCognome;
	}
}
