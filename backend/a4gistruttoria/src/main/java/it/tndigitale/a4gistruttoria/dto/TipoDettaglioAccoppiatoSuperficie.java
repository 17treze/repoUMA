/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

/**
 * @author S.DeLuca
 *
 */
public enum TipoDettaglioAccoppiatoSuperficie {

	SUPERFICI_IMPEGNATE,
	DICHIARAZIONI,
	//INFORMAZIONI_DOMANDA,
	CONTROLLI_SOSTEGNO,
	DATI_DOMANDA,
	DATI_PARTICELLA,
	INSERIMENTO_DATI_ISTRUTTORIA,
	DATI_DISCIPLINA_FINANZIARIA
	
	//TODO - decommentare i valori man mano che si implementano le strategies, altrimenti il test it.tndigitale.a4gistruttoria.DomandeApplicationTest.getDatiASSuccessNoExpand()
	//fallisce (poiché senza expand carica tutte le sesione, ma non trova la strategy andando in NullPointer)
}
