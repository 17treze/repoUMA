package it.tndigitale.a4g.fascicolo.anagrafica.dto.persona;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDate;

public class AnagraficaDto implements Serializable {
	private static final long serialVersionUID = 5206874041834500611L;

	@ApiModelProperty(value = "Nome della persona", required = true)
    private String nome;

    @ApiModelProperty(value = "Cognome della persona", required = true)
    private String cognome;

    @ApiModelProperty(value = "Sesso della persona", required = true)
    private String sesso;

    @ApiModelProperty(value = "Data di nascita della persona", required = true)
    private LocalDate dataNascita;

    @ApiModelProperty(value = "Comune di nascita della persona della persona")
    private String comuneNascita;

    @ApiModelProperty(value = "Provincia di nascita della persona")
    private String provinciaNascita;
    
    @ApiModelProperty(value = "Se si tratta di una persona deceduta", required = true)
    private boolean deceduto;

	@ApiModelProperty(value = "Data di morte della persona")
	private LocalDate dataMorte;

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

	public String getSesso() {
		return sesso;
	}

	public void setSesso(String sesso) {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isDeceduto() {
		return deceduto;
	}

	public void setDeceduto(boolean deceduto) {
		this.deceduto = deceduto;
	}

	public LocalDate getDataMorte() {
		return dataMorte;
	}

	public void setDataMorte(LocalDate dataMorte) {
		this.dataMorte = dataMorte;
	}
}
