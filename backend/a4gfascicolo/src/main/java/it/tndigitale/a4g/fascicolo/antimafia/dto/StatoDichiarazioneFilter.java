/**
 * 
 */
package it.tndigitale.a4g.fascicolo.antimafia.dto;

import java.util.List;

/**
 * @author B.Conetta
 *	contiene lista degli stati di una dichiarazione 
 */
public class StatoDichiarazioneFilter {

	private List<String> statiDichiarazione;

	public List<String> getStatiDichiarazione() {
		return statiDichiarazione;
	}

	public void setStatiDichiarazione(List<String> statiDichiarazione) {
		this.statiDichiarazione = statiDichiarazione;
	}
}
