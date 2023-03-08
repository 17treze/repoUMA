package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy;

import java.util.HashMap;
import java.util.Map;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.Carica;

// cfr. PARIX DATI - Dizionario Dati RI_v8.pdf pag. 48
public class CaricheRappresentanteLegaleMap {
	
	private CaricheRappresentanteLegaleMap() {}
	
	private static final Map<String, Carica> CARICHE_RAPPRESENTANTI_LEGALI = populateMap();
	
	private static HashMap<String, Carica> populateMap() {
		HashMap<String, Carica> map = new HashMap<>();
		map.put("OAS", Carica.ACCOMANDATARIO_DI_SAPA);
		map.put("AMM", Carica.AMMINISTRATORE);
		map.put("ACR", Carica.AMMINISTRATORE_CON_REQUISITI);
		map.put("AMD", Carica.AMMINISTRATORE_DELEGATO);
		map.put("ADP", Carica.AMMINISTRATORE_DELEGATO_E_PREPOSTO);
		map.put("APR", Carica.AMMINISTRATORE_E_PREPOSTO);
		map.put("ART", Carica.AMMINISTRATORE_E_RESPONSABILE_TECNICO); 
		map.put("AMG", Carica.AMMINISTRATORE_GIUDIZIARIO);
		map.put("AMP", Carica.AMMINISTRATORE_PROVVISORIO);
		map.put("AMS", Carica.AMMINISTRATORE_STRAORDINARIO);
		map.put("AUN", Carica.AMMINISTRATORE_UNICO);
		map.put("AUP", Carica.AMMINISTRATORE_UNICO_E_PREPOSTO);
		map.put("COA", Carica.COAMMINISTRATORE);
		map.put("COZ", Carica.COMMISSARIO_GIUDIZIALE);
		map.put("COG", Carica.COMMISSARIO_GIUDIZIARIO); 
		map.put("COV", Carica.COMMISSARIO_GOVERNATIVO);
		map.put("COL", Carica.COMMISSARIO_LIQUIDATORE);
		map.put("CM", Carica.COMMISSARIO_MINISTERIALE); 
		map.put("COP", Carica.COMMISSARIO_PREFETTIZIO);
		map.put("CMS", Carica.COMMISSARIO_STRAORDINARIO); 
		map.put("CST", Carica.COMMISSARIO_STRAORDINARIO); 
		map.put("COD", Carica.CONSIGLIERE_DELEGATO);
		map.put("000001", Carica.CONSIGLIERE_DELEGATO_E_PREPOSTO); 
		map.put("CDT", Carica.CONSIGLIERE_DELEGATO_E_RESPONSABILE_TECNICO);
		map.put("CLR", Carica.CONSIGLIERE_E_LEGALE_RAPPRESENTANTE);
		map.put("CRT", Carica.CURATORE);
		map.put("CUE", Carica.CURATORE_DELLO_EMANCIPATO); 
		map.put("CUF", Carica.CURATORE_FALLIMENTARE);
		map.put("CUM", Carica.CURATORE_SPECIALE_DI_MINORE); 
		map.put("IN", Carica.INSTITORE);
		map.put("LER", Carica.LEGALE_RAPPRESENTANTE); 
		map.put("LRF", Carica.LEGALE_RAPPRESENTANTE_FIRMATARIO); 
		map.put("LR2", Carica.LEGALE_RAPPRESENTANTE_ART_2_L_25_8_91_N_287); 
		map.put("LSA", Carica.LEGALE_RAPPRESENTANTE_DI_CUI_ALL_ART_2_DELLA_LEGGE_87_DEL_25_8_1991); 
		map.put("RIN", Carica.LEGALE_RAPPRESENTANTE_DI_INCAPACE);
		map.put("LGR", Carica.LEGALE_RAPPRESENTANTE_DI_SOCIETA);
		map.put("LRT", Carica.LEGALE_RAPPRESENTANTE_E_RESPONSABILE_TECNICO);
		map.put("RFM", Carica.LEGALE_RAPPRESENTANTE_FIGLIO_MINORE);
		map.put("LGT", Carica.LEGALE_RAPPRESENTANTE_INTESTATARIO_DEL_TESSERINO); 
		map.put("LI", Carica.LIQUIDATORE);
		map.put("LIG", Carica.LIQUIDATORE_GIUDIZIARIO); 
		map.put("MPP", Carica.MADRE_ESERCENTE_LA_PATRIA_POTESTA);
		map.put("PPP", Carica.PADRE_ESERCENTE_LA_PATRIA_POTESTA);
		map.put("PM", Carica.PADRE_O_MADRE_ESERCENTE_LA_PATRIA_POTESTA); 
		map.put("PSS", Carica.PREPOSTO_DELLA_SEDE_SECONDARIA);
		map.put("PRE", Carica.PRESIDENTE);
		map.put("PCD", Carica.PRESIDENTE_COMITATO_DIRETTIVO); 
		map.put("PCE", Carica.PRESIDENTE_COMITATO_ESECUTIVO);
		map.put("PCA", Carica.PRESIDENTE_CONSIGLIO_AMMINISTRAZIONE);
		map.put("PGD", Carica.PRESIDENTE_CONSIGLIO_DIRETTIVO);
		map.put("PCO", Carica.PRESIDENTE_CONSORZIO);
		map.put("PCG", Carica.PRESIDENTE_DEL_COMITATO_DI_GESTIONE); 
		map.put("PGS", Carica.PRESIDENTE_DEL_CONSIGLIO_DI_GESTIONE); 
		map.put("OPN", Carica.PRESIDENTE_DI_CONSORZIO);
		map.put("PAD", Carica.PRESIDENTE_E_AMMINISTRATORE_DELEGATO);
		map.put("PDC", Carica.PRESIDENTE_E_CONSIGLIERE_DELEGATO);
		map.put("PEO", Carica.PRESIDENTE_E_PREPOSTO);
		map.put("PTE", Carica.PRESIDENTE_E_RESPONSABILE_TECNICO);
		map.put("PED", Carica.PRESIDENTE_EFFETTIVO_CONSIGLIO_DIRETTIVO);
		map.put("PGE", Carica.PRESIDENTE_GIUNTA_ESECUTIVA);
		map.put("PG", Carica.PROCURATORE_GENERALE);
		map.put("PRP", Carica.PROPRIETARIO);
		map.put("PRQ", Carica.PROPRIETARIO_AUTORIZZATO_A_RISCUOTERE_E_QUIETANZARE);
		map.put("RIT", Carica.RAPPRESENTANTE_IN_ITALIA);
		map.put("RSS", Carica.RAPPRESENTANTE_LEGALE_DELLE_SEDI_SECONDARIE);
		map.put("UM1", Carica.RAPPRESENTANTE_LEGALE_DI_CUI_ALL_ART_2_LEGGE_REG_N_37_DEL_30_8_1988); 
		map.put("RAP", Carica.RAPPRESENTANTE_LEGALE_DI_CUI_ALL_ART_93_DEL_RD_18_6_1931_N_773);
		map.put("PSE", Carica.RAPPRESENTANTE_PREPOSTO_A_SEDE_SECONDARIA_IN_ITALIA);
		map.put("PDI", Carica.RAPPRESENTANTE_PREPOSTO_ALLA_DIPENDENZA_IN_ITALIA);
		map.put("RES", Carica.RAPPRESENTANTE_SOCIETA_ESTERA);
		map.put("RAS", Carica.RAPPRESENTANTE_STABILE);
		map.put("RAF", Carica.RAPPRESENTATE_LEGALE_DI_CUI_ALL_ART_93_DEL_RD_18_6_1931_N_773); 
		map.put("COM", Carica.SOCIO);
		map.put("SOR", Carica.SOCIO_ACCOMANDATARIO); 
		map.put("SAO", Carica.SOCIO_ACCOMANDATARIO_D_OPERA);
		map.put("SAP", Carica.SOCIO_ACCOMANDATARIO_E_PREPOSTO); 
		map.put("SOL", Carica.SOCIO_ACCOMANDATARIO_E_RAPPRESENTANTE_LEGALE);
		map.put("SOA", Carica.SOCIO_AMMINISTRATORE);
		map.put("SFC", Carica.SOCIO_CON_FIRMA_CONGIUNTA); 
		map.put("SOT", Carica.SOCIO_CONTITOLARE);
		map.put("SPR", Carica.SOCIO_DI_SOCIETA_DI_PERSONE_RAPPRES); 
		map.put("SLR", Carica.SOCIO_E_LEGALE_RAPPRESENTANTE);
		map.put("SCR", Carica.SOCIO_RAPPRESENTANTE);
		map.put("SOU", Carica.SOCIO_UNICO);
		map.put("TI", Carica.TITOLARE);
		map.put("ATI", Carica.TITOLARE_DELL_IMPRESA_ARTIGIANA); 
		map.put("TI2", Carica.TITOLARE_DI_CUI_ART_2_LEGGE_25_8_91_N_287); 
		map.put("TTE", Carica.TITOLARE_E_RESPONSABILE_TECNICO);
		map.put("TIT", Carica.TITOLARE_FIRMATARIO);
		map.put("TU", Carica.TUTORE);
		map.put("VPC", Carica.VICE_PRESIDENTE_E_CONSIGLIERE_DELEGATO);
		return map;
	}

	public static Map<String, Carica> getCaricheRappresentantiLegali() {
		return CARICHE_RAPPRESENTANTI_LEGALI;
	}
}
