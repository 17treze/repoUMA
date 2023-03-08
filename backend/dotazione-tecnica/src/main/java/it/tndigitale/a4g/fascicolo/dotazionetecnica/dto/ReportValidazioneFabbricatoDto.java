package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.TipoConduzione;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneFabbricatoDto {
	private String comune;
	private Long volume;
	private Long superficie;
	private String tipologia;

	public String getComune() {
		return comune;
	}

	public ReportValidazioneFabbricatoDto setComune(String comune) {
		this.comune = comune;
		return this;
	}

	public Long getVolume() {
		return volume;
	}

	public ReportValidazioneFabbricatoDto setVolume(Long volume) {
		this.volume = volume;
		return this;
	}

	public Long getSuperficie() {
		return superficie;
	}

	public ReportValidazioneFabbricatoDto setSuperficie(Long superficie) {
		this.superficie = superficie;
		return this;
	}

	public String getTipologia() {
		return tipologia;
	}

	public ReportValidazioneFabbricatoDto setTipologia(String tipologia) {
		this.tipologia = tipologia;
		return this;
	}
}
