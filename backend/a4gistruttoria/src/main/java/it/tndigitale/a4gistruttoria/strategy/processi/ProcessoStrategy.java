/**
 * 
 */
package it.tndigitale.a4gistruttoria.strategy.processi;

import it.tndigitale.a4gistruttoria.dto.ControlliSostegno;
import it.tndigitale.a4gistruttoria.dto.ProcessoAnnoCampagnaDomandaDto;

/**
 * Interfaccia da implementare per la strategy relativa all'avvio dei processi.<br>
 * Es. {@link ControlliSostegno}
 * 
 * @author S.DeLuca
 *
 */
public interface ProcessoStrategy {

	/**
	 * Avvia il processo.
	 * 
	 * @param processoDomanda
	 */
	public void avvia(ProcessoAnnoCampagnaDomandaDto processoDomanda);
}
