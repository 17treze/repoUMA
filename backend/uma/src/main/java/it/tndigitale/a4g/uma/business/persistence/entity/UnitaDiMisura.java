package it.tndigitale.a4g.uma.business.persistence.entity;

public enum UnitaDiMisura {
	ARNIE("Arnie"),
	KM("Km"),
	UBA("UBA"),
	MC("Mc"),
	T("T"),
	CAPO("Capo"),
	MQ("Mq"),
	HA("Ha"),
	CVH("CVh"),
	QLI("Quintali"),
	QLI_LATTE("Q.li Latte"),
	QLI_OLIVE("Q.li Olive"),
	QLI_ACQUA("Q.li Acqua"),
	QLI_PRODOTTO("Q.li Prodotto"),
	NUM("Num"),
	HL_VINO("Hl vino"),
	HL_ACQUA("Hl acqua");
	
private String descrizione;
	
	private UnitaDiMisura(final String descrizione) {
		this.descrizione = descrizione;
    }
	public String getDescrizione() {
		return descrizione;
	}
	void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
