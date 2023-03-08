package it.tndigitale.a4g.psr.utente;

import it.tndigitale.a4g.framework.security.model.Ruolo;

public enum Ruoli implements Ruolo {
	
	VISUALIZZA_FASCICOLO(Ruoli.VISUALIZZA_FASCICOLO_COD),
	VISUALIZZA_DOMANDA_PSR(Ruoli.VISUALIZZA_DOMANDA_PSR_COD),
	VISUALIZZA_DOMANDA_DU(Ruoli.VISUALIZZA_DOMANDA_DU_COD),
	MOVIMENTA_DOMANDA_DU(Ruoli.MOVIMENTA_DOMANDA_DU_COD),
	VISUALIZZA_LIQUIDABILITA_DOMANDA_DU(Ruoli.VISUALIZZA_DOMANDA_LIQUIDABILITA_DU_COD),
	VISUALIZZA_PIANICOLTURE(Ruoli.VISUALIZZA_PIANICOLTURE_COD),
	VISUALIZZA_ANAGRAFICA_AZIENDALE(Ruoli.VISUALIZZA_ANAGRAFICA_AZIENDALE_COD),
	VISUALIZZA_DOMANDA_DU_AZD(Ruoli.VISUALIZZA_DOMANDA_DU_AZD_COD),;


	private String codiceRuolo;

	private Ruoli(String codiceRuolo) {
		this.codiceRuolo = codiceRuolo;
	}

	public String getCodiceRuolo() {
		return codiceRuolo;
	}


	public static final String VISUALIZZA_FASCICOLO_COD = "a4g.ags.fascicolo.visualizza";
	public static final String VISUALIZZA_DOMANDA_PSR_COD = "a4g.ags.domanda.psr.visualizza";
	public static final String VISUALIZZA_DOMANDA_DU_COD = "a4g.ags.domanda.du.visualizza";
	public static final String VISUALIZZA_DOMANDA_LIQUIDABILITA_DU_COD = "a4g.ags.domanda.du.liquidabilita.visualizza"; //20.04.2020: usato dal profilo caa
	public static final String MOVIMENTA_DOMANDA_DU_COD = "a4g.ags.domanda.du.movimenta";
	public static final String VISUALIZZA_PIANICOLTURE_COD = "a4g.ags.pianicolture.visualizza";
	public static final String VISUALIZZA_ANAGRAFICA_AZIENDALE_COD = "a4g.ags.fascicolo.anagrafica.visualizza";
	public static final String VISUALIZZA_DOMANDA_DU_AZD_COD = "a4g.ags.domanda.du.azienda.visualizza";  //20.04.2020: usato dal profilo azienda
}
