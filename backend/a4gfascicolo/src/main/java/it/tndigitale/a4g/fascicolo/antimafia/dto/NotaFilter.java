/**
 * 
 */
package it.tndigitale.a4g.fascicolo.antimafia.dto;

/**
 * @author S.DeLuca
 *
 */
public class NotaFilter {

	private TipoNotaEnum tipoNota;
	private String chiaveEsterna;

	public TipoNotaEnum getTipoNota() {
		return tipoNota;
	}

	public void setTipoNota(TipoNotaEnum tipoNota) {
		this.tipoNota = tipoNota;
	}

	public String getChiaveEsterna() {
		return chiaveEsterna;
	}

	public void setChiaveEsterna(String chiaveEsterna) {
		this.chiaveEsterna = chiaveEsterna;
	}
}
