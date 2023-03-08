package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import it.tndigitale.a4gistruttoria.config.ImportoBigDecimalJsonSerializer;
import it.tndigitale.a4gistruttoria.config.LocalDateJsonSerializer;

public class DatiLiquidazioneStampa {

	@JsonSerialize(using = LocalDateJsonSerializer.class)
	private LocalDate dataElenco;
	private String codiceElenco;
	private String campagna;
	private String tipoPagamento;
	private String intervento;
	private List<DomandaLiquidataStampa> elencoDomande;
	@JsonSerialize(using = ImportoBigDecimalJsonSerializer.class)
	private BigDecimal totaleElencoLiquidazione;

	public LocalDate getDataElenco() {
		return dataElenco;
	}

	public void setDataElenco(LocalDate dataElenco) {
		this.dataElenco = dataElenco;
	}

	public String getCodiceElenco() {
		return codiceElenco;
	}

	public void setCodiceElenco(String codiceElenco) {
		this.codiceElenco = codiceElenco;
	}

	public String getCampagna() {
		return campagna;
	}

	public void setCampagna(String campagna) {
		this.campagna = campagna;
	}

	public String getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(String tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public String getIntervento() {
		return intervento;
	}

	public void setIntervento(String intervento) {
		this.intervento = intervento;
	}

	public List<DomandaLiquidataStampa> getElencoDomande() {
		return elencoDomande;
	}

	public void setElencoDomande(List<DomandaLiquidataStampa> elencoDomande) {
		this.elencoDomande = elencoDomande;
	}

	public BigDecimal getTotaleElencoLiquidazione() {
		return totaleElencoLiquidazione;
	}

	public void setTotaleElencoLiquidazione(BigDecimal totaleElencoLiquidazione) {
		this.totaleElencoLiquidazione = totaleElencoLiquidazione;
	}

}
