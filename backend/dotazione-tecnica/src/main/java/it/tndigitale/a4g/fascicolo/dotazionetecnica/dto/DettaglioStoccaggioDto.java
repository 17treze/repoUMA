
package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.Copertura;

@JsonTypeName("STOCCAGGIO")
public class DettaglioStoccaggioDto extends DettaglioFabbricatoAbstract {
	
	private Long altezza;
	private Copertura copertura;
	
	public Long getAltezza() {
		return altezza;
	}
	public DettaglioStoccaggioDto setAltezza(Long altezza) {
		this.altezza = altezza;
		return this;
	}
	public Copertura getCopertura() {
		return copertura;
	}
	public DettaglioStoccaggioDto setCopertura(Copertura copertura) {
		this.copertura = copertura;
		return this;
	}
}
