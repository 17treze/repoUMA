package it.tndigitale.a4gistruttoria.util;

public enum TipoControlliSostegno {
	INFO_CALCOLO("Informazioni di Calcolo", 1), ANOMALIE_CALCOLO_PREMIO("Anomalie Calcolo Premio", 2), ANOMALIE_PASCOLI("Anomalie Pascoli", 3), ANOMALIE_LIQUIDABILITA("Anomalie Liquidabilita'", 4);
	private String descrizione;
	private Integer ordine;

	private TipoControlliSostegno(String descrizione, Integer ordine) {
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
