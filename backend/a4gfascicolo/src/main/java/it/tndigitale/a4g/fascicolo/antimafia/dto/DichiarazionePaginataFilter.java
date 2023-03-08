/**
 * 
 */
package it.tndigitale.a4g.fascicolo.antimafia.dto;

import java.util.List;

import it.tndigitale.a4g.fascicolo.antimafia.StatoDichiarazioneEnum;

/**
 * @author B.Conetta
 *
 */
public class DichiarazionePaginataFilter {
	
	private List<StatoDichiarazioneEnum> statiDichiarazione;
	private String filtroGenerico;

	public List<StatoDichiarazioneEnum> getStatiDichiarazione() {
		return statiDichiarazione;
	}
	public void setStatiDichiarazione(List<StatoDichiarazioneEnum> statiDichiarazione) {
		this.statiDichiarazione = statiDichiarazione;
	}
	public String getFiltroGenerico() {
		return filtroGenerico;
	}
	public void setFiltroGenerico(String filtroGenerico) {
		this.filtroGenerico = filtroGenerico;
	}
	
	
}
