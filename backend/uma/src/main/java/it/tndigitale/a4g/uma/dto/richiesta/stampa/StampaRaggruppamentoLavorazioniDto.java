package it.tndigitale.a4g.uma.dto.richiesta.stampa;

import java.util.List;

public class StampaRaggruppamentoLavorazioniDto {
	
	private Integer indice;
	private String nome;
	private List<StampaTipoLavorazioneDto> tipi;
	
	public Integer getIndice() {
		return indice;
	}
	public void setIndice(Integer indice) {
		this.indice = indice;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<StampaTipoLavorazioneDto> getTipi() {
		return tipi;
	}
	public void setTipi(List<StampaTipoLavorazioneDto> tipi) {
		this.tipi = tipi;
	}
}
