package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;

public class InterventoDuPremio {
	private Long id;
	private InterventoDto interventoDto;
	private BigDecimal valoreUnitarioIntervento;
	private Integer priorita;

	public InterventoDto getIntervento() {
		return interventoDto;
	}

	public BigDecimal getValoreUnitarioIntervento() {
		return valoreUnitarioIntervento;
	}

	public void setIntervento(InterventoDto interventoDto) {
		this.interventoDto = interventoDto;
	}

	public void setValoreUnitarioIntervento(BigDecimal valoreUnitarioIntervento) {
		this.valoreUnitarioIntervento = valoreUnitarioIntervento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getPriorita() {
		return priorita;
	}

	public void setPriorita(Integer priorita) {
		this.priorita = priorita;
	}

}
