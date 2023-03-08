package it.tndigitale.a4g.uma.dto.richiesta.stampa;

import java.util.List;

import it.tndigitale.a4g.uma.dto.richiesta.FabbricatoDto;

public class StampaTipoLavorazioneDto {
	private String nome;
	private Integer indice;
	// Parametro presente solo per le lavorazioni a superficie: Superficie Massima dichiarabile
	private Long superficieMassima;
	// Parametro presente solo per le lavorazioni a fabbricati: Lista id Fabbricati per cui posso richiedere carburante
	private FabbricatoDto fabbricato;
	// Parametro presente solo per le lavorazioni sotto serra: ricavato dividendo volume/quantita
	private Integer mesi;
	private List<StampaLavorazioneDto> lavorazioni;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getIndice() {
		return indice;
	}

	public void setIndice(Integer indice) {
		this.indice = indice;
	}

	public Long getSuperficieMassima() {
		return superficieMassima;
	}

	public void setSuperficieMassima(Long superficieMassima) {
		this.superficieMassima = superficieMassima;
	}

	public List<StampaLavorazioneDto> getLavorazioni() {
		return lavorazioni;
	}

	public void setLavorazioni(List<StampaLavorazioneDto> lavorazioni) {
		this.lavorazioni = lavorazioni;
	}

	public FabbricatoDto getFabbricato() {
		return fabbricato;
	}

	public void setFabbricato(FabbricatoDto fabbricato) {
		this.fabbricato = fabbricato;
	}
	
	public Integer getMesi() {
		return mesi;
	}

	public void setMesi(Integer mesi) {
		this.mesi = mesi;
	}

}
