package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class DatiLiquidazioneStampaAcz {

	private Date dataElenco;
	private String codiceElenco;
	private String campagna;
	private String tipoPagamento;
	private String intervento;
	private List<DomandaLiquidataStampaAcz> elencoDomande;
	private BigDecimal totaleElencoLiquidazione;

	public Date getDataElenco() {
		return dataElenco;
	}

	public void setDataElenco(Date dataElenco) {
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

	public List<DomandaLiquidataStampaAcz> getElencoDomande() {
		return elencoDomande;
	}

	public void setElencoDomande(List<DomandaLiquidataStampaAcz> elencoDomande) {
		this.elencoDomande = elencoDomande;
	}

	public BigDecimal getTotaleElencoLiquidazione() {
		return totaleElencoLiquidazione;
	}

	public void setTotaleElencoLiquidazione(BigDecimal totaleElencoLiquidazione) {
		this.totaleElencoLiquidazione = totaleElencoLiquidazione;
	}

}
