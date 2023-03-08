package it.tndigitale.a4g.proxy.bdn.dto;

import java.math.BigDecimal;
import java.util.Date;

public class ConsistenzaAlPascoloPAC2015DO implements Comparable<ConsistenzaAlPascoloPAC2015DO> {

	private Long idCpws;
	private String codiFiscSogg;
	private Long numeCamp;
	private String codiPasc;
	private String fascEtaa;
	private Double numeCapi;
	private Double numeCapiMedi;
	private Long giorPasc;
	private String codiGrupSpec;
	private String codiSpec;
	private String descSpec;
	private Long coorLati;
	private Long coorLong;
	private Long numeFogl;
	private String numePart;
	private String codiSezi;
	private String codiSuba;
	private String codiSiglProv;
	private String codiProv; 	// da valorizzare in fase di insert sul DB usando query "select ISTATP from SITI.CATA_PROV where SIGLA_PROV = ?;", this.getCodiSiglProv()
	private String codiComu;
	private String descLoca;
	private String codiFiscResp;
	private Long decoStat;
	private Date dataIniz;
	private Date dataFine;
	private Date dataAggi;
	private String userName;
	 
	public long getIdCpws() {
		return idCpws;
	}
	public ConsistenzaAlPascoloPAC2015DO setIdCpws(Long idCpws) {
		this.idCpws = idCpws;
		return this;
	}
	public String getCodiFiscSogg() {
		return codiFiscSogg;
	}
	public ConsistenzaAlPascoloPAC2015DO setCodiFiscSogg(String codiFiscSogg) {
		this.codiFiscSogg = codiFiscSogg;
		return this;
	}
	public Long getNumeCamp() {
		return numeCamp;
	}
	public ConsistenzaAlPascoloPAC2015DO setNumeCamp(Long numeCamp) {
		this.numeCamp = numeCamp;
		return this;
	}
	public String getCodiPasc() {
		return codiPasc;
	}
	public ConsistenzaAlPascoloPAC2015DO setCodiPasc(String codiPasc) {
		this.codiPasc = codiPasc;
		return this;
	}
	public String getFascEtaa() {
		return fascEtaa;
	}
	public ConsistenzaAlPascoloPAC2015DO setFascEtaa(String fascEtaa) {
		this.fascEtaa = fascEtaa;
		return this;
	}
	public Double getNumeCapi() {
		return numeCapi;
	}
	public ConsistenzaAlPascoloPAC2015DO setNumeCapi(Double numeCapi) {
		this.numeCapi = numeCapi;
		return this;
	}
	public Double getNumeCapiMedi() {
		return numeCapiMedi;
	}
	public ConsistenzaAlPascoloPAC2015DO setNumeCapiMedi(Double numeCapiMedi) {
		this.numeCapiMedi = numeCapiMedi;
		return this;
	}
	public Long getGiorPasc() {
		return giorPasc;
	}
	public ConsistenzaAlPascoloPAC2015DO setGiorPasc(Long giorPasc) {
		this.giorPasc = giorPasc;
		return this;
	}
	public String getCodiGrupSpec() {
		return codiGrupSpec;
	}
	public ConsistenzaAlPascoloPAC2015DO setCodiGrupSpec(String codiGrupSpec) {
		this.codiGrupSpec = codiGrupSpec;
		return this;
	}
	public String getCodiSpec() {
		return codiSpec;
	}
	public ConsistenzaAlPascoloPAC2015DO setCodiSpec(String codiSpec) {
		this.codiSpec = codiSpec;
		return this;
	}
	public String getDescSpec() {
		return descSpec;
	}
	public ConsistenzaAlPascoloPAC2015DO setDescSpec(String descSpec) {
		this.descSpec = descSpec;
		return this;
	}
	public Long getCoorLati() {
		return coorLati;
	}
	public ConsistenzaAlPascoloPAC2015DO setCoorLati(Long coorLati) {
		this.coorLati = coorLati;
		return this;
	}
	public Long getCoorLong() {
		return coorLong;
	}
	public ConsistenzaAlPascoloPAC2015DO setCoorLong(Long coorLong) {
		this.coorLong = coorLong;
		return this;
	}
	public Long getNumeFogl() {
		return numeFogl;
	}
	public ConsistenzaAlPascoloPAC2015DO setNumeFogl(Long numeFogl) {
		this.numeFogl = numeFogl;
		return this;
	}
	public String getNumePart() {
		return numePart;
	}
	public ConsistenzaAlPascoloPAC2015DO setNumePart(String numePart) {
		this.numePart = numePart;
		return this;
	}
	public String getCodiSezi() {
		return codiSezi;
	}
	public ConsistenzaAlPascoloPAC2015DO setCodiSezi(String codiSezi) {
		this.codiSezi = codiSezi;
		return this;
	}
	public String getCodiSuba() {
		return codiSuba;
	}
	public ConsistenzaAlPascoloPAC2015DO setCodiSuba(String codiSuba) {
		this.codiSuba = codiSuba;
		return this;
	}
	public String getCodiSiglProv() {
		return codiSiglProv;
	}
	public ConsistenzaAlPascoloPAC2015DO setCodiSiglProv(String codiSiglProv) {
		this.codiSiglProv = codiSiglProv;
		return this;
	}
	public String getCodiProv() {
		return codiProv;
	}
	public ConsistenzaAlPascoloPAC2015DO setCodiProv(String codiProv) {
		this.codiProv = codiProv;
		return this;
	}
	public String getCodiComu() {
		return codiComu;
	}
	public ConsistenzaAlPascoloPAC2015DO setCodiComu(String codiComu) {
		this.codiComu = codiComu;
		return this;
	}
	public String getDescLoca() {
		return descLoca;
	}
	public ConsistenzaAlPascoloPAC2015DO setDescLoca(String descLoca) {
		this.descLoca = descLoca;
		return this;
	}
	public String getCodiFiscResp() {
		return codiFiscResp;
	}
	public ConsistenzaAlPascoloPAC2015DO setCodiFiscResp(String codiFiscResp) {
		this.codiFiscResp = codiFiscResp;
		return this;
	}
	public Long getDecoStat() {
		return decoStat;
	}
	public ConsistenzaAlPascoloPAC2015DO setDecoStat(Long decoStat) {
		this.decoStat = decoStat;
		return this;
	}
	public Date getDataIniz() {
		return dataIniz;
	}
	public ConsistenzaAlPascoloPAC2015DO setDataIniz(Date dataIniz) {
		this.dataIniz = dataIniz;
		return this;
	}
	public Date getDataFine() {
		return dataFine;
	}
	public ConsistenzaAlPascoloPAC2015DO setDataFine(Date dataFine) {
		this.dataFine = dataFine;
		return this;
	}
	public Date getDataAggi() {
		return dataAggi;
	}
	public ConsistenzaAlPascoloPAC2015DO setDataAggi(Date dataAggi) {
		this.dataAggi = dataAggi;
		return this;
	}
	public String getUserName() {
		return userName;
	}
	public ConsistenzaAlPascoloPAC2015DO setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	@Override
	public int compareTo(ConsistenzaAlPascoloPAC2015DO o) {

		BigDecimal thisNumeCapi = BigDecimal.valueOf(this.getNumeCapi());
		BigDecimal oNumeCapi = BigDecimal.valueOf(o.getNumeCapi());

		if (thisNumeCapi.compareTo(oNumeCapi) != 0)
			return thisNumeCapi.compareTo(oNumeCapi);
		if (!this.getGiorPasc().equals(o.getGiorPasc()))
			return (int) (this.getGiorPasc() - o.getGiorPasc());
		if (!this.getCoorLong().equals(o.getCoorLong()))
			return (int) (this.getCoorLong() - o.getCoorLong());
		if (!this.getCoorLati().equals(o.getCoorLati()))
			return (int) (this.getCoorLati() - o.getCoorLati());
		if (!this.getNumeFogl().equals(o.getNumeFogl()))
			return (int) (this.getNumeFogl() - o.getNumeFogl());

		if ((this.getNumePart() == null) && (o.getNumePart() != null))
			return -1;
		if ((this.getNumePart() != null) && (o.getNumePart() == null))
			return 1;
		if ((this.getNumePart() != null) && (o.getNumePart() != null)) {
			if (this.getNumePart().compareTo(o.getNumePart()) != 0)
				return this.getNumePart().compareTo(o.getNumePart());
		}

		if ((this.getCodiSezi() == null) && (o.getCodiSezi() != null))
			return -1;
		if ((this.getCodiSezi() != null) && (o.getCodiSezi() == null))
			return 1;
		if ((this.getCodiSezi() != null) && (o.getCodiSezi() != null)) {
			if (this.getCodiSezi().compareTo(o.getCodiSezi()) != 0)
				return this.getCodiSezi().compareTo(o.getCodiSezi());
		}

		if ((this.getCodiSiglProv() == null) && (o.getCodiSiglProv() != null))
			return -1;
		if ((this.getCodiSiglProv() != null) && (o.getCodiSiglProv() == null))
			return 1;
		if ((this.getCodiSiglProv() != null) && (o.getCodiSiglProv() != null)) {
			if (this.getCodiSiglProv().compareTo(o.getCodiSiglProv()) != 0)
				return this.getCodiSiglProv().compareTo(o.getCodiSiglProv());
		}

		if ((this.getCodiComu() == null) && (o.getCodiComu() != null))
			return -1;
		if ((this.getCodiComu() != null) && (o.getCodiComu() == null))
			return 1;
		if ((this.getCodiComu() != null) && (o.getCodiComu() != null)) {
			if (this.getCodiComu().compareTo(o.getCodiComu()) != 0)
				return this.getCodiComu().compareTo(o.getCodiComu());
		}

		if ((this.getDescLoca() == null) && (o.getDescLoca() != null))
			return -1;
		if ((this.getDescLoca() != null) && (o.getDescLoca() == null))
			return 1;
		if ((this.getDescLoca() != null) && (o.getDescLoca() != null)) {
			if (this.getDescLoca().compareTo(o.getDescLoca()) != 0)
				return this.getDescLoca().compareTo(o.getDescLoca());
		}

		if ((this.getCodiFiscResp() == null) && (o.getCodiFiscResp() != null))
			return -1;
		if ((this.getCodiFiscResp() != null) && (o.getCodiFiscResp() == null))
			return 1;
		if ((this.getCodiFiscResp() != null) && (o.getCodiFiscResp() != null)) {
			if (this.getCodiFiscResp().compareTo(o.getCodiFiscResp()) != 0)
				return this.getCodiFiscResp().compareTo(o.getCodiFiscResp());
		}

		return 0;
	}
}
