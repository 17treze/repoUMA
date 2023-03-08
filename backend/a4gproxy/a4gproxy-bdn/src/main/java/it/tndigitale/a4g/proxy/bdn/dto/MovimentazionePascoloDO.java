package it.tndigitale.a4g.proxy.bdn.dto;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiOVIINGRESSIPASCOLO.OVIINGRESSIPASCOLO;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiOVIUSCITEPASCOLO.OVIUSCITEPASCOLO;

public class MovimentazionePascoloDO implements Comparable<MovimentazionePascoloDO> {

	public final static String ingressoPascolo = "IP";
	public final static String uscitaPascolo = "UP";

	long idMovi;
	long idAlle;
	String codiAsll;
	String codiFiscAlle;
	String codiSpec;
	String codiFiscDete;
	String codiAslAzie;
	long idPasc;
	String codiPasc;
	String descComuPasc;
	String aslPasc;
	Date dataIngrPasc;
	Date dataUsciPasc;
	long numeOvin;
	Date dataComuAuto;
	String codiDocu;
	Date dataDocu;
	String tipoMovi;
	long numeCamp;
	int flagCont;
	Date dataInse;
	Date dataRevi;
	Date dataFine;

	public MovimentazionePascoloDO(OVIINGRESSIPASCOLO o, String cuaa, int annoCampagna) throws Exception {

		// this.setIdMovi();
		this.setIdAlle(o.getALLEVID() == null ? 0 : o.getALLEVID().longValue());
		this.setCodiAsll(o.getAZIENDACODICE());
		this.setCodiFiscAlle(o.getALLEVIDFISCALE());
		this.setCodiSpec(o.getSPECODICE());
		this.setCodiFiscDete(o.getCUAADETENTORE());
		this.setCodiAslAzie(o.getASLAZIENDA());
		this.setIdPasc(o.getPASID() == null ? 0 : o.getPASID().longValue());
		this.setCodiPasc(o.getCODICEPASCOLO());
		this.setDescComuPasc(o.getCOMUNEPASCOLO());
		this.setAslPasc(o.getASLPASCOLO());
		this.setDataIngrPasc(o.getDTINGRESSOALPASCOLO() == null ? null : toDate(o.getDTINGRESSOALPASCOLO()));
		this.setDataUsciPasc(null); // Record Ingresso pascolo, la data di uscita mai valorizzata
		this.setNumeOvin(o.getNUMOVINI() == null ? 0 : o.getNUMOVINI().longValue());
		this.setDataComuAuto(o.getDTCOMAUTORITA() == null ? null : toDate(o.getDTCOMAUTORITA()));
		this.setCodiDocu(o.getESTREMIMODACCOMPAGNAMENTO());
		this.setDataDocu(o.getDTMODELLO() == null ? null : toDate(o.getDTMODELLO()));
		this.setTipoMovi(MovimentazionePascoloDO.ingressoPascolo);
		this.setNumeCamp(annoCampagna);
		// this.setFlagCont(); //TODO campo non usato, chiedere a Giustino e Navarra per conferma
	}

