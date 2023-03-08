package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the A4GT_CAMPIONE database table.
 * 
 */
@Entity
@Table(name="A4GT_CAMPIONE")
@NamedQuery(name="CampioneModel.findAll", query="SELECT a FROM CampioneModel a")
public class CampioneModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

	private static final long serialVersionUID = -4785759603632736925L;

	@Column(name="ANNO_CAMPAGNA")
	private Integer annoCampagna;

	@Column(name="CUAA")
	private String cuaa;

	@Column(name="TIPO_CAMPIONE")
	@Enumerated(EnumType.STRING)
	private AmbitoCampione ambitoCampione;

	@Column(name="TIPO")
	@Enumerated(EnumType.STRING)
	private CampioneStatistico campioneStatistico;

	@Column(name="BOVINI")
	private Boolean bovini;

	@Column(name="OVINI")
	private Boolean ovini;

	public Integer getAnnoCampagna() {
		return this.annoCampagna;
	}

	public void setAnnoCampagna(Integer annoCampagna) {
		this.annoCampagna = annoCampagna;
	}

	public String getCuaa() {
		return this.cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public AmbitoCampione getAmbitoCampione() {
		return this.ambitoCampione;
	}

	public void setAmbitoCampione(AmbitoCampione ambitoCampione) {
		this.ambitoCampione = ambitoCampione;
	}

	public CampioneStatistico getCampioneStatistico() {
		return this.campioneStatistico;
	}

	public void setCampioneStatistico(CampioneStatistico campioneStatistico) {
		this.campioneStatistico = campioneStatistico;
	}

	public Boolean getBovini() {
		return this.bovini;
	}

	public void setBovini(Boolean bovini) {
		this.bovini = bovini;
	}

	public Boolean getOvini() {
		return this.ovini;
	}

	public void setOvini(Boolean ovini) {
		this.ovini = ovini;
	}
}