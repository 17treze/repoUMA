/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

/**
 * @author B.Conetta
 *
 */
public class TrasmissioneBdnaFilter {

	private Long id;
	private String cfOperatore;
	private StatoTrasmissioneBdna statoTrasmissione;
	

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
	public StatoTrasmissioneBdna getStatoTrasmissione() {
		return statoTrasmissione;
	}
	public void setStatoTrasmissione(StatoTrasmissioneBdna statoTrasmissione) {
		this.statoTrasmissione = statoTrasmissione;
	}
	
	public enum StatoTrasmissioneBdna {
		NON_CONFERMATA, CONFERMATA
	}
}
