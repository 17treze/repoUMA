package it.tndigitale.a4g.proxy.ags.bdn.business.persistence.repository;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.izs.bdr.dscapiallev_g.DsCAPIALLEVG;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiANAGRAFICAALLEVAMENTO;

@Repository
public class ZootecniaDao extends JdbcDaoSupport {
	
    @Autowired
    private DataSource dataSource;
    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
	
	private final static String SQL_SEQUENCE_ID_IMPORT = "SELECT SIMP_REGISTRO.NEXTVAL FROM DUAL";

	private final static String SQL_INSERT_ALLEVAMENTO = "INSERT INTO TIMP_REC_ALLEVAMENTO "
			+ "(ID_IMPORT, PROGR_REC, COD_ID_BDN, COD_ID_AZIENDALE, COD_SPECIE, DENOMINAZIONE, ISTATP, ISTATC,"
			+ " INDIRIZZO, CAP, LOCALITA, COMUNE, TIPO_PRODUZIONE, FG_AUT_LATTE, DT_INIZIO_ATTIVITA, DT_FINE_ATTIVITA,"
			+ " COD_FISC_PROPR, DEMOM_PROPR, COD_FISC_DETENT, DENOM_DETENT, DT_INIZIO_DETENT, DT_FINE_DETENT, FG_SOCCIDA,"
			+ " FG_LIBRO_GEN, LATITUDINE, LONGITUDINE, SEZIONE, PARTICELLA, SUBALTERNO, CAPI_TOTALI, DT_CALC_CAPI,"
			+ " ORIENTAMENTO_PRODUTTIVO, FG_MAIN) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private final static String SQL_INSERT_CAPO = "INSERT INTO TIMP_REC_CAPO_ANIMALE "
			+ "(ID_IMPORT, PROGR_REC, COD_ID_REG_STA, COD_ID_CAPO, CODICE, FG_SESSO, DT_NASCITA, COD_MADRE,"
			+ " COD_PADRE, COD_ID_TAG, COD_PRECEDENTE, DT_APPLICAZIONE_MARCHIO, DT_INIZIO_LATTAZIONE,"
			+ " DT_FINE_LATTAZIONE, DT_COMPILAZIONE_CEDOLA, FG_STATO_CAPO, FG_INSEMINAZIONE, FG_PASSAPORTO, FG_ERR_TEMPO,"
			+ " COD_TIPO_ORIGINE, COD_ID_RAZZA, COD_RAZZA, COD_ID_ANIMO, COD_ID_ALLEV, COD_ID_ALLEV_FISCALE, COD_ID_ALLEV_FISCALE_DETEN,"
			+ " COD_ID_STATO, COD_ID_LIBRO_GEN, COD_ID_AZIENDALE, COD_AZIENDA, COD_SPECIE, COD_AZIENDA_ATTUALE, COD_ID_FISCALE_ALLEV_ATTUALE,"
			+ " DENOM_ALLEV_ATTUALE, FG_MOTIVO_INGRESSO, DT_INGRESSO, DT_USCITA, COD_ID_BDN_ALLEV )"
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String FORMAT_DATE = "%4d%02d%02d";

