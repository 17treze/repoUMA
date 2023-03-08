package it.tndigitale.a4g.uma.dto.richiesta.stampa;

import java.util.List;

import it.tndigitale.a4g.uma.dto.richiesta.FabbisognoDto;

public class StampaLavorazioneDto {
	private Long id;
	private Integer indice;
	private String nome;
	private String unitaDiMisura;
	private String tipologia;
	private List<FabbisognoDto> dichiarazioni;
	public Long getId() {
		return id;
	}
	public StampaLavorazioneDto setId(Long id) {
		this.id = id;
		return this;
	}
	public Integer getIndice() {
		return indice;
	}
	public StampaLavorazioneDto setIndice(Integer indice) {
		this.indice = indice;
		return this;
	}
	public String getNome() {
		return nome;
	}
	public StampaLavorazioneDto setNome(String nome) {
		this.nome = nome;
		return this;
	}
	public String getUnitaDiMisura() {
		return unitaDiMisura;
	}
	public StampaLavorazioneDto setUnitaDiMisura(String unitaDiMisura) {
		this.unitaDiMisura = unitaDiMisura;
		return this;
	}
	public String getTipologia() {
		return tipologia;
	}
	public StampaLavorazioneDto setTipologia(String tipologia) {
		this.tipologia = tipologia;
		return this;
	}
	public List<FabbisognoDto> getDichiarazioni() {
		return dichiarazioni;
	}
	public StampaLavorazioneDto setDichiarazioni(List<FabbisognoDto> dichiarazioni) {
		this.dichiarazioni = dichiarazioni;
		return this;
	}
}
