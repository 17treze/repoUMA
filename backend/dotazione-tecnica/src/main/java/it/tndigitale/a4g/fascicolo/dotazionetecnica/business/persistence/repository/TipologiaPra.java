package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository;

import java.util.Arrays;


public enum TipologiaPra {
	TRATTRICI("TRATTRICI"), 
	MOTOAGRICOLE("MOTOAGRICOLE"), 
	RIMORCHI_SUP15("RIMORCHI SUP. A 15 QLI") ;
	
	private final String tipologia;
	
	TipologiaPra(String tipologia) {
        this.tipologia = tipologia;
    }

	public String getTipologia() {
		return tipologia;
	}
	
    public static TipologiaPra fromDescTipologia(String descrizioneTipologia) {
        return Arrays.asList(TipologiaPra.values())
                .stream()
                .filter(tipo -> tipo.getTipologia().equals(descrizioneTipologia))
                .findFirst()
                .orElse(null);
    }
}