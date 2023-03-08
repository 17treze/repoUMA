/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

import java.util.List;

/**
 * @author a.pasca
 *
 */
public class EsitiBdna {
	private List<String> domandeAggiornate;
	private List<String> domandeConErrori;
	
	public List<String> getDomandeAggiornate() {
		return domandeAggiornate;
	}
	public void setDomandeAggiornate(List<String> domandeAggiornate) {
		this.domandeAggiornate = domandeAggiornate;
	}
	public List<String> getDomandeConErrori() {
		return domandeConErrori;
	}
	public void setDomandeConErrori(List<String> domandeConErrori) {
		this.domandeConErrori = domandeConErrori;
	}
}
