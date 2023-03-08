/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;

/**
 * @author s.caccia
 *
 */
@ApiModel(description = "Rappresenta il modello dei filtri di ricerca dei processi")
public class ProcessoFilter {

	@ApiParam(value="Stato del processo da ricercare", required = true)
	private StatoProcesso statoProcesso;

	@ApiParam(value="Tipo del processo da ricercare", required = false)
	private TipoProcesso tipoProcesso;

	public StatoProcesso getStatoProcesso() {
		return statoProcesso;
	}

	public void setStatoProcesso(StatoProcesso statoProcesso) {
		this.statoProcesso = statoProcesso;
	}

	public TipoProcesso getTipoProcesso() {
		return tipoProcesso;
	}

	public void setTipoProcesso(TipoProcesso tipoProcesso) {
		this.tipoProcesso = tipoProcesso;
	}

	@Override
	public String toString() {
		return "ProcessoFilter ["
				+ "statoProcesso=" + statoProcesso + 
				", tipoProcesso=" + tipoProcesso + "]";
	}

}
