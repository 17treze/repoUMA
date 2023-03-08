package it.tndigitale.a4g.uma.dto.distributori;

import it.tndigitale.a4g.uma.dto.richiesta.PrelievoDto;

public class PresentaPrelievoDto {

	private Long idRichiesta;
	private PrelievoDto prelievo;

	public Long getIdRichiesta() {
		return idRichiesta;
	}
	public PresentaPrelievoDto setIdRichiesta(Long idRichiesta) {
		this.idRichiesta = idRichiesta;
		return this;
	}
	public PrelievoDto getPrelievo() {
		return prelievo;
	}
	public PresentaPrelievoDto setPrelievo(PrelievoDto prelievo) {
		this.prelievo = prelievo;
		return this;
	}
}
