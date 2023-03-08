package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.util.Date;
import java.util.List;

public class StampaLiquidabilita {

	private Date dtElenco;
	private String codiceElenco;
	private String campagna;
	private String tipoPagamento;
	private String intervento;
	List<DomandaLiquidabilitaStampa> domande;

	public StampaLiquidabilita(Date dtElenco, String codiceElenco, String campagna, String tipoPagamento, String intervento, List<DomandaLiquidabilitaStampa> domande) {
		super();
		this.dtElenco = dtElenco;
		this.codiceElenco = codiceElenco;
		this.campagna = campagna;
		this.tipoPagamento = tipoPagamento;
		this.intervento = intervento;
		this.domande = domande;
	}

	public StampaLiquidabilita() {

	}

	public Date getDtElenco() {
		return dtElenco;
	}

	public void setDtElenco(Date dtElenco) {
		this.dtElenco = dtElenco;
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

	public List<DomandaLiquidabilitaStampa> getDomande() {
		return domande;
	}

	public void setDomande(List<DomandaLiquidabilitaStampa> domande) {
		this.domande = domande;
	}

}
