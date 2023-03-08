package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "A4GD_CLASSE_FUNZIONALE")
public class ClasseFunzionaleModel extends TipologiaAbstractModel {

	private static final long serialVersionUID = -6489267374450280098L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TIPOLOGIA")
	private TipologiaModel tipologia;

	@OneToMany(mappedBy = "classefunzionale", fetch = FetchType.LAZY)
	private List<SottotipoModel> sottotipologie;
	
	@OneToMany(mappedBy = "classeFunzionale", fetch = FetchType.LAZY)
	private List<MappingAgsModel> mappingAgs;

	public List<MappingAgsModel> getMappingAgs() {
		return mappingAgs;
	}

	public void setMappingAgs(List<MappingAgsModel> mappingAgs) {
		this.mappingAgs = mappingAgs;
	}

	public TipologiaModel getTipologia() {
		return tipologia;
	}

	public void setTipologia(TipologiaModel tipologia) {
		this.tipologia = tipologia;
	}

	public List<SottotipoModel> getSottotipologieMacchinario() {
		return sottotipologie;
	}

	public TipologiaAbstractModel setSottotipologie(List<SottotipoModel> sottotipologie) {
		this.sottotipologie = sottotipologie;
		return this;
	}
}