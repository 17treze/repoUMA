package it.tndigitale.a4g.uma.dto.richiesta;

import java.util.List;

public class RaggruppamentoLavorazioniDto {
	private Long id;
	private Integer indice;
	private String nome;
	// Parametro presente solo per le lavorazioni a superficie: Superficie Massima dichiarabile
	private Long superficieMassima;
	// Parametro presente solo per le lavorazioni a fabbricati: Lista id Fabbricati per cui posso richiedere carburante
	private List<FabbricatoDto> fabbricati;

	private List<LavorazioneDto> lavorazioni;

	public Long getId() {
		return id;
	}
	public RaggruppamentoLavorazioniDto setId(Long id) {
		this.id = id;
		return this;
	}
	public Integer getIndice() {
		return indice;
	}
	public RaggruppamentoLavorazioniDto setIndice(Integer indice) {
		this.indice = indice;
		return this;
	}
	public String getNome() {
		return nome;
	}
	public RaggruppamentoLavorazioniDto setNome(String nome) {
		this.nome = nome;
		return this;
	}
	public List<LavorazioneDto> getLavorazioni() {
		return lavorazioni;
	}
	public RaggruppamentoLavorazioniDto setLavorazioni(List<LavorazioneDto> lavorazioni) {
		this.lavorazioni = lavorazioni;
		return this;
	}
	public Long getSuperficieMassima() {
		return superficieMassima;
	}
	public RaggruppamentoLavorazioniDto setSuperficieMassima(Long superficieMassima) {
		this.superficieMassima = superficieMassima;
		return this;
	}
	public List<FabbricatoDto> getFabbricati() {
		return fabbricati;
	}
	public RaggruppamentoLavorazioniDto setFabbricati(List<FabbricatoDto> fabbricati) {
		this.fabbricati = fabbricati;
		return this;
	}
}
