/**
 * 
 */
package it.tndigitale.a4g.fascicolo.antimafia.dto;

import java.util.List;

/**
 * @author S.DeLuca
 *
 */
public class DichiarazioneFilter extends Dichiarazione {

	private List<String> statiDichiarazione;

	private boolean filtroUtenteEnte;

	public List<String> getStatiDichiarazione() {
		return statiDichiarazione;
	}

	public void setStatiDichiarazione(List<String> statiDichiarazione) {
		this.statiDichiarazione = statiDichiarazione;
	}

	public boolean isFiltroUtenteEnte() {
		return filtroUtenteEnte;
	}

	public void setFiltroUtenteEnte(boolean filtroUtenteEnte) {
		this.filtroUtenteEnte = filtroUtenteEnte;
	}

}