	public void saveAllevamento(ArrayOfRootDatiANAGRAFICAALLEVAMENTO allevamento) {
		if ((allevamento == null) || (allevamento.getANAGRAFICAALLEVAMENTO() == null))
			return;

		Long importSequence = this.getJdbcTemplate().queryForObject(SQL_SEQUENCE_ID_IMPORT, Long.class);
		long idImport = importSequence.longValue();
		long progrRecord = 1;

		for (ArrayOfRootDatiANAGRAFICAALLEVAMENTO.ANAGRAFICAALLEVAMENTO anagrafica : allevamento.getANAGRAFICAALLEVAMENTO()) {

			getJdbcTemplate().update(SQL_INSERT_ALLEVAMENTO,
					new Object[] { idImport, // ID_IMPORT
							progrRecord++, // PROGR_REC
							anagrafica.getALLEVID() == null ? "" : anagrafica.getALLEVID().toPlainString(), // COD_ID_BDN
							anagrafica.getAZIENDACODICE() == null ? "" : anagrafica.getAZIENDACODICE(), // COD_ID_AZIENDALE
							anagrafica.getSPECODICE() == null ? "" : anagrafica.getSPECODICE(), // COD_SPECIE
							anagrafica.getDENOMINAZIONE() == null ? "" : anagrafica.getDENOMINAZIONE(), // DENOMINAZIONE
							"", // ISTATP
							"", // ISTATC
							anagrafica.getINDIRIZZO() == null ? "" : anagrafica.getINDIRIZZO(), // INDIRIZZO
							anagrafica.getCAP() == null ? "" : anagrafica.getCAP(), // CAP
							anagrafica.getLOCALITA() == null ? "" : anagrafica.getLOCALITA(), // LOCALITA
							anagrafica.getCOMUNE() == null ? "" : anagrafica.getCOMUNE(), // COMUNE
							anagrafica.getTIPOPRODUZIONE() == null ? "" : anagrafica.getTIPOPRODUZIONE(), // TIPO_PRODUZIONE
							anagrafica.getAUTORIZZAZIONELATTE() == null ? "" : anagrafica.getAUTORIZZAZIONELATTE(), // FG_AUT_LATTE
							anagrafica.getDTINIZIOATTIVITA() == null ? ""
									: anagrafica.getDTINIZIOATTIVITA().replaceAll("-", ""), // DT_INIZIO_ATTIVITA
							anagrafica.getDTFINEDETENTORE() == null ? ""
									: anagrafica.getDTFINEDETENTORE().replaceAll("-", ""), // DT_FINE_ATTIVITA
							anagrafica.getCODFISCALEPROP() == null ? "" : anagrafica.getCODFISCALEPROP(), // COD_FISC_PROPR
							anagrafica.getDENOMPROPRIETARIO() == null ? "" : anagrafica.getDENOMPROPRIETARIO(), // DEMOM_PROPR
							anagrafica.getCODFISCALEDETEN() == null ? "" : anagrafica.getCODFISCALEDETEN(), // COD_FISC_DETENT
							anagrafica.getDENOMDETENTORE() == null ? "" : anagrafica.getDENOMDETENTORE(), // DENOM_DETENT
							anagrafica.getDTINIZIODETENTORE() == null ? ""
									: anagrafica.getDTINIZIODETENTORE().replaceAll("-", ""), // DT_INIZIO_DETENT
							anagrafica.getDTFINEDETENTORE() == null ? ""
									: anagrafica.getDTFINEDETENTORE().replaceAll("-", ""), // DT_FINE_DETENT
							anagrafica.getSOCCIDA() == null ? "" : anagrafica.getSOCCIDA(), // FG_SOCCIDA
							"", // FG_LIBRO_GEN
							anagrafica.getLATITUDINE() == null ? "" : anagrafica.getLATITUDINE().toPlainString(), // LATITUDINE
							anagrafica.getLONGITUDINE() == null ? "" : anagrafica.getLONGITUDINE().toPlainString(), // LONGITUDINE
							anagrafica.getSEZIONE() == null ? "" : anagrafica.getSEZIONE(), // SEZIONE
							anagrafica.getPARTICELLA() == null ? "" : anagrafica.getPARTICELLA(), // PARTICELLA
							anagrafica.getSUBALTERNO() == null ? "" : anagrafica.getSUBALTERNO(), // SUBALTERNO
							anagrafica.getCAPITOTALI() == null ? "" : anagrafica.getCAPITOTALI().toPlainString(), // CAPI_TOTALI
							anagrafica.getDATACALCOLOCAPI() == null ? ""
									: anagrafica.getDATACALCOLOCAPI().replaceAll("-", ""), // DT_CALC_CAPI
							anagrafica.getORIENTAMENTOPRODUTTIVO() == null ? ""
									: anagrafica.getORIENTAMENTOPRODUTTIVO(), // ORIENTAMENTO_PRODUTTIVO
							"" // FG_MAIN
					});
		}
	}

