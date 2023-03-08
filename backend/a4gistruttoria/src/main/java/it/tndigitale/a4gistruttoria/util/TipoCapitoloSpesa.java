/**
 * 
 */
package it.tndigitale.a4gistruttoria.util;

/**
 * @author a.pasca
 *
 */
public enum TipoCapitoloSpesa {
	
	CONDISC("CON DISCIPLINA"),
	SENZADISC("SENZA DISCIPLINA"),
	RID50("RIDUZIONE 50"),
	RID100("RIDUZIONE 100");
	

	public String getTipoCapitoloSpesa() {
		return tipoCapitoloSpesa;
	}

	private TipoCapitoloSpesa(String tipoCapitoloSpesa) {
		this.tipoCapitoloSpesa = tipoCapitoloSpesa;
	}
	
	private String tipoCapitoloSpesa;
	
}
