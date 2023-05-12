package it.tndigitale.a4g.uma.dto;

import it.tndigitale.a4g.uma.business.persistence.entity.TipologiaLavorazione;
import it.tndigitale.a4g.uma.business.persistence.entity.UnitaDiMisura;

public class LavorazioneDto {
	
	private Long id;
	private Integer indice;
	private String nome;
	private TipologiaLavorazione tipologia;
	private UnitaDiMisura unitaDiMisura;
	private GruppoLavorazioneDto gruppoLavorazione;
	
	public Long getId() {
		return this.id;
	}
	
	public LavorazioneDto setId(Long id) {
		this.id = id;
		return this;
	}
	
	public Integer getIndice() {
		return indice;
	}
	
	public LavorazioneDto setIndice(Integer indice) {
		this.indice = indice;
		return this;
	}
	
	public String getNome() {
		return nome;
	}
	
	public LavorazioneDto setNome(String nome) {
		this.nome = nome;
		return this;
	}
	
	public TipologiaLavorazione getTipologia() {
		return tipologia;
	}
	
	public LavorazioneDto setTipologia(TipologiaLavorazione tipologia) {
		this.tipologia = tipologia;
		return this;
	}
	
	public UnitaDiMisura getUnitaDiMisura() {
		return unitaDiMisura;
	}
	
	public LavorazioneDto setUnitaDiMisura(UnitaDiMisura unitaDiMisura) {
		this.unitaDiMisura = unitaDiMisura;
		return this;
	}
	
	public GruppoLavorazioneDto getGruppoLavorazione() {
		return gruppoLavorazione;
	}
	
	public LavorazioneDto setGruppoLavorazione(GruppoLavorazioneDto gruppoLavorazione) {
		this.gruppoLavorazione = gruppoLavorazione;
		return this;
	}
	
}