	public MovimentazionePascoloDO(OVIUSCITEPASCOLO o, String cuaa, int annoCampagna) throws Exception {

		// this.setIdMovi();
		this.setIdAlle(o.getALLEVID() == null ? 0 : o.getALLEVID().longValue());
		this.setCodiAsll(o.getAZIENDACODICE());
		this.setCodiFiscAlle(o.getALLEVIDFISCALE());
		this.setCodiSpec(o.getSPECODICE());
		this.setCodiFiscDete(o.getCUAADETENTORE());
		this.setCodiAslAzie(o.getASLAZIENDA());
		this.setIdPasc(o.getPASID() == null ? 0 : o.getPASID().longValue());
		this.setCodiPasc(o.getCODICEPASCOLO());
		this.setDescComuPasc(o.getCOMUNEPASCOLO());
		this.setAslPasc(o.getASLPASCOLO());
		this.setDataIngrPasc(null);
		this.setDataUsciPasc(o.getDTUSCITADAPASCOLO() == null ? null : toDate(o.getDTUSCITADAPASCOLO()));
		this.setNumeOvin(o.getNUMOVINI() == null ? 0 : o.getNUMOVINI().longValue());
		this.setDataComuAuto(o.getDTCOMAUTORITA() == null ? null : toDate(o.getDTCOMAUTORITA()));
		this.setCodiDocu(o.getESTREMIMODACCOMPAGNAMENTO());
		this.setDataDocu(o.getDTMODELLO() == null ? null : toDate(o.getDTMODELLO()));
		this.setTipoMovi(MovimentazionePascoloDO.uscitaPascolo);
		this.setNumeCamp(annoCampagna);
		// this.setFlagCont(); //TODO campo non usato, chiedere a Giustino e Navarra per conferma
	}

	public MovimentazionePascoloDO() {

	}

	@Override
	public int compareTo(MovimentazionePascoloDO o) {

		if (this.getIdAlle() != o.getIdAlle())
			return (int) (this.getIdAlle() - o.getIdAlle());
		if (this.getCodiAsll().compareTo(o.getCodiAsll()) != 0)
			return this.getCodiAsll().compareTo(o.getCodiAsll());
		if (this.getCodiFiscAlle().compareTo(o.getCodiFiscAlle()) != 0)
			return this.getCodiFiscAlle().compareTo(o.getCodiFiscAlle());
		if (this.getCodiSpec().compareTo(o.getCodiSpec()) != 0)
			return this.getCodiSpec().compareTo(o.getCodiSpec());
		if (this.getCodiAslAzie().compareTo(o.getCodiAslAzie()) != 0)
			return this.getCodiAslAzie().compareTo(o.getCodiAslAzie());
		if (this.getIdPasc() != o.getIdPasc())
			return (int) (this.getIdPasc() - o.getIdPasc());
		if (this.getCodiPasc().compareTo(o.getCodiPasc()) != 0)
			return this.getCodiPasc().compareTo(o.getCodiPasc());
		if (this.getAslPasc().compareTo(o.getAslPasc()) != 0)
			return this.getAslPasc().compareTo(o.getAslPasc());
		if (this.getNumeOvin() != o.getNumeOvin())
			return (int) (this.getNumeOvin() - o.getNumeOvin());

		if ((this.getDataDocu() == null) && (o.getDataDocu() != null))
			return 1;
		if ((this.getDataDocu() != null) && (o.getDataDocu() == null))
			return -1;

		boolean bDataComuAuto = false;
		if ((this.getDataComuAuto() == null) && (o.getDataComuAuto() == null)) {
			bDataComuAuto = true;
		} else {
			if ((this.getDataComuAuto() != null) && (o.getDataComuAuto() != null)) {
				bDataComuAuto = this.getDataComuAuto().compareTo(o.getDataComuAuto()) == 0 ? true : false;
			}
		}

		boolean bCodiDocu = false;
		if ((this.getCodiDocu() == null) && (o.getCodiDocu() == null)) {
			bCodiDocu = true;
		} else {
			if ((this.getCodiDocu() != null) && (o.getCodiDocu() != null)) {
				bCodiDocu = this.getCodiDocu().compareTo(o.getCodiDocu()) == 0 ? true : false;
			}
		}

		boolean bDataDocu = false;
		if ((this.getDataDocu() == null) && (o.getDataDocu() == null)) {
			bDataDocu = true;
		} else {
			if ((this.getDataDocu() != null) && (o.getDataDocu() != null)) {
				bDataDocu = this.getDataDocu().compareTo(o.getDataDocu()) == 0 ? true : false;
			}
		}

		if (bDataComuAuto && bCodiDocu && bDataDocu)
			return 0;

		return -1;
	}

