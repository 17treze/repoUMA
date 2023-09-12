package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TAB_AGRI_UMAL_COMUNI")
public class ComuneModel implements Serializable {

	private static final long serialVersionUID = 1543374526522006579L;

	@Column(name="CODI_PROV", nullable = false, length = 3)
	private String codiProv;

	@Column(name="CODI_COMU", nullable = false, length = 3)
	private String codiComu;

	@Column(name="DESC_COMU", nullable = false, length = 100)
	private String descComu;

	@Column(name="CODI_NCAP", nullable = true, length = 5)
	private String codiNcap;

	@Column(name="CODI_COMU_CAPO", nullable = true, length = 3)
	private String codiComuCapo;

	@Id
	@Column(name="CODI_CATA", nullable = true, length = 4)
	private String codiCata;

	public String getCodiProv() {
		return codiProv;
	}
	public void setCodiProv(String codiProv) {
		this.codiProv = codiProv;
	}
	public String getCodiComu() {
		return codiComu;
	}
	public void setCodiComu(String codiComu) {
		this.codiComu = codiComu;
	}
	public String getDescComu() {
		return descComu;
	}
	public void setDescComu(String descComu) {
		this.descComu = descComu;
	}
	public String getCodiNcap() {
		return codiNcap;
	}
	public void setCodiNcap(String codiNcap) {
		this.codiNcap = codiNcap;
	}
	public String getCodiComuCapo() {
		return codiComuCapo;
	}
	public void setCodiComuCapo(String codiComuCapo) {
		this.codiComuCapo = codiComuCapo;
	}
	public String getCodiCata() {
		return codiCata;
	}
	public void setCodiCata(String codiCata) {
		this.codiCata = codiCata;
	}
}
