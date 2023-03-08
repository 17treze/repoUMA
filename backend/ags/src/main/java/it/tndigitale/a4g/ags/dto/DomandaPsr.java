package it.tndigitale.a4g.ags.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DomandaPsr implements Serializable{
	

	private static final long serialVersionUID = 3730327764955988788L;
	
	private String codModulo;
	private Integer anno;
	private String cuaa;
	private String ragioneSociale;
	private Long idDomanda;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date dataDomanda;
	private String scoSettore;
	private String statoDomanda;
	private BigDecimal importoRichiesto;
	
	public DomandaPsr(String codModulo, Integer anno, String cuaa, String ragioneSociale, Long idDomanda, Date dataDomanda, String scoSettore, String statoDomanda, BigDecimal importoRichiesto) {
		super();
		this.codModulo = codModulo;
		this.anno = anno;
		this.cuaa = cuaa;
		this.ragioneSociale = ragioneSociale;
		this.idDomanda = idDomanda;
		this.dataDomanda = dataDomanda;
		this.scoSettore = scoSettore;
		this.statoDomanda = statoDomanda;
		this.importoRichiesto = importoRichiesto;
	}
	
	public String getCodModulo() {
		return codModulo;
	}
	public void setCodModulo(String codModulo) {
		this.codModulo = codModulo;
	}
	public Integer getAnno() {
		return anno;
	}
	public void setAnno(Integer anno) {
		this.anno = anno;
	}
	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	public Long getIdDomanda() {
		return idDomanda;
	}
	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}
	public Date getDataDomanda() {
		return dataDomanda;
	}
	public void setDataDomanda(Date dataDomanda) {
		this.dataDomanda = dataDomanda;
	}
	public String getScoSettore() {
		return scoSettore;
	}
	public void setScoSettore(String scoSettore) {
		this.scoSettore = scoSettore;
	}
	public String getStatoDomanda() {
		return statoDomanda;
	}
	public void setStatoDomanda(String statoDomanda) {
		this.statoDomanda = statoDomanda;
	}
	public BigDecimal getImportoRichiesto() {
		return importoRichiesto;
	}
	public void setImportoRichiesto(BigDecimal importoRichiesto) {
		this.importoRichiesto = importoRichiesto;
	}

}
