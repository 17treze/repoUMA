package it.tndigitale.a4g.proxy.dto.catasto;

import java.util.List;

public class InformazioniImmobileDto {

//	private List<InformazioniImmobileDettaglioDto> dettaglio;

	private List<String> indirizzo;
	private DatiClassamentoDto datiClassamento;
//	private Double rendita;
//	private String piani;

//	public List<InformazioniImmobileDettaglioDto> getDettaglio() {
//		return dettaglio;
//	}
//
//	public void setDettaglio(List<InformazioniImmobileDettaglioDto> dettaglio) {
//		this.dettaglio = dettaglio;
//	}

	public List<String> getIndirizzo() {
		return indirizzo;
	}

	public InformazioniImmobileDto setIndirizzo(List<String> indirizzo) {
		this.indirizzo = indirizzo;
		return this;
	}

	public DatiClassamentoDto getDatiClassamento() {
		return datiClassamento;
	}

	public void setDatiClassamento(DatiClassamentoDto datiClassamento) {
		this.datiClassamento = datiClassamento;
	}

//	public Double getRendita() {
//		return rendita;
//	}
//
//	public void setRendita(Double rendita) {
//		this.rendita = rendita;
//	}
//
//	public String getPiani() {
//		return piani;
//	}
//
//	public void setPiani(String piani) {
//		this.piani = piani;
//	}
}
