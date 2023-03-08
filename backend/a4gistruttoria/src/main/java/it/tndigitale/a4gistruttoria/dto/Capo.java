/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoBdn;

/**
 * @author S.DeLuca
 *
 */
public class Capo extends RichiestaAllevamDuEsito {

	private static final long serialVersionUID = 1L;

	private Boolean duplicato;
	private Boolean controlloSuperato;
	private Boolean controlloNonSuperato;
	private Boolean richiesto;
	private String stato;
	private CapoBdn capoBdn;

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Boolean getDuplicato() {
		return duplicato;
	}

	public void setDuplicato(Boolean duplicato) {
		this.duplicato = duplicato;
	}

	public Boolean getControlloSuperato() {
		return controlloSuperato;
	}

	public void setControlloSuperato(Boolean controlloSuperato) {
		this.controlloSuperato = controlloSuperato;
	}

	public Boolean getControlloNonSuperato() {
		return controlloNonSuperato;
	}

	public void setControlloNonSuperato(Boolean controlloNonSuperato) {
		this.controlloNonSuperato = controlloNonSuperato;
	}
	
	public Boolean getRichiesto() {
		return richiesto;
	}

	public void setRichiesto(Boolean richiesto) {
		this.richiesto = richiesto;
	}

	public CapoBdn getCapoBdn() {
		return capoBdn;
	}

	public void setCapoBdn(CapoBdn capoBdn) {
		this.capoBdn = capoBdn;
	}

}
