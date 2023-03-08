package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

class DatiLavorazioneControlloIntersostegno {

	private TransizioneIstruttoriaModel transizione;
	private Double importoSostegno;
	private Boolean hasSostegno2;
	private Double importoSostegno2;
	private Boolean hasSostegno3;
	private Double importoSostegno3;
	private StatoIstruttoria statoLavorazioneSostegno2;
	private StatoIstruttoria statoLavorazioneSostegno3;
	
	TransizioneIstruttoriaModel getTransizione() {
		return transizione;
	}
	Double getImportoSostegno() {
		return importoSostegno;
	}
	Boolean getHasSostegno2() {
		return hasSostegno2;
	}
	Double getImportoSostegno2() {
		return importoSostegno2;
	}
	Boolean getHasSostegno3() {
		return hasSostegno3;
	}
	Double getImportoSostegno3() {
		return importoSostegno3;
	}
	StatoIstruttoria getStatoLavorazioneSostegno2() {
		return statoLavorazioneSostegno2;
	}
	StatoIstruttoria getStatoLavorazioneSostegno3() {
		return statoLavorazioneSostegno3;
	}
	void setTransizione(TransizioneIstruttoriaModel transizione) {
		this.transizione = transizione;
	}
	void setImportoSostegno(Double importoSostegno) {
		this.importoSostegno = importoSostegno;
	}
	void setHasSostegno2(Boolean hasSostegno2) {
		this.hasSostegno2 = hasSostegno2;
	}
	void setImportoSostegno2(Double importoSostegno2) {
		this.importoSostegno2 = importoSostegno2;
	}
	void setHasSostegno3(Boolean hasSostegno3) {
		this.hasSostegno3 = hasSostegno3;
	}
	void setImportoSostegno3(Double importoSostegno3) {
		this.importoSostegno3 = importoSostegno3;
	}
	void setStatoLavorazioneSostegno2(StatoIstruttoria statoLavorazioneSostegno2) {
		this.statoLavorazioneSostegno2 = statoLavorazioneSostegno2;
	}
	void setStatoLavorazioneSostegno3(StatoIstruttoria statoLavorazioneSostegno3) {
		this.statoLavorazioneSostegno3 = statoLavorazioneSostegno3;
	}
	
}
