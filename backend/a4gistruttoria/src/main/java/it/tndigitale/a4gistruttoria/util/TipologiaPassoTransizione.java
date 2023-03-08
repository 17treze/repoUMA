package it.tndigitale.a4gistruttoria.util;

public enum TipologiaPassoTransizione {

	AMMISSIBILITA("Ammissibilita", 1),
	ANOMALIE_MANTENIMENTO("Anomalie mantenimento", 2),
	RIDUZIONI_BPS("Riduzioni BPS", 3),
	SANZIONI_BPS("Sanzioni BPS", 4),
	GREENING("Greening", 5),
	GIOVANE_AGRICOLTORE("Giovane agricoltore", 6),
	RIEPILOGO_SANZIONI("Repilogo sanzioni", 7),
	CONTROLLI_FINALI("Controlli finali", 8),
	LIQUIDABILITA("Liquidabilita", 9),
	CONTROLLO_IMPORTO_MINIMO("Controllo importo minimo liquidabile per il sostegno", 10),
	CONTROLLO_IMPORTO_ANTIMAFIA("Controllo importo antimafia",11),
	DISCIPLINA_FINANZIARIA("Disciplina finanziaria", 12),
	CONTROLLO_INTERSOSTEGNO("Controllo intersostegno", 13),
	CALCOLO_ACS("Calcolo accoppiato superficie", 20),
	CALCOLO_ACZ("Calcolo accoppiato zootecnia", 30);

	String descrizione;
	Integer ordine;

	private TipologiaPassoTransizione(String descrizione, Integer ordine) {
		this.descrizione = descrizione;
		this.ordine = ordine;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public Integer getOrdine() {
		return ordine;
	}
}
