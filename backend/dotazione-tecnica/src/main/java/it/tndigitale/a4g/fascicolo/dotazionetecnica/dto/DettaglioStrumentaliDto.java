package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("STRUMENTALE")
public class DettaglioStrumentaliDto extends DettaglioFabbricatoAbstract {
	
	private Long superficieCoperta;
	private Long superficieScoperta;
	
	public Long getSuperficieCoperta() {
		return superficieCoperta;
	}
	public DettaglioStrumentaliDto setSuperficieCoperta(Long superficieCoperta) {
		this.superficieCoperta = superficieCoperta;
		return this;
	}
	public Long getSuperficieScoperta() {
		return superficieScoperta;
	}
	public DettaglioStrumentaliDto setSuperficieScoperta(Long superficieScoperta) {
		this.superficieScoperta = superficieScoperta;
		return this;
	}

	
}
