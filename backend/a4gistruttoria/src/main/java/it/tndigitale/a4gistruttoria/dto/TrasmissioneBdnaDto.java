/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

import java.util.Date;

import it.tndigitale.a4gistruttoria.util.TipoDomandaCollegata;

/**
 * @author B.Conetta
 *
 */
public class TrasmissioneBdnaDto {

	private Long id;
	private String cfOperatore;
	private Date dtCreazione;
	private Date dtConferma;
	private TipoDomandaCollegata tipoDomanda;
	

	public TipoDomandaCollegata getTipoDomanda() {
		return tipoDomanda;
	}
	public void setTipoDomanda(TipoDomandaCollegata tipoDomanda) {
		this.tipoDomanda = tipoDomanda;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCfOperatore() {
		return cfOperatore;
	}
	public void setCfOperatore(String cfOperatore) {
		this.cfOperatore = cfOperatore;
	}
	public Date getDtCreazione() {
		return dtCreazione;
	}
	public void setDtCreazione(Date dtCreazione) {
		this.dtCreazione = dtCreazione;
	}
	public Date getDtConferma() {
		return dtConferma;
	}
	public void setDtConferma(Date dtConferma) {
		this.dtConferma = dtConferma;
	}
	
}
