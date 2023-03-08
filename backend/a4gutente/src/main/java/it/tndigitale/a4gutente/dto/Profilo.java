/**
 * 
 */
package it.tndigitale.a4gutente.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel("Rappresenta il modello del profilo di un utente")
public class Profilo {

	@ApiModelProperty(value = "Id del profilo", required = true)
	private Long id;
	@ApiModelProperty(value = "Identificativo del profilo", required = true)
	private String identificativo;
	@ApiModelProperty(value = "Descrizione del profilo")
	private String descrizione;
	@ApiModelProperty(value = "Responsabilità associata al profilo")
	private Responsabilita responsabilita;
	@ApiModelProperty(value = "Flag che dice se il profilo possiede dei ruoli")
	private Boolean haRuoli;
	@ApiModelProperty(value = "Flag che dice se il profilo è stato disabilitato")
	private Boolean disabled = Boolean.FALSE;

	public Long getId() {
		return id;
	}

	public Profilo setId(Long id) {
		this.id = id;
		return this;
	}

	public String getIdentificativo() {
		return identificativo;
	}

	public Profilo setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
		return this;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public Profilo setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		return this;
	}

	public Responsabilita getResponsabilita() {
		return responsabilita;
	}

	public Profilo setResponsabilita(Responsabilita responsabilita) {
		this.responsabilita = responsabilita;
		return this;
	}

	public Boolean getHaRuoli() {
		return haRuoli;
	}

	public Profilo setHaRuoli(Boolean haRuoli) {
		this.haRuoli = haRuoli;
		return this;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public Profilo setDisabled(Boolean disabled) {
		this.disabled = disabled;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Profilo profilo = (Profilo) o;
		return Objects.equals(id, profilo.id) &&
				Objects.equals(identificativo, profilo.identificativo) &&
				Objects.equals(descrizione, profilo.descrizione) &&
				responsabilita == profilo.responsabilita &&
				Objects.equals(haRuoli, profilo.haRuoli) &&
				Objects.equals(disabled, profilo.disabled);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, identificativo, descrizione, responsabilita, haRuoli, disabled);
	}
}