	public void persistCapiAllevamenti(DsCAPIALLEVG capiBovini) {

		// SE NULL non ci sono capi animali collegati. Esco e non inserisco nulla su TIMP_REC_CAPI_ANIMALI
		if (capiBovini == null)
			return;

		// Inizializzazione di progrRecord ed idImport
		Long importSequence = this.getJdbcTemplate().queryForObject(SQL_SEQUENCE_ID_IMPORT, Long.class);
		long idImport = importSequence.longValue();
		long progrRecord = 1;

		List<DsCAPIALLEVG.CAPIBOVINI> capi = capiBovini.getCAPIBOVINI();

		for (DsCAPIALLEVG.CAPIBOVINI capo : capi) {
			getJdbcTemplate().update(SQL_INSERT_CAPO,
					new Object[] { idImport, // ID_IMPORT
							progrRecord++, // PROGR_REC
							capo.getREGSTAID() == null? "" : capo.getREGSTAID().toPlainString(), // COD_ID_REG_STA
							capo.getCAPOID() == null? "" : capo.getCAPOID().toPlainString(), // COD_ID_CAPO
							capo.getCODICE() == null? "" : capo.getCODICE(), // CODICE
							capo.getSESSO() == null? "" : capo.getSESSO(), // FG_SESSO
							capo.getDTNASCITA() == null? "" : String.format(FORMAT_DATE, capo.getDTNASCITA().getYear(),capo.getDTNASCITA().getMonth(), capo.getDTNASCITA().getDay() ), // DT_NASCITA
							capo.getCODMADRE() == null? "" : capo.getCODMADRE(), // COD_MADRE
							capo.getCODPADRE() == null? "" : capo.getCODPADRE(), // COD_PADRE
							capo.getTAG() == null? "" : capo.getTAG(), // COD_ID_TAG
							capo.getCODPRECEDENTE() == null? "" : capo.getCODPRECEDENTE(), // COD_PRECEDENTE
							capo.getDTAPPLICAZIONEMARCHIO() == null? "" : String.format(FORMAT_DATE, capo.getDTAPPLICAZIONEMARCHIO().getYear(),capo.getDTAPPLICAZIONEMARCHIO().getMonth(), capo.getDTAPPLICAZIONEMARCHIO().getDay() ), // DT_APPLICAZIONE_MARCHIO
							capo.getDTINIZIOLATTAZIONE() == null? "" : String.format(FORMAT_DATE, capo.getDTINIZIOLATTAZIONE().getYear(),capo.getDTINIZIOLATTAZIONE().getMonth(), capo.getDTINIZIOLATTAZIONE().getDay() ), // DT_INIZIO_LATTAZIONE
							capo.getDTFINELATTAZIONE() == null? "" : String.format(FORMAT_DATE, capo.getDTFINELATTAZIONE().getYear(),capo.getDTFINELATTAZIONE().getMonth(), capo.getDTFINELATTAZIONE().getDay() ), // DT_FINE_LATTAZIONE
							capo.getDTCOMPILAZIONECEDOLA() == null? "" : String.format(FORMAT_DATE, capo.getDTCOMPILAZIONECEDOLA().getYear(),capo.getDTCOMPILAZIONECEDOLA().getMonth(), capo.getDTCOMPILAZIONECEDOLA().getDay() ), // DT_COMPILAZIONE_CEDOLA
							capo.getFLAGSTATOCAPO() == null? "": capo.getFLAGSTATOCAPO(), 			// FG_STATO_CAPO
							capo.getFLAGINSEMINAZIONE() == null? "" : capo.getFLAGINSEMINAZIONE(), 	// FG_INSEMINAZIONE
							capo.getFLAGPASSAPORTO() == null? "" : capo.getFLAGPASSAPORTO(), 			// FG_PASSAPORTO
							capo.getFLAGERRTEMPO() == null? "" : capo.getFLAGERRTEMPO(), 				// FG_ERR_TEMPO
							capo.getTIPOORIGINE() == null? "" : capo.getTIPOORIGINE(), 			// COD_TIPO_ORIGINE
							capo.getRAZZAID() == null? "" : capo.getRAZZAID().toPlainString(), // COD_ID_RAZZA
							capo.getRAZZACODICE() == null? "" : capo.getRAZZACODICE(), // COD_RAZZA
							capo.getANIMOID() == null? "" : capo.getANIMOID().toPlainString(), // COD_ID_ANIMO
							capo.getALLEVID() == null? "" : capo.getALLEVID(), // COD_ID_ALLEV
							capo.getALLEVIDFISCALE() == null? "" : capo.getALLEVIDFISCALE(), // COD_ID_ALLEV_FISCALE
							capo.getALLEVIDFISCALEDETEN() == null? "" : capo.getALLEVIDFISCALEDETEN(), // COD_ID_ALLEV_FISCALE_DETEN
							capo.getSTID() == null? "" : capo.getSTID().toPlainString(), // COD_ID_STATO
							"", // COD_ID_LIBRO_GEN
							capo.getAZIENDAID() == null? "" : capo.getAZIENDAID().toPlainString(), // COD_ID_AZIENDALE
							capo.getAZIENDACODICE() == null? "" : capo.getAZIENDACODICE(), // COD_AZIENDA
							capo.getSPECODICE() == null? "" : capo.getSPECODICE(), // COD_SPECIE
							capo.getAZIENDACODICEATTUALE() == null? "" : capo.getAZIENDACODICEATTUALE(), // COD_AZIENDA_ATTUALE
							capo.getALLEVIDFISCALEATTUALE() == null? "" : capo.getALLEVIDFISCALEATTUALE(), // COD_ID_FISCALE_ALLEV_ATTUALE
							capo.getALLEVDENOMINAZIONEATTUALE() == null? "" : capo.getALLEVDENOMINAZIONEATTUALE(), // DENOM_ALLEV_ATTUALE
							capo.getMOTIVOINGRESSO() == null? "" : capo.getMOTIVOINGRESSO(), // FG_MOTIVO_INGRESSO
							capo.getDTINGRESSO() == null? "" : String.format(FORMAT_DATE, capo.getDTINGRESSO().getYear(),capo.getDTINGRESSO().getMonth(), capo.getDTINGRESSO().getDay() ), // DT_INGRESSO
							capo.getDTUSCITA() == null? "" : String.format(FORMAT_DATE, capo.getDTUSCITA().getYear(),capo.getDTUSCITA().getMonth(), capo.getDTUSCITA().getDay() ), // DT_USCITA
							capo.getALLEVID() == null? "" : capo.getALLEVID()// COD_ID_BDN_ALLEV
					});

		}
	}
}
