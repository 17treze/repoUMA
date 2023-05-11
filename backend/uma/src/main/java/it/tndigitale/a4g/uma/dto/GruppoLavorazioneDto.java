package it.tndigitale.a4g.uma.dto;

import it.tndigitale.a4g.uma.business.persistence.entity.AmbitoLavorazione;

public class GruppoLavorazioneDto {
	
	private Integer annoFine;
	private Integer annoInizio;
	private Integer indice;
	private String nome;
	private AmbitoLavorazione ambitoLavorazione;
	private Long id;
	
	public Long getId() {
		return this.id;
	}
	
	public GruppoLavorazioneDto setId(Long id) {
		this.id = id;
		return this;
	}
	
	public Integer getAnnoFine() {
		return this.annoFine;
	}
	
	public GruppoLavorazioneDto setAnnoFine(Integer annoFine) {
		this.annoFine = annoFine;
		return this;
	}
	
	public Integer getAnnoInizio() {
		return this.annoInizio;
	}
	
	public GruppoLavorazioneDto setAnnoInizio(Integer annoInizio) {
		this.annoInizio = annoInizio;
		return this;
	}
	
	public Integer getIndice() {
		return this.indice;
	}
	
	public GruppoLavorazioneDto setIndice(Integer indice) {
		this.indice = indice;
		return this;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public GruppoLavorazioneDto setNome(String nome) {
		this.nome = nome;
		return this;
	}
	
	public AmbitoLavorazione getAmbitoLavorazione() {
		return this.ambitoLavorazione;
	}
	
	public GruppoLavorazioneDto setAmbitoLavorazione(AmbitoLavorazione ambitoLavorazione) {
		this.ambitoLavorazione = ambitoLavorazione;
		return this;
	}
	
}