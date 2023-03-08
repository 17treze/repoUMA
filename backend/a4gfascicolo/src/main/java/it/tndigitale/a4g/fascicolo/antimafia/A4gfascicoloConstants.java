package it.tndigitale.a4g.fascicolo.antimafia;

public final class A4gfascicoloConstants {

	private A4gfascicoloConstants() {
	}

	public static final String DEFAULT_ROLE_PREFIX = "ROLE_";
	// ELENCO_CARICHE_TITOLARE_RAPP_LEGALE: https://docs.google.com/spreadsheets/d/1px9EaUW874epZQqFwvXp_9mHsgZDesJQCtVjq1ZcaWM/edit#gid=0
	public static final String CARICA_RESPONSABILE = "-ACR-ADP-AMD-AMG-AMM-AMP-AMS-APR-ART-ATI-AUN-AUP-CDP-CDT-CLR-CM-CMS-COA-COD-COG-COL-COM-COP-COV-COZ-CRT-CST-CUE-CUF-CUM-IN-LER-LGR-LGT-LI-LIG-LR2-LRF-LRT-LSA-MPP-OAS-OPN-PAD-PCA-PCD-PCE-PCG-PCO-PDC-PDI-PED-PEO-PG-PGD-PGE-PGS-PM-PPP-PRE-PRP-PRQ-PSE-PSS-PTE-RAF-RAP-RAS-RES-RFM-RIN-RIT-RSS-SAO-SAP-SCR-SFC-SLR-SOA-SOL-SOR-SOT-SOU-SPR-TI-TI2-TIT-TTE-TU-UM1-VPC-";
	public static final String IS_RESPONSABILE = "SI";
	public static final String DURATA_ILLIMITATA = "SI";
	public static final String STATO_BOZZA = "BOZZA";
	public static final String STATO_PROTOCOLLATA = "PROTOCOLLATA";
	public static final String DITTA_INDIVIDUALE = "DI";
	public static final String DURATA_ILLIMITATA_AMF = "ILLIMITATA";
	// costanti utenti
	public static final String HEADER_CF = "codicefiscale";
	public static final String HEADER_UPN = "upn";
	// path per chiamate a A4gutente
	public static final String PREFISSO_UTENTE = "utenti";
	public static final String RUOLI_UTENTE = PREFISSO_UTENTE + "/utente/ruoli";
	public static final String ENTI_UTENTE = PREFISSO_UTENTE + "/utente/enti";
	public static final String AZIENDE_UTENTE = PREFISSO_UTENTE + "/utente/aziende";
}