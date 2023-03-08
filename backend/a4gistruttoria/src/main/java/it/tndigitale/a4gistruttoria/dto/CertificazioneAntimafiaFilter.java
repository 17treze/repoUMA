/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

import it.tndigitale.a4gistruttoria.util.StatoDichiarazioneEnum;

/**
 * @author a.pasca
 *
 */
public class CertificazioneAntimafiaFilter {
	private StatoDichiarazioneEnum stato;
	private String filtroGenerico;
	
	public StatoDichiarazioneEnum getStato() {
		return stato;
	}
	public void setStato(StatoDichiarazioneEnum stato) {
		this.stato = stato;
	}
	public String getFiltroGenerico() {
		return filtroGenerico;
	}
	public void setFiltroGenerico(String filtroGenerico) {
		this.filtroGenerico = filtroGenerico;
	}
	
}
