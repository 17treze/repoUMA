package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.AmbitoTipologia;

@Entity
@Table(name = "A4GD_TIPOLOGIA")
public class TipologiaModel extends TipologiaAbstractModel {

	private static final long serialVersionUID = -8567088004005542488L;

	@Column(name = "AMBITO", length = 20)
	@Enumerated(EnumType.STRING)
	private AmbitoTipologia ambito;

	@OneToMany(mappedBy = "tipologia", fetch = FetchType.LAZY)
	private List<SottotipoModel> sottotipologie;

	@OneToMany(mappedBy = "tipologia", fetch = FetchType.LAZY)
	private List<ClasseFunzionaleModel> classiFunzionali;
	
	@OneToMany(mappedBy = "tipologia", fetch = FetchType.LAZY)
	private List<MappingAgsModel> mappingAgs;

	public List<MappingAgsModel> getMappingAgs() {
		return mappingAgs;
	}

	public void setMappingAgs(List<MappingAgsModel> mappingAgs) {
		this.mappingAgs = mappingAgs;
	}

	public List<SottotipoModel> getSottotipologieMacchinario() {
		return sottotipologie;
	}

	public TipologiaAbstractModel setSottotipologie(List<SottotipoModel> sottotipologie) {
		this.sottotipologie = sottotipologie;
		return this;
	}

	public List<ClasseFunzionaleModel> getClassiFunzionaliMacchinario() {
		return classiFunzionali;
	}

	public void setClassiFunzionali(List<ClasseFunzionaleModel> classiFunzionali) {
		this.classiFunzionali = classiFunzionali;
	}

}
