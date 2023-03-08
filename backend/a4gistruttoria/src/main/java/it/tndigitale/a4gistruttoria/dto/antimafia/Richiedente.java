
package it.tndigitale.a4gistruttoria.dto.antimafia;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "nome",
    "cognome",
    "codiceFiscale",
    "comuneNascita",
    "provinciaNascita",
    "dataNascita",
    "sesso",
    "residenza",
    "indirizzoPEC"
})
public class Richiedente {

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
    @JsonProperty("indirizzoPEC")
    private String indirizzoPEC;
	@JsonProperty("carica")
	private String carica;
	@JsonProperty("dtInizioCarica")
	private String dtInizioCarica;
	@JsonProperty("dtFineCarica")
	private String dtFineCarica;

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

    @JsonProperty("indirizzoPEC")
    public String getIndirizzoPEC() {
        return indirizzoPEC;
    }

    @JsonProperty("indirizzoPEC")
    public void setIndirizzoPEC(String indirizzoPEC) {
        this.indirizzoPEC = indirizzoPEC;
    }

	@JsonProperty("carica")
	public String getCarica() {
		return carica;
	}

	@JsonProperty("carica")
	public void setCarica(String carica) {
		this.carica = carica;
	}
	
	@JsonProperty("dtInizioCarica")
	public String getDtInizioCarica() {
		return dtInizioCarica;
	}

	@JsonProperty("dtInizioCarica")
	public void setDtInizioCarica(String dtInizioCarica) {
		this.dtInizioCarica = dtInizioCarica;
	}

	@JsonProperty("dtFineCarica")
	public String getDtFineCarica() {
		return dtFineCarica;
	}

	@JsonProperty("dtFineCarica")
	public void setDtFineCarica(String dtFineCarica) {
		this.dtFineCarica = dtFineCarica;
	}

}
