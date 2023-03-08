package it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filtro di ricerca su macchine di un fascicolo")
public class MacchinaAgsFilter {

	@Parameter(name = "cuaa" , required = false)
	private String cuaa;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Schema(required = true, example = "2021-12-31T23:00" , description = "utilizzare il formato yyyy-MM-dd'T'HH:mm")
	private LocalDateTime data;

	@Parameter(name = "tipiCarburante", required = false , description = "Filtro macchine in base al carburante utilizzato")
	@Schema(enumAsRef = true)
	private Set<TipoCarburante> tipiCarburante;

	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	public Set<TipoCarburante> getTipiCarburante() {
		return tipiCarburante;
	}
	public void setTipiCarburante(Set<TipoCarburante> tipiCarburante) {
		this.tipiCarburante = tipiCarburante;
	}
	public LocalDateTime getData() {
		return data;
	}
	public void setData(LocalDateTime data) {
		this.data = data;
	} 
}
