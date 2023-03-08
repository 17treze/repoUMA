package it.tndigitale.a4gistruttoria.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;

public enum TipologiaStatistica {
	// Intervento BASE o BPS del sostegno Disaccoppiato
	CS21(TipoProcesso.STATISTICA_CS21),

	// Intervento Grening del sostegno Disaccoppiato
	CS22(TipoProcesso.STATISTICA_CS22),

	// Intervento Giovane del sostegno Disaccoppiato
	CS25(TipoProcesso.STATISTICA_CS25),

	// Intervento dei sostegni accoppiati (superficie e zootecnia)
	CS27(TipoProcesso.STATISTICA_CS27);

	private TipologiaStatistica(TipoProcesso elaborazione) {
		this.elaborazione = elaborazione;
	}

	private TipoProcesso elaborazione;

	public TipoProcesso getElaborazione() {
		return elaborazione;
	}
	
	//questa parte è stata creata per deserializzare correttamente l'enum in caso di chiamata dal FE
	//purtroppo non c'era uniformità di informazioni ed è stato preferito creare una mappa che contenesse
	//le giuste accoppiate chiave valore sia per le statistiche che per i relativi processi 
	private static Map<String, TipologiaStatistica> namesMap = new HashMap<String, TipologiaStatistica>();
    static {
        namesMap.put("STATISTICA_CS21", CS21);
        namesMap.put("STATISTICA_CS22", CS22);
        namesMap.put("STATISTICA_CS25", CS25);
        namesMap.put("STATISTICA_CS27", CS27);
        namesMap.put("CS21", CS21);
        namesMap.put("CS22", CS22);
        namesMap.put("CS25", CS25);
        namesMap.put("CS27", CS27);
    }
    
    @JsonCreator
    public static TipologiaStatistica forValue(String value) {
        return namesMap.get(value == null ? null : value.toUpperCase() );
    }
}
