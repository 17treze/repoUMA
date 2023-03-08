package it.tndigitale.a4g.uma.dto.richiesta;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;

public class PrelieviFilter {
	// id del distributore 
	private Long id;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Schema(required = true, example = "2021-11-01T23:59" , description = "utilizzare il formato yyyy-MM-dd'T'HH:mm")
	private LocalDateTime dataPrelievo;

	private Boolean isConsegnato;
	
	private Long campagna;

	public Long getId() {
		return id;
	}
	public PrelieviFilter setId(Long idDistributore) {
		this.id = idDistributore;
		return this;
	}
	public LocalDateTime getDataPrelievo() {
		return dataPrelievo;
	}
	public PrelieviFilter setDataPrelievo(LocalDateTime dataPrelievo) {
		this.dataPrelievo = dataPrelievo;
		return this;
	}
	public Boolean getIsConsegnato() {
		return isConsegnato;
	}
	public PrelieviFilter setIsConsegnato(Boolean isConsegnato) {
		this.isConsegnato = isConsegnato;
		return this;
	}
	public Long getCampagna() {
		return campagna;
	}
	public PrelieviFilter setCampagna(Long campagna) {
		this.campagna = campagna;
		return this;
	}
}
