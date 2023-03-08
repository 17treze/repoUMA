/**
 * 
 */
package it.tndigitale.a4gistruttoria.strategy;

import it.tndigitale.a4gistruttoria.dto.AccoppiatoSuperficie;
import it.tndigitale.a4gistruttoria.dto.ControlliSostegno;
import it.tndigitale.a4gistruttoria.dto.Domanda;

/**
 * Interfaccia da implementare per la strategy relativa alla sezione dei dati di dettaglio domanda.<br>
 * Es. {@link ControlliSostegno}
 * 
 * @author S.Caccia
 *
 */
public interface DatiDomanda {

	/**
	 * Recupera il dettaglio della domanda popolando l'opportuno elemento nell'oggetto {@link AccoppiatoSuperficie}.
	 * 
	 * @param accoppiatoSuperficie
	 */
	public void recupera(Domanda domanda);
}
