package it.tndigitale.a4gistruttoria.util;

public enum IstruttoriaAntimafiaStatoEnum {
	// TODO: rifattorizza o aggiungi descrizione, aggiungi su db?
	C_01("C_01"),
	DCM_01("DCM_01"),
	DCM_02("DCM_02"),
	DCM_03("DCM_03"),
	DCM_04("DCM_04"),
	DCM_05("DCM_05");
	
	private String stato;

	private IstruttoriaAntimafiaStatoEnum(String stato) {
		this.stato = stato;
	}
	public String getStato() {
		return stato;
	}
}
