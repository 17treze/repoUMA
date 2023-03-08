
package it.tndigitale.a4g.fascicolo.antimafia.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "nome", "cognome", "codiceFiscale", "comuneNascita", "provinciaNascita", "dataNascita", "sesso",
		"residenza", "gradoParentela" })
public class Familiare {

	@JsonProperty("nome")
	private String nome;

	@JsonProperty("cognome")
	private String cognome;

	@JsonProperty("codiceFiscale")
	private String codiceFiscale;

	@JsonProperty("comuneNascita")
	private String comuneNascita;

	@JsonProperty("provinciaNascita")
	private String provinciaNascita;

	@JsonProperty("dataNascita")
	private String dataNascita;

	@JsonProperty("sesso")
	private String sesso;

	@JsonProperty("residenza")
	private Residenza residenza;

	@JsonProperty("gradoParentela")
	private String gradoParentela;

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

	@JsonProperty("comuneNascita")
	public String getComuneNascita() {
		return comuneNascita;
	}

	@JsonProperty("comuneNascita")
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

	@JsonProperty("residenza")
	public Residenza getResidenza() {
		return residenza;
	}

	@JsonProperty("residenza")
	public void setResidenza(Residenza residenza) {
		this.residenza = residenza;
	}

	@JsonProperty("gradoParentela")
	public String getGradoParentela() {
		return gradoParentela;
	}

	@JsonProperty("gradoParentela")
	public void setGradoParentela(String gradoParentela) {
		this.gradoParentela = gradoParentela;
	}

}