	public long getIdMovi() {
		return idMovi;
	}

	public void setIdMovi(long idMovi) {
		this.idMovi = idMovi;
	}

	public long getIdAlle() {
		return idAlle;
	}

	public void setIdAlle(long idAlle) {
		this.idAlle = idAlle;
	}

	public String getCodiAsll() {
		return codiAsll;
	}

	public void setCodiAsll(String codiAsll) {
		this.codiAsll = codiAsll;
	}

	public String getCodiFiscAlle() {
		return codiFiscAlle;
	}

	public void setCodiFiscAlle(String codiFiscAlle) {
		this.codiFiscAlle = codiFiscAlle;
	}

	public String getCodiSpec() {
		return codiSpec;
	}

	public void setCodiSpec(String codiSpec) {
		this.codiSpec = codiSpec;
	}

	public String getCodiFiscDete() {
		return codiFiscDete;
	}

	public void setCodiFiscDete(String codiFiscDete) {
		this.codiFiscDete = codiFiscDete;
	}

	public String getCodiAslAzie() {
		return codiAslAzie;
	}

	public void setCodiAslAzie(String codiAslAzie) {
		this.codiAslAzie = codiAslAzie;
	}

	public long getIdPasc() {
		return idPasc;
	}

	public void setIdPasc(long idPasc) {
		this.idPasc = idPasc;
	}

	public String getCodiPasc() {
		return codiPasc;
	}

	public void setCodiPasc(String codiPasc) {
		this.codiPasc = codiPasc;
	}

	public String getDescComuPasc() {
		return descComuPasc;
	}

	public void setDescComuPasc(String descComuPasc) {
		this.descComuPasc = descComuPasc;
	}

	public String getAslPasc() {
		return aslPasc;
	}

	public void setAslPasc(String aslPasc) {
		this.aslPasc = aslPasc;
	}

	public Date getDataIngrPasc() {
		return dataIngrPasc;
	}

	public void setDataIngrPasc(Date dataIngrPasc) {
		this.dataIngrPasc = dataIngrPasc;
	}

	public Date getDataUsciPasc() {
		return dataUsciPasc;
	}

	public void setDataUsciPasc(Date dataUsciPasc) {
		this.dataUsciPasc = dataUsciPasc;
	}

	public long getNumeOvin() {
		return numeOvin;
	}

	public void setNumeOvin(long numeOvin) {
		this.numeOvin = numeOvin;
	}

	public Date getDataComuAuto() {
		return dataComuAuto;
	}

	public void setDataComuAuto(Date dataComuAuto) {
		this.dataComuAuto = dataComuAuto;
	}

	public String getCodiDocu() {
		return codiDocu;
	}

	public void setCodiDocu(String codiDocu) {
		this.codiDocu = codiDocu;
	}

	public Date getDataDocu() {
		return dataDocu;
	}

	public void setDataDocu(Date dataDocu) {
		this.dataDocu = dataDocu;
	}

	public String getTipoMovi() {
		return tipoMovi;
	}

	public void setTipoMovi(String tipoMovi) {
		this.tipoMovi = tipoMovi;
	}

	public long getNumeCamp() {
		return numeCamp;
	}

	public void setNumeCamp(long numeCamp) {
		this.numeCamp = numeCamp;
	}

	public int getFlagCont() {
		return flagCont;
	}

	public void setFlagCont(int flagCont) {
		this.flagCont = flagCont;
	}

	public Date getDataInse() {
		return dataInse;
	}

	public void setDataInse(Date dataInse) {
		this.dataInse = dataInse;
	}

	public Date getDataRevi() {
		return dataRevi;
	}

	public void setDataRevi(Date dataRevi) {
		this.dataRevi = dataRevi;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	private static Date toDate(XMLGregorianCalendar calendar) {
		if (calendar == null) {
			return null;
		}
		return calendar.toGregorianCalendar().getTime();
	}

}
