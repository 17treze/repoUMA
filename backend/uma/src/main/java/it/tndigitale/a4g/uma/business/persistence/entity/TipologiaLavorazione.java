package it.tndigitale.a4g.uma.business.persistence.entity;

public enum TipologiaLavorazione {
	BASE("Base"),
	STRAORDINARIA("Straordinaria"),
	BOVINI_BUFALINI_LATTE_CARNE("Bovini e bufalini da latte/carne (oltre i 6 mesi)"),
	VITELLI("Vitelli (fino a 6 mesi)"),
	SUINI_DA_RIPRODUZIONE("Suini da riproduzione"),
	SUINI_DA_INGRASSO("Suini da ingrasso"),
	SUINI_A_CICLO_CHIUSO("Suini a ciclo chiuso"),
	OVINI_E_CAPRINI("Ovini e caprini"),
	AVICOLI_CUNICOLI_STRUZZI("Avicoli, cunicoli e struzzi"),
	EQUINI_E_UNGULATI("Equini e ungulati"),
	PISCICOLTURA_INTENSIVA("Piscicoltura intensiva"),
	PISCICOLTURA_ESTENSIVA("Piscicoltura estensiva"),
	APICOLTURA("Apicoltura");
	
	private String descrizione;
	
	private TipologiaLavorazione(final String descrizione) {
		this.descrizione = descrizione;
    }
	public String getDescrizione() {
		return descrizione;
	}
	void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
