package it.tndigitale.a4g.proxy.bdn.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiCONSISTENZAALLEVAMENTO.CONSISTENZAALLEVAMENTO;

public class ConsistenzaAllevamentoDO implements Comparable<ConsistenzaAllevamentoDO> {

	long idCaws;
	String codiFiscSogg;
	long numeCamp;
	long idAlleBdn;
	String codiSpec;
	String codiAsll;
	String codiFiscProp;
	String codiFiscDete;
	double consCapi06;
	double consCapi624;
	double consCapiOver24;
	double consTota;
	double consVaccOver20;
	Date dataInizDete;
	Date dataFineDete;
	long decoStat;
	Date dataIniz;
	Date dataFine;
	Date dataAggi;
	String userName;
	String codiceComune;

	public ConsistenzaAllevamentoDO(CONSISTENZAALLEVAMENTO o, String cuaa, int annoCampagna) throws Exception {

		DateFormat df = new SimpleDateFormat("mm/dd/yyyy");
		// this.setIdCaws();
		this.setCodiFiscSogg(cuaa);
		this.setNumeCamp(annoCampagna);
		this.setIdAlleBdn(o.getPALLEVID() == null ? 0 : o.getPALLEVID().longValue());
		this.setCodiSpec(o.getSPECODICE());
		this.setCodiAsll(o.getAZIENDACODICE());
		this.setCodiFiscProp(o.getCODFISCALEPROP());
		this.setCodiFiscDete(o.getCODFISCALEDETE());
		this.setConsCapi06(o.getCAPI06MESI() == null ? 0 : Double.parseDouble(o.getCAPI06MESI().replaceAll(",", ".")));
		this.setConsCapi624(o.getCAPI06MESI() == null ? 0 : Double.parseDouble(o.getCAPI624MESI().replaceAll(",", ".")));
		this.setConsCapiOver24(o.getCAPIOLTRE24MESI() == null ? 0 : Double.parseDouble(o.getCAPIOLTRE24MESI().replaceAll(",", ".")));
		this.setConsTota(o.getCONSISTENZAMEDIATOTALE() == null ? 0 : Double.parseDouble(o.getCONSISTENZAMEDIATOTALE().replaceAll(",", ".")));
		this.setConsVaccOver20(o.getVACCHEOLTRE20() == null ? 0 : Double.parseDouble(o.getVACCHEOLTRE20().replaceAll(",", ".")));
		this.setDataInizDete(o.getDTINIZIOPERIODO() == null ? null : df.parse(o.getDTINIZIOPERIODO()));
		this.setDataFineDete(o.getDTINIZIOPERIODO() == null ? null : df.parse(o.getDTINIZIOPERIODO()));
		this.setDecoStat(85);
	}

	public ConsistenzaAllevamentoDO() {
	}

	@Override
	public int compareTo(ConsistenzaAllevamentoDO o) {
		if (this.getNumeCamp() == o.getNumeCamp() && this.getCodiSpec().equals(o.getCodiSpec()) && this.getCodiFiscSogg().equals(o.getCodiFiscSogg()) 
			&& this.getCodiFiscDete().equals(o.getCodiFiscDete()) && this.getCodiFiscProp().equals(o.getCodiFiscProp()) && this.getIdAlleBdn() == o.getIdAlleBdn()
			&& this.getCodiAsll().equals(o.getCodiAsll()) && this.getConsCapi06() == o.getConsCapi06() && this.getConsCapi624() == o.getConsCapi624() 
			&& this.getConsCapiOver24() == o.getConsCapiOver24() && this.getConsVaccOver20() == o.getConsVaccOver20() && this.getConsTota() == o.getConsTota()) {
			return 0;
		}

		return -1;
	}

	public long getIdCaws() {
		return idCaws;
	}

	public void setIdCaws(long idCaws) {
		this.idCaws = idCaws;
	}

	public String getCodiFiscSogg() {
		return codiFiscSogg;
	}

	public void setCodiFiscSogg(String codiFiscSogg) {
		this.codiFiscSogg = codiFiscSogg;
	}

	public long getNumeCamp() {
		return numeCamp;
	}

	public void setNumeCamp(long numeCamp) {
		this.numeCamp = numeCamp;
	}

	public long getIdAlleBdn() {
		return idAlleBdn;
	}

	public void setIdAlleBdn(long idAlleBdn) {
		this.idAlleBdn = idAlleBdn;
	}

	public String getCodiSpec() {
		return codiSpec;
	}

	public void setCodiSpec(String codiSpec) {
		this.codiSpec = codiSpec;
	}

	public String getCodiAsll() {
		return codiAsll;
	}

	public void setCodiAsll(String codiAsll) {
		this.codiAsll = codiAsll;
	}

	public String getCodiFiscProp() {
		return codiFiscProp;
	}

	public void setCodiFiscProp(String codiFiscProp) {
		this.codiFiscProp = codiFiscProp;
	}

	public String getCodiFiscDete() {
		return codiFiscDete;
	}

	public void setCodiFiscDete(String codiFiscDete) {
		this.codiFiscDete = codiFiscDete;
	}

	public double getConsCapi06() {
		return consCapi06;
	}

	public void setConsCapi06(double consCapi06) {
		this.consCapi06 = consCapi06;
	}

	public double getConsCapi624() {
		return consCapi624;
	}

	public void setConsCapi624(double consCapi624) {
		this.consCapi624 = consCapi624;
	}

	public double getConsCapiOver24() {
		return consCapiOver24;
	}

	public void setConsCapiOver24(double consCapiOver24) {
		this.consCapiOver24 = consCapiOver24;
	}

	public double getConsTota() {
		return consTota;
	}

	public void setConsTota(double consTota) {
		this.consTota = consTota;
	}

	public double getConsVaccOver20() {
		return consVaccOver20;
	}

	public void setConsVaccOver20(double consVaccOver20) {
		this.consVaccOver20 = consVaccOver20;
	}

	public Date getDataInizDete() {
		return dataInizDete;
	}

	public void setDataInizDete(Date dataInizDete) {
		this.dataInizDete = dataInizDete;
	}

	public Date getDataFineDete() {
		return dataFineDete;
	}

	public void setDataFineDete(Date dataFineDete) {
		this.dataFineDete = dataFineDete;
	}

	public long getDecoStat() {
		return decoStat;
	}

	public void setDecoStat(long decoStat) {
		this.decoStat = decoStat;
	}

	public Date getDataIniz() {
		return dataIniz;
	}

	public void setDataIniz(Date dataIniz) {
		this.dataIniz = dataIniz;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public Date getDataAggi() {
		return dataAggi;
	}

	public void setDataAggi(Date dataAggi) {
		this.dataAggi = dataAggi;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCodiceComune() {
		return codiceComune;
	}

	public void setCodiceComune(String codiceComune) {
		this.codiceComune = codiceComune;
	}

}
