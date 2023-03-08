package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "mittente del mandato")
public class MittenteDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "nome del mittente", required = true)
	private String name;

	@ApiModelProperty(value = "cognome del mittente", required = false)
	private String surname;

	@ApiModelProperty(value = "mail del mittente", required = true)
	private String email;

	@ApiModelProperty(value = "nationalIdentificationNumber del mittente", required = true)
	private String nationalIdentificationNumber;
	
	@ApiModelProperty(value = "stringa contenente CUAA + descrizione impresa", required = true)
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNationalIdentificationNumber() {
		return nationalIdentificationNumber;
	}

	public void setNationalIdentificationNumber(String nationalIdentificationNumber) {
		this.nationalIdentificationNumber = nationalIdentificationNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
