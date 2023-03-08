package it.tndigitale.a4g.fascicolo.territorio.dto.legacy;

public enum CriterioMantenimento {
	NESSUNA_PRATICA("NESSUNA PRATICA"),
	PRATICA_ORDINARIA("PRATICA ORDINARIA"),
	PASCOLAMENTO_CON_ANIMALI_DI_TERZI("PASCOLAMENTO CON ANIMALI DI TERZI"),
	PASCOLAMENTO_CON_ANIMALI_PROPRI("PASCOLAMENTO CON ANIMALI PROPRI"),
	PASCOLAMENTO_E_SFALCIO("PASCOLAMENTO E SFALCIO"),
	PRATICHE_COLTURALI_VOLTE_AL_MIGLIORAMENTO_DEL_PASCOLO("PRATICHE COLTURALI VOLTE AL MIGLIORAMENTO DEL PASCOLO"),
	SFALCIO_CON_CADENZA_BIENNALE("SFALCIO CON CADENZA BIENNALE"),
	SFALCIO_MANUALE("SFALCIO MANUALE"),
	SFALCIO_MECCANIZZATO("SFALCIO MECCANIZZATO");
	
	private String descrizione;
	
	private CriterioMantenimento(final String descrizione) {
		this.descrizione = descrizione;
    }
	public String getDescrizione() {
		return descrizione;
	}
	void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
