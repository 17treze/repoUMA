package it.tndigitale.a4g.proxy.dto.persona;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDate;

public class AnagraficaDto implements Serializable {
    private static final long serialVersionUID = 8187340067648198832L;

    @ApiModelProperty(value = "Nome della persona", required = true)
    private String nome;

    @ApiModelProperty(value = "Cognome della persona", required = true)
    private String cognome;

    @ApiModelProperty(value = "Sesso della persona", required = true)
    private Sesso sesso;

    @ApiModelProperty(value = "Data di nascita della persona", required = true)
    private LocalDate dataNascita;

    @ApiModelProperty(value = "Comune di nascita della persona della persona")
    private String comuneNascita;

    @ApiModelProperty(value = "Provincia di nascita della persona")
    private String provinciaNascita;

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

    public Sesso getSesso() {
        return sesso;
    }

    public void setSesso(Sesso sesso) {
        this.sesso = sesso;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getComuneNascita() {
        return comuneNascita;
    }

    public void setComuneNascita(String comuneNascita) {
        this.comuneNascita = comuneNascita;
    }

    public String getProvinciaNascita() {
        return provinciaNascita;
    }

    public void setProvinciaNascita(String provinciaNascita) {
        this.provinciaNascita = provinciaNascita;
    }
}