package it.tndigitale.a4g.uma.dto.richiesta;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.distributori.DistributoreDto;

@JsonInclude(Include.NON_NULL)
public class PrelievoDto {

	private Long id;
	private String cuaa;
	private String denominazione;
	private DistributoreDto distributore;
	private CarburanteDto carburante;
	private LocalDateTime data;
	private Boolean isConsegnato;
	private String estremiDocumentoFiscale;

	public DistributoreDto getDistributore() {
		return distributore;
	}
	public PrelievoDto setDistributore(DistributoreDto distributore) {
		this.distributore = distributore;
		return this;
	}
	public LocalDateTime getData() {
		return data;
	}
	public PrelievoDto setData(LocalDateTime data) {
		this.data = data;
		return this;
	}
	public CarburanteDto getCarburante() {
		return carburante;
	}
	public PrelievoDto setCarburante(CarburanteDto carburante) {
		this.carburante = carburante;
		return this;
	}
	public String getCuaa() {
		return cuaa;
	}
	public PrelievoDto setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public PrelievoDto setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}
	public Boolean getIsConsegnato() {
		return isConsegnato;
	}
	public PrelievoDto setIsConsegnato(Boolean isConsegnato) {
		this.isConsegnato = isConsegnato;
		return this;
	}
	public String getEstremiDocumentoFiscale() {
		return estremiDocumentoFiscale;
	}
	public PrelievoDto setEstremiDocumentoFiscale(String estremiDocumentoFiscale) {
		this.estremiDocumentoFiscale = estremiDocumentoFiscale;
		return this;
	}
	public Long getId() {
		return id;
	}
	public PrelievoDto setId(Long id) {
		this.id = id;
		return this;
	}

}
