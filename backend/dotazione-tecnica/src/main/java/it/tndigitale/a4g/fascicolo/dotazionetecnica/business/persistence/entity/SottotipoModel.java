package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "A4GD_SOTTOTIPO")
public class SottotipoModel extends TipologiaAbstractModel {

	private static final long serialVersionUID = -6489267374450280098L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TIPOLOGIA")
	private TipologiaModel tipologia;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CLASSE_FUNZIONALE")
	private ClasseFunzionaleModel classefunzionale;
	
	@OneToMany(mappedBy = "sottotipo", fetch = FetchType.LAZY)
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

	public ClasseFunzionaleModel getClassefunzionale() {
		return classefunzionale;
	}

	public void setClassefunzionale(ClasseFunzionaleModel classefunzionale) {
		this.classefunzionale = classefunzionale;
	}

}
