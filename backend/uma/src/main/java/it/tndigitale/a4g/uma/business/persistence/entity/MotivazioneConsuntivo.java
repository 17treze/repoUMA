package it.tndigitale.a4g.uma.business.persistence.entity;
/**
 * 
 * Giustifica il caricamento di allegati relativamente ad un consuntivo in dichiarazione consumi.
 * Carburante ammissibile (solo gasolio conto terzi): FURTO, UTILIZZO_IMPROPRIO.
 * Carburante recupero: ASSEGNAZIONE_SVINCOLATA, ALTRO.
 * Per tutti gli altri consuntivi non ci sono allegati, pertanto non ci sono motivazioni.
 * @author B.Conetta
 */
public enum MotivazioneConsuntivo {
	// UMA-03-05-G Allegati Motivo recupero - motivazione allegati campo recupero
	FURTO("Furto"),
	UTILIZZO_IMPROPRIO("Utilizzo Improprio"),
	
	// UMA-03-05-F Allegati Motivo ammesso - motivazione allegati campo ammissibile
	ASSEGNAZIONE_SVINCOLATA("Assegnazione Svincolata"),
	ALTRO("Altro");
	
	public final String label;

    private MotivazioneConsuntivo(String label) {
        this.label = label;
    }
}
